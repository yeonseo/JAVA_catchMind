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
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import Model.MakeRoomVO;
import Model.UserRankingVO;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

	@FXML 
	TableView<MakeRoomVO> roomInfo;
	ObservableList<MakeRoomVO>gameMakeRoomdata; 
	 private int selectedIndex;
	 private ObservableList<MakeRoomVO> selectMakeRoom; 
	 public static String roomName;
	 
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

	//회원 정보 수정창
		PasswordField userPreviousPwd;
		PasswordField userChangePwd;
		PasswordField userChangeCheckPwd;
		Button btnUserPreviousCheckPwd;
		Button btnUserCheckPwd;
		Button btnCorrection;
		Button btnExit;
		Label GamerId;
		String findPreviousPwd=null;
		private boolean previousPwdCheck=false; // 등록하는 순간 기존패스워드 확인버튼을 눌렀는지 체크 
		private boolean changePwdCheck=false; //등록하는 순간 변경할 비밀번호 체크혹인 버튼을 눌렀는지 체크
		//이미지 찾기(회원 정보 수정창)
		Button btnChangeImage;
		ImageView userChangeImage;
		private String selectFileName = ""; 
		String fileName ;
		
//		private File dirSave = new File("C://남채현/java/java_img"); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
		String path = System.getProperty("user.dir")+"/images/";
		private File dirSave = new File(path); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	
		public static boolean hideGameStartBtn=false;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnUserSend.setDisable(false);
		
		startClient("localhost", 9876);
		Platform.runLater(() -> {
			txtChatArea.appendText("[Chat Start] \n");
			send("welcome1"+","+GamerLoginController.UserId+","+"님이 대기방에 입장하셨습니다. \n");
		});
		
		btnUserSend.setOnAction(event -> {
			// 메세지 전송시자신의 상태외 아이디, 메세지를 함께 보냄
			send(userState + "," + GamerLoginController.UserId + "," + txtInputMessage.getText() + "\n");
			txtInputMessage.setText("");
			txtInputMessage.requestFocus();
		});
		//테이블 뷰 셋팅
		tableViewSetting();
		
		// 로그인된 게임대기방 이미지와 아이디 보이기
		getUserIDandUserImage();
		
		//처음 대기방 들어갈떄 만들어진 방 보이기
		TableViewTotalMakeRoomName();
		
		//만들어진 방 더블클릭시 조건(방상태가 GameRun상태일때 경고창 띄우기 or 아님)
		roomInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		          if(event.getClickCount()>1) {
		        		selectedIndex=roomInfo.getSelectionModel().getSelectedIndex();
		        		selectMakeRoom=roomInfo.getSelectionModel().getSelectedItems();
		        		String roomState=selectMakeRoom.get(0).getGameRunOrWaitState();
		        		roomName=selectMakeRoom.get(0).getRoomName();
		        		String user2Name=selectMakeRoom.get(0).getGamer2();
		        		if(roomState.equals("Wait")) {
		        			if(user2Name!=null) {
								AlertDisplay.alertDisplay(1,"방입장실패" ,"방에 입장하실 수 없습니다.", "인원이 꽉찼습니다.다른 방을 이용해주세요ㅠ^ㅠ");
	        					return;
	        				}
		        			gdao=new GamerDAO();
		        			int i=gdao.getUser2EnterGameRoom(userId, roomName);//wait 상태에 있는방에 DB에 이름 저장
		        			if(i==1) {
		        				AlertDisplay.alertDisplay(5,"방입장" ,"user2방 DB등록 성공", "wait상태에 있는방에 user2이름을 등록하는데 성공!");	
		        				gameMakeRoomdata.removeAll(gameMakeRoomdata); //기존에 있던 데이터 지우고
								TableViewTotalMakeRoomName();	//다시 테이블뷰에 부름.!
		        				((Stage)btnGameRoomExit.getScene().getWindow()).close();
		        				//게임방 부르기
		        				GameRoomStart();
		        				hideGameStartBtn=true;
		        				send("welcome2"+UserGameState.GAMER_GAMEROOM_ENTER_AND_WAIT+GamerLoginController.UserId+"님이 "+","+roomName+"방에 입장하셨습니다. \n");
		        				
		        			}else {
		        				AlertDisplay.alertDisplay(1,"방입장오류" ,"방입장오류", "wait상태에 있는방에 user2이름을 등록하지 못했습니다.");
		        			}
		        				
		        			
		        		}else {
		        			AlertDisplay.alertDisplay(1,"방입장실패" ,"방에 입장하실 수 없습니다.", "인원이 꽉찼습니다.다른 방을 이용해주세요ㅠ^ㅠ");
		        			
		        		}
		        	  
		          }
		     	}
		     });
		
	
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
				send(UserGameState.GAMER_OFFLINE);
				txtChatArea.appendText("[Chat Out] \n");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				stopClient();
				Platform.exit();
			});

		});
	}	


	// 테이블 뷰 셋팅
	public void tableViewSetting() {
		gameMakeRoomdata=FXCollections.observableArrayList();
		
		roomInfo.setEditable(false);
		
		TableColumn colMakeRoomName=new TableColumn("방이름");
		colMakeRoomName.setMaxWidth(300.0);
		colMakeRoomName.setStyle("-fx-alignment: CENTER;");
		colMakeRoomName.setCellValueFactory(new PropertyValueFactory("roomName"));
		
		TableColumn colMakeRoomUser=new TableColumn("방장");
		colMakeRoomUser.setMaxWidth(80.0);
		colMakeRoomUser.setStyle("-fx-alignment: CENTER;");
		colMakeRoomUser.setCellValueFactory(new PropertyValueFactory("Gamer1"));
		
		TableColumn colGameUser=new TableColumn("참여자");
		colGameUser.setMaxWidth(80.0);
		colGameUser.setStyle("-fx-alignment: CENTER;");
		colGameUser.setCellValueFactory(new PropertyValueFactory("Gamer2"));
		
		TableColumn colRoomState=new TableColumn("방상태");
		colRoomState.setMaxWidth(80.0);
		colRoomState.setStyle("-fx-alignment: CENTER;");
		colRoomState.setCellValueFactory(new PropertyValueFactory("GameRunOrWaitState"));
		
		roomInfo.setItems(gameMakeRoomdata);
		roomInfo.getColumns().addAll(colRoomState, colMakeRoomUser, colGameUser, colMakeRoomName);
		
		
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
//																											// 이름의 url
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

	// 내정보 수정하기(모달창)
		public void handlerBtnMyInfoChangeAtion(ActionEvent e) {
			Parent MyInfoChangeRoot = null;
			Stage MyInfoChangeStage = null;
			try {
				MyInfoChangeRoot = FXMLLoader.load(getClass().getResource("/View/MyInfoChange.fxml"));
				MyInfoChangeStage=new Stage(StageStyle.UTILITY);
				MyInfoChangeStage.initModality(Modality.WINDOW_MODAL);
				MyInfoChangeStage.initOwner(btnMyInfoChange.getScene().getWindow());
				MyInfoChangeStage.setTitle("내정보수정");
				MyInfoChangeStage.setResizable(false);
				
				userChangeImage=(ImageView) MyInfoChangeRoot.lookup("#userChangeImage");
				userPreviousPwd=(PasswordField)MyInfoChangeRoot.lookup("#userPreviousPwd");
				userChangePwd=(PasswordField)MyInfoChangeRoot.lookup("#userChangePwd");
				userChangeCheckPwd=(PasswordField)MyInfoChangeRoot.lookup("#userChangeCheckPwd");
				btnUserPreviousCheckPwd=(Button)MyInfoChangeRoot.lookup("#btnUserPreviousCheckPwd");
				btnChangeImage=(Button)MyInfoChangeRoot.lookup("#btnChangeImage");
				btnUserCheckPwd=(Button)MyInfoChangeRoot.lookup("#btnUserCheckPwd");
				btnCorrection=(Button)MyInfoChangeRoot.lookup("#btnCorrection");
				btnExit=(Button)MyInfoChangeRoot.lookup("#btnExit");
				GamerId=(Label)MyInfoChangeRoot.lookup("#userId");
				
				//로그인된 아이디
				GamerId.setText(userId);
				
				//기존 이미지 가져오기
				userChangeImage.setImage(userImg);
						  
				//기존 이미지 바꾸기
				btnChangeImage.setOnAction(e1->{       handlerBtnChangeImageAction(e1);        });
						
				//기존 비밀번호 체크확인
				btnUserPreviousCheckPwd.setOnAction(e1->{     handlerBtnUserPreviousCheckPwdAction(e1);      });
				
				//변경 비밀번호 체크확인
				btnUserCheckPwd.setOnAction(e1->{     handlerbtnUserCheckPwdAction(e1);      });
				
				//전체 정보 저장 (비밀번호 and 이미지)			
				btnCorrection.setOnAction(e1->{      handlerBtnCorrectionAction(e1);        });
						
				//나가기
				btnExit.setOnAction(e1->{  		
					((Stage) btnExit.getScene().getWindow()).close();
				});
				
				Scene scene = new Scene(MyInfoChangeRoot);
				MyInfoChangeStage.setScene(scene);
				MyInfoChangeStage.show();
				
				
			} catch (IOException e1) {
				AlertDisplay.alertDisplay(1, "내정보수정 창 가져오기 오류", "내정보수정 창 가져오기 오류", e1.toString());
			}

		}
		//전체 정보 저장 (비밀번호 and 이미지)	
		public void handlerBtnCorrectionAction(ActionEvent e1) {
			if(userPreviousPwd.getText().equals("") || userChangePwd.getText().equals("")
					|| userChangeCheckPwd.getText().equals("")  || localUrl==null) {
					
					AlertDisplay.alertDisplay(1, "정보미기입", "정보 미기입", "미기입된 정보가 있습니다. 다시 확인해주세요!");			
					return;
				}
				if(previousPwdCheck!=true) {
					AlertDisplay.alertDisplay(1, "기존비밀번호 확인", "기존 비밀번호 확인 요망!", "기존 비밀번호 확인 버튼을 눌러 확인해주세요!");
					return;
				}
				if(changePwdCheck !=true) {
					AlertDisplay.alertDisplay(1, "변경 비밀번호 확인", "변경할 비밀번호 확인 요망!", "변경할 비밀번호 확인 버튼을 눌러 "
							+ "비밀번호를 다시 확인해주세요!");
					return;
				}
				gdao=new GamerDAO();
				
				int i=gdao.getUserPwdChange(userChangePwd.getText(), userId);
				if(i==1 && previousPwdCheck==true && changePwdCheck==true) {		
					
					if(userChangePwd.getText().equals(userChangeCheckPwd.getText())) {
						
						
						changUserImage(); 	//이미지 DB 으로 저장
						AlertDisplay.alertDisplay(5, "내정보수정", "내 정보 수정 성공!", "이미지와 비밀번호를 수정하였습니다.");
						((Stage)btnExit.getScene().getWindow()).close();
						getUserIDandUserImage();
											
						
					}else {
						AlertDisplay.alertDisplay(1, "비밀번호확인", "비밀번호가 다릅니다,", "비밀번호를 다시 확인해주세요!");
					}	
					
				}else {
					AlertDisplay.alertDisplay(1, "내정보수정 실패", "내 정보 수정 실패", "이미지와 비밀번호를 수정하는데 실패했습니다.");
				}
		}
		//변경 비밀번호 체크확인
		public void handlerbtnUserCheckPwdAction(ActionEvent e1) {
			if(userChangePwd.getText().equals("") || userChangeCheckPwd.getText().equals("")) {
				AlertDisplay.alertDisplay(1, "비밀번호확인", "변경 비밀번호 미입력!", "바꿀 비밀번호를 입력해주세요!");
				return;
			}
			if(userChangePwd.getText().equals(findPreviousPwd)){
				AlertDisplay.alertDisplay(1, "비밀번호확인", "기존에 비밀번호와 중복됩니다.", "바꿀 비밀번호를 입력해주세요!");
				return;
			}
			if(userChangePwd.getText().equals(userChangeCheckPwd.getText())) {
				AlertDisplay.alertDisplay(5, "비밀번호확인", "비밀번호 일치합니다!", "비밀번호 변경이 가능합니다.");
				changePwdCheck=true;
			}else {
				AlertDisplay.alertDisplay(1, "비밀번호확인", "비밀번호가 다릅니다,", "비밀번호를 다시 확인해주세요!");
			}	
		}
		//기존 비밀번호 체크확인
		public  void handlerBtnUserPreviousCheckPwdAction(ActionEvent e1) {

			gdao=new GamerDAO();
			findPreviousPwd=gdao.getUserPreviousCheck(userId);
			if(userPreviousPwd.getText().equals("") ) {
				AlertDisplay.alertDisplay(1, "비밀번호확인", "변경 비밀번호 미입력!", "바꿀 비밀번호를 입력해주세요!");
				return;
			}
			if(userPreviousPwd.getText().equals(findPreviousPwd)) {
				AlertDisplay.alertDisplay(5, "기존비밀번호확인", "기존 비밀번호 확인", "비밀번호 변경이 가능합니다.");
				previousPwdCheck=true; // 기존 비밀번호를 체크를 해야 비밀번호를 바꿀 수 있다.
			}else {
				AlertDisplay.alertDisplay(1, "기존 비밀번호확인", "기존에 비밀번호와 맞지않습니다.", "다시 확인해주세요!");
			}
			
		}
		// 기존 이미지 바꾸기
		public void handlerBtnChangeImageAction(ActionEvent e1) {
			//이미지 선택
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif", "*.jpeg"));
				try {
					selectedFile = fileChooser.showOpenDialog(btnChangeImage.getScene().getWindow());
					if (selectedFile != null) {
						// 이미지 파일 경로
						localUrl = selectedFile.toURI().toURL().toString(); //선택된 이미지 파일 경로를 저장
						selectFileName = selectedFile.getName();//선택된 이미지 이름!! 도 저장함.

					}else {
						AlertDisplay.alertDisplay(1, "이미지오류","이미지 선택요망!", "이미지를 선택해주세요!");
					}
					userImg = new Image(localUrl, false); //이미지 객체
					userChangeImage.setImage(userImg);
					userChangeImage.setFitHeight(350);
					userChangeImage.setFitWidth(350);	
					
				} catch (Exception e2) {
					AlertDisplay.alertDisplay(1, "이미지오류", "이미지를 찾기, 수정 오류!", "선택된 이미지 파일경로 오류, 선택된 이미지 이름 저장 오류"+e1.toString());
				}
				
				
				
				
		}
		//이미지 저장
		public String imageSave(File file1) {
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;

					int data = -1;
					String fileName = null;
					try {
						// 이미지 파일명 생성
						fileName = "changeImage" + System.currentTimeMillis() + "_" + file1.getName();
						bis = new BufferedInputStream(new FileInputStream(file1)); //선택한 파일 이미지를 읽어옴.
						bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName)); //이미지를 보냄!

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
					return fileName; //<< String 으로 리턴값
				}
		//이미지 DB로 저장
		public void changUserImage() {
					try {
						File dirMake = new File(dirSave.getAbsolutePath());
						if (!dirMake.exists()) {
							dirMake.mkdir();
						}
						// 이미지 파일 저장
						fileName = imageSave(selectedFile);
						System.out.println("fileName :"+selectedFile);
						System.out.println(userPreviousPwd.getText());
						System.out.println(fileName);
						gdao =new GamerDAO();

							gdao.getUserImageChange(fileName, userPreviousPwd.getText());
						} catch (Exception e1) {
							AlertDisplay.alertDisplay(1, "이미지오류", "이미지등록 오류!", "선택된 이미지 이름 저장 오류"+e1.toString());
						}
						
					
				}
		
		
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 게임방 만들기

	public static ManagerManagmentVO mmVO;
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
							roomName=txtMakeRoomName.getText();
	
							
							//등록한 방이름,방장 테이블뷰에 나타내기!
							gameMakeRoomdata.removeAll(gameMakeRoomdata); //기존에 있던 데이터 지우고
							TableViewTotalMakeRoomName();	//다시 테이블뷰에 부름.!		
							stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!
							stopClient();
							((Stage)btnGameRoomExit.getScene().getWindow()).close(); //대기방 창 닫기	
							//게임방 부르기
							GameRoomStart();
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
	//등록한 방이름,방장 전체  테이블뷰에 나타내기!
	public void TableViewTotalMakeRoomName() {
		ArrayList<MakeRoomVO> list=null;
		GamerDAO gdao=new GamerDAO();
		MakeRoomVO mrvo=null;
		list=gdao.getMakeRoomName();
		if(list==null) {
			AlertDisplay.alertDisplay(1, "테이블뷰 실패", "테이블뷰 등록 실패", "테이블뷰 등록 실패");
			return;
		}
		for(int i=0;i<list.size();i++) {
			mrvo=list.get(i);
			gameMakeRoomdata.add(mrvo);
		}
	}

	//게임방 부르기!
	public  void GameRoomStart() {
		Parent gameRoomRoot=null;
		Stage GameRoomstage=null;
		try {
			gameRoomRoot=FXMLLoader.load(getClass().getResource("/View/GameRoom.fxml"));
			Scene scene =new Scene(gameRoomRoot);
			GameRoomstage=new Stage();
			GameRoomstage.setTitle("캐치마인드");
			GameRoomstage.setResizable(false);
			GameRoomstage.setScene(scene);
			GameRoomstage.show();
			
		} catch (Exception e1) {
			AlertDisplay.alertDisplay(1, "게임방불러오기 오류", "게임방 불러오기 실패", e1.toString());
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
					case "welcome1" : txtChatArea.appendText(sendMessage[1]+sendMessage[2]); break;
					case "roomStateUpdate" : 
						gameMakeRoomdata.removeAll(gameMakeRoomdata); //기존에 있던 데이터 지우고
						TableViewTotalMakeRoomName();	//다시 테이블뷰에 부름.!		
						break;
					case UserGameState.GAMER_WAITROOM : txtChatArea.appendText(sendMessage[1]+" : "+sendMessage[2]); break;
					default : {
						/*
						 * 업데이트가 필요한 행위를 넣어요~
						 * 
						 * 예를 들면 테이블뷰 업뎃, 사용자 업뎃 등등 유저가 방을 나가게 되면 하는 일들
						 * */
						gameMakeRoomdata.removeAll(gameMakeRoomdata); //기존에 있던 데이터 지우고
						TableViewTotalMakeRoomName();	//다시 테이블뷰에 부름.!
						
						
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
		return array ;
	}


}
