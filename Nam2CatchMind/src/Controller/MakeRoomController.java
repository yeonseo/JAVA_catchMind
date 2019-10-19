package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import Model.UserStateVO;
import Model.UserVO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MakeRoomController implements Initializable {

	@FXML
	TextField txtMakeRoomName;
	@FXML
	Button btnMakeRoom;
	@FXML
	Button btnCancel;

	/*
	 * 
	 * 
	 * 
	 * */

	ManagerManagmentVO mmVO;
	UserStateDAO usdao;
	boolean roomNameCheck = false;

	UserStateVO usvo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnCancel.setOnAction(e -> {
			((Stage) btnCancel.getScene().getWindow()).close();
		});

		btnMakeRoom.setOnAction(e -> {
			handlerBtnMakeRoomAction();
		});

	}

	private void handlerBtnMakeRoomAction() {
		try {
			mmVO = new ManagerManagmentVO(txtMakeRoomName.getText()); // 아이디값 vo넣음
			usdao = new UserStateDAO(); // gdao 객체 가져옴
			roomNameCheck = usdao.checkRoomName(mmVO); // 아이디값 vo를 gdao.checkUserID()에 매개변수로 넣고 불리언 으로 true, false로 받는다.
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
						UserGameState.gamerGameWait + "," + txtMakeRoomName.getText(), null,
						GamerLoginController.UserId, null, "Wait");
				usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
				int count = usdao.getUserGameRoomRegistration(mmVO.getRoomName(), mmVO.getThreadState(),
						mmVO.getManagerID(), mmVO.getMakeRoomUserID(), mmVO.getEnterRoomUserID(),
						mmVO.getGameRunOrWaitState()); // DAO에 UserID, UserThreadState를 넣어줌!
				AlertDisplay.alertDisplay(3, "DB 방등록", "등록성공!", "상태 : " + mmVO.getThreadState());
				
				if (count != 0) {
					/*
					 * 방만들기 시도 후 데이터베이스에서 등록 성공시, 유저의 상태변경
					 */
					usvo = new UserStateVO(GamerLoginController.UserId, mmVO.getThreadState()); // DB에 아이디와 상태를
																									// DAO에게!
					usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
					int count2 = usdao.getUserStateRegistration(usvo.getUserID(), mmVO.getThreadState()); // DAO에 UserID,
																											// UserThreadState를
																											// 넣어줌!
					if (count2 != 0) {
						AlertDisplay.alertDisplay(3, "상태변동", "변동성공!", "상태 : " + UserGameState.gamerEnter);
					} else {
						throw new Exception("데이터베이스 등록실패!");
					}
					Stage stage = (Stage) btnCancel.getScene().getWindow();
					stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!

				} else {
					throw new Exception("데이터베이스 등록실패!");
				}
			}

		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "방등록", "등록실패!", e.toString());
		}

	}

}
