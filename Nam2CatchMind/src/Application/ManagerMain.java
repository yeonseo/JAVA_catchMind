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

public class ManagerMain extends Application {
		
		// parse command line port number and start server
		public static void main(String [] args) {
			launch(args);
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			Parent managerMainRoot=FXMLLoader.load(getClass().getResource("/View/ManagerLogin.fxml"));
			Scene scene=new Scene(managerMainRoot);
			primaryStage.setTitle("로그인");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}
