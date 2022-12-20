package vo;

import java.sql.Date;

public class ProductBean {
//----------멤버변수-------------
private int product_idx; //상품번호
private String product_name; // 상품 이름
private String product_brand; // 상품 브랜드명
private int product_price; // 상품 가격
private String product_size; // 상품 사이즈
private Date product_release_price; // 상품 쿠폰적용가격
private int product_buy_price; // 상품 누적 가격
private int product_amount; // 상품 재고량
private int product_sell_count; // 상품 판매수
private String product_exp; // 상품 요약설명
private String product_detail_exp; // 상품 상세설명
private String product_color; // 상품 색상 카테고리
private double product_discount_price; // 상품 할인율
private String product_img; // 상품 사진


//------------getter, setter-------------------
public int getProduct_idx() {
	return product_idx;
}
public void setProduct_idx(int product_idx) {
	this.product_idx = product_idx;
}
public String getProduct_name() {
	return product_name;
}
public void setProduct_name(String product_name) {
	this.product_name = product_name;
}
public String getProduct_brand() {
	return product_brand;
}
public void setProduct_brand(String product_brand) {
	this.product_brand = product_brand;
}
public int getProduct_price() {
	return product_price;
}
public void setProduct_price(int product_price) {
	this.product_price = product_price;
}
public String getProduct_size() {
	return product_size;
}
public void setProduct_size(String product_size) {
	this.product_size = product_size;
}
public Date getProduct_release_price() {
	return product_release_price;
}
public void setProduct_release_price(Date product_release_price) {
	this.product_release_price = product_release_price;
}
public int getProduct_buy_price() {
	return product_buy_price;
}
public void setProduct_buy_price(int product_buy_price) {
	this.product_buy_price = product_buy_price;
}
public int getProduct_amount() {
	return product_amount;
}
public void setProduct_amount(int product_amount) {
	this.product_amount = product_amount;
}
public int getProduct_sell_count() {
	return product_sell_count;
}
public void setProduct_sell_count(int product_sell_count) {
	this.product_sell_count = product_sell_count;
}
public String getProduct_exp() {
	return product_exp;
}
public void setProduct_exp(String product_exp) {
	this.product_exp = product_exp;
}
public String getProduct_detail_exp() {
	return product_detail_exp;
}
public void setProduct_detail_exp(String product_detail_exp) {
	this.product_detail_exp = product_detail_exp;
}
public String getProduct_color() {
	return product_color;
}
public void setProduct_color(String product_color) {
	this.product_color = product_color;
}
public double getProduct_discount_price() {
	return product_discount_price;
}
public void setProduct_discount_price(double product_discount_price) {
	this.product_discount_price = product_discount_price;
}
public String getProduct_img() {
	return product_img;
}
public void setProduct_img(String product_img) {
	this.product_img = product_img;
}
//-----------------toString--------------------

@Override
public String toString() {
	return "ProductBean [product_idx=" + product_idx + ", product_name=" + product_name + ", product_brand="
			+ product_brand + ", product_price=" + product_price + ", product_size=" + product_size
			+ ", product_release_price=" + product_release_price + ", product_buy_price=" + product_buy_price
			+ ", product_amount=" + product_amount + ", product_sell_count=" + product_sell_count + ", product_exp="
			+ product_exp + ", product_detail_exp=" + product_detail_exp + ", product_color=" + product_color
			+ ", product_discount_price=" + product_discount_price + ", product_img=" + product_img + "]";
}





}