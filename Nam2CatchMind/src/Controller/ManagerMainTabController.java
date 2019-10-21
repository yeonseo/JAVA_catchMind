package Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.DrowInfoVO;
import Model.ManagerManagmentVO;
import Model.UserStateVO;
import Model.UserVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManagerMainTabController implements Initializable {
	/*
	 * 관리 메인텝
	 */
	@FXML
	private ImageView imgMainUser;
	@FXML
	private Label lblMainUserId;
	@FXML
	private Label lblMainAccess;
	@FXML
	private Label lblMainState;
	@FXML
	private Button btnMainLoginTimeChart;
	@FXML
	private Button btnMainTabUserEdit;
	@FXML
	private TableView<ManagerManagmentVO> tableView;
	@FXML
	private Label lblMainTabServerState;
	@FXML
	private TextArea txtMainAreaServerLog;
	@FXML
	private Button btnMainServerOpen;
	@FXML
	private Button btnMainServerClose;
	@FXML
	private Button btnManagerMainTabExit;
	@FXML
	private Tab tabManager;

	ObservableList<ManagerManagmentVO> userData;
	ObservableList<ManagerManagmentVO> userRoomData;

	ManagerManagmentVO mmVO;
	ManagerManagmentDAO mmdao;

	// 회원정보 수정이나 관리할 유저의 정보를 가져올 때 사용할 것.
	public static String selectedID;

	// (로그인한)관리자의 정보가져오기
	String userId;
	String userAccess;
	String userState;
	Image userImg;
	String managerMainTabFileName;
	String loginTime;

	GamerDAO gdao;
	UserVO uvo;

	// 게임방 만들기
	UserStateDAO usdao;
	boolean roomNameCheck = false;
	UserStateVO usvo;

	TextArea txtTextArea;
	Label word;
	Canvas canvas;

	// 테이블 뷰에서 클릭시 방정보 가져오기
	private ObservableList<ManagerManagmentVO> selectedRoom;
	private int selectedRoomIndex;
	private String enteringRoomName;

	// 정보를 가져오기 위한 경로지정
	private File selectedFile = null;
	private String localUrl = "";

	ObservableList<DrowInfoVO> drowData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * 소켓 통신 시작
		 */
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtMainAreaServerLog.appendText("관리자님, 어서오세요. *^ㅁ^* \n");
		});

		/*
		 * 
		 * [ 탭 ] 관리자 메인
		 * 
		 * 
		 */

		// 로그인한 관리자의 정보 보이기
		getUserIDandUserImage();
		handlerUserInfoShow();
		btnMainTabUserEdit.setOnAction(e -> {
			handlerBtnMyInfoChangeAtion(e);
		});
		btnMainLoginTimeChart.setOnAction(e2 -> {
			handlerBtnMainLoginTimeChartAction(e2);
		});

		// 테이블 뷰 셋팅
		tableViewSetting();
		totalList();
		// 클릭 정보가져오기
		tableView.setOnMousePressed(e3 -> {
			handlerTableViewSelectEvent(e3);
		});
		// 더블클릭시 방 진입
		tableView.setOnMouseClicked(e4 -> {
			handlerTableViewEnterGameRoomAction(e4);
		});

		// 시간등록하기
		int Time = ManagerLoginController.loginTime;
		if (Time != 0) {
			gdao = new GamerDAO();
			loginTime = gdao.getCurrentTime(userId);
		} else {
			System.out.println("시간 등록안됨");
		}

		/*
		 * 
		 * [ 탭 ] 유저 관리(나중에 텝으로 묶기)
		 * 
		 * 
		 */

