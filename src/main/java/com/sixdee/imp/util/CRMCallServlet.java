package com.sixdee.imp.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberDetailsDTO;
import com.sixdee.imp.simulator.RedeemSimulator;

public class CRMCallServlet extends HttpServlet{

	Logger logger=Logger.getLogger(CRMCallServlet.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//execute(request, response);
		String number=request.getParameter("number");
		//new MyThread(number).start();
		
		/*RedeemSimulator level= new RedeemSimulator();
		 
		if(number!=null)
		{
			List<BigDecimal> list = new ArrayList<BigDecimal>();
			list.add(new BigDecimal(number));
			level.findingOpeningBalance(list);
		}
		else
			level.start();*/
		
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//execute(request, response);
		/*String number=request.getParameter("number");
		new MyThread(number).start();
		*/
		
	}
	
	
	
	
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{

		PrintWriter printWriter=null;
		
		try{
			printWriter=response.getWriter();
			
			String number=request.getParameter("number");
			
			CRMCalling calling=new CRMCalling();
			
			logger.info("Subscriber Number : "+number);
			printWriter.println("Subscriber Number : "+number);
			printWriter.flush();
			
			LoyaltyProfileTabDTO loyaltyProfileTabDTO=new LoyaltyProfileTabDTO();
			
			calling.getBasicSubscriberInfo(loyaltyProfileTabDTO,number,"simulator");
			
			logger.info("Get Basic Subscriber Info ");
			logger.info("Name : "+loyaltyProfileTabDTO.getFirstName()+" ");
			printWriter.println("Get Basic Subscriber Info ");
			printWriter.println("Name : "+loyaltyProfileTabDTO.getFirstName()+" ");
			 
			logger.info("Get Account Info ");
			logger.info(" Cust ID and Type "+loyaltyProfileTabDTO.getCustIdList());
			
			printWriter.println("Get Account Info ");
			
			Object[] objects= calling.getAllLineNumber(loyaltyProfileTabDTO,"simulator");
			
			calling.getAdditionalAccountInfo(loyaltyProfileTabDTO,"simulator");
			
			System.out.println("Obj  "+objects);
			
			if(objects!=null)
			{
				Map<String,SubscriberDetailsDTO> map=(Map<String, SubscriberDetailsDTO>)objects[0];
				
				for(String lineNumber:map.keySet())
				{
					logger.info("Line Number "+lineNumber);
					logger.info("Account Number "+map.get(lineNumber).getAccountNumber());
					logger.info("Account Type "+map.get(lineNumber).getAccountType());
					logger.info("Subscriber Type "+map.get(lineNumber).getAccountType()+"  "+Cache.accountTypeMap.get(map.get(lineNumber).getAccountType()+"_1"));
					
					printWriter.println("Line Number "+lineNumber);
					printWriter.println("Account Number "+map.get(lineNumber).getAccountNumber());
					printWriter.println("Subscriber Type "+map.get(lineNumber).getAccountType()+"  "+Cache.accountTypeMap.get(map.get(lineNumber).getAccountType()+"_1"));
				}
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			printWriter.println(e.getLocalizedMessage());
		}
	}
	
}

class MyThread extends Thread
{
	String number;
	
	public MyThread(String number) {
		this.number=number;
	}
	@Override
	public void run() {
		
	}
}
