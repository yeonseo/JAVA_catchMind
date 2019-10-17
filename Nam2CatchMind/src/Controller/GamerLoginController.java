package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GamerLoginController implements Initializable {
	
	@FXML TextField gamerId;
	@FXML PasswordField gamerPwd;
	@FXML Button btnLogin;
	@FXML Button btnMemberShip;
	@FXML Button btnExit;
	

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//회원가입 버튼
		btnMemberShip.setOnAction(e->{  handlerBtnMemberShipAction(e); });
	}
	/*
	 * btnMemberShip by JM 19.10.16
	 * ->회원가입 버튼을 누르면 회원가입창을 연다!
	 *   
	 */

	public  void handlerBtnMemberShipAction(ActionEvent e) {
		Parent memberShip;
		Stage stage;
		try {
			memberShip=FXMLLoader.load(getClass().getResource("/View/GamerMemberShip.fxml"));
			stage=new Stage();
			Scene scene=new Scene(memberShip);
			stage.setTitle("회원가입");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMemberShip.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
				
		} catch (IOException e1) {
				DBUtil.alertDisplay(1, "오류", "회원가입창을 가져오는데 실패했습니다.", e1.toString());
		}
		
		
	}
	
	
	

}
