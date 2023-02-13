/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.AppMsisdnMappingDTO;
import com.sixdee.imp.dto.ApplicationCodesDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;

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
public class TierUpgradationDAO {

	private static final Logger logger = Logger.getLogger(TierUpgradationDAO.class);
	public ArrayList<ApplicationCodesDTO> getCodeForCurrentTier(String txnId, long loyaltyId, int nextTierID,String appId) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<ApplicationCodesDTO> applicationCodes = null;
		
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(ApplicationCodesDTO.class);
			logger.debug("Service : PointCalculation -- Transaction ID :"+txnId+" LoyaltyId :"+loyaltyId+" Fetching codes for app");
			criteria.add(Restrictions.and(
						Restrictions.eq("codeBlocked", 0), 
						Restrictions.and(
								Restrictions.eq("appId", appId), Restrictions.eq("tierId", nextTierID))));
			criteria.setMaxResults(1);
			applicationCodes = (ArrayList<ApplicationCodesDTO>) criteria.list();
			transaction.commit();
		}catch(HibernateException e){
			logger.error("Hibernate Exception ,some problem with query",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return applicationCodes;
	}
	public int blockCodeForTier(String txnId, long loyaltyId, String appCode, int nextTierID) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		int updateCount = 0;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			logger.debug("Service : PointCalculation -- Transaction ID :"+txnId+" LoyaltyId "+loyaltyId+" Fetched code :"
					+appCode+ " for Tier :"+nextTierID);
			query = session.createQuery("Update ApplicationCodesDTO set codeBlocked = :blockStatus where "
					+ "appCode = :appCode and codeBlocked = :unBlockStatus and tierId=:tierId");
			query.setParameter("blockStatus", 1);
			query.setParameter("appCode", appCode);
			query.setParameter("unBlockStatus", 0);
			query.setParameter("tierId", nextTierID);
			updateCount = query.executeUpdate();
			transaction.commit();
		}catch(HibernateException e){
			logger.error("Hibernate Exception ,some problem with query",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}finally{
			if(session!=null){
				session.close();
			}
		}
		return updateCount;
	}
	public boolean mapAppToMsisdn(String txnId,
			AppMsisdnMappingDTO appMsisdnMapping) {
		Session session = null;
		Transaction transaction = null;
		boolean isCommited = false;
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(appMsisdnMapping);
			transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.debug("TransactionId :"+txnId+" Time Taken for Executing DB operation " + (t2 - t1));
			isCommited = true;
		} catch (HibernateException e) {
			logger.error("TransactionId :"+txnId+" Hibernate Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
			}
		} catch (Exception e) {
			logger.error("TransactionId :"+txnId+" Exception occured  ", e);
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return isCommited;
	}

}
