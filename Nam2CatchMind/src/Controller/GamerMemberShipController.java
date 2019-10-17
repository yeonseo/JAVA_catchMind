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

import Model.UserVO;
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
	@FXML TextField membershipId;
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
	
<<<<<<< HEAD
	private String selectFileName = ""; // �̹��� ���ϸ�
	private String localUrl = ""; // �̹��� ���� ���
    private Image localImage;
	private File selectedFile = null;
	private File dirSave = new File("C://��ä��/java/java_img"); //�̹��� ������ ������ �Ű������� ���� ��ü ����
	UserVO gvo;
    
=======
	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
    private Image localImage;
	private File selectedFile = null;
	private File dirSave = new File("C://남채현/java/java_img"); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	GamerVO gvo;
	GamerDAO gdao;
>>>>>>> pr/3
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
<<<<<<< HEAD
		btnRegister.setOnAction(e->{      handlerBtnRegisterAction(e);          });
		btnImg.setOnAction(e->{  handlerBtnImgFileAction(e);    }); // �̹��� ����
=======
		btnRegister.setOnAction(e->{      handlerBtnRegisterAction(e);          }); //회원가입!
		btnImg.setOnAction(e->{  handlerBtnImgFileAction(e);    }); // 이미지 선택
>>>>>>> pr/3
	}
	//회원가입
	public void handlerBtnRegisterAction(ActionEvent e) {
		
<<<<<<< HEAD
		
		// �̹��� ���� ���� ����
		File dirMake = new File(dirSave.getAbsolutePath());
		if (!dirMake.exists()) {
			dirMake.mkdir();
		}
		// �̹��� ���� ����
		String fileName = imageSave(selectedFile);
		System.out.println("fileName:"+selectedFile);
		gvo=new UserVO(memberShipId.getText(), memberShipPwd.getText(),
		          genderGrup.getSelectedToggle().getUserData().toString(),fileName); // DB�� ���̵�� �н����� ���� �̹����� ����!
=======
		try {
			// 이미지 저장 폴더 생성
			File dirMake = new File(dirSave.getAbsolutePath());
			if (!dirMake.exists()) {
				dirMake.mkdir();
			}
			// 이미지 파일 저장
			String fileName = imageSave(selectedFile);
			System.out.println("fileName:"+selectedFile);
			gvo=new GamerVO(membershipId.getText(), memberShipPwd.getText(),
			          genderGrup.getSelectedToggle().getUserData().toString(),fileName); // DB에 아이디와 패스워드 성별 이미지를 DAO에게!
			
			gdao=new GamerDAO(); //DAO의 객체를 부름
			int count=gdao.getGamerRegistration(gvo); //DAO에 gvo객체를 넣어줌!	
			if(count!= 0) {
				DBUtil.alertDisplay(3,"등록", "등록성공!", "회원가입이 되었습니다!^^즐거운 플레이되세요!^^");
				Stage stage=(Stage)btnExit.getScene().getWindow(); 
				stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!
		}else {
			throw new Exception("데이터베이스 등록실패!");
		}
>>>>>>> pr/3
		
		} catch (Exception e2) {
			DBUtil.alertDisplay(1,"등록", "등록실패!", "등록에 실패하셨습니다 ㅠㅠ");
		}
	}
	/*
<<<<<<< HEAD
	 *  >>>> �̹��� ����
	 *  �̹�������Ŭ������ ���� selectedFile �ּҰ��� �޾ƿ��� �� �ּҰ��� localUrl�� ������.
	 *  localUrl�� Image�� �Ű������� �־��ְ� �̹�����ü�� ����.
	 *  imageView.setImage() �Լ��� �� �̹��� ��ü�� �־���!
=======
	 *  >>>> 이미지 선택
	 *  이미지츄져클래스로 인해 selectedFile 주소값을 받아오고 그 주소값을 localUrl로 저장함.
	 *  localUrl을 Image의 매개변수에 넣어주고 이미지객체를 만듬.
	 *  imageView.setImage() 함수에 그 이미지 객체를 넣어줌!
>>>>>>> pr/3
	 * public void handlerBtnImageFileAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
			try {
				selectedFile = fileChooser.showOpenDialog(btnOk.getScene().getWindow());
				if (selectedFile != null) {
<<<<<<< HEAD
					// �̹��� ���� ���
=======
					// 이미지 파일 경로
>>>>>>> pr/3
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
	
	
<<<<<<< HEAD
	>>> �̹��� ����
	������ �̹��� ����(selecteFile) �� �ŰԺ�����  ���� �̹����� �����Ѵ�.
	��ǲ��Ʈ�� �ƿ�ǲ ��Ʈ������!!!
=======
	>>> 이미지 저장
	선택한 이미지 파일(selecteFile) 로 매게변수로  보내 이미지를 저장한다.
	인풋스트림 아웃풋 스트림으로!!!
>>>>>>> pr/3
	public String imageSave(File file1) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
<<<<<<< HEAD
			// �̹��� ���ϸ� ����
=======
			// 이미지 파일명 생성
>>>>>>> pr/3
			fileName = "student" + System.currentTimeMillis() + "_" + file1.getName();
			bis = new BufferedInputStream(new FileInputStream(file1));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

<<<<<<< HEAD
			// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
=======
			// 선택한 이미지 파일 InputStream의 마지막에 이르렀을 경우는 -1
>>>>>>> pr/3
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
<<<<<<< HEAD
		return fileName; //<< String ���� ���ϰ�  
	}
	 *
	 *>> selctedFile��  String fileName = imgeSave(selctedFile)�� �־��ְ� file Name��  GamerDAO �Ű������� !!
	 * 
	 */
	//�̹��� �ʱ�ȭ �⺻�̹���
	public void imageViewInit() {
		localUrl = "/images/gamerBasicImg.jpg";
		localImage = new Image(localUrl, false);
		System.out.println("�⺻�̹��� ���� : "+localImage);
		gamerImg.setImage(localImage);
		
	}
	//�̹��� ����!
