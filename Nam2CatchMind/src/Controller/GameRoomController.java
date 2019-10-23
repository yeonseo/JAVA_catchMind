package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import Model.UserGameHistoryVO;
import Model.UserStateVO;
import Model.UserVO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Model.DrowInfoVO;
import Model.KeyWordVO;
import Model.MakeRoomVO;

public class GameRoomController implements Initializable {

	@FXML
	Label word;
	@FXML
	Canvas canvas;
	@FXML
	TextArea txtTextArea;
	@FXML
	TextField txtTextField;
	@FXML
	Button btnStrColorBlack;
	@FXML
	Button btnStrColorRed;
	@FXML
	Button btnStrColorBlue;
	@FXML
	Button btnSend;
	@FXML
	Button btnGameStart;
	@FXML
	Button btnExit;
	String Gamer1;
	String Gamer2;
	String keyWord; // 랜덤 단어

	String userState = UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT;

	UserVO uvo;
	UserStateVO usvo;
	UserStateDAO usdao;
	GamerDAO gdao;

	ArrayList<MakeRoomVO> mrlist;
	ArrayList<MakeRoomVO> mrvoList;
	ObservableList<DrowInfoVO> drowData;
	UserGameHistoryVO ughvo;
	UserGameHistoryVO ughvo2;
	int Play = 0;
	int Gamer1Sence = 0;
	boolean sence = true;
	int Win = 0;

	/*
	 * 그림을 그리기 위한 변수
	 * 
	 * (데이터 베이스를 염두해 두어서 데이터 베이스의 형식으로 네이밍&만들어짐) arPt 좌표 리스트 DrowInfoDAO 좌표값을 DB에
	 * 저장하는 클래스 boolean, color를 위한 변수
	 */
	static ArrayList<DrowInfoVO> arPt = new ArrayList<DrowInfoVO>();
	private DrowInfoDAO drowInfoDAO;
	static boolean down = false;
	String drowMessage;
	String[] array;
	double x;
	double y;
	boolean draw;
	int color;
	DrawCanvas drow;
	GraphicsContext g;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		btnSend.setDisable(false);

		startClient(UserGameState.IP, 9876);
		Platform.runLater(() -> {
			txtTextArea.appendText("[Chat Start] \n");
			
			// 방장에 이름으로 방이름 보내기
			send(userState + "," + GameWaitRoomController.roomName + "," + "welcome2" + ","
					+ GamerLoginController.UserId + " 님이" + GameWaitRoomController.roomName + "에 입장하셨습니다.\n");
		});
		// 방장 버튼, 캔버스 초기화
		BtnInitialization();

		// 게임시작 버튼!
		btnGameStart.setOnAction(e -> {
			handlerBtnGameStartAction(e);
		});

		// 메시지 시작 버튼!
		btnSend.setOnAction(event -> {
			// 메세지 전송시자신의 상태외 아이디, 메세지를 함께 보냄

			/*
			 * 여기부터 > A < 게임방에서는 방이름까지 합쳐서 던저주는 걸로 바꿨어용~~~~
			 * 
			 */
			send(userState + "," + GameWaitRoomController.roomName + "," + GamerLoginController.UserId + ","
					+ txtTextField.getText() + "," + "\n");

			System.out.println(userState + "," + GameWaitRoomController.roomName + "," + GamerLoginController.UserId
					+ "," + txtTextField.getText());
			/** 여기까지 바꿨어용~~~~~~ **/
			txtTextField.setText("");
			txtTextField.requestFocus();

		});

