package com.sixdee.imp.bo;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>May 14,2013 06:02:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.SubscriberProfileDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.ActionServiceDetailsDTO;
import com.sixdee.imp.service.httpcall.dto.SubscriberDataSet;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequest;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequestParam;

public class SubscriberProfileBL extends BOCommon {
	
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	
	DateFormat df=new SimpleDateFormat("ddMMyyyy HH:mm:ss");
	Logger logger = Logger.getLogger(SubscriberProfileBL.class);
	
	
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

	//	logger.info("Class => SubscriberProfileBL :: Method => buildProcess()");
		 TableInfoDAO tableInfoDAO = null;
		Date today=new Date();		
		SubscriberRequest subsReqDTO = (SubscriberRequest) genericDTO.getObj();
		String tableName=null;
		SubscriberProfileDAO subProfileDAO=null;
		ActionServiceDetailsDTO serviceDTO=null;
		String msisdn = null;
		String ruleName = null;
		String serviceName = null;
		ArrayList<SubscriberRequestParam> param = null;
		SubscriberDataSet subscriberDataSet = null;
		int i = 0;
		Object[] createDate = null;
		boolean insertsuccess=true;
		boolean insertfailure=true;
		boolean isSync = false;	
		String rspURL = null;
		try{
			//subsReqDTO = (SubscriberRequest) reqDTO.();
			logger.info("Request recieved ");
			long start=System.currentTimeMillis();
			tableInfoDAO = new TableInfoDAO();
			subProfileDAO=new SubscriberProfileDAO();
			serviceDTO = null;
				//subsDTO.setTransactionId(subsReqDTO.getRequestId());
			isSync = subsReqDTO.isSync();
			subscriberDataSet = subsReqDTO.getDataSet();
			param = subscriberDataSet.getParam();
		/*	for(int i=0;i<subsReqDTO.getDataSet().getParam().size();i++)
			{
				if(subsReqDTO.getDataSet().getParam().get(i).getId().equalsIgnoreCase("MSISDN"))
					subsDTO.setSubscriberNumber(Long.parseLong(subsReqDTO.getDataSet().getParam().get(i).getValue()));
				else if(subsReqDTO.getDataSet().getParam().get(i).getId().equalsIgnoreCase("ACTION_KEY"))
					subsDTO.setServiceName(subsReqDTO.getDataSet().getParam().get(i).getValue());
				else if(subsReqDTO.getDataSet().getParam().get(i).getId().equalsIgnoreCase("SERVICE_NAME"))
					subsDTO.setRuleName(subsReqDTO.getDataSet().getParam().get(i).getValue());	
			}	*/
			for(SubscriberRequestParam subscriberRequestParam:param){
				if(subscriberRequestParam.getId().equalsIgnoreCase("MSISDN")){
					msisdn = subscriberRequestParam.getValue();
					i++;
				}else if(subscriberRequestParam.getId().equalsIgnoreCase("ACTION_KEY")){
					serviceName = subscriberRequestParam.getValue();
					i++;
				}else if(subscriberRequestParam.getValue().equalsIgnoreCase("SERVICE_NAME")){
					ruleName = subscriberRequestParam.getValue();
					i++;
				}else if(subscriberRequestParam.getId().equalsIgnoreCase("ACTION_RESP_URL")){
					rspURL = subscriberRequestParam.getValue();
					i++;
				}
				if(i==4)
					break;
			}
			/*if(msisdn==null || serviceName==null)
				process=false;
			*/
			
			//Getting tableName
			if(!(msisdn==null || serviceName==null))
				{
				tableName = tableInfoDAO.getSubscriberProfileDBTable(msisdn);
				serviceDTO = Cache.serviceDetailsMap.get(serviceName.toUpperCase());
			
				if (serviceDTO != null) 
					
				{
					/*subsDTO.setStartDate(today);
					subsDTO.setEndDate(serviceDTO.getEndDate());
					subsDTO.setServiceId(serviceDTO.getServiceID());
				*/	logger.info("SUBSCRIBER NUMBER ::"+ msisdn);

					if ((createDate=subProfileDAO.getSubscriberProfileDetails(tableName,serviceDTO.getServiceID(),msisdn))==null)
						insertsuccess=subProfileDAO.addSubscriberDetails(msisdn, serviceDTO.getServiceID(),  serviceName,ruleName, today, serviceDTO.getEndDate());
					else if(subProfileDAO.isNewPromo(tableName, serviceDTO.getServiceID(), msisdn, createDate[0].toString())){
							subProfileDAO.updateSubscriberDetails(msisdn,serviceDTO.getServiceID(),(Integer)createDate[1]);
						
						}
					if(!isSync){
						if(insertsuccess)
							sendResponse(subsReqDTO,msisdn,rspURL,1);
					}
				}
				else 
				{
					subsReqDTO.setStatus("SC0001");
					subsReqDTO.setStatusDesc("Service Not Identified "+serviceName.toUpperCase());
					insertfailure=subProfileDAO.addFailureDetails(subsReqDTO.getRequestId(),msisdn,serviceName,ruleName,"Service ID Not Found");
					if(!isSync){
						if(insertfailure)
							sendResponse(subsReqDTO,msisdn,rspURL,2);
					}
				}

			}
			else
			{	
				if(msisdn==null)
				{	
					subProfileDAO.addFailureDetails(subsReqDTO.getRequestId(),msisdn,serviceName,ruleName,"MSISDN Tag Not Found");
						subsReqDTO.setStatus("SC0001");
						subsReqDTO.setStatusDesc("MANDATORY PARAM MISSING - MSISDN");
					if(!isSync)
						sendResponse(subsReqDTO,msisdn,rspURL,2);
				}
				else if (serviceName==null)
				{	
					subProfileDAO.addFailureDetails(subsReqDTO.getRequestId(),msisdn,serviceName,ruleName,"ACTION_KEY Tag Not Found");
							subsReqDTO.setStatus("SC0001");
							subsReqDTO.setStatusDesc("MANDATORY PARAM MISSING - Action Key");		
					if(!isSync)	
						sendResponse(subsReqDTO,msisdn,rspURL,2);
				
				}
			}
			
			long finaltime=System.currentTimeMillis()-start;
			//logger.info("OVER ALL IN SubscriberProfileBL()#######################"+finaltime);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			subProfileDAO.addFailureDetails(subsReqDTO.getRequestId(),msisdn,serviceName,ruleName,"Invalid XML Format");	
			subsReqDTO.setStatus("SC0002");
			subsReqDTO.setStatusDesc("System Error");
			if(!isSync)
				sendResponse(subsReqDTO,msisdn,rspURL,3);
			
			
		}
		finally{
			serviceDTO=null;
			tableName=null;
			//subscriberDTO=null;
			//subsDTO=null;
			createDate = null;
			subProfileDAO = null;
			subsReqDTO = null;
			subsReqDTO = null;
			//xstream = null;
			param = null;
			ruleName = null;
			serviceDTO = null;
			subscriberDataSet = null;
			serviceName= null;
			today = null;
			
		}
					
