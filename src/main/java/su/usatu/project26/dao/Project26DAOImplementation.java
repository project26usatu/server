package su.usatu.project26.dao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import su.usatu.project26.model.*;

import su.usatu.project26.util.MySQLJDBCUtil;
import su.usatu.project26.util.PDFUtil;
import su.usatu.project26.util.PasswordUtil;
import su.usatu.project26.util.StringUtil;

public class Project26DAOImplementation implements Project26DAO {

	String output = null;

	@Override
	public Rates getRatesById(int id) {
		Rates rates = new Rates();

		String sqlQuery = "SELECT * FROM rates WHERE rates_set_id = " + id + ";";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {

				rates.id = rs.getInt("rates_set_id");

				rates.single_rate_price = rs.getDouble("single_rate_price");

				rates.daily_rate_price = rs.getDouble("daily_rate_price");
				rates.night_rate_price = rs.getDouble("night_rate_price");

				rates.peak_zone_rate_price = rs.getDouble("peak_zone_rate_price");
				rates.semipeak_zone_rate_price = rs.getDouble("semipeak_zone_rate_price");
				rates.night_zone_rate_price = rs.getDouble("night_zone_rate_price");

				break;

			}

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return rates;

	}

	@Override
	public void editRates(Rates updatingRates, String editorName) {
		String sqlUpdate = "UPDATE rates SET updated_at = UNIX_TIMESTAMP(), updated_by = '" + editorName
				+ "', single_rate_price = " + updatingRates.single_rate_price + ", daily_rate_price = "
				+ updatingRates.daily_rate_price + ", night_rate_price = " + updatingRates.night_rate_price
				+ ", peak_zone_rate_price = " + updatingRates.peak_zone_rate_price + ", semipeak_zone_rate_price = "
				+ updatingRates.semipeak_zone_rate_price + ", night_zone_rate_price = "
				+ updatingRates.night_zone_rate_price + " WHERE rates_set_id = " + updatingRates.id + " ;";

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
	public boolean addUser(User user, String tableName) {
		try {
			String sqlInsert = "INSERT INTO users (id, username, password, salt, email, full_name, created_at, group_id, api_token, meter_mode, rates_set_id) VALUES (NULL,?,?,?,?,?,UNIX_TIMESTAMP(),?,?,?,?)";
			Connection conn = MySQLJDBCUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getSalt());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getFullName());
			pstmt.setInt(6, 2);
			pstmt.setString(7, user.getApiToken());
			pstmt.setInt(8, 0);
			pstmt.setInt(9, 0);
			pstmt.executeUpdate();
			pstmt.close();

			return true;
		} catch (SQLException e) {
			output = e.getMessage();
			e.printStackTrace();

			return false;
		}
	}

	@Override
	public User getUserInfo(String token, String tableName) {
		User user = new User();
		String sqlQuery = "SELECT * FROM " + tableName + " WHERE api_token = '" + token + "';";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setFullName(rs.getString("full_name"));
				user.setGroupId(rs.getInt("group_id"));
				user.setMeterMode(rs.getInt("meter_mode"));
				user.setRatesSetId(rs.getInt("rates_set_id"));

				break;

			}

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return user;
	}

	@Override
	public boolean updateUserInfo(String token, User user) {
		try {
			String sqlUpdate = "UPDATE users SET email = ?, full_name = ?, meter_mode = ?, rates_set_id = ? WHERE api_token = '"
					+ token + "';";
			Connection conn = MySQLJDBCUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
			pstmt.setString(1, user.getEmail());
			pstmt.setString(2, user.getFullName());
			pstmt.setInt(3, user.getMeterMode());
			pstmt.setInt(4, user.getRatesSetId());
			pstmt.executeUpdate();
			pstmt.close();

			return true;
		} catch (SQLException e) {
			output = e.getMessage();
			e.printStackTrace();

			return false;
		}
	}

	@Override
	public boolean checkDbValueIfUnique(String rowLabel, String value, String tableName) {

		boolean result = true;

		String sqlQuery = "SELECT * FROM " + tableName + " WHERE " + rowLabel + " = '" + value + "'";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {
				result = false;
				break;
			}

		} catch (SQLException ex) {
			result = false;
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return result;
	}

	@Override
	public boolean checkLoginPasswordMatch(User user, String tableName) {

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

				try {
					result = PasswordUtil.checkPassword(user.getPassword(), userSaltFromDb, userPasswordFromDb);
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					output = e.getMessage();
					e.printStackTrace();
				}

				break;

			}

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

	@Override
	public String createPdfReport(ReportData dataForPDF) throws IllegalStateException, IOException {

		final String WWW_DIR = "/srv/nginx/";
		final String CONTENT_DIR = "/user-content/";
		final String FONTS_DIR = WWW_DIR + CONTENT_DIR + "/fonts/";
		final String IMG_DIR = WWW_DIR + CONTENT_DIR + "/img/";

		byte[] array = new byte[16]; // length is bounded by 16
		new Random().nextBytes(array);
		String newPdfName = StringUtil.generateRandomString();

		String userAccessPath = CONTENT_DIR + newPdfName + ".pdf";
		String savingPath = WWW_DIR + userAccessPath;
		if (PDFUtil.generateNewPDF(dataForPDF, FONTS_DIR, IMG_DIR, savingPath)) {
			return userAccessPath;
		} else {
			return "Failed";
		}
	}

	@Override
	public boolean assignPdfReportToUser(String token, String documentName) {
		try {
			String sqlInsert = "INSERT INTO reports (id, owner_id, document_name, created_at) VALUES (NULL, ?, ?, UNIX_TIMESTAMP());";
			Connection conn = MySQLJDBCUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
			User user = getUserInfo(token, "users");
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, documentName);
			pstmt.executeUpdate();
			pstmt.close();

			return true;
		} catch (SQLException e) {
			output = e.getMessage();
			e.printStackTrace();

			return false;
		}
	}

	@Override
	public String[] getUserPdfFiles(int userId) {
		String sqlQuery = "SELECT * FROM reports WHERE owner_id = '" + userId + "';";

		String[] myArray = null;

		List<String> list = new ArrayList<String>();

		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);) {

			while (rs.next()) {
				list.add(rs.getString("document_name"));

			}

			myArray = new String[list.size()];
			list.toArray(myArray);

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return myArray;
	}

	@Override
	public boolean deletePdfReport(String token, String documentName) {

		boolean deletionStatus = false;

		User user = new User();
		user = getUserInfo(token, "users");
		int userId = user.getId();

		String sqlUpdate = "DELETE FROM reports WHERE document_name = '" + documentName + "' AND owner_id = " + userId + ";";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

			int rowAffected = pstmt.executeUpdate();
			if (rowAffected == 1) deletionStatus = true;

		} catch (SQLException ex) {
			output = ex.getMessage();
			System.out.println(ex.getMessage());
		}

		return deletionStatus;
	}

}