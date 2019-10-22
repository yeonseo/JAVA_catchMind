package Model;

public class UserGameHistroryVO {
	
	private String UserID;
	private int Play;
	private int Win;
	private int Sence;
	
	public UserGameHistroryVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	

	public UserGameHistroryVO(String userID, int play) {
		super();
		UserID = userID;
		Play = play;
	}





	public UserGameHistroryVO(String userID, int play, int win, int sence) {
		super();
		UserID = userID;
		Play = play;
		Win = win;
		Sence = sence;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public int getPlay() {
		return Play;
	}

	public void setPlay(int play) {
		Play = play;
	}

	public int getWin() {
		return Win;
	}

	public void setWin(int win) {
		Win = win;
	}

	public int getSence() {
		return Sence;
	}

	public void setSence(int sence) {
		Sence = sence;
	}
	
	
	
}
