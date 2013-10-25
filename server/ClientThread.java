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
		ClientThread clientThread;

		public ClientThread(Socket connection, ClientThread[] threads) {
			this.connection = connection;
			this.threads = threads;
			maxClientsCount = threads.length;
			gameHandler = new GameHandler();
			clientThread = this;
		}
		
		public void run(){
			int maxClientsCount = this.maxClientsCount;
			ClientThread[] threads = this.threads;
					
			try {
				/*
				 * Ask player to input playerName, and check if the name i available
				 * If name ok, send positions for all currently connected players, and give the new player a position.
				 */
				input = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
				output = new DataOutputStream(connection.getOutputStream());				
				boolean nameOk = false;
				while (!nameOk) {
					output.writeBytes("Please enter your name: \n");
					String nameToCheck = input.readLine().trim();
					if (!checkPlayerName(nameToCheck)) {
						playerName = nameToCheck;
						output.writeBytes("Welcome " + playerName + " to the game.\n");
						nameOk = true;
						gameHandler.addPlayer(playerName);
						
						for (int i = 0; i < maxClientsCount; i++) {
							if(threads[i] == clientThread){
								System.out.println(gameHandler.getAllPlayerTokens());
								output.writeBytes(gameHandler.getAllPlayerTokens() + "\n");
							}else if(threads[i] != null){
								threads[i].output.writeBytes("new:" + gameHandler.getPlayerToken(playerName)+ "\n");
							}
						}
					} else {
						output.writeBytes(nameToCheck + " is already taken! Please try again.\n");
					}
				}
				
				/*
				 * Listen to playermoves.
				 */
				String responseLine;
				while((responseLine = input.readLine()) != null) {
					if(responseLine.substring(0,5).equals("move:")){
						String newPosition = gameHandler.playerMoved(responseLine.substring(5));
						if(newPosition.startsWith("p:")){
							for (int i = 0; i < maxClientsCount; i++) {
								if (threads[i] != null ) {
									threads[i].output.writeBytes(newPosition + "\n");
								}
							}
						}else{
							System.out.println("we heff a problem. Wall hit.");
						}
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
			System.out.println("chek nam");
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
