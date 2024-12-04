<%@page import="DTO.WifiDTO"%>
<%@page import="java.util.List"%>
<%@page import="DAO.WifiDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>와이파이 정보 구하기</h1>
    <a href="http://localhost:8090/WiFiList/main.jsp">홈</a>
    <a href="#">|</a>
    <a href="http://localhost:8090/WiFiList/history.jsp">위치 히스토리 목록</a>
    <a href="#">|</a>
    <a href="http://localhost:8090/WiFiList/load-wifi.jsp">Open API 와이파이 정보 가져오기</a>

    <div class="input">
        <span>LAT:</span> 
        <input type="number" id="lat" min="37.413294" max="37.715133" step="0.0001" value="<%=request.getParameter("lat") != null ? request.getParameter("lat") : "" %>">

        <span>LNT:</span> 
        <input type="number" id="lnt" min="126.734086" max="127.269311" step="0.0001" value="<%=request.getParameter("lnt") != null ? request.getParameter("lnt") : "" %>">

        <button id="btn_cur_position">
            <span>내 위치 가져오기</span>
        </button>
        <button id="btn_nearest_wifi">
            <span>근처 Wifi 정보 보기</span>
        </button>
    </div>

    <%
        String latParam = request.getParameter("lat");
        String lntParam = request.getParameter("lnt");
        double lat = 0.0;
        double lnt = 0.0;
        boolean isValid = true;
        List<WifiDTO> list = null;

        try {
            if (latParam != null && lntParam != null) {
                lat = Double.parseDouble(latParam);
                lnt = Double.parseDouble(lntParam);

                // 서울의 위도 및 경도 범위 확인
                if (lat < 37.413294 || lat > 37.715133) {
                    isValid = false;
                    out.println("<script>alert('위도 값은 서울 지역 내에 있어야 합니다. (37.413294 ~ 37.715133)');</script>");
                }
                if (lnt < 126.734086 || lnt > 127.269311) {
                    isValid = false;
                    out.println("<script>alert('경도 값은 서울 지역 내에 있어야 합니다. (126.734086 ~ 127.269311)');</script>");
                }
            }
        } catch (NumberFormatException e) {
            isValid = false;
            out.println("<script>alert('유효하지 않은 위도 또는 경도 값입니다.');</script>");
        }

        if (isValid && latParam != null && lntParam != null) {
            WifiDAO wifiDAO = new WifiDAO();
            list = wifiDAO.getNearestWifiList(latParam, lntParam);
        }
    %>

    <div>
        <table>
            <thead>
                <tr>
                    <th>거리(km)</th>
                    <th>관리번호</th>
                    <th>자치구</th>
                    <th>와이파이명</th>
                    <th>도로명 주소</th>
                    <th>상세 주소</th>
                    <th>설치 위치(층)</th>
                    <th>설치 기관</th>
                    <th>설치 유형</th>
                    <th>서비스 구분</th>
                    <th>망 종류</th>
                    <th>설치 년도</th>
                    <th>실내 외 구분</th>
                    <th>WIFI 접속 환경</th>
                    <th>x좌표</th>
                    <th>y좌표</th>
                    <th>작업일자</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (list != null && !list.isEmpty()) {
                        for (WifiDTO wifiDTO : list) {
                %>
                <tr>
                    <td><%=wifiDTO.getDistance()%></td>
                    <td><%=wifiDTO.getXSwifiMgrNo()%></td>
                    <td><%=wifiDTO.getXSwifiWrdofc()%></td>
                    <td><%=wifiDTO.getXSwifiMainNm()%></td>
                    <td><%=wifiDTO.getXSwifiAdres1()%></td>
                    <td><%=wifiDTO.getXSwifiAdres2()%></td>
                    <td><%=wifiDTO.getXSwifiInstlFloor()%></td>
                    <td><%=wifiDTO.getXSwifiInstlMby()%></td>
                    <td><%=wifiDTO.getXSwifiInstlTy()%></td>
                    <td><%=wifiDTO.getXSwifiSvcSe()%></td>
                    <td><%=wifiDTO.getXSwifiCmcwr()%></td>
                    <td><%=wifiDTO.getXSwifiCnstcYear()%></td>
                    <td><%=wifiDTO.getXSwifiInoutDoor()%></td>
                    <td><%=wifiDTO.getXSwifiRemars3()%></td>
                    <td><%=wifiDTO.getLat()%></td>
                    <td><%=wifiDTO.getLnt()%></td>
                    <td><%=wifiDTO.getWorkDttm()%></td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan='17'>위치 정보를 입력하신 후에 조회해 주세요.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <script>
        let getCurPosition = document.getElementById("btn_cur_position");
        let getNearestWifi = document.getElementById("btn_nearest_wifi");

        getCurPosition.addEventListener("click", function () {
            if ('geolocation' in navigator) {
                navigator.geolocation.getCurrentPosition(function (position){
                    let latitude = position.coords.latitude;
                    let longitude = position.coords.longitude;
                    document.getElementById("lat").value = latitude;
                    document.getElementById("lnt").value = longitude;
                })
            } else {
                alert("위치 정보를 확인할 수 없으니 직접 입력해주시기 바랍니다.");
            }
        });

        getNearestWifi.addEventListener("click", function (){
            let latitude = document.getElementById("lat").value;
            let longitude = document.getElementById("lnt").value;

            if (latitude !== "" && longitude !== "") {
                window.location.assign("http://localhost:8090/WiFiList/main.jsp?lat=" + latitude + "&lnt=" + longitude);
            } else {
                alert("위치 정보를 입력하신 후에 조회해주세요.");
            }
        });
    </script>

</body>
</html>
