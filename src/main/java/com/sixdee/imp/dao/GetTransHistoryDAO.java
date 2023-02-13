/**
 * 
 */
package com.sixdee.imp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.SubscriberTransactionHistroyDTO;

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
public class GetTransHistoryDAO {
	private static final Logger logger = Logger.getLogger(GetTransHistoryDAO.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	public ArrayList<SubscriberTransactionHistroyDTO> getTransactionDetails(String tableName , String subsNumber, int noOfLastTrans) throws Exception {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<SubscriberTransactionHistroyDTO> transHistoryList = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("subscriberNumber", Long.parseLong(subsNumber)));
			criteria.addOrder(Order.desc("id"));
			criteria.setMaxResults(noOfLastTrans);
			transHistoryList = (ArrayList<SubscriberTransactionHistroyDTO>) criteria.list();
			transaction.commit();
		}catch (HibernateException he) {
			logger.error("Hibernate Exception occured ",he);
			if(transaction != null)
				transaction.rollback();
			throw he;
		}
		catch (Exception e) {
	
			logger.error("Exception occured ",e);
			throw e;
		}finally{
			if(session != null)
				session.close();
			criteria = null;
			transaction = null;
		}
		return transHistoryList;
	}
	public ArrayList<SubscriberTransactionHistroyDTO> getTransactionDetails(String tableName,String subsNumber, int noOfLastTrans,
			String fromDate, String endDate) throws Exception {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<SubscriberTransactionHistroyDTO> transHistoryList = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.ge("startDate", sdf.parse(fromDate)));
			criteria.add(Restrictions.le("endDate", sdf.parse(endDate)));
			criteria.add(Restrictions.eq("subscriberNumber", subsNumber));
			criteria.addOrder(Order.desc("id"));
			criteria.setMaxResults(noOfLastTrans);
			transHistoryList = (ArrayList<SubscriberTransactionHistroyDTO>) criteria.list();
			transaction.commit();
		}catch (HibernateException he) {
			logger.error("Hibernate Exception occured ",he);
			if(transaction != null)
				transaction.rollback();
			throw he;
		}
		catch (Exception e) {
	
			logger.error("Exception occured ",e);
			throw e;
		}finally{
			if(session != null)
				session.close();
			criteria = null;
			transaction = null;
		}
		return transHistoryList;
		
	}
	public ArrayList<SubscriberTransactionHistroyDTO> getTransactionDetails(String tableName,
			String subsNumber, String fromDate, String endDate) throws Exception {


		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<SubscriberTransactionHistroyDTO> transHistoryList = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.ge("startDate", sdf.parse(fromDate)));
			criteria.add(Restrictions.le("endDate", sdf.parse(endDate)));
			criteria.add(Restrictions.eq("subscriberNumber", subsNumber));
			//criteria.addOrder(Order.desc("id"));
			//criteria.setMaxResults(noOfLastTrans);
			transHistoryList = (ArrayList<SubscriberTransactionHistroyDTO>) criteria.list();
			transaction.commit();
		}catch (HibernateException he) {
			logger.error("Hibernate Exception occured ",he);
			if(transaction != null)
				transaction.rollback();
			throw he;
		}
		catch (Exception e) {
	
			logger.error("Exception occured ",e);
			throw e;
		}finally{
			if(session != null)
				session.close();
			criteria = null;
			transaction = null;
		}
		return transHistoryList;
		
	
	}

	
	
}
