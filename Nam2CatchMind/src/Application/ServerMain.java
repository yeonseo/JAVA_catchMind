package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Controller.AlertDisplay;
import Controller.Client;
import Controller.UserGameState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.ArrayList;

public class ServerMain{
	
	/* 이것을 실행시켜야 서버가 시작 됨.
	 * 메니저 모드는 없에고 관리자 모드만 실행하기로 함
	 * 
	 * 
	 * */
		private ServerSocket serverSocket;
		private static UserGameState userGameState;

		// constructor
		public ServerMain(int port) throws IOException {
			serverSocket = new ServerSocket(port);
		}

		// thread manager
		public void acceptClients() throws IOException {
			// for keeping track of client number
			int clientNumber = 0;

			// create an open-ended thread pool
			ExecutorService threadPool = Executors.newCachedThreadPool();
			try {
				while (!Thread.currentThread().isInterrupted()) {
					// wait for client to connect
					AlertDisplay.log("Up and listening on port " + serverSocket.getLocalPort() + "...");
					Socket clientSocket = serverSocket.accept();
					AlertDisplay.log("Just connected to " + clientSocket.getRemoteSocketAddress());

					// create new client handler and fork to background thread
					threadPool.submit(new ClientHandler(clientSocket, clientNumber++));
				}
			} finally {
				// shut down thread pool when done
				threadPool.shutdown();
			}
		}

		// method for killing the server
		public void stop() throws IOException {
			serverSocket.close();
		}

		// this class handles each client connection
		private static class ClientHandler implements Runnable {
			private final Socket clientSocket;
			private int clientNumber;

			// constructor
			public ClientHandler(Socket clientSocket, int clientNumber) {
				this.clientSocket = clientSocket;
				this.clientNumber = clientNumber;
				AlertDisplay.log("New connection with client #" + clientNumber + " at " + clientSocket);
			}

			// main brains
			public void run() {
				String clientResponse;

				try {
					// initialize input and output streams
					BufferedReader in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

					// send greeting to client
					out.println("Welcome, you are client #" + clientNumber + ".");

					while (true) {
						// read and parse client response
						clientResponse = in.readLine();
						userGameState.parseOpcode(clientResponse, out);
					}
				} catch (IOException e) {
					AlertDisplay.log("Error handling client #" + clientNumber + ": " + e);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						clientSocket.close();
					} catch (IOException e) {
						AlertDisplay.log("Error closing client connection");
					}
				}
				AlertDisplay.log("Connection with client #" + clientNumber + " has been terminated.");
			}


		}

//		// method for authenticating client
//		public static Boolean authenticate(String username, String pass1) throws Exception {
//			if (MD5(pass1).equals(sqlDB.getPassDB(username))) {
//				return true;
//			}
//			return false;
//		}
//
//		// method for setting online field of authenticated user to 1
//		public static void setOnline(String username) throws Exception {
//			sqlDB.setOnline(username, true);
//		}
//
//		// method for setting online field of authenticated user to offline
//		public static void setOffline(String username) throws Exception {
//			sqlDB.setOnline(username, false);
//		}
//
//		// method for signing up a new user
//		public static Boolean addNewUser(String name, String surname, String username, String password) throws ClassNotFoundException, SQLException {
//			return sqlDB.addUserDB(username, name, surname, password);
//		}
//
//		public static Boolean changePass(String username, String pass) throws ClassNotFoundException, SQLException {
//			return sqlDB.changePassDB(username, pass);
//		}
//
//		// Create MD5 hash for password authentication
//		public static String MD5(String md5) {
//			try {
//				java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
//				byte[] array = md.digest(md5.getBytes());
//				StringBuffer sb = new StringBuffer();
//				for (int i = 0; i < array.length; ++i) {
//					sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
//				}
//				return sb.toString();
//			} catch (java.security.NoSuchAlgorithmException e) {
//			}
//			return null;
//		}

		// parse command line port number and start server
		public static void main(String [] args) {
			int port = 9876;
			try {
				new server(port).acceptClients();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
