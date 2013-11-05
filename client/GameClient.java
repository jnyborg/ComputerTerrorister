package client;

import game.KeyClass;
import game.ScoreList;
import game.Screen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient implements Runnable  {
	private static DataOutputStream output;
	//input from server
	private static BufferedReader input;
	private static String host;
	private static int port;
	private static Socket clientSocket;
	//input from user
	private static BufferedReader inputLine;
	private static KeyClass keyClass;
	private static Screen screen;
	private static ScoreList scoreList; 
	private static String playerName;

	public static void main(String[] args) {
//		host = args[0];
		new Thread(new GameClient()).start();
	}

	public void run() {
		host = "localhost"; // The IP you connect to. For debugging purposes: localhost
		port = 2222; // The post the socket listens to.
		/*
		 * Open a socket on a given host and port. Open input and output streams.
		 */
		try {
			clientSocket = new Socket(host, port);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new DataOutputStream(clientSocket.getOutputStream());	      
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to the host "
					+ host);
		}
		
		
		
		
		/*
		 * If everything is setup correct
		 */
		if (clientSocket != null && input != null && output != null) {
			try {
				/*
				 * Setup. Check name with server, and draw players on screen.
				 */
				String responseLine;
				boolean nameOk = false;		
				while(!nameOk){
					System.out.println(responseLine = input.readLine());
					String tempName = inputLine.readLine();
					output.writeBytes(tempName+"\n");
					if((responseLine = input.readLine()).startsWith("Welcome")){
						playerName = tempName;
						//Listen to next input from server, which should be a token with player info
						responseLine = input.readLine();
						init(responseLine);
						nameOk = true;
					}else{
						System.out.println(responseLine + " is already taken.");
					}
				}
				
				/*
				 * Listen to responses from server when name is okay, and take appropriate action
				 */
				while ((responseLine = input.readLine()) != null) {
					//Player changed position
					if (responseLine.startsWith("p:")) {
						String [] playerData;
						if(responseLine.substring(2).startsWith("chest:")){
							playerData = responseLine.substring(8).split("#");
							scoreList.updateScore(playerData[0], playerData[6]);
						}else{
							playerData = responseLine.substring(2).split("#");
						}
						screen.movePlayerOnScreen(Integer.parseInt(playerData[1]), Integer.parseInt(playerData[2]), Integer.parseInt(playerData[3]), Integer.parseInt(playerData[4]), playerData[5]);
					}
					//New player joined
					else if (responseLine.startsWith("new:")) {
						String[] playerData = responseLine.substring(4).split("#");
						screen.drawPlayer(Integer.parseInt(playerData[1]), Integer.parseInt(playerData[2]), playerData[3]);
						scoreList.addPlayer(playerData[0], playerData[4]);
					}
					//Player disconnects
					else if(responseLine.startsWith("disconnected:")){
						responseLine = responseLine.substring(13);
						String[] playerInfo = responseLine.split("#");
						scoreList.removePlayer(playerInfo[0]);
						screen.removePlayer(Integer.parseInt(playerInfo[1]), Integer.parseInt(playerInfo[2]));
					}
					//Treasure spawned
					else if (responseLine.startsWith("t:")) {
						String[] treasureData = responseLine.substring(2).split("#");
						screen.drawTreasure(Integer.parseInt(treasureData[0]), Integer.parseInt(treasureData[1]));
					}
					//Player action
					else if(responseLine.startsWith("action:")){
						String[] actions = responseLine.split("¤");
						for (String s : actions) {
							String action = s.substring(7);
							String actionData[];
							if(action.startsWith("melee:")){
								actionData = action.substring(6).split("#");
								screen.meleeHit(actionData[0], Integer.parseInt(actionData[1]));
								if(actionData[0].length() == 0){
									//player not hit
								}else {
									scoreList.updateScore(actionData[0], actionData[1]);
								}
							}
							else if(action.startsWith("gun:")){
								String data = action.substring(4);
								actionData = data.substring(2).split("#");
								screen.fireGun(actionData[0], Integer.parseInt(actionData[1]), Integer.parseInt(actionData[2]), Integer.parseInt(actionData[3]));
								//if player is hit
								if(data.startsWith("p:")){
									scoreList.updateScore(actionData[4], actionData[5]);
									
								//if chest is hit
								}else if(data.startsWith("c:")){
									screen.putFloor(Integer.parseInt(actionData[4]), Integer.parseInt(actionData[5]));
						
								}

							}
							else if(action.startsWith("mine:")){
								actionData = action.substring(5).split("#");
								screen.placeMine(Integer.parseInt(actionData[0]), Integer.parseInt(actionData[1]));
							}
							else if(action.startsWith("boom:")) {
								actionData = action.substring(5).split("#");
								screen.putFloor(Integer.parseInt(actionData[0]), Integer.parseInt(actionData[1]));
							}
						}
						
					// handles the time, fires once every second
					} else if (responseLine.startsWith("time:")) {
						scoreList.setTime(responseLine.substring(5));
					
					// handles action for a player when he joins, get information about all players, treasures and mines.
					} else if (responseLine.startsWith("newgame:")) {
						responseLine = responseLine.substring(8);
						String[] gameData = responseLine.split("%");
						String[] players = gameData[0].split("¤");
						String[] chests = gameData[1].substring(2).split("¤");
						String[] mines = gameData[2].substring(2).split("¤");
						screen.dispose();
						screen = new Screen();
						keyClass = new KeyClass(this);
						screen.addKeyListener(keyClass);
						for (String p : players) {
							String[] playerData = p.split("#");
							screen.drawPlayer(Integer.parseInt(playerData[1]), Integer.parseInt(playerData[2]), playerData[3]);
							scoreList.updateScore(playerData[0], playerData[4]);
						}
						if (!chests[0].equals("")) {
							for (String c : chests) {
								String[] chestData = c.split("#");
								screen.drawTreasure(Integer.parseInt(chestData[0]), Integer.parseInt(chestData[1]));
							}
						}
						if (!mines[0].equals("")) {
							for (String m : mines) {
								String[] mineData = m.split("#");
								screen.placeMine(Integer.parseInt(mineData[0]), Integer.parseInt(mineData[1]));
							}
						}
					}
					// sets the winner label on the scoreList when a game is over
					else if(responseLine.startsWith("winner:")){
						System.out.println("Winner");
						responseLine = responseLine.substring(7);
						System.out.println(responseLine);
						String[] winnerInfo = responseLine.split("#");
						scoreList.setLastWinner(winnerInfo[0] + " with " + winnerInfo[1] + " points.");
					}
				}
			
				//close connection
				clientSocket.close();
				input.close();
				output.close();
			} catch (IOException e) {
				System.err.println("IOException:" + e);
			}
		}
	}
	/*
	 * Draws the background.
	 */
	public void init(String responseLine) {
		
		screen = new Screen();
		keyClass = new KeyClass(this);
		screen.addKeyListener(keyClass);
		scoreList = new ScoreList();
		scoreList.setVisible(true);
		responseLine = responseLine.substring(8);
		String[] gameData = responseLine.split("%");
		String[] players = gameData[0].split("¤");
		String[] chests = gameData[1].substring(2).split("¤");
		String[] mines = gameData[2].substring(2).split("¤");
		for (String p : players) {
			String[] playerData = p.split("#");
			screen.drawPlayer(Integer.parseInt(playerData[1]), Integer.parseInt(playerData[2]), playerData[3]);
			scoreList.addPlayer(playerData[0], playerData[4]);
		}
		if (!chests[0].equals("")) {
			for (String c : chests) {
				String[] chestData = c.split("#");
				screen.drawTreasure(Integer.parseInt(chestData[0]), Integer.parseInt(chestData[1]));
			}
		}
		if (!mines[0].equals("")) {
			for (String m : mines) {
				String[] mineData = m.split("#");
				screen.placeMine(Integer.parseInt(mineData[0]), Integer.parseInt(mineData[1]));
			}
		}
	}
	
	/**
	 * This method is called by pressing the arrow keys. When pressed, write to server the 
	 * @param direction
	 */
	public static void movePlayer(String direction){
		try{
			output.writeBytes("move:"+playerName + "#" + direction + "\n");
		}catch(IOException e){
			System.err.println(e);
		}
	}
	
	public static void useWeapon() {
		try {
			output.writeBytes("weapon:" + playerName +"\n");
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void disconnect(){
		try{
			output.writeBytes("quit\n");
		}catch(IOException e){
			System.err.println(e);
		}
	}
}


