<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Api.OpenApi"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<html>
<head>
<title>와이파이 정보 구하기</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>

	<%--와이파이 개수 --%>
	<%
		OpenApi apiService = new OpenApi();
	int count = apiService.getWifiCount();
	%>

	<%--와이파이 개수 디스플레이 --%>
	<div>
		<%
			if (count > 0) {
		%>
		<div style="text-align: center;">
			<h1 style="margin: 20px 0"><%=count%>건의 데이터를 성공적으로 저장했습니다.
			</h1>
			<a href="http://localhost:8090/WiFiList/main.jsp">홈으로 돌아가기</a>
		</div>
		<%
			} else {
		%>
		<h1 style="text-align: center">데이터 저장 실패</h1>
		<%
			}
		%>
	</div>

</body>
</html>