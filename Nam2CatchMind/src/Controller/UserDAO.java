package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.UserVO;

/*
 * ȸ�����Կ� ���� ���� by JM 191016
	 * 
	 * ���̵�,��й�ȣ,����, �̹��� ���� ������.
	 * �� String����
	 * 
	 * 1.���̵� �ߺ��˻�
	 * boolean checkUserID(String UserID )
	 * sql=select * from UserInfo where UserId like ?
	 * String userID=UserID; /// ã�� ���̵�
	 * con=DBUtil.getConnection();
	pstmt=con.prepareStatement(sql);
	pstmt.setString(1,UserID);
	rs=pstmt.executeQuery();
	 * 
	 * check=false;
	 * 
	 * while(rs.next()){
	 *     check=true
	 *     
	 *   
	 * }
	 * 
	 * 
	 * return check;
	 * 
	 * 
	 * 
 * 
 */

public class UserDAO {
		
	//ȸ�� ���
	
	  public int getGamerRegistration(UserVO gamerVO) {
		 String sql = "insert into UserInfo "+"(UserID,UserPassword,UserGender,UserImage)" + " values " + "(?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null; 
		int count=0;
		try {
		// �� DBUtil Ŭ������ getConnection( )�޼���� �����ͺ��̽��� ����
		con = DBUtil.getConnection();
		// �� �Է¹��� ȸ�� ������ ó���ϱ� ���Ͽ� SQL������ ����
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, gamerVO.getUserID());
		pstmt.setString(2, gamerVO.getUserPassword());
		pstmt.setString(3, gamerVO.getUserGender());
		pstmt.setString(4, gamerVO.getImage());	
		// �� SQL���� ������ ó�� ����� ����
		count= pstmt.executeUpdate(); //������ ��¥ ����!
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
			AlertDisplay.alertDisplay(1, "������(DB) ����", "�������� �������µ� ������",e.toString());
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
			AlertDisplay.alertDisplay(1, "DB��� ����", "DB����ϴµ� ������",e.toString());
		} finally {
			try {
			// �� �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
			if (pstmt != null)
			pstmt.close();
			if (con != null)
			con.close();
			} catch (SQLException e) {
				AlertDisplay.alertDisplay(1, "DB������������", "DB�������� ����",e.toString());
			}
		}
		return count;
	  }	

	
	
	
	
	
	
}
