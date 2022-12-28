package action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.ProductModifyProService;
import vo.ActionForward;
import vo.BoardBean;
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
		
		MultipartRequest multi;
		multi = new MultipartRequest(
				request, realPath, maxSize, "UTF-8", new DefaultFileRenamePolicy()
				);
		
			try {


				product.setProduct_name(multi.getParameter("name"));
				product.setProduct_brand(multi.getParameter("brand"));
				product.setProduct_price(Integer.parseInt(multi.getParameter("price")));
				product.setProduct_discount_price(Double.parseDouble(multi.getParameter("discount")));
				product.setProduct_size(multi.getParameter("size"));
				product.setProduct_amount(Integer.parseInt(multi.getParameter("amount")));
				product.setProduct_color(multi.getParameter("color"));
				product.setProduct_exp(multi.getParameter("exp"));
				product.setProduct_detail_exp(multi.getParameter("detail_exp"));
				
				//파일을 수정을 위해 선택할 경우 기존의 파일을 삭제하고, 새로운 파일을 업로드
			
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			ProductModifyProService service = new ProductModifyProService();
			
			boolean isUpdateSuccess = service.modifyProduct(product); 

			if(!isUpdateSuccess) {
				try {
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('상품 수정 실패!')");
					out.println("history.back()");
					out.println("</script>");	
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			 
				

			} else{ // 업데이트 성공 시 
				if(isNewFile) {
					File f = new File(realPath, multi.getParameter("))
				}
				forward = new ActionForward();
				forward.setPath("ProductList.po");
				forward.setRedirect(false);
			}
		
		return forward;
	
	

}

	
}
