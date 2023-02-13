package com.sixdee.imp.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.EbillNotificationDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class EbillNotificationCheckDAO {
	Logger logger = Logger.getLogger(EbillNotificationCheckDAO.class);

	public void getEbillSubscriber(EbillNotificationDTO ebillNotificationDTO) { 		    
		    
    	    TableDetailsDAO  tableDetailsDAO=new TableDetailsDAO();
		    LoyaltyProfileTabDTO loyaltyProfileTabDTO=null;
		    
			SubscriberNumberTabDTO subscriberNumberTabDTO=tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(ebillNotificationDTO.getSubscriberNumber()));
			
			logger.info("MSISDN:"+ebillNotificationDTO.getSubscriberNumber()+"Subcriber DTO:"+subscriberNumberTabDTO);
			if(subscriberNumberTabDTO!=null)
			{   
				ebillNotificationDTO.setLoyaltyID(subscriberNumberTabDTO.getLoyaltyID());	
			}
			
			if(ebillNotificationDTO.getLoyaltyID()==null)
			{
				AccountNumberTabDTO accountNumberTabDTO=tableDetailsDAO.getAccountNumberDetails(ebillNotificationDTO.getSubscriberNumber());
				
				logger.info("MSISDN:"+ebillNotificationDTO.getSubscriberNumber()+ "Account Number DTO:"+accountNumberTabDTO);
				if(accountNumberTabDTO!=null)
				{
					ebillNotificationDTO.setLoyaltyID(accountNumberTabDTO.getLoyaltyID());
					
				}
			}
			
			if(ebillNotificationDTO.getLoyaltyID()==null)
			{
				loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfile(Long.parseLong(ebillNotificationDTO.getSubscriberNumber()));
				
				logger.info("MSISDN:"+ebillNotificationDTO.getSubscriberNumber()+ "LoyaltyProfile DTO:"+loyaltyProfileTabDTO);
				if(loyaltyProfileTabDTO!=null)
				{
					ebillNotificationDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
				}
			}

			if(ebillNotificationDTO.getLoyaltyID()==null)
			{
			
				ebillNotificationDTO.setNotSubscriber(true);
			
			}
			
			logger.info("MSISDN:"+ebillNotificationDTO.getSubscriberNumber()+ "IS SUBSCRIBER NO:"+ebillNotificationDTO.isNotSubscriber());
			
		}
public void  getEbillAccount(EbillNotificationDTO ebillNotificationDTO) {
		
		
		
		TableDetailsDAO  tableDetailsDAO=new TableDetailsDAO();
	    LoyaltyProfileTabDTO loyaltyProfileTabDTO=null;
		SubscriberNumberTabDTO subscriberNumberTabDTO=tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(ebillNotificationDTO.getAccountNumber()));
		logger.info("MSISDN:"+ebillNotificationDTO.getAccountNumber()+"Subcriber DTO:"+subscriberNumberTabDTO);
		if(subscriberNumberTabDTO!=null)
		{
			ebillNotificationDTO.setLoyaltyID(subscriberNumberTabDTO.getLoyaltyID());	
		}
		
		if(ebillNotificationDTO.getLoyaltyID()==null)
		{
			AccountNumberTabDTO accountNumberTabDTO=tableDetailsDAO.getAccountNumberDetails(ebillNotificationDTO.getAccountNumber());
			logger.info("MSISDN:"+ebillNotificationDTO.getAccountNumber()+"Account Number DTO:"+accountNumberTabDTO);
			if(accountNumberTabDTO!=null)
			{
				ebillNotificationDTO.setLoyaltyID(accountNumberTabDTO.getLoyaltyID());
				
			}
		}
		
		if(ebillNotificationDTO.getLoyaltyID()==null)
		{
			loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfile(Long.parseLong(ebillNotificationDTO.getAccountNumber()));
			logger.info("MSISDN:"+ebillNotificationDTO.getAccountNumber()+"Account Number DTO:"+loyaltyProfileTabDTO);
			if(loyaltyProfileTabDTO!=null)
			{
				ebillNotificationDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
			}
		}

		if(ebillNotificationDTO.getLoyaltyID()==null)
		{
		   ebillNotificationDTO.setNotAccount(true);
     	}
		
		logger.info("MSISDN:"+ebillNotificationDTO.getAccountNumber()+"IS ACCOUNT NUMBER:"+ebillNotificationDTO.isNotAccount());
	}

	

		
	public boolean checkIfLIDExist(Long loyaltyID) {
		
		boolean yesLIDexist= false;
		Session session=null;
		Transaction transaction=null;
		
		try{
		     
		    session = HiberanteUtil.getSessionFactory().openSession();;
		    transaction=session.beginTransaction();
			String sql="SELECT COUNT(1) FROM EBILL_LOYALTY_DETAILS WHERE LOYALTY_ID=? and STATUS=?" ;
			
			Query query=session.createSQLQuery(sql);
			
			query.setParameter(0,loyaltyID);
			query.setParameter(1,"C");
			
			List<Object[]> list=query.list();
			
			if(list!=null&&list.size()>0)
			{
				if(Integer.parseInt(list.get(0)+"")>0)
					yesLIDexist=true;
			}
			logger.info("LID exist:"+yesLIDexist);
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
      	return yesLIDexist;
	}
	
	public int countofEbillNotficationEntry(Long loyaltyID) {

		int count = 0;
		Session session=null;
		Transaction transaction=null;
		try{
		
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			String sql="SELECT COUNT(LOYALTY_ID) from EBILL_LOYALTY_DETAILS where STATUS=?" ;
		    Query query=session.createSQLQuery(sql);
		    query.setParameter(0, "C");
			List<Object[]> list=query.list();
			if(list!=null && list.size()>0)
				count = Integer.parseInt(list.get(0)+"");
			logger.info("the total no of rows in table EBILL_LOYALTY_DETAILS is "+count);
			transaction.commit();
			}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		return count;
	}

}
