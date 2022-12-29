package svc;

import java.sql.Connection;
import java.util.List;

import dao.BoardDAO;
import dao.ProductDAO;
import db.JdbcUtil;
import vo.ProductBean;

public class CartListProService {

	public List<ProductBean> getCartlist(int member_idx, int startRow, int listLimit) {
		List<ProductBean> cartlist = null;
		Connection con = JdbcUtil.getConnection();
		
		ProductDAO dao = ProductDAO.getInstance();
		
		dao.setConnection(con);
			
		cartlist = dao.getCartList(member_idx,startRow,listLimit);
		
		JdbcUtil.close(con);
		
		return cartlist;
	}

	public int getCartListCount(int member_idx) {
		int listCount = 0;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		ProductDAO dao = ProductDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 객체의 selectBoardListCount() 메서드를 호출하여 글목록 갯수 조회 작업 수행
		// => 파라미터 : 검색어     리턴타입 : int(listCount)
		listCount = dao.selectCartListCount(member_idx);
		
		// 공통작업-4. Connection 객체 반환하기
		JdbcUtil.close(con);
		
		return listCount;
	}

	public List<ProductBean> getWishlist(int member_idx, int startRow, int listLimit) {
		List<ProductBean> wishlist = null;
		Connection con = JdbcUtil.getConnection();
		
		ProductDAO dao = ProductDAO.getInstance();
		
		dao.setConnection(con);
			
		wishlist = dao.getWishList(member_idx,startRow,listLimit);
		
		JdbcUtil.close(con);
		
		return wishlist;
	}

	public int getWishListCount(int member_idx) {
		int listCount = 0;
		
		Connection con = JdbcUtil.getConnection();
		
		ProductDAO dao = ProductDAO.getInstance();
		
		dao.setConnection(con);
		
		listCount = dao.selectWishListCount(member_idx);
		
		JdbcUtil.close(con);
		
		return listCount;
	}

	
	
	
}
