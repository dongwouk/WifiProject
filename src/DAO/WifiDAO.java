package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DB.connectionDB;
import DTO.WifiDTO;

public class WifiDAO {

    // 공공 와이파이 데이터 삽입 메서드
    public static int WifiInfo(JsonArray jsonArray) {
        String sql = "INSERT INTO public_wifi " +
                "(x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, " +
                "x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, " +
                "x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int count = 0;

        try (Connection connection = connectionDB.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false); // Auto-Commit 해제

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject data = jsonArray.get(i).getAsJsonObject();

                preparedStatement.setString(1, data.get("X_SWIFI_MGR_NO").getAsString());
                preparedStatement.setString(2, data.get("X_SWIFI_WRDOFC").getAsString());
                preparedStatement.setString(3, data.get("X_SWIFI_MAIN_NM").getAsString());
                preparedStatement.setString(4, data.get("X_SWIFI_ADRES1").getAsString());
                preparedStatement.setString(5, data.get("X_SWIFI_ADRES2").getAsString());
                preparedStatement.setString(6, data.get("X_SWIFI_INSTL_FLOOR").getAsString());
                preparedStatement.setString(7, data.get("X_SWIFI_INSTL_TY").getAsString());
                preparedStatement.setString(8, data.get("X_SWIFI_INSTL_MBY").getAsString());
                preparedStatement.setString(9, data.get("X_SWIFI_SVC_SE").getAsString());
                preparedStatement.setString(10, data.get("X_SWIFI_CMCWR").getAsString());
                preparedStatement.setString(11, data.get("X_SWIFI_CNSTC_YEAR").getAsString());
                preparedStatement.setString(12, data.get("X_SWIFI_INOUT_DOOR").getAsString());
                preparedStatement.setString(13, data.get("X_SWIFI_REMARS3").getAsString());
                preparedStatement.setString(14, data.get("LAT").getAsString());
                preparedStatement.setString(15, data.get("LNT").getAsString());
                preparedStatement.setString(16, data.get("WORK_DTTM").getAsString());

                preparedStatement.addBatch();

                // 1000개 단위로 배치 실행
                if ((i + 1) % 1000 == 0) {
                    int[] result = preparedStatement.executeBatch();
                    count += result.length;
                    connection.commit();
                }
            }

            // 나머지 배치 실행
            int[] result = preparedStatement.executeBatch();
            count += result.length;
            connection.commit();

        } catch (SQLException e) {
            System.err.println("와이파이 데이터 삽입 중 오류 발생: " + e.getMessage());
        }

        return count;
    }

    // 위도와 경도를 이용하여 가장 가까운 와이파이 정보 목록 조회
    public List<WifiDTO> getNearestWifiList(String lat, String lnt) {
        List<WifiDTO> list = new ArrayList<>();
        String sql = "SELECT *, round(6371 * acos(cos(radians(?)) * cos(radians(LAT)) * cos(radians(LNT) - radians(?)) + " +
                     "sin(radians(?)) * sin(radians(LAT))), 4) AS distance " +
                     "FROM public_wifi ORDER BY distance LIMIT 20";

        try (Connection connection = connectionDB.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, Double.parseDouble(lat));
            preparedStatement.setDouble(2, Double.parseDouble(lnt));
            preparedStatement.setDouble(3, Double.parseDouble(lat));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    WifiDTO wifiDTO = WifiDTO.builder()
                            .distance(resultSet.getDouble("distance"))
                            .xSwifiMgrNo(resultSet.getString("x_swifi_mgr_no"))
                            .xSwifiWrdofc(resultSet.getString("x_swifi_wrdofc"))
                            .xSwifiMainNm(resultSet.getString("x_swifi_main_nm"))
                            .xSwifiAdres1(resultSet.getString("x_swifi_adres1"))
                            .xSwifiAdres2(resultSet.getString("x_swifi_adres2"))
                            .xSwifiInstlFloor(resultSet.getString("x_swifi_instl_floor"))
                            .xSwifiInstlTy(resultSet.getString("x_swifi_instl_ty"))
                            .xSwifiInstlMby(resultSet.getString("x_swifi_instl_mby"))
                            .xSwifiSvcSe(resultSet.getString("x_swifi_svc_se"))
                            .xSwifiCmcwr(resultSet.getString("x_swifi_cmcwr"))
                            .xSwifiCnstcYear(resultSet.getString("x_swifi_cnstc_year"))
                            .xSwifiInoutDoor(resultSet.getString("x_swifi_inout_door"))
                            .xSwifiRemars3(resultSet.getString("x_swifi_remars3"))
                            .lat(resultSet.getString("lat"))
                            .lnt(resultSet.getString("lnt"))
                            .workDttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime()))
                            .build();

                    list.add(wifiDTO);
                }
            }

        } catch (SQLException e) {
            System.err.println("가까운 와이파이 목록 조회 중 오류 발생: " + e.getMessage());
        }

        // 히스토리 저장
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.insertHistory(lat, lnt);
        System.out.println("히스토리 저장 완료");

        return list;
    }
}
