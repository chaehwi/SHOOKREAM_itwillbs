package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.CouponMainListService;
import svc.MemberIdCheckService;
import svc.ProductListService;
import vo.ActionForward;
import vo.CouponBean;
import vo.MemberCouponBean;
import vo.ProductBean;
import vo.WishBean;

public class CouponMainListProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
		
		String coupon_content = request.getParameter("coupon_content");
		System.out.println("coupon_content : " + coupon_content);
		
		CouponMainListService service = new CouponMainListService();
		List<CouponBean> couponList = service.getCouponMainList(coupon_content);
		request.setAttribute("couponList", couponList);
		
		HttpSession session = request.getSession();
		String sId = (String)session.getAttribute("sId");
		// session 에 저장된 id로 member_idx 조회
		MemberIdCheckService service2 = new MemberIdCheckService();
		int member_idx = service2.getMemberIdx(sId);
		System.out.println("member_idx : " + member_idx);
		
		MemberCouponBean member_coupon = service.getMemberCouponInfo(coupon_content, member_idx);
		
		if(member_coupon != null) {
			request.setAttribute("member_coupon", member_coupon);
		} else {
			request.setAttribute("member_coupon", null);
		}
		
		forward = new ActionForward();
		forward.setPath("main_coupon.jsp?coupon_content="+coupon_content);
		forward.setRedirect(false);
		
		return forward;
	}

}
