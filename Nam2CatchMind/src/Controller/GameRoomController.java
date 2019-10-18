package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GameRoomController implements Initializable {
	  
	@FXML Label gameStartTime;
	@FXML Label AllPlayTime;
	@FXML Button btnMakeRoom;
	@FXML Button btnMyRecord;
	@FXML Button btnMyInfoChange;
	@FXML TableView<T> roomInfo;
	@FXML TableView<T> userRanking;
	@FXML TextArea txtChatArea;
	@FXML TextField txtInputMessage;
	@FXML Button btnUserSend;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
