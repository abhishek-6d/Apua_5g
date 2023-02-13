package com.sixdee.imp.simulator;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class RemoveCorporateAccount1 
{
	private Logger log = Logger.getLogger(RemoveCorporateAccount1.class);
	
	
	public void start()
	{
		log.info("Getting all loyalty id from corporate table ");
		List<Object[]> loyaltyIds = getAllLoyaltyIds();
		log.info("Getting all loyalty id from corporate table DONEEEEEEEEEe");
		
	//	deleteFromSubscriberTab(loyaltyIds);
		
		log.info("<<<<<<<<<<<<completed>>>>>>>>>>>>>>>>>>>>>>");
		 
	}

	public void deleteFromSubscriberTab(SubscriberNumberTabDTO dto)
	{
		Session session = null;
		TableInfoDAO info = new TableInfoDAO();
		Criteria ctr = null;
		SubscriberNumberTabDTO subsdto = dto;
		try
		{
			
				try
				{
					log.info("<<<<<>>>>>>><<<<<>>>>>>>>Started processing loyalty id = "+subsdto.getLoyaltyID());
					//Object[] all = it.next();
					String subsTable = info.getSubscriberNumberTable(subsdto.getSubscriberNumber()+"");
					
					session = HiberanteUtil.getSessionFactory().openSession();
					session.beginTransaction();
					
					log.info("getting data from subscriber number table");
					ctr = session.createCriteria(subsTable);
					ctr.add(Restrictions.eq("loyaltyID", subsdto.getLoyaltyID()));
					ctr.add(Restrictions.eq("subscriberNumber", subsdto.getSubscriberNumber()));
					
					subsdto = (SubscriberNumberTabDTO)ctr.uniqueResult();
					
					log.info("Deleting ");
					session.delete(subsdto);
					
					log.info("finding count for deleted account number ");
					ctr = session.createCriteria(subsTable);
					ctr.add(Restrictions.eq("accountNumber", subsdto.getAccountNumber()));
					ctr.setProjection(Projections.rowCount());
				
					int rowcount = (Integer)ctr.uniqueResult();
					log.info("row count = "+rowcount);
					if(rowcount == 0)
						deleteAccount(session, subsdto.getAccountNumber(), info.getAccountNumberTable(subsdto.getAccountNumber().toString()));
				
					log.info("Deleting linked lines");
					deleteLinkedLine(session,subsdto.getSubscriberNumber()+"",info.getLoyaltyRegisteredNumberTable(subsdto.getLoyaltyID()+""));
					log.info("Deleting transaction ");
					deleteTransaction(session,subsdto.getLoyaltyID(),info.getLoyaltyTransactionDBTable(subsdto.getLoyaltyID()+""));
					
					log.info("Updating details .. ");
					updateDetails(session,subsdto.getLoyaltyID());
					
					session.getTransaction().commit();
					log.info("<<<<<<<<>>>>>>>>>>><<<<<<<<<>>>>>>>End processing loyalty id = "+subsdto.getLoyaltyID());
				
				}
				catch (Exception e) 
				{
					session.getTransaction().rollback();
					log.info("Exception for loyalty id = "+subsdto.getLoyaltyID());
					log.error("Exception = ",e);
				}
				finally
				{
					if(session!=null)
						session.close();
				}
			
			
		}
		catch (Exception e) 
		{
			log.error("Exception = ",e);
		}
		
	}
	
	private void updateDetails(Session session,Long loyaltyId)
	{
		TableInfoDAO info = new TableInfoDAO();
		Criteria ctr = session.createCriteria(info.getLoyaltyProfileTable(loyaltyId.toString()));
		ctr.add(Restrictions.eq("loyaltyID", loyaltyId));
		
		//LoyaltyProfileTabDTO loyaltyDTO = (LoyaltyProfileTabDTO)session.load(info.getLoyaltyProfileTable(loyaltyId.toString()), loyaltyId);
		LoyaltyProfileTabDTO loyaltyDTO = (LoyaltyProfileTabDTO)ctr.uniqueResult();
		loyaltyDTO.setRewardPoints(0.0);
		loyaltyDTO.setTierId(1);
		loyaltyDTO.setStatusPoints(0.0);
		
		Query query = session.createSQLQuery("UPDATE "+info.getLoyaltyTransactionDBTable(loyaltyId.toString())+" SET PRE_REWARD_POINTS=0,CUR_REWARD_POINTS=0,PRE_STATUS_POINTS=0,CUR_STATUS_POINTS=0,REWARD_POINTS=0,STATUS_POINTS=0,PRE_TIER_ID=1,CUR_TIER_ID=1 WHERE LOYALTY_ID="+loyaltyId);
		query.executeUpdate();
		
	}
	
	private void deleteTransaction(Session session,Long loyaltyId,String tableName)
	{
		Query query = session.createSQLQuery("DELETE FROM "+tableName+" WHERE MONTH_INDEX IN (9,10,11,12,1,2,3,4,5,6,7) AND LOYALTY_ID="+loyaltyId+" AND STATUS_ID NOT IN (1,2)");
		query.executeUpdate();
	}
	
	private void deleteLinkedLine(Session session ,String linkedNumber,String tableName)
	{
		Criteria ctr = session.createCriteria(tableName);
		ctr.add(Restrictions.eq("linkedNumber", linkedNumber));
		LoyaltyRegisteredNumberTabDTO dto = (LoyaltyRegisteredNumberTabDTO)ctr.uniqueResult();
		
		session.delete(dto);
	}
	
	private void deleteAccount(Session session ,String accountNumber,String tableName)
	{
		log.info("Deleting account table record");
		Criteria ctr = session.createCriteria(tableName);
		ctr.add(Restrictions.eq("accountNumber", accountNumber));
		AccountNumberTabDTO acctDTO = (AccountNumberTabDTO)ctr.uniqueResult();
		session.delete(acctDTO);
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getAllLoyaltyIds()
	{
		Session session = null;
		Query query = null;
		List<Object[]> loyaltyIds = null;
		
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			
			query = session.createSQLQuery("SELECT LOYALTY_ID,SUBSCRIBER_NUMBER FROM CORPORATE_LINES");
			//query.setMaxResults(500);
			
			loyaltyIds = query.list();
		}
		catch (Exception e) 
		{
			log.error("Exception ",e);
		}
		finally
		{
			query = null;
			if(session!=null)
				session.close();
		}
		
		return loyaltyIds;
	}
	
}

