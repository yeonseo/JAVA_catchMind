package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.UserVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GamerLoginController implements Initializable {
   
   @FXML TextField gamerId;
   @FXML PasswordField gamerPwd;
   @FXML Button btnLogin;
   @FXML Button btnMemberShip;
   @FXML Button btnExit;
   
   GamerDAO gdao;
   UserVO uvo;
   ArrayList<UserVO> list;
   
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
      
      btnLogin.setOnAction(e->{  handlerBtnLoginAction(e);  }); // 로그인 확인
      btnMemberShip.setOnAction(e->{  handlerBtnMemberShipAction(e); });   //회원가입 버튼
      btnExit.setOnAction(e->{ Platform.exit(); });
   }
   //로그인확인
   public void handlerBtnLoginAction(ActionEvent e) {
      gdao=new GamerDAO();
      list=gdao.getLoginCheck(gamerId.getText(), gamerPwd.getText());
      System.out.println("로그인 리스트 사이즈 : "+list.size());
      if(gamerId.getText().equals("") ||  gamerPwd.getText().equals("")) {
         AlertDisplay.alertDisplay(1, "아이디 및 패스워드 미입력", "아이디 및 패스워드 미입력", "아이디와 패스워드를 입력해주세요!");
         return;
      }
      if(list.size() != 0) {
         AlertDisplay.alertDisplay(5, "로그인 성공", "로그인 성공!", "로그인이 되었습니다.");
         
      }else {
         AlertDisplay.alertDisplay(1, "로그인 실패", "아이디 및 패스워드 찾을 수 없음.", "아이디와 패스워드를 다시 확인해주세요!");
      }
      

      
   }
   /*
    * btnMemberShip by JM 19.10.16
    * ->회원가입 버튼을 누르면 회원가입창을 연다!
    *   
    */
   //회원가입창
   public  void handlerBtnMemberShipAction(ActionEvent e) {
      Parent memberShip;
      Stage stage;
      try {
         memberShip=FXMLLoader.load(getClass().getResource("/View/GamerMemberShip.fxml"));
         stage=new Stage();
         Scene scene=new Scene(memberShip);
         stage.setTitle("회원가입");
         stage.initModality(Modality.WINDOW_MODAL);
         stage.initOwner(btnMemberShip.getScene().getWindow());
         stage.setResizable(false);
         stage.setScene(scene);
         stage.show();
            
      } catch (IOException e1) {
         AlertDisplay.alertDisplay(1, "오류", "회원가입창을 가져오는데 실패했습니다.", e1.toString());
      }
      
      
   }
   
   
   

}