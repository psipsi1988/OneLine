<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사진 리스트</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script>
$(function(){

//     //업로드 버튼 클릭시 실행
//     $("#fileDeleteBtn2222222").click(function(){
    	
//     	alert("정말로 삭제하시겠습니까?");
// // 		if ($('#userfile1').val()==""){
// // 			alert("업로드할 이미지를 선택하세요.");
// // 			$('#userfile1').click();
// // 				return false;	
// // 			}
//      	$("#fileFrm").attr("action", "fileDelete").attr("method", "post").submit();
//     })
        
}); //제이쿼리

	function fileDeleteAction(btn){
		var deleteFile = $(btn).attr('id');
		alert("정말로 삭제하시겠습니까?");
	    $.ajax ({
	        type: "POST", 
	        url: "fileDelete", 
	        //contentType: "application/x-www-form-urlencoded;charset=UTF-8", 
	        data: 	"fileDelete=" + deleteFile, 
	        //dataType:"json",
	        success: function(d){
	            if(d != 1) {
	                alert("삭제 오류");
	            }
	            else{
	            	location.reload(); //새로고침
	                alert("삭제 성공");
	            }
	        },
	        error: function(){
	        	alert("서버통신 오류");
	        	location.reload(); //새로고침
	       	}
	    });
		//$("#fileFrm").attr("action", "fileDelete").attr("method", "post").submit();
		
	}

//  	function fileDeleteAction(btn){
//  		alert("클릭");
//  		$("#fileFrm").attr("action", "fileDelete").attr("method", "post").submit();
//  	}
</script>
</head>
<body>
<div class="container">

	<h2>upload폴더의 파일목록 보기</h2>
	
	<form name="fileFrm" id="fileFrm" >
		<table>
		<c:forEach items="${fileMap }" var="file">
			<li>
				파일명 : ${file.key }
				&nbsp;&nbsp;
				파일크기 : ${file.value }Kb
				&nbsp;&nbsp;
				<!-- 다운로드시 원본파일명으로 변경하려면 기존 파일명을 DB에 저장해야 하므로 여기서는 임시로 파일명을 지정한다.  -->
			</li>
			<li><img src="../resources/upload/${file.key }" width="50" /><a href="download.do?fileName=${file.key }&oriFileName=임시파일명.jpg">다운로드</a>    
	<!-- 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="fileDeleteBtn" onclick="fileDeleteAction()" value="삭제하기" />  -->
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="${file.key }" onclick="fileDeleteAction(this)" value="삭제하기" />
			</li>
		</c:forEach>
		</table>
	</form>
	
</div>
</body>
</html>