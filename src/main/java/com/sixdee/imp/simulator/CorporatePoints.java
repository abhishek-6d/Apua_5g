package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;

public class CorporatePoints
{
	private Logger log = Logger.getLogger(CorporatePoints.class);
	
	public void start()
	{
		List<BigDecimal> loyaltyIds = getAllLoyaltyId();
		
		for(BigDecimal b : loyaltyIds)
		{
			long key = b.longValue();
			double rewardPoints = findSum(key, 5);
			double redeemPoints = findSum(key, 8);
			
			insertReport(key,rewardPoints,redeemPoints);
		}
	}
	private List<BigDecimal> getAllLoyaltyId()
	{
		Session session = null;
		Query query = null;
		List<BigDecimal> list = null; 
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = "SELECT DISTINCT LOYALTY_ID FROM CORPORATE_LINES";
			 
			query = session.createSQLQuery(sql);
			
			list = query.list();
			log.info("Got All Loyalty Ids from corporate....");
			
		}
		catch (Exception e) 
		{
			log.error("Exception ",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
		}
		
		return list;
	}
	
	private double findSum(long loyaltyId,int statusid )
	{
		double totalPoints =0.0;
		Session session = null;
		Criteria ctr = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		List<Double> list = null;
		
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			
			ctr = session.createCriteria(tableInfoDAO.getLoyaltyTransactionTable(loyaltyId+""));
			ctr.add(Restrictions.eq("loyaltyID", loyaltyId));
			ctr.add(Restrictions.eq("statusID", statusid));
			ctr.setProjection(Projections.property("rewardPoints"));
			
			list = ctr.list();
			for(Double d : list)
			{
				totalPoints = totalPoints+ d;
			}
		}
		catch (Exception e) {
			log.error("Exception = ",e);
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		
		return totalPoints;
	}
	
	private void insertReport(long loyaltyId,double rewardPoints,double redeemPoints)
	{
		Session session = null;
		Query query = null;
		
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("INSERT INTO LOYALTY_REWARD_REDEEM (LOYALTY_ID,REWARD_POINTS,REDEEM_POINTS) VALUES (?,?,?)");
			
			query.setParameter(0, loyaltyId);
			query.setParameter(1, rewardPoints);
			query.setParameter(2, redeemPoints);
			
			query.executeUpdate();
			log.info("Eompleted for loyalty id = "+loyaltyId);
			
		}
		catch (Exception e) {
			log.error("Exception =",e);
			
		}
		finally
		{
			if(session!=null)
				session.close();
		}
	}
	
}
