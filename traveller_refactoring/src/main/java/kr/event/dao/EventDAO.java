package kr.event.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.event.vo.EventReplyVO;
import kr.event.vo.EventVO;
import kr.money.vo.MoneyVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class EventDAO {
	
	//싱글턴 패턴
	private static EventDAO instance = new EventDAO();
	
	public static EventDAO getInstance() {
		return instance;
	}
	
	private EventDAO() {}
	
	
	//이벤트 전체/검색 레코드 수
	public int getEventCount(String keyfield, String keyword, String category) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		String sub_sql2 = "";
		int count = 0;
		int cnt = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			if(keyword!=null && !"".equals(keyword)) { //검색 시
				if(keyfield.equals("1")) sub_sql += " WHERE event_title LIKE ? ";
				else if(keyfield.equals("2")) sub_sql += " WHERE event_content LIKE ? ";
				
				if(category!=null && !"".equals(category)) sub_sql2 += "AND event_category=?";
			} else {
				if(category!=null && !"".equals(category)) sub_sql2 += "WHERE event_category=?";
			}
			sql = "SELECT COUNT(*) FROM event " + sub_sql + sub_sql2;
			
			pstmt = conn.prepareStatement(sql);
			
			if(keyword!=null && !"".equals(keyword)) pstmt.setString(++cnt, "%"+keyword+"%");
			if(category!=null && !"".equals(category)) pstmt.setInt(++cnt, Integer.parseInt(category));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) count = rs.getInt(1);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	
	//이벤트 전체/검색 목록
	public List<EventVO> getEventList(int start, int end, String keyfield, String keyword, String category)throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EventVO> list = null;
		String sql = null;
		String sub_sql = "";
		String sub_sql2 = "";
		int cnt = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			if(keyword!=null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE event_title LIKE ?";
				else if(keyfield.equals("2")) sub_sql += "WHERE event_content LIKE ?";
				
				if(category!=null && !"".equals(category)) sub_sql2 += "AND event_category=?";
			} else {
				if(category!=null && !"".equals(category)) sub_sql2 += "WHERE event_category=?";				
			}
			sql = "SELECT * FROM ( SELECT a.*,rownum rnum FROM ( SELECT * FROM event "
					+ sub_sql + sub_sql2 + " ORDER BY event_end DESC)a) WHERE rnum>=? AND rnum<=?";
			
			pstmt = conn.prepareStatement(sql);
			
			if(keyword!=null && !"".equals(keyword)) pstmt.setString(++cnt, "%"+keyword+"%");
			if(category!=null && !"".equals(category)) pstmt.setInt(++cnt,Integer.parseInt(category));
			pstmt.setInt(++cnt,start);
			pstmt.setInt(++cnt,end);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<EventVO>();
			
			while(rs.next()) {
				EventVO event = new EventVO();
				
				event.setEvent_num(rs.getInt("event_num"));
				event.setEvent_category(rs.getInt("event_category"));
				event.setEvent_photo1(rs.getString("event_photo1"));
				event.setEvent_title(StringUtil.useBrNoHtml(rs.getString("event_title")));
				event.setEvent_start(rs.getString("event_start"));
				event.setEvent_end(rs.getString("event_end"));
				
				list.add(event);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	//이벤트 상세정보
	public EventVO getEventDetail(int event_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventVO event = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT * FROM event WHERE event_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, event_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				event = new EventVO();
				
				event.setEvent_num(event_num);
				event.setEvent_category(rs.getInt("event_category"));
				event.setEvent_title(rs.getString("event_title"));
				event.setEvent_start(rs.getString("event_start"));
				event.setEvent_end(rs.getString("event_end"));
				event.setEvent_photo1(rs.getString("event_photo1"));
				event.setEvent_photo2(rs.getString("event_photo2"));
				event.setEvent_photo3(rs.getString("event_photo3"));
				event.setEvent_photo4(rs.getString("event_photo4"));
				event.setEvent_answer(rs.getString("event_answer"));
				event.setEvent_content(rs.getString("event_content"));
				event.setEvent_hit(rs.getInt("event_hit"));
				event.setEvent_regdate(rs.getDate("event_regdate"));
				event.setEvent_modifydate(rs.getDate("event_modifydate"));
				event.setEvent_count(rs.getInt("event_count"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return event;
	}
	
	//이벤트 글 조회수 증가
	public void updateEventHit(int event_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "UPDATE event SET event_hit=event_hit+1 WHERE event_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, event_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.getConnection();
		}
	}
	
	//관) 이벤트 수정 - 종료 처리는 날짜로 설정하게
	public void eventUpdate(EventVO event) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			if(event.getEvent_photo1()!=null) sub_sql += ",event_photo1=?";
			sql = "UPDATE event SET event_start=?,event_end=?,event_content=?,"
					+ "event_modifydate=sysdate,event_count=?,event_answer=? "
					+ sub_sql
					+ "WHERE event_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(++cnt, event.getEvent_start());
			pstmt.setString(++cnt, event.getEvent_end());
			pstmt.setString(++cnt, event.getEvent_content());
			pstmt.setInt(++cnt, event.getEvent_count());
			pstmt.setString(++cnt, event.getEvent_answer());
			if(event.getEvent_photo1()!=null) pstmt.setString(++cnt, event.getEvent_photo1());
			pstmt.setInt(++cnt, event.getEvent_num());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//댓글 등록
	public void insertEventReply(EventReplyVO eventReply) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "INSERT INTO event_reply (event_renum,event_recontent,event_reip,event_num,mem_num) "
					+ "VALUES (event_re_seq.nextval,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, eventReply.getEvent_recontent());
			pstmt.setString(2, eventReply.getEvent_reip());
			pstmt.setInt(3, eventReply.getEvent_num());
			pstmt.setInt(4, eventReply.getMem_num());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//댓글 갯수
	public int getEventReplyCount(int event_num) throws Exception{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT COUNT(*) FROM event_reply WHERE event_num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, event_num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) count=rs.getInt(1);
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	
	//댓글 목록
	public List<EventReplyVO> getEventReplyList(int start, int end, int event_num) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EventReplyVO> list = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "SELECT * FROM (SELECT a.*,rownum rnum FROM (SELECT * FROM event_reply JOIN member USING(mem_num) WHERE event_num=? ORDER BY event_renum DESC)a) WHERE rnum>=? AND rnum<=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, event_num);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<EventReplyVO>();
			
			while(rs.next()) {
				EventReplyVO reply = new EventReplyVO();
				
				reply.setEvent_renum(rs.getInt("event_renum"));
				reply.setEvent_recontent(StringUtil.useBrNoHtml(rs.getString("event_recontent")));
				reply.setEvent_redate(rs.getDate("event_redate"));
				reply.setEvent_modifydate(rs.getDate("event_modifydate"));
				reply.setEvent_reip(rs.getString("event_reip"));
				reply.setEvent_num(rs.getInt("event_num"));
				reply.setMem_num(rs.getInt("mem_num"));
				reply.setId(rs.getString("id"));
				
				list.add(reply);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	//댓글 상세 - 수정, 삭제 시 작성자 체크 용도
	public EventReplyVO getEventReplyDetail(int event_renum) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventReplyVO reply = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM event_reply WHERE event_renum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, event_renum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				reply = new EventReplyVO();
				
				reply.setEvent_renum(rs.getInt("event_renum"));
				reply.setMem_num(rs.getInt("mem_num"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return reply;
	}
	
	//댓글 수정
	public void updateEventReply(EventReplyVO reply) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "UPDATE event_reply SET event_recontent=?,event_modifyDate=sysdate,event_reip=? WHERE event_renum=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, reply.getEvent_recontent());
			pstmt.setString(2, reply.getEvent_reip());
			pstmt.setInt(3, reply.getEvent_renum());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//댓글 삭제
	public void deleteEventReply(int event_renum) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			
			sql = "DELETE FROM event_reply WHERE event_renum=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, event_renum);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//이벤트 참여 - 이벤트 횟수 차감, 적립금 지급
	public void updateEventCount(int event_num, MoneyVO money) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			/*----이벤트 횟수 차감-----*/
			sql = "UPDATE event SET event_count=event_count-1 WHERE event_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, event_num);
			pstmt.executeUpdate();
			
			/*----적립금 지급-----*/
			sql = "INSERT INTO money (sm_num,mem_num,saved_money,sm_content,event_num) VALUES (money_seq.nextval,?,?,?,?)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, money.getMem_num());
			pstmt2.setString(2, money.getSaved_money());
			pstmt2.setString(3, money.getSm_content());
			pstmt2.setInt(4, event_num);
			pstmt2.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
}