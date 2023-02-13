package com.sixdee.imp.common.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ReloadServlet extends HttpServlet {

	Logger logger=Logger.getLogger(ReloadServlet.class);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		execute();
		
	}
	
	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		execute();
	}
	
	
	public void execute()
	{
		logger.info("Reload Cache Start at : "+new Date());
		
		Globals.reload();
		
		logger.info("Reload Cache Stop at : "+new Date());
		
	}//execute
	
}
