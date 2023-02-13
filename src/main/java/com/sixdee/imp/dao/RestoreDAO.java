/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionDetailsDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;

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
public class RestoreDAO {
	
	private Logger logger = Logger.getLogger(RestoreDAO.class);

	public LoyaltyProfileTabDTO getLoyaltyDetails(String loyaltyId,double redeemPoints){
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		TableInfoDAO tableInfoDAO = null;
		String tableName = null;
		Criteria criteria = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		ArrayList<LoyaltyProfileTabDTO> profileTableList = null;
		try{
			tableInfoDAO = new TableInfoDAO();
			tableName = tableInfoDAO.getLoyaltyProfileTable(loyaltyId);
			logger.info("Searching for loyalty id ["+loyaltyId+"] in table ["+tableName+"]");
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(LoyaltyProfileTabDTO.class,loyaltyId);
			profileTableList =  (ArrayList<LoyaltyProfileTabDTO>) criteria.list();
			for(LoyaltyProfileTabDTO loyaltyProf : profileTableList ){
				loyaltyProf.setRewardPoints(loyaltyProf.getRewardPoints()+redeemPoints);
				session.update(loyaltyProf);
				loyaltyProfileTabDTO = loyaltyProf;
				break;
			}
			transaction.commit();
			
		}catch (Exception e) {
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
			session = null;
			transaction = null;
			query = null;
			sql = null;
			tableInfoDAO=null;
		}
		return loyaltyProfileTabDTO;
	}

	
	public void saveLoyaltyTransacation(LoyaltyTransactionTabDTO loyaltyTransactionDetailsDTO){
		Session session = null;
		Transaction transaction = null;
		TableInfoDAO tableInfoDAO = null;
		String tableName = null;
		try{
			tableInfoDAO = new TableInfoDAO();
			tableName = tableInfoDAO.getLoyaltyTransactionTable(loyaltyTransactionDetailsDTO.getLoyaltyID()+"");
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(tableName,loyaltyTransactionDetailsDTO);
			transaction.commit();
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
			session=null;
			tableInfoDAO=null;
		}
	}
	
}
