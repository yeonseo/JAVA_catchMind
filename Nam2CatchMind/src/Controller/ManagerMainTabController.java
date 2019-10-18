package Controller;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Model.UserVO;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManagerMainTabController implements Initializable {
	/*
	 * 관리 메인텝
	 * */
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
	private TableView<UserVO> tableView;
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
	
//	
//	ObservableList<UserVO> userData;
//
//	private ObservableList<UserVO> selectUser;
//	private int selectUserIndex;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		tableViewSetting();
		
	}

//	private void tableViewSetting() {
//
//		userData = FXCollections.observableArrayList();
//
//		tableView.setEditable(false); // tableView 수정 x
//
//		DecimalFormat format = new DecimalFormat("###");
//		
//		TableColumn RoomName = new TableColumn("RoomName");
//		RoomName.setMaxWidth(30);
//		RoomName.setStyle("-fx-alignment: CENTER;");
//		RoomName.setCellValueFactory(new PropertyValueFactory("RoomName"));
//
//		TableColumn Gamer1 = new TableColumn("Gamer1");
//		Gamer1.setMaxWidth(43);
//		Gamer1.setStyle("-fx-alignment: CENTER;");
//		Gamer1.setCellValueFactory(new PropertyValueFactory("Gamer1"));
//
//		TableColumn Gamer2 = new TableColumn("Gamer2");
//		Gamer2.setMaxWidth(43);
//		Gamer2.setStyle("-fx-alignment: CENTER;");
//		Gamer2.setCellValueFactory(new PropertyValueFactory("Gamer2"));
//
//		TableColumn ThreadState = new TableColumn("ThreadState");
//		ThreadState.setMaxWidth(43);
//		ThreadState.setStyle("-fx-alignment: CENTER;");
//		ThreadState.setCellValueFactory(new PropertyValueFactory("ThreadState"));
//
//		tableView.setItems(userData);
//		tableView.getColumns().addAll(RoomName, Gamer1, Gamer2, ThreadState);
//
//	}// end of tableViewSetting


	private void handlerBtnManagerMainTabExitAction() {
		Platform.exit();
	}
	

}
