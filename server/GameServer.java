package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	
	private static GameServer instance = null;
	public static GameServer getInstance(){
		if(instance == null){
			instance = new GameServer();
		}
		return instance;
	}

	private static Socket connection;
	private static ServerSocket serverSocket;
	private final static int maxClientsCount = 10;
	private final static ClientThread[] threads = new ClientThread[maxClientsCount];

	public static void main(String[] args) {
		// Default port number
		int portNumber = 2222;
		// if you want to make your own portNumber
		if (args.length < 1) {
			System.out.println("Usage: java MultiThreadChatServer <portNumber>\n" + "Now using port number=" + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}
		// Open server socket on portNumber
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		// Wait for clients to connect
		while (true) {
			try {
				connection = serverSocket.accept();
				System.out.println("accept");
				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new ClientThread(connection, threads)).start();
						break;
					}
				}
				// If the server is full. Max connections is 10.
				if (i == maxClientsCount) {
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					out.writeBytes("Too many people on the server, please try again later \n");
					out.close();
					connection.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
	public void createTreasures(String token) throws IOException{
		for (int i = 0; i < maxClientsCount; i++) {
			threads[i].getOutput().writeBytes("t:" + token);
			
		}
	}
}
