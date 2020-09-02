<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
								<td colspan="7"><ul >${pagingImg2 }</ul></td>
							</c:when>
							<c:otherwise>
								<td colspan="7"><ul >${pagingImg }</ul></td>
							</c:otherwise>
						</c:choose>
					</tr>
			</table>
