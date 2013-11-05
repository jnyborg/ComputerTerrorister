package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientThread extends Thread {

		private BufferedReader input = null;
		private DataOutputStream output = null;
		private Socket connection;
		private int maxClientsCount; //Maximum allowed clients in the game
		private final ClientThread[] threads;
		private String playerName;	
		private GameHandler gameHandler;
		ClientThread clientThread;
		private boolean isReady;

		public boolean isReady() {
			return isReady;
		}

		public void setReady(boolean isReady) {
			this.isReady = isReady;
		}

		public ClientThread(Socket connection, ClientThread[] threads) {
			this.connection = connection;
			this.threads = threads;
			maxClientsCount = threads.length;
			gameHandler = GameHandler.getInstance();
			clientThread = this;
			isReady = false;
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
							if (threads[i] == clientThread){
								output.writeBytes(gameHandler.getGameData() + "\n");
								isReady = true;
							} else if (threads[i] != null) {
								threads[i].output.writeBytes("new:" + gameHandler.getPlayerToken(playerName)+ "\n");
							}
						}
					} else {
						System.out.println("name is taken yo");
						output.writeBytes(nameToCheck + "\n");
					}
				}
				
				/*
				 * Listen to playermoves.
				 */
				String responseLine;
				while((responseLine = input.readLine()) != null) {
					if(responseLine.startsWith("move:")){
						gameHandler.movePlayer(responseLine.substring(5));
					}else if(responseLine.startsWith("weapon:")) {
						gameHandler.useWeapon(responseLine.substring(7));	
					}else if(responseLine.equals("quit")){
						gameHandler.removePlayer(playerName);
						break;
					}
				}
				
				// Clean up - so new clients can connect to the game.
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
				
				/*
				 * Close output, input and connection(socket).
				 */
				closeConnection();
			}catch (IOException e) {
				//do nothing
			}
		}
		
		public boolean checkPlayerName(String playerName) {
			boolean nameFound = false;	
			for (int i = 0; i < maxClientsCount; i++) {
				if (threads[i] != null && threads[i] != this ) {
					if (threads[i].getPlayerName().equals(playerName)) {
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
		
		public DataOutputStream getOutput(){
			return output;
		}
		
		public void closeConnection(){
			try{
				input.close();
				output.close();
				connection.close();
			}catch (IOException e){
				System.err.println(e);
			}
		}
		
}
