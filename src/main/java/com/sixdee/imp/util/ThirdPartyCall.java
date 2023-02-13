/**
 * 
 */
package com.sixdee.imp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.RevertLoyaltyDAO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.threadpool.ThreadInitiator;
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
public class ThirdPartyCall {

	private Logger logger = Logger.getLogger(ThirdPartyCall.class);
	
	public String makeThirdPartyCall(String xml,String tpUrl,int timeout,boolean isRetry){

		StringBuffer sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;

		try {
			sb = new StringBuffer();
			url = new URL(tpUrl.trim());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(xml);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
		} catch (Exception e) {
			logger.error("Error while calling URL:"+tpUrl+", Exception:",e);
		//	throw e;
		} finally {
			url = null;
			
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out = null;

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in = null;
			
			if(connection!=null){
				connection.disconnect();
			}
			
			connection = null;
			
			decodedString = null;
		}
		return sb.toString();
	
	}


	public void makeRECall(RERequestHeader response) {


		StringBuffer sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String xml = null;
		String reUrl = null;
		try {
			//url = response.getUrl();
			reUrl = response.getUrl();
			sb = new StringBuffer();
			url = new URL(reUrl.trim());
			response.setUrl(null);
			response.setObj(null);
			response.setKeyWord(null);
			xml = ReResponseParser.getInstanceReqStream().toXML(response);
			logger.info("Calling url "+reUrl+" With XML ["+xml+"]");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(xml);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
			if(sb != null)
				logger.info("Response "+sb.toString()+" From Url ["+url+"]");
		} catch (Exception e) {
			logger.error("Error while calling URL:"+url+", Exception:",e);
		//	throw e;
		} finally {
			url = null;
			
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out = null;

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in = null;
			
			if(connection!=null){
				connection.disconnect();
			}
			
			connection = null;
			
			decodedString = null;
		}
		
	}

