package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import Model.UserStateVO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import Model.DrowInfoVO;

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
	
	String userState = UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT;
	
	GamerDAO gdao;
	
	ObservableList<DrowInfoVO> drowData;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		btnSend.setDisable(false);
		
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtTextArea.appendText("[Chat Start] \n");
			//방장에 이름으로 방이름 보내기
			send("welcome2"+","+GamerLoginController.UserId+","+"님이"+GameWaitRoomController.roomName+"에 입장하셨습니다.\n");
		});
		
		btnSend.setOnAction(event -> {
			// 메세지 전송시자신의 상태외 아이디, 메세지를 함께 보냄
			
			/*
			 * 여기부터
			 * > A < 게임방에서는 방이름까지 합쳐서 던저주는 걸로 바꿨어용~~~~
			 * 
			 * */
			send(userState + "," + GameWaitRoomController.roomName + "," + GamerLoginController.UserId + "," + txtTextField.getText() + "\n");
			System.out.println(userState + "," + GameWaitRoomController.roomName + "," + GamerLoginController.UserId + "," + txtTextField.getText());
			/**여기까지 바꿨어용~~~~~~**/
			txtTextField.setText("");
			txtTextField.requestFocus();
		});
		
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
					
					case "welcome2" : txtTextArea.appendText(sendMessage[1]+sendMessage[2]); break;
					
					case UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT:
						System.out.println("확인 : " + sendMessage[2]);
						
						if(sendMessage[1].equals(GameWaitRoomController.roomName)) {
							
							
							
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
										txtTextArea.appendText(sendMessage[2]+" : "+sendMessage[3]);
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
