package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class connectionDB {

	// SQLite 데이터베이스 파일 위치
	private static final String DB_FILE_PATH = "C:/servlet_study/eclipse/workspace/WiFiList/src/wifiInfo.db";
	private static final String URL = "jdbc:sqlite:" + DB_FILE_PATH;

	// 데이터베이스 연결 메서드
	public static Connection connectDB() {
		Connection connection = null;

		try {
			Class.forName("org.sqlite.JDBC"); // JDBC 드라이버 로드
			connection = DriverManager.getConnection(URL);
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("데이터베이스 연결 실패: " + e.getMessage());
		}

		return connection;
	}

	// 데이터베이스 자원 해제 메서드
	public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if (preparedStatement != null && !preparedStatement.isClosed()) {
				preparedStatement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("데이터베이스 자원 해제 실패: " + e.getMessage());
		}
	}

	// 데이터베이스 자원 해제 (Statement 사용 시)
	public static void close(Connection connection, java.sql.Statement statement, ResultSet resultSet) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("데이터베이스 자원 해제 실패: " + e.getMessage());
		}
	}

}
