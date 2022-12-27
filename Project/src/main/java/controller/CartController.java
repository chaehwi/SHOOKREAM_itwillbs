package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.CartDeleteProAction;
import action.CartInsertProAction;
import action.CartListProAction;
import action.LikeInsertProAction;
import vo.ActionForward;

@WebServlet("*.ca") // 장바구니 컨트롤러
public class CartController extends HttpServlet{
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("pomberController()");
		
		String command = request.getServletPath();
		System.out.println("현재 주소 :"+command);
		
		ActionForward forward = null;
		Action action = null;
		
		//-----------------------------------------------
		//장바구니
		if(command.equals("/CartList.ca")) {//Cart 폼화면
			action = new CartListProAction();
			forward = action.execute(request, response);
		}else if(command.equals("/CartInsertPro.ca")) {//Cart Pro
			action = new CartInsertProAction();
			forward = action.execute(request, response);
		}else if(command.equals("/CartDeleteForm.ca")) { //Cart 삭제 창
			forward = new ActionForward();
			forward.setPath("product/CartDeleteForm.jsp");
			forward.setRedirect(false);
		}else if(command.equals("/CartDeletePro.ca")) { //Cart 삭제 pro
			action = new CartDeleteProAction();
			forward = action.execute(request, response);
		}
		
		//-----------------------------------------------
		//찜목록
		
		if(command.equals("/LikeList.ca")) {
		}else if(command.equals("/LikeInsertPro.ca")) { // 찜하기 누름
			action = new LikeInsertProAction();
			forward = action.execute(request, response);
		}else if(command.equals("/LikeDeleteForm.ca")) { //Cart 삭제 창
		
		}else if(command.equals("/LikeDeletePro.ca")) { //Cart 삭제 pro
			
		}	
		if(forward != null) {
			if(forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			}else {
				RequestDispatcher dis = request.getRequestDispatcher(forward.getPath());
				dis.forward(request, response);
			}
		}
}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}

