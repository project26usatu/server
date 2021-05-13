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

@WebServlet("get_user_info")

public class UserInfo extends HttpServlet {

	private Project26DAO dao;

	public UserInfo() {
		dao = new Project26DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		String apiKey = request.getParameter("token");

		User user = dao.getUserInfo(apiKey, "users");
		
		if (user.getId() == 0) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "User Not Found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "User found", user);
		}


		out.println(jsonOutput);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
