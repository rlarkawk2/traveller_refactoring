package kr.accom.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import kr.accom.dao.AccomDAO;
import kr.accom.vo.AccomReplyVO;
import kr.controller.Action;

public class UpdateReplyAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 전송된 데이터 인코딩 처리
		request.setCharacterEncoding("utf-8");
		//댓글 번호
		int accom_renum = Integer.parseInt(request.getParameter("accom_renum"));
		
		AccomDAO dao = AccomDAO.getInstance();
		AccomReplyVO db_reply = dao.getReplyAccom(accom_renum);
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		Map<String, String> mapAjax = new HashMap<String, String>();
		if(user_num == null) {//로그인이 되지 않은 경우
			mapAjax.put("result", "logout");
		}else if(user_num != null && user_num == db_reply.getMem_num()) {
			//로그인한 회원번호와 작성자 회원번호 일치
			//자바빈(VO) 생성
			AccomReplyVO reply = new AccomReplyVO();
			reply.setAccom_renum(accom_renum);
			reply.setAccom_recontent(request.getParameter("accom_recontent"));
			reply.setAccom_reip(request.getRemoteAddr());
			
			dao.updateReplyAccom(reply);
			
			mapAjax.put("result", "success");
			
		}else {//로그인한 회원번호와 작성자 회원번호 불일치
			mapAjax.put("result", "wrongAccess");
		}
		//JSON 문자열 반환
		ObjectMapper mapper = new ObjectMapper();
		String ajaxData = mapper.writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		return "/WEB-INF/views/common/ajax_view.jsp";
	}
}
