package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Model.MakeRoomVO;
import Model.UserVO;

public class GamerDAO {

	public GamerDAO() {
		super();
	}

	/*
	 * 메니저 등록하기
	 * 
	 */
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
	 */
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
			System.out.println("DAO에서 " + list.get(0).getImage());
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

	// 시작시간 등록
	public int setCurrentTime(String userId) {
		String sql = "insert into usertime  (UserID,EnterTime) values (?,now())";
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

	// 시작시간 찾기
	public String getCurrentTime(String userId) {
		String sql = "select EnterTime from  usertime where UserID like ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		String currentTime = null;
		ResultSet rs = null;
		String id = userId;
		SimpleDateFormat sdf;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = rs.getTimestamp("EnterTime");
				currentTime = sdf.format(date);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "로그인된 이미지 가져오기 오류", "로그인된 아이디와 이미지 가져오기 오류", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "로그인된 이미지 가져오기 오류", "로그인된 이미지 가져오기 오류창 닫기 실패", e.toString());
			}
		}

		return currentTime;
	}

	// 이미지를 찾기
	public String getUserLoginIdAndImage(String userID) {
		String sql = "select UserImage from  userinfo where UserID like ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		String userImg = null;
		ResultSet rs = null;
		String id = userID;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userImg = rs.getString(1);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "로그인된 이미지 가져오기 오류", "로그인된 아이디와 이미지 가져오기 오류", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "로그인된 이미지 가져오기 오류", "로그인된 이미지 가져오기 오류창 닫기 실패", e.toString());
			}
		}
		return userImg;
	}

	// 기존 비밀번호 찾기 (회원 수정에서)
	public String getUserPreviousCheck(String userID) {

		String sql = "select UserPassword from  userinfo where UserID like ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		String userPreviousPwd = null;
		ResultSet rs = null;
		String id = userID;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userPreviousPwd = rs.getString(1);
			}
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "기존에 있던 패스워드 찾기 오류", "기존에 있던 패스워드 찾기 오류", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "기존에 있던 패스워드 찾기 오류", "기존에 있던 패스워드 찾기 오류창 닫기 실패", e.toString());
			}
		}

		return userPreviousPwd;
	}

	// 바꾼 이미지 저장(회원 수정에서)
	public int getUserImageChange(String FileName, String userId) throws Exception {
		String sql = "update userinfo set UserImage =? where UserID=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, FileName);
			pstmt.setString(2, userId);

			i = pstmt.executeUpdate();

			if (i == 1) {
				AlertDisplay.alertDisplay(5, "이미지 수정", "이미지가 수정성공!", "이미지가 수정되었습니다.");

			} else {

				AlertDisplay.alertDisplay(1, "이미지 수정", "이미지가 수정실패", "이미지를 수정하는데 실패했습니다.");
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
		return i;
	}

	// 바꾼 비밀번호 저장(회원 수정에서)
	public int getUserPwdChange(String changePwd, String userId) {
		String sql = "update userinfo set UserPassword=? where UserID=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, changePwd);
			pstmt.setString(2, userId);

			i = pstmt.executeUpdate();

			if (i == 1) {
				System.out.println("비밀번호 수정 ok");
			} else {
				System.out.println("비밀번호 수정 실패");
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
		return i;
	}
	
	//등록한 방 이름과 방장 이름 가져오기!
	public ArrayList<MakeRoomVO> getMakeRoomName() {
		
		ArrayList<MakeRoomVO> list = new ArrayList<MakeRoomVO>();
		String dml = "select * from usergameroom";

		Connection con = null;
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		MakeRoomVO MakeRoomVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MakeRoomVO = new MakeRoomVO(rs.getString(1),rs.getString(4),rs.getString(5),rs.getString(6));
				list.add(MakeRoomVO);		
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
	
	//게임유저2 방 등록 하기 wait 상태일때만
	public int getUser2EnterGameRoom(String userId , String makeRoomName ) {
		String sql = "update usergameroom set Gamer2=? where RoomName=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, makeRoomName);

			i = pstmt.executeUpdate();

			if (i == 1) {
				System.out.println("게임방 user2 들어가기 성공!");
			} else {
				System.out.println("게임방 user2 들어가기 실패!");
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
		return i;
		
	}
	
	
	//방장의 이름으로 방이름 가져오기
	public String getRoomNamefromGamer1(String userID) {
		String sql = "select RoomName from usergameroom where Gamer1=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		String RoomName=null;
		ResultSet rs = null;
		String Gamer1 = userID;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Gamer1);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				RoomName = rs.getString(1);
				System.out.println("test : "+RoomName);
			}
			System.out.println("sql에서 방이름 : "+RoomName);
		} catch (Exception e) {
			AlertDisplay.alertDisplay(1, "기존에 있던 방이름 찾기 오류", "기존에 있던 방이름 찾기 오류", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "기존에 있던 방이름 찾기 오류", "기존에 있던 방이름 찾기 오류창 닫기 실패", e.toString());
			}
		}
		return RoomName;
	}
	
	//gamer2의 이름으로 방이름 가져오기
		public String getRoomNamefromGamer2(String userID) {
			String sql = "select RoomName from usergameroom where Gamer2=?";

			Connection con = null;
			PreparedStatement pstmt = null;
			String RoomName=null;
			ResultSet rs = null;
			String Gamer2 = userID;
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, Gamer2);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					RoomName = rs.getString(1);
				}
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "기존에 있던 방이름 찾기 오류", "기존에 있던 방이름 찾기 오류", e.toString());
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (Exception e) {
					AlertDisplay.alertDisplay(1, "기존에 있던 방이름 찾기 오류", "기존에 있던 방이름 찾기 오류창 닫기 실패", e.toString());
				}
			}
			return RoomName;
		}
	
		//방이름으로 user1,user2 찾기
		public ArrayList<MakeRoomVO> getGamer1andGamer2(String MakeRoom) {
			String sql = "select * from usergameroom where RoomName=?";
			MakeRoomVO mrvo;
			Connection con = null;
			PreparedStatement pstmt = null;
			String RoomName=null;
			ResultSet rs = null;
			ArrayList<MakeRoomVO> list = new ArrayList<MakeRoomVO>();
			String makeRoom = MakeRoom;
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, makeRoom);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					mrvo = new MakeRoomVO(rs.getString(4), rs.getString(5));
					list.add(mrvo);
				}
				
			} catch (Exception e) {
				AlertDisplay.alertDisplay(1, "유저1,유저2 찾기 실패", "방이름으로 유저들 찾기 실패!", e.toString());
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (Exception e) {
					AlertDisplay.alertDisplay(1, "유저찾기실패", "방이름으로 유저들 찾기 오류창 닫기 실패", e.toString());
				}
			}
			return list;
		}
		
		//방상태 Update wait-->GameRun 
		public int MakeRoomUpdateState(String RoomName) {
			String sql = "update usergameroom set GameRunOrWaitState='GameRun' where RoomName=? ";
			Connection con = null;
			PreparedStatement pstmt = null;
			int i = 0;
			try {
				con = DBUtil.getConnection();

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, RoomName);

				i = pstmt.executeUpdate();

				if (i == 1) {
					System.out.println("방상태 업데이트 ok");
				} else {
					System.out.println("방상태 업데이트 실패");
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
			return i;
		}
		
}
