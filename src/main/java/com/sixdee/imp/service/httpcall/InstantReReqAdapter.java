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

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequestParam;
import com.sixdee.imp.threadpool.ThreadInitiator;
import com.sixdee.imp.util.parser.ReRequestParser;
import com.sixdee.imp.vo.InstantRewardsVO;

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
public class InstantReReqAdapter extends HttpServlet{

	Logger logger = Logger.getLogger(InstantReReqAdapter.class);
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
		InstantRewardsVO instantRequestDTO = null;
		GenericDTO genericDTO = null;
		ArrayList<SubscriberRequestParam> reqParamList = null;
		try{
			long t1 = System.currentTimeMillis();
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
		    	xml = sb.toString();
		    	logger.debug("Request recieved in POST Mode ["+xml+"]");

				
			}
			boolean isSynch = false;
			boolean isRedeem = false;
				//instantRequestDTO.setDataSet(subsReqDTO.get)
			if(xml != null){
				int i =0;
				subsReqDTO = (RERequestHeader) ReRequestParser.getInstanceReqStream().fromXML(xml);
				logger.info("Request recieved ["+subsReqDTO.getRequestId()+"]");
				RERequestDataSet dataSet = subsReqDTO.getDataSet();
				instantRequestDTO = new InstantRewardsVO();
				instantRequestDTO.setTransactionId(subsReqDTO.getRequestId());
				instantRequestDTO.setTimestamp(subsReqDTO.getTimeStamp());
				
				//instantRequestDTO.setbSubscriberNumber(subsReqDTO.getFeatureId());
				//instantRequestDTO.setDataSet(dataSet);
				if(dataSet !=null){
				
					for (ReResponseParameter param : dataSet.getResponseParam()) {
						if (param != null) {
							if (param.getId().equalsIgnoreCase("ACTION_TYPE")) {
								if (param.getValue().equalsIgnoreCase("synch")) {
									isSynch = true;
								}
							} else if (param.getId().equalsIgnoreCase("MSISDN")) {
								instantRequestDTO.setSubscriberNumber(param
										.getValue().trim());
								i++;
							} else if (param.getId()
									.equalsIgnoreCase("B_PARTY")) {
								instantRequestDTO.setbSubscriberNumber(param
										.getValue().trim());
								if(!instantRequestDTO.isReverseTransfer())
									instantRequestDTO.setTransfer(true);
								i++;
							} else if (param.getId().equalsIgnoreCase(
									"OFFER_NAME")) {
								instantRequestDTO.setOfferId(param.getValue()
										.trim());
								i++;
							} else if (param.getId().equalsIgnoreCase(
									"ACTION_RESP_URL")) {
								instantRequestDTO.setRespUrl(param.getValue());
							} else if (param.getId().equalsIgnoreCase(
									"TRANSFER_TYPE")) {
								if (param.getValue().trim()
										.equalsIgnoreCase("RT")){
									instantRequestDTO.setReverseTransfer(true);
									instantRequestDTO.setTransfer(false);
									logger.debug("Reverse Transfer is success");
								}
							}
						}

					}
					if(i==0){
						throw new CommonException("Mandatory Parameter is missing");
					}
					if(instantRequestDTO.isTransfer() && instantRequestDTO.getOfferId()==null)
						throw new CommonException("No Offer Name specified for Transfer "+instantRequestDTO.getbSubscriberNumber()+" from ["+instantRequestDTO.getSubscriberNumber()+"]");
				}
				subsReqDTO.setObj(instantRequestDTO);
				logger.info("Is Transfer ["+instantRequestDTO.isTransfer()+"] for Request ["+instantRequestDTO.getTransactionId()+"] Response Url ["+instantRequestDTO.getRespUrl()+"]");
				if(isSynch){

					genericDTO = new GenericDTO();
					genericDTO.setObj(subsReqDTO);
					
				//	InstantRewardsBO instantRewardsBO = new InstantRewardsBO();
				//	genericDTO  = instantRewardsBO.buildProcess(genericDTO);
					//subsReqDTO = (SubscriberRequest) genericDTO.getObj();
				
				}else{
					RequestProcessDTO processDTO = new RequestProcessDTO();
					processDTO=new RequestProcessDTO();
					processDTO.setFeatureName("InstantRewards");
					processDTO.setObject(subsReqDTO);
					SendRespMsg(resp,"SC0000","SUCCESS");
					logger.info("Success Response Send , adding to Threadpool ["+subsReqDTO.getRequestId()+"] @ ["+(System.currentTimeMillis()-t1)+"]");
					//ThreadInitiator.instantRewardPool.addTask(processDTO);
				}
			}else{
				logger.error("Request is empty");
			}
		}catch(CommonException e){
			logger.error("Common Exception ["+e.getMessage()+"]");
			SendRespMsg(resp,"FC0001",e.getMessage());
			
		}
		catch(Exception e){
			logger.error("Exception occured ",e);
			SendRespMsg(resp,"FC0002","Request Failed");
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
		    //writer.write("<RSP><RESULT>SUCCESS</RESULT><ERR><NO>0</NO><DESC>SUCCESSFULLY PROCESSED</DESC></ERR></RSP>");
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
