package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManagerMain extends Application {
	
	/*server FXML load*/
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/View/ManagerLogin.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("LOGIN");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}	
	public static void main(String[] args) {
		launch(args);
	}
}
