package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.util.CRMCalling;
import com.sixdee.imp.util.parser.ReRequestParser;
import com.sixdee.imp.util.parser.ReResponseParser;

public class GetAccountNumber extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(GetAccountNumber.class);
	@Override
	public void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doPerform(arg0, arg1);
	}
	
	@Override
	public void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doPerform(arg0, arg1);
	}
	
	private void doPerform(HttpServletRequest req, HttpServletResponse resp)
	{
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		BufferedInputStream bis = null;
		StringBuilder sb = null;
		String xml = null;
		String respXml = null;
		RERequestHeader requestHeader = null;
		CRMCalling calling = new CRMCalling();
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null; 
		ReResponseParameter  parameter = null;
		try
		{

			bis = new BufferedInputStream(req.getInputStream(),32*1024);
			int i;
			sb = new StringBuilder();
			while ((i = bis.read()) != -1) {
				sb.append((char) i);
			}

			xml = sb.toString();
			logger.info("Request => " + xml);
			requestHeader = validate(xml);
			
			if(requestHeader.getDataSet()==null)
				requestHeader.setDataSet(new RERequestDataSet());
			if(requestHeader.getDataSet().getResponseParam()==null)
				requestHeader.getDataSet().setResponseParam(new ArrayList<ReResponseParameter>());
			
			loyaltyProfileTabDTO = new LoyaltyProfileTabDTO();
			int returnCode = calling.getBasicSubscriberInfo1(loyaltyProfileTabDTO, requestHeader.getMsisdn(), requestHeader.getRequestId());
			
			logger.info("The return code =>>>>>>>>>>>> "+returnCode);
			switch (returnCode) {
			case 0:
				respXml = createResponse("SC0001", "Failure", requestHeader);
				break;
			case 1:
				respXml = createResponse("SC0002", "Failure !! Act Category is Different", requestHeader);
				break;
			case 2:
				parameter = new ReResponseParameter();
				parameter.setId("CUST_ID");
				parameter.setValue(loyaltyProfileTabDTO.getCustID());
				requestHeader.getDataSet().getResponseParam().add(parameter);
				
				parameter = new ReResponseParameter();
				parameter.setId("ACCOUNT_NUMBER");
				parameter.setValue(loyaltyProfileTabDTO.getAccountNumber());
				requestHeader.getDataSet().getResponseParam().add(parameter);
				
				if(loyaltyProfileTabDTO.getDateOfBirth()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("DOB");
					parameter.setValue(df.format(loyaltyProfileTabDTO.getDateOfBirth()));
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				if(loyaltyProfileTabDTO.getEmailID()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("EMAIL");
					parameter.setValue(loyaltyProfileTabDTO.getEmailID());
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				if(loyaltyProfileTabDTO.getFirstName()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("E_NAME");
					parameter.setValue(loyaltyProfileTabDTO.getFirstName()+" "+loyaltyProfileTabDTO.getLastName());
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				if(loyaltyProfileTabDTO.getArbicFirstName()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("A_NAME");
					parameter.setValue(loyaltyProfileTabDTO.getArbicFirstName()+" "+loyaltyProfileTabDTO.getArbicLastName());
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				if(loyaltyProfileTabDTO.getGender()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("GENDER");
					parameter.setValue(loyaltyProfileTabDTO.getGender());
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				if(loyaltyProfileTabDTO.getContactNumber()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("CONTACT_NUMBER");
					parameter.setValue(loyaltyProfileTabDTO.getContactNumber());
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				if(loyaltyProfileTabDTO.getNationality()!=null)
				{
					parameter = new ReResponseParameter();
					parameter.setId("NATIONALITY");
					parameter.setValue(loyaltyProfileTabDTO.getNationality());
					requestHeader.getDataSet().getResponseParam().add(parameter);
				}
				respXml = createResponse("SC0000", "SUCCESS", requestHeader);
				break;				

			default:
				respXml = createResponse("SC0001", "Failure", requestHeader);
				break;
			}
			
		}
		catch (Exception e) 
		{
			logger.error("Exception = ",e);
			respXml = createResponse("SC0001", "Failure", requestHeader);
		}
		finally
		{
			try
			{
				if(bis!=null)
					bis.close();
				bis = null;
			}
			catch (Exception e) {
			}
			sb = null;
			xml = null;
			//respXml = null;
			//requestHeader = null;
			calling = null;
			loyaltyProfileTabDTO = null; 
			parameter = null;
		}
		
		sendResp(resp, respXml, requestHeader.getRequestId());
	}
	
	private RERequestHeader validate(String xml)
	{
		RERequestHeader requestHeader = null;
		try
		{
			requestHeader = (RERequestHeader) ReRequestParser.getInstanceReqStream().fromXML(xml);
		}
		catch (Exception e) {
			logger.error("Exception ",e);
		}
		return requestHeader;
	}
	
	private String createResponse(String statusCode ,String statusDesc,RERequestHeader requestHeader)
	{
		String xml = null;
		try
		{
			requestHeader.setStatus(statusCode);
			requestHeader.setStatusDesc(statusDesc);
			xml = ReResponseParser.getInstanceReqStream().toXML(requestHeader);
		}
		catch (Exception e) {
			logger.error("Exception , ",e);
		}
		
		return xml;
	}
	
	private void sendResp(HttpServletResponse resp, String message,String referenceNo) {
		BufferedWriter bufferWriter = null;
		try{
			logger.info(referenceNo+" : Response ==> "+message);
			bufferWriter = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
			if(message != null){
				bufferWriter.write(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		if(bufferWriter != null){
			try {
				bufferWriter.flush();
				bufferWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
			
	}
}
