package Model;

public class GamerVO {
	private String UserID;
	private String UserPassword;
	private String UserGender;
	private String Image;
	/* 
	 * 
	 * gameUser�� ����ϱ����Ѱ�
	 * 
	 * 
	 */
	
	public GamerVO(String userID, String userPassword, String userGender, String image) {
		super();
		UserID = userID;
		UserPassword = userPassword;
		UserGender = userGender;
		Image = image;
	}

	public GamerVO() {
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
