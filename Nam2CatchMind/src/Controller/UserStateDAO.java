package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.ManagerManagmentVO;

public class UserStateDAO {
	
	/*
	 * 유저들의 상태 등록
	 * 
	 * */
	public int getUserStateRegistration(String userID, String userThreadState) {
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
			pstmt.setString(1, userID);
			pstmt.setString(2, userThreadState);
			pstmt.setString(3, userID);
			pstmt.setString(4, userThreadState);
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

	public int getUserGameRoomRegistration(String roomName,String threadState,
			String managerID, String makeRoomUserID, String enterRoomUserID,
			String gameRunOrWaitState) {
		System.out.println("방 등록");
		String sql = "insert into UserGameRoom (RoomName,ThreadState,ManagerID,Gamer1,Gamer2,GameRunOrWaitState) values (?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, roomName);
			pstmt.setString(2, threadState);
			pstmt.setString(3, managerID);
			pstmt.setString(4, makeRoomUserID);
			pstmt.setString(5, enterRoomUserID);
			pstmt.setString(6, gameRunOrWaitState);
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

	public boolean checkRoomName(ManagerManagmentVO mmVO) {
		boolean check = false;
		String sql="select * from UserGameRoom where RoomName like ?";
		Connection con=null;
		PreparedStatement pstmt=null;
		
		ResultSet rs=null; 
		String roomName=mmVO.getRoomName();	
		try {
			con=DBUtil.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,roomName);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				check=true;
				String txtRoomName=rs.getString(1).toString();
				System.out.println(txtRoomName);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "아이디중복검사 오류", "아이디 중복 오류입니다.", e.toString());
		}finally {
			try {
				if(rs !=null) rs.close();
				if(pstmt !=null)  pstmt.close(); 
				if(con != null) con.close();  		
			}catch(Exception e) {
				AlertDisplay.alertDisplay(1, "아이디중복검사 오류", "아이디 중복창 닫기 실패", e.toString());
			}		
		}	
		return check;
	}
	
	

}