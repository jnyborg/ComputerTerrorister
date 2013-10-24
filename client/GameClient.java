package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient {
	private static DataOutputStream output;
	//input from server
	private static BufferedReader input;
	private static String host;
	private static int port;
	private static Socket clientSocket;
	//input from user
	private static BufferedReader inputLine;
	
	public static void main(String[] args) {
		//default host and port
		host = "localhost";
		port = 90210;	
		
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
	    		//write code here to run
	    		
	    		//close connection
	    		clientSocket.close();
				input.close();
		    	output.close();
			} catch (IOException e) {
				System.err.println("IOException:" + e);
			}
	    	
	    }

	}

}
