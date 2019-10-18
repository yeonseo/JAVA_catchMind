package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.UserStateVO;
import Model.UserVO;

public class UserStateDAO {
	
	/*
	 * 유저들의 상태 등록
	 * 
	 * */
	public int getUserStateRegistration(UserStateVO usVO) {
		System.out.println("상태 등록");
		String sql = "insert into UserGameState (UserID,ThreadState) values (?,?) ON DUPLICATE KEY UPDATE UserID=?, ThreadState=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, usVO.getUserID());
			pstmt.setString(2, usVO.getThreadState());
			pstmt.setString(3, usVO.getUserID());
			pstmt.setString(4, usVO.getThreadState());
			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			count = pstmt.executeUpdate(); // 쿼리문 진짜 실행! // 아이디,패스워드,이미지,성별 DB저장!
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
			AlertDisplay.alertDisplay(1, "쿼리문(DB) 오류", "쿼리문을 가져오는데 실패함", e.toString());
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
			AlertDisplay.alertDisplay(1, "DB등록 오류", "DB등록하는데 실패함", e.toString());
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				AlertDisplay.alertDisplay(1, "DB연결해제오류", "DB연결해제 오류", e.toString());
			}
		}
		return count;
	}

}