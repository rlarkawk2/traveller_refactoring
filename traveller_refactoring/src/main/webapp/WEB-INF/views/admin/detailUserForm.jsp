<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정(관리자 전용)</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
</head>
<body class="chrome">
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <div id="__nuxt">
    	<div id="__layout">
    		<div id="wrapper">
    			<div id="screenArea">
    				<div id="wrap">
    					<div id="container">
    						<div class="inr">
    						<!-- 여기서 부터 사이드바 입니다.-->
    							<div id="lnb" class="lnb">
    								<div class="inr">
    									<dl>
											<dt class="type">		
												<a href="${pageContext.request.contextPath}/member/myPage.do">관리자 페이지</a>
											</dt>
											<dd>
												<ul class="list_lnb">
										  			<li><a href="${pageContext.request.contextPath}/admin/adminItemList.do">관리자 상품 게시판</a></li>
										 			<li><a href="${pageContext.request.contextPath}/admin/adminOrderList.do">관리자 예약관리 게시판</a></li>
										 			<li><a href="${pageContext.request.contextPath}/admin/itemInsert.do">관리자 상품등록</a></li>
										 			<li><a href="${pageContext.request.contextPath}/admin/userList.do">회원관리</a></li>
												</ul>
											</dd>
										</dl>
    								</div>
    							</div> 
    							<!-- 여기까지 사이드바 입니다.-->
    							<div id="contents" class="contents">
    								<div class="order-form">
									<h1 class="order-title order-center">회원정보 수정</h1>
									<form action="detailUser.do" method="post" id="detail_form" class="input-form">
										<input type="hidden" name="mem_num" value="${mem.mem_num}">
										<ul class="order-ul">
											<li>
												<label for=id>아이디</label>
												<input type="text" name="id" id="id" value="${mem.id}">
											</li>
											<li>
												<label for="name">이름</label>
												<input type="text" name="name" id="id" value="${mem.name}">
											</li>
											<li>
												<label for="passwd">비밀번호</label>
												<input type="text" name="passwd" id="passwd" value="${mem.passwd}">
											</li>
											<li>
												<label for="phone">전화번호</label>
												<input type="text" name="phone" id="phone" value="${mem.phone}">
											</li>
											<li>
												<label for="birth">생년월일</label>
												<input type="text" name="birth" id="birth" value="${mem.birth}">
											</li>
											<li>
												<label for="gender">성별</label>
												<input type="radio" name="gender" id="gender1" value="M"<c:if test="${mem.gender == 'M'}">checked</c:if>>남자
												<input type="radio" name="gender" id="gender2" value="F"<c:if test="${mem.gender == 'F'}">checked</c:if>>여자
											</li>
											<li>
												<label for="email">이메일</label>
												<input type="email" name="email" id="email" value="${mem.email}">
											</li>
											<li>
												<label for="zipcode">우편번호</label>
												<input type="text" name="zipcode" id="zipcode" value="${mem.zipcode}">
												<input type="button" onclick="execDaumPostcode()" value="우편번호 찾기" id="zipcode_check">
											</li>
											<li>
												<label for="address1">주소</label>
												<input type="text" name="address1" id="address1" value="${mem.address1}">
											</li>
											<li>
												<label for="address2">상세주소</label>
												<input type="text" name="address2" id="address2" value="${mem.address2}">
											</li>
											<li>
												<label for="push">광고 수신 동의</label>
												<input type="radio" name="push" id="push1" value="Y"<c:if test="${mem.push == 'Y'}">checked</c:if>>동의
												<input type="radio" name="push" id="push2" value="N"<c:if test="${mem.push == 'N'}">checked</c:if>>비동의
											</li>
											<li>
												<label for="auth">등급</label>
												<c:if test="${mem.auth != 9}">
												<input type="radio" name="auth" value="0" id="auth1"<c:if test="${mem.auth == 0}">checked</c:if>>탈퇴
												<input type="radio" name="auth" value="1" id="auth2"<c:if test="${mem.auth == 1}">checked</c:if>>VVIP
												<input type="radio" name="auth" value="2" id="auth3"<c:if test="${mem.auth == 2}">checked</c:if>>VIP
												<input type="radio" name="auth" value="3" id="auth4"<c:if test="${mem.auth == 3}">checked</c:if>>일반
												</c:if>
												<c:if test="${mem.auth == 9}">
												<input type="radio" name="auth" value="9" id="auth5" checked>관리
												</c:if>
											</li>
										</ul>
										<div class="align-center">
											<c:if test="${mem.auth != 9}">
											<input type="submit" value="수정">
											</c:if>
											<input type="button" value="목록" onclick="location.href='userList.do'" id="home">
										</div>
									</form>
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
<!-- 우편번호 검색 시작 -->
	<!-- iOS에서는 position:fixed 버그가 있음, 적용하는 사이트에 맞게 position:absolute 등을 이용하여 top,left값 조정 필요 -->
<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;">
<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
</div>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // 우편번호 찾기 화면을 넣을 element
    var element_layer = document.getElementById('layer');

    function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    //(주의)address1에 참고항목이 보여지도록 수정
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    //(수정) document.getElementById("address2").value = extraAddr;
                
                } 
                //(수정) else {
                //(수정)    document.getElementById("address2").value = '';
                //(수정) }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zipcode').value = data.zonecode;
                //(수정) + extraAddr를 추가해서 address1에 참고항목이 보여지도록 수정
                document.getElementById("address1").value = addr + extraAddr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("address2").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_layer.style.display = 'none';
            },
            width : '100%',
            height : '100%',
            maxSuggestItems : 5
        }).embed(element_layer);

        // iframe을 넣은 element를 보이게 한다.
        element_layer.style.display = 'block';

        // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
        initLayerPosition();
    }

    // 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
    // resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
    // 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
    function initLayerPosition(){
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        // 위에서 선언한 값들을 실제 element에 넣는다.
        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';
        // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
    }
</script>
<!-- 우편번호 검색 끝 -->
</html>