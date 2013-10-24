package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

	private int portNumber;
	private ServerSocket serverSocket;
	private Socket connection;
	private static final int maxClientsCount = 10;
	private static final ClientThread[] threads = new ClientThread[maxClientsCount];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
