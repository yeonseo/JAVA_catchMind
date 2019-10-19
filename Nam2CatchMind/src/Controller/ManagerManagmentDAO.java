package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.ManagerManagmentVO;

public class ManagerManagmentDAO {
	/* * * * * * * * * * * * * * * * 
	 * 메니저 메인창에서 회원관리를 위한 클래스
	 * 
	 * * * * * * * * * * * * * * * */
	
	
	/*
	 * 관리자 관리탭에서 관리자 권한을 가진 유저들만 조회하기 위한 명령어
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
	 * 관리자 관리탭에서 관리자 권한을 가진 유저들만 조회하기 위한 명령어
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
	
	
	
	/* 관리자 관리 텝
	 * 관리자의 아이디로 권한과 상태를 가지고 온다
	 * */
	public ArrayList<ManagerManagmentVO> getUserStateView(String userID) {
		System.out.println("관리자 권한 상태 조회");
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
}
