package kr.accom.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.accom.dao.AccomDAO;
import kr.accom.vo.AccomInfoVO;
import kr.controller.Action;
import kr.util.PageUtil;

public class RequestAccomListAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		AccomDAO dao = AccomDAO.getInstance();
		List<Integer> accom_nums = dao.getRequestAccom(user_num);
		
		int count = dao.getRequestAccomInfoCount(keyfield, keyword, accom_nums);
		
		//페이지 처리
		PageUtil page = new PageUtil(keyfield, keyword, Integer.parseInt(pageNum),count,10,20,"requestAccomList.do");
		
		List<AccomInfoVO> list = null;
		if(count > 0) {
			list = dao.getRequestAccomInfo(page.getStartRow(), page.getEndRow(), keyfield, keyword, accom_nums);
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
		
		return "/WEB-INF/views/accom/requestAccomList.jsp";
	}	
}
