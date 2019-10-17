package Model;

public class UserVO {
	private String UserID;
	private String UserPassword;
	private String UserGender;
	private String Image;
	/* 
	 * 
<<<<<<< HEAD:Nam2CatchMind/src/Model/UserVO.java
	 * gameUser�� ����ϱ����Ѱ�
=======
	 * gameUser를 등록하기위한것
>>>>>>> pr/3:Nam2CatchMind/src/Model/GamerVO.java
	 * 
	 * 
	 */
	
	public UserVO(String userID, String userPassword, String userGender, String image) {
		super();
		UserID = userID;
		UserPassword = userPassword;
		UserGender = userGender;
		Image = image;
	}

	public UserVO() {
		super();
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

}
