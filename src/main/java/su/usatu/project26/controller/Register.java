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
import su.usatu.project26.util.*;

@WebServlet("register")

public class Register extends HttpServlet {

	private Project26DAO dao;
	String jsonOutput;

	public Register() {
		dao = new Project26DAOImplementation();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		User user = new User();
		String salt = PasswordUtil.getSalt();

		user.setUsername(request.getParameter("username"));

		user.setEmail(request.getParameter("email"));
		user.setFullName(request.getParameter("full_name"));

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			String password = request.getParameter("password");
			String hashedPassword = PasswordUtil.hashPassword(password, salt);
			String apiKey = TokenUtil.generateNewToken();
			user.setPassword(hashedPassword);
			user.setSalt(salt);
			user.setApiToken(apiKey);
			dao.addUser(user, "users");

			Cookie cookie = new Cookie("token", apiKey);
			cookie.setPath("/");
			cookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(cookie);

			// FIXME "Registration successful" when submit already existent username
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Registration successful", apiKey);
			out.println(jsonOutput);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", e.getMessage());
			e.printStackTrace();
			out.println(jsonOutput);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
