package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Application.ServerMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

	ServerClient serverClient;
	ServerMain serverMain;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setOnAction((event) -> {
			handlerBtnLoginAction(event);
		});
		btnMemberShip.setOnAction((event) -> {
			handlerBtnMemberShipAction(event);
		});
		btnExit.setOnAction((event) -> {
			handlerBtnExitAction(event);
		});

	}

	/*
	 * 1. btnLogin by Yan 2019.10.16
	 * 
	 * 10.17 로그인 성공 - main tab 창 열고 서버 start exit가능하게 함.
	 * 
	 * 
	 */
	public void handlerBtnLoginAction(ActionEvent event) {
		System.out.println("login");
		
		if(managerId.getText().equals("") || managerPwd.getText().equals("")) {
			
			AlertDisplay.alertDisplay(1, "Login False", "Not Input Id or Password", "Try again Id , Password");
			
		} else if(managerId.getText().equals("admin") && managerPwd.getText().equals("1234")) {
			Parent mainTabView = null;
			Stage mainStage = null;
			try {
				int port = 9876;
				String host = "localhost";
				startClient(host, port);
				AlertDisplay.alertDisplay(1, "socker", "gogogo", "plzzzzzz");
				
				
				System.out.println("ID : "+managerId.getText()+"  state : "+UserGameState.managerEnter);
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
			
		}else {
			
			AlertDisplay.alertDisplay(1, "Login False", "Not Matched Id or Password", "Try again Id , Password");
			
		}
		/* 
		 * DB UserInfo 데이터와 확인하기
		 * 
		 * try (
		 *  	if(managerId==UserInfo. && managerPwd) 일치하는지확인
		 * 		case 권한==3(admin) -> {
		 * 			로그인액션 성공시 데이터 베이스 저장할 것들 {
		 *				UserTime.EnterTime 기록
		 * 				UserGameState.ThreadState = "UserWaitRoom"
		 * 			}
		 * 			Admin.fxml 실행
		 * 			소켓 실행();
		 * 		}
		 * 			[Check] log(DB.managerId,DB.권한)
		 * 		case 권한==2(관리자) -> {
		 * 			로그인액션 성공시 데이터 베이스 저장할 것들 {
		 *				UserTime.EnterTime 기록
		 * 				UserGameState.ThreadState = "UserWaitRoom"
		 * 			}
		 * 			Manager.fxml 실행
		 * 			소켓 실행();
		 * 		}
		 * 		case 권한==1(예비관리자) -> {
		 * 			관리자 자격 심사중입니다.
		 * 		}
		 * 		case 권한==0(유저) -> {
		 * 			유저시군요???? 잘못오셨지만 안내해 드립니다~
		 * 			;-) " "을 사용해주세요.
		 * 		}
		 * 			[Check] log(DB.managerId,DB.권한)
		 * 	else if(managerId==UserInfo.)
		 * 		비밀번호를 확인하세요.
		 * ) else {
		 * 		가입되지 않은 사용자 입니다.
		 * }
		 *
		 *
		 * 소켓 실행(); 클라이언트의 소켓 통신 정보가 서버로 전달이 되고 스레드가 생성
		 * 
		 * 
		 * 
		 * 
		 * */
		
	}

	/*
	 * 2. btnMemberShip by Yan 2019.10.16 관리자 등록 화면을 보여주는 이벤트처리
	 * 
	 */
	private void handlerBtnMemberShipAction(ActionEvent event) {
		System.out.println("ID : " + managerId.getText() + "  state : " + UserGameState.managerEnter);
		/*
		 * 회원가입 화면을 보여주는 이벤트처리 학생관리MVC 에서 수정버튼 이벤트 참고해서 창 새로 열
		 * 
		 * try {
		 * 
		 * buttonInitSetting(false, true, false, true, false, true, true);
		 * textFieldInitSetting(false, false, false, false, false, false, false, false,
		 * false, false, false, true, true);
		 * 
		 * Parent formEditDialog =
		 * FXMLLoader.load(getClass().getResource("/view/formedit.fxml")); Stage
		 * dialogStage = new Stage(StageStyle.UTILITY);
		 * dialogStage.initModality(Modality.WINDOW_MODAL);
		 * dialogStage.initOwner(btnOk.getScene().getWindow());
		 * dialogStage.setTitle("수정");
		 * 
		 * TextField editName = (TextField) formEditDialog.lookup("#txtName"); TextField
		 * editYear = (TextField) formEditDialog.lookup("#txtYear"); TextField editBan =
		 * (TextField) formEditDialog.lookup("#txtBan"); TextField editGender =
		 * (TextField) formEditDialog.lookup("#txtGender"); TextField editKorean =
		 * (TextField) formEditDialog.lookup("#txtKorean"); TextField editEnglish =
		 * (TextField) formEditDialog.lookup("#txtEnglish"); TextField editMath =
		 * (TextField) formEditDialog.lookup("#txtMath"); TextField editSic =
		 * (TextField) formEditDialog.lookup("#txtSic"); TextField editSoc = (TextField)
		 * formEditDialog.lookup("#txtSoc"); TextField editMusic = (TextField)
		 * formEditDialog.lookup("#txtMusic"); TextField editTotal = (TextField)
		 * formEditDialog.lookup("#txtTotal"); TextField editAvg = (TextField)
		 * formEditDialog.lookup("#txtAvg");
		 * 
		 * editTotal.setDisable(true); editAvg.setDisable(true);
		 * 
		 * editName.setText(selectStudent.get(0).getName());
		 * editYear.setText(selectStudent.get(0).getLevel());
		 * editBan.setText(selectStudent.get(0).getBan());
		 * editGender.setText(selectStudent.get(0).getGender());
		 * editKorean.setText(String.valueOf(selectStudent.get(0).getKorean()));
		 * editEnglish.setText(String.valueOf(selectStudent.get(0).getEnglish()));
		 * editMath.setText(String.valueOf(selectStudent.get(0).getMath()));
		 * editSic.setText(String.valueOf(selectStudent.get(0).getSic()));
		 * editSoc.setText(String.valueOf(selectStudent.get(0).getSoc()));
		 * editMusic.setText(String.valueOf(selectStudent.get(0).getMusic()));
		 * editTotal.setText(String.valueOf(selectStudent.get(0).getTotal()));
		 * editAvg.setText(String.valueOf(selectStudent.get(0).getAvg()));
		 * 
		 * Button btnCal = (Button) formEditDialog.lookup("#btnCal"); Button btnFormAdd
		 * = (Button) formEditDialog.lookup("#btnFormAdd"); Button btnFormCancel =
		 * (Button) formEditDialog.lookup("#btnFormCancel");
		 * 
		 * btnCal.setOnAction(e1 -> {
		 * 
		 * int koreanScore = Integer.parseInt(editKorean.getText()); int englishScore =
		 * Integer.parseInt(editEnglish.getText()); int mathScore =
		 * Integer.parseInt(editMath.getText()); int sicScore =
		 * Integer.parseInt(editSic.getText()); int socScore =
		 * Integer.parseInt(editSoc.getText()); int musicScore =
		 * Integer.parseInt(editMusic.getText());
		 * 
		 * int sum = koreanScore + englishScore + mathScore + sicScore + socScore +
		 * musicScore;
		 * 
		 * editTotal.setText(String.valueOf(sum)); editAvg.setText(String.valueOf(sum /
		 * 6.0));
		 * 
		 * });
		 * 
		 * btnFormAdd.setOnAction(e2 -> {
		 * 
		 * try {
		 * 
		 * if (editTotal.getText().equals("") || editAvg.getText().equals("")) {
		 * 
		 * throw new Exception();
		 * 
		 * } else {
		 * 
		 * StudentVO svo = new StudentVO(selectStudent.get(0).getNo(),
		 * editName.getText(), editYear.getText(), editBan.getText(),
		 * editGender.getText(), Integer.parseInt(editKorean.getText().trim()),
		 * Integer.parseInt(editEnglish.getText().trim()),
		 * Integer.parseInt(editMath.getText().trim()),
		 * Integer.parseInt(editSic.getText().trim()),
		 * Integer.parseInt(editSoc.getText().trim()),
		 * Integer.parseInt(editMusic.getText().trim()),
		 * Integer.parseInt(editTotal.getText().trim()),
		 * Double.parseDouble(editAvg.getText().trim()));
		 * 
		 * StudentDAO studentDAO = new StudentDAO(); StudentVO studentVO =
		 * studentDAO.getStudentUpdate(svo, selectStudent.get(0).getNo());
		 * 
		 * if (editDelete == true && studentVO != null) {
		 * 
		 * studentData.remove(selectStudentIndex); studentData.add(selectStudentIndex,
		 * svo); editDelete = false;
		 * 
		 * } else {
		 * 
		 * throw new Exception("수정 등록 오류");
		 * 
		 * } // end of if & else
		 * 
		 * buttonInitSetting(false, true, false, true, false, true, true);
		 * 
		 * } // end of if & else
		 * 
		 * } catch (Exception e3) {
		 * 
		 * e3.printStackTrace(); // alertWarningDisplay(1, "등록 실패", "합계나 평균을 확인 바랍니다.",
		 * e.toString()); } // end of try & catch });
		 * 
		 * btnFormCancel.setOnAction(e4 -> {
		 * 
		 * dialogStage.close();
		 * 
		 * });
		 * 
		 * Scene scene = new Scene(formEditDialog); dialogStage.setScene(scene);
		 * dialogStage.setResizable(false); dialogStage.show();
		 * 
		 * } catch (IOException e) {
		 * 
		 * e.printStackTrace();
		 * 
		 * } // end of try & catch
		 * 
		 * buttonInitSetting(false, true, false, true, false, true, true);
		 * 
		 * textFieldInitSetting(false, false, false, false, false, false, false, false,
		 * false, false, false, true, true);
		 * 
		 * txtName.setText(""); cbYear.setValue(""); txtBan.setText("");
		 * rbMale.setSelected(false); rbFemale.setSelected(false); txtKo.setText("");
		 * txtEng.setText(""); txtMath.setText(""); txtSic.setText("");
		 * txtSoc.setText(""); txtMusic.setText(""); txtTotal.setText("");
		 * txtAvg.setText("");
		 *
		 * 
		 */

	}

	/*
	 * 3. btnExit by Yan 2019.10.16
	 * 
	 * 
	 */
	private void handlerBtnExitAction(ActionEvent event) {
		System.out.println("out");
		Platform.exit();
		/*
		 * 
		 * 데이터 베이스에서 삭제 -> 메세지 나가면 스레드 종료 -> 서버에서 확인 FXML 종료가 이루어져야 함
		 * 
		 */

	}
	
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

}
