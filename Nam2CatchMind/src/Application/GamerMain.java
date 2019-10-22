package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GamerMain extends Application {
	public static void main(String[] args) {
		launch(args);
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent gameMainRoot=FXMLLoader.load(getClass().getResource("/View/GamerLogin.fxml"));
		Scene scene=new Scene(gameMainRoot);
		primaryStage.setTitle("로그인");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


}
