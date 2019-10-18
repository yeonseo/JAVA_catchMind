package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.UserVO;

public class GamerDAO {

	
	/*
	 * 메니저 등록하기
	 * 
	 * */
	public int getManagerRegistration(UserVO userVO) {
		System.out.println("manager 등록");
		String sql = "insert into UserInfo (UserID,UserPassword,UserAccess,UserImage) values (?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userVO.getUserID());
			pstmt.setString(2, userVO.getUserPassword());
			pstmt.setInt(3, userVO.getUserAccess());
			pstmt.setString(4, userVO.getImage());
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
	
	/*
	 * 게임 회원 등록
	 * */

	public int getGamerRegistration(UserVO userVO) {
		String sql = "insert into UserInfo (UserID,UserPassword,UserImage) values (?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 회원 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userVO.getUserID());
			pstmt.setString(2, userVO.getUserPassword());
			pstmt.setString(3, userVO.getImage());
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

	// 아이디 중복 검사
	public boolean checkUserID(UserVO userVo) {
		boolean check = false;
		String sql = "select * from UserInfo where UserID like ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		String id = userVo.getUserID();
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				check = true;
				String txtid = rs.getString(1).toString();
				System.out.println(txtid);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "아이디중복검사 오류", "아이디 중복 오류입니다.", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "아이디중복검사 오류", "아이디 중복창 닫기 실패", e.toString());
			}
		}
		return check;
	}

	// 로그인 아이디 및 패스워드 확인
	public ArrayList<UserVO> getLoginCheck(String gamerId, String gamerPwd) {
		String sql = "select * from UserInfo where UserID=? && UserPassword=?"; // 찾는 아이디와 찾는 패스워드가 DB에서 둘다 맞는걸 찾아라
		ArrayList<UserVO> list = new ArrayList<UserVO>(); // 찾은 회원정보 넣는곳
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO uvo = null;
		String loginId = gamerId;
		String loginPassword = gamerPwd;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, loginId); // 찾는 아이디
			pstmt.setString(2, loginPassword); // 찾는 패스워드
			rs = pstmt.executeQuery();
			while (rs.next()) {
				uvo = new UserVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)); // 회원 정보 모두 넣음
				list.add(uvo);
			}
			System.out.println("DAO에서list 사이즈" + list.size());
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "로그인 아이디 및 패스워드 찾기 오류", "로그인 아이디 및 패스워드 찾기 오류", e.toString());

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "로그인창 닫기 오류", "로그인창 닫기 오류", e.toString());
			}
		}

		return list;
	}

}