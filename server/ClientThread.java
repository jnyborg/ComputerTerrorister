package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {

		private BufferedReader input = null;
		private DataOutputStream output = null;
		private Socket connection;
		private int maxClientsCount;
		private final ClientThread[] threads;
		private String playerName;	
		private GameHandler gameHandler;

		public ClientThread(Socket connection, ClientThread[] threads) {
			this.connection = connection;
			this.threads = threads;
			maxClientsCount = threads.length;
			gameHandler = new GameHandler();
		}
		
		public void run(){
			int maxClientsCount = this.maxClientsCount;
			ClientThread[] threads = this.threads;
					
			try {
				/*
				 * Ask player to input playerName, and check if the name i available
				 * Turtles = op ¤¤¤¤¤
				 */
				input = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
				output = new DataOutputStream(connection.getOutputStream());				
				boolean nameOk = false;
				while (!nameOk) {
					output.writeBytes("Enter your name: ");
					String nameToCheck = input.readLine().trim();
					if (!checkPlayerName(nameToCheck)) {
						playerName = nameToCheck;
						output.writeBytes("Welcome " + playerName + "to the game.");
						nameOk = true;
						gameHandler.addPlayer(playerName);
					} else {
						output.writeBytes(nameToCheck + " is already taken! Please try again.");
					}
				}
				
				while(input.readLine() != null) {
					String playerMove = input.readLine();
					String[] moves = playerMove.split(",");
					String pos = gameHandler.playerMoved(playerName, moves[1]); //TODO: Måske moves[3]
					for (int i = 0; i < maxClientsCount; i++) {
						threads[i].output.writeBytes(pos);
					}
				}
								
				
				// Clean up - so new clients can connect to the game
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
				
				/*
				 * Close output, input and connection(socket).
				 */
				input.close();
				output.close();
				connection.close();
				
			} 
			catch (IOException e) {
				//do nothing
			}
		}
		
		public boolean checkPlayerName(String playerName) {
			boolean nameFound = false;	
			for (int i = 0; i < maxClientsCount; i++) {
				if (threads[i] != null && threads[i] != this ) {
					if (threads[i].getPlayerName() == playerName) {
						return nameFound = true;
					}
				}
			}			
			return nameFound;
		}
		
		public String getPlayerName() {
			return playerName;
		}

		public void setPlayerName(String playerName) {
			this.playerName = playerName;
		}
		
}
