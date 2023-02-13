/**
 * 
 */
package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.threadpool.ThreadInitiator;
import com.sixdee.imp.util.parser.ReResponseParser;

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
 * <td>December 23, 2013</td>
 * <td>Rahul K K</td>
 * <td>This class will recieve the request from MAGIK for the cases where in call is failure</td>
 * </tr>
 * </table>
 * </p>
 */
public class IRStatusAdapter extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9159998004259324710L;
	private static Logger logger = Logger.getLogger(IRStatusAdapter.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,2);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp, int method) {
		String xml = null;
		BufferedInputStream bis = null;
		StringBuffer sb = null;
		try{
			/*bis = new BufferedInputStream(req.getInputStream(),32 * 1024);
			sb = new StringBuilder();int character = -1;
			while((character = bis.read())==-1)
				sb.append((char)character);
			xml = sb.toString();*/
			if(method == 1){
				xml=req.getQueryString();
				logger.info("Request recieved in GET Mode ["+xml+"]");
			}else {
				
				bis = new BufferedInputStream(req.getInputStream(),32 * 1024);
			
		    	sb = new StringBuffer();
		    	int character = bis.read();
		    	while (character != -1) {
		    		
		    		sb.append((char) character);
		    		character = bis.read();
		    	}
		    	xml = sb.toString();
		    	logger.debug("Request recieved in POST Mode ["+xml+"]");

				
			}
			if(logger.isDebugEnabled())
				logger.debug("Transaction Id :-  Message :- XML Recieved "+xml);
			RERequestHeader requestHeader = validateXml(xml);
			
			RequestProcessDTO processDTO = new RequestProcessDTO();
			processDTO=new RequestProcessDTO();
			processDTO.setFeatureName("IRStatusUpdation");
			processDTO.setObject(requestHeader);
			SendRespMsg(resp,"SC0000","SUCCESS");
			logger.info("Success Response Send , adding to Threadpool ["+requestHeader.getRequestId()+"] ");
			//ThreadInitiator.instantRewardPool.addTask(processDTO);
		
		} catch (IOException e) {
			logger.error("Some problem with Network IO , Please check on priority after confirmation from technical team",e);
			SendRespMsg(resp,"SC0001","FAILURE");
			
		} catch (CommonException e) {
			logger.error("Some thing wrong in the format it seems , "+e.getMessage());
			SendRespMsg(resp,"SC0002",e.getMessage());
			
		}finally{
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private RERequestHeader validateXml(String xml) throws CommonException {
		RERequestHeader requestHeader = null;
		RERequestDataSet dataSet =  null;
		ArrayList<ReResponseParameter> reResp = null;
		String reqId = null;
		boolean isMsisdnPresent = false;
		boolean isOfferNamePresent = false;
		try{
			requestHeader = (RERequestHeader) ReResponseParser.getInstanceReqStream().fromXML(xml);
			reqId =requestHeader.getRequestId(); 
			if(reqId==null){
				throw new CommonException("Need to have RequestId in request , Please try again with proper request format");
			}
			
			
		}finally{
			
		}
		return requestHeader;
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
		   // writer.write("<RSP><RESULT>SUCCESS</RESULT><ERR><NO>0</NO><DESC>SUCCESSFULLY PROCESSED</DESC></ERR></RSP>");
		    writer.write(respMsg);
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
