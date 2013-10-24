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
		
		public ClientThread(Socket connection, ClientThread[] threads) {
			this.connection = connection;
			this.threads = threads;
			maxClientsCount = threads.length;
		}
		
		public void run(){
			int maxClientsCount = this.maxClientsCount;
			ClientThread[] threads = this.threads;
			
			/*
			 * Create input and output streams for this client.
			 * Turtles = op ¤¤¤¤¤
			 */
			try {
				input = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
				output = new DataOutputStream(connection.getOutputStream());
				output.writeBytes("Enter your name: ");
				String name = input.readLine().trim();
				output.writeBytes("Welcome " + name + "to the game.");
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] != this ) {
						threads[i].output.writeBytes(" A new user " + name + " has joined the game");
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
}
