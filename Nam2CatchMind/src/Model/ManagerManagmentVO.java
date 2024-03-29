package Model;

public class ManagerManagmentVO {
	/*
	 * 관리자 창에서 사용할 용도로 만든 것!
	 * 
	 */

	private String UserID;
	private String UserPassword;
	private int UserAccess;
	private String UserGender;
	private String Image;
	private String ThreadState; // 게임방을 구분하기위한 상태
	private String EnterTime; // 로그인시 체크
	private String OutTime; // 로그아웃시 체크
	private String ConnetedTime; // 시간계산 음... 안쓸수도

	/*
	 * UserGameRoom 에서 방 만들고, 입장이 될 시에 사용하는 용도로 쓰이는 변수들
	 * 
	 */
	private String RoomName;
	private String ManagerID;
	private String MakeRoomUserID;
	private String EnterRoomUserID;
	private String GameRunOrWaitState; // 게임 진행중인지를 구분하기 위한 상태

	/*
	 * 방만들기 관련된 함수 YS 191019
	 * 
	 * 방 만들기 시도시, 비교하기 위함
	 */
	public ManagerManagmentVO(String roomName) {
		RoomName = roomName;
	}

	/*
	 * 방만들기 관련된 함수 YS 191019
	 * 
	 * 관리 메인창에서 만들어진 방의 목록을 보기위한 생성자
	 */
	public ManagerManagmentVO(String roomName, String threadState, String managerID, String makeRoomUserID,
			String enterRoomUserID, String gameRunOrWaitState) {
		RoomName = roomName;
		ThreadState = threadState;
		ManagerID = managerID;// 나중에 없어도 될.. 걸!! 관리자는 상태만 가지고 가서소켓을 볼 수 있으면 됨
		MakeRoomUserID = makeRoomUserID;
		EnterRoomUserID = enterRoomUserID;
		GameRunOrWaitState = gameRunOrWaitState;
	}

	/*
	 * 관리 메인창에서 관리자 프로필에 아이디, 권한, 상태를 나타내기 위한 생성자
	 */
	public ManagerManagmentVO(String userID, int userAccess, String threadState) {
		super();
		UserID = userID;
		UserAccess = userAccess;
		ThreadState = threadState;
	}

	/*
	 * 관리 메인 텝에서 방이름 테이블 뷰를 위한 생성자
	 */
	public ManagerManagmentVO(String roomName, String threadState, String makeRoomUserID, String enterRoomUserID,
			String gameRunOrWaitState) {
		RoomName = roomName;
		ThreadState = threadState;
		MakeRoomUserID = makeRoomUserID;
		EnterRoomUserID = enterRoomUserID;
		GameRunOrWaitState = gameRunOrWaitState;
	}

	/*
	 * 관리 관리자 탭에서 관리자 테이블 뷰를 위한 생성자 ui.UserID, ui.UserPassword, ui.UserAccess,
	 * ui.UserImage, ugs.ThreadState
	 */
	public ManagerManagmentVO(String userID, String userPassword, int userAccess, String image, String threadState) {
		super();
		UserID = userID;
		UserPassword = userPassword;
		UserAccess = userAccess;
		Image = image;
		ThreadState = threadState;
	}

	/*
	 * 관리자 유저 관리에서 유저 테이블을 만들기 위한 생성자 ui.UserID, ui.UserImage, ugs.ThreadState
	 */
	public ManagerManagmentVO(String userID, String image, String threadState) {
		UserID = userID;
		Image = image;
		ThreadState = threadState;
	}

	/*
	 * 파이차트를 위한 시간 가져오기
	 */
	public ManagerManagmentVO(String userID, String enterTime) {
		UserID = userID;
		EnterTime = enterTime;
	}

	public String getEnterTime() {
		return EnterTime;
	}

	public void setEnterTime(String enterTime) {
		EnterTime = enterTime;
	}

	public String getOutTime() {
		return OutTime;
	}

	public void setOutTime(String outTime) {
		OutTime = outTime;
	}

	public String getConnetedTime() {
		return ConnetedTime;
	}

	public void setConnetedTime(String connetedTime) {
		ConnetedTime = connetedTime;
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
