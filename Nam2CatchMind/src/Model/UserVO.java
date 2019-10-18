package Model;

public class UserVO {
   private String UserID;
   private String UserPassword;
   private int UserAccess;
   private String UserGender;
   private String Image;
   
   /* 
    * 
    * gameUserID 확인
    * 
    * 
    */
   
   public UserVO(String userID) {
      super();
      UserID = userID;
   }
   
   /* 
    * 
    * loginUserID 및 loginUserPwd 확인 
    * 
    * 
    */
   
   public UserVO(String userID, String userPassword) {
      super();
      UserID = userID;
      UserPassword = userPassword;
   }
   
   
   /* 
    * 
    * gameUser를 등록하기위한것
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
   
   
   /* 
    * 
    * managerUser를 등록하기위한것
    * 
    * 
    */
   
   public UserVO(String userID, String userPassword, int userAccess, String userGender, String image) {
	   super();
	   UserID = userID;
	   UserPassword = userPassword;
	   UserAccess = userAccess;
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

	public int getUserAccess() {
		return UserAccess;
	}
	
	public void setUserAccess(int userAccess) {
		UserAccess = userAccess;
	}
   
   

}