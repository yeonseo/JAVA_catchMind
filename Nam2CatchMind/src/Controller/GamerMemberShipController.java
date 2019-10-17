package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.GamerVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class GamerMemberShipController implements Initializable {
	@FXML TextField memberShipId;
	@FXML PasswordField  memberShipPwd;
	@FXML PasswordField  memberShipCheckPwd;
	@FXML ImageView  gamerImg;
	@FXML Button btnIdCheck;
	@FXML Button btnImg;
	@FXML Button btnCheckPwd;
	@FXML Button btnRegister;
	@FXML Button btnExit;
	@FXML ToggleGroup genderGrup;
	@FXML RadioButton radioWoman;
	@FXML RadioButton radioMan;
	
	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
    private Image localImage;
	private File selectedFile = null;
	private File dirSave = new File("C://남채현/java/java_img"); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	GamerVO gvo;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		btnRegister.setOnAction(e->{      handlerBtnRegisterAction(e);          });
		btnImg.setOnAction(e->{  handlerBtnImgFileAction(e);    }); // 이미지 선택
	}
	public void handlerBtnRegisterAction(ActionEvent e) {
		
		
		// 이미지 저장 폴더 생성
		File dirMake = new File(dirSave.getAbsolutePath());
		if (!dirMake.exists()) {
			dirMake.mkdir();
		}
		// 이미지 파일 저장
		String fileName = imageSave(selectedFile);
		System.out.println("fileName:"+selectedFile);
		gvo=new GamerVO(memberShipId.getText(), memberShipPwd.getText(),
		          genderGrup.getSelectedToggle().getUserData().toString(),fileName); // DB에 아이디와 패스워드 성별 이미지를 보냄!
		
	}
	/*
	 *  >>>> 이미지 선택
	 *  이미지츄져클래스로 인해 selectedFile 주소값을 받아오고 그 주소값을 localUrl로 저장함.
	 *  localUrl을 Image의 매개변수에 넣어주고 이미지객체를 만듬.
	 *  imageView.setImage() 함수에 그 이미지 객체를 넣어줌!
	 * public void handlerBtnImageFileAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
			try {
				selectedFile = fileChooser.showOpenDialog(btnOk.getScene().getWindow());
				if (selectedFile != null) {
					// 이미지 파일 경로
					localUrl = selectedFile.toURI().toURL().toString();
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			localImage = new Image(localUrl, false);
			imageView.setImage(localImage);
			imageView.setFitHeight(250);
			imageView.setFitWidth(230);
			btnOk.setDisable(false);

			if (selectedFile != null) {
				selectFileName = selectedFile.getName();
			}
	}
	
	
	>>> 이미지 저장
	선택한 이미지 파일(selecteFile) 로 매게변수로  보내 이미지를 저장한다.
	인풋스트림 아웃풋 스트림으로!!!
	public String imageSave(File file1) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// 이미지 파일명 생성
			fileName = "student" + System.currentTimeMillis() + "_" + file1.getName();
			bis = new BufferedInputStream(new FileInputStream(file1));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

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
	 *
	 *>> selctedFile을  String fileName = imgeSave(selctedFile)로 넣어주고 file Name을  GamerDAO 매개변수로 !!
	 * 
	 */
	//이미지 초기화 기본이미지
	public void imageViewInit() {
		localUrl = "/images/gamerBasicImg.jpg";
		localImage = new Image(localUrl, false);
		System.out.println("기본이미지 셋팅 : "+localImage);
		gamerImg.setImage(localImage);
		
	}
	//이미지 선택!
	 public void handlerBtnImgFileAction(ActionEvent e) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(btnRegister.getScene().getWindow());
					if (selectedFile != null) {
						// 이미지 파일 경로
						localUrl = selectedFile.toURI().toURL().toString(); //선택된 이미지 파일 경로를 저장
					}
					if (selectedFile != null) {
						selectFileName = selectedFile.getName();//선택된 이미지 이름!! 도 저장함.
					}
				} catch (MalformedURLException e1) {
					DBUtil.alertDisplay(1, "사진오류", "사진을 찾을 수 없습니다.", "선택된 이미지 파일경로 오류, 선택된 이미지 이름 저장 오류"+e1.toString());
				}
				localImage = new Image(localUrl, false); //이미지 객체
				gamerImg.setImage(localImage);
				gamerImg.setFitHeight(250);
				gamerImg.setFitWidth(230);				
		}
		
	 //이미지 저장  
	 public String imageSave(File file) {
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			int data = -1;
			String fileName = null;
			try {
				// 이미지 파일명 생성
				fileName = "gamerUser" + System.currentTimeMillis() + "_" + file.getName();
				bis = new BufferedInputStream(new FileInputStream(file)); //선택한 파일 이미지를 읽어옴.
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
	

}
