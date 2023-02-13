package com.sixdee.imp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sixdee.imp.simulator.RedeemSimulator1;
 
public class RAReport extends HttpServlet {

 
	private static final long serialVersionUID = 125L;

	public RAReport() {
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String number=req.getParameter("number");
		RedeemSimulator1 rs = new RedeemSimulator1(number);
		rs.start();
	}

}
