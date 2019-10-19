package Model;

public class ManagerManagmentVO {
	/*
	 * 관리자 창에서 사용할 용도로 만든 것!
	 * 
	 * */
	
	private String UserID;
	private String UserPassword;
	private int UserAccess;
	private String UserGender;
	private String Image;
	private String ThreadState; //게임방을 구분하기위한 상태
	
	
	/*
	 * UserGameRoom 에서 방 만들고, 입장이 될 시에 사용하는 용도로 쓰이는 변수들
	 * 
	 * */
	private String RoomName;
	private String ManagerID;
	private String MakeRoomUserID;
	private String EnterRoomUserID;
	private String GameRunOrWaitState; //게임 진행중인지를 구분하기 위한 상태
	
	
	
	
	
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
	 * 방만들기 관련된 함수 YS 191019
	 * 
	 * 관리 메인창에서 만들어진 방의 목록을 보기위한 생성자
	 * */
	public ManagerManagmentVO(String roomName, String threadState, String managerID, String makeRoomUserID, String enterRoomUserID, String gameRunOrWaitState) {
		RoomName = roomName;
		ThreadState = threadState;
		ManagerID = managerID;//나중에 없어도 될.. 걸!! 관리자는 상태만 가지고 가서소켓을 볼 수 있으면 됨
		MakeRoomUserID = makeRoomUserID;
		EnterRoomUserID = enterRoomUserID;
		GameRunOrWaitState = gameRunOrWaitState;
	}

	/*
	 * 방만들기 관련된 함수 YS 191019
	 * 
	 * 방 만들기 시도시, 비교하기 위함
	 * */
	public ManagerManagmentVO(String roomName) {
		RoomName = roomName;
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

	public String getRoomName() {
		return RoomName;
	}

	public void setRoomName(String roomName) {
		RoomName = roomName;
	}

	public String getManagerID() {
		return ManagerID;
	}

	public void setManagerID(String managerID) {
		ManagerID = managerID;
	}

	public String getMakeRoomUserID() {
		return MakeRoomUserID;
	}

	public void setMakeRoomUserID(String makeRoomUserID) {
		MakeRoomUserID = makeRoomUserID;
	}

	public String getEnterRoomUserID() {
		return EnterRoomUserID;
	}

	public void setEnterRoomUserID(String enterRoomUserID) {
		EnterRoomUserID = enterRoomUserID;
	}

	public String getGameRunOrWaitState() {
		return GameRunOrWaitState;
	}

	public void setGameRunOrWaitState(String gameRunOrWaitState) {
		GameRunOrWaitState = gameRunOrWaitState;
	}
}
