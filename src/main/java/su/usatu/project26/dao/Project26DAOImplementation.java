package su.usatu.project26.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import su.usatu.project26.model.*;

import su.usatu.project26.util.MySQLJDBCUtil;
import su.usatu.project26.util.PasswordUtil;

public class Project26DAOImplementation implements Project26DAO {

	String output = null;

	@Override
	public Rates selectRates(String tableName) {
		Rates rates = new Rates();

		String sqlQuery = "SELECT * FROM " + tableName + " WHERE id = 1";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {

				rates.single_rate_price = rs.getDouble("single_rate_price");

				rates.daily_rate_price = rs.getDouble("daily_rate_price");
				rates.night_rate_price = rs.getDouble("night_rate_price");

				rates.peak_zone_rate_price = rs.getDouble("peak_zone_rate_price");
				rates.semipeak_zone_rate_price = rs.getDouble("semipeak_zone_rate_price");
				rates.night_zone_rate_price = rs.getDouble("night_zone_rate_price");

				break;

			}

			// stmt.close(); no need to do, we use try-with-resources

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return rates;

	}

	@Override
	public void editRates(Rates updatingRates, String tableName, String userName) {
		String sqlUpdate = "UPDATE " + tableName + " SET updated_at = UNIX_TIMESTAMP(), updated_by = '" + userName
				+ "', single_rate_price = " + updatingRates.single_rate_price + ", daily_rate_price = "
				+ updatingRates.daily_rate_price + ", night_rate_price = " + updatingRates.night_rate_price
				+ ", peak_zone_rate_price = " + updatingRates.peak_zone_rate_price + ", semipeak_zone_rate_price = "
				+ updatingRates.semipeak_zone_rate_price + ", night_zone_rate_price = "
				+ updatingRates.night_zone_rate_price + " WHERE id = 1 ;";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

			int rowAffected = pstmt.executeUpdate();
			output = String.format("Row affected %d", rowAffected);

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

	}

	@Override
	public void addUser(User user, String tableName) {
		try {
			String sqlInsert = "INSERT INTO users (id, username, password, salt, email, full_name, created_at, group_id, api_token) VALUES (NULL,?,?,?,?,?,UNIX_TIMESTAMP(),?,?)";
			Connection conn = MySQLJDBCUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getSalt());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getFullName());
			pstmt.setInt(6, 2);
			pstmt.setString(7, user.getApiToken());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			output = e.getMessage();
			e.printStackTrace();
		}
	}

	@Override
	public User getUserInfo(String token, String tableName) {
		User user = new User();
		String sqlQuery = "SELECT * FROM " + tableName + " WHERE api_token = '" + token + "'";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setFullName(rs.getString("full_name"));
				user.setGroupId(rs.getInt("group_id"));

				break;

			}

			// stmt.close(); no need to do, we use try-with-resources

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return user;
	}

	@Override
	public boolean checkUsernamePassword(User user, String tableName) {

		boolean result = false;

		String sqlQuery = "SELECT * FROM " + tableName + " WHERE username = '" + user.getUsername() + "'";

		String userPasswordFromDb = null;
		String userSaltFromDb = null;

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			// loop through the result set
			while (rs.next()) {

				userPasswordFromDb = rs.getString("password");

				userSaltFromDb = rs.getString("salt");

				break;

			}

			try {
				result = PasswordUtil.checkPassword(user.getPassword(), userSaltFromDb, userPasswordFromDb);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				output = e.getMessage();
				e.printStackTrace();
			}

			// stmt.close(); no need to do, we use try-with-resources

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return result;

	}

	@Override
	public boolean assignApiToken(User user, String tableName) {

		boolean result = false;

		String sqlUpdate = "UPDATE " + tableName + " SET api_token = '" + user.getApiToken() + "' WHERE username = '"
				+ user.getUsername() + "'";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

			int rowAffected = pstmt.executeUpdate();
			output = String.format("Row affected %d", rowAffected);

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return result;

	}

	@Override
	public int getUserGroupByToken(String token, String tableName) {

		int groupId = 3; // set guest group

		String sqlQuery = "SELECT * FROM " + tableName + " WHERE api_token = '" + token + "'";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {

				// if token not found or not specified groupId remains 3 (guest group)

				groupId = rs.getInt("group_id");

				break;

			}

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return groupId;

	}

}