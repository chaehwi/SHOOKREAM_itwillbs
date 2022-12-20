package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.JdbcUtil;
import vo.ProductBean;

public class ProductDAO {
private ProductDAO() {}
	
	private static ProductDAO instance = new ProductDAO();

	public static ProductDAO getInstance() {
		return instance;
	}
	// ----------------------------------------------------------------------------------
	// 데이터베이스 접근에 사용할 Connection 객체를 Service 객체로부터 전달받기 위한
	// Connection 타입 멤버변수 선언 및 Setter 메서드 정의
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
//---------------------상품 등록-------------------

	public int insertProduct(ProductBean product) {
		int insertCount = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		try {
			//----------------idx 작업------------------
			String sql = "SELECT MAX(product_idx) FROM product";
			int idx = 1; // 새 글 번호
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) { 
				 //true -> 조회결과가 있을 경우 (= 게시물이 하나라도 존재할 경우)
				 //존재하지 않을 경우 rs.next는 false , DB에서는 NULL이 표기된다.
				idx = rs.getInt(1) + 1;
			}
			System.out.println("새글 번호 :" + idx);
			
			//----------------상품 등록----------------------
			sql = "INSERT INTO product VALUES(?,?,?,?,?,0,0,?,0,?,?,?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx); //idx
			pstmt.setString(2, product.getProduct_name()); //name
			pstmt.setString(3, product.getProduct_brand()); //brand
			pstmt.setString(4, product.getProduct_size()); // size
			pstmt.setInt(5, product.getProduct_price()); // price
			pstmt.setInt(6, product.getProduct_amount()); // amount
			pstmt.setString(7, product.getProduct_exp());
			pstmt.setString(8, product.getProduct_detail_exp());
			pstmt.setString(9, product.getProduct_color());
			pstmt.setDouble(10, product.getProduct_discount_price());
			pstmt.setString(11, product.getProduct_img());
			
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("상품등록 - 관리자");
			e.printStackTrace();
		}
		
		
		return insertCount;
	}
	
	//----------------장바 구니----------------------
		public List<ProductBean> getCartList() {
			 List<ProductBean> cartlist = null;
			 PreparedStatement pstmt =  null;
			 ResultSet rs = null;
				
			 String sql ="SELECT c.cart_idx,p.product_name, p.product_size, p.product_price,p.product_brand,p.product_image  "
			 		+ "FROM shookream.cart c join shookream.product p "
			 		+ "on c.product_idx = p.product_idx";
			 try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				cartlist = new ArrayList<ProductBean>();
				while(rs.next()) {
					ProductBean vo = new ProductBean();
					vo.setCart_idx(rs.getInt("cart_idx"));
					vo.setProduct_name(rs.getString("product_name"));
					vo.setProduct_size(rs.getNString("product_size"));
					vo.setProduct_price(rs.getInt("product_price"));
					vo.setProduct_brand(rs.getNString("product_brand"));
					vo.setProduct_img(rs.getString("product_image"));
					cartlist.add(vo);
				}
			 } catch (SQLException e) {
				e.printStackTrace();
			}finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
			return cartlist;
		}
	
	
}//DAO 끝
