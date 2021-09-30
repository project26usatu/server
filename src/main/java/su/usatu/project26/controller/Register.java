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

	public Register() {
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
		
		User user = new User();
		String salt = PasswordUtil.getSalt();

		user.setUsername(request.getParameter("username"));
		user.setEmail(request.getParameter("email"));
		user.setFullName(request.getParameter("full_name"));



		boolean usernameIsUnique = dao.checkDbValueIfUnique("username", user.getUsername(), "users");
		boolean emailIsUnique = dao.checkDbValueIfUnique("email", user.getEmail(), "users");

		if (!usernameIsUnique) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "The username is already taken");
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		} else if (!emailIsUnique) {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "The email address is already taken");
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		} else {

			try {
				String password = request.getParameter("password");
				String hashedPassword = PasswordUtil.hashPassword(password, salt);
				String apiKey = TokenUtil.generateNewToken();
				user.setPassword(hashedPassword);
				user.setSalt(salt);
				user.setApiToken(apiKey);

				boolean userAdded = dao.addUser(user, "users");

				if (!userAdded) {
					jsonOutput = JsonResponseUtil.formJsonResponse("failure", "SQLException");
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					User userInfo = new User();
					userInfo = dao.getUserInfoByToken(apiKey, "users");
					jsonOutput = JsonResponseUtil.formJsonResponse("success", "Registration successful", userInfo);
				}

			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				jsonOutput = JsonResponseUtil.formJsonResponse("failure", e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
			}
		}

		out.println(jsonOutput);

	}

}
