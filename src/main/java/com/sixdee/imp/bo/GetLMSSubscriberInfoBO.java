package com.sixdee.imp.bo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.GetLMSSubscriberInfoDAO;
import com.sixdee.imp.dao.LoyaltyExpiryDAO;
import com.sixdee.imp.dto.LoyaltyExpiryDTO;

import com.sixdee.imp.service.httpcall.dto.LMSSubscriberInfoDTO;
import com.sixdee.imp.service.httpcall.dto.Parameters;

public class GetLMSSubscriberInfoBO {
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final Logger logger = Logger.getLogger(GetLMSSubscriberInfoBO.class);
	public void getSubscriberDetails(LMSSubscriberInfoDTO subscriberInfoDTO)
	{
		Calendar cal = null;
		String partitionIndex=null;
		String loyaltyId = null;
		GetLMSSubscriberInfoDAO getLMSSubscriberInfoDAO = null;
		SimpleDateFormat sdf=null;
		try {
			getLMSSubscriberInfoDAO = new GetLMSSubscriberInfoDAO();
			subscriberInfoDTO = getLMSSubscriberInfoDAO
					.getSubscriberInfoDetails(subscriberInfoDTO);
			new LoyaltyExpiryDAO();			
			cal = Calendar.getInstance();
			String months1 = Cache.getConfigParameterMap()
					.get("REWARD_EXPIRY_NOTIFICATION_PERIOD").getParameterValue()
					.trim();
			int months=Integer.parseInt(months1);
			cal.add(Calendar.MONTH, -months);
			sdf=new SimpleDateFormat("yyyyMM");
			loyaltyId = subscriberInfoDTO.getLoyalityId();
			partitionIndex=sdf.format(cal.getTime());
			System.out.println("partationIndex:::::::"+partitionIndex);
			String expiryPoints = getLMSSubscriberInfoDAO
					.getRewardExpiryDetails1(partitionIndex, loyaltyId);			
			Parameters loyalityIdParam = new Parameters();
			loyalityIdParam.setId("EXPIRYREWARDPOINTS");
			if (expiryPoints == null) {
				expiryPoints = "0";
			}
			loyalityIdParam.setValue(expiryPoints);
			subscriberInfoDTO.getDataSet().getParams().add(loyalityIdParam);
			StringBuilder sb=new StringBuilder();
			sb.append(subscriberInfoDTO.getRequestId()+"|"+df.format(new java.util.Date())+"|"+subscriberInfoDTO.getMsisdn()+"|"+subscriberInfoDTO.getStatus()+"|"+subscriberInfoDTO.getStatusDesc());
			for(Parameters p:subscriberInfoDTO.getDataSet().getParams())
			{
				
				if(p.getId().equalsIgnoreCase("Points"))
				{
					sb.append("|");
					sb.append(p.getValue());
				}
				if(p.getId().equalsIgnoreCase("TierName"))
				{
					sb.append("|");
					sb.append(p.getValue());
				}
				if(p.getId().equalsIgnoreCase("EXPIRYREWARDPOINTS"))
				{
					sb.append("|");
					sb.append(p.getValue());
				}
				
				
			}
			/*logger.fatal(String						 				 
					.format("%s|%s|%s||||||||%s|'%s'|%s||||||||||||",
							subscriberInfoDTO.getRequestId(),
							df.format(new java.util.Date()),
							subscriberInfoDTO.getMsisdn(),							
							subscriberInfoDTO.getStatus(),
							subscriberInfoDTO.getStatusDesc(),
							expiryPoints			
							
							));*/
			logger.fatal(sb.toString());
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}
		
	}//getSubscriberDetails

	/*private Calendar[] getCalendarDates() {
		Calendar cal[] = new Calendar[2];
		
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.HOUR_OF_DAY, 23);
		toDate.set(Calendar.MINUTE, 59);
		toDate.set(Calendar.SECOND, 59);
		fromDate.set(Calendar.DATE, 28);
		fromDate.set(Calendar.MONTH, 1);
		fromDate.set(Calendar.YEAR, 2014);
		toDate.set(Calendar.DATE,31 );
		toDate.set(Calendar.MONTH, 2);
		toDate.set(Calendar.YEAR, 2014);		
		cal[0] = fromDate;
		cal[1] = toDate;
		return cal;
	}*/
	public static void main(String[] args) {
		String msisdn = "9914020815";
		System.err.println(msisdn.substring(8, 9));
		
		
	}
	
}
