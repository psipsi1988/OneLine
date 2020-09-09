<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "com.tobesoft.platform.*" %>
<%@ page import = "com.tobesoft.platform.data.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "java.io.*" %>
    
<%-- <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.min.js"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" />  
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>  
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>  
<script>


$(function(){
	
	
    $("#deleteBtn1").click(function(){
    	//alert("삭제 버튼 클릭");
    	var cnt = $("input[name='checkbox']:checked").length;
    	var arr = new Array();
        $("input[name='checkbox']:checked").each(function() {
            arr.push($(this).attr('id'));
        });
//         alert("선택된 글 개수:"+cnt);
//         alert("선택된 seq:"+arr);
        if(cnt == 0){
            alert("삭제할 글을 선택해주세요.");
        }
        else{
            $.ajax ({
                type: "POST", 
                url: "delete.do", 
                contentType: "application/x-www-form-urlencoded;charset=UTF-8", 
                data: 	"arr=" + arr + "&cnt=" + cnt, 
                dataType:"json",
                success: function(d){
                    if(d != 1) {
                        alert("삭제 오류");
                    }
                    else{
                        alert("삭제 성공");
                        location.reload(); //새로고침
                    }
                },
                error: function(){
                	alert("서버통신 오류");
                	
               	}
            });
        }
    })
    
    $("#deleteBtn2").click(function(){    
    	var cnt = $("input[name='checkbox']:checked").length;
        if(cnt == 0){
            alert("삭제할 글을 선택해주세요.");
        }
        else{
    	$("#listFrm").attr("action", "delete2.do").attr("method", "post").submit();
    	}
    })
    
    //검색어 검색 버튼을 눌렀을 때 실행되는 함수
    $("#search").click(function(){    
    	var text =  $("#searchField").val();
    	console.log(text);
        if(text =='choice'){
            alert("검색할 내용을 선택하세요.");
            return false;
        }
        if($('#searchbar').val()=="" && $('#startDate').val()=="" && $('#endDate').val()==""){
            alert("검색할 조건을 하나 이상 입력하세요.");
        	$('#searchbar').focus();
            return false;
        }
        $("#searchfrm").attr({"action":"list", "method":"get"}).submit();
    })
    
    var today = new Date();

	var tomorrow = new Date(Date.parse(today) + (1000 * 60 * 60 * 24));

    $( "#startDate" ).datepicker({
        changeMonth: true, 
        dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
        dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'], 
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], 
    	dateFormat: "yy-mm-dd", 
       	

       	onClose: function( selectedDate ) {
            $("#endDate").datepicker( "option", "minDate", selectedDate );
        }  



    	
 	});
    
    $( "#endDate" ).datepicker({
        changeMonth: true, 
        dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
        dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'], 
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], 
        dateFormat: "yy-mm-dd", 
        

       	onClose: function( selectedDate ) {
            $("#startDate").datepicker( "option", "maxDate", selectedDate );
        }  
     
 	});

    
   		$("#searchAjax").click(function(){
// 			$("#frm").attr({"action":"list", "method":"post"}).submit();
			$.ajax({
				url : "list2",
				data : $("#searchfrm").serialize(),
				type : "post",
				success : function(data){
					//alert("조회 성공");
					$("#listFrm").html(data);
				},
				error : function(){
					alert("code:"+request.status+"\n\n\n\n\n\n"+"message:"+request.responseText+"\n\n\n\n\n\n\n\n"+"error:"+error);

				}
			})
		})
		
		//엑셀 다운로드 버튼
	    $("#excelBtn").click(function(){    
    		$("#listFrm").attr("action", "exceldownload").attr("method", "post").submit();
	    })

    
        
});//제이 쿼리

window.onload = function(){
	jQuery("#searchField").val("${searchField }").attr("selected", "selected");
	var sValue= $("#searchFieldValue").val();
	if(sValue==""){
		jQuery("#searchField").val("choice").attr("selected", "selected");
	}
}

</script>
<style>
	ul li{
		float:left;
		list-style:none;
	}
</style>
</head>
<body>
	<div>
	<button type="button" onclick="location.href='${pageContext.request.contextPath}/write'" >글쓰기</button>
	<button id="deleteBtn1" onsubmit="false">삭제1</button>
	<button id="deleteBtn2" onsubmit="false">삭제2</button><br/>
	<button id="excelBtn" onsubmit="false">엑셀 다운로드</button>

	
	</div>
    <!-- 검색창 -->
	<form  id="searchfrm">
	<div>
	<input type="hidden" name="searchFieldValue"  id="searchFieldValue" value="${searchField }"/>
   		<select name="searchField"  id="searchField" value="${searchField }">
     		<option value="choice">선택</option>
     		<option value="MEM_NAME">작성자</option>
			<option value="BOARD_SUBJECT">제목</option>
			<option value="BOARD_CONTENT">제목+내용</option>
	    </select>
    		

	    <input name="searchTxt" id="searchbar"  type="text" placeholder="검색어를 입력하세요" value="${searchTxt }" autocomplete="off">
	
