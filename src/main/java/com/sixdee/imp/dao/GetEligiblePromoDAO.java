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
 * <td>April 24,2013 05:44:52 PM</td>
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;

public class GetEligiblePromoDAO {

	private static Logger logger = Logger.getLogger(GetEligiblePromoDAO.class);
	public ArrayList<String> getEligiblePromos(String tableName ,String subScriber) {
		ArrayList<String> eligiblePromos = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		try{
			logger.info("Fetching Subscriber Service Details from Transaction History");
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("subscriberNumber", Long.parseLong(subScriber)));
			criteria.setProjection(Projections.property("serviceID"));
			eligiblePromos = (ArrayList<String>) criteria.list();
			transaction.commit();
		}catch (Exception e) {
			if(transaction != null)
				transaction.rollback();
			logger.error("Exception occured ",e);
		}finally{
			if(session != null)
				session.close();
			transaction = null;
			session = null;
			criteria = null;
		}
		return eligiblePromos;
	}


}
