package Model;

public class UserStateVO {
	private String UserID;
	private String ThreadState;

	public UserStateVO(String userID) {
		super();
		UserID = userID;
	}

	public UserStateVO(String userID, String threadState) {
		super();
		UserID = userID;
		ThreadState = threadState;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getThreadState() {
		return ThreadState;
	}

	public void setThreadState(String threadState) {
		ThreadState = threadState;
	}

}