package Api;

import java.io.IOException;
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
    private static final String API_URL = "http://openapi.seoul.go.kr:8088/61427665696b776f3336436369684e/json/TbPublicWifiInfo/";
    private static final int BATCH_SIZE = 1000; // 한 번에 가져올 데이터 크기
    // OkHttpClient 객체 생성
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    // 서울시 와이파이 총합 가져오기
    public static int getWifiCount() throws IOException {
        int count = 0;

        URL url = new URL(API_URL + "1/1");
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    JsonElement jsonElement = JsonParser.parseString(responseBody.string());
                    count = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                                       .getAsJsonObject().get("list_total_count").getAsInt();
                    System.out.println("찾은 와이파이 개수 = " + count);
                }
            } else {
                handleApiError(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // 서울시 와이파이 정보 가져오기
    public static int getWifiInfo() throws IOException {
        int totalCnt = getWifiCount();
        int count = 0;

        for (int i = 0; i <= totalCnt / BATCH_SIZE; i++) {
            int start = 1 + (BATCH_SIZE * i);
            int end = Math.min((i + 1) * BATCH_SIZE, totalCnt);

            URL url = new URL(API_URL + start + "/" + end);
            Request request = new Request.Builder().url(url).get().build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        JsonElement jsonElement = JsonParser.parseString(responseBody.string());
                        JsonArray jsonArray = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                                                         .getAsJsonObject().get("row").getAsJsonArray();
                        count += WifiDAO.WifiInfo(jsonArray); // 데이터 로드 갯수
                    }
                } else {
                    handleApiError(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    // API 호출 실패 처리
    private static void handleApiError(Response response) {
        System.out.println("API 호출 실패: " + response.code());
    }
}
