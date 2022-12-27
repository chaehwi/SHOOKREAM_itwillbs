package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.ModifyMemberService;
import svc.ProductModifyService;
import vo.ActionForward;
import vo.ProductBean;

public class ProductModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		
		ActionForward forward  =null;
		
		ProductBean product = new ProductBean();
		
		String uploadPath = "/upload"; // 업로드 가상 디렉토리(이클립스가 관리)
		String realPath = request.getServletContext().getRealPath(uploadPath);
		System.out.println("실제 업로드 경로 : " + realPath);
		int filesize = 1024 * 1024 * 10;
		int maxSize = 10 * 1024 * 1024; 
		
		
			try {
				MultipartRequest multi;
				multi = new MultipartRequest(
						request, realPath, maxSize, "UTF-8", new DefaultFileRenamePolicy()
						);


				product.setProduct_name(multi.getParameter("name"));
				product.setProduct_brand(multi.getParameter("brand"));
				product.setProduct_price(Integer.parseInt(multi.getParameter("price")));
				product.setProduct_size(multi.getParameter("size"));
				product.setProduct_amount(Integer.parseInt(multi.getParameter("amount")));
				product.setProduct_color(multi.getParameter("color"));
				product.setProduct_exp(multi.getParameter("exp"));
				product.setProduct_detail_exp(multi.getParameter("detail_exp"));
				product.setProduct_discount_price(Double.parseDouble(multi.getParameter("discount")));
				product.setProduct_img(multi.getFilesystemName("file"));
//				product.setProduct_img(multi.getFilesystemName("file2"));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			ProductModifyService service = new ProductModifyService();
			
			boolean updateProduct = service.updateProduct(product); 

			if(updateProduct ==true) {
			
				
					forward = new ActionForward();
					forward.setPath("./");
					forward.setRedirect(false);
				
				

			} else{
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter out;
					try {
						out = response.getWriter();
						out.println("<script>");
						out.println("alert('상품 수정 실패!');");
						out.println("history.back()");
						out.println("</script>");
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		
		return forward;
	
	

}
}
