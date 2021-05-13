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

@WebServlet("get_prices")

public class RatesGetter extends HttpServlet {

	private Project26DAO dao;

	public RatesGetter() {
		dao = new Project26DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		String queriedTableParameter = request.getParameter("table_id");
		int queriedTableId = Integer.parseInt(queriedTableParameter);

		String[] tablesArray = { "private_user_prices", "electric_stove_private_user_prices",
				"alternative_private_user_prices" };

		Rates ratesOutput = dao.selectRates(tablesArray[queriedTableId]);

		jsonOutput = JsonResponseUtil.formJsonResponse("success", "Found", ratesOutput);

		out.println(jsonOutput);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// POST is not allowed on this page
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;
		
		jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Operation failed: only GET allowed");
		
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		out.println(jsonOutput);
	}

}
