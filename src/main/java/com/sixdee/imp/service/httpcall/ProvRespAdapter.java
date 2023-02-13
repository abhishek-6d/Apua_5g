/**
 * 
 */
package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.SubscriberProfileBL;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.SubscriberProfileDTO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequest;
import com.sixdee.imp.threadpool.ThreadInitiator;
import com.sixdee.imp.util.parser.ReResponseParser;
import com.thoughtworks.xstream.XStream;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ProvRespAdapter extends HttpServlet {

	Logger logger = Logger.getLogger(InstantReReqAdapter.class);
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,1);
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,2);
	}


	private void execute(HttpServletRequest req, HttpServletResponse res,int method) {
		
		InputStream inputStream = null;
		StringBuffer sb = null;
		String xml = null;
		String respCode = null;
		String respMsg = null;
		SubscriberProfileDTO subscriberDTO=null;
		RequestProcessDTO processDTO=null;
		RERequestHeader subsReqDTO = null;
		XStream xstream= ReResponseParser.getInstanceReqStream();	
		boolean isSynch = false;
		GenericDTO genericDTO = null;
		SubscriberProfileBL subscriberProfileBL = null;
		try {

		//	logger.info(" ************* Request Reached ***************** ");

			inputStream = new BufferedInputStream(req.getInputStream(),32 * 1024);
			sb = new StringBuffer();

			int character = inputStream.read();

			while (character != -1) {
				sb.append((char) character);
				character = inputStream.read();
			
			}			
			xml =sb.toString();
			
			
			if(xml!=null && xml.length()!=0)
			{	
				logger.info("REQUEST recieved "+xml);
				//subscriberDTO= new SubscriberProfileDTO();
				subsReqDTO= (RERequestHeader) xstream.fromXML(xml);			
				//logger.info("Subscriber Profile "+subsReqDTO);
				//subscriberDTO.setReqXML(xml);
				
				
				long start=System.currentTimeMillis();
				//logger.info("Request firing "+System.currentTimeMillis());
			
			
					
					//ThreadInitiator.provRespPool.addTask(subsReqDTO);	
				long end=System.currentTimeMillis()-start;
			//	logger.debug("Request processed in "+end+" milliseconds");
				if(!isSynch){
					respCode="0";
					respMsg = "Succesfully Processed";
				}
			}
			else
			{
				logger.info("REQUEST XML is NULL...!!!");
				if(!isSynch){
					respCode="1";
					respMsg = "XML not Found in Request";
				}else{
					subsReqDTO = new RERequestHeader();
					subsReqDTO.setStatus("SC0001");
					subsReqDTO.setStatusDesc("XML is null");
				}
			}		
		//		SendRespMsg(res,respCode, respMsg);
		}catch (Exception e) {		
			e.printStackTrace();		
			logger.error("Exception Occured in Execution:::"+xml);
			if(!isSynch){
				respCode="1";
				respMsg = "Invalid XML.Please Check your XML Format";
			}else{
				if(subsReqDTO == null){
					subsReqDTO = new RERequestHeader();
				}
				subsReqDTO.setStatus("SC0002");
				subsReqDTO.setStatusDesc("System Failure");
			}
				SendRespMsg(res,respCode, respMsg);
		
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputStream = null;
			}
			xml = null;
			sb = null;
			respCode = null;
			respMsg = null;	
			subscriberDTO=null;
			processDTO=null;
		}
	}
	public void SendRespMsg(HttpServletResponse res, String respCode,String respMsg) 
	{
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));
		    if (respMsg == null) 
		    {
		    	throw new Exception("cannot send ack message");
		    }
		    writer.write("<RSP><RESULT>SUCCESS</RESULT><ERR><NO>0</NO><DESC>SUCCESSFULLY PROCESSED</DESC></ERR></RSP>");
		    writer.flush();
		}
		catch (Exception exp) 
		{
			logger.error("Exception Occured while Sending Response:::"+exp);
		}
		finally 
		{
			if(writer != null){
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    respCode = null;
		    respMsg =null;
		}
	}

	
	
}
