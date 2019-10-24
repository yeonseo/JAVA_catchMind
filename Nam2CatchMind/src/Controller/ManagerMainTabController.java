package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

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

	/*
	 * 유저 관리 메인텝 (13)
	 */
	@FXML
	private Tab tabGamer;
	@FXML
	private ImageView imgGamerUser;
	@FXML
	private Label lblGamerUserId;
	@FXML
	private Label lblGamerState;
	@FXML
	private Button btnGamerChart;
	@FXML
	private Button btnGamerDel;
	@FXML
	private TableView<ManagerManagmentVO> tableViewGamer;
	@FXML
	private Button btnGamerSelectedCancle;
	@FXML
	private Button btnGamerSelectedDel;
	@FXML
	private Button btnGamerExit;

	/*
	 * 통계 관리 메인텝 (4)
	 */
	@FXML
	private BubbleChart<String, String> chartBubble;
	@FXML
	private Button btnBubbleTotal;
	@FXML
	private Button btnBubbleManager;
	@FXML
	private Button btnBubbleBeManager;
	@FXML
	private Button btnBubbleGamer;

	/*
	 * 데이타 리스트 모음
	 */
	ObservableList<ManagerManagmentVO> userData; // 메인페이지의 관리자의 DB 가지고 오기위한 데이터
	ObservableList<ManagerManagmentVO> userRoomData; // 게임방의 테이블 뷰를 위한 데이터
	ObservableList<ManagerManagmentVO> managerData; // 관리자의 테이블 뷰를 위한 데이터
	ObservableList<ManagerManagmentVO> gamerData; // 유저의 테이블 뷰를 위한 데이터
	ObservableList<ManagerManagmentVO> mainTimeData; // 메인페이지의 관리자의 파이 차트를 만들기 위한 DB에서 가져온시간데이터
	ObservableList<String> managerTimeData; // 테이블 뷰의 관리자의 파이 차트를 만들기 위한 DB에서 가져온시간데이터
	ObservableList<String> userTimeData; // 테이블 뷰의 유저의 파이 차트를 만들기 위한 DB에서 가져온시간데이터

	GamerDAO gdao;
	UserVO uvo;
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

	// (로그인한) 관리자의 회원 정보 수정창을 위한 변수
	PasswordField userPreviousPwd;
	PasswordField userChangePwd;
	PasswordField userChangeCheckPwd;
	Button btnUserPreviousCheckPwd;
	Button btnUserCheckPwd;
	Button btnCorrection;
	Button btnExit;
	Label GamerId;
	String findPreviousPwd = null;
	private boolean previousPwdCheck = false; // 등록하는 순간 기존패스워드 확인버튼을 눌렀는지 체크
	private boolean changePwdCheck = false; // 등록하는 순간 변경할 비밀번호 체크혹인 버튼을 눌렀는지 체크
	// 이미지 찾기(회원 정보 수정창)
	Button btnChangeImage;
	ImageView userChangeImage;
	private String selectFileName = "";
	String fileName;

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
	// 테이블 뷰에서 클릭시 메니저 가져오기
	private ObservableList<ManagerManagmentVO> selectedManager;
	private int selectedManagerIndex;
	private String selectingManagerName;
	// 테이블 뷰에서 클릭시 저 가져오기
	private ObservableList<ManagerManagmentVO> selectedGamer;
	private int selectedGamerIndex;
	private String selectingGamerName;

	// 정보를 가져오기 위한 경로지정
	private File selectedFile = null;
	private String localUrl = "";

