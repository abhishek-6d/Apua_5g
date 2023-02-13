package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class CommonUtilDAO {
	private Logger logger = Logger.getLogger(ServiceManagementDAO.class);
	
	public ArrayList<SubscriberNumberTabDTO> getSubscriberInformation(String transactionId,
			String subscriberTableName, String subscriberNumber) {
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(subscriberTableName);
			criteria.add(Restrictions.eq("subscriberNumber", Long.parseLong(subscriberNumber)));
			subscriberNumberList = (ArrayList<SubscriberNumberTabDTO>) criteria.list();
			//transaction.commit();
		}catch(Exception e){
			logger.error("TransactionId : "+transactionId+" Subscriber Number : "+subscriberNumber+" Exception occured When Getting Details from subscriber Number table for ",e);
			/*
			 * if(transaction != null){ transaction.rollback(); }
			 */
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
	
	public List<LoyaltyProfileTabDTO> getLoyaltyProfileInfo(String txnId, String tableName, Long loyaltyId)
			throws CommonException {
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyProfileTabDTO> loyaltyProfileDetails = null;
		try {
			logger.info("Transaction ID : " + txnId + " LoyaltyId : " + loyaltyId
					+ " Getting loyalty linked numbers from " + tableName);
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName).add(Restrictions.eq("loyaltyID",loyaltyId));
			loyaltyProfileDetails = (ArrayList<LoyaltyProfileTabDTO>) criteria.list();
			//transaction.commit();
		} catch (HibernateException e) {
			logger.error("Transaction ID : " + txnId + " LoyaltyId : " + loyaltyId
					+ " Account Number :  Hibernate Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			throw new CommonException(e.getMessage());

		} catch (Exception e) {
			logger.error("Transaction ID : " + txnId + " LoyaltyId : " + loyaltyId
					+ " Account Number :   Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			throw new CommonException(e.getMessage());

		} finally {

			if (session != null) {
				session.close();
			}
		}
		return loyaltyProfileDetails;
	}

}
