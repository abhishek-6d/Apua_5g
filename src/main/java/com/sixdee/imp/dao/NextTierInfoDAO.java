package com.sixdee.imp.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.TierDetails;

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
 * <td>June 02,2013 11:26:13 AM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */




public class NextTierInfoDAO {

	private Logger logger = Logger.getLogger(NextTierInfoDAO.class);
	private int retryCount = 0;
	
	public TierDetails getPointsToNextTier(String tableName,String loyaltyId) throws CommonException{
		Session session = null;
		Transaction transaction = null;
		Query hQuery = null;
		String sql = null;
		TierDetails tier = null;
		boolean isCommunicationFailure = false;
		try{
			sql = "Select TIER_ID,STATUS_POINTS,TIER_UPDATED_DATE,REWARD_POINTS from "+tableName+" where LOYALTY_ID=?";
			 session = HiberanteUtil.getSessionFactory().openSession();
			 transaction = session.beginTransaction();
			 logger.info("Sql Executed "+sql);
			 hQuery = session.createSQLQuery(sql);
			 hQuery.setParameter(0, loyaltyId);
			 for(Object[] tierInfo : (List<Object[]>)hQuery.list()){
				if(tierInfo != null){
					tier = new TierDetails();
					if(tierInfo[0]!=null){
						tier.setTierId((int)Double.parseDouble(tierInfo[0]+""));
					}if(tierInfo[1]!=null){
						tier.setStatusPoints((int)Double.parseDouble(tierInfo[1]+""));
					}if(tierInfo[2]!=null){
					   tier.setTierUpdateDate((Date)tierInfo[2]);
					}
					if(tierInfo[3]!=null){
					   tier.setRewardPoints((long)(Double.parseDouble(tierInfo[3].toString())));
					}
				}else{
					throw new CommonException("No Details about Customer with loyalty id "+loyaltyId);
				}
			 }
		}catch (HibernateException e) {
			logger.error("Hibernate Exception occured ",e);
			if(e.getCause().equals("CommunicationsExceptions")){
				isCommunicationFailure = true;
			}
			
			if(transaction != null)
				transaction.rollback();
		}finally{
			if(session != null)
				session.close();
			if(isCommunicationFailure && retryCount<5){
				retryCount++;
				tier = getPointsToNextTier(tableName,loyaltyId);
			}
			
			retryCount -- ;
		}
		return tier;
	}
	
	public static void main(String[] args) {
		System.out.println((int)Double.parseDouble("2.0"));
	}

}
