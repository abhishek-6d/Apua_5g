package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.httpcall.dto.DataSet;
import com.sixdee.imp.service.httpcall.dto.Parameters;
import com.sixdee.imp.service.httpcall.dto.Request;
import com.sixdee.imp.service.serviceDTO.req.RedeemDTO;

public class LuckyDraw  extends HttpServlet  {

	Logger logger=Logger.getLogger(LuckyDraw.class);
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) {
		BufferedInputStream bis = null;
		StringBuilder sb = null;
		String xml = null;
		RedeemDTO redeemDTO=null;		
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO=null;
		try{
			bis = new BufferedInputStream(req.getInputStream(), 32 * 1024);
			int i;
			sb = new StringBuilder();
			while ((i = bis.read()) != -1) {
				sb.append((char) i);
			}

			xml = sb.toString();
			logger.info("Request => " + xml);
			if(xml!=null)
			{
			redeemDTO=validateXml(xml);
			}
			else{
				SendRespMsg(resp, "SC0001",
						"Request Xml is comming as null");
			}
			adapter=new LMSWebServiceAdapter();
			if(redeemDTO!=null)
			{
			genericDTO = adapter.callFeature("RedeemPoints", redeemDTO);
			logger.info(" Service:LuckyDraw TransactionId:"+redeemDTO.getTransactionID()+"Response from Redeem service:"+genericDTO.getStatusCode());
			SendRespMsg(resp, genericDTO.getStatusCode(),
					genericDTO.getStatus());
			}
			logger.info("Service:LuckyDraw TransactionId:"+redeemDTO.getTransactionID()+" Finished Processing");
		}
		catch(Exception e){
			logger.error("xml:"+xml+"Service:LuckyDraw Exception occured:"+e);
			e.printStackTrace();
			
		
		}
		finally{
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
				
			}
			 bis = null;
			 sb = null;
			 xml = null;
			 redeemDTO=null;		
			 adapter=null;
			 genericDTO=null;
		}
		
	}

	private void SendRespMsg(HttpServletResponse res, String respCode,
			String respMsg) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					res.getOutputStream()));
			if (respMsg == null) {
				throw new Exception("cannot send ack message");
			}
			// writer.write("<RSP><RESULT>SUCCESS</RESULT><ERR><NO>0</NO><DESC>SUCCESSFULLY PROCESSED</DESC></ERR></RSP>");
			writer.write(respMsg);

			writer.flush();
		} catch (Exception exp) {
			logger.error("Exception Occured while Sending Response:::" + exp);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	private RedeemDTO validateXml(String xml) {

		Request request = null;				
		RedeemDTO redeemDTO=null;
		try {			
			request = (Request) Cache.coomonReqXstream.fromXML(xml);
			logger.info("Service:LuckyDraw TransactionId:"+request.getRequestId()+"Xml parsing finished");
			DataSet dataSet = request.getDataSet();
			if(request.getRequestId()==null)
			{
				throw new CommonException("Request id not recieved");
			}
			redeemDTO=new RedeemDTO();			
			redeemDTO.setTransactionID(request.getRequestId());
			redeemDTO.setTimestamp(request.getTimeStamp());
		
			if (dataSet != null) {
				for (Parameters param : dataSet.getParams()) {
					if (param.getId().equalsIgnoreCase("MSISDN")) {
						redeemDTO.setMoNumber(param.getValue());
						redeemDTO.setLineNumber(param.getValue());

					}
					if (param.getId().equalsIgnoreCase("CHANNEL")) {
						redeemDTO.setChannel(param.getValue()); 

					}
					
					if (param.getId().equalsIgnoreCase("PACK_ID")) {
						redeemDTO.setPackID(Integer.parseInt(param.getValue())); 

					}
				}
			}
			if(redeemDTO.getMoNumber()==null)
			{
				throw new CommonException("MSISDN not recieved");
			}
			
			redeemDTO.setLanguageId(1);
			logger.info("Service:LuckyDraw TransactionId:"+request.getRequestId()+" Msisdn:"+redeemDTO.getMoNumber()+" Channel:"+redeemDTO.getChannel()+" PackId:"+redeemDTO.getPackID());

		} catch (Exception e) {
			logger.error("Service:LuckyDraw Exception occoured" + e + " request xml is :" + xml);
			e.printStackTrace();
		}
		return redeemDTO;
	
		
	}
}
