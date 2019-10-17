package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost/drowInfoMVC";

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url, "root", "ysnam007");
		return con;
	}
}
