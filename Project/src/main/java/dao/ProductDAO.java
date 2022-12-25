package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db.JdbcUtil;
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
	
	
	//--------상품 등록 작업--------------
	public int insertProduct(ProductBean product) {
		int insertCount = 0; 
		int insertCount2 = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
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

			sql = "INSERT INTO product VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, idx); //idx
			pstmt2.setString(2, product.getProduct_name()); //name
			pstmt2.setString(3, product.getProduct_brand()); //brand
			pstmt2.setString(4, product.getProduct_size()); // size
			pstmt2.setInt(5, product.getProduct_price()); // price
			pstmt2.setInt(6, 0); // release price(쿠폰적용가격)
			pstmt2.setInt(7, 0); // buy price(누적가격)
			pstmt2.setInt(8, product.getProduct_amount()); // amount
			pstmt2.setInt(9, 0); // sell_count
			pstmt2.setString(10, product.getProduct_exp()); //요약 설명
			pstmt2.setString(11, product.getProduct_detail_exp()); //상세 설명
			pstmt2.setString(12, product.getProduct_color()); //색상
			pstmt2.setDouble(13, product.getProduct_discount_price()); //할인율
			
			insertCount = pstmt2.executeUpdate();
			
			//----------------이미지 등록------------------
			// image_idx 작업
			if(insertCount > 0) {
				sql = "SELECT MAX(image_idx) FROM image";
				int idx2 = 1; // 새 글 번호
				pstmt3 = con.prepareStatement(sql);
				rs = pstmt3.executeQuery();
				
				if(rs.next()) { 
					 //true -> 조회결과가 있을 경우 (= 게시물이 하나라도 존재할 경우)
					 //존재하지 않을 경우 rs.next는 false , DB에서는 NULL이 표기된다.
					idx2 = rs.getInt(1) + 1;
				}
				
				sql = "INSERT INTO image (image_idx, product_idx, image_main_file, image_real_file1, image_real_file2) VALUES(?,?,?,?,?)";
				pstmt4 = con.prepareStatement(sql);
				pstmt4.setInt(1, idx2);
				pstmt4.setInt(2, idx);
				pstmt4.setString(3, product.getProduct_img());
				pstmt4.setString(4, product.getProduct_img2());
				pstmt4.setString(5, product.getProduct_img3());
				
				insertCount2 = pstmt4.executeUpdate();
			}
			
			
		} catch (SQLException e) {
			System.out.println("상품등록 - 관리자");
			e.printStackTrace();
		} 
		return insertCount2;
	}



	// 상품 상세 정보 조회
	public ProductBean selectProduct(int product_idx) {
		ProductBean product = null;
		System.out.println(product_idx);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM product WHERE product_idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, product_idx);
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있을 경우
			if(rs.next()) {
				// ProductBean 객체 생성 후 조회 데이터 저장
				product = new ProductBean();
				product.setProduct_idx(rs.getInt("product_idx"));
				product.setProduct_name(rs.getString("product_name"));
				product.setProduct_brand(rs.getString("product_brand"));
				product.setProduct_price(rs.getInt("product_price"));
				product.setProduct_size(rs.getString("product_size"));
				product.setProduct_release_price(rs.getInt("product_release_price"));
				product.setProduct_buy_price(rs.getInt("product_buy_price"));
				product.setProduct_amount(rs.getInt("product_amount"));
				product.setProduct_sell_count(rs.getInt("product_sell_count"));
				product.setProduct_exp(rs.getString("product_exp"));
				product.setProduct_detail_exp(rs.getString("product_detail_exp"));
				product.setProduct_color(rs.getString("product_color"));
				product.setProduct_discount_price(rs.getDouble("product_discount_price"));
//				product.setProduct_img(rs.getString("product_img"));
				product.setProduct_date(rs.getTimestamp("product_date"));
//				System.out.println(product);
			}
		} catch (SQLException e) {
			System.out.println("SQL구문 오류 - selectProduct()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		return product;
	}

	// 관리자 - 상품 목록 조회
	public List<ProductBean> selectProductList() {
		ArrayList<ProductBean> productList = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, p.product_date, p.product_amount, p.product_color, i.image_main_file "
					+ "FROM shookream.product p join shookream.image i "
					+ "on p.product_idx = i.product_idx ORDER BY product_date desc";
			
//			String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, p.product_date, p.product_amount, p.product_color, i.image_main_file "
//					+ "FROM shookream.product p join shookream.image i "
//					+ "on p.product_idx = i.product_idx GROUP BY product_name ORDER BY product_date desc";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			productList = new ArrayList<ProductBean>();
			
			while(rs.next()) {
				ProductBean product = new ProductBean();
				product.setProduct_idx(rs.getInt("product_idx"));
				product.setProduct_name(rs.getString("product_name"));
				product.setProduct_brand(rs.getString("product_brand"));
//				product.setProduct_size(rs.getString("product_size"));
				product.setProduct_price(rs.getInt("product_price"));
//				product.setProduct_release_price(rs.getInt("product_release_price"));
//				product.setProduct_buy_price(rs.getInt("product_buy_price"));
				product.setProduct_amount(rs.getInt("product_amount"));
//				product.setProduct_sell_count(rs.getInt("product_sell_count"));
//				product.setProduct_exp(rs.getString("product_exp"));
//				product.setProduct_detail_exp(rs.getString("product_detail_exp"));
				product.setProduct_color(rs.getString("product_color"));
//				product.setProduct_discount_price(rs.getDouble("product_discount_price"));
				product.setProduct_img(rs.getString("image_main_file"));
				product.setProduct_date(rs.getTimestamp("product_date"));
				
				productList.add(product);
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - selectProductList()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		return productList;
	}
	
	//----------------장바 구니----------------------
		public int CartInsert(int product_idx, int member_idx) {
			int CartInsert = 0;
			PreparedStatement pstmt1,pstmt2 = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT MAX(cart_idx) FROM cart";
				int cart_idx = 1;
				pstmt1 = con.prepareStatement(sql);
				rs = pstmt1.executeQuery();
				
				if(rs.next()) { 
					cart_idx = rs.getInt(1) + 1;
				}
				System.out.println(cart_idx);
		 //--------------------장바구니 등록--------------------------------		
			sql = "INSERT INTO cart VALUES(?,?,?,now())";	
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, cart_idx);
			pstmt2.setInt(2, member_idx);	
			pstmt2.setInt(3, product_idx);	
			CartInsert = pstmt2.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return CartInsert;
		}
	
	
	
		public List<ProductBean> getCartList() {
			 List<ProductBean> cartlist = null;
			 PreparedStatement pstmt =  null;
			 ResultSet rs = null;
				
			 String sql ="SELECT c.cart_idx,p.product_name, p.product_size, p.product_price,p.product_brand,i.image_main_file,m.member_id "
			 		+ "FROM shookream.cart c join shookream.product p join shookream.image i join shookream.member m "
			 		+ "on c.product_idx = p.product_idx and c.product_idx = i.product_idx and c.member_idx = m.member_idx";
			 
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
					vo.setProduct_img(rs.getString("image_main_file"));
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

		// 메인 - 메인화면 베스트 상품 목록 조회
		public List<ProductBean> selectBestProductList() {
			ArrayList<ProductBean> productBestList = null;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, i.image_main_file "
						+ "FROM shookream.product p join shookream.image i "
						+ "on p.product_idx = i.product_idx GROUP BY product_name ORDER BY p.product_idx ASC";
				
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				productBestList = new ArrayList<ProductBean>();
				
				while(rs.next()) {
					ProductBean product = new ProductBean();
					product.setProduct_idx(rs.getInt("product_idx"));
					product.setProduct_name(rs.getString("product_name"));
					product.setProduct_brand(rs.getString("product_brand"));
//					product.setProduct_size(rs.getString("product_size"));
					product.setProduct_price(rs.getInt("product_price"));
//					product.setProduct_release_price(rs.getInt("product_release_price"));
//					product.setProduct_buy_price(rs.getInt("product_buy_price"));
////					product.setProduct_amount(rs.getInt("product_amount"));
//					product.setProduct_sell_count(rs.getInt("product_sell_count"));
//					product.setProduct_exp(rs.getString("product_exp"));
//					product.setProduct_detail_exp(rs.getString("product_detail_exp"));
//					product.setProduct_color(rs.getString("product_color"));
//					product.setProduct_discount_price(rs.getDouble("product_discount_price"));
					product.setProduct_img(rs.getString("image_main_file"));
//					product.setProduct_date(rs.getTimestamp("product_date"));
					
					productBestList.add(product);
				}
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 - selectBestProductList()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
			return productBestList;
		}

		
		// 메인 - 메인화면 최근 등록 상품 목록 조회
		public List<ProductBean> selectNewProductList() {
			ArrayList<ProductBean> productNewList = null;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, p.product_date, i.image_main_file "
				+ "FROM shookream.product p join shookream.image i "
				+ "on p.product_idx = i.product_idx GROUP BY product_name ORDER BY product_date desc";
				
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				productNewList = new ArrayList<ProductBean>();
				
				while(rs.next()) {
					ProductBean product = new ProductBean();
					product.setProduct_idx(rs.getInt("product_idx"));
					product.setProduct_name(rs.getString("product_name"));
					product.setProduct_brand(rs.getString("product_brand"));
//					product.setProduct_size(rs.getString("product_size"));
					product.setProduct_price(rs.getInt("product_price"));
//					product.setProduct_release_price(rs.getInt("product_release_price"));
//					product.setProduct_buy_price(rs.getInt("product_buy_price"));
//					product.setProduct_amount(rs.getInt("product_amount"));
//					product.setProduct_sell_count(rs.getInt("product_sell_count"));
//					product.setProduct_exp(rs.getString("product_exp"));
//					product.setProduct_detail_exp(rs.getString("product_detail_exp"));
//					product.setProduct_color(rs.getString("product_color"));
//					product.setProduct_discount_price(rs.getDouble("product_discount_price"));
					product.setProduct_img(rs.getString("image_main_file"));
					product.setProduct_date(rs.getTimestamp("product_date"));
					
					productNewList.add(product);
				}
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 - selectNewProductList()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
			return productNewList;
		}

		
		// 메인 - 메인화면 세일 상품 목록 조회
		public List<ProductBean> selectSaleProductList() {
			ArrayList<ProductBean> productSaleList = null;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, i.image_main_file "
						+ "FROM shookream.product p join shookream.image i "
						+ "on p.product_idx = i.product_idx "
						+ "WHERE product_discount_price > 0 "
						+ "GROUP BY product_name ORDER BY product_discount_price ASC";
				
//				String sql = "SELECT * FROM product "
//						+ "WHERE product_discount_price IS NOT NULL "
//						+ "ORDER BY product_discount_price";
				
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				productSaleList = new ArrayList<ProductBean>();
				
				while(rs.next()) {
					ProductBean product = new ProductBean();
					product.setProduct_idx(rs.getInt("product_idx"));
					product.setProduct_name(rs.getString("product_name"));
					product.setProduct_brand(rs.getString("product_brand"));
//					product.setProduct_size(rs.getString("product_size"));
					product.setProduct_price(rs.getInt("product_price"));
//					product.setProduct_release_price(rs.getInt("product_release_price"));
//					product.setProduct_buy_price(rs.getInt("product_buy_price"));
//					product.setProduct_amount(rs.getInt("product_amount"));
//					product.setProduct_sell_count(rs.getInt("product_sell_count"));
//					product.setProduct_exp(rs.getString("product_exp"));
//					product.setProduct_detail_exp(rs.getString("product_detail_exp"));
//					product.setProduct_color(rs.getString("product_color"));
//					product.setProduct_discount_price(rs.getDouble("product_discount_price"));
					product.setProduct_img(rs.getString("image_main_file"));
//					product.setProduct_date(rs.getTimestamp("product_date"));
					
					productSaleList.add(product);
				}
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 - selectSaleProductList()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
			return productSaleList;
		}

		// 메인 - 카테고리별 상품 목록 조회
		public List<ProductBean> selectCGProductList(String cg) {
			ArrayList<ProductBean> productCGList = null;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, i.image_main_file "
						+ "FROM shookream.product p join shookream.image i "
						+ "on p.product_idx = i.product_idx "
						+ "WHERE product_brand LIKE ? "
						+ "GROUP BY product_name ORDER BY product_sell_count ASC";
				
//				String sql = "SELECT * FROM product "
//						+ "WHERE product_brand LIKE ? "
//						+ "ORDER BY product_sell_count asc";
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, "%"+cg+"%");
				
				rs = pstmt.executeQuery();
				
				productCGList = new ArrayList<ProductBean>();
				
				while(rs.next()) {
					ProductBean product = new ProductBean();
					product.setProduct_idx(rs.getInt("product_idx"));
					product.setProduct_name(rs.getString("product_name"));
					product.setProduct_brand(rs.getString("product_brand"));
//					product.setProduct_size(rs.getString("product_size"));
					product.setProduct_price(rs.getInt("product_price"));
//					product.setProduct_release_price(rs.getInt("product_release_price"));
//					product.setProduct_buy_price(rs.getInt("product_buy_price"));
//					product.setProduct_amount(rs.getInt("product_amount"));
//					product.setProduct_sell_count(rs.getInt("product_sell_count"));
//					product.setProduct_exp(rs.getString("product_exp"));
//					product.setProduct_detail_exp(rs.getString("product_detail_exp"));
//					product.setProduct_color(rs.getString("product_color"));
//					product.setProduct_discount_price(rs.getDouble("product_discount_price"));
					product.setProduct_img(rs.getString("image_main_file"));
//					product.setProduct_date(rs.getTimestamp("product_date"));
					
					productCGList.add(product);
//					System.out.println(productCGList);
				}
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 - selectCGProductList()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
			return productCGList;
		}

		
		// 메인 - 검색어 상품 목록 조회
		public List<ProductBean> selectKeywordProductList(String keyword) {
			ArrayList<ProductBean> productSearchList = null;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT p.product_idx, p.product_brand, p.product_name, p.product_price, i.image_main_file "
						+ "FROM shookream.product p join shookream.image i "
						+ "on p.product_idx = i.product_idx "
						+ "WHERE product_brand LIKE ? OR product_name LIKE ?"
						+ "GROUP BY product_name ORDER BY product_sell_count ASC";
				
//				String sql = "SELECT * FROM product "
//						+ "WHERE product_brand LIKE ? "
//						+ "ORDER BY product_sell_count asc";
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, "%"+keyword+"%");
				pstmt.setString(2, "%"+keyword+"%");
				
				rs = pstmt.executeQuery();
				
				productSearchList = new ArrayList<ProductBean>();
				
				while(rs.next()) {
					ProductBean product = new ProductBean();
					product.setProduct_idx(rs.getInt("product_idx"));
					product.setProduct_name(rs.getString("product_name"));
					product.setProduct_brand(rs.getString("product_brand"));
//					product.setProduct_size(rs.getString("product_size"));
					product.setProduct_price(rs.getInt("product_price"));
//					product.setProduct_release_price(rs.getInt("product_release_price"));
//					product.setProduct_buy_price(rs.getInt("product_buy_price"));
//					product.setProduct_amount(rs.getInt("product_amount"));
//					product.setProduct_sell_count(rs.getInt("product_sell_count"));
//					product.setProduct_exp(rs.getString("product_exp"));
//					product.setProduct_detail_exp(rs.getString("product_detail_exp"));
//					product.setProduct_color(rs.getString("product_color"));
//					product.setProduct_discount_price(rs.getDouble("product_discount_price"));
					product.setProduct_img(rs.getString("image_main_file"));
//					product.setProduct_date(rs.getTimestamp("product_date"));
					
					productSearchList.add(product);
					System.out.println(productSearchList);
				}
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 - selectKeywordProductList()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
			return productSearchList;
		}


		
	
	
}//DAO 끝
