package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Model.MakeRoomVO;
import Model.UserRankingVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
	@FXML
	TableView<MakeRoomVO> roomInfo;
	@FXML
	TableView<UserRankingVO> userRanking;
	@FXML
	TextArea txtChatArea;
	@FXML
	TextField txtInputMessage;

	GamerLoginController gamerLoginController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
/**********추가***********/
		btnUserSend.setText("Send");
		btnUserSend.setDisable(false);

		btnUserSend.setOnAction(event -> {
			send(txtInputMessage.getText() + "\n");
			txtInputMessage.setText("");
			txtInputMessage.requestFocus();
		});
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtChatArea.appendText("[Chat Start] \n");
		});

		/**********추가***********/
		
		// 나의 전적
		btnMyRecord.setOnAction(e -> {
		});
		// 게임방만들기
		btnMakeRoom.setOnAction(e -> {
			handlerBtnMakeRoomAction(e);
		});
		// 나가기
		
		
		
		
		
		
		/**********추가***********/
		btnGameRoomExit.setOnAction(e -> {
			
			Platform.runLater(() -> {
				txtChatArea.appendText("[Chat Out] \n");
			});
			txtInputMessage.setDisable(true);
			btnUserSend.setDisable(true);
			stopClient();
			Platform.exit();
		});
		
		/**********추가***********/
		

	}

	// 게임방 만들기
	public void handlerBtnMakeRoomAction(ActionEvent e) {
		Parent makeGameRoomRoot = null;
		Stage makeRoomStage = null;
		try {
			makeGameRoomRoot = FXMLLoader.load(getClass().getResource("/View/MakeRoom.fxml"));
			Scene scene = new Scene(makeGameRoomRoot);
			makeRoomStage = new Stage();
			makeRoomStage.setTitle("방만들기");
			makeRoomStage.initModality(Modality.WINDOW_MODAL);
			makeRoomStage.initOwner(btnMakeRoom.getScene().getWindow());
			makeRoomStage.setResizable(false);
			makeRoomStage.setScene(scene);
			makeRoomStage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
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