		btnExit.setOnAction(e -> {
			send(userState + "," + GameWaitRoomController.roomName + "," + "welcome2out" + "," + ">>>>>"
					+ GamerLoginController.UserId + " 님이 " + GameWaitRoomController.roomName + "나가셨습니다.<<<<<\n");
			Parent gameRoomRoot = null;
			Stage gameRoomStage = null;
			try {
				gameRoomRoot = FXMLLoader.load(getClass().getResource("/View/GameWaitRoom.fxml"));
				Scene scene = new Scene(gameRoomRoot);
				gameRoomStage = new Stage();
				gameRoomStage.setTitle("게임대기방");
				gameRoomStage.setScene(scene);
				gameRoomStage.setResizable(false);
				((Stage) btnExit.getScene().getWindow()).close();
				send("roomStateUpdate" + "," + UserGameState.GAMER_WAITROOM);
				gameRoomStage.show();
//				stopClient();
				
				
				
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

	}

	public void BtnInitialization() {
		word.setText("제시어");
		btnSend.setDisable(false);
		txtTextField.setEditable(true);
		txtTextField.setDisable(false);
		btnGameStart.setDisable(false);
		txtTextArea.setEditable(false);
		canvas.setDisable(true);
		arPt.removeAll(arPt);
		btnStrColorBlack.setDisable(true);
		btnStrColorBlue.setDisable(true);
		btnStrColorRed.setDisable(true);
		btnExit.setDisable(false);
		if (GameWaitRoomController.makeRoom == false) {
			btnGameStart.setDisable(true);
			canvas.setDisable(true);
			btnStrColorBlack.setDisable(true);
			btnStrColorBlue.setDisable(true);
			btnStrColorRed.setDisable(true);
		}
	}

	// 게임시작버튼
	public void handlerBtnGameStartAction(ActionEvent e) {
		GameWaitRoomController.startGame = true;
		gdao = new GamerDAO();
		mrvoList = gdao.getGamer1andGamer2(GameWaitRoomController.roomName);
		Gamer2 = mrvoList.get(0).getGamer2(); // 방이름을 통해 유저2 가 null인지 판단.
		if (Gamer2 != null) {

			AlertDisplay.alertDisplay(5, "게임시작", "게임을 시작합니다!", "즐거운 플레이되세요!>_<");
			canvas.setDisable(false);
			btnStrColorBlack.setDisable(false);
			btnStrColorBlue.setDisable(false);
			btnStrColorRed.setDisable(false);

			// DB에 등록된 제시어 가져오기!
			startGetKeyWord();
			send(UserGameState.GAMER_GAMEROOM_ENTER_AND_START + "," + GameWaitRoomController.roomName + ","
					+ GamerLoginController.UserId + "," + ">>>>게임이 시작되었습니다!<<<<\n" + "," + keyWord + "," + sence + ","
					+ Win + "," + Play);
			// 방상태 업데이트 gameRun
			int i = gdao.MakeRoomUpdateState(GameWaitRoomController.roomName);
			if (i == 1) {
				// 테이블 업데이트용 메세지 전송
				send("roomStateUpdate" + "," + UserGameState.GAMER_WAITROOM);

				btnGameStart.setDisable(true);
				txtTextField.setDisable(true);
			}
		} else {
			AlertDisplay.alertDisplay(1, "게임시작", "게임시작을 할수 없습니다.", "다른 유저가 올때까지 기다려야합니다...ㅠㅠㅠ");
		}

	}

	// 제시어 가져오기!
	public void startGetKeyWord() {
		gdao = new GamerDAO();
		ArrayList<KeyWordVO> list = gdao.getKeyWord();
		int random = (int) (Math.random() * (list.size() - 1 + 1) + 0);
		keyWord = list.get(random).getKeyWord();
		word.setText(keyWord);
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
					case UserGameState.GAMER_GAMEROOM_ENTER_AND_START:
						txtTextArea.appendText(sendMessage[3]);
						gdao = new GamerDAO();
						mrlist = gdao.getGamer1andGamer2(GameWaitRoomController.roomName);
						Gamer1 = mrlist.get(0).getGamer1();
						Gamer2 = mrlist.get(0).getGamer2();
						System.out.println(Gamer1 + " " + Gamer2);

						if (!(sendMessage[2].equals(GamerLoginController.UserId))) {
							// 방장이 아닌 user2 상태에서
							word.setText("맞춰봥");

							keyWord = sendMessage[4];
							System.out.println("제시어 : " + keyWord);

						}
						break;
					case UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT:
						System.out.println("확인 : " + sendMessage[2]);
						if (sendMessage[1].equals(GameWaitRoomController.roomName)) {
							/*
							 * 그리기에 대한 메세지인 경우
							 */
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
								break;
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
								break;
							}
							switch (sendMessage[2]) {
							case "welcome2":
								BtnInitialization();
								txtTextArea.appendText(sendMessage[3]);
								if (GameWaitRoomController.makeRoom == false) {
									btnGameStart.setDisable(true);
									canvas.setDisable(true);
									btnStrColorBlack.setDisable(true);
									btnStrColorBlue.setDisable(true);
									btnStrColorRed.setDisable(true);
								}
								break;

							// 참여자가 나갈 시 상테 업데이트
							case "welcome2out":
								txtTextArea.appendText(sendMessage[3]);
								if (GameWaitRoomController.makeRoom == true) {
									try {
										handlerWelcom2OutDBUdateAction();
									} catch (Exception e) {
										System.out.println(e.toString());
										;
									}
								}
								break;
							case "reStart":
								txtTextArea.appendText(sendMessage[3]);
								try {
									BtnInitialization();
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								break;
							case "sence":
								txtTextArea.appendText(sendMessage[3]);
								try {
									handlerGamer1SencePlus();
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								break;
							default: // 정답이 아닌 방에서의 채팅
								txtTextArea.appendText(sendMessage[2] + " : " + sendMessage[3] + "\n");
								break;
							}

							// Gamer2가 정답일경우
							if (sendMessage[3].equals(keyWord)/* ||count<5 */) {
								/*
								 * 방장에게는 센스표의 득표찬스 참여자에게는 승리수 득표찬스, 플레이수 +1
								 */
								// if() count ==2 인 경우 동시에 send restart

								/*
								 * GameWaitRoomController.makeRoom true는 방장 GameWaitRoomController.makeRoom
								 * false는 참여자
								 */

								// 방장
								if (GameWaitRoomController.makeRoom) {
									send(userState + "," + GameWaitRoomController.roomName + ","
											+ ">>>>>정답을 맞추셨습니다. 그림그리기를 멈춥니다.<<<<<\n");
									// 사용자의 플레이수를 증가시
									handlerPlayUpdateDBAction();
									// GameWaitRoomController.startGame = false;

								}

								// 참여자
								if (!GameWaitRoomController.makeRoom) {
									// 방장에게 그림에대한 센스를 투표함
									AlertDisplay.alertDisplay(2, "정답", "정답입니다!", "센스표를 방장에게!");
									if (AlertDisplay.result.get() == ButtonType.OK) {
										send(userState + "," + GameWaitRoomController.roomName + "," + "sence" + ","
												+ ">>>>>참여자가 당신의 센스를 칭찬합니다.<<<<<\n");
									}
									// 사용자의 플레이수를 증가시
									handlerPlayUpdateDBAction();

									//맞춘 수를 업데이트
									handlerGamer2WinPlus();

									// 게임을 더 할 것인지 물어봄 OK : 게임룸 유지 / NO : 게임대기방 진입
									handlerEndGameEnterGamerAction();
								}

							} // end of 키워드를 맞춘경우
						}
						break;
					default: {
						/*
						 * 업데이트가 필요한 행위를 넣어요~
						 * 
						 * 예를 들면 테이블뷰 업뎃, 사용자 업뎃 등등 유저가 방을 나가게 되면 하는 일들
						 */
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

	private void handlerGamer1SencePlus() {
		if (GameWaitRoomController.makeRoom) {
			// 디비에 등록 확인하기
			int Gamer1PSencePlus = gdao.getGamer1SencePlus(GamerLoginController.UserId);
			if (Gamer1PSencePlus != 0) {
				AlertDisplay.alertDisplay(5, "Win 등록", "Win 등록성공!", "Win 등록했습니다!");
			} else {
				AlertDisplay.alertDisplay(1, "Win 실패", "Win 등록실패!", "Win 실패했습니다!");
			} // end of 디비에 등록 확인하기
		}

	}

	private void handlerGamer2WinPlus() {
		if (!GameWaitRoomController.makeRoom) {
			// 디비에 등록 확인하기
			int Gamer2PWinPlus = gdao.getGamer2WinPlus(GamerLoginController.UserId);
			if (Gamer2PWinPlus != 0) {
				AlertDisplay.alertDisplay(5, "Win 등록", "Win 등록성공!", "Win 등록했습니다!");
			} else {
				AlertDisplay.alertDisplay(1, "Win 실패", "Win 등록실패!", "Win 실패했습니다!");
			} // end of 디비에 등록 확인하기
		}

	}

	private void handlerPlayUpdateDBAction() {
		// 디비에 등록 확인하기
		int Gamer1Gamer2PlayPlus = gdao.getGamer1andGamer2PlayPlus(GamerLoginController.UserId);
		if (Gamer1Gamer2PlayPlus != 0) {
			AlertDisplay.alertDisplay(5, "Play 등록", "Play 등록성공!", "Play 등록했습니다!");
		} else {
			AlertDisplay.alertDisplay(1, "Play 실패", "Play 등록실패!", "Play 실패했습니다!");
		} // end of 디비에 등록 확인하기

	}

	// 게임을 더 할 것인지 물어봄 OK : 게임룸 유지 / NO : 게임대기방 진입
	private void handlerEndGameEnterGamerAction() {

		AlertDisplay.alertDisplay(2, "게임종료", "게임이 종료되었습니다.", "계속하시겠습니까?");
		if (AlertDisplay.result.get() == ButtonType.OK) {
			// 유저의 상태를 유지하고, restart상태를 보내서 드로잉 다시 셋팅하기
			send(userState + "," + GameWaitRoomController.roomName + "," + "reStart" + ","
					+ ">>>>>게임을 시작할 준비를 합니다.<<<<<\n");
		} else {
			send(userState + "," + GameWaitRoomController.roomName + "," + "welcome2out" + "," + ">>>>>"
					+ GamerLoginController.UserId + " 님이 " + GameWaitRoomController.roomName + "나가셨습니다.<<<<<\n");
			// 게임대기방 진입
			Parent gameRoomRoot = null;
			Stage gameRoomStage = null;
			try {
				gameRoomRoot = FXMLLoader.load(getClass().getResource("/View/GameWaitRoom.fxml"));
				Scene scene = new Scene(gameRoomRoot);
				gameRoomStage = new Stage();
				gameRoomStage.setTitle("게임대기방");
				gameRoomStage.setScene(scene);
				gameRoomStage.setResizable(false);
				((Stage) btnExit.getScene().getWindow()).close();
				gameRoomStage.show();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}

	}

	// 방장과 참여자가 나가기버튼을 통해 대기방진입시 디비업뎃
	private void handlerWelcom2OutDBUdateAction() {
		GameWaitRoomController.mmVO = new ManagerManagmentVO(GameWaitRoomController.roomName,
				UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT + "," + GameWaitRoomController.roomName, null,
				GamerLoginController.UserId, null, "Wait");
		GameWaitRoomController.usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
		int count = GameWaitRoomController.usdao.getUserGameRoomRegistration(GameWaitRoomController.mmVO.getRoomName(),
				GameWaitRoomController.mmVO.getThreadState(), GameWaitRoomController.mmVO.getManagerID(),
				GameWaitRoomController.mmVO.getMakeRoomUserID(), GameWaitRoomController.mmVO.getEnterRoomUserID(),
				GameWaitRoomController.mmVO.getGameRunOrWaitState());

		// 방장인 경우, 현재 방에 참여자가 아무도 없는지 확인하고 방을 삭제한다.
		if (GameWaitRoomController.makeRoom) {
			send("table_update,\n");
			GameWaitRoomController.mmVO = new ManagerManagmentVO(GameWaitRoomController.roomName,
					UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT + "," + GameWaitRoomController.roomName, null,
					GamerLoginController.UserId, null, "Wait");
			GameWaitRoomController.usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
			GameWaitRoomController.usdao.getUserGameRoomDelete(GameWaitRoomController.mmVO.getRoomName());
			
			send("table_update,\n");
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
//		String chatMessage = array[1] + " : " + array[2];
		return array;
	}

	/*
	 * 드로잉
	 * 
	 */

	/* 데이터베이스 관련 (관리자 모드때 사용할 수도 있음) */

	public void totalList() {
		Object[][] totalData;
		ArrayList<String> title;

		ArrayList<DrowInfoVO> list = null;

		DrowInfoVO drowInfoVO = null;
		DrowInfoDAO drowInfoDAO = new DrowInfoDAO();

		list = drowInfoDAO.getDrowTotal();

		if (list == null) {
			AlertDisplay.alertDisplay(1, "Error : DB ", "DB null", "Check");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			drowInfoVO = list.get(i);
			drowData.add(drowInfoVO);
		}
	}

	/* 그림 좌표를 데이터베이스에 등록하는 함수 (관리자 모드때 사용할 수도 있음) */
	public void totalListSaveDB(double x, double y, boolean draw, int color) {
		try {
			DrowInfoVO dvo = new DrowInfoVO(x, y, draw, color);

			drowInfoDAO = new DrowInfoDAO(); // 데이타베이스 테이블에 입력값을 입력하는 함수.
			int count = drowInfoDAO.getDrowRegiste(dvo);
			if (count != 0) {
				drowData.add(dvo);
				totalList();
			} else {
				throw new Exception("데이타 베이스 등록실패");
			}
			AlertDisplay.alertDisplay(5, "등록성공", "테이블등록성공", "축하축하~!");
		} catch (Exception e2) {
			// AlertDisplay.alertDisplay(1, "등록실패", "합계, 평균을 확인해주세요!", e2.toString());
			return;
		}

	}

	/*
	 * 캔버스에서 마우스 액션에 대한 함수
	 * 
	 * MOUSE_DRAGGED 상태만 그림 상태(true)로 list에 저장시킴 이 외의 상태에서는 그림 상태(false)로 list에 저장시킴
	 */
	public void handlerCuoserAction(MouseEvent event) {

		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			send(userState + "," + GameWaitRoomController.roomName + "," + "NoDrow," + event.getX() + "," + event.getY()
					+ "," + 0 + "," + color);
		}
		if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			send(userState + "," + GameWaitRoomController.roomName + "," + "Drow," + event.getX() + "," + event.getY()
					+ "," + 1 + "," + color);
		}
		if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			System.out.println(userState + "," + GameWaitRoomController.roomName + "," + "X : " + event.getX()
					+ ", Y : " + event.getY() + " No Dorw " + color);
		}
	}

	/* 캔버스 하단의 버튼 3개를 누를 시 펜 색깔이 바뀜 */
	public void handlerBtnColorRedAction(MouseEvent event) {
		color = 1;
	}

	public void handlerBtnColorBlueAction(MouseEvent event) {
		color = 2;
	}

	public void handlerBtnColorBlackAction(MouseEvent event) {
		color = 3;
	}

	public void handlerCanvasResetAction() {
		drowData = null;
	}
}

/*
 * 그림그리기에 사용되는 클래스들
 * 
 * DrawCanvas (실제캔버스에 그림을 그리기 위한 라인이 만들어지는 곳) arPt : 마우스커서의 좌표 리스트 paint : 내장되어
 * 있는 함수들을 모아서 함수화 한 것 for문에서 true값인 좌표들을 선별해서 그려지도록 함
 * 
 * DrowInfoVO : 그리는 정보에 대한 값에 대한 클래스 x, y, 그린행위에 대해서 boolean 값, color : int 값으로
 * 저장됨 getDBColor() : case문, 색깔을 리턴해줌
 * 
 */
class DrawCanvas extends Canvas {
	ArrayList<DrowInfoVO> arPt;

	DrawCanvas(ArrayList<DrowInfoVO> arPt) {
		this.arPt = arPt;
	}

	public void paint(GraphicsContext g) {
		g.setLineWidth(2.0);

		for (int i = 0; i < arPt.size() - 1; i++) {
			if (arPt.get(i).isDraw()) {
				int color = arPt.get(i).getDBColor();
				Color penColor = arPt.get(i).getColor(color);
				g.setStroke(penColor);
				g.strokeLine(arPt.get(i).getX(), arPt.get(i).getY(), arPt.get(i + 1).getX(), arPt.get(i + 1).getY());
			}
		}
	}
}
