package com.sixdee.imp.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyExpiryDTO;
import com.sixdee.imp.service.httpcall.dto.LMSSubscriberInfoDTO;
import com.sixdee.imp.service.httpcall.dto.Parameters;

public class GetLMSSubscriberInfoDAO {

	Logger logger=Logger.getLogger(GetLMSSubscriberInfoDAO.class);
	
	public LMSSubscriberInfoDTO getSubscriberInfoDetails(LMSSubscriberInfoDTO subscriberInfoDTO)
	{
		
		Session session=null;
		String sql=null;
		TableInfoDAO infoDAO=null;
		ArrayList<Parameters> list=null;
		try{
			

			if(subscriberInfoDTO.getDataSet().getParams()==null)
			{
				list=new ArrayList<Parameters>();
				subscriberInfoDTO.getDataSet().setParams(list);
			}
			
			if(subscriberInfoDTO.getLanguageID()==null||subscriberInfoDTO.getLanguageID().trim().equals(""))
			{
				subscriberInfoDTO.setLanguageID(Cache.defaultLanguageID);
				
				Parameters languageIDParam=new Parameters();
				languageIDParam.setId("LANG_ID");
				languageIDParam.setValue(subscriberInfoDTO.getLanguageID());
				
				subscriberInfoDTO.getDataSet().getParams().add(languageIDParam);
				
			}
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			sql="SELECT REWARD_POINTS,TIER_ID,LOYALTY_ID from LOYALTY_PROFILE_0 where CONTACT_NUMBER=?";
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,subscriberInfoDTO.getMsisdn());
	
			Object[] objects=(Object[])query.uniqueResult();
			
			if(objects!=null&&objects.length>0)
			{
				Long rewardPoints=((Double)Double.parseDouble(objects[0].toString())).longValue();
				String tierId=objects[1].toString();
				String loyalityId=objects[2].toString();
				logger.info(subscriberInfoDTO.getRequestId()+"  MDN: "+subscriberInfoDTO.getMsisdn()+" Reward Points "+rewardPoints+"  Tier ID "+tierId);
			
				Parameters rewardPointsParam=new Parameters();
				rewardPointsParam.setId(NotificationTokens.points.replace("<","").replace(">",""));
				rewardPointsParam.setValue(rewardPoints+"");
				
				subscriberInfoDTO.getDataSet().getParams().add(rewardPointsParam);
				
				Parameters tierNameParam=new Parameters();
				tierNameParam.setId(NotificationTokens.tierName.replace("<","").replace(">",""));
				tierNameParam.setValue(Cache.tierLanguageInfoMap.get(tierId+"_"+subscriberInfoDTO.getLanguageID())); 
				
				subscriberInfoDTO.getDataSet().getParams().add(tierNameParam);
				subscriberInfoDTO.setLoyalityId(loyalityId);
				subscriberInfoDTO.setStatus("SC0000");
				subscriberInfoDTO.setStatusDesc("SUCCESS");
			}
			
			
			
		}catch (Exception e) {
			logger.info(subscriberInfoDTO.getRequestId()+" : ",e);
		}finally{
			if(session!=null)
				session.close();
			session=null;
			sql=null;
			infoDAO=null;
			list=null;
		}
		return subscriberInfoDTO;
		

		
	}//getSubscriberInfoDetails

	public String getRewardExpiryDetails(Calendar[] allCal,
			String index,String loyaltyId) {
		Session session = null;
		Query query = null;
		List<BigDecimal> list = null;
		
		Map<Long,LoyaltyExpiryDTO> expiryMap = new HashMap<Long,LoyaltyExpiryDTO>();
		
		LoyaltyExpiryDTO dto = null;
		String expiryPoints = null;
		try
		{
			
			
			logger.info("From=>"+allCal[0].getTime()+" to"+allCal[1].getTime());
			
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT SUM(REWARD_POINTS) FROM LOYALTY_TRAN_SUMMARISED_1"+index +
					" WHERE CREATE_DATE BETWEEN ? AND ? AND REWARD_POINTS>0 AND LOYALTY_ID = ? ");
			
			query.setParameter(0, allCal[0].getTime());// From Date
			query.setParameter(1, allCal[1].getTime());// To Date
			query.setParameter(2, loyaltyId);
			list = query.list();
			

			for(BigDecimal all : list)
			{
				expiryPoints = (all.toString());
				

			}
			
		}
		catch (Exception e) 
		{
			logger.info("LoyaltyId "+loyaltyId+" Exception while getting details =>",e);
			
		}
		finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
		}
		
		return expiryPoints;
		}

	public String getRewardExpiryDetails1(
			String partitionIndex,String loyaltyId) {

		  Session session = null;
		  Query query = null;
		  List<BigDecimal> list = null;
		  
		  Map<Long,LoyaltyExpiryDTO> expiryMap = new HashMap<Long,LoyaltyExpiryDTO>();
		  
		  LoyaltyExpiryDTO dto = null;
		  String expiryPoints = null;
		  try
		  {
		   
		   
		 //  logger.info("From=>"+allCal[0].getTime()+" to"+allCal[1].getTime());
		   
		   session = HiberanteUtil.getSessionFactory().openSession();
		   query = session.createSQLQuery("SELECT SUM(REWARD_POINTS) FROM  LOYALTY_TRAN_SUMMARY_MONTHLY "+
		     " WHERE PARTITION_INDEX in ("+Cache.getCacheMap().get("NOTIFICATION_PARTITION_INDEX")+") AND REWARD_POINTS>0 AND LOYALTY_ID = ? ");
		   
		   //query.setParameter(0,partitionIndex);// From Date
		   //query.setParameter(1, allCal[1].getTime());// To Date
		   query.setParameter(0, loyaltyId);
		   list = query.list();
		   

		   for(BigDecimal all : list)
		   {
			   if(all!=null)
				   expiryPoints = (all.toString());
		    

		   }
		   
		  }
		  catch (Exception e) 
		  {
		   logger.info("LoyaltyId "+loyaltyId+" Exception while getting details =>",e);
		   
		  }
		  finally
		  {
		   try
		   {
		    if(session!=null)
		     session.close();
		   }
		   catch (Exception e) {
			   logger.error(e);
		   }
		  }
		  
		  return expiryPoints;
		  }
	
}
