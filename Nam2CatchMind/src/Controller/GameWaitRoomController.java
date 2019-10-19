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

import Model.UserRankingVO;
import Model.UserVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameWaitRoomController implements Initializable {
	  
	@FXML Label gameStartTime;
	@FXML Label gameUserId;
	@FXML ImageView gamerImg;
	@FXML Button btnMakeRoom;
	@FXML Button btnMyRecord;
	@FXML Button btnMyInfoChange;
	@FXML Button btnUserSend;
	@FXML Button btnGameRoomExit;
//	@FXML TableView<UserGameRoomVO> roomInfo;
	@FXML TableView<UserRankingVO> userRanking;
	@FXML TextArea txtChatArea;
	@FXML TextField txtInputMessage;
	 String userId;
	GamerDAO gdao;
	UserVO uvo;
	ArrayList<UserVO> userIdAndImagList;
	private File selectedFile = null;
	private String localUrl = "";
	Image userImg;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		/**********추가시작***********/
		btnUserSend.setDisable(false);

		btnUserSend.setOnAction(event -> {
			send(GamerLoginController.UserId +" : "+txtInputMessage.getText() + "\n");
			txtInputMessage.setText("");
			txtInputMessage.requestFocus();
		});
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtChatArea.appendText("[Chat Start] \n");
		});

		/**********추가끝***********/
		

		//로그인된 게임대기방 이미지와 아이디 보이기
		getUserIDandUserImage();	
		  
		//내정보수정하기
		btnMyInfoChange.setOnAction(e->{ handlerBtnMyInfoChangeAtion(e); });
		//나의 전적
		btnMyRecord.setOnAction(e->{  });
		//게임방만들기
		btnMakeRoom.setOnAction(e->{  handlerBtnMakeRoomAction(e);   });
		//나가기
		btnGameRoomExit.setOnAction(e->{  
			
			Platform.runLater(() -> {
				txtChatArea.appendText("[Chat Out] \n");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				txtInputMessage.setDisable(true);
				btnUserSend.setDisable(true);
				stopClient();
				Platform.exit();
			});

			
		});
	}
	//로그인된 게임대기방 이미지와 아이디 보이기
	public void getUserIDandUserImage() {
		userId=GamerLoginController.UserId;
		 System.out.println("로그인 들어온 아이디 : "+userId);
		  gdao=new GamerDAO();
		  gameUserId.setText(userId);
		  
		  try {
			  	String fileName=gdao.getUserLoginIdAndImage(userId); // 저장한 이미지의 파일 이름
				selectedFile=new File("C://남채현/java/java_img/"+fileName); //저장한 이미지의 파일 이름의 url
				if(selectedFile != null){ 
					//저장한 파일의 이름의 url이 null값이 아니라면!
					localUrl =selectedFile.toURI().toURL().toString();
					userImg =new Image(localUrl, false); 
					gamerImg.setImage(userImg);
					gamerImg.setFitHeight(250);
					gamerImg.setFitWidth(230);
					  System.out.println("들어온 image : "+userImg);
			}
		  } catch (Exception e1) {
			  AlertDisplay.alertDisplay(1, "로그인된 아이디 이미지 가져오기 실패", "이미지 가져오기 실패!", e1.toString());
		  }	  
		
	}
	//내정보 수정하기
	public void handlerBtnMyInfoChangeAtion(ActionEvent e) {
		Parent MyInfoChangeRoot=null;
		Stage MyInfoChangeStage=null;
		try {
			MyInfoChangeRoot=FXMLLoader.load(getClass().getResource("/View/MyInfoChange.fxml"));
			Scene scene=new Scene(MyInfoChangeRoot);
			MyInfoChangeStage=new Stage();
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
	//게임방 만들기
	public void handlerBtnMakeRoomAction(ActionEvent e) {
		Parent makeGameRoomRoot=null;
		Stage makeRoomStage=null;
		try {
			makeGameRoomRoot =FXMLLoader.load(getClass().getResource("/View/MakeRoom.fxml"));
			Scene scene=new Scene(makeGameRoomRoot);
			makeRoomStage=new Stage();
			makeRoomStage.setTitle("방만들기");
			makeRoomStage.initModality(Modality.WINDOW_MODAL);
			makeRoomStage.initOwner(btnMakeRoom.getScene().getWindow());
			makeRoomStage.setResizable(false);
			makeRoomStage.setScene(scene);
			makeRoomStage.show();
		} catch (IOException e1) {
			  AlertDisplay.alertDisplay(1, "방 만들기 오류", "방 만들기 오류", e1.toString());
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
					if (message.startsWith("NoDrow")) {
						txtChatArea.appendText("안 그릴꺼얏!!\n");
						
					} else if (message.startsWith("Drow")) {
						txtChatArea.appendText("그릴꺼얏!!\n");
					} else {
						txtChatArea.appendText(message);
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

	
	
	

}
