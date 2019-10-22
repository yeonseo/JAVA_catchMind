package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import Model.UserGameHistroryVO;
import Model.UserStateVO;
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

	@FXML Label word;
	@FXML Canvas canvas;
	@FXML TextArea txtTextArea;
	@FXML TextField txtTextField;
	@FXML Button btnStrColorBlack;
	@FXML Button btnStrColorRed;
	@FXML Button btnStrColorBlue;
	@FXML Button btnSend;
	@FXML Button btnGameStart;
	@FXML Button btnExit;
	String Gamer1;
	String Gamer2;
	String keyWord; //랜덤 단어 
	
	String userState = UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT;
	
	GamerDAO gdao;
	ArrayList<MakeRoomVO> mrlist;
	ArrayList<MakeRoomVO> mrvoList;
	ObservableList <DrowInfoVO> drowData;
	UserGameHistroryVO ughvo;
	UserGameHistroryVO ughvo2;
	int Play=0;
	int Gamer1Sence=0;
	boolean sence=true;
	int Win=0;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		btnSend.setDisable(false);
		
		
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtTextArea.appendText("[Chat Start] \n");
			//방장에 이름으로 방이름 보내기
			send("welcome2"+","+GamerLoginController.UserId+","+"님이"+GameWaitRoomController.roomName+"에 입장하셨습니다.\n");
		});
		//방장 버튼, 캔버스 초기화
		BtnInitialization();
	
		
		// 게임시작 버튼!
		btnGameStart.setOnAction(e->{  handlerBtnGameStartAction(e);   });
			
		
		
		//메시지 시작 버튼!
		btnSend.setOnAction(event -> {
			// 메세지 전송시자신의 상태외 아이디, 메세지를 함께 보냄
			
			/*
			 * 여기부터
			 * > A < 게임방에서는 방이름까지 합쳐서 던저주는 걸로 바꿨어용~~~~
			 * 
			 * */
			send(userState + "," + GameWaitRoomController.roomName + "," + GamerLoginController.UserId + "," + txtTextField.getText() + ","+"\n");
			System.out.println(userState + "," + GameWaitRoomController.roomName + "," + GamerLoginController.UserId + "," + txtTextField.getText());
			/**여기까지 바꿨어용~~~~~~**/
			txtTextField.setText("");
			txtTextField.requestFocus();
			
		
		
		});
		
		btnExit.setOnAction(e -> {
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
				stopClient();
				gameRoomStage.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		});
		
		
	}
	public void BtnInitialization() {
		btnGameStart.setDisable(false);
		txtTextArea.setEditable(false);
		canvas.setDisable(true);
		btnStrColorBlack.setDisable(true);
		btnStrColorBlue.setDisable(true);
		btnStrColorRed.setDisable(true);
	}
	//게임시작버튼
	public void handlerBtnGameStartAction(ActionEvent e) {
		GameWaitRoomController.startGame=true;
		gdao=new GamerDAO();
		mrvoList=gdao.getGamer1andGamer2(GameWaitRoomController.roomName);
		Gamer2=mrvoList.get(0).getGamer2(); //방이름을 통해 유저2 가  null인지 판단.
		if(Gamer2 !=null) {	
			
				AlertDisplay.alertDisplay(5,"게임시작" ,"게임을 시작합니다!", "즐거운 플레이되세요!>_<");
				canvas.setDisable(false);
				btnStrColorBlack.setDisable(false);
				btnStrColorBlue.setDisable(false);
				btnStrColorRed.setDisable(false);
				
				
				//DB에 등록된 제시어 가져오기!		
				startGetKeyWord();
				send(UserGameState.GAMER_GAMEROOM_ENTER_AND_START 
						+","+GameWaitRoomController.roomName+","
						+GamerLoginController.UserId +","
						+">>>>게임이 시작되었습니다!<<<<\n"+","+keyWord+","+sence+","+Win+","+Play);
				//방상태 업데이트 gameRun
				int i=gdao.MakeRoomUpdateState(GameWaitRoomController.roomName);
				if(i==1) {
					AlertDisplay.alertDisplay(5,"방상태 업데이트" ,"방상태 업데이트 성공", "방상태 업데이트 성공!");
					//테이블 업데이트용 메세지 전송
					send("roomStateUpdate"+","+UserGameState.GAMER_WAITROOM);
				}else {
					AlertDisplay.alertDisplay(1,"방상태업데이트오류" ,"방상태 업데이트 오류!", "방상태 등록오류!");
				}
				
				btnGameStart.setDisable(true);
				txtTextField.setDisable(true);
				
			
		}else {
			AlertDisplay.alertDisplay(1,"게임시작" ,"게임시작을 할수 없습니다.", "다른 유저가 올때까지 기다려야합니다...ㅠㅠㅠ");
		}
		
	}

	//제시어 가져오기!
	public void startGetKeyWord() {
		gdao=new GamerDAO();
		ArrayList<KeyWordVO>list=gdao.getKeyWord();
		int random=(int)(Math.random()*(list.size()-1+1)+0);
		keyWord=list.get(random).getKeyWord();
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
					
					case "welcome2" : txtTextArea.appendText(sendMessage[1]+sendMessage[2]);	
						if(GameWaitRoomController.hideGameStartBtn==true) {
							btnGameStart.setDisable(true);
							canvas.setDisable(true);
							btnStrColorBlack.setDisable(true);
							btnStrColorBlue.setDisable(true);
							btnStrColorRed.setDisable(true);
						}
						break;
					case UserGameState.GAMER_GAMEROOM_ENTER_AND_START : 		
						txtTextArea.appendText(sendMessage[3]);
						gdao=new GamerDAO();
						mrlist=gdao.getGamer1andGamer2(GameWaitRoomController.roomName);
						Gamer1=mrlist.get(0).getGamer1();
						Gamer2=mrlist.get(0).getGamer2();				
//						Gamer1Play++;	// 방장의 플레이	
//						ughvo=new UserGameHistroryVO(Gamer1, Play,0);
//						int Gamer1Play=gdao.getGamer1andGamer2Result(ughvo);
//						 if(Gamer1Play!=0) {
//							 AlertDisplay.alertDisplay(5,"Gamer1 Play등록" ,"Gamer1 Play등록성공!", "Gamer1 Play등록했습니다!");
//						 }else {
//							 AlertDisplay.alertDisplay(1,"Gamer1 Play실패" ,"Gamer1 Play등록실패!", "Gamer1 Play실패했습니다!");
//						 }
						System.out.println(Gamer1+" "+Gamer2);
//						System.out.println("방장의 play수 : "+Gamer1Play);	
						
						if(!(sendMessage[2].equals(GamerLoginController.UserId ))) {
							//방장이 아닌  user2 상태에서 
							word.setText("맞춰봥");
//							ughvo2=new UserGameHistroryVO(Gamer2, Play);
//							 int Gamer2Play=gdao.getGamer1andGamer2Result(ughvo2);
//							 if(Gamer2Play!=0) {
//								 AlertDisplay.alertDisplay(5,"Gamer2 Play등록" ,"Gamer2 Play등록성공!", "Gamer2 Play등록했습니다!");
//							 }else {
//								 AlertDisplay.alertDisplay(1,"Gamer2 Play실패" ,"Gamer2 Play등록실패!", "Gamer2 Play실패했습니다!");
//							 }
							keyWord=sendMessage[4];
							System.out.println("제시어 : "+keyWord);
							
						}
						break;
					case UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT:
						System.out.println("확인 : " + sendMessage[2]);
						if(sendMessage[1].equals(GameWaitRoomController.roomName)) {
							
							//Gamer2가 정답일경우
							if(sendMessage[3].equals(keyWord)) {
								/*
								 * 방장에게는 센스표의 득표찬스
								 * 참여자에게는 승리수 득표찬스, 플레이수 +1
								 * */
								
								/*
								 * 
								 * */
								if(sendMessage[2].equals(GamerLoginController.UserId)) {
									ughvo2=new UserGameHistroryVO(Gamer2, Win);
									 int Gamer2Win=gdao.getGamer1andGamer2Result(ughvo2);
									 if(Gamer2Win!=0) {
										 AlertDisplay.alertDisplay(5,"Gamer2 Win등록" ,"Gamer2 Win등록성공!", "Gamer2 Win등록했습니다!");
									 }else {
										 AlertDisplay.alertDisplay(1,"Gamer2 Win실패" ,"Gamer2 Win등록실패!", "Gamer2 Win실패했습니다!");
									 }
//									Gamer2Win++;
									//Gamer2인경우
//									System.out.println(GamerLoginController.UserId+"님의 win"+Gamer2Win);
									AlertDisplay.alertDisplay(2, "정답", "정답입니다!", "센스표를 방장에게!");
									if (AlertDisplay.result.get() == ButtonType.OK) {
										sence=true;
										//센스표 업뎃
									}
									/*
									 * OK : 게임룸 유지 / NO : 게임대기방 진입
									 * */
									AlertDisplay.alertDisplay(2, "게임종료", "게임이종료되었습니다.", "계속하시겠습니까?");
									if (AlertDisplay.result.get() == ButtonType.OK) {
										ughvo=new UserGameHistroryVO(Gamer1, Play);
										ughvo2=new UserGameHistroryVO(Gamer2, Play);
//										System.out.println(ughvo.getUserID()+" "+ughvo.getWin()+" "+ughvo.getPlay());
//										System.out.println(ughvo2.getUserID()+" "+ughvo2.getWin()+" "+ughvo2.getPlay());
									   int GamerOnePlay=gdao.getGamer1andGamer2Result(ughvo);
									   int Gamer2Play=gdao.getGamer1andGamer2Result(ughvo2);
									   if(GamerOnePlay!=0 && Gamer2Play!=0) {
										   AlertDisplay.alertDisplay(5,"Gamer1,Gamer2 play등록" ,"Gamer1,Gamer2 play등록성공!", "Gamer1,Gamer2 play등록했습니다!");
									   }else {
										   AlertDisplay.alertDisplay(1,"Gamer1,Gamer2 play등록실패" ,"Gamer1,Gamer2 play등록실패", "Gamer1,Gamer2 play실패했습니다!");
									   }
										
									} else {
										//게임대기방 진입
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
							            	AlertDisplay.alertDisplay(1, "게임 대기방 진입 실패", "게임 대기방을 불러오는데 실패했습니다.", e.toString());
										}
									}
								}else {
									//방장일경우
									AlertDisplay.alertDisplay(5,"정답" ,"정답 맞춤!", "상대방이 정답을 맞췄습니다.");
									System.out.println(sence);
									if(sence==true) {
										System.out.println(sence);
										Gamer1Sence=gdao.getGamer1Sence(Gamer1);
										if(Gamer1Sence==1) {
											AlertDisplay.alertDisplay(5,"센스표" ,"센스표등록 완료!", "센스표를 받았습니다!");
										}else {
											AlertDisplay.alertDisplay(1,"센스표" ,"센스표등록 실패!", "센스표를 받지못했습니다.");
										}
									}
									
									
								}
								
							}else {
												
							}
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
								txtTextArea.appendText(sendMessage[2]+" : "+sendMessage[3] +"\n");
							}
						}
						break;
					default : {
						/*
						 * 업데이트가 필요한 행위를 넣어요~
						 * 
						 * 예를 들면 테이블뷰 업뎃, 사용자 업뎃 등등 유저가 방을 나가게 되면 하는 일들
						 * */
						
						
						
						
						
						
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
//		String chatMessage = array[1] + " : " + array[2];
		return array;
	}

	
	/*
	 * 드로잉
	 * 
	 * */
	
	/*그림을 그리기 위한 변수
	 * 
	 * (데이터 베이스를 염두해 두어서 데이터 베이스의 형식으로 네이밍&만들어짐)
	 * arPt 좌표 리스트
	 * DrowInfoDAO 좌표값을 DB에 저장하는 클래스
	 * boolean, color를 위한 변수
	 * */
	static ArrayList<DrowInfoVO> arPt = new ArrayList<DrowInfoVO>();
	private DrowInfoDAO drowInfoDAO;
	static boolean down = false;
	static int color = 0;
	

	/* 데이터베이스 관련 (관리자 모드때 사용할 수도 있음)*/
	
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
	
	/*그림 좌표를 데이터베이스에 등록하는 함수 (관리자 모드때 사용할 수도 있음)*/
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
	
	
	/* 캔버스에서 마우스 액션에 대한 함수
	 * 
	 * MOUSE_DRAGGED 상태만 그림 상태(true)로 list에 저장시킴
	 * 이 외의 상태에서는 그림 상태(false)로 list에 저장시킴*/
	public void handlerCuoserAction(MouseEvent event) {
		
		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			send(userState + "," + GameWaitRoomController.roomName + "," + "NoDrow," + event.getX() + "," + event.getY() + "," + 0 + "," + color);
		}
		if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			send(userState + "," + GameWaitRoomController.roomName + "," + "Drow," + event.getX() + "," + event.getY() + "," + 1 + "," + color);
		}
		if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			System.out.println(userState + "," + GameWaitRoomController.roomName + "," + "X : " + event.getX() + ", Y : " + event.getY() + " No Dorw " + color);
		}
	}
	
	/*캔버스 하단의 버튼 3개를 누를 시 펜 색깔이 바뀜*/
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
		drowData=null;
	}
}

/* 그림그리기에 사용되는 클래스들
 * 
 * DrawCanvas (실제캔버스에 그림을 그리기 위한 라인이 만들어지는 곳)
 * 	arPt : 마우스커서의 좌표 리스트
 * 	paint : 내장되어 있는 함수들을 모아서 함수화 한 것
 * 			for문에서 true값인 좌표들을 선별해서 그려지도록 함
 * 	
 * DrowInfoVO : 그리는 정보에 대한 값에 대한 클래스
 * 		x, 
 * 		y,
 * 		그린행위에 대해서 boolean 값,
 * 		color : int 값으로 저장됨
 * 			getDBColor() : case문, 색깔을 리턴해줌
 * 
 * */
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
