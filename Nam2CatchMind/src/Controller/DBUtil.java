package Controller;

import java.sql.Connection;
import java.sql.DriverManager;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DBUtil {

	private static String driver = "com.mysql.jdbc.Driver";
//	private static String url = "jdbc:mysql://localhost/Nam2CatchMind";
	private static String url = "jdbc:mysql://192.168.0.210/Nam2CatchMind";

	public static Connection getConnection() throws Exception {

		Class.forName(driver);
		Connection connection = DriverManager.getConnection(url, "root", "123456");
		return connection;
	}
}