package com.sixdee.imp.dao;

/**
 * 
 * @author Rahul K K
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
 * <td>January 21,2014 12:55:09 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class AccountConversionDAO {

	private static final Logger logger = Logger.getLogger(AccountConversionDAO.class);
	public ArrayList<SubscriberNumberTabDTO> getSubscriberInformation(String transactionId,
			String subscriberTableName, String subscriberNumber) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		
		try{
			
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(subscriberTableName);
			criteria.add(Restrictions.eq("subscriberNumber", Long.parseLong(subscriberNumber)));
			subscriberNumberList = (ArrayList<SubscriberNumberTabDTO>) criteria.list();
			transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" Subscriber Number : "+subscriberNumber+" Exception occured When Getting Details from subscriber Number table for ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
			criteria = null;
			subscriberNumber = null;
			transactionId = null;
		}
		return subscriberNumberList;
	}
	
	
	public ArrayList<LoyaltyRegisteredNumberTabDTO> getLoyaltyLinkedInfo(
			String transactionId, long loyaltyId, String subscriberNumber, String loyaltyLinkedTableName) {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyLinkNumberList = null;
		
		try{
			logger.info("Transaction ID "+transactionId+" Action : Getting Loyalty Registered Information for LoyaltyId "+loyaltyId+" Subscriber Number "+subscriberNumber);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(loyaltyLinkedTableName);
			criteria.add(Restrictions.and(Restrictions.eq("linkedNumber", subscriberNumber),Restrictions.eq("loyaltyID", loyaltyId)));
			
			loyaltyLinkNumberList = (ArrayList<LoyaltyRegisteredNumberTabDTO>) criteria.list();
			transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" Subscriber Number : "+subscriberNumber+" Exception occured When Getting Details from subscriber Number table for ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
			criteria = null;
			subscriberNumber = null;
			transactionId = null;
		}
		return loyaltyLinkNumberList;
	
	}

	public ArrayList<LoyaltyRegisteredNumberTabDTO> getLoyaltyLinkedInfo(
			String transactionId, long loyaltyId, String loyaltyLinkedTableName) {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyLinkNumberList = null;
		
		try{
			logger.info("Transaction ID "+transactionId+" LoyaltyId : "+loyaltyId+" Action : Getting Loyalty Registered Information ");
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(loyaltyLinkedTableName);
			criteria.add(Restrictions.eq("loyaltyID", loyaltyId));
			
			loyaltyLinkNumberList = (ArrayList<LoyaltyRegisteredNumberTabDTO>) criteria.list();
			transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" LoyaltyId : "+loyaltyId+" Exception occured When Getting Details from subscriber Number table for ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
			criteria = null;

			transactionId = null;
		}
		return loyaltyLinkNumberList;
	
	}


	public boolean updateSubscriberTypeInformation(String transactionId,
			String subscriberTableName, Session modifySession, Transaction modifyTransaction, SubscriberNumberTabDTO subscriberInfoDTO) throws CommonException {


		//Session session = null;
		//Transaction transaction = null;
		boolean isUpdated = true;
		try{

			modifySession = modifySession!=null?modifySession:HiberanteUtil.getSessionFactory().openSession();
			modifyTransaction = modifyTransaction!=null?modifyTransaction:modifySession.beginTransaction();
			modifySession.update(subscriberTableName,subscriberInfoDTO);
			//transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" Subscriber Number : "+subscriberInfoDTO.getSubscriberNumber()+" Exception occured When Updating Type in subscriber Number table for ",e);
			
			isUpdated = false;
			throw new CommonException("DataBase Updation Failure");
		}
		finally{
			subscriberTableName=null;
			subscriberInfoDTO = null;
		}
		return isUpdated;
	
	
	}
	
	
	public boolean updateLoyaltyLinkedInformation(String transactionId,
			String loyaltyLinkedTable, Session modifySession, Transaction modifyTransaction, LoyaltyRegisteredNumberTabDTO loyaltyLinkedInfo) throws CommonException {


		boolean isUpdated = true;
		try{
			
			modifySession = modifySession!=null?modifySession:HiberanteUtil.getSessionFactory().openSession();
			modifyTransaction = modifyTransaction!=null?modifyTransaction:modifySession.beginTransaction();
			modifySession.update(loyaltyLinkedTable,loyaltyLinkedInfo);
			//transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" Subscriber Number : "+loyaltyLinkedInfo.getLinkedNumber()+" Exception occured When Updating Type in subscriber Number table for ",e);
			
			isUpdated = false;
			throw new CommonException("DataBase Updation Failure");
			
		}
		finally{
			loyaltyLinkedInfo = null;
			loyaltyLinkedTable = null;
		}
		return isUpdated;
	
	
	}


}
