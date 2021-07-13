package su.usatu.project26.controller;

import java.io.IOException;
import java.io.PrintWriter;

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
import su.usatu.project26.util.TokenUtil;

@WebServlet("login")

public class Login extends HttpServlet {

	private Project26DAO dao;

	public Login() {
		dao = new Project26DAOImplementation();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		User user = new User();

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username == "" || password == "") {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Login failed: missing required parameters");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {

			user.setPassword(password);
			user.setUsername(username);

			boolean passwordIsCorrect = dao.checkLoginPasswordMatch(user, "users");

			if (!passwordIsCorrect) {
				jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Login failed: wrong username or password");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else {

				String apiKey = TokenUtil.generateNewToken();

				user.setApiToken(apiKey);
				dao.assignApiToken(user, "users");

				Cookie cookie = new Cookie("token", apiKey);
				cookie.setPath("/");
				cookie.setSecure(true);
				cookie.setMaxAge(10 * 365 * 24 * 60 * 60);
				response.addCookie(cookie);

				jsonOutput = JsonResponseUtil.formJsonResponse("success", "Login successful", apiKey);
			}
		}

		out.println(jsonOutput);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//GET is not allowed on this page
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Operation failed: only POST allowed");
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		out.println(jsonOutput);
	}

}
