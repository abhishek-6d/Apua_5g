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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.NationalNumberTabDTO;
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
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class CustomerProfileDetailsDAO {

	private static final Logger logger = Logger.getLogger(CustomerProfileDetailsDAO.class);
	
	@SuppressWarnings("unchecked")
	public ArrayList<SubscriberNumberTabDTO> getLoyaltyIdBasedOnSubscriberNumber(String service,String txnId,String tableName,String subscriberNumber) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();

			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(
					Restrictions.eq("subscriberNumber",
							Long.parseLong(subscriberNumber)));
			subscriberNumberList = (ArrayList<SubscriberNumberTabDTO>) criteria.list();
			transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.debug("Time Taken for Executing DB operation " + (t2 - t1));

		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
			}
		} catch (Exception e) {
			logger.error("Exception occured  ", e);
			if (transaction != null) {
				transaction.rollback();
			}			
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return subscriberNumberList;
	}

	public ArrayList<NationalNumberTabDTO> getNationalIdBasedOnLoyaltyId(
			String service, String txnId, String tableName, long loyaltyID) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<NationalNumberTabDTO> nationalNumberList = null;
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();

			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(
					Restrictions.eq("loyaltyID",(loyaltyID)));
			nationalNumberList = (ArrayList<NationalNumberTabDTO>) criteria.list();
			transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.debug("Time Taken for Executing DB operation " + (t2 - t1));

		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
			}
		} catch (Exception e) {
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return nationalNumberList;
	}

	public int updateNationalIdInfo(String service, String txnId,
			String tableName, NationalNumberTabDTO nationalNumberTabDTO, String newIdType) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String hql = null;
		int queryUpdateCount = 0;
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
		//	session.update(tableName,nationalNumberTabDTO);
			hql = "Update "+tableName+" set idType = :newIdType where loyaltyID = :loyaltyId and idType=:oldIdType";
			query = session.createQuery(hql);
			query.setParameter("newIdType", newIdType);
			query.setParameter("loyaltyId", nationalNumberTabDTO.getLoyaltyID());
			query.setParameter("oldIdType", nationalNumberTabDTO.getIdType());
			queryUpdateCount = query.executeUpdate();
			transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.debug("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+txnId+"] "
					+ "LoyaltyId :- ["+nationalNumberTabDTO.getLoyaltyID()+"] old Id ["+nationalNumberTabDTO.getIdType()+"] newId ["+newIdType+"] Time Taken for Executing DB operation " + (t2 - t1));
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
			}
		} catch (Exception e) {
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return queryUpdateCount;
	}

	
}
