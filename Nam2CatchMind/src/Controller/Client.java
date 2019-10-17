package Controller;

import java.net.*;
import java.io.*;

public class Client {
	private Socket clientSocket;
	private BufferedReader in;
    private PrintWriter out;
    
    // constructs the client by connecting to a server
    public Client(String hostname, int port) throws IOException {
    	// establish connection with the server
		log("Connecting to " + hostname + " on port " + port);
		clientSocket = new Socket(hostname, port);
		log("Connection with " + clientSocket.getRemoteSocketAddress() + " has been established.");
		
		// initialize input and output streams
		in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true); 
    }
    
	// method for killing the client connection
	public void stop() throws IOException {
		clientSocket.close();
	} 
      
    // receive message from server
    public String receiveMsg() {
    	String serverResponse = null;
    	try {
			serverResponse = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return serverResponse;
    }
    
    // send response to server
    public void sendMsg(String clientResponse) {
    	out.println(clientResponse);
    	out.flush();
    }
		
	// for logging to server console
    private static void log(String message) {
        System.out.println(message);
    }
}
