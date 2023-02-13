/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

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
 * <td>This class is created with the intention to generalize <br> methods like getting loyalty number into <br> one single class</td>
 * </tr>
 * </table>
 * </p>
 */
public class BasicInformationDAO {

	private static final Logger logger = Logger.getLogger(BasicInformationDAO.class);
	/**
	 * GetLoyaltyId is a function which will return the loyaltyId based on the subscriber number 
	 *
	 * @param String for which loyalty Id has to be find
	 * @return LoyaltyId of the subscriber
	 */
	public String getLoyaltyId(String tableName,String subscriberNumber) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		String loyaltyID = null;
		Query query = null;
		try{
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createSQLQuery("Select LOYALTY_ID from "+tableName+" where SUBSCRIBER_NUMBER=?");
			query.setParameter(0 , subscriberNumber);
			for(Object lID : ((List<Object>)query.list()))
				loyaltyID = lID+"";
			transaction.commit();
			long t2 = System.currentTimeMillis();
			if(logger.isDebugEnabled())
				logger.debug("Loyalty Id for ["+subscriberNumber+"] from Table ["+tableName+"] is ["+loyaltyID+"] @ ["+(t2-t1)+"]");
			}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(transaction != null)
					transaction.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
			return loyaltyID;
	}
	
	public SubscriberNumberTabDTO getLoyaltyDetails(String tableName,long subscriber){
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		List<SubscriberNumberTabDTO> subscriberNumberList = null;
		SubscriberNumberTabDTO subsNumDTO = null;
		boolean rollback = false;
		try{
			logger.info("Transaction ID :   Subscriber : "+subscriber+" Identifing Loyalty Details ");
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.or(Restrictions.eq("subscriberNumber", subscriber),Restrictions.eq("accountNumber", subscriber)));
			criteria.add(Restrictions.eq("statusID", 1));
			subscriberNumberList = criteria.list();
			for(SubscriberNumberTabDTO subscriberNumberTabDTO : subscriberNumberList){
				subsNumDTO = subscriberNumberTabDTO;
			}
			transaction.commit();
		}catch (HibernateException e) {
			logger.error("Exception occured ",e);
			rollback = true;
		}catch (ClassCastException e) {
			logger.error("Class cast exception occured . If EmptylistCast exception no need to worry,probably no data in table ["+tableName+"] for subscriber number ["+subscriber+"]");
			rollback = true;
		}finally{
			if(rollback){
				if(transaction != null){
					transaction.rollback();
					transaction = null;
				}
			}
			if(session != null){
				session.close();
				session = null;
			}
			criteria = null;
			transaction = null;
			subscriberNumberList = null;
		}
		return subsNumDTO;
	}

	public String getAdslLoyaltyId(String tableName, String subscriberNumber) {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		String loyaltyID = null;
		Query query = null;
		try{
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createSQLQuery("Select LOYALTY_ID from "+tableName+" where ADSL_NO=?");
			query.setParameter(0 , subscriberNumber);
			for(Object lID : ((List<Object>)query.list()))
				loyaltyID = lID+"";
			transaction.commit();
			long t2 = System.currentTimeMillis();
			if(logger.isDebugEnabled())
				logger.debug("Loyalty Id for ["+subscriberNumber+"] from Table ["+tableName+"] is ["+loyaltyID+"] @ ["+(t2-t1)+"]");
			}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(transaction != null)
					transaction.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
			return loyaltyID;
	
	}
	
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
	
/*	public ArrayList<SubscriberNumberTabDTO> getSubscriberInformation(String transactionId,
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
	
*/	
	public ArrayList<AccountNumberTabDTO> getSubscriberAccountInformation(String transactionId,
			String accountTableName,long accountNumber) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<AccountNumberTabDTO> accountList = null;
		
		try{
			
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(accountTableName);
			criteria.add(Restrictions.eq("accountNumber",accountNumber));
			accountList = (ArrayList<AccountNumberTabDTO>) criteria.list();
			transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" Account Number : "+accountNumber+" Exception occured When Getting Details from subscriber Number table for ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
			criteria = null;
			//accountNumber = null;
			transactionId = null;
		}
		return accountList;
	}

	public void saveOrUpdateInstance(String transactionId,
			Session modifySession, Transaction modifyTransaction, String accountTableName, AccountNumberTabDTO accountNumberTabDTO,int opCode) throws CommonException {
		try{
			logger.debug("Started Updating "+accountTableName+" "+accountNumberTabDTO.getAccountNumber());
			
			modifySession = modifySession!=null?modifySession:HiberanteUtil.getSessionFactory().openSession();
			modifyTransaction = modifyTransaction!=null?modifyTransaction:modifySession.beginTransaction();
			if(opCode==1)
				modifySession.save( accountTableName,accountNumberTabDTO);
			else
				modifySession.update(accountTableName,accountNumberTabDTO);
			logger.info("Finished Updating "+accountNumberTabDTO.getAccountNumber());
		}catch(Exception e){

			logger.error("TransactionId : "+transactionId+"  Exception occured When Upadting Account information ",e);
			throw new CommonException(e.getMessage());
		}finally{
			accountNumberTabDTO = null;
			accountTableName = null;
		}
		
	}

}
