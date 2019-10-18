package Application;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.ArrayList;

public class ServerMain extends Application {
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();
	ServerSocket serverSocket;

	// Client submit threadPool(ExecutorService)
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));

		} catch (Exception e) {
			e.printStackTrace();
			if (!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		} // end of try/catch

		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[acsses client] " + socket.getRemoteSocketAddress() + " : "
								+ Thread.currentThread().getName());
					} catch (Exception e) {
						e.printStackTrace();
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					} // end of if/else
				} // end of while
			}// end of run()
		};// end of thread
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}// end of startServer

	public void stopServer() {
		try {
			Iterator<Client> itr = clients.iterator();
			while (itr.hasNext()) {
				Client client = itr.next();
				client.getSocket().close();
				itr.remove();
			}
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if (threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {

		}
	}

	// server UI
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));

		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", 15));
		root.setCenter(textArea);

		Button toggleButton = new Button("start");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);

		String IP = "localhost";
		int port = 9876;

		toggleButton.setOnAction(event -> {
			if (toggleButton.getText().equals("start")) {
				startServer(IP, port);
				Platform.runLater(() -> {
					String message = String.format("server start \n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("exit");
				});
			} else {
				stopServer();
				Platform.runLater(() -> {
					String message = String.format("server exit \n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("start");
				});// end of Platform.runLater
			} // end of if/else
		});// end of toggleButton
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("Chat server");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();

	}// end of start

	public static void main(String[] args) {
		launch(args);
	}

////		// method for authenticating client
////		public static Boolean authenticate(String username, String pass1) throws Exception {
////			if (MD5(pass1).equals(sqlDB.getPassDB(username))) {
////				return true;
////			}
////			return false;
////		}
////
////		// method for setting online field of authenticated user to 1
////		public static void setOnline(String username) throws Exception {
////			sqlDB.setOnline(username, true);
////		}
////
////		// method for setting online field of authenticated user to offline
////		public static void setOffline(String username) throws Exception {
////			sqlDB.setOnline(username, false);
////		}
////
////		// method for signing up a new user
////		public static Boolean addNewUser(String name, String surname, String username, String password) throws ClassNotFoundException, SQLException {
////			return sqlDB.addUserDB(username, name, surname, password);
////		}
////
////		public static Boolean changePass(String username, String pass) throws ClassNotFoundException, SQLException {
////			return sqlDB.changePassDB(username, pass);
////		}
////
////		// Create MD5 hash for password authentication
////		public static String MD5(String md5) {
////			try {
////				java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
////				byte[] array = md.digest(md5.getBytes());
////				StringBuffer sb = new StringBuffer();
////				for (int i = 0; i < array.length; ++i) {
////					sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
////				}
////				return sb.toString();
////			} catch (java.security.NoSuchAlgorithmException e) {
////			}
////			return null;
////		}

}