			return genericDTO;
		}
	private void sendResponse(SubscriberRequest subsReqDTO,String msisdn,String rspURL, int option)  {	
		String txnId="";
		String subNum="";
		String resp=null;
		StringBuffer sb = new StringBuffer();
		Date date = null;
		String urlPath=rspURL!=null?rspURL:Cache.cacheMap.get("RULE_ENGINE_URL");
		//logger.info("URL to fire the request"+urlPath);
		//String urlPath="http://10.0.0.69:9310/RuleEngine/Action";
		URL url = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		OutputStreamWriter out = null;
		String decodedString;
		String response = null;
		
		try{
			if(urlPath ==null || (urlPath.trim().equalsIgnoreCase(""))){
			}else{
			 url = new URL(urlPath.trim());
			 connection =(HttpURLConnection) url.openConnection();
				date = new Date();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);		
			 out = new OutputStreamWriter(connection.getOutputStream());
			if(option==1)
			{
			resp="<Response>"+
			"<ClientTxnId>"+subsReqDTO.getRequestId()+"</ClientTxnId>"+
			"<Timestamp>"+date+"</Timestamp>"+
			"<RespCode>SC0000</RespCode>"+
			"<RespDesc>SUCCESS</RespDesc>"+
			"<Msisdn>"+msisdn+"</Msisdn>"+
			"</Response>";
			out.write(resp);
			out.flush();
			}
			else if (option==2)
			{
				if(subsReqDTO.getRequestId()!=null)
				txnId=subsReqDTO.getRequestId();
				else
				txnId=df.format(date);
				if(msisdn!=null)
					subNum=""+msisdn;
				else
					subNum="MSISDN Not Found";
				if(subsReqDTO.getMsisdn()!=null)
				{
					resp="<Response>"+
					"<ClientTxnId>"+txnId+"</ClientTxnId>"+
					"<Timestamp>"+df.format(date)+"</Timestamp>"+
					"<RespCode>SC0001</RespCode>"+
					"<RespDesc>FAILURE-Service ID Not Found</RespDesc>"+
					"<Msisdn>"+subNum+"</Msisdn>"+
					"</Response>";
						out.write(resp);
						out.flush();
					
				}
				else
				{
					resp="<Response>"+
					"<ClientTxnId>"+txnId+"</ClientTxnId>"+
					"<Timestamp>"+df.format(date)+"</Timestamp>"+
					"<RespCode>SC0001</RespCode>"+
					"<RespDesc>FAILURE-Invalid XML Format</RespDesc>"+
					"<Msisdn>"+subNum+"</Msisdn>"+
					"</Response>";
						out.write(resp);
						out.flush();
					
				}
				
			}
			else
			{
				if(subsReqDTO.getRequestId()!=null)
					txnId=subsReqDTO.getRequestId();
					else
					txnId=df.format(date);
					if(msisdn!=null)
					subNum=""+msisdn;
					else
					subNum="MSISDN Not Found";	
					resp="<Response>"+
							"<ClientTxnId>NA</ClientTxnId>"+
							"<Timestamp>"+df.format(date)+"</Timestamp>"+
							"<RespCode>SC0002</RespCode>"+
							"<RespDesc>FAILURE-Invalid XML Format</RespDesc>"+
							"<Msisdn>NA</Msisdn>"+
							"</Response>";
					out.write(resp);	
					out.flush();
					
				
			}

			
			
			 in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((decodedString = in.readLine()) != null){
				sb.append(decodedString.trim());
			}
			response = sb.toString();
			
			logger.info("REQUEST SEND ::"+resp +" on "+urlPath+ " Response "+response);
			}
			
		} catch (IOException e) {
			logger.error("Exception occured",e);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			if(out!=null)
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(in != null)
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(connection!=null)
				connection.disconnect();
			sb = null;
			url = null;
			sb= null;
			urlPath = null;
			resp = null;
			subsReqDTO = null;
			txnId = null;
			decodedString = null;
		}
		
		
	}
	
}
