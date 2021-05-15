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

		int rateSetId;

		if (request.getParameter("rates_set_id").trim().isEmpty()) {

			jsonOutput = JsonResponseUtil.formJsonResponse("failure", "'rates_set_id' parameter is required");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		} else {
			String ratesIdinString = request.getParameter("rates_set_id");
			rateSetId = Integer.parseInt(ratesIdinString);

			if (!(rateSetId >= 1 && rateSetId < 4)) {

				jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Inconsistent 'rates_set_id' parameter");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			} else {

				Rates selectedRates = dao.getRatesById(rateSetId);

				jsonOutput = JsonResponseUtil.formJsonResponse("success", "Found", selectedRates);

			}
		}

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
