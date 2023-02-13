/**
 * 
 */
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
import com.sixdee.imp.dto.RedeemIRDTO;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.threadpool.ThreadInitiator;
import com.sixdee.imp.util.parser.ReRequestParser;

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
public class RedeemReqAdapter extends HttpServlet{

	Logger logger = Logger.getLogger(RedeemReqAdapter.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,1);
	}
	
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
		execute(req,resp,2);
	}
	
	private void execute(HttpServletRequest req, HttpServletResponse resp, int method) {
		String xml = null;
		BufferedInputStream inputStream = null;
		StringBuffer sb = null;
		RERequestHeader subsReqDTO = null;
		RedeemIRDTO instantRequestDTO = null;
		GenericDTO genericDTO = null;
		try{
			if(method == 1){
				xml=req.getQueryString();
				logger.info("Request recieved in GET Mode ["+xml+"]");
			}else {
				
				inputStream = new BufferedInputStream(req.getInputStream(),32 * 1024);
			
		    	sb = new StringBuffer();
		    	int character = inputStream.read();
		    	while (character != -1) {
		    		
		    		sb.append((char) character);
		    		character = inputStream.read();
		    	}
		    	if (sb.toString() != null && !sb.toString().equals("")){
		    		xml = sb.toString();
		    		logger.info("Request recieved in POST Mode ["+xml+"]");
		    	}

				
			}
			boolean isSynch = false;
			boolean isRedeem = false;
				//instantRequestDTO.setDataSet(subsReqDTO.get)
			if(xml != null){
				int i =0;
				subsReqDTO = (RERequestHeader) ReRequestParser.getInstanceReqStream().fromXML(xml);
				
				RERequestDataSet dataSet = subsReqDTO.getDataSet();
				instantRequestDTO = new RedeemIRDTO();
				instantRequestDTO.setTransactionId(subsReqDTO.getRequestId());
				//logger.info(instantRequestDTO.getTransactionId());
				instantRequestDTO.setTimestamp(subsReqDTO.getTimeStamp());
				//logger.info(instantRequestDTO.getTimestamp());
				
				//instantRequestDTO.setbSubscriberNumber(subsReqDTO.getFeatureId());
				//instantRequestDTO.setDataSet(dataSet);
				if(dataSet !=null){
					String paramName = null;
					String paramValue = null;
					for (ReResponseParameter param : dataSet.getResponseParam()) {
						//logger.info(param.getId()+" Value "+param.getValue());
						paramName = param.getId();
						if (paramName.equalsIgnoreCase("ACTION_TYPE")) {
							if (param.getValue().equalsIgnoreCase("synch")) {
								isSynch = true;
							}
						} else if (paramName.equalsIgnoreCase("MSISDN")) {
							instantRequestDTO.setSubscriberNumber(param
									.getValue().trim());
							i++;
						}  else if (paramName.equalsIgnoreCase("OFFER_NAME")||paramName.equalsIgnoreCase("KEYWORD")) {
							instantRequestDTO.setOfferId(param.getValue()
									.trim());
							i++;
						}else if (paramName.equalsIgnoreCase("ACTION_RESP_URL")) {
							subsReqDTO.setUrl(param.getValue());
						}
						
					}
					
					if(instantRequestDTO.getSubscriberNumber()==null)
						throw new CommonException("Subscriber Number recieved is null");
					else if(instantRequestDTO.getOfferId()==null)
						throw new CommonException("Offer Id recieved is null");
				}
				subsReqDTO.setObj(instantRequestDTO);
				logger.info("Redeem Request For ["+subsReqDTO.getRequestId()+"] MO ["+subsReqDTO.getMsisdn()+"] Response URL ["+subsReqDTO.getUrl()+"]");
				if(isSynch){

					genericDTO = new GenericDTO();
					genericDTO.setObj(subsReqDTO);
					
					//RedeemIRBO instantRewardsBO = new RedeemIRBO();
					//genericDTO  = instantRewardsBO.buildProcess(genericDTO);
					//subsReqDTO = (SubscriberRequest) genericDTO.getObj();
				
				}else{
					RequestProcessDTO processDTO = new RequestProcessDTO();
				//	processDTO=new RequestProcessDTO();
					processDTO.setFeatureName("RedeemIR");
					processDTO.setObject(subsReqDTO);
				
					//ThreadInitiator.instantRewardPool.addTask(processDTO);
					SendRespMsg(resp, "SC0000", "SUCCESS");
				}
			}else{
				logger.error("Request is empty");
				SendRespMsg(resp, "SC0001", "Request Xml is empty");
				
			}
		}catch(CommonException e){
			logger.error("Common Exception ["+e.getMessage()+"]");
			SendRespMsg(resp, "SC0001", e.getMessage());
		}
		catch(Exception e){
			logger.error("Exception occured ",e);
			SendRespMsg(resp, "SC0001", "Request Failed");
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
