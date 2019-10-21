package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.UserStateVO;
import Model.UserVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GamerLoginController implements Initializable {

	@FXML
	TextField gamerId;
	@FXML
	PasswordField gamerPwd;
	@FXML
	Button btnLogin;
	@FXML
	Button btnMemberShip;
	@FXML
	Button btnExit;

	GamerDAO gdao;
	UserVO uvo;
	UserStateVO usvo;
	UserStateDAO usdao;
	
	public  ArrayList<UserVO> list;
	public static String UserId;
	public static int loginTime;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnLogin.setOnAction(e -> {
			handlerBtnLoginAction();
		}); // 로그인 확인
		btnMemberShip.setOnAction(e -> {
			handlerBtnMemberShipAction(e);
		}); // 회원가입 버튼
		btnExit.setOnAction(e -> {
			Platform.exit();
		});
		gamerPwd.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				
				KeyCode keyCode = ke.getCode();

				if (keyCode.equals(KeyCode.ENTER)) {
					handlerBtnLoginAction();
				}
			}

		}); // end of btnLogin
	}

	// 로그인확인
	public void handlerBtnLoginAction() {
		gdao = new GamerDAO();
		list = gdao.getLoginCheck(gamerId.getText(), gamerPwd.getText());
		System.out.println("로그인 리스트 사이즈 : " + list.size());
		if (gamerId.getText().equals("") || gamerPwd.getText().equals("")) {
			AlertDisplay.alertDisplay(1, "아이디 및 패스워드 미입력", "아이디 및 패스워드 미입력", "아이디와 패스워드를 입력해주세요!");
			return;
		}
		if (list.size() != 0) {
			// 로그인 성공부분
			UserId = list.get(0).getUserID();
			System.out.println(UserId);
			// 유저 GameRoom FXML 로드
			Parent gameRoomRoot = null;
			Stage gameRoomStage = null;
			AlertDisplay.alertDisplay(5, "로그인 성공", "로그인 성공!", "로그인 합니다.");
			
			//현재시간 등록	
			gdao=new GamerDAO();
			loginTime=gdao.setCurrentTime(UserId);
			
			try {
	            usvo = new UserStateVO(gamerId.getText(), UserGameState.GAMER_WAITROOM); // DB에 아이디와 상태를 DAO에게!
				usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
				int count = usdao.getUserStateRegistration
						(usvo.getUserID()
						,usvo.getThreadState()); // DAO에 UserID, UserThreadState를 넣어줌!
				if (count != 0) {
					AlertDisplay.alertDisplay(5, "상태등록", "등록성공!", "상태 : "+UserGameState.GAMER_WAITROOM);
					Stage stage = (Stage) btnExit.getScene().getWindow();
					stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!
				} else {
					throw new Exception("데이터베이스 등록실패!");
				}
	            
	            System.out.println("ID : " + gamerId.getText() + "  state : " + UserGameState.GAMER_WAITROOM);
	            
	            //게임대기방 진입
	            gameRoomRoot = FXMLLoader.load(getClass().getResource("/View/GameWaitRoom.fxml"));
				Scene scene = new Scene(gameRoomRoot);
				gameRoomStage = new Stage();
				gameRoomStage.setTitle("게임대기방");
				gameRoomStage.setScene(scene);
				gameRoomStage.setResizable(false);
				((Stage) btnExit.getScene().getWindow()).close();
				gameRoomStage.show();
			} catch (IOException e2) {
				AlertDisplay.alertDisplay(1, "로그인 실패", "게임방을 불러오는데 실패했습니다.", e2.toString());
			}
			catch (Exception e1) {
				AlertDisplay.alertDisplay(1, "상태 등록", "상태등록 실패했습니다.", e1.toString());
			}

		} else {
			AlertDisplay.alertDisplay(1, "로그인 실패", "아이디 및 패스워드 찾을 수 없음.", "아이디와 패스워드를 다시 확인해주세요!");
		}

	}

	/*
	 * btnMemberShip by JM 19.10.16 ->회원가입 버튼을 누르면 회원가입창을 연다!
	 * 
	 */
	// 회원가입창
	public void handlerBtnMemberShipAction(ActionEvent e) {
		Parent memberShip;
		Stage stage;
		try {
			memberShip = FXMLLoader.load(getClass().getResource("/View/GamerMemberShip.fxml"));
			stage = new Stage();
			Scene scene = new Scene(memberShip);
			stage.setTitle("회원가입");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMemberShip.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			AlertDisplay.alertDisplay(1, "오류", "회원가입창을 가져오는데 실패했습니다.", e1.toString());
		}

	}

}


