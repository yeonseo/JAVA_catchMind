package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Application.ServerMain;
import Model.ManagerManagmentVO;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManagerLoginController implements Initializable {
	@FXML
	private TextField managerId;
	@FXML
	private TextField managerPwd;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnMemberShip;
	@FXML
	private Button btnExit;

	ServerMain serverMain;

	GamerDAO gdao;
	UserVO uvo;
	
	UserStateVO usvo;
	UserStateDAO usdao;
	ManagerManagmentDAO mmdao;
	String managerStartTime;
	
	public  ArrayList<UserVO> list;
	public  ArrayList<ManagerManagmentVO> mmlist;
	public static String UserId;
	public static int UserAccess;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setOnAction((event) -> {
			handlerBtnLoginAction();
		});
		btnMemberShip.setOnAction((event) -> {
			handlerBtnMemberShipAction(event);
		});
		btnExit.setOnAction((event) -> {
			Platform.exit();
		});
		
		managerPwd.setOnKeyPressed(new EventHandler<KeyEvent>() {

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
		mmdao = new ManagerManagmentDAO();
		mmlist = mmdao.getLoginCheck(managerId.getText(), managerPwd.getText());
		System.out.println("로그인 리스트 사이즈 : " + mmlist.size());
		if (managerId.getText().equals("") || managerPwd.getText().equals("")) {
			AlertDisplay.alertDisplay(1, "아이디 및 패스워드 미입력", "아이디 및 패스워드 미입력", "아이디와 패스워드를 입력해주세요!");
			return;
		}
		if (mmlist.size() != 0) {
			// 로그인 성공부분
			UserId = mmlist.get(0).getUserID();
			UserAccess = mmlist.get(0).getUserAccess();
			System.out.println(UserAccess);
			// 유저 GameRoom FXML 로드
			Parent gameRoomRoot = null;
			Stage gameRoomStage = null;
			AlertDisplay.alertDisplay(5, "로그인 성공", "로그인 성공!", "로그인 합니다.");
			try {
	            
	            usvo = new UserStateVO(managerId.getText(), UserGameState.MANAGER_ONLINE); // DB에 아이디와 상태를 DAO에게!
				usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
				int count = usdao.getUserStateRegistration(usvo.getUserID(), usvo.getThreadState()); // DAO에 UserStateVO객체를 넣어줌!
				if (count != 0) {
					Stage stage = (Stage) btnExit.getScene().getWindow();
					stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!
				} else {
					throw new Exception("상태 데이터베이스 등록실패!");
				}
				//현재시간 디비에 저장하기
				mmdao=new ManagerManagmentDAO();
				mmdao.setEnterTime(UserId);
				
	            System.out.println("ID : " + managerId.getText() + "  state : " + UserGameState.MANAGER_ONLINE);
	            
	            //로그인 성공, 메인화면으로 진입하
	            try {
	            	gameRoomRoot = FXMLLoader.load(getClass().getResource("/View/ManagerMainTab.fxml"));
					Scene scene = new Scene(gameRoomRoot);
					gameRoomStage = new Stage();
					gameRoomStage.setTitle("관리자");
					gameRoomStage.setScene(scene);
					gameRoomStage.setResizable(false);
					((Stage) btnExit.getScene().getWindow()).close();
					gameRoomStage.show();
					// 로그인 시, 접속 시간 표시하기
					gdao = new GamerDAO();
					String loginTime = gdao.getCurrentTime(managerId.getText());
					String[] loginTimeArray = loginTime.split(",");
					managerStartTime = loginTimeArray[0]+"년"+loginTimeArray[1]+"월"+loginTimeArray[2]+"일  "
											+loginTimeArray[3]+":"+loginTimeArray[4];
					System.out.println("로그인 시간:" + loginTime);
					mmdao.getCurrentTime(managerId.getText());
//					mmdao.getCurrentTime("nnn");
					AlertDisplay.alertDisplay(5, "로그인 성공", usvo.getUserID() + " 관리자님, 안녕하세요."
							, "접속시간은 " + managerStartTime + " 입니다.");
					
	            }catch(Exception fxml) {
	            	AlertDisplay.alertDisplay(1, "fxml 실패", "메니저 메인을 불러오는데 실패했습니다.", fxml.toString());
	            }
	            
			} catch (IOException e2) {
				AlertDisplay.alertDisplay(1, "로그인 실패", "메니저 메인을 불러오는데 실패했습니다.", e2.toString());
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
			memberShip = FXMLLoader.load(getClass().getResource("/View/ManagerMemberShip.fxml"));
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
