package com.sixdee.imp.common.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.lms.bo.OnlineRuleInitiatorBO;


public class CommonUtil {
	
	private static final Logger logger = Logger.getLogger(CommonUtil.class);

	public boolean isSoftDeletePeriodOver(Date date)
	{
		
		Calendar todayCal=Calendar.getInstance();
		
		Calendar statusCal=Calendar.getInstance();
		statusCal.setTime(date);

		long noOfDays=(todayCal.getTimeInMillis()-statusCal.getTimeInMillis())/(1000*60*60*24);

		long softDelete=Long.parseLong(Cache.getConfigParameterMap().get("SOFT_DELETE_PERIOD").getParameterValue());
		
		if(noOfDays>softDelete) 
			return true;
		
		return false;
		
	}//isSoftDeletePeriodOver
	
	public boolean isItChar(String number)
	{
		//Pattern pattern=Pattern.compile("[a-zA-Z]{1,}");
		Pattern pattern=Pattern.compile("[^0-9]");
		Matcher matcher= pattern.matcher(number);
		if(matcher.find())
			return true;
		
		return false;
		
	}// isItChar
	
	public String getTransaction()
	{
		return System.currentTimeMillis()+"";
	}
	
	
	public String getStatusDescription(String message,String[] tokens,String[] values)
	{
		
		if(tokens!=null&&values!=null)
		{
			for(int i=0;i<tokens.length;i++)
			{
				try{
					message=message.replaceAll(tokens[i],values[i]);
				}catch(Exception e){
					logger.error("Message "+message+" Token not found "+tokens[i]+" Value "+values[i]);
				}
			}
		}
		return message;
		
		
	}//getStatusDescription
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> callProcedure(String callMethod, String loyaltyId, String points, int type){
		logger.info("**************  callProcedure  *******************  " + callMethod);

		Connection conn = null;
		CallableStatement cstmt = null;
		boolean execute = false;
		ResultSet resultSet = null;
		ArrayList<String> result = null;
		Session session = null;
		Transaction txn = null;
		long startTime=System.currentTimeMillis();
		try {
	
			session = HiberanteUtil.getSessionFactory().openSession();
			//conn = session.connection();
			txn = session.beginTransaction();
			cstmt = conn.prepareCall(callMethod);

		   cstmt.setString(1, loyaltyId);
		   cstmt.setString(2, points);
		   cstmt.setInt(3, type);
		   cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
		   cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
		   logger.info(">>>>>>>>>> loyaltyId  >>>>>>"+loyaltyId);
		   logger.info(">>>>>>> points>>>>>>>>>"+points);
		   logger.info(">>>>>>>>> type >>>>>>>>>"+type);
		   logger.info(">>>>>>>>>>>"+cstmt);
		   try{
		    cstmt.executeUpdate();
			int deductedPoints = cstmt.getInt(4);
			logger.info("Deducted points "+deductedPoints);
		    
		    if(deductedPoints >0){
		    	int tp = cstmt.getInt(4);
		    	int bp = cstmt.getInt(5);
		    	 logger.info(">>>>resultSet>>>>"+tp+">>>"+bp);
		    	 result = new ArrayList<String>();
				   result.add(tp+"");
				   result.add(bp+"");
		    }
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   
		   txn.commit();
	     }catch(Exception e){
	    	 e.printStackTrace();
	    	 
			} finally {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					} finally {
						resultSet = null;
					}
				}
				if (cstmt != null) {
					try {
						cstmt.close();
						cstmt = null;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
					} finally {
						cstmt = null;
					}
				}

				if (session != null) {
					session.close();
					session = null;
					txn = null;

				}
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						conn = null;
					}
				}
			logger.info("Total Time taken callProcedure "+(System.currentTimeMillis()-startTime));
			}
	return result;
	}
	
	public void generateNotifyRequest(String transactionId, String Keyword,String msisdn,
			HashMap<String, String> map) {
		GenericDTO genericDTO2 = new GenericDTO();
		Request request = new Request();
		request.setRequestId(transactionId);
		request.setMsisdn(msisdn);
		request.setKeyWord(Keyword);
		DataSet dataSet = new DataSet();
		ArrayList<Param> paramList = new ArrayList<Param>();
		
		if(map!=null){
			Set<String> s = map.keySet();
			for(String key:s){
				logger.info(">>key >>"+key);
				Param p = new Param();
				p.setId(key);
				p.setValue(map.get(key));
				paramList.add(p);
			  	
			}
		}
		
		dataSet.setParameter1(paramList);
		request.setDataSet(dataSet);
		
		genericDTO2.setObj(request);
		
		OnlineRuleInitiatorBO onlineRuleInitiatorBO = new OnlineRuleInitiatorBO();
		
		onlineRuleInitiatorBO.executeBusinessProcess(genericDTO2);
	}
	
	public HashMap<String, String> convertingDataToMap(Data dataTags[])
	{
		HashMap<String, String> buildingMap = null;
		try {
			buildingMap = new HashMap<String, String>();
			Data data[] = dataTags;
			if (data != null && data.length > 0) {
				for (int i = 0; i < data.length; i++) {
					if (data[i].getName() != null && data[i].getValue() != null)
						buildingMap.put(data[i].getName(), data[i].getValue());
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return buildingMap;
	}
	
	public GenericDTO getStatusCodeDescription(GenericDTO genericDTO,String key,String txnId){
		ServiceStatusDTO actionServiceDetailsDTO = null;
		if((actionServiceDetailsDTO = Cache.getServiceStatusMap().get(key))!=null){
			genericDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
			genericDTO.setStatus(actionServiceDetailsDTO.getStatusDesc());
		}else
		{
			logger.warn(" Service : CreateLoyalty -- Transaction ID : "+txnId+""+ " No Key Defined in Service Status table for Key :- "+key);
			genericDTO.setStatusCode("SC0002");
			genericDTO.setStatus(key);	
		}
		return genericDTO;
	}
	
	public String discardCountryCodeIfExists(String moNumber) {
		logger.info(">>msisdn in request >>"+moNumber);
		
		String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
		Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
		Integer countryCodeLength = Integer.parseInt(Cache.getConfigParameterMap().get("COUNTRY_CODE_LENGTH").getParameterValue());
		logger.debug(">>>total length >>"+(subscriberSize+countryCodeLength));
		logger.debug(">>country code>>subscriber num size >>>country code length >>"+subscriberCountryCode+"  /"+subscriberSize+"  /"+countryCodeLength);
		if(moNumber!=null && moNumber.length()==(subscriberSize+countryCodeLength)){
			if(moNumber.startsWith(subscriberCountryCode))
				moNumber = moNumber.replaceFirst(subscriberCountryCode, "");
		}
		logger.info(">>msisdn returned after validating >>"+moNumber);
		return moNumber;
	}
}
