<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
			
			
			