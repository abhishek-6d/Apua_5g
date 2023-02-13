package com.sixdee.imp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.simulator.AddRewardPoint;

public class ProcessThreshold extends HttpServlet 
{
	private Logger log = Logger.getLogger(ProcessThreshold.class);
	private static final long serialVersionUID = 1543L;

	public void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doProcess(arg0, arg1);
	}
	public void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doProcess(arg0, arg1);
	}
	
	private void doProcess(HttpServletRequest req, HttpServletResponse resp)
	{
		log.info("Threshold data processing start.........");
		AddRewardPoint rewardPoint = new AddRewardPoint();
		rewardPoint.getThresholdData();
		log.info("Threshold data processing done .........");
	}
}
