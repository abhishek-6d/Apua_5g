package com.sixdee.imp.common.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.bo.ConsumePointsBL;

public class RedeemPointsTestServlet extends HttpServlet {

	Logger logger = Logger.getLogger(RedeemPointsTestServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		execute(request);

	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		execute(arg0);
	}

	public void execute(HttpServletRequest request) {
		ConsumePointsBL consumePointsBL = null;
		try {
			consumePointsBL = new ConsumePointsBL();
			logger.info("RedeemPointsTestServlet Service Details Start at : " + new Date());
			// Globals.reloadServiceDetails();
			String pathInfo = request.getPathInfo(); // /{value}/test
			String[] pathParts = pathInfo.split("/");
			String loyaltyId = pathParts[1]; // {value}
			long points = Long.valueOf(pathParts[2]); // test
			long consumePoints = consumePointsBL.consumeMyPoints(System.currentTimeMillis() + "", loyaltyId, points,
					false);
			logger.info("Consumed points " + consumePoints);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logger.info("Reload Service Details Stop at : " + new Date());

	}// execute

}
