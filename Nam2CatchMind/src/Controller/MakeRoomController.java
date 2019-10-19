package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MakeRoomController implements Initializable {
	
	@FXML TextField txtMakeRoomName;
	@FXML Button btnMakeRoom;
	@FXML Button btnCancel;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnCancel.setOnAction(e->{
			
		});

	}

}
