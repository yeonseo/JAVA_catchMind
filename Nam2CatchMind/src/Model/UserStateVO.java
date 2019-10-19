package Model;

public class UserStateVO {
	
	/* 
	 * DB - UserGameState
	 * 상태 업데이트 용
	 * 채현이랑 같이 한거니까 건들지 말것!!!!
	 * 로그인 시 상태 업데이트까지 완료
	 * 
	 * */
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