package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
		
		BoardBean board = new BoardBean();

		board.setNotice_category(request.getParameter("notice_category"));
		board.setNotice_subject(request.getParameter("notice_subject"));
		board.setNotice_content(request.getParameter("notice_content"));
		board.setNotice_type(request.getParameter("notice_type"));
		
		
		new BoardModifyProService().modifyBoard(board);
		
		System.out.println("notice_idx = " + board.getNotice_idx()); // 0이  출력
		System.out.println("notice_idx = " + request.getParameter("notice_idx")); // null 이 출력
		
		
		
		forward = new ActionForward();
		forward.setPath("AdminNoticeManage.ad?notice_idx=" + request.getParameter("notice_idx") + "&pageNum=" + request.getParameter("pageNum"));
		forward.setRedirect(true);
		
		return forward;
	}

}
