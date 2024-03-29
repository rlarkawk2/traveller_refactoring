<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>동행구하기 목록</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/totalMenu.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eunseo.css">
<script type="text/javascript">
window.onload=function(){
	let myForm = document.getElementById('search_form');
	//이벤트 연결
	myForm.onsubmit=function(){
		let keyword = document.getElementById('keyword');
		if(keyword.value.trim()==''){
			alert('검색어를 입력하세요!');
			keyword.value = '';
			keyword.focus();
			return false;
		}
	};
};
</script>
</head>
<body class="chrome">
	<div id="__nuxt">
		<div id="__layout">
			<div id="wrapper">
				<div id="screenArea">  
					<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<!-- 여기서 부터 사이드바 입니다.-->
<div id="container">
	<div class="inr">
		<div id="lnb" class="lnb">
			<div class="inr">
				<dl>
					<dt class="type">
						<a href="#">트래블러 클럽</a>
					</dt>
					<dd>
						<ul class="list_lnb">
							<li><a href="${pageContext.request.contextPath}/accom/list.do">동행구하기</a></li>
							<li><a href="${pageContext.request.contextPath}/comm/list.do">커뮤니티</a></li>
						</ul>
					</dd>
				</dl>
			</div>
		</div> 
<!-- 여기까지 사이드바 입니다. -->
<!-- 여기서부터는 본문 입니다. -->
		<div id="contents" class="contents">
			<div class="ly_wrap main_cuc">
				<div class="inr">
					<div class="text_wrap big fix">
						<strong class="tit"><em>트래블러 동행구하기</em> 입니다.</strong>
					</div> 
					<!-- 여기서부터는 검색 필드입니다. -->
					<div class="search_field">
						<div class="box_search wide_type">
						<form id="search_form" action="list.do" method="get">
						<select name="keyfield" class="keyfield" style="height:54px;">
							<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>제목</option>
							<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>작성자</option>	
							<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>내용</option>
						</select>
				
						<input type="search" class="input_keyword" size="20" placeholder="검색어를 입력해 주세요" name="keyword" id="keyword" value="${param.keyword}" >
						<input type="submit" class="btn"  value="검색" style="height:54px;">
						
						</form>
						</div> 
					</div>
					<!-- 여기까지 검색창입니다. -->
				<div id="contents" class="contents" data-v-bead000e="">
					<div class="text_wrap big"><strong class="tit">최근 게시물</strong>
						<!-- 여기서부터 버튼입니다. -->						
						<!-- 여기까지 버튼입니다. -->
						
						<div class="panels">
							<div id="tabPkg" class="panel selected">
								<div class="option_wrap">
									<div class="list-space">
										<input class="btn" type="button" value="글쓰기" onclick="location.href='writeForm.do'"
			   								 <c:if test="${empty user_num}">disabled="disabled"</c:if>> 
										<input class="btn" type="button" value="목록" onclick="location.href='list.do'"> 
										<input class="btn" type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">                      
									</div>		
								</div> 
								<div class="tbl">
 										<c:if test="${count == 0}">
										<div class="result-display">표시할 게시물이 없습니다.</div>
										</c:if>
										<c:if test="${count > 0}">
 										<table>
 										<thead>
 											<tr>
 												<th>글번호</th>
												<th>글상태</th>
												<th>제목</th>
												<th>작성자</th>
												<th>작성일</th>
												<th>현재인원/모집인원</th>
												<th>조회수</th>
												<th>추천수</th>
   											</tr>
   										</thead>
   										<c:forEach var="accom" items="${list}">
   										<tbody>
   											<tr>
   											<td>${accom.accom_num}</td>
											<td>
											<c:if test="${accom.accom_status == 0}">진행중</c:if>
				   							 <c:if test="${accom.accom_status == 1}">완료</c:if>
											</td>
											<td><a href="detail.do?accom_num=${accom.accom_num}">${accom.accom_title}</a></td>
											<td>${accom.id}</td>
											<td>${accom.accom_regdate}</td>
											<td>${accom.present_cnt}/${accom.accom_quantity}</td>
											<td>${accom.accom_hit}</td>
											<td>${accom.cnt}</td>
											</tr>
										</tbody>	
										</c:forEach>
   										</table> 
   										</c:if>
   										 <br>
									    <div style="text-align:center;">${page}</div>
   								</div> 
     						</div>
  						</div>
  					</div>
  				</div>
  			</div>
  		</div>
   	</div>
</div>
</div>
</div>
</div>
</div>
</div>
</body>
</html>
