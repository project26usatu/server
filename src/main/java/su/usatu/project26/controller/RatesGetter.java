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

	public String[] tablesArray = { "private_user_prices", "electric_stove_private_user_prices",
			"alternative_private_user_prices" };

	public RatesGetter() {
		dao = new Project26DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// SqlConnTest sqlTest = new SqlConnTest();

		String queriedTableParameter = request.getParameter("table_id");
		int queriedTableId = Integer.parseInt(queriedTableParameter);

		Rates ratesOutput = dao.selectRates(tablesArray[queriedTableId]);

		String jsonOutput = JsonResponseUtil.formJsonResponse("success", "Found", ratesOutput);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(jsonOutput);
	}

	public void destroy() {
		// do nothing.
	}

}