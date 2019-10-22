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
	 * 관리 메인텝 (12)
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
	
	/*
	 * 메니저 관리 메인텝 (13)
	 */
	@FXML
	private Tab tabManager; 
	@FXML
	private ImageView imgManagerUser;
	@FXML
	private Label lblManagerUserId;
	@FXML
	private Label lblManagerAccess;
	@FXML
	private Label lblManagerState;
	@FXML
	private Button btnManagerChart;
	@FXML
	private Button btnManagerUserEdit;
	@FXML
	private Button btnManagerDel;
	@FXML
	private TableView<ManagerManagmentVO> tableViewManager;
	@FXML
	private Button btnManagerSelectedEditAcc;
	@FXML
	private Button btnManagerSelectedCancle;
	@FXML
	private Button btnManagerSelectedDel;
	@FXML
	private Button btnManagerExit;
	

	ObservableList<ManagerManagmentVO> userData;
	ObservableList<ManagerManagmentVO> userRoomData;
	ObservableList<ManagerManagmentVO> managerData;

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
	private ObservableList<ManagerManagmentVO> selectedManager;
	private int selectedManagerIndex;
	private String selectingManagerName;
	
	
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
		 * [ 탭 ] 관리자 관리(나중에 텝으로 묶기)
		 * 아이디 패스워드 권한 이미지 상태
		 * 
		 */
		tableViewManagerSetting();
		handlerManagerInfoGet();
		
		btnManagerChart.setOnAction(e5 -> {
			handlerBtnMainLoginTimeChartAction(e5);
		});
		// 관리자 관리 탭
		btnManagerUserEdit.setOnAction(e6 -> {
			((Stage) btnMainTabUserEdit.getScene().getWindow()).close();
			handlerBtnMyInfoChangeAtion(e6);
		});
		// 클릭 정보가져오기
		tableViewManager.setOnMousePressed(e7 -> {
			handlerMangagerTableViewSelectEvent(e7);
		});
		


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
			MyInfoChangeStage.setTitle("정보수정");
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
		tableView.isResizable();

		TableColumn roomName = new TableColumn("방이름");
		roomName.setMaxWidth(160);
		roomName.setStyle("-fx-alignment: CENTER;");
		roomName.setCellValueFactory(new PropertyValueFactory("RoomName"));

		TableColumn threadState = new TableColumn("접속상태");
		threadState.setMaxWidth(200);
		threadState.setStyle("-fx-alignment: CENTER;");
		threadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));

		TableColumn makeRoomUserID = new TableColumn("방장");
		makeRoomUserID.setMaxWidth(160);
		makeRoomUserID.setStyle("-fx-alignment: CENTER;");
		makeRoomUserID.setCellValueFactory(new PropertyValueFactory("MakeRoomUserID"));

		TableColumn enterRoomUserID = new TableColumn("참여자");
		enterRoomUserID.setMaxWidth(160);
		enterRoomUserID.setStyle("-fx-alignment: CENTER;");
		enterRoomUserID.setCellValueFactory(new PropertyValueFactory("EnterRoomUserID"));

		TableColumn gameRunOrWaitState = new TableColumn("게임방상태");
		gameRunOrWaitState.setMaxWidth(160);
		gameRunOrWaitState.setStyle("-fx-alignment: CENTER;");
		gameRunOrWaitState.setCellValueFactory(new PropertyValueFactory("gameRunOrWaitState"));

		tableView.setItems(userRoomData);
		tableView.getColumns().addAll(roomName, threadState, makeRoomUserID, enterRoomUserID, gameRunOrWaitState);

	}// end of tableViewSetting

	// 테이블 뷰를 새로고침하기 위한 함수
	private void handlerButtonTotalListAction() {
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

			chart.setData(FXCollections.observableArrayList(
					new PieChart.Data("총점", (double) 3.4),
					new PieChart.Data("1점", (double) 3.4), 
					new PieChart.Data("2점", (double) 3.4)));

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

	
	/*
	 * 유저 관리 탭을 위한 함수들
	 */

	/*
	 * 
	 * 모든 유저들의 정보를 가지고 옴 total list 참고함
	 * 
	 */
	private void tableViewManagerSetting() {
		managerData = FXCollections.observableArrayList();

		tableViewManager.setEditable(true);
		tableView.isResizable();
		//ui.UserID, ui.UserAccess, ui.UserImage, ugs.ThreadState
		TableColumn managerName = new TableColumn("UserID");
		managerName.setMaxWidth(160);
		managerName.setStyle("-fx-alignment: CENTER;");
		managerName.setCellValueFactory(new PropertyValueFactory("UserID"));

		TableColumn managerAccess = new TableColumn("UserAccess");
		managerAccess.setMaxWidth(160);
		managerAccess.setStyle("-fx-alignment: CENTER;");
		managerAccess.setCellValueFactory(new PropertyValueFactory("UserAccess"));

		TableColumn threadState = new TableColumn("ThreadState");
		threadState.setMaxWidth(160);
		threadState.setStyle("-fx-alignment: CENTER;");
		threadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));

		TableColumn managerImage = new TableColumn("UserImage");
		managerImage.setMaxWidth(160);
		managerImage.setStyle("-fx-alignment: CENTER;");
		managerImage.setCellValueFactory(new PropertyValueFactory("UserImage"));

		tableViewManager.setItems(managerData);
		tableViewManager.getColumns().addAll(managerName, managerAccess, threadState, managerImage);

	}// end of tableViewSetting

	// 테이블 뷰를 새로고침하기 위한 함수
	private void handlerManagerDataTableReloadAction(ActionEvent e9) {
		try {
			managerData.removeAll(managerData);
			handlerManagerInfoGet();
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "error", "error", e.toString());
			e.printStackTrace();
		}
	} // end of handlerButtonTotalListAction

	// 테이블 뷰에 넣을 데이터를 가져오기
	public void handlerManagerInfoGet() {
		ArrayList<ManagerManagmentVO> list = null;
		ManagerManagmentVO mmVO = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();
		list = mmDAO.getTableViewManagerInfoTotal();

		if (list == null) {
			AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			mmVO = list.get(i);
			managerData.add(mmVO);
//			System.out.println(mmVO.getRoomName() + " " + mmVO.getThreadState() + " " + mmVO.getMakeRoomUserID() + " "
//					+ mmVO.getEnterRoomUserID() + " " + mmVO.getGameRunOrWaitState() + " " + mmVO.getImage());
		}
	} // end of totalList

	// 테이블 클릭, 선택시, 메니저이름 가지고 오고, 상단에 정보 표시하기
	private void handlerMangagerTableViewSelectEvent(MouseEvent e3) {
		try {
			/*
			 * UserID = userID;
		UserPassword = userPassword;
		UserAccess = userAccess;
		Image = image;
		ThreadState = threadState;
			 * 
			 * */
			selectedManagerIndex = tableViewManager.getSelectionModel().getSelectedIndex();
			selectedManager = tableViewManager.getSelectionModel().getSelectedItems();

			selectingManagerName = selectedManager.get(0).getUserID();
			txtMainAreaServerLog.appendText("메니져이름 : " + selectingManagerName + "\n");
			lblManagerUserId.setText(selectingManagerName);
			switch (selectedManager.get(0).getUserAccess()) {
			case 1:
				lblManagerAccess.setText("예비관리자");
				break;
			case 2:
				lblManagerAccess.setText("정식관리자");
				break;
			case 3:
				lblManagerAccess.setText("총 관리자");
				break;
			}
			userState = selectedManager.get(0).getThreadState();
			lblManagerState.setText(userState);
			
			selectedFile = new File(System.getProperty("user.dir") 
					+ "/images\\" + selectedManager.get(0).getImage()); // 저장한 이미지의 파일이름
			if (selectedFile != null) {
				// 저장한 파일의 이름의 url이 null값이 아니라면!
				localUrl = selectedFile.toURI().toURL().toString();
				userImg = new Image(localUrl, false);
				imgManagerUser.setImage(userImg);
				imgManagerUser.setFitHeight(250);
				imgManagerUser.setFitWidth(230);
				System.out.println("들어온 image : " + userImg);
			}
		} catch (Exception e2) {
			AlertDisplay.alertDisplay(3, "메니져 가져오기", "메니져를 가져올 수 없습니다.", "메니져 정보를"
					+ " 가져오지 못했습니다.");
		} // end of try catch
	}// end of handlerTableViewSelectEvent
	
	
	// 채현이꺼 받아서 적용하기!!!
	public void handlerBtnUserInfoChangeAtion(ActionEvent e) {
		Parent MyInfoChangeRoot = null;
		Stage MyInfoChangeStage = null;
		selectedID=selectingManagerName;//테이블에서 값 가지고 오기
		try {
			MyInfoChangeRoot = FXMLLoader.load(getClass().getResource("/View/ManagerMyInfoChange.fxml"));
			Scene scene = new Scene(MyInfoChangeRoot);
			MyInfoChangeStage = new Stage();
			MyInfoChangeStage.setTitle("정보수정");
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
						handlerButtonTotalListAction();
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
						handlerButtonTotalListAction();

						break;
					default: {
						/*
						 * 업데이트가 필요한 행위를 넣어요~
						 * 
						 * 예를 들면 테이블뷰 업뎃, 사용자 업뎃 등등 유저가 방을 나가게 되면 하는 일들
						 */

						// 메인에 방 테이블 뷰 셋팅
						System.out.println("여기????");
						handlerButtonTotalListAction();
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
