package su.usatu.project26.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLJDBCUtil {

	public static Connection getConnection() throws SQLException {
		Connection conn = null;

		String url = "jdbc:mysql://mysql:3306/project26";
		String user = "db_admin";
		String password = "db_password";

		conn = DriverManager.getConnection(url, user, password);

		return conn;
	}

}