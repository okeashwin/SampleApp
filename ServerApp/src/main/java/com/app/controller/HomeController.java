package com.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.RecordEntryDAO;
import com.app.model.RecordEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@Autowired
	private RecordEntryDAO recordEntryDAO;

	private static final Logger logger = Logger.getLogger(HomeController.class.getName());

	@RequestMapping(value = "/")
	public ModelAndView test(HttpServletResponse response) throws ServletException, IOException {
		logger.log(Level.INFO, "Received request for /");
		return new ModelAndView("home");
	}

	@RequestMapping(value="/add", method = RequestMethod.POST)
	public void addMetrics(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
		Integer id=Integer.parseInt(request.getParameter("id"));
		//Timestamp startTime = Timestamp.valueOf(request.getParameter("startTime"));
		//Timestamp endTime = Timestamp.valueOf(request.getParameter("endTime"));
		Timestamp startTime= new Timestamp(Long.parseLong(request.getParameter("startTime")));
		Timestamp endTime= new Timestamp(Long.parseLong(request.getParameter("endTime")));
		float cpuUsed=Float.valueOf(request.getParameter("cpuUsed"));
		float memUsed=Float.valueOf(request.getParameter("memUsed"));
		float diskUsed=Float.valueOf(request.getParameter("diskUsed"));

		recordEntryDAO.saveOrUpdate(new RecordEntry(id,startTime, endTime, cpuUsed, memUsed, diskUsed));
		response.setContentType("application/json");
	}

	@RequestMapping(value = "/getState", method = RequestMethod.GET)
	public void getState(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
		String stateTime=request.getParameter("timestamp");
		Timestamp state=null;
		if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(stateTime)) {
			state = Timestamp.valueOf(stateTime);
		}
		else {
			Date date= new Date();
			state = new Timestamp(date.getTime());
		}
		response.setContentType("application/json");
		RecordEntry result=recordEntryDAO.get(state);
		if(result!=null) {
			PrintWriter out = response.getWriter();
			out.print(result.toString());
		}

	}
}
