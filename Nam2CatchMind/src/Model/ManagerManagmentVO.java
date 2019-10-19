package Model;

public class ManagerManagmentVO {
	private String UserID;
	private String UserPassword;
	private int UserAccess;
	private String UserGender;
	private String Image;
	private String ThreadState; //게임방을 구분하기위한 상태
	
	private String MakeRoomUserID;
	private String EnterRoomUserID;
	private String RoomState; //게임 진행중인지를 구분하기 위한 상태
	
	
	/*
	 * 관리 메인창에서 관리자 프로필에 아이디, 권한, 상태를 나타내기 위한 생성자
	 * */
	public ManagerManagmentVO(String userID, int userAccess, String threadState) {
		super();
		UserID = userID;
		UserAccess = userAccess;
		ThreadState = threadState;
	}
	
	/*
	 * 관리 메인창에서 만들어진 방의 목록을 보기위한 생성자
	 * */
	public ManagerManagmentVO(String threadState, String makeRoomUserID, String enterRoomUserID, String roomState) {
		super();
		ThreadState = threadState;
		MakeRoomUserID = makeRoomUserID;
		EnterRoomUserID = enterRoomUserID;
		RoomState = roomState;
	}

	


	public String getUserID() {
		return UserID;
	}
	
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getUserPassword() {
		return UserPassword;
	}
	public void setUserPassword(String userPassword) {
		UserPassword = userPassword;
	}
	public int getUserAccess() {
		return UserAccess;
	}
	public void setUserAccess(int userAccess) {
		UserAccess = userAccess;
	}
	public String getUserGender() {
		return UserGender;
	}
	public void setUserGender(String userGender) {
		UserGender = userGender;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getThreadState() {
		return ThreadState;
	}
	public void setThreadState(String threadState) {
		ThreadState = threadState;
	}
	public String getMakeRoomUserID() {
		return MakeRoomUserID;
	}
	public void setMakeRoomUserID(String makeRoomUserID) {
		this.MakeRoomUserID = makeRoomUserID;
	}
	public String getEnterRoomUserID() {
		return EnterRoomUserID;
	}
	public void setEnterRoomUserID(String enterRoomUserID) {
		this.EnterRoomUserID = enterRoomUserID;
	}

	public String getRoomState() {
		return RoomState;
	}

	public void setRoomState(String roomState) {
		RoomState = roomState;
	}
	
}
