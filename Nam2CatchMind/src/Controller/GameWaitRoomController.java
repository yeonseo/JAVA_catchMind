package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.MakeRoomVO;
import Model.UserRankingVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameWaitRoomController implements Initializable {
     
   @FXML Label gameStartTime;
   @FXML Label gameUserId;
   @FXML ImageView gamerImg;
   @FXML Button btnMakeRoom;
   @FXML Button btnMyRecord;
   @FXML Button btnMyInfoChange;
   @FXML Button btnUserSend;
   @FXML Button btnGameRoomExit;
   @FXML TableView<MakeRoomVO> roomInfo;
   @FXML TableView<UserRankingVO> userRanking;
   @FXML TextArea txtChatArea;
   @FXML TextField txtInputMessage;
   
   
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
      
      
      
      //나의 전적
      btnMyRecord.setOnAction(e->{});
      //게임방만들기
      btnMakeRoom.setOnAction(e->{  handlerBtnMakeRoomAction(e);   });
      //나가기
      
   }
   //게임방 만들기
   public void handlerBtnMakeRoomAction(ActionEvent e) {
      Parent makeGameRoomRoot=null;
      Stage makeRoomStage=null;
      try {
         makeGameRoomRoot =FXMLLoader.load(getClass().getResource("/View/MakeRoom.fxml"));
         Scene scene=new Scene(makeGameRoomRoot);
         makeRoomStage=new Stage();
         makeRoomStage.setTitle("방만들기");
         makeRoomStage.initModality(Modality.WINDOW_MODAL);
         makeRoomStage.initOwner(btnMakeRoom.getScene().getWindow());
         makeRoomStage.setResizable(false);
         makeRoomStage.setScene(scene);
         makeRoomStage.show();
      } catch (IOException e1) {
         e1.printStackTrace();
      }
      
   }

}