//		// 유저들의 정보 보이기
//		handlerUserInfoShow();
//
//		btnMainTabUserEdit.setOnAction(e -> {
//			((Stage) btnMainTabUserEdit.getScene().getWindow()).close();
//			handlerBtnMyInfoChangeAtion(e);
//		});
//

		// 나가기
		btnManagerMainTabExit.setOnAction(e999 -> {

			Platform.runLater(() -> {
				stopClient();
				Platform.exit();
			});

		});
	}

	/*
	 * 
	 * [ 탭 ] 관리자 메인
	 * 
	 * 
	 */

	/*
	 * 관리자의 정보를 가져오기 위한 함수
	 */
	private void handlerUserInfoShow() {
		ManagerManagmentVO mmVO = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();
		try {
			userId = ManagerLoginController.UserId;
			System.out.println("로그인 들어온 아이디 : " + userId);
			mmVO = mmDAO.getManagerUserInfoForMainData(userId);

			if (mmVO == null) {
				AlertDisplay.alertDisplay(1, "DB 관리자 정보 들고오기 실패!!", "Null Point", "Error");
				return;
			}
			System.out.println(mmVO.getUserID());
		} catch (Exception e1) {
			AlertDisplay.alertDisplay(1, "로그인된 아이디 이미지 가져오기 실패", "이미지 가져오기 실패!", e1.toString());
		}

		/*
		 * 불러온 값 설정하기
		 */
		userState = mmVO.getThreadState();
		switch (mmVO.getUserAccess()) {
		case 1:
			userAccess = "예비관리자";
			break;
		case 2:
			userAccess = "정식관리자";
			break;
		case 3:
			userAccess = "총 관리자";
			break;
		}
		lblMainUserId.setText(mmVO.getUserID());
		lblMainAccess.setText(userAccess);
		lblMainState.setText(mmVO.getThreadState());
	}

	// 로그인된 메인텝에 이미지와 아이디 보이기
	public void getUserIDandUserImage() {
		try {
			userId = ManagerLoginController.UserId;
			System.out.println("로그인 들어온 아이디 : " + userId);
			gdao = new GamerDAO();
			lblMainUserId.setText(userId);
			managerMainTabFileName = gdao.getUserLoginIdAndImage(userId); // 저장한 이미지의 파일 이름
//					selectedFile = new File("C://남채현/java/java_img/" + GameWaitRoomFileName); // 저장한 이미지의 파일 이름의 url
			selectedFile = new File(System.getProperty("user.dir") + "/images\\" + managerMainTabFileName); // 저장한 이미지의
																											// 파일이름의 url
			if (selectedFile != null) {
				// 저장한 파일의 이름의 url이 null값이 아니라면!
				localUrl = selectedFile.toURI().toURL().toString();
				userImg = new Image(localUrl, false);
				imgMainUser.setImage(userImg);
				imgMainUser.setFitHeight(250);
				imgMainUser.setFitWidth(230);
				System.out.println("들어온 image : " + userImg);
			}
		} catch (Exception e1) {
			AlertDisplay.alertDisplay(1, "로그인된 아이디 이미지 가져오기 실패", "이미지 가져오기 실패!", e1.toString());
		}

	}

	/*
	 * 정보 수정하기 Controller.ManagerMyInfoChangeController userChangeImage
	 * btnChangeImage btnUserPreviousCheckPwd btnCorrection btnExit userId
	 * userPreviousPwd userChangeCheckPwd userChangePwd
	 */
	public void handlerBtnMyInfoChangeAtion(ActionEvent e) {
		Parent MyInfoChangeRoot = null;
		Stage MyInfoChangeStage = null;
		selectedID = ManagerLoginController.UserId;
		try {
			MyInfoChangeRoot = FXMLLoader.load(getClass().getResource("/View/ManagerMyInfoChange.fxml"));
			Scene scene = new Scene(MyInfoChangeRoot);
			MyInfoChangeStage = new Stage();
			MyInfoChangeStage.setTitle("내 정보수정");
			MyInfoChangeStage.initModality(Modality.WINDOW_MODAL);
			MyInfoChangeStage.initOwner(btnMainServerOpen.getScene().getWindow());
			MyInfoChangeStage.setResizable(false);
			MyInfoChangeStage.setScene(scene);
			((Stage) btnManagerMainTabExit.getScene().getWindow()).close();
			MyInfoChangeStage.show();
		} catch (IOException e1) {
			AlertDisplay.alertDisplay(1, "내정보수정 창 가져오기 오류", "내정보수정 창 가져오기 오류", e1.toString());
		}

	}

	/*
	 * 메인 방이름 테이블뷰 방이름, 방장, 참여자, 상태
	 */
	private void tableViewSetting() {
		userRoomData = FXCollections.observableArrayList();

		tableView.setEditable(true);

		TableColumn roomName = new TableColumn("RoomName");
		roomName.setMaxWidth(60);
		roomName.setStyle("-fx-alignment: CENTER;");
		roomName.setCellValueFactory(new PropertyValueFactory("RoomName"));

		TableColumn threadState = new TableColumn("ThreadState");
		threadState.setMaxWidth(60);
		threadState.setStyle("-fx-alignment: CENTER;");
		threadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));

		TableColumn makeRoomUserID = new TableColumn("MakeRoomUserID");
		makeRoomUserID.setMaxWidth(60);
		makeRoomUserID.setStyle("-fx-alignment: CENTER;");
		makeRoomUserID.setCellValueFactory(new PropertyValueFactory("MakeRoomUserID"));

		TableColumn enterRoomUserID = new TableColumn("EnterRoomUserID");
		enterRoomUserID.setMaxWidth(60);
		enterRoomUserID.setStyle("-fx-alignment: CENTER;");
		enterRoomUserID.setCellValueFactory(new PropertyValueFactory("EnterRoomUserID"));

		TableColumn gameRunOrWaitState = new TableColumn("GameRunOrWaitState");
		gameRunOrWaitState.setMaxWidth(60);
		gameRunOrWaitState.setStyle("-fx-alignment: CENTER;");
		gameRunOrWaitState.setCellValueFactory(new PropertyValueFactory("gameRunOrWaitState"));

		tableView.setItems(userRoomData);
		tableView.getColumns().addAll(roomName, threadState, makeRoomUserID, enterRoomUserID, gameRunOrWaitState);

	}// end of tableViewSetting

	// 테이블 뷰를 새로고침하기 위한 함수
	private void handlerButtonTotalListAction(ActionEvent e9) {
		try {
			userRoomData.removeAll(userRoomData);
			totalList();
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "error", "error", e.toString());
			e.printStackTrace();
		}
	} // end of handlerButtonTotalListAction

	// 테이블 뷰에 넣을 데이터를 가져오기
	public void totalList() {
		ArrayList<ManagerManagmentVO> list = null;
		ManagerManagmentVO mmVO = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();
		list = mmDAO.getTableViewRoomInfoTotal();

		if (list == null) {
			AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			mmVO = list.get(i);
			userRoomData.add(mmVO);
//			System.out.println(mmVO.getRoomName() + " " + mmVO.getThreadState() + " " + mmVO.getMakeRoomUserID() + " "
//					+ mmVO.getEnterRoomUserID() + " " + mmVO.getGameRunOrWaitState() + " " + mmVO.getImage());
		}
	} // end of totalList

	// 테이블 클릭, 선택시, 방이름 가져오기
	private void handlerTableViewSelectEvent(MouseEvent e3) {

		try {

			selectedRoomIndex = tableView.getSelectionModel().getSelectedIndex();
			selectedRoom = tableView.getSelectionModel().getSelectedItems();

			enteringRoomName = selectedRoom.get(0).getRoomName();
			txtMainAreaServerLog.appendText("방이름 : " + enteringRoomName + "\n");

		} catch (Exception e2) {
			AlertDisplay.alertDisplay(3, "방이름 가져오기", "방이름을 가져올 수 없습니다.", "방이름을 가져오지 못했습니다.");
		} // end of try catch

	}// end of handlerTableViewSelectEvent

	// 테이블 뷰를 더블 클릭할 시, 게임방 창을 열어주는 함수
	private void handlerTableViewEnterGameRoomAction(MouseEvent e4) {

		try {

			if (e4.getClickCount() != 2) {
				return;
			}
			Parent gameRoomRoot = FXMLLoader.load(getClass().getResource("/View/GameRoomForManager.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMainTabUserEdit.getScene().getWindow());
			stage.setTitle("유저의 게임방 : 게임방 이름");

			word = (Label) gameRoomRoot.lookup("#word");
			canvas = (Canvas) gameRoomRoot.lookup("#canvas");
			txtTextArea = (TextArea) gameRoomRoot.lookup("#txtTextArea");
			TextField txtTextField = (TextField) gameRoomRoot.lookup("#txtTextField");
			Button btnSend = (Button) gameRoomRoot.lookup("#btnSend");
			Button btnGameStart = (Button) gameRoomRoot.lookup("#btnGameStart");
			Button btnExit = (Button) gameRoomRoot.lookup("#btnExit");

			word.setText("제시어는 공개되지 않습니다.");

			btnExit.setOnAction(e2 -> {
				stage.close();
			});

			Scene scene = new Scene(gameRoomRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e33) {

			e33.printStackTrace();
		}

	}// end of handlerPieChartAction

	/*
	 * 드로잉
	 * 
	 */

	/*
	 * 그림을 그리기 위한 변수
	 * 
	 * (데이터 베이스를 염두해 두어서 데이터 베이스의 형식으로 네이밍&만들어짐) arPt 좌표 리스트 DrowInfoDAO 좌표값을 DB에
	 * 저장하는 클래스 boolean, color를 위한 변수
	 */
	static ArrayList<DrowInfoVO> arPt = new ArrayList<DrowInfoVO>();
	private DrowInfoDAO drowInfoDAO;
	static boolean down = false;
	static int color = 0;

	// 시간을 가지고 파이차트 만들기
	public void handlerBtnMainLoginTimeChartAction(ActionEvent e2) {
		try {

			Parent pieChart = FXMLLoader.load(getClass().getResource("/View/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMainTabUserEdit.getScene().getWindow());
			stage.setTitle("총점과 평균");

			PieChart chart = (PieChart) pieChart.lookup("#pieChart");
			Button btnClose = (Button) pieChart.lookup("#btnClose");

			chart.setData(FXCollections.observableArrayList(new PieChart.Data("총점", (double) 3.4),
					new PieChart.Data("1점", (double) 3.4), new PieChart.Data("2점", (double) 3.4)));

			btnClose.setOnAction(e22 -> {
				stage.close();
			});

			Scene scene = new Scene(pieChart);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e22) {

			e22.printStackTrace();
		}

	}

	// 테이블 더블 클릭시 게임방으로 진입하게 값 가지고, handlerBtnMakeRoomAction()
	// 유저의 게임방을 창으로 띄우기 (방 만들기 대신에상태 가지고 가서 들어가기) reference 방만들기
	public void handlerBtnMakeRoomAction(ActionEvent e) {

		try {

			Parent makeGameRoomRoot = FXMLLoader.load(getClass().getResource("/View/MakeRoom.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMainTabUserEdit.getScene().getWindow());
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
						AlertDisplay.alertDisplay(5, "DB 방등록", "등록성공!", "상태 : " + mmVO.getThreadState());

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
								AlertDisplay.alertDisplay(5, "상태변동", "변동성공!", "상태 : " + mmVO.getThreadState());
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

	/*
	 * 유저 관리 탭을 위한 함수들
	 */

	/*
	 * 
	 * 모든 유저들의 정보를 가지고 옴 total list 참고함
	 * 
	 */
	private void handlerUserInfoGet() {

		ArrayList<ManagerManagmentVO> list = null;
		ManagerManagmentVO mmVO = null;
		ManagerManagmentDAO mmdao = new ManagerManagmentDAO();
		list = mmdao.getUsersTotalForUserTabData();

		if (list == null) {
			AlertDisplay.alertDisplay(1, "Users DB List Null Point", "Null Point", "Error");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			mmVO = list.get(i);
			userRoomData.add(mmVO);
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
					String[] sendMessage = messageSplit(message);

					switch (sendMessage[0]) {
					case UserGameState.GAMER_WAITROOM:

						tableViewSetting();
						totalList();
						txtMainAreaServerLog.appendText(sendMessage[1] + " : " + sendMessage[2]);
						break;
					case UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT:
						if (sendMessage[1].equals(enteringRoomName)) {
							if (sendMessage[2].startsWith("NoDrow")) {
								txtTextArea.appendText("안 그릴꺼얏!!\n");
								String drowMessage = message;
								String[] array = drowMessage.split(",");
								double x = Double.parseDouble(array[3]);
								double y = Double.parseDouble(array[4]);
								boolean draw = false;
								int color = Integer.parseInt(array[6]);

								arPt.add(new DrowInfoVO(x, y, draw, color));

								DrawCanvas drow = new DrawCanvas(arPt);
								GraphicsContext g = canvas.getGraphicsContext2D();

								drow.paint(g);
							} else if (sendMessage[2].startsWith("Drow")) {
								txtTextArea.appendText("그릴꺼얏!!\n");
								String drowMessage = message;
								String[] array = drowMessage.split(",");
								double x = Double.parseDouble(array[3]);
								double y = Double.parseDouble(array[4]);
								boolean draw = true;
								int color = Integer.parseInt(array[6]);

								DrawCanvas drow = new DrawCanvas(arPt);
								GraphicsContext g = canvas.getGraphicsContext2D();
								arPt.add(new DrowInfoVO(x, y, draw, color));
								drow.paint(g);
							} else {
								txtTextArea.appendText(sendMessage[2] + " : " + sendMessage[3]);
							}

						}
						tableViewSetting();
						totalList();
						// txtMainAreaServerLog.appendText(sendMessage[1]+" : "+sendMessage[2]);

						break;
					default: {
						/*
						 * 업데이트가 필요한 행위를 넣어요~
						 * 
						 * 예를 들면 테이블뷰 업뎃, 사용자 업뎃 등등 유저가 방을 나가게 되면 하는 일들
						 */

						// 메인에 방 테이블 뷰 셋팅
						System.out.println("여기????");
						tableViewSetting();
						totalList();
						txtMainAreaServerLog.appendText(message);
						break;
					}
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

	public String[] messageSplit(String message) {
		String systemMessage = message;
		String[] array = systemMessage.split(",");
		return array;
	}

}
