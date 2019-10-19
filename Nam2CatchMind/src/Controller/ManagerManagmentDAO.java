package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.ManagerManagmentVO;

public class ManagerManagmentDAO {
	/* * * * * * * * * * * * * * * * 
	 * 메니저 메인창에서 동작을 위한 클래스
	 * 
	 * * * * * * * * * * * * * * * */
	
	/*
	 * 관리자 로그인시 시간을 저장하기 위한 함수
	 * */
	   public int setEnterTime(String userId ) {
	       String sql = "insert into usertime (UserID,EnterTime) values (?,now())";
	         Connection con = null;
	         PreparedStatement pstmt = null; 
	         int count=0;
	         try {
	         // ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
	         con = DBUtil.getConnection();
	         // ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
	         pstmt = con.prepareStatement(sql);
	         pstmt.setString(1, userId);      
	         // ⑤ SQL문을 수행후 처리 결과를 얻어옴
	         count= pstmt.executeUpdate(); //쿼리문 진짜 실행! // 
	         } catch (SQLException e) {
	            System.out.println("e=[" + e + "]");
	            AlertDisplay.alertDisplay(1, "쿼리문(DB) 오류", "쿼리문을 가져오는데 실패함",e.toString());
	         } catch (Exception e) {
	            System.out.println("e=[" + e + "]");
	            AlertDisplay.alertDisplay(1, "DB등록 오류", "DB등록하는데 실패함",e.toString());
	         } finally {
	            try {
	            // ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
	            if (pstmt != null)
	            pstmt.close();
	            if (con != null)
	            con.close();
	            } catch (SQLException e) {
	               AlertDisplay.alertDisplay(1, "DB연결해제오류", "DB연결해제 오류",e.toString());
	            }
	         }
	         return count;
	   }
	   
	
	/*
	 * 관리자 관리탭에서 관리자 권한을 가진 유저들만 조회하기 위한 함수
	 * */
	public ArrayList<ManagerManagmentVO> getManagerAccessUserView(String userID) {
		System.out.println("생성된 방 상태 조회");
		ArrayList<ManagerManagmentVO> list = new ArrayList<ManagerManagmentVO>();
		String dml = "select * from Nam2CatchMind.UserInfo where UserAccess > 0";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 디비에서 가져온 데이터를 임시로 저장하고 있는 공간
		ManagerManagmentVO mmVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mmVo = new ManagerManagmentVO(
						rs.getString(1), rs.getInt(2), rs.getString(3));
				list.add(mmVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}
	
	
	
	/*
	 * 관리자 관리탭에서 유저들만 조회하기 위한 명령어
	 * */
	public ArrayList<ManagerManagmentVO> getGameUserView(String userID) {
		System.out.println("생성된 방 상태 조회");
		ArrayList<ManagerManagmentVO> list = new ArrayList<ManagerManagmentVO>();
		String dml = "select * from Nam2CatchMind.UserInfo where UserAccess = 0";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 디비에서 가져온 데이터를 임시로 저장하고 있는 공간
		ManagerManagmentVO mmVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mmVo = new ManagerManagmentVO(
						rs.getString(1), rs.getInt(2), rs.getString(3));
				list.add(mmVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	
	/*
	 * 관리자의 정보를 가지고 오기위한 함수
	 * 
	 * String userID, String userPassword, int userAccess, 
	 * String image, String threadState,String enterTime, String outTime
	 * 
	 * */
	public ArrayList<ManagerManagmentVO> getUserTotalData() {
		ArrayList<ManagerManagmentVO> list = new ArrayList<ManagerManagmentVO>();
		String dml = "select * from schoolchild";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 디비에서 가져온 데이터를 임시로 저장하고 있는 공간
		ManagerManagmentVO mmVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				//String userID, String userPassword, int userAccess, 
				//String image, String threadState,String enterTime, String outTime
				mmVO = new ManagerManagmentVO(rs.getString(1), rs.getString(2), rs.getInt(3),
						rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(7));
				list.add(mmVO);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}
	
}