<!-- 		<button id="search" onclick="submit();">검색</button> -->
		<input type="button" id="search" value="검색" />
		<input type="button" id="searchAjax" value="검색ajax" />

<!-- 		<button id="search">검색</button> -->
<!-- 		<button id="searchAjax">검색ajax</button> -->
		<a href="${pageContext.request.contextPath}/list">전체 보기</a>
	<br/>
	작성일 : <input type="text" id="startDate" name="startDate" value="${startDate }" autocomplete="off"> ~ <input type="text" id="endDate" name="endDate" value="${endDate }" autocomplete="off">
	</div>  
	</form>
	
	
	<div>
	<form name="listFrm" id="listFrm" >
		<table border="1" style="margin: 10px" >
			<tr>
				<th>번호</th>
				<th>글번호</th>
				<th>작성자(ID)</th>
				<th>제목</th>
				<th>작성일</th>
				<th>수정일</th>
				<th>조회수</th>
			</tr>
			<c:choose>
				<c:when test="${not empty list}">
				<c:forEach items="${list }" var = "list">
					<tr>
						<td>${list.virtualNum }${virtualNum }</td>
						<td><input type="checkbox" name="checkbox" id="${list.seq }" value="${list.seq }"/><label for="${list.seq }">${list.seq }</label></td>
						<td>
						<a href="${pageContext.request.contextPath}/list?searchFieldValue=&searchField=MEM_NAME&searchTxt=${list.memName }&startDate=&endDate=">${list.memName }</a>
						(${list.memId })</td>
						<td><a href="${pageContext.request.contextPath}/detail?seq=${list.seq }">${list.boardSubject }</a></td>
						<td>${list.regDate }</td>
						<td>${list.uptDate }</td>
						<td>${list.viewCnt }</td> 
					</tr>		
				</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td align="center" colspan="7" > 표시할 내역이 없습니다.<br/> <a href="${pageContext.request.contextPath}/list">전체 보기</a></td>
					</tr>
				</c:otherwise>
			</c:choose>
					<tr>
						<!-- 페이지처리. 검색어 유무에 따라 다르다 -->
						<c:choose>
							<c:when test="${empty searchTxt}">
								<td colspan="6"><ul >${pagingImg2 }</ul></td>
							</c:when>
							<c:otherwise>
								<td colspan="6"><ul >${pagingImg }</ul></td>
							</c:otherwise>
						</c:choose>
						<td>기존 방식</td>
					</tr>
					<tr>
						<td colspan="6">
							<c:if test="${pageMap.curBlock > 1}">
			                    <a href="javascript:goPage('1')">[처음]</a>
			                </c:if>
			                
			                <!-- **이전페이지 블록으로 이동 : 현재 페이지 블럭이 1보다 크면 [이전]하이퍼링크를 화면에 출력 -->
			                <c:if test="${pageMap.curBlock > 1}">
			                    <a href="javascript:goPage('${pageMap.prevPage}')">[이전]</a>
			                </c:if>
			                
			                <!-- **하나의 블럭에서 반복문 수행 시작페이지부터 끝페이지까지 -->
			                <c:forEach var="num" begin="${pageMap.blockBegin}" end="${pageMap.blockEnd}">
			                    <!-- **현재페이지이면 하이퍼링크 제거 -->
			                    <c:choose>
			                        <c:when test="${num == pageMap.curPage}">
			                            <span style="color: red">${num}</span>&nbsp;
			                        </c:when>
			                        <c:otherwise>
			                            <a href="javascript:goPage('${num}')">${num}</a>&nbsp;
			                        </c:otherwise>
			                    </c:choose>
			                </c:forEach>
			                
			                <!-- **다음페이지 블록으로 이동 : 현재 페이지 블럭이 전체 페이지 블럭보다 작거나 같으면 [다음]하이퍼링크를 화면에 출력 -->
			                <c:if test="${pageMap.curBlock <= pageMap.totBlock}">
			                    <a href="javascript:goPage('${pageMap.nextPage}')">[다음]</a>
			                </c:if>
			                
			                <!-- **끝페이지로 이동 : 현재 페이지가 전체 페이지보다 작거나 같으면 [끝]하이퍼링크를 화면에 출력 -->
			                <c:if test="${pageMap.curPage <= pageMap.totPage}">
			                    <a href="javascript:goPage('${pageMap.totPage}')">[끝]</a>
			                </c:if>
						</td>
						<td>ajax 방식</td>
					</tr>
			</table>
		</form> 
	</div>
</body>
</html>