package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import draw.AppMainStageScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class Main extends Application {
	Socket socket;
	TextArea textArea;
	
	public void startClient(String IP, int port) {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(IP, port);
					recive();
				} catch (Exception e) {
					e.printStackTrace();
					if(!socket.isClosed()) {
						System.out.println("server accesse fail.");
						stopClient();
						Platform.exit();
					}
					
				}
				
			}
		};
		thread.start();
		
	}
	
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			} 
		}catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public void recive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[512];
				int length = in.read(buffer);
				if(length == -1) throw new IOException();
				String message = new String(buffer, 0, length, "UTF-8");
				Platform.runLater(()->{
					textArea.appendText(message);
				});
			}catch (Exception e) {
				stopClient();
				break;
			}
		}
	}
	
	public void send(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream os = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					os.write(buffer);
					os.flush();
					
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		
		TextField userName = new TextField();
		userName.setPrefWidth(150);
		userName.setPromptText("Enter your name!");
		HBox.setHgrow(userName, Priority.ALWAYS);
		
		TextField IPText = new TextField("localhost");
		TextField portText = new TextField("9876");
		portText.setPrefWidth(80);
		
		hbox.getChildren().addAll(userName, IPText, portText);
		root.setTop(hbox);
		
		textArea = new TextArea();
		textArea.setEditable(false);
		root.setCenter(textArea);
		
		TextField input = new TextField();
		input.setPrefWidth(Double.MAX_VALUE);
		input.setDisable(true);
		
		input.setOnAction(event->{
			send(userName.getText() + " : "+input.getText()+"\n");
			input.setText("");
			input.requestFocus();
		});
		
		Button sendButton = new Button("Send");
		sendButton.setDisable(true);
		
		sendButton.setOnAction(event -> {
			send(userName.getText() + " : "+ input.getText()+"\n");
			input.setText("");
			input.requestFocus();
		});
		
		
		Button connectionButton = new Button("Enter");
		connectionButton.setOnAction(event -> {
			if(connectionButton.getText().equals("Enter")) {
				int port = 9876;
				try {
					port = Integer.parseInt(portText.getText());
				}catch(Exception e) {
					e.printStackTrace();
				}
				startClient(IPText.getText(), port);
				Platform.runLater(()->{
					textArea.appendText("[Chat Start] \n");
				});
				
				connectionButton.setText("Exit");
				input.setDisable(false);
				sendButton.setDisable(false);
				input.requestFocus();
			}
			else {
				stopClient();
				Platform.runLater(()->{
					textArea.appendText("[Chat Out] \n");
				});
				connectionButton.setText("Enter");
				input.setDisable(true);
				sendButton.setDisable(true);
				
			}
		});
		
		BorderPane pane = new BorderPane();
		pane.setLeft(connectionButton);
		pane.setCenter(input);
		pane.setRight(sendButton);
		
		root.setBottom(pane);
		Scene scene = new Scene(root,400,400);
		primaryStage.setTitle("[Chat Client]");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
		
		connectionButton.requestFocus();
		
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
