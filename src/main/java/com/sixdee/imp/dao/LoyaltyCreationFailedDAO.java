package com.sixdee.imp.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.CreateAccountDTO;
import com.sixdee.imp.dto.LoyaltyCreationFailedDTO;

public class LoyaltyCreationFailedDAO {

	Logger logger=Logger.getLogger(LoyaltyCreationFailedDAO.class);
	
	public void insertLoyaltyCreation(GenericDTO genericDTO,List<String> registerNumbers)
	{
	
		
		Session session=null;
		Transaction transaction=null;
		CreateAccountDTO createAccountDTO=(CreateAccountDTO)genericDTO.getObj();
		
		try{
			logger.info("Failed Subscriber Number "+registerNumbers+" Transaction ID "+createAccountDTO.getTransactionId());
			
			if(registerNumbers==null||registerNumbers.size()<=0)
				return;
			
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String sql="SELECT SUBSCRIBER_NUMBER from LOYALTY_CREATION_FAILED WHERE SUBSCRIBER_NUMBER in(:subNumberList) ";
			
			Query query=session.createSQLQuery(sql).setParameterList("subNumberList", registerNumbers);
			
			List<String> existSubscriberNumber=query.list();
			
			if(existSubscriberNumber!=null&&existSubscriberNumber.size()>0)
			{
				sql="UPDATE LOYALTY_CREATION_FAILED SET COUNT=COUNT+1,STATUS='P' where SUBSCRIBER_NUMBER in(:subNumberList)";
				query=session.createSQLQuery(sql).setParameterList("subNumberList", existSubscriberNumber);
				query.executeUpdate();
				
				registerNumbers.removeAll(existSubscriberNumber);
				
			}
			
			
			if(registerNumbers!=null&&registerNumbers.size()>0)
			{
				
				sql="INSERT INTO LOYALTY_CREATION_FAILED (TRANSACTION_ID,SUBSCRIBER_NUMBER,CHANNEL,STATUS_ID,COUNT) values(?,?,?,?,?)";
				
				query=session.createSQLQuery(sql);
				
				for(String subscriberNumber:registerNumbers)
				{
					query.setParameter(0,createAccountDTO.getTransactionId());
					query.setParameter(1,subscriberNumber);
					query.setParameter(2,Cache.channelDetails.get(createAccountDTO.getChannel()));
					query.setBigInteger(3,createAccountDTO.getStatusID()==null?null:new BigInteger(createAccountDTO.getStatusID()+""));
					query.setParameter(4,0);
					query.executeUpdate();
				}
			
			}
			
			transaction.commit();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
			if(transaction!=null)
			  transaction.rollback();
			
		}finally{
			if(session!=null)
				session.close();
			
			session=null;
			transaction=null;
		}
		
	}//getLoyaltyCreationFailed
	
	
	
	public void updateLoyaltyCreationFailed(List<LoyaltyCreationFailedDTO> list)
	{
	
		
		Session session=null;
		Transaction transaction=null;
		
		try{
			
			if(list==null||list.size()<=0)
				return;
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String sql="UPDATE LOYALTY_CREATION_FAILED SET STATUS=? where TRANSACTION_ID in (:tranIds) ";
			Query query=session.createSQLQuery(sql);
			
			List<String> transactionIdList=new ArrayList<String>();
			for(LoyaltyCreationFailedDTO creationFailedDTO:list)
			{
				transactionIdList.add(creationFailedDTO.getTransactionID());
			}
			
			
			query.setParameter(0,"C");
			query.setParameterList("tranIds",(Collection<String>)transactionIdList);
			
			query.executeUpdate();
			
			transaction.commit();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
			if(transaction!=null)
			  transaction.rollback();
			
		}finally{
			if(session!=null)
				session.close();
			
			session=null;
			transaction=null;
		}
		
	}//updateLoyaltyCreationFailed
	
	
	
	public List<LoyaltyCreationFailedDTO> getLoyaltyCreationFailed()
	{
	
		
		Session session=null;
		Transaction transaction=null;
		
		List<LoyaltyCreationFailedDTO> list=null;
		
		try{
			
			list=new ArrayList<LoyaltyCreationFailedDTO>();
			
			Map<String,LoyaltyCreationFailedDTO> map=new HashMap<String,LoyaltyCreationFailedDTO>();
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String sql="SELECT TRANSACTION_ID,SUBSCRIBER_NUMBER,CHANNEL,STATUS_ID FROM LOYALTY_CREATION_FAILED WHERE STATUS=? AND COUNT<=2 ";
			
			Query query=session.createSQLQuery(sql).setParameter(0,"P");
			
			List<Object[]> list2=query.list();
			
			if(list2!=null)
			{
				LoyaltyCreationFailedDTO creationFailedDTO=null;
				for(Object[] obj:list2)
				{
					
					
					if(map.containsKey(obj[0].toString()))
					{
						map.get(obj[0].toString()).getSubscriberNumber().add(obj[1].toString());
						
					}else{
						creationFailedDTO=new LoyaltyCreationFailedDTO();
						
						List<String> subList=new ArrayList<String>();
						subList.add(obj[1].toString());
						creationFailedDTO.setTransactionID(obj[0].toString());
						creationFailedDTO.setSubscriberNumber(subList);
						creationFailedDTO.setChannel(obj[2].toString());
						creationFailedDTO.setStatus(((obj[3]==null||obj[3].toString().trim().equals(""))?null:Integer.parseInt(obj[3].toString())));
						
						map.put(obj[0].toString(),creationFailedDTO);
					}
					
					
				}
			}
			
			logger.info(map);
			
			if(map!=null)
			{
				
				for(String transactionID:map.keySet())
				{
					list.add(map.get(transactionID));
				}
			}
			 
			
			
			transaction.commit();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
			if(transaction!=null)
			  transaction.rollback();
			
		}finally{
			if(session!=null)
				session.close();
			
			session=null;
			transaction=null;
		}
		
		return list;
		
	}//getLoyaltyCreationFailed
	
}
