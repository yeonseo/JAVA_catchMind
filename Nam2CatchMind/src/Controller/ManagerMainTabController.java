package Controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ManagerManagmentVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ManagerMainTabController implements Initializable {
	/*
	 * 관리 메인텝
	 */

	@FXML
	private ImageView imgMainUser;
	@FXML
	private Label lblMainUserId;
	@FXML
	private Label lblMainAccess;
	@FXML
	private Label lblMainState;
	@FXML
	private Button btnMainLoginTimeChart;
	@FXML
	private Button btnMainTabUserEdit;
	@FXML
	private TableView<ManagerManagmentVO> tableView;
	@FXML
	private Label lblMainTabServerState;
	@FXML
	private TextArea txtMainAreaServerLog;
	@FXML
	private Button btnMainServerOpen;
	@FXML
	private Button btnMainServerClose;
	@FXML
	private Button btnManagerMainTabExit;
	@FXML
	private Tab tabManager;

	ObservableList<ManagerManagmentVO> userData;

	ManagerManagmentVO mmVO;
	ManagerManagmentDAO mmdao;

	String selectedUserID;
	String selectedUserAccess;
	String selectedUserState;

	private ObservableList<ManagerManagmentVO> selectUser;
	private int selectUserIndex;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handlerUserInfoShow();
		tableViewSetting();

		btnManagerMainTabExit.setOnAction(e -> {
			handlerBtnManagerMainTabExitAction();
		});
	}

	private void tableViewSetting() {

		userData = FXCollections.observableArrayList();

		tableView.setEditable(false); // tableView 수정 x

		DecimalFormat format = new DecimalFormat("###");

		TableColumn RoomName = new TableColumn("RoomName");
		RoomName.setMaxWidth(30);
		RoomName.setStyle("-fx-alignment: CENTER;");
		RoomName.setCellValueFactory(new PropertyValueFactory("RoomName"));

		TableColumn Gamer1 = new TableColumn("Gamer1");
		Gamer1.setMaxWidth(43);
		Gamer1.setStyle("-fx-alignment: CENTER;");
		Gamer1.setCellValueFactory(new PropertyValueFactory("Gamer1"));

		TableColumn Gamer2 = new TableColumn("Gamer2");
		Gamer2.setMaxWidth(43);
		Gamer2.setStyle("-fx-alignment: CENTER;");
		Gamer2.setCellValueFactory(new PropertyValueFactory("Gamer2"));

		TableColumn ThreadState = new TableColumn("게임상태State");
		ThreadState.setMaxWidth(43);
		ThreadState.setStyle("-fx-alignment: CENTER;");
		ThreadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));

		tableView.setItems(userData);
		tableView.getColumns().addAll(RoomName, Gamer1, Gamer2, ThreadState);

	}// end of tableViewSetting

	/*
	 * 모든 유저들의 정보를 가지고 옴 total list 참고함
	 */
	private void handlerUserInfoGet() {

		ArrayList<ManagerManagmentVO> list = null;
		ManagerManagmentVO mmVO = null;
		ManagerManagmentDAO mmdao = new ManagerManagmentDAO();
		list = mmdao.getUserTotalData();

		if (list == null) {
			AlertDisplay.alertDisplay(1, "DB List Null Point", "Null Point", "Error");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			mmVO = list.get(i);
			userData.add(mmVO);
		}
	}

	private void handlerUserInfoShow() {
//		imgMainUser
		lblMainUserId.setText(selectedUserID);
		lblMainAccess.setText(selectedUserAccess);
		lblMainState.setText(selectedUserState);
	}

	private void handlerBtnManagerMainTabExitAction() {
		Platform.exit();
	}

}
