package Model;

public class MakeRoomVO {
	
			
	private String RoomName;
	private String ThreadState;
	private String Gamer1;
	private String Gamer2;
	private String GameRunOrWaitState;
	
	public MakeRoomVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public MakeRoomVO(String gamer1, String gamer2) {
		super();
		Gamer1 = gamer1;
		Gamer2 = gamer2;
	}


	//방이름 등록한것 DB에서 가져오기
	public MakeRoomVO(String roomName, String gamer1, String gamer2, String gameRunOrWaitState) {
		super();
		RoomName = roomName;
		Gamer1 = gamer1;
		Gamer2 = gamer2;
		GameRunOrWaitState = gameRunOrWaitState;
	}
	
	public MakeRoomVO(String roomName, String threadState, String gamer1, String gamer2,
			String gameRunOrWaitState) {
		super();
		RoomName = roomName;
		ThreadState = threadState;
		Gamer1 = gamer1;
		Gamer2 = gamer2;
		GameRunOrWaitState = gameRunOrWaitState;
	}

	


	public String getRoomName() {
		return RoomName;
	}

	public void setRoomName(String roomName) {
		RoomName = roomName;
	}

	public String getThreadState() {
		return ThreadState;
	}

	public void setThreadState(String threadState) {
		ThreadState = threadState;
	}

	public String getGamer1() {
		return Gamer1;
	}

	public void setGamer1(String gamer1) {
		Gamer1 = gamer1;
	}

	public String getGamer2() {
		return Gamer2;
	}

	public void setGamer2(String gamer2) {
		Gamer2 = gamer2;
	}

	public String getGameRunOrWaitState() {
		return GameRunOrWaitState;
	}

	public void setGameRunOrWaitState(String gameRunOrWaitState) {
		GameRunOrWaitState = gameRunOrWaitState;
	}
	
	
	
	
}
