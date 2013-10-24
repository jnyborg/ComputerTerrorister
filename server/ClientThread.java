package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {

		private DataOutputStream output;
		private BufferedReader input;
		private Socket connection;
		private int maxClientsCount;
		private final ClientThread[] threads;
		
		public ClientThread(Socket connection, ClientThread[] threads) {
			this.connection = connection;
			this.threads = threads;
			maxClientsCount = threads.length;
		}
		
		public void run(){
			
		}
}