=======
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
>>>>>>> pr/3
	 public void handlerBtnImgFileAction(ActionEvent e) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(btnRegister.getScene().getWindow());
					if (selectedFile != null) {
<<<<<<< HEAD
						// �̹��� ���� ���
						localUrl = selectedFile.toURI().toURL().toString(); //���õ� �̹��� ���� ��θ� ����
					}
					if (selectedFile != null) {
						selectFileName = selectedFile.getName();//���õ� �̹��� �̸�!! �� ������.
					}
				} catch (MalformedURLException e1) {
					AlertDisplay.alertDisplay(1, "��������", "������ ã�� �� �����ϴ�.", "���õ� �̹��� ���ϰ�� ����, ���õ� �̹��� �̸� ���� ����"+e1.toString());
				}
				localImage = new Image(localUrl, false); //�̹��� ��ü
=======
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
>>>>>>> pr/3
				gamerImg.setImage(localImage);
				gamerImg.setFitHeight(250);
				gamerImg.setFitWidth(230);				
		}
		
<<<<<<< HEAD
	 //�̹��� ����  
	 public String imageSave(File file) {
=======
	 //이미지 저장  
	 public String imageSave(File file1) {
>>>>>>> pr/3
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			int data = -1;
			String fileName = null;
			try {
<<<<<<< HEAD
				// �̹��� ���ϸ� ����
				fileName = "gamerUser" + System.currentTimeMillis() + "_" + file.getName();
				bis = new BufferedInputStream(new FileInputStream(file)); //������ ���� �̹����� �о��.
				bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName)); //�̹����� ����!

				// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
=======
				// 이미지 파일명 생성
				fileName = "gamerUser" + System.currentTimeMillis() + "_" + file1.getName();
				bis = new BufferedInputStream(new FileInputStream(file1)); //선택한 파일 이미지를 읽어옴.
				bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName)); //이미지를 보냄!

				// 선택한 이미지 파일 InputStream의 마지막에 이르렀을 경우는 -1
>>>>>>> pr/3
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
<<<<<<< HEAD
			return fileName; //<< String ���� ���ϰ�  
=======
			return fileName; //<< String 으로 리턴값  
>>>>>>> pr/3
		}
	

}
