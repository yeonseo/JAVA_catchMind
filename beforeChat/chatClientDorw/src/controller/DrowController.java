package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.DrowInfoVO;


public class DrowController implements Initializable {
	
	@FXML
	private Canvas canDrow;
	@FXML
	private Button btnStrColorRed;
	@FXML
	private Button btnStrColorBlue;
	@FXML
	private Button btnStrColorBlack;
	@FXML
	private TextField txtUserStrName;
	@FXML
	private TextField txtSocketIP;
	@FXML
	private TextField txtSocketPort;
	@FXML
	private Button btnServerEnter;
	@FXML
	private TextArea txtAreaChat;
	@FXML
	private TextField txtFieldUserInput;
	@FXML
	private Button btnUserInput;
	
	ObservableList<DrowInfoVO> data;
	
	/**/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		txtUserStrName.setPromptText("Enter your name!");
		
		txtSocketIP.setText("localhost");
		txtSocketPort.setText("9876");
		
		txtAreaChat.setEditable(false);
		txtFieldUserInput.setDisable(true);
		
		txtFieldUserInput.setOnAction(event -> {
			send(txtUserStrName.getText() + " : " + txtFieldUserInput.getText() + "\n");
			txtFieldUserInput.setText("");
			txtFieldUserInput.requestFocus();
		});
		
		btnUserInput.setText("Send");
		btnUserInput.setDisable(true);
		
		btnUserInput.setOnAction(event -> {
			send(txtUserStrName.getText() + " : " + txtFieldUserInput.getText() + "\n");
			txtFieldUserInput.setText("");
			txtFieldUserInput.requestFocus();
		});
		
		btnServerEnter.setText("Enter");
		btnServerEnter.setOnAction(event -> {
			if (btnServerEnter.getText().equals("Enter")) {
				int port = 9876;
				try {
					port = Integer.parseInt(txtSocketPort.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				startClient(txtSocketIP.getText(), port);
				Platform.runLater(() -> {
					txtAreaChat.appendText("[Chat Start] \n");
				});
				
				btnServerEnter.setText("Exit");
				txtFieldUserInput.setDisable(false);
				btnUserInput.setDisable(false);
				txtFieldUserInput.requestFocus();
			} else {
				stopClient();
				Platform.runLater(() -> {
					txtAreaChat.appendText("[Chat Out] \n");
				});
				btnServerEnter.setText("Enter");
				txtFieldUserInput.setDisable(true);
				btnUserInput.setDisable(true);
				
			}
		});
	}
	
	
	
	
	
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
	
	
	
	
	
	/* 통신관련 
	 * 
	 * startClient : 클라이언트가 전달한 정보를 가지고 스레드&소켓 생성
	 * recive : 메세지를 받아서 처리하는 함
	 * 		Drow/NoDrow : 캔버스에서 활동할 시 붙어지는 키워드
	 * 		(위의 방식으로 키워드를 이용해 여러가지 활동을 추가할 수 있음)
	 * 		채팅메시지는 키워드가 없음
	 * send : 메세지를 보내는 함수. 이 함수를 사용해서 서버로 메세지를 보냄
	 * 
	 * stopClient
	 * 
	 * */
	
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
			
			/*특정 사용자에게 그리기 권한을 주는 부분 (함수화 할 예정)
			 *캔버스를 사용할 수 없게 함
			 * (기능추가 가능함, 추가할 예)
			 **/
			if (txtUserStrName.getText().equals("aaa")) {
				canDrow.setDisable(false);
				
			} else {
				canDrow.setDisable(true);
			}
			
			
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[512];
				int length = in.read(buffer);
				if (length == -1)
					throw new IOException();
				String message = new String(buffer, 0, length, "UTF-8");
				Platform.runLater(() -> {
					if (message.startsWith("NoDrow")) {
						txtAreaChat.appendText("안 그릴꺼얏!!\n");
						String drowMessage = message;
						String[] array = drowMessage.split(",");
						double x = Double.parseDouble(array[1]);
						double y = Double.parseDouble(array[2]);
						boolean draw = false;
						int color = Integer.parseInt(array[4]);
						
						arPt.add(new DrowInfoVO(x, y, draw, color));
						
						DrawCanvas drow = new DrawCanvas(arPt);
						GraphicsContext g = canDrow.getGraphicsContext2D();
						
						drow.paint(g);
					} else if (message.startsWith("Drow")) {
						txtAreaChat.appendText("그릴꺼얏!!\n");
						String drowMessage = message;
						String[] array = drowMessage.split(",");
						double x = Double.parseDouble(array[1]);
						double y = Double.parseDouble(array[2]);
						boolean draw = true;
						int color = Integer.parseInt(array[4]);
						
						DrawCanvas drow = new DrawCanvas(arPt);
						GraphicsContext g = canDrow.getGraphicsContext2D();
						arPt.add(new DrowInfoVO(x, y, draw, color));
						drow.paint(g);
					} else {
						txtAreaChat.appendText(message);
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
	
	
	
	
	
	/* 데이터베이스 관련 (안씀 필요없음)*/
	
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
			data.add(drowInfoVO);
		}
	}
	
	/*그림 좌표를 데이터베이스에 등록하는 함수 (안씀 필요없음)*/
	public void totalListSaveDB(double x, double y, boolean draw, int color) {
		try {
			DrowInfoVO dvo = new DrowInfoVO(x, y, draw, color);
			
			drowInfoDAO = new DrowInfoDAO(); // 데이타베이스 테이블에 입력값을 입력하는 함수.
			int count = drowInfoDAO.getDrowRegiste(dvo);
			if (count != 0) {
				data.add(dvo);
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
			System.out.println("X : " + event.getX() + ", Y : " + event.getY() + " No Dorw " + color);
			send("NoDrow," + event.getX() + "," + event.getY() + "," + 0 + "," + color);
		}
		if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			txtAreaChat.appendText("X : " + event.getX() + ", Y : " + event.getY() + " Dorw " + color + "\n");
			send("Drow," + event.getX() + "," + event.getY() + "," + 1 + "," + color);
		}
		if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			System.out.println("X : " + event.getX() + ", Y : " + event.getY() + " No Dorw " + color);
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