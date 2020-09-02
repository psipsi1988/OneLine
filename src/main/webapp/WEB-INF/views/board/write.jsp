<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.min.js"></script>
<script>
$(function(){

        $("#inserBtn").click(function(){
    
    		if ($('#memId').val()==""){
    			alert("아이디를 입력하세요");
    			$('#memId').focus();
 				return false;	
 			}
    		if ($('#memName').val()==""){
    			alert("이름을 입력하세요");
    			$('#memName').focus();
 				return false;	
 			}
    		if ($('#boardSubject').val()==""){
    			alert("제목을 입력하세요");
    			$('#boardSubject').focus();
 				return false;	
 			}
    		if ($('#boardContent').val()==""){
    			alert("내용을 입력하세요");
    			$('#boardContent').focus();
 				return false;	
 			}
        	
        	$("#frm").attr("action", "writeAction.do").attr("method", "post").submit();
        })
        
        $("#updateBtn").click(function(){
        	
    		if ($('#memId').val()==""){
    			alert("아이디를 입력하세요");
    			$('#memId').focus();
 				return false;	
 			}
    		if ($('#memName').val()==""){
    			alert("이름을 입력하세요");
    			$('#memName').focus();
 				return false;	
 			}
    		if ($('#boardSubject').val()==""){
    			alert("제목을 입력하세요");
    			$('#boardSubject').focus();
 				return false;	
 			}
    		if ($('#boardContent').val()==""){
    			alert("내용을 입력하세요");
    			$('#boardContent').focus();
 				return false;	
 			}
    		
        	$("#frm").attr("action", "editAction.do").attr("method", "post").submit();
        })
        
        
        //업로드 버튼 클릭시 실행
        $("#imageUploadBtn").click(function(){
    		if ($('#userfile1').val()==""){
    			alert("업로드할 이미지를 선택하세요.");
    			$('#userfile1').click();
 				return false;	
 			}
        	$("#fileFrm").attr("action", "uploadAction").attr("method", "post").attr("enctype", "multipart/form-data").submit();
        })
        
        //////////////////////////////////// 
        
        
        var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
    	var maxSize = 5242880; //5MB
    	
    	function checkExtension(fileName, fileSize){
    		if(fileSize >= maxSize){
    			alert("파일 사이즈 초과");
    			return false;
    		}
    		
    		if(regex.test(fileName)){
    			alert("해당 종류의 파일은 업로드할 수 없습니다.");
    			return false;
    		}
    		return true;
    	}
    	
    	
    	
    	
    	
    	
    	$("#userfile1").on("change", function() {
    		$("#image_section").empty();
    		//$('#image_section').attr('src', 11);
        	$("#image_section").attr("hidden", "hidden");
    	    var fileName = $('#userfile1').val();
    	    fileName = fileName
    		
    		var reg = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
    	  	if(fileName.match(reg)) {
//     			alert("해당 파일은 이미지 파일입니다.");
    		} else {
    			alert("해당 파일은 이미지 파일이 아닙니다.");
    			$('#userfile1').val("");
    		    setTimeout(function(){
    		    	$('#userfile1').click();
    		     }, 500);
    		}
		    var file  = this.files[0];
		    var _URL = window.URL || window.webkitURL;
		    var img = new Image();
		    img.src = _URL.createObjectURL(file);
		    
		    img.onload = function() {
		        if(img.width > 300 || img.height > 300) {
		            alert("이미지는 300*300 미만만 업로드 가능합니다.\n"+"현재 이미지 사이즈 : "+img.width +"*"+ img.height);
		            $("#userfile1").val("");
		        } 
		    }
		    readURL(this);

    	  	
    	});
    	
    	$("#userfile1").on("click", function() {
    		$("#image_section").empty();
    		//$('#image_section').attr('src', 11);
        	$("#image_section").attr("hidden", "hidden");
    	});

    	
    	
//     	$("#imageUploadBtn").on("click", function(e){
//     		var formData = new FormData();
//     		var inputFile = $("input[name='uploadFile']");
//     		var files = inputFile[0].files;
//     		console.log(files);
    		
//     		//add file data to formdata
//     		for(var i=0; i<files.length; i++){
    			
//     			if(!checkExtension(files[i].name, files[i].size)){
//     				return false;
//     			}
    			
//     			formData.append("uploadFile", files[i]);
//     		}
    		
//     		$.ajax({
//     			url : '/uploadAjaxAction',
//     			processData : false,
//     			contentType : false,
//     			data : formData,
//     			type : 'POST',
//     			success : function(result){
//     				alert("Uploaded");
//     			}
//     		}); // $.ajax
//     	});
        
    	
    	
    	/* 첨부된 이미지 미리보기 영역에 띄우기 */
    	function readURL(input) {
    		if (input.files && input.files[0]) {
    			
    			var reader = new FileReader();
    			var html = '';
    			
    			reader.onload = function (e) {											 
    				$('#image_section').removeAttr('hidden'); //hidden 속성 제거
    				$('#image_section').attr('style', 'width:200px; padding-top:20px;');
    				$('#image_section').attr('src', e.target.result);
    			}
    			
    			reader.readAsDataURL(input.files[0]);
    		}
    	}
    	
    	$('#consult-request-submit').click(function(){
    		alert("작품 신청이 완료되었습니다. ");
   		});
    	
    		 
    	// 이벤트를 바인딩해서 input에 파일이 올라올때 위의 함수를 this context로 실행합니다.
    	$("#imageUrl").change(function(){
    		readURL(this);
    	});
    	
       
});
</script>
</head>
<body>
<div class="container">
	<!-- 글쓰기 폼 -->
	<form name="frm" id="frm" >
		<input type="hidden" name="seq" id="seq" value="${detail.seq }" />
			아이디 : <input type="text" name="memId" id="memId" value="${detail.memId }"/><br/>
			작성자 : <input type="text" name="memName" id="memName" value="${detail.memName }" /><br/>
			제목 : <input type="text" name="boardSubject" id="boardSubject" value="${detail.boardSubject }"/><br/>
			내용 : <textarea name="boardContent" id="boardContent" value="">${detail.boardContent }</textarea><br/>
			
			<c:if test="${empty detail}">
		 		<button id="inserBtn" >등록</button>&nbsp;
			</c:if>
			<c:if test="${not empty detail}">
		 		<button id="updateBtn" >수정</button>&nbsp;
			</c:if>
				<button type="button" id="cancel" onclick="location.href='${pageContext.request.contextPath}/list'" >취소</button>
	</form>	
	
	<!-- 파일업로드 폼  -->
