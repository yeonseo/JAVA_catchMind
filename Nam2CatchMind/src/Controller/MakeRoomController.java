package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
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
			
			Parent gameRoomRoot = null;
			Stage gameRoomStage = null;
			/*
			 * <mmVO> String roomName, String threadState (유저의 상태와 방이름으로!) String managerID,
			 * String makeRoomUserID, String enterRoomUserID, String gameRunOrWaitState
			 * 
			 */
			mmVO = new ManagerManagmentVO(txtMakeRoomName.getText(),
					UserGameState.gamerGameWait + "," + txtMakeRoomName.getText(), null, GamerLoginController.UserId,
					null, "Wait");
			usdao = new UserStateDAO(); // UserStateDAO의 객체를 부름
			int count = usdao.getUserGameRoomRegistration(mmVO.getRoomName(), mmVO.getThreadState(),
					mmVO.getManagerID(), mmVO.getMakeRoomUserID(), mmVO.getEnterRoomUserID(),
					mmVO.getGameRunOrWaitState()); // DAO에 UserID, UserThreadState를 넣어줌!
			if (count != 0) {
				AlertDisplay.alertDisplay(3, "방등록", "등록성공!", "상태 : " + mmVO.getThreadState());
				Stage stage = (Stage) btnCancel.getScene().getWindow();
				stage.close(); // 등록 alert 띄우고 그 페이지 닫아짐!

			} else {
				throw new Exception("데이터베이스 등록실패!");
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "방등록", "등록실패!", e.toString());
		}

	}

}
