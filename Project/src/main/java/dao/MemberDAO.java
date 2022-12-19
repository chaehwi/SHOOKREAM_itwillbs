package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.JdbcUtil;
import vo.MemberBean;

public class MemberDAO {
private MemberDAO() {}
	
	private static MemberDAO instance = new MemberDAO();

	public static MemberDAO getInstance() {
		return instance;
	}
	// ----------------------------------------------------------------------------------
	// 데이터베이스 접근에 사용할 Connection 객체를 Service 객체로부터 전달받기 위한
	// Connection 타입 멤버변수 선언 및 Setter 메서드 정의
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	
	// ----------------------------------------------------------------------------------
	// 로그인
	public boolean isLoginUser(String id, String pass) {
		boolean isLogintUser = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT member_id,member_pass from member where member_id=? and member_pass=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pass);
		
			rs = pstmt.executeQuery();
			if(rs.next()) {
				isLogintUser = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isLogintUser;
	}
	// 로그인


	
	// 회원가입
	public int insertMember(MemberBean member) {
		int insertCount = 0;
		
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		
		try {
			int member_idx = 1; // 회원 idx 처리
			String sql = "SELECT MAX(member_idx) FROM member";
			pstmt= con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member_idx = rs.getInt(1) + 1;
			} 
			
			sql = "INSERT INTO member VALUES(?,?,?,?,?,now(),?,?,?,?)";
			pstmt2= con.prepareStatement(sql);
			
			pstmt2.setInt(1, member_idx);
			pstmt2.setString(2, member.getMember_id());
			pstmt2.setString(3, member.getMember_name());
			pstmt2.setString(4, member.getMember_pass());
			pstmt2.setString(5, member.getMember_email());
			pstmt2.setString(6, member.getMember_phone());
			pstmt2.setInt(7, 0);
			pstmt2.setInt(8, 0);
			pstmt2.setString(9, member.getMember_address());
		
			insertCount = pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류! - insertMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(pstmt2);
		}
		return insertCount;
	}
	// 회원가입
	
	public boolean isDeleteUser(String pass) {
		int deleteCount = 0;
		boolean isDeleteSuccess = false;
		PreparedStatement pstmt = null;
		
		try {
			
			String sql = "DELETE FROM member WHERE member_pass=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pass);
			deleteCount = pstmt.executeUpdate();
			if(deleteCount>0) {
				isDeleteSuccess =true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// DB 자원 반환
			JdbcUtil.close(pstmt);
		}
		
		return isDeleteSuccess;
	} //회원삭제
	
	// 회원 정보 수정 updateMember()
	public int updateMember(MemberBean member) {
		int updateMember = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE member "
									+ "SET "
									+ "member_name=?,"
									+ "member_id=?,"
									+ "member_pass=?,"
									+ "member_address=?,"
									+ "member_email=?,"
									+ "member_phone=?"
									+ "WHERE member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getMember_name());
			pstmt.setString(2, member.getMember_id());
			pstmt.setString(3, member.getMember_pass());
			pstmt.setString(4, member.getMember_address());
			pstmt.setString(5, member.getMember_email());
			pstmt.setString(6, member.getMember_phone());
			pstmt.setString(7, member.getMember_id());
			
			updateMember = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("sql 구문 오류 - updateMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		return updateMember;
		
	} // 회원 정보 수정 끝


	public MemberBean getInfo(String id) {
		MemberBean vo = null;
		ResultSet rs  = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "SELECT * FROM member WHERE member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo = new MemberBean();
				vo.setMember_name(rs.getString("member_name"));
				vo.setMember_id(rs.getString("member_id"));
				vo.setMember_pass(rs.getString("member_pass"));
				vo.setMember_address(rs.getString("member_address"));
				vo.setMember_email(rs.getString("member_email"));
				vo.setMember_phone(rs.getString("member_phone"));
				System.out.println(vo);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return vo; 
	} // 회원 목록 끝



	// ID 중복 체크를 위한 회원 ID 조회
	public int selectAllId(String id) {
		int result = 0;
		
		ResultSet rs  = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "SELECT member_id FROM member WHERE member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = 1; // 존재할 경우
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - selectAllId()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		return result;
	}
	//----------------------회원 조회(관리자)--------------------------
			public List<MemberBean> selectMemberList() {
				List<MemberBean> memberList = null;
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				//전체 회원 조회 -> WHERE 절 넣지 않음.
				String sql = "SELECT * FROM member";
				
				try {
					pstmt = con.prepareStatement(sql);
					rs = pstmt.executeQuery();
					
					memberList = new ArrayList<MemberBean>();
					while(rs.next()) {
						//있을 경우 member 객체에 저장
						MemberBean member = new MemberBean();
						member.setMember_idx(rs.getInt("member_idx")); // 회원번호
						member.setMember_id(rs.getString("member_id")); // ID
						member.setMember_pass(rs.getString("member_pass")); // 패스워드
						member.setMember_name(rs.getString("member_name")); // 이름(성함)
						member.setMember_email(rs.getString("member_email")); // 이메일
						member.setMember_date(rs.getDate("member_date")); // 가입날짜
						member.setMember_phone(rs.getString("member_phone")); // 휴대폰 번호
						member.setMember_address(rs.getString("member_address")); // 주소
						member.setMember_dec(rs.getInt("member_dec")); // 신고횟수
						member.setMember_point(rs.getInt("member_point")); // 적립금
						
						memberList.add(member);
						//확인작업
						System.out.println(member);
					}
					
				} catch (SQLException e) {
					System.out.println("회원조회 실패 - 관리자");
					e.printStackTrace();
				} finally {
					JdbcUtil.close(rs);
					JdbcUtil.close(pstmt);
				}
				
				return memberList;
			}
}