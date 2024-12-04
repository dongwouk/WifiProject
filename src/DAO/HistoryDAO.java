package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.connectionDB;
import DTO.HistoryDTO;

public class HistoryDAO {

    // 히스토리 추가 메서드
    public void insertHistory(String lat, String lnt) {
        String sql = "INSERT INTO history (lat, lnt, date) VALUES (?, ?, datetime('now','localtime'))";

        try (Connection connection = connectionDB.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, lat);
            preparedStatement.setString(2, lnt);

            preparedStatement.executeUpdate();
            System.out.println("데이터가 삽입 완료되었습니다.");

        } catch (SQLException e) {
            System.err.println("히스토리 삽입 중 오류 발생: " + e.getMessage());
        }
    }

    // 히스토리 목록 조회 메서드
    public List<HistoryDTO> searchHistoryList() {
        List<HistoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM history ORDER BY id DESC";

        try (Connection connection = connectionDB.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                HistoryDTO historyDTO = new HistoryDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("lat"),
                        resultSet.getString("lnt"),
                        resultSet.getString("date")
                );
                list.add(historyDTO);
            }

        } catch (SQLException e) {
            System.err.println("히스토리 목록 조회 중 오류 발생: " + e.getMessage());
        }

        return list;
    }

    // 히스토리 삭제 메서드
    public void deleteHistoryList(String id) {
        String sql = "DELETE FROM history WHERE id = ?";

        try (Connection connection = connectionDB.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.executeUpdate();
            System.out.println("삭제 완료");

        } catch (SQLException e) {
            System.err.println("히스토리 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