	public GenericDTO makeProvisionCall(RERequestHeader response, boolean flowTypeFlag,GenericDTO genericDTO) {


		StringBuffer sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String xml = null;
		String responseXml = null;
		String reUrl = null;
		String msisdn = null;
		
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		BufferedInputStream bis = null;
		BufferedWriter writer = null;
		 
		String responseCode=null;
		String responseDesc=null;
		String uniqueId=null;
		Long loyaltyID=null;
		RevertLoyaltyDAO dao = null;
		RERequestHeader header = null;
		
		RERequestDataSet dataSet = null;
		ArrayList<ReResponseParameter> list = null;
		int packId=0;
		int points =0;
		double totalPoint = 0;
		String channel ="";
		String transactionId="";
		String tierId="";
		String acctTypeId="";
		String isTestNumber="";
		
		try {
			//url = response.getUrl();
			reUrl = response.getUrl();
			sb = new StringBuffer();
			url = new URL(reUrl.trim());
			response.setUrl(null);
			response.setObj(null);
			response.setKeyWord(null);
			response.setStatus(null);
			response.setStatusDesc(null);
			msisdn = response.getMsisdn();
		//	redeemPoints = response.getRewardPoint()!=null?Double.parseDouble(response.getRewardPoint()):0;
			
		//	response.setKeyWord(null);
			xml = ReRequestParser.getInstanceReqStream().toXML(response);
			logger.info("Calling url "+reUrl+" With XML ["+xml+"]");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(xml);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
				logger.info("Response "+sb.toString()+" From Url ["+url+"]");
				
				
			if(flowTypeFlag){
				
				responseXml = sb.toString();
				logger.info("RESPONSE XML FOR SYNC FLOW IS => "+responseXml);
				
			/*	writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
				writer.write("SUCCESS");
				writer.flush();
				writer.close();
				*/
				if(responseXml!=null && !responseXml.equalsIgnoreCase(""))
				{
					header = (RERequestHeader)ReResponseParser.getInstanceReqStream().fromXML(responseXml);
					transactionId = header.getRequestId();
					msisdn = header.getMsisdn();
					responseCode = header.getStatus();
					responseDesc = header.getStatusDesc();
					dataSet = header.getDataSet();
					list = dataSet.getResponseParam();
					
					
					
					for(ReResponseParameter p : list)
					{
						logger.info(p.getId()+"  "+p.getValue());
						if(p.getId().equalsIgnoreCase("UNIQUE_ID"))
							uniqueId = p.getValue();
						
						if(p.getId().equalsIgnoreCase("LOYALTY_ID"))
							loyaltyID = Long.parseLong(p.getValue());
						
						if(p.getId().equalsIgnoreCase("PackageID")||p.getId().equalsIgnoreCase("OfferId"))
							packId=Integer.parseInt(p.getValue());
						
						if(p.getId().equalsIgnoreCase("Points")||p.getId().equalsIgnoreCase("RedeemPoints"))
							points = Integer.parseInt(p.getValue()!=null?p.getValue():"0");
						
						if(p.getId().equalsIgnoreCase("CHANNEL"))
							channel = p.getValue();
						
						if(p.getId().equalsIgnoreCase("TOTAL_POINTS"))
							totalPoint = Double.parseDouble(p.getValue());
						
						if(p.getId().equalsIgnoreCase("TIER_ID"))
							tierId = p.getValue();
						
						if(p.getId().equalsIgnoreCase("ACC_TYPE_ID"))
							acctTypeId = p.getValue();
						
						if(p.getId().equalsIgnoreCase("TEST_NUMBER"))
							isTestNumber = p.getValue();
						
					}
					logger.info("UNIQUE ID REVERT TABLE::"+uniqueId);
					logger.info("LOYALTY ID IN REVERT TABLE::"+loyaltyID);
	 
					if(responseCode!=null && !responseCode.equalsIgnoreCase("") && (responseCode.equalsIgnoreCase("0")||responseCode.equalsIgnoreCase("SC0000")))
					{
						logger.info("RESPONSE CODE SETTING IS::"+responseCode);
						logger.info("RESPONSE DESC SETTING IS::"+responseDesc);
						genericDTO.setStatusCode(responseCode);
						genericDTO.setStatus(responseDesc);
						//Success Case
						dao = new RevertLoyaltyDAO();
						dao.updateStatus(null,uniqueId,loyaltyID, "C",false,0);
						//dao.updateStatusSync(uniqueId, loyaltyID, "C");
						dao.insertRedeem(loyaltyID,msisdn,points,packId,tierId,acctTypeId,channel);
						dao.insertTransaction(channel, loyaltyID, totalPoint, points, packId, msisdn,isTestNumber,transactionId);
//						CDR Writing
						logger.warn(String.format("%s|%s|%s|||%s|||%s|%s|%s|'%s'|%s|%s|%s|%s|%s||||", transactionId,df.format(new Date()),msisdn,loyaltyID,tierId,tierId,"SC0000","SUCCESS",points,packId,acctTypeId,channel,isTestNumber));
						
						//ThreadInitiator.provRespPool.addTask(header);
						
					}
					else
					{
						dao = new RevertLoyaltyDAO();
						dao.revertLoyalty(uniqueId,loyaltyID);
//						CDR Writing
						logger.warn(String.format("%s|%s|%s|||%s|||%s|%s|%s|'%s'|%s|%s|%s|%s|%s||||", transactionId,df.format(new Date()),msisdn,loyaltyID,tierId,tierId,responseCode,"FAILURE",points,packId,acctTypeId,channel,isTestNumber));

						//ThreadInitiator.provRespPool.addTask(header);
					}
				}
			}
				
		}/*catch(ConnectException ce){
			logger.error("Transaction ID"+response.getRequestId()+" Connection Exception , Calling for revert of offer "+ce.getMessage());
			response.setStatus("SC0001");
			response.setStatusDesc("Provision is Down");
			xml = ReResponseParser.getInstanceReqStream().toXML(response);
		//	response.setUrl(Cache.cacheMap.get("IR_STATUS_URL"));
			makeThirdPartyCall(xml, Cache.cacheMap.get("IR_STATUS_URL"), 10000, false);
			
		}catch (FileNotFoundException e) {
			logger.error("Transaction ID"+response.getRequestId()+" FileNotFound Exception , Calling for revert of offer "+e.getMessage());
			response.setStatus("SC0001");
			response.setStatusDesc("Configure Provision Url properly in IN_SERVICE_DETAILS");
			xml = ReResponseParser.getInstanceReqStream().toXML(response);
		//	response.setUrl(Cache.cacheMap.get("IR_STATUS_URL"));
			makeThirdPartyCall(xml, Cache.cacheMap.get("IR_STATUS_URL"), 10000, false);
			
		}*/
		catch (Exception e) {
			logger.error("Error while calling URL:"+url+", Exception:",e);
		} finally {
			url = null;
			
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out = null;

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in = null;
			
			if(connection!=null){
				connection.disconnect();
			}
			
			connection = null;
			response = null;
			decodedString = null;
		}
		
		return genericDTO;
		
	}
	
	public String makeThirdPartyCall1(String xml,String tpUrl,int timeout) throws Exception
	{

		StringBuffer sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;

		try {
			sb = new StringBuffer();
			url = new URL(tpUrl.trim());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(xml);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
		} catch (Exception e) {
			logger.error("Error while calling URL:"+tpUrl+", Exception:",e);
			throw e;
		} finally {
			url = null;
			
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out = null;

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in = null;
			
			if(connection!=null){
				connection.disconnect();
			}
			
			connection = null;
			
			decodedString = null;
		}
		return sb.toString();
	
	}
	
	public String makeThirdPartyCallJsonRequestType(String jsonRequest, String URL, int timeout) {
		StringBuilder sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String response = null;
		try {

			logger.info("Calling url " + URL + " With JSON [" + jsonRequest.toString() + "]");
			sb = new StringBuilder();
			url = new URL(URL.trim());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setRequestProperty("content-type", "application/json");
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(jsonRequest);
			out.flush();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
			response=sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error while calling URL:" + URL + " Exception: " + e);
			// throw e;
		} finally {
			url = null;

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out = null;

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in = null;

			if (connection != null) {
				connection.disconnect();
			}

			connection = null;

			decodedString = null;
		}
		return response;

	}

}
