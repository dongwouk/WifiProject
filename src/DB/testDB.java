package DB;

import java.sql.*;

public class testDB {

		// 인증키
		// 61427665696b776f3336436369684e
		// http://openapi.seoul.go.kr:8088/(인증키)/xml/TbPublicWifiInfo/1/5/
	
	public static Connection connectDB() {

		// 쿼리문 작성 변수
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs =null;
		
        //SQLite 데이터베이스 파일 위치
		String dbFilePath = "C:/servlet_study/eclipse/workspace/WiFiList/src/wifiInfo.db";
//        String dbFilePath = "C:/sqlite/wifiInfo.db";
        String url = "jdbc:sqlite:" + dbFilePath;


        try {
            Class.forName("org.sqlite.JDBC");  //JDBC 드라이버 로드
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        System.out.println("test");
       return connection;
    }
	
	public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

        try {
            if (resultSet != null && ! resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null && ! preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null && ! connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	// 아래 main 코드들은 테이블 생성 및 데이터 확인용으로 작성한 내용
	// 실제 코드에 사용되지 않는다.
	public static void main(String[] args) {
//		
//		Connection connection = null;
//
//        try {
//            connection = connectDB();
//            Statement statement = connection.createStatement();
//
//            
//            statement.executeUpdate("drop table if exists history");
//            statement.executeUpdate("drop table if exists public_wifi");
//            
//            // wifi 정보 테이블 생성 쿼리
//            statement.executeUpdate("create table public_wifi (id INTEGER PRIMARY KEY AUTOINCREMENT, X_SWIFI_MGR_NO text, X_SWIFI_WRDOFC text, X_SWIFI_MAIN_NM text, X_SWIFI_ADRES1 text, X_SWIFI_ADRES2 text, X_SWIFI_INSTL_FLOOR text, X_SWIFI_INSTL_TY text, X_SWIFI_INSTL_MBY text, X_SWIFI_SVC_SE text, X_SWIFI_CMCWR text, X_SWIFI_CNSTC_YEAR text, X_SWIFI_INOUT_DOOR text, X_SWIFI_REMARS3 text, LAT text, LNT text, WORK_DTTM text)");
//            
//            // 히스토리 테이블 생성 쿼리
//            statement.executeUpdate("create table history (id INTEGER PRIMARY KEY AUTOINCREMENT, lat text, lnt text, date text)");
//            // 히스토리 데이터 확인용 쿼리
//            statement.executeUpdate("insert into history (lat, lnt, date) values(3,2,datetime('now','localtime')) ");
//            
//            ResultSet rs = statement.executeQuery("select * from history");
//
//            while(rs.next())
//            {
//                System.out.println("id = " + rs.getInt("id"));
//                System.out.println("date = " + rs.getString("date"));
//            }
//
//            rs.close();
//            connection.close(); 
//        }
//        catch(SQLException e)
//        {
//            System.err.println(e.getMessage());
//        }
    }
	

}
