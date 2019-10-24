package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Model.ManagerManagmentVO;

public class ManagerManagmentDAO {
	/*
	 * * * * * * * * * * * * * * * * 메니저 메인창에서 동작을 위한 클래스
	 * 
	 * * * * * * * * * * * * * * *
	 */

	/*
	 * 관리자 로그인시 시간을 저장하기 위한 함수
	 */
	public int setEnterTime(String userId) {
		String sql = "insert into usertime (UserID,EnterTime) values (?,now())";
		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			count = pstmt.executeUpdate(); // 쿼리문 진짜 실행! //
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

	/*
	 * 시간을 가져오기 위한 함수
	 */
	// 모든 유저의 시작시간 찾기
	public ArrayList<String> getCurrentTime(String userId) {
		ArrayList<String> list = new ArrayList<String>();
		String currentTime = null;
		String sql = "select EnterTime from usertime where UserID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleDateFormat sdf;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
				Date enterTime = rs.getTimestamp("EnterTime");
				currentTime = sdf.format(enterTime);
				list.add(currentTime);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "시간 가져오기 오류", "시간 가져오기 오류", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "시간 가져오기 오류", "시간 가져오기 실패", e.toString());
			}
		}
		return list;
	}

	// 특정 접근권한 유저의 시작시간 찾기
	public ArrayList<String> getFindUseAccessCurrentTime(int userAccess) {
		ArrayList<String> list = new ArrayList<String>();
		String currentTime = null;
		String sql = "select EnterTime from usertime join UserInfo on usertime.UserID=UserInfo.UserID where UserAccess = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleDateFormat sdf;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userAccess);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
				Date enterTime = rs.getTimestamp("EnterTime");
				currentTime = sdf.format(enterTime);
				list.add(currentTime);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "시간 가져오기 오류", "시간 가져오기 오류", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "시간 가져오기 오류", "시간 가져오기 실패", e.toString());
			}
		}
		return list;
	}

	/*
	 * 관리자 관리탭에서 관리자 권한을 가진 유저들만 조회하기 위한 함수
	 */
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
				mmVo = new ManagerManagmentVO(rs.getString(1), rs.getInt(2), rs.getString(3));
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
	 * 특정 관리자의 정보를 가지고 오기위한 함수
	 * 
	 * String userID, String userPassword, int userAccess, String image, String
	 * threadState,String enterTime, String outTime
	 * 
	 */
	public ManagerManagmentVO getManagerUserInfoForMainData(String managerID) {
		boolean check = false;
		String sql = "SELECT ui.UserID, ui.UserAccess, ugs.ThreadState " + "from UserInfo as ui "
				+ "left join UserGameState as ugs on ugs.UserID = ui.UserID where ui.UserID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 디비에서 가져온 데이터를 임시로 저장하고 있는 공간
		ManagerManagmentVO mmVO = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, managerID);
			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			con = DBUtil.getConnection();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// String userID, String userPassword, int userAccess,
				// String image, String threadState,String enterTime, String outTime
				mmVO = new ManagerManagmentVO(rs.getString(1), rs.getInt(2), rs.getString(3));
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
			AlertDisplay.alertDisplay(1, "쿼리문(DB) 오류", "쿼리문을 가져오는데 실패함", e.toString());
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
		return mmVO;
	}

	/*
	 * 관리자 관리탭에서 유저들만 조회하기 위한 명령어
	 */
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
				mmVo = new ManagerManagmentVO(rs.getString(1), rs.getInt(2), rs.getString(3));
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
	 * 관리자 메인텝에서 테이블뷰에 쓰일 함
	 */
	public ArrayList<ManagerManagmentVO> getTableViewRoomInfoTotal() {
		ArrayList<ManagerManagmentVO> list = new ArrayList<ManagerManagmentVO>();
		String dml = "select RoomName, ThreadState, Gamer1, Gamer2, GameRunOrWaitState from Nam2CatchMind.UserGameRoom";

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
				mmVO = new ManagerManagmentVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
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

	/*
	 * 관리자 관리 탭에서 관리자 정보를 가져올 함수
	 */
	public ArrayList<ManagerManagmentVO> getTableViewManagerInfoTotal() {
		ArrayList<ManagerManagmentVO> list = new ArrayList<ManagerManagmentVO>();
		String dml = "SELECT ui.UserID, ui.UserPassword, ui.UserAccess, ui.UserImage, ugs.ThreadState "
				+ "FROM UserInfo AS ui  left join UserGameState as ugs "
				+ "on ugs.UserID = ui.UserID where UserAccess > 0;";

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
				mmVO = new ManagerManagmentVO(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
						rs.getString(5));
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

	/*
	 * 관리자의 권한을 바꾸기 위한 함수
	 */
	public int setManagerAccess(String managerID, int managerAccess) {
		String sql = "update Nam2CatchMind.UserInfo set UserAccess =? where UserID=?";
		System.out.println("ttt1");
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("ttt2");
		int count = 0;
		try {
			con = DBUtil.getConnection();
			System.out.println("ttt3");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, managerAccess);
			pstmt.setString(2, managerID);
			count = pstmt.executeUpdate();
			System.out.println("ttt4");
			if (count == 1) {
				AlertDisplay.alertDisplay(5, "권한 수정", "권한 수정성공!", "권한 수정되었습니다.");
			} else {

				AlertDisplay.alertDisplay(1, "권한 수정", "권한 수정실패", "권한 수정하는데 실패했습니다.");
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return count;
	}

	public ArrayList<ManagerManagmentVO> getTableViewGamerInfoTotal() {
		ArrayList<ManagerManagmentVO> list = new ArrayList<ManagerManagmentVO>();
		String dml = "SELECT ui.UserID, ui.UserImage, ugs.ThreadState "
				+ "FROM UserInfo AS ui  left join UserGameState as ugs "
				+ "on ugs.UserID = ui.UserID where UserAccess = 0;";

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
				mmVO = new ManagerManagmentVO(rs.getString(1), rs.getString(2), rs.getString(3));
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
