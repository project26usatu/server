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
import su.usatu.project26.model.Rates;
import su.usatu.project26.model.ReportData;
import su.usatu.project26.util.JsonResponseUtil;

@WebServlet("generate_pdf")

public class PDFGenerator extends HttpServlet {

	private Project26DAO dao;
	private Rates rates;

	public PDFGenerator() {
		dao = new Project26DAOImplementation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		boolean ratesIdInputIsValid = false;
		boolean validInputData = false;
		ReportData rd = new ReportData();
		String pdfReport;

		rd.meterMode = Integer.parseInt(request.getParameter("meterMode"));
		rd.ratesId = Integer.parseInt(request.getParameter("ratesId"));

		if ((rd.ratesId >= 1 && rd.ratesId <= 3)) {
			ratesIdInputIsValid = true;
			rates = dao.getRatesById(rd.ratesId);
		}

		if (rd.meterMode == 1 && ratesIdInputIsValid) {

			rd.firstMeterPrevReadings = request.getParameter("firstMeterPrevReadings");
			rd.firstMeterCurrReadings = request.getParameter("firstMeterCurrReadings");

			rd.consumptionByFirstMeter = request.getParameter("consumptionByFirstMeter");

			rd.firstMeterAmount = request.getParameter("firstMeterAmount");

			rd.totalAmount = request.getParameter("totalAmount");

			rd.firstRatePrice = String.valueOf(rates.single_rate_price);

			validInputData = true;

		} else if (rd.meterMode == 2 && ratesIdInputIsValid) {

			rd.firstMeterPrevReadings = request.getParameter("firstMeterPrevReadings");
			rd.firstMeterCurrReadings = request.getParameter("firstMeterCurrReadings");
			rd.secondMeterPrevReadings = request.getParameter("secondMeterPrevReadings");
			rd.secondMeterCurrReadings = request.getParameter("secondMeterCurrReadings");

			rd.consumptionByFirstMeter = request.getParameter("consumptionByFirstMeter");
			rd.consumptionBySecondMeter = request.getParameter("consumptionBySecondMeter");

			rd.firstMeterAmount = request.getParameter("firstMeterAmount");
			rd.secondMeterAmount = request.getParameter("secondMeterAmount");

			rd.totalAmount = request.getParameter("totalAmount");

			rd.firstRatePrice = String.valueOf(rates.daily_rate_price);
			rd.secondRatePrice = String.valueOf(rates.night_rate_price);

			validInputData = true;

		} else if (rd.meterMode == 3 && ratesIdInputIsValid) {

			rd.firstMeterPrevReadings = request.getParameter("firstMeterPrevReadings");
			rd.firstMeterCurrReadings = request.getParameter("firstMeterCurrReadings");
			rd.secondMeterPrevReadings = request.getParameter("secondMeterPrevReadings");
			rd.secondMeterCurrReadings = request.getParameter("secondMeterCurrReadings");
			rd.thirdMeterPrevReadings = request.getParameter("thirdMeterPrevReadings");
			rd.thirdMeterCurrReadings = request.getParameter("thirdMeterCurrReadings");

			rd.consumptionByFirstMeter = request.getParameter("consumptionByFirstMeter");
			rd.consumptionBySecondMeter = request.getParameter("consumptionBySecondMeter");
			rd.consumptionByThirdMeter = request.getParameter("consumptionByThirdMeter");

			rd.firstMeterAmount = request.getParameter("firstMeterAmount");
			rd.secondMeterAmount = request.getParameter("secondMeterAmount");
			rd.thirdMeterAmount = request.getParameter("thirdMeterAmount");

			rd.totalAmount = request.getParameter("totalAmount");

			rd.firstRatePrice = String.valueOf(rates.peak_zone_rate_price);
			rd.secondRatePrice = String.valueOf(rates.semipeak_zone_rate_price);
			rd.thirdRatePrice = String.valueOf(rates.night_zone_rate_price);

			validInputData = true;

		}

		if (!validInputData) {

			jsonOutput = JsonResponseUtil.formJsonResponse("failure",
					"meterMode and ratesId params must be at range from 1 to 3");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Status code 400

		} else {

			pdfReport = dao.createPdfReport(rd);

			if (pdfReport == "Failed") {
				jsonOutput = JsonResponseUtil.formJsonResponse("failure", "Failed to create PDF report");
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Status code 500
			} else {
				jsonOutput = JsonResponseUtil.formJsonResponse("success", "OK", pdfReport);
			}
		}

		out.println(jsonOutput);

	}

}
