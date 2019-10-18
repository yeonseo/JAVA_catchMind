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
	@FXML
	TextField membershipId;
	@FXML
	PasswordField memberShipPwd;
	@FXML
	PasswordField memberShipCheckPwd;
	@FXML
	ImageView gamerImg;
	@FXML
	Button btnIdCheck;
	@FXML
	Button btnImg;
	@FXML
	Button btnCheckPwd;
	@FXML
	Button btnRegister;
	@FXML
	Button btnExit;
	@FXML
	ToggleGroup toggleGrup;
	@FXML
	RadioButton radioWoman;
	@FXML
	RadioButton radioMan;

	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	private File selectedFile = null;
//	private File dirSave = new File("C://남채현/java/java_img"); //이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("/Users/nambbo/Documents/Backup_CatchMind/membership_image"); // 이미지 저장할 폴더를 매개변수로
	UserVO uvo;
	GamerDAO gdao;
	boolean idcheck = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnImg.setOnAction(e -> {
			handlerBtnImgFileAction(e);
		}); // 이미지 선택
		btnIdCheck.setOnAction(e -> {
			handlerbtnIdCheckAction(e);
		}); // 아이디 중복 검사
		btnCheckPwd.setOnAction(e -> {
			handlerBtnCheckPwdAciotn(e);
		});
		btnRegister.setOnAction(e -> {
			handlerBtnRegisterAction(e);
		}); // 회원가입 등록!
		btnExit.setOnAction(e -> {
			handlerBtnExitAtion(e);
		});
	}

	// 회원가입
	public void handlerBtnRegisterAction(ActionEvent e) {
		try {
			// 등록 누르는 순간 다시 체크!
			if (memberShipPwd.getText().equals("") || memberShipPwd.getText().equals("")
					|| memberShipCheckPwd.getText().equals("") || localUrl.equals("")) {
				AlertDisplay.alertDisplay(1, "미입력 확인", "미입력된 부분이있습니다!", "아이디 와 패스워드 이미지 다시 확인해주세요!");
				return;
			}

			// 등록버튼 누를때 비밀번호 다시 확인 . 이미지 다시 확인, 아이디 다시 확인
			if (memberShipPwd.getText().equals(memberShipCheckPwd.getText()) && localUrl != null && idcheck != true) {

				// 이미지 저장 폴더 생성
				File dirMake = new File(dirSave.getAbsolutePath());
				if (!dirMake.exists()) {
					dirMake.mkdir();
				}
				// 이미지 파일 저장
				String fileName = imageSave(selectedFile);
				System.out.println("fileName:" + selectedFile);
				System.out.println(membershipId.getText().toString() + memberShipPwd.getText().toString());
				System.out.println(fileName);
				uvo = new UserVO(membershipId.getText(), memberShipPwd.getText(), null, fileName); // DB에 아이디와 패스워드 성별
																									// 이미지를 DAO에게!

				gdao = new GamerDAO(); // DAO의 객체를 부름
				int count = gdao.getGamerRegistration(uvo); // DAO에 gvo객체를 넣어줌!
				if (count != 0) {
					AlertDisplay.alertDisplay(3, "등록", "등록성공!", "회원가입이 되었습니다!^^즐거운 플레이되세요!^^");
					Stage stage = (Stage) btnExit.getScene().getWindow();
					stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!
				} else {
					throw new Exception("데이터베이스 등록실패!");
				}

			} else {
				AlertDisplay.alertDisplay(1, "등록오류", "패스워드가 불일치하거나 아이디가 중복되거나 선택된 이미지오류!",
						"아이디 및 패스워드 및 이미지를 다시 확인해주세요!");
			}

		} catch (Exception e2) {
			AlertDisplay.alertDisplay(1, "등록", "등록실패!", "등록에 실패하셨습니다 ㅠㅠ");
		}
	}

	// 아이디 중복검사
	public void handlerbtnIdCheckAction(ActionEvent e) {
		uvo = new UserVO(membershipId.getText()); // 아이디값 vo넣음
		gdao = new GamerDAO(); // gdao 객체 가져옴
		idcheck = gdao.checkUserID(uvo); // 아이디값 vo를 gdao.checkUserID()에 매개변수로 넣고 불리언 으로 true, false로 받는다.
		if (membershipId.getText().equals("")) {
			// 아디이값이 없을때
			AlertDisplay.alertDisplay(1, "아이디 미입력", "아이디 미입력.", "아이디를 입력해주세요!");
			return;
		}
		if (idcheck == true) {
			// 중복된 아이디일때
			AlertDisplay.alertDisplay(1, "아이디중복!", "아이디가 중복되었습니다.", "다른 아이디를 적어주세요!");
		} else {
			// 중복된 아이디가 없을때
			AlertDisplay.alertDisplay(5, "아이디확인", "아이디가 확인되었습니다.", "사용가능한 아이디입니다!");
		}
	}

	// 패스워드 중복검사
	public void handlerBtnCheckPwdAciotn(ActionEvent e) {
		if (memberShipPwd.getText().equals("") || memberShipCheckPwd.getText().equals("")) {
			// 패스워드 미입력이면 여기서 함수 끝냄.
			AlertDisplay.alertDisplay(1, "패스워드 미입력", "패스워드 미입력!", "패스워드를  입력해주세요!");
			return;
		}
		if (memberShipPwd.getText().equals(memberShipCheckPwd.getText())) {
			// 패스워드 같을때
			AlertDisplay.alertDisplay(5, "패스워드확인", "패스워드가 확인되었습니다.", "패스워드 사용가능합니다.");
		} else {
			// 패스워드 다를때
			AlertDisplay.alertDisplay(1, "패스워드 불일치", "패스워드가 불일치 합니다.", "패스워드를 다시 확인해주세요!");
		}

	}

	// 이미지 선택!
	public void handlerBtnImgFileAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif", "*.jpeg"));
		try {
			selectedFile = fileChooser.showOpenDialog(btnRegister.getScene().getWindow());
			if (selectedFile != null) {
				// 이미지 파일 경로
				localUrl = selectedFile.toURI().toURL().toString(); // 선택된 이미지 파일 경로를 저장
			}
			if (selectedFile != null) {
				selectFileName = selectedFile.getName();// 선택된 이미지 이름!! 도 저장함.
			}
			localImage = new Image(localUrl, false); // 이미지 객체
			gamerImg.setImage(localImage);
			gamerImg.setFitHeight(250);
			gamerImg.setFitWidth(230);
		} catch (Exception e1) {
			AlertDisplay.alertDisplay(1, "사진오류", "사진을 찾을 수 없습니다.", "선택된 이미지 파일경로 오류, 선택된 이미지 이름 저장 오류" + e1.toString());
		}
	}

	// 이미지 저장
	public String imageSave(File file1) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// 이미지 파일명 생성
			fileName = "gamerUser" + System.currentTimeMillis() + "_" + file1.getName();
			bis = new BufferedInputStream(new FileInputStream(file1)); // 선택한 파일 이미지를 읽어옴.
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName)); // 이미지를
																												// 보냄!

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
		return fileName; // << String 으로 리턴값
	}

	// 나가기
	public void handlerBtnExitAtion(ActionEvent e) {
		((Stage) btnExit.getScene().getWindow()).close();
	}

}
