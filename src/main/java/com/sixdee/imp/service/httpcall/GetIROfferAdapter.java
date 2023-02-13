/**
 * 
 */
package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.util.parser.ReRequestParser;
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
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GetIROfferAdapter extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(GetIROfferAdapter.class);
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,1);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,2);

	}

	private void execute(HttpServletRequest req, HttpServletResponse resp, int method) {
		String xml = null;
		BufferedInputStream bis = null;
		StringBuffer sb = null;
		HashMap<String, String> reqMap = null;
		String status = "SC0000";
		String statusDesc = "SUCCESS";
		RERequestHeader requestHeader = null;
		String respXml = null;
		try{
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
			 requestHeader = validateXml(xml);
			reqMap = requestHeader.getRespMap();
			String offerName = reqMap.get("OFFER_NAME");
			String offerSynonym = Cache.instantOfferMapping.get(offerName);
			if(offerSynonym != null){
				ReResponseParameter reResponseParameter = new ReResponseParameter();
				reResponseParameter.setId("OFFER_SYN");
				reResponseParameter.setValue(offerSynonym);
				requestHeader.getDataSet().getResponseParam().add(reResponseParameter);
			}
		} catch (CommonException e) {
			logger.error(e);
			status = "SC0001";
			statusDesc = e.getMessage();
		} catch (IOException e) {
			logger.error(e);
			status = "SC0001";
			statusDesc = e.getMessage();
		}catch (Exception e) {
			logger.error(e);
			status = "SC0001";
			statusDesc = e.getMessage();
		}
		finally{
			requestHeader.setStatus(status);
			requestHeader.setStatusDesc(statusDesc);
			respXml = ReResponseParser.getInstanceReqStream().toXML(requestHeader);
			SendRespMsg(resp, status, respXml);
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			xml = null;
			sb = null;
			requestHeader = null;
			reqMap = null;
			resp = null;
			respXml = null;
		}
	}
		
		private RERequestHeader validateXml(String xml) throws CommonException {
			RERequestHeader requestHeader = null;
			RERequestDataSet dataSet =  null;
			ArrayList<ReResponseParameter> reResp = null;
			String reqId = null;
			HashMap<String, String> respMap = new HashMap<String, String>();
			try{
				requestHeader = (RERequestHeader) ReRequestParser.getInstanceReqStream().fromXML(xml);
				reqId =requestHeader.getRequestId(); 
				if(reqId==null){
					throw new CommonException("Need to have RequestId in request , Please try again with proper request format");
				}
				reResp = requestHeader.getDataSet().getResponseParam();
				for(ReResponseParameter response : reResp){
					if(response.getId()!=null)
						respMap.put(response.getId().toUpperCase(), response.getValue());
				}
				requestHeader.setRespMap(respMap);
				
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
