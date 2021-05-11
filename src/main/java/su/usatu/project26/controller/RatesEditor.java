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
import su.usatu.project26.util.JsonResponseUtil;

@WebServlet("edit_prices")

public class RatesEditor extends HttpServlet {

	private Project26DAO dao;
	public String message;
	double[] updatingRates;
	int i = 0;
	public String[] tablesArray = { "private_user_prices", "electric_stove_private_user_prices",
			"alternative_private_user_prices" };

	public RatesEditor() {
		dao = new Project26DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String jsonOutput;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String token = request.getParameter("token");
		int groupId = dao.getUserGroupByToken(token, "users");

		Rates rates = new Rates();

		rates.single_rate_price = Double.parseDouble(request.getParameter("single_rate_price"));

		rates.daily_rate_price = Double.parseDouble(request.getParameter("daily_rate_price"));
		rates.night_rate_price = Double.parseDouble(request.getParameter("night_rate_price"));

		rates.peak_zone_rate_price = Double.parseDouble(request.getParameter("peak_zone_rate_price"));
		rates.semipeak_zone_rate_price = Double.parseDouble(request.getParameter("semipeak_zone_rate_price"));
		rates.night_zone_rate_price = Double.parseDouble(request.getParameter("night_zone_rate_price"));

		String queriedTableParameter = request.getParameter("table_id");
		int tableId = Integer.parseInt(queriedTableParameter);

		String tableName = tablesArray[tableId];

		String userName = "user";

		if (groupId == 1) {
			dao.editRates(rates, tableName, userName);
			jsonOutput = JsonResponseUtil.formJsonResponse("success", "Изменения внесены", null);
			out.println(jsonOutput);
		} else {
			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Access Denied");
			out.println(jsonOutput);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}

	}

	public void destroy() {
		// do nothing.
	}

}