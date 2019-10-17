package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.GamerVO;

/*
 * 회원가입에 대한 내용 by JM 191016
	 * 
	 * 아이디,비밀번호,성별, 이미지 값이 들어가야함.
	 * 다 String으로
	 * 
	 * 1.아이디 중복검사
	 * boolean checkUserID(String UserID )
	 * sql=select * from UserInfo where UserId like ?
	 * String userID=UserID; /// 찾는 아이디
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

public class GamerDAO {
		
	//회원 등록
	
	  public int getGamerRegistration(GamerVO gamerVO) {
		 String sql = "insert into UserInfo "+"(UserID,UserPassword,UserGender,UserImage)" + " values " + "(?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null; 
		int count=0;
		try {
		// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
		con = DBUtil.getConnection();
		// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, gamerVO.getUserID());
		pstmt.setString(2, gamerVO.getUserPassword());
		pstmt.setString(3, gamerVO.getUserGender());
		pstmt.setString(4, gamerVO.getImage());	
		// ⑤ SQL문을 수행후 처리 결과를 얻어옴
		count= pstmt.executeUpdate(); //쿼리문 진짜 실행!
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
			DBUtil.alertDisplay(1, "쿼리문(DB) 오류", "쿼리문을 가져오는데 실패함",e.toString());
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
			DBUtil.alertDisplay(1, "DB등록 오류", "DB등록하는데 실패함",e.toString());
		} finally {
			try {
			// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
			if (pstmt != null)
			pstmt.close();
			if (con != null)
			con.close();
			} catch (SQLException e) {
				DBUtil.alertDisplay(1, "DB연결해제오류", "DB연결해제 오류",e.toString());
			}
		}
		return count;
	  }	

	
	
	
	
	
	
}
