<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadAction</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script>
$(function(){

        
});
</script>
</head>
<body>
<div class="container">
	<h2>파일 업로드 결과보기</h2>
	
	<a href="write">파일업로드 폼 바로가기</a><br/>
	<a href="uploadList">파일업로드 리스트 바로가기</a>
	
	<c:forEach begin="0" end="${returnObj.files.size()-1 }" var="i">
		<ul>
			<li>제목${i+1 } : ${returnObj.files[i].title }</li>
			<li>원본파일명${i+1 } : ${returnObj.files[i].originalName }</li>
			<li>저장된 파일명${i+1 } : ${returnObj.files[i].saveFileName }</li>
			<li>전체 경로${i+1 } : ${returnObj.files[i].serverFullName }</li>
			<li><img src="../resources/upload/${returnObj.files[i].saveFileName }" width="200" /></li>
		</ul>
	</c:forEach>
</div>
</body>
</html>