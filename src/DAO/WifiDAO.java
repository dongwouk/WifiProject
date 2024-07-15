package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DB.testDB;
import DTO.WifiDTO;
import static DAO.HistoryDAO.searchHistory;

public class WifiDAO {
	public static Connection connection;
	public static ResultSet resultSet;
	public static PreparedStatement preparedStatement;

	public static int insertPublicWifi(JsonArray jsonArray) {
		connection = null;
		preparedStatement = null;
		resultSet = null;

		int count = 0;

		try {
			connection = testDB.connectDB();
			connection.setAutoCommit(false); // Auto-Commit 해제

			// wifi정보 insert 쿼리 문
			String sql = " insert into public_wifi "
					+ " ( x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, "
					+ " x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, "
					+ " x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm) "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < jsonArray.size(); i++) {

				JsonObject data = (JsonObject) jsonArray.get(i).getAsJsonObject();

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

				preparedStatement.addBatch(); // 쿼리 실행을 하지 않고 쿼리 구문을 메모리에 올려두었다가, 한번에 DB쪽으로 쿼리를 날린다.
				preparedStatement.clearParameters(); // 파라미터 클리어

				// 1000개 기준으로 임시 batch 실행
				if ((i + 1) % 1000 == 0) {
					int[] result = preparedStatement.executeBatch();
					count += result.length; // 배치한 완료 개수
					connection.commit();
				}
			}

			int[] result = preparedStatement.executeBatch();
			count += result.length; // 배치한 완료 개수
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();

			try {
				connection.rollback();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}

		} finally {
			testDB.close(connection, preparedStatement, resultSet);
		}

		return count;
	}

	// 위도 경도를 이용하여 거리 계산
	public List<WifiDTO> getNearestWifiList(String lat, String lnt) {

		connection = null;
		preparedStatement = null;
		resultSet = null;

		List<WifiDTO> list = new ArrayList<>();

		try {

			connection = testDB.connectDB();

			// 위도 경도를 이요하여 거리를 구하는 식 쿼리문
			String sql = " SELECT *, " + " round(6371*acos(cos(radians(?))*cos(radians(LAT))*cos(radians(LNT) "
					+ " -radians(?))+sin(radians(?))*sin(radians(LAT))), 4) " + " AS distance " + " FROM public_wifi "
					+ " ORDER BY distance " + " LIMIT 20;";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, Double.parseDouble(lat));
			preparedStatement.setDouble(2, Double.parseDouble(lnt));
			preparedStatement.setDouble(3, Double.parseDouble(lat));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				WifiDTO wifiDTO = WifiDTO.builder().distance(resultSet.getDouble("distance"))
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
						.xSwifiRemars3(resultSet.getString("x_swifi_remars3")).lat(resultSet.getString("lat"))
						.lnt(resultSet.getString("lnt"))
						.workDttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime())).build();

				list.add(wifiDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			testDB.close(connection, preparedStatement, resultSet);
		}
		// 해당 값을 조회한 경우 history 데이터에 추가
		searchHistory(lat, lnt);
		System.out.println("히스토리 저장");

		return list;
	}

}
