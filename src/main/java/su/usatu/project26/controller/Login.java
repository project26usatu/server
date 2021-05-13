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

		User user = new User();

		user.setUsername(request.getParameter("username"));
		String password = request.getParameter("password");
		user.setPassword(password);

		boolean passwordIsCorrect = dao.checkUsernamePassword(user, "users");

		String jsonOutput;

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		if (!passwordIsCorrect) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Login failed: wrong username or password");
		} else {

			String apiKey = TokenUtil.generateNewToken();

			user.setApiToken(apiKey);
			dao.assignApiToken(user, "users");

			Cookie cookie = new Cookie("token", apiKey);
			cookie.setPath("/");
			cookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(cookie);

			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Success login", apiKey);
		}

		out.println(jsonOutput);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
