package DAO;

import java.sql.*;
import java.text.DateFormatSymbols; //
import java.text.SimpleDateFormat; //
import java.util.ArrayList;
import java.util.List;

import DB.testDB;
import DTO.HistoryDTO;

public class HistoryDAO {
	public static Connection connection;
	public static PreparedStatement preparedStatement;
	public static ResultSet resultSet;

	public static void searchHistory(String lat, String lnt) {

		connection = null;
		preparedStatement = null;
		resultSet = null;

		try {
			connection = testDB.connectDB();

			String sql = " insert into history " + " (lat, lnt, date) "
					+ " values ( ?, ?, datetime('now','localtime') )";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, lat);
			preparedStatement.setString(2, lnt);

			preparedStatement.executeUpdate();

			System.out.println("데이터가 삽입 완료되었습니다.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			testDB.close(connection, preparedStatement, resultSet);
		}
	}

	public List<HistoryDTO> searchHistoryList() {
		List<HistoryDTO> list = new ArrayList<>();

		connection = null;
		preparedStatement = null;
		resultSet = null;

		try {
			connection = testDB.connectDB();
			String sql = " select * " + " from history " + " order by id desc ";

			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				HistoryDTO historyDTO = new HistoryDTO(resultSet.getInt("id"), resultSet.getString("lat"),
						resultSet.getString("lnt"), resultSet.getString("date"));
				list.add(historyDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			testDB.close(connection, preparedStatement, resultSet);
		}

		return list;
	}

	public void deleteHistoryList(String id) {

		connection = null;
		preparedStatement = null;
		resultSet = null;

		try {
			connection = testDB.connectDB();
			String sql = "delete from history where id = ? ";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, Integer.parseInt(id));
			preparedStatement.executeUpdate();

			System.out.println("삭제 완료");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			testDB.close(connection, preparedStatement, resultSet);
		}
	}
}
