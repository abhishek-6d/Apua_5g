/**
 * 
 */
package com.sixdee.lms.dao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

/**
 * @author rahul.kr
 *
 */
public class LoyaltyProfileManagerDAO {
	
	private static final Logger logger = Logger.getLogger("LoyaltyProfileManagerDAO");

	public void persistLoyaltyRegisteredNumber(Session session, Transaction transaction, 
			String tableName,LoyaltyRegisteredNumberTabDTO loyaltyRegisteredMDN) {
		try{
			session.persist(tableName,loyaltyRegisteredMDN);
		}finally{
			
		}
	}

	public void persistAccountNumberDetails(Session session, Transaction transaction,
			String tableName,AccountNumberTabDTO accountNumberTabDTO) {
		try{
			session.persist(tableName,accountNumberTabDTO);
		}finally{
			
		}
	}

	public void persistLoyaltyAccount(Session session, Transaction transaction,
			String tableName, LoyaltyProfileTabDTO loyaltyProfileTabDTO) {
		try{
			session.persist(tableName,loyaltyProfileTabDTO);
		}finally{
			
		}
	}
	

	public int updateLoyaltyProfileForHierarchyBilling(String requestId, Long loyaltyID, int hierarchyEnabled) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		int i = 0;
		//LoyaltyProfileTabDTO
		try{
			
			sql = "update LoyaltyProfileTabDTO set isHierarchyBillingActivated=:flag where loyaltyID=:loyaltyID ";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(sql);
			//query.setParameter("blackListed", blackListed);
			query.setParameter("flag", hierarchyEnabled);
			query.setParameter("loyaltyID", loyaltyID);
			i = query.executeUpdate();
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}
		finally{
			if(transaction!=null)
				transaction.rollback();
			if(session != null)
				session.close();
		}
		return i;
	}

	public int updateLoyaltyProfileForBlacklisted(String requestId, Long loyaltyID, int blackListed) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		int i = 0;
	//	LoyaltyProfileTabDTO l = null;
		try{
			
			sql = "update LoyaltyProfileTabDTO set isHierarchyBillingActivated=:blackListed where loyaltyID=:loyaltyID ";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(sql);
			//query.setParameter("blackListed", blackListed);
			query.setParameter("isBlackListed", blackListed);
			query.setParameter("loyaltyID", loyaltyID);
			i = query.executeUpdate();
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}
		finally{
			if(transaction!=null)
				transaction.rollback();
			if(session != null)
				session.close();
		}
		return i;
	}

	public void persistSubscriberNumberDetails(Session session, Transaction transaction,
			String tableName, SubscriberNumberTabDTO subscriberNumberTabDTO) {
		//Session session = null;
		try{
			session.persist(tableName,subscriberNumberTabDTO);
		}finally{
			
		}
	}

	public void persistLoyaltyTransaction(String requestId, Session session, Transaction transaction, String tableName,
			String channelId, LoyaltyTransactionTabDTO loyaltyTransactionTabDTO) {
		//Session session = null;
		try{
		//	logger.info("TableName");
			session.persist(tableName,loyaltyTransactionTabDTO);
		}finally{
			
		}
	}

}