//	private File dirSave = new File("C://남채현/java/java_img"); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	String path = System.getProperty("user.dir") + "/images/";
	private File dirSave = new File(path); // 이미지 저장할 폴더를 매개변수로 파일 객체 생성

	ObservableList<DrowInfoVO> drowData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * 소켓 통신 시작
		 */
		startClient(UserGameState.IP, 9876);
		Platform.runLater(() -> {
			txtMainAreaServerLog.appendText("관리자님, 어서오세요. *^ㅁ^* \n");
		});

		/*
		 * 
		 * [ 탭 ] 관리자 메인
		 * 
		 * 
		 */
		if (ManagerLoginController.UserAccess < 2) {
			tabManager.setDisable(true);
		}
		// 로그인한 관리자의 정보 보이기
		getUserIDandUserImage();
		handlerUserInfoShow();
		// 로그인한 본인의정보 수정하기
		btnMainTabUserEdit.setOnAction(e -> {
			handlerBtnMyInfoChangeAtion(e);
			handlerButtonTotalListAction();
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

		/*
		 * 
		 * [ 탭 ] 관리자 관리 관리자의 아이디 패스워드 권한 이미지 상태 가지고
		 * 
		 */
		tableViewManagerSetting();
		handlerManagerInfoGet();

		btnManagerChart.setOnAction(e5 -> {
			handlerBtnManagerLoginTimeChartAction(e5);
		});
		// 관리자 권한 상승시키기
		btnManagerUserEdit.setOnAction(e6 -> {
			handlerBtnManagerUserEditAction(e6);
		});
		// 클릭 정보가져오기
		tableViewManager.setOnMousePressed(e7 -> {
			handlerMangagerTableViewSelectEvent();
		});

		/*
		 * 
		 * [ 탭 ] 유저 관리 유저 아이디 이미지 상태 접속시간 맞춘수, 센스표 수
		 * 
		 */
		tableViewGamerSetting();
		handlerGamerInfoGet();
		btnGamerChart.setOnAction(e5 -> {
			handlerBtnGamerLoginTimeChartAction(e5);
		});
		tableViewGamer.setOnMousePressed(e8 -> {
			handlerGamerTableViewSelectEvent();
		});

		/*
		 * 통계 탭
		 */
		btnBubbleTotal.setOnAction(e9 -> {
			try {
				handlerBtnBubbleTotalAction(3);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		btnBubbleManager.setOnAction(e9 -> {
			try {
				handlerBtnBubbleTotalAction(2);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		btnBubbleBeManager.setOnAction(e9 -> {
			try {
				handlerBtnBubbleTotalAction(1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		btnBubbleGamer.setOnAction(e9 -> {
			try {
				handlerBtnBubbleTotalAction(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// 나가기
		btnManagerMainTabExit.setOnAction(e999 -> {
			handlerBtnExitAction();
		});
		btnManagerExit.setOnAction(e999 -> {
			handlerBtnExitAction();
		});
		btnGamerExit.setOnAction(e999 -> {
			handlerBtnExitAction();
		});

	}

	// 나가기버튼
	private void handlerBtnExitAction() {
		Platform.runLater(() -> {
			stopClient();
			Platform.exit();
		});
	}

	/*
	 * 
	 * [ 탭 ] 관리자 메인
	 * 
	 * 
	 */

	/*
	 * 로그인한 관리자의 정보를 가져오기 위한 함수
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
//			System.out.println("로그인 들어온 아이디 : " + userId);
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

	// 내정보 수정하기(모달창)
	public void handlerBtnMyInfoChangeAtion(ActionEvent e) {
		Parent MyInfoChangeRoot = null;
		Stage MyInfoChangeStage = null;
		try {
			MyInfoChangeRoot = FXMLLoader.load(getClass().getResource("/View/MyInfoChange.fxml"));
			MyInfoChangeStage = new Stage(StageStyle.UTILITY);
			MyInfoChangeStage.initModality(Modality.WINDOW_MODAL);
			MyInfoChangeStage.initOwner(btnMainTabUserEdit.getScene().getWindow());
			MyInfoChangeStage.setTitle("내정보수정");
			MyInfoChangeStage.setResizable(false);

			userChangeImage = (ImageView) MyInfoChangeRoot.lookup("#userChangeImage");
			userPreviousPwd = (PasswordField) MyInfoChangeRoot.lookup("#userPreviousPwd");
			userChangePwd = (PasswordField) MyInfoChangeRoot.lookup("#userChangePwd");
			userChangeCheckPwd = (PasswordField) MyInfoChangeRoot.lookup("#userChangeCheckPwd");
			btnUserPreviousCheckPwd = (Button) MyInfoChangeRoot.lookup("#btnUserPreviousCheckPwd");
			btnChangeImage = (Button) MyInfoChangeRoot.lookup("#btnChangeImage");
			btnUserCheckPwd = (Button) MyInfoChangeRoot.lookup("#btnUserCheckPwd");
			btnCorrection = (Button) MyInfoChangeRoot.lookup("#btnCorrection");
			btnExit = (Button) MyInfoChangeRoot.lookup("#btnExit");
			GamerId = (Label) MyInfoChangeRoot.lookup("#userId");

			// 로그인된 아이디
			GamerId.setText(userId);

			// 기존 이미지 가져오기
			userChangeImage.setImage(userImg);

			// 기존 이미지 바꾸기
			btnChangeImage.setOnAction(e1 -> {
				handlerBtnChangeImageAction(e1);
			});

			// 기존 비밀번호 체크확인
			btnUserPreviousCheckPwd.setOnAction(e1 -> {
				handlerBtnUserPreviousCheckPwdAction(e1);
			});

			// 변경 비밀번호 체크확인
			btnUserCheckPwd.setOnAction(e1 -> {
				handlerbtnUserCheckPwdAction(e1);
			});

			// 전체 정보 저장 (비밀번호 and 이미지)
			btnCorrection.setOnAction(e1 -> {
				handlerBtnCorrectionAction(e1);
			});

			// 나가기
			btnExit.setOnAction(e1 -> {
				((Stage) btnExit.getScene().getWindow()).close();
			});

			Scene scene = new Scene(MyInfoChangeRoot);
			MyInfoChangeStage.setScene(scene);
			MyInfoChangeStage.show();

		} catch (IOException e1) {
			AlertDisplay.alertDisplay(1, "내정보수정 창 가져오기 오류", "내정보수정 창 가져오기 오류", e1.toString());
		}

	}

	// 전체 정보 저장 (비밀번호 and 이미지)
	public void handlerBtnCorrectionAction(ActionEvent e1) {
		if (userPreviousPwd.getText().equals("") || userChangePwd.getText().equals("")
				|| userChangeCheckPwd.getText().equals("") || localUrl == null) {

			AlertDisplay.alertDisplay(1, "정보미기입", "정보 미기입", "미기입된 정보가 있습니다. 다시 확인해주세요!");
			return;
		}
		if (previousPwdCheck != true) {
			AlertDisplay.alertDisplay(1, "기존비밀번호 확인", "기존 비밀번호 확인 요망!", "기존 비밀번호 확인 버튼을 눌러 확인해주세요!");
			return;
		}
		if (changePwdCheck != true) {
			AlertDisplay.alertDisplay(1, "변경 비밀번호 확인", "변경할 비밀번호 확인 요망!", "변경할 비밀번호 확인 버튼을 눌러 " + "비밀번호를 다시 확인해주세요!");
			return;
		}
		gdao = new GamerDAO();

		int i = gdao.getUserPwdChange(userChangePwd.getText(), userId);
		if (i == 1 && previousPwdCheck == true && changePwdCheck == true) {

			if (userChangePwd.getText().equals(userChangeCheckPwd.getText())) {

				changUserImage(); // 이미지 DB 으로 저장
				AlertDisplay.alertDisplay(5, "내정보수정", "내 정보 수정 성공!", "이미지와 비밀번호를 수정하였습니다.");
				((Stage) btnExit.getScene().getWindow()).close();
				getUserIDandUserImage();

			} else {
				AlertDisplay.alertDisplay(1, "비밀번호확인", "비밀번호가 다릅니다,", "비밀번호를 다시 확인해주세요!");
			}

		} else {
			AlertDisplay.alertDisplay(1, "내정보수정 실패", "내 정보 수정 실패", "이미지와 비밀번호를 수정하는데 실패했습니다.");
		}
	}

	// 변경 비밀번호 체크확인
	public void handlerbtnUserCheckPwdAction(ActionEvent e1) {
		if (userChangePwd.getText().equals("") || userChangeCheckPwd.getText().equals("")) {
			AlertDisplay.alertDisplay(1, "비밀번호확인", "변경 비밀번호 미입력!", "바꿀 비밀번호를 입력해주세요!");
			return;
		}
		if (userChangePwd.getText().equals(findPreviousPwd)) {
			AlertDisplay.alertDisplay(1, "비밀번호확인", "기존에 비밀번호와 중복됩니다.", "바꿀 비밀번호를 입력해주세요!");
			return;
		}
		if (userChangePwd.getText().equals(userChangeCheckPwd.getText())) {
			AlertDisplay.alertDisplay(5, "비밀번호확인", "비밀번호 일치합니다!", "비밀번호 변경이 가능합니다.");
			changePwdCheck = true;
		} else {
			AlertDisplay.alertDisplay(1, "비밀번호확인", "비밀번호가 다릅니다,", "비밀번호를 다시 확인해주세요!");
		}
	}

	// 기존 비밀번호 체크확인
	public void handlerBtnUserPreviousCheckPwdAction(ActionEvent e1) {

		gdao = new GamerDAO();
		findPreviousPwd = gdao.getUserPreviousCheck(userId);
		if (userPreviousPwd.getText().equals("")) {
			AlertDisplay.alertDisplay(1, "비밀번호확인", "변경 비밀번호 미입력!", "바꿀 비밀번호를 입력해주세요!");
			return;
		}
		if (userPreviousPwd.getText().equals(findPreviousPwd)) {
			AlertDisplay.alertDisplay(5, "기존비밀번호확인", "기존 비밀번호 확인", "비밀번호 변경이 가능합니다.");
			previousPwdCheck = true; // 기존 비밀번호를 체크를 해야 비밀번호를 바꿀 수 있다.
		} else {
			AlertDisplay.alertDisplay(1, "기존 비밀번호확인", "기존에 비밀번호와 맞지않습니다.", "다시 확인해주세요!");
		}

	}

	// 기존 이미지 바꾸기
	public void handlerBtnChangeImageAction(ActionEvent e1) {
		// 이미지 선택
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif", "*.jpeg"));
		try {
			selectedFile = fileChooser.showOpenDialog(btnChangeImage.getScene().getWindow());
			if (selectedFile != null) {
				// 이미지 파일 경로
				localUrl = selectedFile.toURI().toURL().toString(); // 선택된 이미지 파일 경로를 저장
				selectFileName = selectedFile.getName();// 선택된 이미지 이름!! 도 저장함.

			} else {
				AlertDisplay.alertDisplay(1, "이미지오류", "이미지 선택요망!", "이미지를 선택해주세요!");
			}
			userImg = new Image(localUrl, false); // 이미지 객체
			userChangeImage.setImage(userImg);
			userChangeImage.setFitHeight(350);
			userChangeImage.setFitWidth(350);

		} catch (Exception e2) {
			AlertDisplay.alertDisplay(1, "이미지오류", "이미지를 찾기, 수정 오류!",
					"선택된 이미지 파일경로 오류, 선택된 이미지 이름 저장 오류" + e1.toString());
		}

	}

	// 이미지 저장
	public String imageSave(File file1) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// 이미지 파일명 생성
			fileName = "changeImage" + System.currentTimeMillis() + "_" + file1.getName();
			bis = new BufferedInputStream(new FileInputStream(file1)); // 선택한 파일 이미지를 읽어옴.
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName)); // 이미지를
																												// 보냄!

			// 선택한 이미지 파일 InputStream의 마지막에 이르렀을 경우는 -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return fileName; // << String 으로 리턴값
	}

	// 이미지 DB로 저장
	public void changUserImage() {
		try {
			File dirMake = new File(dirSave.getAbsolutePath());
			if (!dirMake.exists()) {
				dirMake.mkdir();
			}
			// 이미지 파일 저장
			fileName = imageSave(selectedFile);
			System.out.println("fileName :" + selectedFile);
			System.out.println(userPreviousPwd.getText());
			System.out.println(fileName);
			gdao = new GamerDAO();

			gdao.getUserImageChange(fileName, userPreviousPwd.getText());
		} catch (Exception e1) {
			AlertDisplay.alertDisplay(1, "이미지오류", "이미지등록 오류!", "선택된 이미지 이름 저장 오류" + e1.toString());
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

			canvas.setDisable(true);
			txtTextField.setDisable(true);
			btnSend.setDisable(true);
			btnGameStart.setDisable(true);

			word.setText("보기모드");

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

	// 관리자 시간을 가지고 파이차트 만들기
	public void handlerBtnMainLoginTimeChartAction(ActionEvent e2) {
		ArrayList<String> timeData = null;
		int[] month = new int[12];
		try {
			Parent pieChart = FXMLLoader.load(getClass().getResource("/View/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMainTabUserEdit.getScene().getWindow());
			stage.setTitle("월별 접속횟수");
			PieChart chart = (PieChart) pieChart.lookup("#pieChart");
			Button btnClose = (Button) pieChart.lookup("#btnClose");
			try {
				timeData = handlerGetUserTimeSplit();
				String[] splitTimeDB = null;
//				for (String i : timeData) {
//					splitTimeDB = i.split(",");
//					txtMainAreaServerLog.appendText(splitTimeDB[0] + " " + splitTimeDB[1] + " " + splitTimeDB[2] + " "
//							+ splitTimeDB[3] + " " + splitTimeDB[4] + " " + splitTimeDB[5] + "\n");
//				}
				for (String i : timeData) {
					splitTimeDB = i.split(",");
					switch (splitTimeDB[1]) {
					case "01":
						month[0]++;
						break;
					case "02":
						month[1]++;
						break;
					case "03":
						month[2]++;
						break;
					case "04":
						month[3]++;
						break;
					case "05":
						month[4]++;
						break;
					case "06":
						month[5]++;
						break;
					case "07":
						month[6]++;
						break;
					case "08":
						month[7]++;
						break;
					case "09":
						month[8]++;
						break;
					case "10":
						month[9]++;
						break;
					case "11":
						month[10]++;
						break;
					case "12":
						month[11]++;
						break;
					}
				}

			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
			}
			txtMainAreaServerLog.appendText("test5 \n");

			chart.setData(FXCollections.observableArrayList(new PieChart.Data("1월", (double) month[0]),
					new PieChart.Data("2월", (double) month[1]), new PieChart.Data("3월", (double) month[2]),
					new PieChart.Data("4월", (double) month[3]), new PieChart.Data("5월", (double) month[4]),
					new PieChart.Data("6월", (double) month[5]), new PieChart.Data("7월", (double) month[6]),
					new PieChart.Data("8월", (double) month[7]), new PieChart.Data("9월", (double) month[8]),
					new PieChart.Data("10월", (double) month[9]), new PieChart.Data("11월", (double) month[10]),
					new PieChart.Data("12월", (double) month[11])));

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

	// 관리자 시간을 가지고 오기
	private ArrayList<String> handlerGetUserTimeSplit() {
		ArrayList<String> list = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();

		try {
			list = mmDAO.getCurrentTime(userId);
			if (list == null) {
				AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
				return null;
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
		}
		return list;
	}

	/*
	 * 메니저 탭 메니저 정보를 가지고 옴 total list 참고함
	 */
	private void tableViewManagerSetting() {
		managerData = FXCollections.observableArrayList();

		tableViewManager.setEditable(true);
		tableView.isResizable();
		// ui.UserID, ui.UserAccess, ui.UserImage, ugs.ThreadState
		TableColumn managerName = new TableColumn("ID");
		managerName.setMaxWidth(160);
		managerName.setStyle("-fx-alignment: CENTER;");
		managerName.setCellValueFactory(new PropertyValueFactory("UserID"));

		TableColumn managerAccess = new TableColumn("권한 단계");
		managerAccess.setMaxWidth(160);
		managerAccess.setStyle("-fx-alignment: CENTER;");
		managerAccess.setCellValueFactory(new PropertyValueFactory("UserAccess"));

		TableColumn threadState = new TableColumn("접속상태");
		threadState.setMaxWidth(160);
		threadState.setStyle("-fx-alignment: CENTER;");
		threadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));

//		TableColumn managerImage = new TableColumn("이미지");
//		managerImage.setMaxWidth(160);
//		managerImage.setStyle("-fx-alignment: CENTER;");
//		managerImage.setCellValueFactory(new PropertyValueFactory("UserImage"));

		tableViewManager.setItems(managerData);
		tableViewManager.getColumns().addAll(managerName, managerAccess, threadState/* ,managerImage */);

	}// end of tableViewSetting

	// 테이블 뷰를 새로고침하기 위한 함수
	private void handlerManagerDataTableReloadAction() {
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
	private void handlerMangagerTableViewSelectEvent() {
		try {
			/*
			 * UserID = userID; UserPassword = userPassword; UserAccess = userAccess; Image
			 * = image; ThreadState = threadState;
			 * 
			 */
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

			selectedFile = new File(System.getProperty("user.dir") + "/images\\" + selectedManager.get(0).getImage()); // 저장한
																														// 이미지의
																														// 파일이름
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
			AlertDisplay.alertDisplay(3, "메니져 가져오기", "메니져를 가져올 수 없습니다.", "메니져 정보를" + " 가져오지 못했습니다.");
		} // end of try catch
	}// end of handlerTableViewSelectEvent

	// 테이블 뷰에서 선택한 관리자 시간을 가지고 파이차트 만들기
	public void handlerBtnManagerLoginTimeChartAction(ActionEvent e2) {
		ArrayList<String> timeData = null;
		int[] month = new int[12];
		try {
			Parent pieChart = FXMLLoader.load(getClass().getResource("/View/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMainTabUserEdit.getScene().getWindow());
			stage.setTitle("월별 접속횟수");
			PieChart chart = (PieChart) pieChart.lookup("#pieChart");
			Button btnClose = (Button) pieChart.lookup("#btnClose");
			try {
				timeData = handlerGetManagerTimeSplit();
				String[] splitTimeDB = null;
//				for (String i : timeData) {
//					splitTimeDB = i.split(",");
//					txtMainAreaServerLog.appendText(splitTimeDB[0] + " " + splitTimeDB[1] + " " + splitTimeDB[2] + " "
//							+ splitTimeDB[3] + " " + splitTimeDB[4] + " " + splitTimeDB[5] + "\n");
//				}
				for (String i : timeData) {
					splitTimeDB = i.split(",");
					switch (splitTimeDB[1]) {
					case "01":
						month[0]++;
						break;
					case "02":
						month[1]++;
						break;
					case "03":
						month[2]++;
						break;
					case "04":
						month[3]++;
						break;
					case "05":
						month[4]++;
						break;
					case "06":
						month[5]++;
						break;
					case "07":
						month[6]++;
						break;
					case "08":
						month[7]++;
						break;
					case "09":
						month[8]++;
						break;
					case "10":
						month[9]++;
						break;
					case "11":
						month[10]++;
						break;
					case "12":
						month[11]++;
						break;
					}
				}

			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
			}
			txtMainAreaServerLog.appendText("test5 \n");

			chart.setData(FXCollections.observableArrayList(new PieChart.Data("1월", (double) month[0]),
					new PieChart.Data("2월", (double) month[1]), new PieChart.Data("3월", (double) month[2]),
					new PieChart.Data("4월", (double) month[3]), new PieChart.Data("5월", (double) month[4]),
					new PieChart.Data("6월", (double) month[5]), new PieChart.Data("7월", (double) month[6]),
					new PieChart.Data("8월", (double) month[7]), new PieChart.Data("9월", (double) month[8]),
					new PieChart.Data("10월", (double) month[9]), new PieChart.Data("11월", (double) month[10]),
					new PieChart.Data("12월", (double) month[11])));

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

	// 테이블 뷰에서 선택한 관리자 시간을 가지고 오기
	private ArrayList<String> handlerGetManagerTimeSplit() {
		ArrayList<String> list = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();

		try {
			list = mmDAO.getCurrentTime(lblManagerUserId.getText());
			if (list == null) {
				AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
				return null;
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
		}
		return list;
	}

	// 관리자 권한 상승시키기
	private void handlerBtnManagerUserEditAction(ActionEvent e6) {
		try {
			switch (selectedManager.get(0).getUserAccess()) {
			case 1:
				AlertDisplay.alertDisplay(2, "권한수정", "권한을 상승합니다", "수정하시겠습니까?");
				if (AlertDisplay.result.get() == ButtonType.OK) {

					// 여기 수정중!!!
					mmdao = new ManagerManagmentDAO();
					mmdao.setManagerAccess(selectedManager.get(0).getUserID(), 2);
					AlertDisplay.alertDisplay(5, "권한수정", "권한을 상승합니다", "정식관리자로 변경합니다.");
					handlerManagerDataTableReloadAction();
					lblManagerAccess.setText("정식관리자");
				} else {
					AlertDisplay.alertDisplay(5, "권한유지", "권한을 유지합니다", "예비관리자입니다.");
				}
				break;
			case 2:
				AlertDisplay.alertDisplay(2, "권한수정", "권한을 뺏습니다", "수정하시겠습니까?");
				if (AlertDisplay.result.get() == ButtonType.OK) {
					AlertDisplay.alertDisplay(5, "권한수정", "권한을 수정합니다", "예비관리자로 변경합니다.");
					mmdao = new ManagerManagmentDAO();
					mmdao.setManagerAccess(selectedManager.get(0).getUserID(), 1);
					System.out.println("testtest6");
					handlerManagerDataTableReloadAction();
					lblManagerAccess.setText("예비관리자");
				} else {
					AlertDisplay.alertDisplay(5, "권한유지", "권한을 유지합니다", "정식관리자입니다.");
				}
				break;
			case 3:
				AlertDisplay.alertDisplay(5, "최고관리자", "최고 권한의 사용자입니다.", "수정이 불가합니다.");
				break;
			}
		} catch (Exception e) {

		}

	}

	/*
	 * 
	 * 유저 탭
	 * 
	 */
	private void tableViewGamerSetting() {
		gamerData = FXCollections.observableArrayList();

		tableViewGamer.setEditable(true);
		tableViewGamer.isResizable();
		// ui.UserID, ui.UserAccess, ui.UserImage, ugs.ThreadState
		TableColumn gamerName = new TableColumn("ID");
		gamerName.setMaxWidth(160);
		gamerName.setStyle("-fx-alignment: CENTER;");
		gamerName.setCellValueFactory(new PropertyValueFactory("UserID"));

		TableColumn gamerAccess = new TableColumn("권한 단계");
		gamerAccess.setMaxWidth(160);
		gamerAccess.setStyle("-fx-alignment: CENTER;");
		gamerAccess.setCellValueFactory(new PropertyValueFactory("UserAccess"));

		TableColumn threadState = new TableColumn("접속상태");
		threadState.setMaxWidth(260);
		threadState.setStyle("-fx-alignment: CENTER;");
		threadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));

//		TableColumn managerImage = new TableColumn("이미지");
//		managerImage.setMaxWidth(160);
//		managerImage.setStyle("-fx-alignment: CENTER;");
//		managerImage.setCellValueFactory(new PropertyValueFactory("UserImage"));

		tableViewGamer.setItems(gamerData);
		tableViewGamer.getColumns().addAll(gamerName, gamerAccess, threadState/* ,managerImage */);

	}// end of tableViewSetting
		// 테이블 뷰를 새로고침하기 위한 함수

	private void handlerGamerDataTableReloadAction() {
		try {
			gamerData.removeAll(gamerData);
			handlerManagerInfoGet();
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "error", "error", e.toString());
			e.printStackTrace();
		}
	} // end of handlerButtonTotalListAction

	// 테이블 뷰에 넣을 데이터를 가져오기
	public void handlerGamerInfoGet() {
		ArrayList<ManagerManagmentVO> list = null;
		ManagerManagmentVO mmVO = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();
		list = mmDAO.getTableViewGamerInfoTotal();

		if (list == null) {
			AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			mmVO = list.get(i);
			gamerData.add(mmVO);
			System.out.println(mmVO.getUserID() + " " + mmVO.getImage() + "" + mmVO.getThreadState());
		}
	} // end of totalList

	// 테이블 클릭, 선택시, 유저 이름 가지고 오고, 상단에 정보 표시하기
	private void handlerGamerTableViewSelectEvent() {
		try {
			/*
			 * UserID = userID; UserPassword = userPassword; UserAccess = userAccess; Image
			 * = image; ThreadState = threadState;
			 * 
			 */
			selectedGamerIndex = tableViewGamer.getSelectionModel().getSelectedIndex();
			selectedGamer = tableViewGamer.getSelectionModel().getSelectedItems();

			selectingGamerName = selectedGamer.get(0).getUserID();
			txtMainAreaServerLog.appendText("메니져이름 : " + selectingGamerName + "\n");
			lblGamerUserId.setText(selectingGamerName);
			lblGamerState.setText(selectedGamer.get(0).getThreadState());

			selectedFile = new File(System.getProperty("user.dir") + "/images\\" + selectedGamer.get(0).getImage()); // 저장한
																														// 이미지의
																														// 파일이름
			if (selectedFile != null) {
				// 저장한 파일의 이름의 url이 null값이 아니라면!
				localUrl = selectedFile.toURI().toURL().toString();
				userImg = new Image(localUrl, false);
				imgGamerUser.setImage(userImg);
				imgGamerUser.setFitHeight(250);
				imgGamerUser.setFitWidth(230);
				System.out.println("들어온 image : " + userImg);
			}
		} catch (Exception e2) {
			AlertDisplay.alertDisplay(3, "메니져 가져오기", "메니져를 가져올 수 없습니다.", "메니져 정보를" + " 가져오지 못했습니다.");
		} // end of try catch
	}// end of handlerTableViewSelectEvent

	// 테이블 뷰에서 선택한 유저 시간을 가지고 파이차트 만들기
	public void handlerBtnGamerLoginTimeChartAction(ActionEvent e2) {
		ArrayList<String> timeData = null;
		int[] month = new int[12];
		try {
			Parent pieChart = FXMLLoader.load(getClass().getResource("/View/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnMainTabUserEdit.getScene().getWindow());
			stage.setTitle("월별 접속횟수");
			PieChart chart = (PieChart) pieChart.lookup("#pieChart");
			Button btnClose = (Button) pieChart.lookup("#btnClose");
			try {
				timeData = handlerGetGamerTimeSplit();
				String[] splitTimeDB = null;
//				for (String i : timeData) {
//					splitTimeDB = i.split(",");
//					txtMainAreaServerLog.appendText(splitTimeDB[0] + " " + splitTimeDB[1] + " " + splitTimeDB[2] + " "
//							+ splitTimeDB[3] + " " + splitTimeDB[4] + " " + splitTimeDB[5] + "\n");
//				}
				for (String i : timeData) {
					splitTimeDB = i.split(",");
					switch (splitTimeDB[1]) {
					case "01":
						month[0]++;
						break;
					case "02":
						month[1]++;
						break;
					case "03":
						month[2]++;
						break;
					case "04":
						month[3]++;
						break;
					case "05":
						month[4]++;
						break;
					case "06":
						month[5]++;
						break;
					case "07":
						month[6]++;
						break;
					case "08":
						month[7]++;
						break;
					case "09":
						month[8]++;
						break;
					case "10":
						month[9]++;
						break;
					case "11":
						month[10]++;
						break;
					case "12":
						month[11]++;
						break;
					}
				}

			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
			}
			txtMainAreaServerLog.appendText("test5 \n");

			chart.setData(FXCollections.observableArrayList(new PieChart.Data("1월", (double) month[0]),
					new PieChart.Data("2월", (double) month[1]), new PieChart.Data("3월", (double) month[2]),
					new PieChart.Data("4월", (double) month[3]), new PieChart.Data("5월", (double) month[4]),
					new PieChart.Data("6월", (double) month[5]), new PieChart.Data("7월", (double) month[6]),
					new PieChart.Data("8월", (double) month[7]), new PieChart.Data("9월", (double) month[8]),
					new PieChart.Data("10월", (double) month[9]), new PieChart.Data("11월", (double) month[10]),
					new PieChart.Data("12월", (double) month[11])));

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

	// 테이블 뷰에서 선택한 관리자 시간을 가지고 오기
	private ArrayList<String> handlerGetGamerTimeSplit() {
		ArrayList<String> list = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();

		try {
			list = mmDAO.getCurrentTime(lblGamerUserId.getText());
			if (list == null) {
				AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
				return null;
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
		}
		return list;
	}

	/*
	 * 
	 * 통계 탭
	 * 
	 */

	// 총 데이터 가지고 버블차트 만들기(우선 유저데이터)
	public void handlerBtnBubbleTotalAction(int access) throws IOException {
		ArrayList<String> timeData = null;
		int[] month = new int[12];
		XYChart.Series<String, String> series = new XYChart.Series();

		// 버블차트 초기화
		series.getData().clear();
		chartBubble.getData().clear();

		String bubbleChartName = null;
		switch (access) {
		case 0:
			bubbleChartName = "유저";
			break;
		case 1:
			bubbleChartName = "예비관리자";
			break;
		case 2:
			bubbleChartName = "정식관리자";
			break;
		case 3:
			bubbleChartName = "총 관리자";
			break;
		}
		series.setName(bubbleChartName);
		timeData = handlerGetBtnBubbleTotalTimeSplit(access);
		String[] splitTimeDB = null;
		String list;
//		for (String i : timeData) {
//			splitTimeDB = i.split(",");
//			txtMainAreaServerLog.appendText(splitTimeDB[0] + " " + splitTimeDB[1] + " " + splitTimeDB[2] + " "
//					+ splitTimeDB[3] + " " + splitTimeDB[4] + " " + splitTimeDB[5] + "\n");
//		}
		int i;
		for (String listData : timeData) {
			splitTimeDB = listData.split(",");
			switch (splitTimeDB[1]) {
			case "01":
				for (i = 0; i < splitTimeDB[2].length(); i++) {
//					System.out.println(Integer.parseInt(splitTimeDB[1])+" "+
//							Integer.parseInt(splitTimeDB[2])+" "+i);
				}
				series.getData().add(new XYChart.Data(splitTimeDB[1], splitTimeDB[2], i));
				break;
			case "02":
				for (i = 0; i < splitTimeDB[2].length(); i++) {
					series.getData().add(new XYChart.Data(splitTimeDB[1], splitTimeDB[2], i));
				}
				break;
			case "03":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData().add(new XYChart.Data(splitTimeDB[1], splitTimeDB[2], i));
				break;
			case "04":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "05":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "06":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "07":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "08":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "09":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "10":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "11":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			case "12":
				for (i = 0; i < splitTimeDB[2].length(); i++) {

				}
				series.getData()
						.add(new XYChart.Data(Integer.parseInt(splitTimeDB[1]), Integer.parseInt(splitTimeDB[2]), i));
				break;
			}
		}

		chartBubble.getData().add(series);

	}

	// 관리자 시간을 가지고 오기
	private ArrayList<String> handlerGetBtnBubbleTotalTimeSplit(int access) {
		ArrayList<String> list = null;
		ManagerManagmentDAO mmDAO = new ManagerManagmentDAO();

		try {
			list = mmDAO.getFindUseAccessCurrentTime(access);
			if (list == null) {
				AlertDisplay.alertDisplay(1, "total DB List Null Point", "Null Point", "Error");
				return null;
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "시간 가져오기 실패", " 출력안됨 ", "시간 가져오는 함수 확인 요망");
		}
		return list;
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
//								txtTextArea.appendText("안 그릴꺼얏!!\n");
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
//								txtTextArea.appendText("그릴꺼얏!!\n");
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
						handlerButtonTotalListAction();
						handlerManagerDataTableReloadAction();
						handlerGamerDataTableReloadAction();

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
