package Application;

import java.sql.*;
import java.util.ArrayList;

public class sqlDB {
	// database parameters and credentials
	static final String USERNAME = "root";
	static final String PASSWORD = "root";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/p350?useSSL=false";
	
	// mode 0: online, mode 1: offline
	public static void setOnline(String user, Boolean mode) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		String query = "UPDATE USERS SET IS_ONLINE = 0 WHERE USERNAME = ?";
				
		if (mode) {
			query = "UPDATE USERS SET IS_ONLINE = 1 WHERE USERNAME = ?";
		}
		// prepare sql query
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);

		// execute sql query
		statement.execute();
		
		// release resources
		statement.close();
		con.close();
	}

 	public static String getPassDB(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		String hash = "";
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	
		// prepare sql query
		String query = "SELECT `PASS` FROM `USERS` WHERE `USERNAME` = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		
		// execute sql query
		ResultSet result = statement.executeQuery(); 
		
		while (result.next()) {
			hash = result.getString(1);
		}
		// close connections
		result.close();
		statement.close();
		con.close();
		
		return hash;
	}

	public static String getNameDB(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		String res = "";
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "SELECT `FNAME` ,`LNAME`  FROM `USERS` WHERE `USERNAME` = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		
		// execute query
		ResultSet result = statement.executeQuery();
			
		while(result.next()) {
			res = result.getString(1) + " " + result.getString(2);
		}
		// close resources
		result.close();
		statement.close();
		con.close();
		
		return res;
	}

	public static Boolean addUserDB(String user, String fname, String lname, String pass) {
		Connection con = null;
		int res;
		
		try {
			// connect to sql database
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
			// prepare query
			String query = "INSERT INTO `USERS` (`USERNAME`, `FNAME`, `LNAME`, `PASS`, `IS_ONLINE`) VALUES (?,?,?,MD5(?),0)";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, user);
			statement.setString(2, fname);
			statement.setString(3, lname);
			statement.setString(4, pass);
		
			// execute query
			res = statement.executeUpdate();
			
			query = "INSERT INTO `SCORES` (`USERNAME`, `GAME`) VALUES (?, 'Tic Tac Toe')";
			statement = con.prepareStatement(query);
			statement.setString(1, user);
			statement.executeUpdate();
			
			// close resources
			statement.close();
			con.close();
		
			if (res == 0) {
				return false;
			} else {
				return true;
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// close resources
			try {
				if (con != null) con.close();
			} catch(SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public static Boolean changePassDB(String user, String pass) {
		Connection con = null;
		
		try {
			// connect to sql database
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
			// prepare query
			String query = "UPDATE USERS SET PASS = MD5(?) WHERE USERNAME = ?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, pass);
			statement.setString(2, user);
		
			// execute query
			statement.executeUpdate();
		
			// close resources
			statement.close();
			con.close();
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// close resources
			try {
				if (con != null) con.close();
			} catch(SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	public static Boolean sendChatDB(String sender, String receiver, String msg) {
		Connection con = null;
		
		try {
			// connect to sql database
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			// prepare query
			String query = "INSERT INTO `CHAT` (`chatID`, `SENDER`, `RECEIVER`, `CONTENT`, `TIME`) VALUES (NULL, ?, ?, ?, NOW());";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, sender);
			statement.setString(2, receiver);
			statement.setString(3, msg);
			
			// execute query
			statement.executeUpdate();
			
			// close resources
			statement.close();
			con.close();
			
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// close resources
			try {
				if (con != null) con.close();
			} catch(SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	public static ArrayList<ArrayList<String>> getChatAllDB() throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select FNAME, CONTENT, chatid from chat, users where users.username = chat.sender and receiver = 'GLOBAL'  order by chatid desc limit 20;";
		PreparedStatement statement = con.prepareStatement(query);

		
		// execute query
		ResultSet result =  statement.executeQuery();
		ArrayList<ArrayList<String>> Rows = new ArrayList<ArrayList<String>>();

		while(result.next()) {
			ArrayList<String> Row = new ArrayList<String>();
			for (int i = 1; i <= 3 ; i++) Row.add(result.getString(i));
			Rows.add(Row);
		}
		// release resources
		statement.close();
		con.close();
		return Rows;
	}
	
	public static String[] getAvailableGames() throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select NAME from GAMES where avail = 1";
		PreparedStatement statement = con.prepareStatement(query);

		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] gamesArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			gamesArr[i] = result.getString(1);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return gamesArr;
	}
	
	public static ArrayList<ArrayList<String>> getChatUserDB(String user, int id, String sender) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "SELECT FNAME, `CONTENT`, chatID FROM `CHAT`, USERS WHERE USERS.USERNAME = CHAT.SENDER AND ((RECEIVER = ? AND  SENDER = ? )OR (SENDER = ? AND RECEIVER = ?)) ORDER BY chatID;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		//statement.setInt(2, id);
		statement.setString(2,sender);
		statement.setString(3,user);
		statement.setString(4,sender);
		
		// execute query
		ResultSet result =  statement.executeQuery();

		ArrayList<ArrayList<String>> Rows = new ArrayList<ArrayList<String>>();
		
		while (result.next()) {
			ArrayList<String> Row = new ArrayList<String>();
			for (int i = 1; i <= 3 ; i++) Row.add(result.getString(i));
			Rows.add(Row);
		}
		// release resources
		statement.close();
		con.close();
		return Rows;
	}
	
	public static String[] getUsersDB(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "SELECT USERNAME FROM USERS , FRIENDSHIP WHERE USERNAME != 'ADMIN' AND USERNAME != ? AND IS_ONLINE = 1 AND USERNAME = USER1 AND USER2 = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, user);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] usersArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			usersArr[i] = result.getString(1);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return usersArr;
	}

	
	public static String[] getUsersFriendsDB(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select username from users where username not in (SELECT user2 from friendship where user1 = ?) and username != ? and username != 'GLOBAL' and username != 'ADMIN';";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, user);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] usersArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			usersArr[i] = result.getString(1);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return usersArr;
	}
	
	public static Boolean addFriendDB(String user1, String user2) {
		Connection con = null;
		
		try {
			// connect to sql database
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			// prepare query
			String query = "INSERT INTO FRIENDSHIP (USER1, USER2) VALUES (?,?), (?,?)";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, user1);
			statement.setString(2, user2);
			statement.setString(3, user2);
			statement.setString(4, user1);
			
			// execute query
			statement.executeUpdate();
			
			// close resources
			statement.close();
			con.close();
			
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// close resources
			try {
				if (con != null) con.close();
			} catch(SQLException se) {
				se.printStackTrace();
			}
		}
		
		
	}
	
	public static String[] getFriendsDB(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "SELECT user2 from friendship where user1 = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] usersArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			usersArr[i] = result.getString(1);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return usersArr;
	}
	
	public static String[] getOnlineFriendsDB(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select user2 from friendship, users where user2 = username and user1 = ? and is_online = 1;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] usersArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			usersArr[i] = result.getString(1);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return usersArr;
	}
	
	public static Boolean removeFriendDB(String user1, String user2) {
		Connection con = null;
		
		try {
			// connect to sql database
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			// prepare query
			String query = "delete FROM FRIENDSHIP where (user1 = ? and user2 = ?) or (user2 = ? and user1 = ?)";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, user1);
			statement.setString(2, user2);
			statement.setString(3, user1);
			statement.setString(4, user2);
			
			// execute query
			statement.executeUpdate();
			
			// close resources
			statement.close();
			con.close();
			
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// close resources
			try {
				if (con != null) con.close();
			} catch(SQLException se) {
				se.printStackTrace();
			}
		}	
	}
	
	public static String MD5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       	}
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
	}
	
	public static String sendGameRequest(String from, String game, String to) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		String query = "INSERT INTO `gameRequests` (`fromPlayer`, `toPlayer`, `game`) VALUES (?,?,?) ";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, from);
		statement.setString(2, to);
		statement.setString(3, game);

		// execute sql query
		int num = statement.executeUpdate();
		if(num == 1)
		{
			statement.close();
			con.close();
			return "Request Sent!";
		}
		else
		{
			statement.close();
			con.close();
			return "error";
		}
		

	}
	
	public static ArrayList<ArrayList<String>> getGameRequests(String to) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		String query = "SELECT `fromPlayer`, `game` FROM gameRequests WHERE `toPlayer` = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, to);
		
		// execute query
		ResultSet result =  statement.executeQuery();

		ArrayList<ArrayList<String>> Rows = new ArrayList<ArrayList<String>>();
		
		while (result.next()) {
			ArrayList<String> Row = new ArrayList<String>();
			for (int i = 1; i <= 2 ; i++) Row.add(result.getString(i));
			Rows.add(Row);
		}
		// release resources
		statement.close();
		con.close();
		return Rows;
	}
	
	public static void deleteGameRequests(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "delete FROM gameRequests where (toplayer = ?) or (fromPlayer = ?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, user);
		
		// execute query
		statement.executeUpdate();
		
		// close resources
		statement.close();
		con.close();
	}
	
	public static void declineGameRequest(String toUser, String fromUser) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "delete FROM gameRequests where (toplayer = ?) and (fromPlayer = ?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, toUser);
		statement.setString(2, fromUser);
		
		// execute query
		statement.executeUpdate();
		
		// close resources
		statement.close();
		con.close();
	}
	public static void acceptGameRequest(String toUser, String fromUser) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "update gameRequests set `accepted` = 1 where (toplayer = ?) and (fromPlayer = ?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, toUser);
		statement.setString(2, fromUser);
		
		// execute query
		statement.executeUpdate();
		
		// close resources
		statement.close();
		con.close();
	}
	
	public static String findAcceptedRequests(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select toPlayer, game from gameRequests where fromPlayer = ? and accepted = 1 limit 1";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		String accRequests;
		if(result.next())
		{
			accRequests = result.getString(1) + "#" + result.getString(2);
		}
		else
		{
			accRequests = "game_acc_error";
		}
		

		
		// delete
		query = "delete from gameRequests where fromPlayer = ? and accepted = 1 limit 1";
		statement = con.prepareStatement(query);
		statement.setString(1, user);
		
		// execute query
		statement.execute();
		
		// release resources
		statement.close();
		con.close();
		return accRequests;
	}
	
	public static String findMovesOpponent(String opponent) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select moveG from gamemoves where fromWho = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, opponent);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		String accRequests;
		
		if(result.next())
		{
		
			accRequests = result.getString(1);
		}
		else
		{
			accRequests = "no_moves";
		}
		
		// delete
		query = "delete from gamemoves where fromWho = ?";
		statement = con.prepareStatement(query);
		statement.setString(1, opponent);
		
		// execute query
		statement.execute();
		
		// release resources
		statement.close();
		con.close();
		return accRequests;
	}
	public static void putMove(String user, String coordinate) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "insert into gamemoves values (?,?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, coordinate);
		
		// execute query
		statement.executeUpdate();
		
		// close resources
		statement.close();
		con.close();
	}
	public static void increaseScore(String player, String game) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "update scores set score = score + 1 where (username = ?) and (game = ?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, player);
		statement.setString(2, game);
		
		// execute query
		statement.executeUpdate();
		
		// close resources
		statement.close();
		con.close();
	}
	public static String [] getScores(String game) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select username , score from scores where game = ? and score > 0 order by score desc";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, game);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] usersArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			usersArr[i] = result.getString(1) + ": " + result.getString(2);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return usersArr;
		
	}
	public static void saveToHistory(String loser, String winner, String game) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "insert into history values (?, ?, ?, now(), ?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, loser);
		statement.setString(2, winner);
		statement.setString(3, game);
		statement.setString(4, winner);
		
		// execute query
		statement.executeUpdate();
		
		// close resources
		statement.close();
		con.close();
	}
	public static String [] getHistory(String user) throws SQLException, ClassNotFoundException {
		Connection con = null;
		
		// connect to sql database
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		
		// prepare query
		String query = "select * from history where player1 = ? or player2 = ? ";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, user);
		
		// execute query
		ResultSet result =  statement.executeQuery();
		
		// determine length of result
		int count = 0;
		result.last();
		count = result.getRow();
		result.beforeFirst();
		
		String[] usersArr = new String[count];
		int i = 0;
		while(result.next()) {
			//if (i == 0) result.first();
			usersArr[i] = "Player 1: " + result.getString(1) + " - Player2: " + result.getString(2)+ " - Game: " + result.getString(3)+ " - Time: " + result.getString(4)+ " - WINNER: " + result.getString(5);
			i++;
		}
		// release resources
		statement.close();
		con.close();
		return usersArr;
		
	}
}