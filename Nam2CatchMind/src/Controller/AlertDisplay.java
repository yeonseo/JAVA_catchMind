package Controller;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


/*
 * 
 * 이르케 쓰세오오~~~
 * // 관리자 권한 상승시키기
		btnManagerUserEdit.setOnAction(e6 -> {
			AlertDisplay.alertDisplay(2, "권한수정", "권한을 상승합니다", "수정하시겠습니까?");
			if (AlertDisplay.result.get() == ButtonType.OK) {
				AlertDisplay.alertDisplay(5, "권한수정", "권한을 상승합니다", "상승합니다");
			} else {
				
			}
		});
 * 
 * */

public class AlertDisplay {
	public static Alert alert=null;
	public static Optional<ButtonType> result;
	public static void alertDisplay(int type, String title, String headerText, String contentText) {
		alert=null;
		if(type==2) {
			alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle(title);
			alert.setHeaderText(headerText);
			alert.setContentText(headerText+"\n"+contentText);
			alert.setResizable(false);
			result=alert.showAndWait();
		} else {
			switch (type) {
			case 1:  alert=new Alert(AlertType.WARNING); break;
			case 3:  alert=new Alert(AlertType.ERROR); break;
			case 4:  alert=new Alert(AlertType.NONE); break;
			case 5:  alert=new Alert(AlertType.INFORMATION); break;
			}
			alert.setTitle(title);
			alert.setHeaderText(headerText);
			alert.setContentText(headerText+"\n"+contentText);
			alert.setResizable(false);
			alert.showAndWait();
		}
		
	}
	

	
	// for logging to server console
	public static void log(String message) {
		System.out.println(message);
	}
}