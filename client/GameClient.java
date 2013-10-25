package client;

import game.KeyClass;
import game.Player;
import game.ScoreList;
import game.Screen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameClient {
	private static DataOutputStream output;
	//input from server
	private static BufferedReader input;
	private static String host;
	private static int port;
	private static Socket clientSocket;
	//input from user
	private static BufferedReader inputLine;
	private static boolean closed;
	private static KeyClass keyClass;
	private static Screen screen;
	private static ScoreList scoreList;
	private static String playerName;

	public static void main(String[] args) {
		
		
		
		
		//default host and port
		host = "localhost";
		port = 2222;	

		/*
		 * Check the args length, so the user can input host + port by the console
		 */		
		if (args.length < 2) {
			System.out.println("Now using host " + host + " and port number " + port);
		} else {
			host = args[0];
			port = Integer.valueOf(args[1]).intValue();
			System.out.println("Now using host " + host + " and port number " + port);
		}

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
					}
				}
			
				//close connection
				clientSocket.close();
				input.close();
				output.close();
			} catch (IOException e) {
				System.err.println("IOException:" + e);
			} finally {
				
			}

		}

	}

	public static void init() {
		screen = new Screen();
		keyClass = new KeyClass();
		screen.addKeyListener(keyClass);
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


