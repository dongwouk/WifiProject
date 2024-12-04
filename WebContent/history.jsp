<%@page import="DTO.HistoryDTO"%>
<%@page import="java.util.List"%>
<%@page import="DAO.HistoryDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<h1>와이파이 히스토리 목록</h1>
	<a href="http://localhost:8090/WiFiList/main.jsp">홈</a>
	<a href="#">|</a>
	<a href="http://localhost:8090/WiFiList/history.jsp">위치 히스토리 목록</a>
	<a href="#">|</a>
	<a href="http://localhost:8090/WiFiList/load-wifi.jsp">Open API
		와이파이 정보 가져오기</a>
	<%-- <%@ include file="header.jsp"%> --%>
	<br>
	<div>
		<%
			HistoryDAO service = new HistoryDAO();
		List<HistoryDTO> historyList = service.searchHistoryList();

		String strID = request.getParameter("id");
		if (strID != null) {
			service.deleteHistoryList(strID);
		}
		%>
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>x좌표</th>
					<th>y좌표</th>
					<th>조회일자</th>
					<th>비고</th>
				</tr>
			</thead>
			<tbody>
				<%
					if (historyList.isEmpty()) {
				%>
				<tr>
					<td colspan="5">위치 정보를 조회하신 이력이 없습니다.</td>
				</tr>
				<%
					} else {
				%>
				<%
					for (HistoryDTO historyDTO : historyList) {
				%>
				<tr>
					<td><%=historyDTO.getId()%></td>
					<td><%=historyDTO.getLat()%></td>
					<td><%=historyDTO.getLnt()%></td>
					<td><%=historyDTO.getDate()%></td>
					<td><button onclick="deleteHistory(<%=historyDTO.getId()%>)">삭제</button></td>
				</tr>
				<%
					}
				}
				%>
			</tbody>
		</table>
	</div>
	<script>
    function deleteHistory(ID) {
        if (confirm("데이터를 삭제하시겠습니까?")) {
            $.ajax({
                url: "http://localhost:8090/WiFiList/history.jsp",
                data: {id : ID},
                success: function () {
                    location.reload();
                },
                error: function (request, status, error) {
                    alert("code: " + request.status + "\n"+ "message: " + request.responseText + "\n" + "error: " + error);
                }
            })
        }
    }
</script>
</body>
</html>