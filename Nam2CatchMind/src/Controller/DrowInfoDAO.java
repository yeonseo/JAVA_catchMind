package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Controller.DBUtil;
import Model.DrowInfoVO;



public class DrowInfoDAO {

	// ① data input
		public int getDrowRegiste(DrowInfoVO dvo) throws Exception {
			// ② 데이터 처리를 위한 SQL 문
			String dml = "insert into drowInfo "
					+ "(x, y, draw, color)" 
					+ " values " + "(?, ?, ?, ?)";
			Connection con = null;
			PreparedStatement pstmt = null; // ������ ���Ѱ�
			int count = 0;
			try {
				// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
				con = DBUtil.getConnection();
				// sVo = new StudentVO();
				// ④ 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
				pstmt = con.prepareStatement(dml);
				pstmt.setDouble(1, dvo.getX());
				pstmt.setDouble(2, dvo.getY());
				pstmt.setBoolean(3, dvo.isDraw());
				pstmt.setInt(4, dvo.getDBColor());

				// ⑤ SQL문을 수행후 처리 결과를 얻어옴
				count = pstmt.executeUpdate();
				System.out.println("????"+count);
			} catch (SQLException e) {
				System.out.println("e=[" + e + "]");
			} catch (Exception e) {
				System.out.println("e=[" + e + "]");
			} finally {
				try {
					// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException e) {
				}
			}
			return count;
		}
		
	public static ArrayList<DrowInfoVO> getDrowTotal() {
		ArrayList<DrowInfoVO> list = new ArrayList<DrowInfoVO>();
		String tml = "select * from drowInfo";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DrowInfoVO emVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(tml);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				emVo = new DrowInfoVO(rs.getDouble(1), rs.getDouble(2), rs.getBoolean(3), rs.getInt(4));
				list.add(emVo);
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
