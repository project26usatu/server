package su.usatu.project26.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import su.usatu.project26.util.JsonResponseUtil;

@WebServlet("/")

//TODO заменить все == на equials (где это нужно)
//TODO убрать все TODO на исключениях (предварительно сделав их нормальную обработку)
//TODO в личном кабинете настройки категории населения и режима работы счётчика


public class Default extends HttpServlet {

	public Default() {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		jsonOutput = JsonResponseUtil.formJsonResponse("failure", "API method not found");
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		
		out.println(jsonOutput);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String jsonOutput;

		jsonOutput = JsonResponseUtil.formJsonResponse("failure", "API method not found");
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		
		out.println(jsonOutput);
	}

}
