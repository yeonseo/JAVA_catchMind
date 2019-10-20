package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.UserVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class MyInfoChangeController implements Initializable {
	
	@FXML ImageView userChangeImage;
	@FXML PasswordField userPreviousPwd;
	@FXML PasswordField userChangePwd;
	@FXML PasswordField userChangeCheckPwd;
	@FXML Button btnUserPreviousCheckPwd;
	@FXML Button btnChangeImage;
	@FXML Button btnUserCheckPwd;
	@FXML Button btnCorrection;
	@FXML Button btnExit;
	@FXML Label userId;
	String previousPwd=GamerLoginController.UserId;
	String findPreviousPwd=null;
	GamerDAO gdao;
	UserVO uvo;
	GameWaitRoomController gameWaitRoomController;
	private String selectFileName = ""; // 이미지 파일명
	private File selectedFile = null;
	private String localUrl = "";
	
	/********************/
	Parent gameRoomRoot = null;
	Stage gameRoomStage = null;
	/********************/
	
	Image changeUserImg;
	String fileName ;
//	private File dirSave = new File("C://남채현/java/java_img"); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	String path = System.getProperty("user.dir")+"/images/";
	private File dirSave = new File(path); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	
	private boolean previousPwdCheck=false; // 등록하는 순간 기존패스워드 확인버튼을 눌렀는지 체크 
	private boolean changePwdCheck=false; //등록하는 순간 변경할 비밀번호 체크혹인 버튼을 눌렀는지 체크
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//로그인된 아이디
		userId.setText(previousPwd);
		
		//기존 이미지 가져오기
		getUserImage();
		  
		//기존 이미지 바꾸기
		btnChangeImage.setOnAction(e->{    handlerBtnChangeImageAction(e);         });
		
		//기존 비밀번호 체크확인
		btnUserPreviousCheckPwd.setOnAction(e->{     handlerBtnUserPreviousCheckPwdAction(e);      });
		
		//변경 비밀번호 체크확인
		btnUserCheckPwd.setOnAction(e->{     handlerbtnUserCheckPwdAction(e);      });
		
		//전체 정보 저장 (비밀번호)
		
		btnCorrection.setOnAction(e->{      handlerBtnCorrectionAction(e);        });
		
		//나가기
		btnExit.setOnAction(e->{  		
			((Stage) btnExit.getScene().getWindow()).close();
			returnGameWaiteRoom();
		});
	
		
	}
	//전체정보 저장(비밀번호)
	public void handlerBtnCorrectionAction(ActionEvent e) {
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
		
		int i=gdao.getUserPwdChange(userChangePwd.getText(), previousPwd);
		if(i==1 && previousPwdCheck==true && changePwdCheck==true) {
		
			changUserImage(); 	//이미지 DB 으로 저장
			
			AlertDisplay.alertDisplay(5, "내정보수정", "내 정보 수정 성공!", "이미지와 비밀번호를 수정하였습니다.");
			
			/***************************************************/
			//다시 gameWaitRoom을 불러오기 ....... (대기방 이미지 안바껴서 차선책ㅠㅠㅠㅠㅠ)
			returnGameWaiteRoom();

			/***************************************************/
		
		}else {
			AlertDisplay.alertDisplay(1, "내정보수정 실패", "내 정보 수정 실패", "이미지와 비밀번호를 수정하는데 실패했습니다.");
		}
		
	}
	
	//기존 이미지 바꾸기
	public void handlerBtnChangeImageAction(ActionEvent e) {
		//이미지 선택
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif", "*.jpeg"));
			try {
				selectedFile = fileChooser.showOpenDialog(btnCorrection.getScene().getWindow());
				if (selectedFile != null) {
					// 이미지 파일 경로
					localUrl = selectedFile.toURI().toURL().toString(); //선택된 이미지 파일 경로를 저장
					selectFileName = selectedFile.getName();//선택된 이미지 이름!! 도 저장함.

				}else {
					AlertDisplay.alertDisplay(1, "이미지오류","이미지 선택요망!", "이미지를 선택해주세요!");
				}
				changeUserImg = new Image(localUrl, false); //이미지 객체
				userChangeImage.setImage(changeUserImg);
				userChangeImage.setFitHeight(350);
				userChangeImage.setFitWidth(350);	
				
			} catch (Exception e1) {
				AlertDisplay.alertDisplay(1, "이미지오류", "이미지를 찾기, 수정 오류!", "선택된 이미지 파일경로 오류, 선택된 이미지 이름 저장 오류"+e1.toString());
			}
			
			

		
	}
	//회원 기존이미지 가져오기
	public void getUserImage() {
		gdao=new GamerDAO();	  
		  try {
			  	String fileName=gdao.getUserLoginIdAndImage(previousPwd); // 저장한 이미지의 파일 이름
				selectedFile=new File("C://남채현/java/java_img/"+fileName); //저장한 이미지의 파일 이름의 url
				if(selectedFile != null){ 
					//저장한 파일의 이름의 url이 null값이 아니라면!
					localUrl =selectedFile.toURI().toURL().toString();
					changeUserImg =new Image(localUrl, false); 
					userChangeImage.setImage(changeUserImg);
					userChangeImage.setFitHeight(350);
					userChangeImage.setFitWidth(350);
					  System.out.println("들어온 image : "+changeUserImg);
					}	  
				}catch (Exception e1) {
				  AlertDisplay.alertDisplay(1, "로그인된 아이디 이미지 가져오기 실패", "이미지 가져오기 실패!", e1.toString());
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
				System.out.println(previousPwd);
				System.out.println(fileName);
				gdao =new GamerDAO();

					gdao.getUserImageChange(fileName, previousPwd);
				} catch (Exception e1) {
					AlertDisplay.alertDisplay(1, "이미지오류", "이미지등록 오류!", "선택된 이미지 이름 저장 오류"+e1.toString());
				}
				
			
		}
		
	//기존 비밀번호 체크 
	public void handlerBtnUserPreviousCheckPwdAction(ActionEvent e) {
		
		gdao=new GamerDAO();
		findPreviousPwd=gdao.getUserPreviousCheck(previousPwd);
		if(userPreviousPwd.getText().equals("") ) {
			AlertDisplay.alertDisplay(1, "비밀번호확인", "변경 비밀번호 미입력!", "바꿀 비밀번호를 입력해주세요!");
			return;
		}
		if(userPreviousPwd.getText().equals(findPreviousPwd)) {
			AlertDisplay.alertDisplay(5, "기존비밀번호확인", "기존 비밀번호 확인", "비밀번호 변경이 가능합니다.");
			previousPwdCheck=true; // 기존 비밀번호를 체크를 해야 비밀번호를 바꿀 수 있다.
		}else {
			AlertDisplay.alertDisplay(1, "비밀번호확인", "기존에 비밀번호와 맞지않습니다.", "다시 확인해주세요!");
		}
		
	}
	
	//바꾼 비밀번호 체크
	public void handlerbtnUserCheckPwdAction(ActionEvent e) {
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

	//게임 대기방 다시 불러옴
		public void returnGameWaiteRoom() {
			try {
				gameRoomRoot = FXMLLoader.load(getClass().getResource("/View/GameWaitRoom.fxml"));
				Scene scene = new Scene(gameRoomRoot);
				gameRoomStage = new Stage();
				gameRoomStage.setTitle("게임대기방");
				gameRoomStage.setScene(scene);
				gameRoomStage.setResizable(false);
				((Stage) btnCorrection.getScene().getWindow()).close();
				gameRoomStage.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
}
