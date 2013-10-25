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
	private static ScoreList scoreList; //TODO: Implement
	private static String playerName;

	public static void main(String[] args) {
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
						System.out.println("responseline: " + responseLine);
						init();
						screen.drawPlayers(responseLine);	
						System.out.println(responseLine);
						nameOk = true;
					}
				}
				
				/*
				 * Listen to playermoves from server, and draw them.
				 */
				while ((responseLine = input.readLine()) != null) {
					if (responseLine.startsWith("p:")) {
						String[] playerPosition = responseLine.substring(2).split("#");
						screen.movePlayerOnScreen(Integer.parseInt(playerPosition[0]), Integer.parseInt(playerPosition[1]), Integer.parseInt(playerPosition[2]), Integer.parseInt(playerPosition[3]), playerPosition[4]);
					} else if (responseLine.startsWith("new:")) {
						screen.drawPlayers(responseLine.substring(4));
//						scoreList.addPlayer(responseLine);
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
	public void init() {
		screen = new Screen();
		keyClass = new KeyClass(this);
		screen.addKeyListener(keyClass);
		scoreList = new ScoreList();
//		scoreList.addPlayer(player);
//		scoreList.setVisible(true);
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
}


