package Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import Model.UserRankingVO;
import Model.UserStateVO;
import Model.UserVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameWaitRoomController implements Initializable {

	@FXML
	Label gameStartTime;
	@FXML
	Label gameUserId;
	@FXML
	ImageView gamerImg;
	@FXML
	Button btnMakeRoom;
	@FXML
	Button btnMyRecord;
	@FXML
	Button btnMyInfoChange;
	@FXML
	Button btnUserSend;
	@FXML
	Button btnGameRoomExit;

//	@FXML TableView<UserGameRoomVO> roomInfo;
	@FXML
	TableView<UserRankingVO> userRanking;
	@FXML
	TextArea txtChatArea;
	@FXML
	TextField txtInputMessage;

	String userId;
	String userState = UserGameState.GAMER_WAITROOM;
	Image userImg;
	String GameWaitRoomFileName;

	GamerDAO gdao;
	UserVO uvo;
	ArrayList<UserVO> userIdAndImagList;

	private File selectedFile = null;
	private String localUrl = "";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnUserSend.setDisable(false);
		// 없애보기
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtChatArea.appendText("[Chat Start] \n");
		});
		btnUserSend.setOnAction(event -> {
			// 메세지 전송시자신의 상태외 아이디, 메세지를 함께 보냄
			send(userState + "," + GamerLoginController.UserId + "," + txtInputMessage.getText() + "\n");
			txtInputMessage.setText("");
			txtInputMessage.requestFocus();
		});

		// 로그인된 게임대기방 이미지와 아이디 보이기
		getUserIDandUserImage();

		// 시간등록하기
		int Time = GamerLoginController.loginTime;
		if (Time != 0) {
			gdao = new GamerDAO();
			String loginTime = gdao.getCurrentTime(userId);
			gameStartTime.setText(loginTime);
			System.out.println("로그인 시간:" + loginTime);
		} else {
			System.out.println("시간 등록안됨");
		}

		// 내정보수정하기
		btnMyInfoChange.setOnAction(e -> {
			((Stage) btnGameRoomExit.getScene().getWindow()).close();
			handlerBtnMyInfoChangeAtion(e);
		});

		// 나의 전적
		btnMyRecord.setOnAction(e -> {
		});

		// 게임방만들기
		btnMakeRoom.setOnAction(e -> {
			handlerBtnMakeRoomAction(e);
		});

		// 나가기
		btnGameRoomExit.setOnAction(e -> {

			Platform.runLater(() -> {

				/**** 이부분지워보기 ****/
				txtChatArea.appendText("[Chat Out] \n");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				/**** 이부분지워보기 ****/
				txtInputMessage.setDisable(true);
				btnUserSend.setDisable(true);
				stopClient();
				Platform.exit();
			});

		});
	}

	// 로그인된 게임대기방 이미지와 아이디 보이기
	public void getUserIDandUserImage() {
		try {
			userId = GamerLoginController.UserId;
			System.out.println("로그인 들어온 아이디 : " + userId);
			gdao = new GamerDAO();
			gameUserId.setText(userId);
			GameWaitRoomFileName = gdao.getUserLoginIdAndImage(userId); // 저장한 이미지의 파일 이름
//			selectedFile = new File("C://남채현/java/java_img/" + GameWaitRoomFileName); // 저장한 이미지의 파일 이름의 url
			selectedFile = new File(System.getProperty("user.dir") + "/images\\" + GameWaitRoomFileName); // 저장한 이미지의 파일
																											// 이름의 url
			if (selectedFile != null) {
				// 저장한 파일의 이름의 url이 null값이 아니라면!
				localUrl = selectedFile.toURI().toURL().toString();
				userImg = new Image(localUrl, false);
				gamerImg.setImage(userImg);
				gamerImg.setFitHeight(250);
				gamerImg.setFitWidth(230);
				System.out.println("들어온 image : " + userImg);
			}
		} catch (Exception e1) {
			AlertDisplay.alertDisplay(1, "로그인된 아이디 이미지 가져오기 실패", "이미지 가져오기 실패!", e1.toString());
		}

	}

	// 내정보 수정하기
	public void handlerBtnMyInfoChangeAtion(ActionEvent e) {
		Parent MyInfoChangeRoot = null;
		Stage MyInfoChangeStage = null;
		try {
			MyInfoChangeRoot = FXMLLoader.load(getClass().getResource("/View/MyInfoChange.fxml"));
			Scene scene = new Scene(MyInfoChangeRoot);
			MyInfoChangeStage = new Stage();
			MyInfoChangeStage.setTitle("내정보수정");
			MyInfoChangeStage.initModality(Modality.WINDOW_MODAL);
			MyInfoChangeStage.initOwner(btnMakeRoom.getScene().getWindow());
			MyInfoChangeStage.setResizable(false);
			MyInfoChangeStage.setScene(scene);
			MyInfoChangeStage.show();
		} catch (IOException e1) {
			AlertDisplay.alertDisplay(1, "내정보수정 창 가져오기 오류", "내정보수정 창 가져오기 오류", e1.toString());
		}

	}

	// 게임방 만들기

	ManagerManagmentVO mmVO;
	UserStateDAO usdao;
	boolean roomNameCheck = false;
	UserStateVO usvo;

	public void handlerBtnMakeRoomAction(ActionEvent e) {
//		// 테스트를 위한 뷰뷰뷰뷰 ㅠㅠㅠㅠ
//		try {
//
//			Parent pieChart = FXMLLoader.load(getClass().getResource("/View/piechart.fxml"));
//			Stage stage = new Stage(StageStyle.UTILITY);
//			stage.initModality(Modality.WINDOW_MODAL);
//			stage.initOwner(btnMakeRoom.getScene().getWindow());
//			stage.setTitle("총점과 평균");
//
//			PieChart chart = (PieChart) pieChart.lookup("#pieChart");
//			Button btnClose = (Button) pieChart.lookup("#btnClose");
//
//			chart.setData(FXCollections.observableArrayList(
//					new PieChart.Data("총점", (double)3.4)
//					, new PieChart.Data("1점", (double)3.4)
//					, new PieChart.Data("2점", (double)3.4)));
//
//			btnClose.setOnAction(e2 -> {
//				stage.close();
//			});
//
//			Scene scene = new Scene(pieChart);
//			stage.setScene(scene);
//			stage.show();
//
//		} catch (IOException e2) {
//
//			e2.printStackTrace();
//		}

		try {

			Parent makeGameRoomRoot = FXMLLoader.load(getClass().getResource("/View/MakeRoom.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMakeRoom.getScene().getWindow());
			stage.setTitle("방만들기");

			TextField txtMakeRoomName = (TextField) makeGameRoomRoot.lookup("#txtMakeRoomName");
			Button btnMakeRoom = (Button) makeGameRoomRoot.lookup("#btnMakeRoom");
			Button btnCancle = (Button) makeGameRoomRoot.lookup("#btnCancle");

			btnMakeRoom.setOnAction(e3 -> {
				try {
					mmVO = new ManagerManagmentVO(txtMakeRoomName.getText()); // 아이디값 vo넣음
					usdao = new UserStateDAO(); // gdao 객체 가져옴
					roomNameCheck = usdao.checkRoomName(mmVO); // 아이디값 vo를 gdao.checkUserID()에 매개변수로 넣고 불리언 으로 true,
																// false로 받는다.
					if (txtMakeRoomName.getText().equals("")) {
						// 아디이값이 없을때
						AlertDisplay.alertDisplay(1, "방이름 미입력", "방이름이 빈칸입니다", "방이름을 입력하세요");
						return;
					}
					if (roomNameCheck == true) {
						// 중복된 아이디일때
						AlertDisplay.alertDisplay(1, "방이름중복!", "방이름이 중복되었습니다.", "다른 방이름을 적어주세요!");
					} else {
						AlertDisplay.alertDisplay(5, "방이름확인", "확인되었습니다.", "사용가능합니다.");

						/*
						 * 중복 검사 통과할 시, 데이터베이스에 방을 만듬 <mmVO> String roomName, String threadState (유저의
						 * 상태와 방이름으로!) String managerID, String makeRoomUserID, String enterRoomUserID,
						 * String gameRunOrWaitState
						 * 
						 */
						mmVO = new ManagerManagmentVO(txtMakeRoomName.getText(),
								UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT + "," + txtMakeRoomName.getText(), null,
								GamerLoginController.UserId, null, "Wait");
						usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
						int count = usdao.getUserGameRoomRegistration(mmVO.getRoomName(), mmVO.getThreadState(),
								mmVO.getManagerID(), mmVO.getMakeRoomUserID(), mmVO.getEnterRoomUserID(),
								mmVO.getGameRunOrWaitState()); // DAO에 UserID, UserThreadState를 넣어줌!
						AlertDisplay.alertDisplay(3, "DB 방등록", "등록성공!", "상태 : " + mmVO.getThreadState());

						if (count != 0) {
							/*
							 * 방만들기 시도 후 데이터베이스에서 등록 성공시, 유저의 상태변경
							 */
							usvo = new UserStateVO(GamerLoginController.UserId, mmVO.getThreadState()); // DB에 아이디와 상태를
																										// DAO에게!
							usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
							int count2 = usdao.getUserStateRegistration(usvo.getUserID(), mmVO.getThreadState()); // DAO에
																													// UserID,
																													// UserThreadState를
																													// 넣어줌!
							if (count2 != 0) {
								AlertDisplay.alertDisplay(3, "상태변동", "변동성공!", "상태 : " + mmVO.getThreadState());
								userState = mmVO.getThreadState();
							} else {
								throw new Exception("데이터베이스 등록실패!");
							}

							// 방만들기 성공후 메세지를 보내서 새로고침하게 함
							send(mmVO.getThreadState());
							stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!

						} else {
							throw new Exception("데이터베이스 등록실패!");
						}
					}

				} catch (Exception e4) {
					AlertDisplay.alertDisplay(1, "방등록", "등록실패!", e4.toString());
				}
			});

			btnCancle.setOnAction(e2 -> {
				stage.close();
			});

			Scene scene = new Scene(makeGameRoomRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e2) {

			e2.printStackTrace();
		}

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
				Platform.runLater(() -> {

					// 게임 대기방의 메세지 받기
					// 새로고침을 위한 옵션
					
					switch (message) {
					case UserGameState.GAMER_WAITROOM : txtChatArea.appendText(messageSplit(message)); break;
					default : {
						/*
						 * 업데이트가 필요한 행위를 넣어요~
						 * 
						 * 예를 들면 테이블뷰 업뎃, 사용자 업뎃 등등 유저가 방을 나가게 되면 하는 일들
						 * */
						break;
					}
					}
					
					// 대화를 구분하기 위한 
					if (message.startsWith(UserGameState.GAMER_WAITROOM)){
						txtChatArea.appendText(messageSplit(message));
						
					} else if (message.startsWith("방상태 변화이면, 업데이트가 되도록")) {
						// table 목록 업
					} else {
//						txtChatArea.appendText(message);
					}
				});
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
	
	public String messageSplit(String message) {
		String systemMessage = message;
		String[] array = systemMessage.split(",");
		String chatMessage = array[1] + " : " + array[2];
		return chatMessage;
	}

}
