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
	
	private String selectFileName = ""; // �̹��� ���ϸ�
	private String localUrl = ""; // �̹��� ���� ���
    private Image localImage;
	private File selectedFile = null;
	private File dirSave = new File("C://��ä��/java/java_img"); //�̹��� ������ ������ �Ű������� ���� ��ü ����
	UserVO gvo;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		btnRegister.setOnAction(e->{      handlerBtnRegisterAction(e);          });
		btnImg.setOnAction(e->{  handlerBtnImgFileAction(e);    }); // �̹��� ����
	}
	public void handlerBtnRegisterAction(ActionEvent e) {
		
		
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
		
	}
	/*
	 *  >>>> �̹��� ����
	 *  �̹�������Ŭ������ ���� selectedFile �ּҰ��� �޾ƿ��� �� �ּҰ��� localUrl�� ������.
	 *  localUrl�� Image�� �Ű������� �־��ְ� �̹�����ü�� ����.
	 *  imageView.setImage() �Լ��� �� �̹��� ��ü�� �־���!
	 * public void handlerBtnImageFileAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
			try {
				selectedFile = fileChooser.showOpenDialog(btnOk.getScene().getWindow());
				if (selectedFile != null) {
					// �̹��� ���� ���
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
	
	
	>>> �̹��� ����
	������ �̹��� ����(selecteFile) �� �ŰԺ�����  ���� �̹����� �����Ѵ�.
	��ǲ��Ʈ�� �ƿ�ǲ ��Ʈ������!!!
	public String imageSave(File file1) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// �̹��� ���ϸ� ����
			fileName = "student" + System.currentTimeMillis() + "_" + file1.getName();
			bis = new BufferedInputStream(new FileInputStream(file1));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

			// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
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
	 public void handlerBtnImgFileAction(ActionEvent e) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
				try {
					selectedFile = fileChooser.showOpenDialog(btnRegister.getScene().getWindow());
					if (selectedFile != null) {
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
				gamerImg.setImage(localImage);
				gamerImg.setFitHeight(250);
				gamerImg.setFitWidth(230);				
		}
		
	 //�̹��� ����  
	 public String imageSave(File file) {
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			int data = -1;
			String fileName = null;
			try {
				// �̹��� ���ϸ� ����
				fileName = "gamerUser" + System.currentTimeMillis() + "_" + file.getName();
				bis = new BufferedInputStream(new FileInputStream(file)); //������ ���� �̹����� �о��.
				bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName)); //�̹����� ����!

				// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
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
			return fileName; //<< String ���� ���ϰ�  
		}
	

}
