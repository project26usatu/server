package su.usatu.project26.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import su.usatu.project26.dao.*;
import su.usatu.project26.model.Rates;
import su.usatu.project26.model.User;
import su.usatu.project26.util.JsonResponseUtil;

@WebServlet("edit_prices")

public class RatesEditor extends HttpServlet {

	private Project26DAO dao;

	public RatesEditor() {
		dao = new Project26DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		String token = request.getParameter("token");
		User user = dao.getUserByToken(token, "users");
		int groupId = user.getGroupId();

		Rates rates = new Rates();

		rates.id = Integer.parseInt(request.getParameter("rates_set_id"));
		
		rates.single_rate_price = Double.parseDouble(request.getParameter("single_rate_price"));

		rates.daily_rate_price = Double.parseDouble(request.getParameter("daily_rate_price"));
		rates.night_rate_price = Double.parseDouble(request.getParameter("night_rate_price"));

		rates.peak_zone_rate_price = Double.parseDouble(request.getParameter("peak_zone_rate_price"));
		rates.semipeak_zone_rate_price = Double.parseDouble(request.getParameter("semipeak_zone_rate_price"));
		rates.night_zone_rate_price = Double.parseDouble(request.getParameter("night_zone_rate_price"));

		if (groupId == 1) {
			dao.editRates(token, rates);
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Изменения внесены", null);
			out.println(jsonOutput);
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Access Denied");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			out.println(jsonOutput);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
