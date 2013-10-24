package client;

import game.KeyClass;
import game.Player;
import game.Screen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameClient implements Runnable {
	private static DataOutputStream output;
	//input from server
	private static BufferedReader input;
	private static String host;
	private static int port;
	private static Socket clientSocket;
	//input from user
	private static BufferedReader inputLine;
	private static boolean closed;
	private KeyClass keyClass;
	private Player player;
	private ArrayList<Player> otherPlayers = new ArrayList<Player>();
	private Screen screen = new Screen();

	public static void main(String[] args) {
		//default host and port
		host = "localhost";
		port = 3000;	

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

		if (clientSocket != null && input != null && output != null) {


			try {

				new Thread(new GameClient()).start();
				while(!closed) {
				
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

	public void run() {
		String responseLine;
		try {
			while ((responseLine = input.readLine()) != null) {
				
				if(responseLine.startsWith("p:")){
					String[] info = responseLine.split(",");
					String playerName = info[0].substring(3);
					int xPos = Integer.parseInt(info[1]);
					int yPos = Integer.parseInt(info[2]);
					String direction = info[3];
					
					if(playerName == player.getName()){
						screen.movePlayerOnScreen(player.getXpos(), player.getYpos(), xPos, yPos, direction);
					} else {
						boolean found = false;
						Player p = null;
						for (Player p1 : otherPlayers) {
							if (p1.getName().equals(playerName)){
								found = true;
								p = p1;
								break;
							}
						}
						if(found) {
							screen.movePlayerOnScreen(p.getXpos(), p.getYpos(), xPos, yPos, direction);
						} else {
							Player newPlayer = new Player(playerName);
							newPlayer.setDirection(direction);
							newPlayer.setXpos(xPos);
							newPlayer.setYpos(yPos);
						}
					}
							
				}
				
				
				
				
//				System.out.println(responseLine);
//				if (responseLine.equals("her er dit navn")) {
//					player = new Player(responseLine.substring(10));
//				} else if (responseLine.equals("position, playername")) {
//					//game.Screen screen = new game.Screen();
//					
//				}
//					break;

			}
			
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
	
	public void movePlayer(String direction){
		try{
			output.writeBytes(player.getName() + "," + direction );
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}