<!-- 	<form name="fileFrm" method="post" action="uploadAction.do" enctype="multipart/form-data" > -->
	<form name="fileFrm" id="fileFrm"  >
	<table class="table table-bordered" style="width:500px;">
		<colgroup>
			<col width="20%" />
			<col width="*" />			
		</colgroup>
		<tr>
			<td colspan="2" style="text-align:center;">
			<div class="form-row cf">
				<div class="form-left">
					<span>이미지 업로드</span><span class="need_label">*</span>
				</div>
				<div class="form-right">
<!-- 					<input type="text" name="title" value="제목입니다." /> -->
					<input type="file" name="userfile1" id="userfile1" class="form-control"/>
<!-- 					<input type='file' id="imageUrl" name="imageUrl-param" class="form-control"/> -->
					<img id="image_section" src="#" hidden/>
				</div>
			</div>
				<input type="button" id="imageUploadBtn" name="imageUploadBtn" value="업로드 버튼"/><br/>
				<a href="uploadList">파일업로드 리스트 바로가기</a>
<!-- 				<input type="button" id="uploadAjaxBtn" name="uploadAjaxBtn" value="Ajax버튼"/> -->
			</td>			
		</tr>
	</table>
	</form>
	
	<ul>
		<c:forEach items="${fileMap }" var="file">
			<li>
				파일명 : ${file.key }
				&nbsp;&nbsp;
				파일크기 : ${file.value }Kb
				&nbsp;&nbsp;
				<a href="download.do?fileName=${file.key }&oriFileName=임시파일명.jpg">
				다운로드
				</a>
				<!-- 다운로드시 원본파일명으로 변경하려면 기존 파일명을 
				DB에 저장해야 하므로 여기서는 임시로 파일명을 지정한다.  -->
			</li>
		</c:forEach>
	</ul>
</div>
</body>
</html>