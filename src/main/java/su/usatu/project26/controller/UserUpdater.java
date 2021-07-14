package su.usatu.project26.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import su.usatu.project26.dao.Project26DAO;
import su.usatu.project26.dao.Project26DAOImplementation;
import su.usatu.project26.model.User;
import su.usatu.project26.util.JsonResponseUtil;

@WebServlet("update_user")

public class UserUpdater extends HttpServlet {

	private Project26DAO dao;

	public UserUpdater() {
		dao = new Project26DAOImplementation();
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

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		String apiKey = request.getParameter("token");

		User user = new User();

		String userEmail = request.getParameter("email");
		String userFullName = request.getParameter("fullName");
		int userMeterMode = Integer.parseInt(request.getParameter("meterMode"));
		int userRatesId = Integer.parseInt(request.getParameter("ratesId"));

		user.setEmail(userEmail);
		user.setFullName(userFullName);
		user.setMeterMode(userMeterMode);
		user.setRatesSetId(userRatesId);

		if (dao.getUserByToken(apiKey, "users").getId() == 0) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "User Not Found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {

			if (dao.updateUserInfo(apiKey, user)) {
				jsonOutput = JsonResponseUtil.formJsonResponse("success", "Updated", user);

			} else {
				jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Update failed");
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Status code 500
			}
		}

		out.println(jsonOutput);
	}

}
