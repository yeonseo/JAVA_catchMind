package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.UserVO;
import javafx.application.Platform;
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
	ArrayList<UserVO> list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnLogin.setOnAction(e -> {
			handlerBtnLoginAction(e);
		}); // 로그인 확인
		btnMemberShip.setOnAction(e -> {
			handlerBtnMemberShipAction(e);
		}); // 회원가입 버튼
		btnExit.setOnAction(e -> {
			Platform.exit();
		});
	}

	// 로그인확인
	public void handlerBtnLoginAction(ActionEvent e) {
		gdao = new GamerDAO();
		list = gdao.getLoginCheck(gamerId.getText(), gamerPwd.getText());
		System.out.println("로그인 리스트 사이즈 : " + list.size());
		if (gamerId.getText().equals("") || gamerPwd.getText().equals("")) {
			AlertDisplay.alertDisplay(1, "아이디 및 패스워드 미입력", "아이디 및 패스워드 미입력", "아이디와 패스워드를 입력해주세요!");
			return;
		}
		if (list.size() != 0) {
			
			/***여기까지이이 추가해요!***/
			Parent mainTabView = null;
			Stage mainStage = null;

			AlertDisplay.alertDisplay(5, "로그인 성공", "로그인 성공!", "로그인이 되었습니다.");

			try {
				int port = 9876;
				String host = "localhost";
				startClient(host, port);
				AlertDisplay.alertDisplay(1, "서버에 접속합니다아아아", "데이터베이스 테스트도 통과하길!!!!", "plzzzzzz");

				System.out.println("ID : " + gamerId.getText() + "  state : " + UserGameState.managerEnter);
				mainTabView = FXMLLoader.load(getClass().getResource("/View/ManagerMainTap.fxml"));
				Scene scene = new Scene(mainTabView);
				mainStage = new Stage();
				mainStage.setTitle("ManagerMaintain");
				mainStage.setScene(scene);
				mainStage.setResizable(true);

				((Stage) btnExit.getScene().getWindow()).close();

				mainStage.show();

			} catch (IOException e1) {
				AlertDisplay.alertDisplay(1, "Main Window Error", "Main Window Load False", e1.toString());
			}
			/***여기까지이이 추가해요!***/
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

	private void handlerBtnExitAction(ActionEvent event) {
		System.out.println("out");
		Platform.exit();
		/*
		 * 
		 * 데이터 베이스에서 삭제 -> 메세지 나가면 스레드 종료 -> 서버에서 확인 FXML 종료가 이루어져야 함
		 * 
		 */

	}

	
	
	
	/***여길추가해요!***/
	Socket socket;

	public void startClient(String IP, int port) {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(IP, port);
					recive();
				} catch (Exception e) {
					e.printStackTrace();
					if (!socket.isClosed()) {
						System.out.println("server accesse fail.");
						stopClient();
						Platform.exit();
					}

				}

			}

		};
		thread.start();

	}

	public void recive() {
		while (true) {

			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[512];
				int length = in.read(buffer);
				if (length == -1)
					throw new IOException();
				String message = new String(buffer, 0, length, "UTF-8");

			} catch (Exception e) {
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

	public void stopClient() {
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/***여기까지이이 추가해요!***/
}