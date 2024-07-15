package Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import DAO.WifiDAO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OpenApi {

	// 서울시 와이파이 API URL
	private static String ApiUrl = "http://openapi.seoul.go.kr:8088/61427665696b776f3336436369684e/json/TbPublicWifiInfo/";
	// OkHttpClient 객체 생성
	private static OkHttpClient okHttpClient = new OkHttpClient();

	public static int WifiCount() throws IOException { // 서울시 와이파이 총 합
		int count = 0;

		// http://openapi.seoul.go.kr:8088/61427665696b776f3336436369684e/json/TbPublicWifiInfo/1/1
		// "list_total_count": 24583 와이파이 개수 가져오기

		URL url = new URL(ApiUrl + "1/1");

		// URL 요청
		// REST API 서버로 전송할 GET 요청 객체 생성
		// builder의 get메서드로 GET 요청을 위한 빌드 작업임을 명시

		Request.Builder builder = new Request.Builder().url(url).get();

		// URL 응답
		// 만들어진 객체를 newCall() 메서드를 이용하여 REST API 서버로 전송
		// 요청에 대한 Response 객체를 반환받기 위해 .execute() 메서드로 리턴을 요청
		Response response = okHttpClient.newCall(builder.build()).execute();

		// 동기식 GET 요청 방식
		// URL을 기반으로 Request 객체를 만들고 호출
		try {

			// Response 객체의 요청이 성공적으로 처리됨을 확인하는 메서드 isSuccessfull()
			// 요청한 내용이 정삭적으로 들어왔으면 response.isSuccessfull() = true
			if (response.isSuccessful()) {

				// body() 메서드를 통해 GET 요청에 대한 정보를 확인
				ResponseBody responseBody = response.body();

				if (responseBody != null) {
					JsonElement jsonElement = JsonParser.parseString(responseBody.string());

//					{
//					    "TbPublicWifiInfo": {
//					        "list_total_count": 24583,
//					        "RESULT": {
//					            "CODE": "INFO-000",
//					            "MESSAGE": "정상 처리되었습니다"
//					        },
//					        "row": [
//					            {
//					                "X_SWIFI_MGR_NO": "ARI00001",
//					                "X_SWIFI_WRDOFC": "서대문구",
//					                "X_SWIFI_MAIN_NM": "상수도사업본부",
//					                "X_SWIFI_ADRES1": "서소문로 51",
//					                "X_SWIFI_ADRES2": "본관 1F",
//					                "X_SWIFI_INSTL_FLOOR": "",
//					                "X_SWIFI_INSTL_TY": "7-1-3. 공공 - 시산하기관",
//					                "X_SWIFI_INSTL_MBY": "서울시(AP)",
//					                "X_SWIFI_SVC_SE": "공공WiFi",
//					                "X_SWIFI_CMCWR": "자가망_수도사업소망",
//					                "X_SWIFI_CNSTC_YEAR": "2019",
//					                "X_SWIFI_INOUT_DOOR": "실내",
//					                "X_SWIFI_REMARS3": "",
//					                "LAT": "37.561924",
//					                "LNT": "126.96675",
//					                "WORK_DTTM": "2024-07-10 11:12:59.0"
//					            }
//					        ]
//					    }
//					}

					count = jsonElement.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject()
							.get("list_total_count").getAsInt();

					System.out.println("찾은 와이파이 개수 = " + count);
				}

			} else {
				System.out.println("API 호출 실패: " + response.code());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public static int WifiInfo() throws IOException {
		int totalCnt = WifiCount();
		int start = 1;
		int end = 1;
		int count = 0;

		try {
			for (int i = 0; i <= totalCnt / 1000; i++) {
				start = 1 + (1000 * i);
				end = (i + 1) * 1000;

				URL url = new URL(ApiUrl + start + "/" + end);

				Request.Builder builder = new Request.Builder().url(url).get();
				Response response = okHttpClient.newCall(builder.build()).execute();

				if (response.isSuccessful()) {
					ResponseBody responseBody = response.body();

					if (responseBody != null) {
						JsonElement jsonElement = JsonParser.parseString(responseBody.string());

						JsonArray jsonArray = jsonElement.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject()
								.get("row").getAsJsonArray();

						count += WifiDAO.insertPublicWifi(jsonArray); // 데이터 로드 갯수
					} else {
						System.out.println("API 호출 실패: " + response.code());
					}
				} else {
					System.out.println("API 호출 실패: " + response.code());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

}
