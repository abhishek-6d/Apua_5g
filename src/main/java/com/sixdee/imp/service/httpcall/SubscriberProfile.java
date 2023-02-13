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
import com.sixdee.imp.dto.RERespone;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.SubscriberProfileDTO;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequest;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequestParam;
import com.sixdee.imp.threadpool.ThreadInitiator;
import com.sixdee.imp.util.RuleEngineRespXStream;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>MAY 11, 2013</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */
public class SubscriberProfile extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(SubscriberProfile.class);

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		execute(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		execute(req, res);
	}

	private void execute(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		
		InputStream inputStream = null;
		StringBuffer sb = null;
		String xml = null;
		String respCode = null;
		String respMsg = null;
		SubscriberProfileDTO subscriberDTO=null;
		RequestProcessDTO processDTO=null;
		SubscriberRequest subsReqDTO = null;
		XStream xstream= Cache.subscriberProfileXstream;	
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
				subsReqDTO= (SubscriberRequest) xstream.fromXML(xml);			
				//logger.info("Subscriber Profile "+subsReqDTO);
				//subscriberDTO.setReqXML(xml);
				
				processDTO=new RequestProcessDTO();
				processDTO.setFeatureName("SubscriberProfile");
				processDTO.setObject(subsReqDTO);
				// Adding to Thread pool
				long start=System.currentTimeMillis();
				//logger.info("Request firing "+System.currentTimeMillis());
			
				for(SubscriberRequestParam param : subsReqDTO.getDataSet().getParam()){
					if(param.getId()!=null && param.getValue() != null && param.getId().trim().equalsIgnoreCase("ACTION_TYPE")&&param.getValue().trim().equalsIgnoreCase("SYNCH")){
						isSynch = true;
						subsReqDTO.setSync(isSynch);
						break;
					}
				}
				if(isSynch){
					genericDTO = new GenericDTO();
					genericDTO.setObj(subsReqDTO);
					subscriberProfileBL = new SubscriberProfileBL();
					genericDTO  = subscriberProfileBL.buildProcess(genericDTO);
					subsReqDTO = (SubscriberRequest) genericDTO.getObj();
				}else	
					ThreadInitiator.requestProcessThreadPool.addTask(processDTO);	
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
					subsReqDTO = new SubscriberRequest();
					subsReqDTO.setStatus("SC0001");
					subsReqDTO.setStatusDesc("XML is null");
				}
			}		
			if(!isSynch)
				SendRespMsg(res,respCode, respMsg);
			else 
				sendSyncResp(res, subsReqDTO);
		}catch (Exception e) {		
			e.printStackTrace();		
			logger.error("Exception Occured in Execution:::"+xml);
			if(!isSynch){
				respCode="1";
				respMsg = "Invalid XML.Please Check your XML Format";
			}else{
				if(subsReqDTO == null){
					subsReqDTO = new SubscriberRequest();
				}
				subsReqDTO.setStatus("SC0002");
				subsReqDTO.setStatusDesc("System Failure");
			}
			if(!isSynch)
				SendRespMsg(res,respCode, respMsg);
			else 
				sendSyncResp(res, subsReqDTO);
	
		} finally {
			if (inputStream != null) {
				inputStream.close();
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
		
	
	public void sendSyncResp(HttpServletResponse resp,SubscriberRequest subsReqDTO){
		RERespone reResponse = formResponseDTO(subsReqDTO);
		BufferedWriter writer = null;
		String respMsg = null;
		try
		{
		    writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
		    respMsg = RuleEngineRespXStream.getRERespXStream().toXML(reResponse);
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
		    respMsg =null;
		}
	
	}
		private RERespone formResponseDTO(SubscriberRequest subsReqDTO) {
			RERespone reRespone = new RERespone();
			reRespone.setCliTransactionId(subsReqDTO.getRequestId());
			reRespone.setMsisdn(subsReqDTO.getMsisdn());
			reRespone.setRespCode(subsReqDTO.getStatus());
			reRespone.setRespDesc(subsReqDTO.getStatusDesc());
			reRespone.setTimeStamp(subsReqDTO.getTimeStamp());
			return reRespone;
			
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
