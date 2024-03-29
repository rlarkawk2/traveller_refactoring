package kr.question.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import kr.controller.Action;
import kr.question.dao.QuestionDAO;
import kr.question.vo.QuestionVO;
import kr.util.FileUtil;

public class DeletePhotoAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		int question_num = Integer.parseInt(request.getParameter("question_num"));
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		QuestionDAO dao = QuestionDAO.getInstance();
		QuestionVO db_question = dao.getQuestionDetail(question_num);
		
		Map<String, String> mapData = new HashMap<String, String>();
		
		//회원 글
		if(db_question.getMem_num()!=0) {
			if(user_num==null) mapData.put("result", "logout"); //미로그인 시
			else if(db_question.getMem_num()!=user_num) mapData.put("result", "worngAccess"); //작성자 불일치
		}
		
		dao.deleteQuestionPhoto(question_num);
		FileUtil.removeFile(request, db_question.getQuestion_photo());
		mapData.put("result", "success");

		ObjectMapper mapper = new ObjectMapper();
		String ajaxData = mapper.writeValueAsString(mapData);
		request.setAttribute("ajaxData", ajaxData);
		
		return "/WEB-INF/views/common/ajax_view.jsp";
	}
}