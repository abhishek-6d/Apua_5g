package com.sixdee.imp.dao;

/**
 * 
 * @author @jith
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
 * <td>January 06,2016 10:55:46 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRedeemDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.MonthlyBillServiceDTO;
import com.sixdee.imp.service.EBillManagement;


public class MonthlyBillServiceDAO {
	private static final Logger logger=Logger.getLogger(MonthlyBillServiceDAO.class);

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	@SuppressWarnings("unused")
	public MonthlyBillServiceDTO getRewardPointDetails(MonthlyBillServiceDTO monthlyBillServiceDTO,LoyaltyProfileTabDTO profileTabDTO) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		String tableName = null;
		String startDate = null;
		String endingDate = null;
		Criteria criteria = null;
		ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
		
		try {
			logger.info("Transaction id -"+monthlyBillServiceDTO.getTranscationId()+"INSIDE REWARD POINT CALCULATION");
			
			 Calendar c = Calendar.getInstance();  
			 Calendar c1 = Calendar.getInstance();// this takes current date
			    c.set(Calendar.DAY_OF_MONTH, 1);
			    System.out.println(c.getTime());  
			    c1.add(Calendar.DATE, 1); 
			    System.out.println(sdf.format(c.getTime()));
			    System.out.println(sdf.format(c1.getTime()));
			    startDate=sdf.format(c.getTime());
			    endingDate=sdf.format(c1.getTime());
			    tableName = tableInfoDAO.getLoyaltyTransactionTable(profileTabDTO.getLoyaltyID()+"");
			    monthlyBillServiceDTO.setBillCycleStartDate(sdf.format(c.getTime()));
			    
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			
			
			

			criteria = session.createCriteria(tableName);
			criteria.add((Restrictions.eq("loyaltyID",
					Long.parseLong(profileTabDTO.getLoyaltyID()+""))));
			criteria.add(Restrictions.ge("createTime",
					sdf.parse(startDate + " 00:00:00")));
			criteria.add(Restrictions.le("createTime",
					sdf.parse(endingDate + " 23:59:59")));
			criteria.setProjection(Projections.sum("rewardPoints"));
			// criteria.setProjection(Projections.rowCount());

			

			// criteria.setMaxResults(limit);
			// query.list();
			Object obj= criteria.uniqueResult();

			logger.info("Transaction id -"+monthlyBillServiceDTO.getTranscationId()+"UNEQUE RESULT IS");
			transaction.commit();
			long t2 = System.currentTimeMillis();
			if(obj!=null)
			monthlyBillServiceDTO.setPointsEarned(obj.toString());
			else
			monthlyBillServiceDTO.setPointsEarned(0+"");
			
			

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return monthlyBillServiceDTO;

	}// updateAccountRegisteredNumberDetails

	
	
	@SuppressWarnings("unused")
	public MonthlyBillServiceDTO getRedeemPointDetails(MonthlyBillServiceDTO monthlyBillServiceDTO,LoyaltyProfileTabDTO profileTabDTO) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		String startDate = null;
		String endingDate = null;
		Criteria criteria = null;
		
		try {
			logger.info("Transaction id -"+monthlyBillServiceDTO.getTranscationId()+"INSIDE REDEEM POINT CALCULATION");
			
			 Calendar c = Calendar.getInstance();  
			 Calendar c1 = Calendar.getInstance();// this takes current date
			    c.set(Calendar.DAY_OF_MONTH, 1);
			    System.out.println(c.getTime());  
			    c1.add(Calendar.DATE, 1); 
			    System.out.println(sdf.format(c.getTime()));
			    System.out.println(sdf.format(c1.getTime()));
			    startDate=sdf.format(c.getTime());
			    endingDate=sdf.format(c1.getTime());
			    
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			
			
			

			criteria = session.createCriteria(LoyaltyRedeemDTO.class);
			criteria.add((Restrictions.eq("loyaltyID",
					Long.parseLong(profileTabDTO.getLoyaltyID()+""))));
			criteria.add(Restrictions.ge("date",
					sdf.parse(startDate + " 00:00:00")));
			criteria.add(Restrictions.le("date",
					sdf.parse(endingDate + " 23:59:59")));
			criteria.setProjection(Projections.sum("redeemPoint"));

			

			Object obj= criteria.uniqueResult();

			logger.info("Transaction id -"+monthlyBillServiceDTO.getTranscationId()+"UNEQUE RESULT IS");
			transaction.commit();
			long t2 = System.currentTimeMillis();
			if(obj!=null)
			monthlyBillServiceDTO.setPointsRedeemed(obj.toString());
			else
				monthlyBillServiceDTO.setPointsRedeemed(0+"");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return monthlyBillServiceDTO;

	}// updateAccountRegisteredNumberDetails
}
