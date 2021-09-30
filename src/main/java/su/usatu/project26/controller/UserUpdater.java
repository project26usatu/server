package su.usatu.project26.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import su.usatu.project26.dao.Project26DAO;
import su.usatu.project26.dao.Project26DAOImplementation;
import su.usatu.project26.model.User;
import su.usatu.project26.util.JsonResponseUtil;
import su.usatu.project26.util.PasswordUtil;
import su.usatu.project26.util.TokenUtil;

@WebServlet("update_profile")

public class UserUpdater extends HttpServlet {

	private Project26DAO dao;

	public UserUpdater() {
		dao = new Project26DAOImplementation();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		String token = request.getParameter("token");

		User user = new User();
		user = dao.getUserByToken(token, "users");

		if (user.getId() == 1) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "User Not Found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Status code 404
			out.println(jsonOutput);
			return;
		}

		String userEmail = request.getParameter("email");
		user.setEmail(userEmail);

		String userFullName = request.getParameter("fullName");
		user.setFullName(userFullName);

		int userMeterMode = Integer.parseInt(request.getParameter("meterMode"));
		user.setMeterMode(userMeterMode);

		int userRatesId = Integer.parseInt(request.getParameter("ratesId"));
		user.setRatesSetId(userRatesId);

		String password = request.getParameter("password");
		boolean passwordUpdate = password.equals("111111111") ? false : true;

		if (passwordUpdate) {
			try {
				String salt = PasswordUtil.getSalt();
				String hashedPassword = PasswordUtil.hashPassword(password, salt);
				String apiKey = TokenUtil.generateNewToken();
				
				user.setPassword(hashedPassword);
				user.setSalt(salt);
				user.setApiToken(apiKey);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				jsonOutput = JsonResponseUtil.formJsonResponse("failure", e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Status code 500
				out.println(jsonOutput);
				return ;
			}
		}

		if (dao.updateUser(token, user)) {
			User updatedUser = new User();
			String username = user.getUsername();
			updatedUser = dao.getUserByUsername(username, "users");
			String newToken = updatedUser.getApiToken();
			User userInfo = new User();
			userInfo = dao.getUserInfoByToken(newToken, "users");
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Operation successful", userInfo);
			out.println(jsonOutput);
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Update failed");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Status code 500
			out.println(jsonOutput);
			return;
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// GET is not allowed on this page
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Operation failed: only POST allowed");

		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		out.println(jsonOutput);
	}
}
