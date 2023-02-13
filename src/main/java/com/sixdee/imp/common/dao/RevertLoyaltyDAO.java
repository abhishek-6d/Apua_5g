package com.sixdee.imp.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dao.RedeemPointsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRedeemDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.RevertLoyalty;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;

public class RevertLoyaltyDAO 
{
	private static final Logger LOG = Logger.getLogger(RevertLoyaltyDAO.class);
	
	
	public void updateStatusSync(String uniqueId,Long loyaltyID,String status)
	{
		LOG.info("IN Update status method"+uniqueId+"-"+loyaltyID+"-"+status);
		Session session = null;
		Query query = null;
		Transaction trx = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			String sql = "UPDATE REVERT_LOYALTY SET STATUS=? WHERE UNIQUE_ID=? and LOYALTY_ID=?";
			query = session.createSQLQuery(sql);
			query.setParameter(0, status);
			query.setParameter(1, uniqueId);
			query.setParameter(2, loyaltyID);
			int i=query.executeUpdate();
			LOG.info("Sql "+sql+" No of rows updated----> "+i+" Status "+status+" uniqueId "+uniqueId+" loyaltyId "+loyaltyID);
			trx.commit();
		}
		catch (Exception e) 
		{
			LOG.error("TransactionId "+uniqueId+" Exception occured ",e);
				if(trx!=null)
					trx.rollback();
		}
		finally
		{
			if (session != null) {
				session.close();
			}
			session = null;
			query = null;
		}
	}
	
	public void updateStatus(Session session1,String uniqueId,Long loyaltyID,String status,boolean flag,double points)
	{
		LOG.info("IN Update status method"+uniqueId+"-"+loyaltyID+"-"+status);
		Session session = null;
		Query query = null;
		Transaction trx = null;
		try
		{
			if(session1!=null)
				session = session1;
			else
			{
				session = HiberanteUtil.getSessionFactory().openSession();
			}
			trx = session.beginTransaction();

			String sql = "UPDATE REVERT_LOYALTY SET STATUS=?"+(flag?",POINTS="+points:"")+" WHERE UNIQUE_ID=? and LOYALTY_ID=?";
			query = session.createSQLQuery(sql);
			query.setParameter(0, status);
			query.setParameter(1, uniqueId);
			query.setParameter(2, loyaltyID);
			
			int i=query.executeUpdate();
			LOG.info("Sql "+sql+" No of rows updated----> "+i+" Status "+status+" uniqueId "+uniqueId+" loyaltyId "+loyaltyID);
			//if(session1==null)
			trx.commit();
		}
		catch (Exception e) 
		{
			LOG.error("TransactionId "+uniqueId+" Exception occured ",e);
			if(session1==null)
				if(trx!=null)
					trx.rollback();
		}
		finally
		{
			if(session1==null)
			{
				if (session != null) {
					session.close();
				}
				session = null;
			}
			query = null;
		}
	}
	
	public void insertRedeem(long loyaltyID, String msisdn, int points, int packId, String tierId, String accntTypeId, String channel)
	{
		LOG.info("IN insertRedeem method");
		Session session = null;
		Transaction trx = null;
		LoyaltyRedeemDTO loyaltyRedeemDTO = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
//			Inserting data into LoyaltyRedeemed Table
			loyaltyRedeemDTO = new LoyaltyRedeemDTO();
			loyaltyRedeemDTO.setLoyaltyID(loyaltyID);
			loyaltyRedeemDTO.setDate(new Date());
			loyaltyRedeemDTO.setPakcageID(packId);
			loyaltyRedeemDTO.setRedeemPoint(points);
			loyaltyRedeemDTO.setSubscriberNumber(msisdn);
			loyaltyRedeemDTO.setTier(Integer.parseInt(tierId));
			loyaltyRedeemDTO.setSubscriberType(accntTypeId);
			loyaltyRedeemDTO.setChannel(Integer.parseInt(channel));
			
			session.save(loyaltyRedeemDTO);
			trx.commit();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
			{
				session.close();
			}
			session = null;
			loyaltyRedeemDTO = null;
		}

	}
	
	public void insertTransaction(String channel,long loyaltyId,double totalPoints,double redeemPoints,int packId,String msisdn,String isTestNumber,String transactionId)
	{
		LOG.info("IN insertTransaction method");
		Session session = null;
		Transaction trx = null;
		LoyaltyTransactionTabDTO transactionDTO = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		HashMap<String, String> loyaltyTransactionMap=null;
		LoyalityCommonTransaction loyalityCommonTransaction=new LoyalityCommonTransaction();
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			loyaltyTransactionMap=new HashMap<String, String>();//added S
			transactionDTO = new LoyaltyTransactionTabDTO();
/*			transactionDTO.setChannel(channel);
			transactionDTO.setLoyaltyID(loyaltyId);
			transactionDTO.setPreRewardPoints(totalPoints);
			transactionDTO.setRewardPoints(redeemPoints);
			transactionDTO.setCurRewardPoints(totalPoints-redeemPoints);
			transactionDTO.setPackageId(packId);
			transactionDTO.setStatusID(LoyaltyTransactionStatus.pointRedeemedforActivationPackage);
			transactionDTO.setSubscriberNumber(msisdn);
			transactionDTO.setCreateTime(new Date());
			transactionDTO.setServerId(Cache.cacheMap.get("SERVER_ID").toString());
			transactionDTO.setTestNumber(Integer.parseInt(isTestNumber));
			transactionDTO.setReqTransactionID(transactionId);*/
			
			loyaltyTransactionMap.put(LoyalityTransactionConstants.channel, channel);
			loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID, String.valueOf(loyaltyId));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.preRewardPoints, String.valueOf(totalPoints));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.rewardPoints, String.valueOf(redeemPoints));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints, String.valueOf(totalPoints-redeemPoints));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.packageId, String.valueOf(packId));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId, String.valueOf(LoyaltyTransactionStatus.pointRedeemedforActivationPackage));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber, msisdn);
			loyaltyTransactionMap.put(LoyalityTransactionConstants.serverId,Cache.cacheMap.get("SERVER_ID").toString());
			loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId, String.valueOf(LoyaltyTransactionStatus.pointRedeemedforActivationPackage));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.testNumber, String.valueOf(isTestNumber));
			loyaltyTransactionMap.put(LoyalityTransactionConstants.reqTransactionID, transactionId);
			transactionDTO=loyalityCommonTransaction.loyaltyTransactionSetter(transactionDTO, loyaltyTransactionMap);
			session.save(tableInfoDAO.getLoyaltyTransactionTable(String.valueOf(loyaltyId)),transactionDTO);
			trx.commit();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
			{
				session.close();
			}
			session = null;
			loyalityCommonTransaction=null;
		}

	}
	
	@SuppressWarnings("unchecked")
	public void revertLoyalty(String uniqueId,Long loyaltyID)
	{
		Session session = null;
		Query query = null;
		List<RevertLoyalty> list = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createQuery("FROM RevertLoyalty WHERE uniqueId=? and loyaltyID=?");
			query.setParameter(0, uniqueId);
			query.setParameter(1, loyaltyID);
			list = query.list();
			
			LOG.info("UNIQUE ID B4 REVERT"+uniqueId);
			LOG.info("LOYALTY ID B4 REVERT"+loyaltyID);
			if(list!=null && list.size()>0)
			{
			revertTransaction(list);
			for(RevertLoyalty r : list)
			{
				updateStatus(null,r.getUniqueId(),r.getLoyaltyID(), "R",true,0);
			}
			}
			else
				LOG.info("List Size is zero =>>> No data in REVERT LOYALTY Table for Loyalty ID ["+loyaltyID+"] and Unique ID ["+uniqueId+"] ");
			session.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			session = null;
			query = null;
		}
	}
	
	private void revertTransaction(List<RevertLoyalty> list)
	{
		LOG.info("List = === "+list.size());
		 
		double totalPoints = 0 ;
		int counter=0;
		LoyaltyProfileTabDTO loyaltyDTO=null;
		TableDetailsDAO tableDetailsDAO=null;
	 
		try
		{
			tableDetailsDAO=new TableDetailsDAO();
			loyaltyDTO=new LoyaltyProfileTabDTO();
			 
			for(RevertLoyalty revertLoyalty:list)
				totalPoints+=revertLoyalty.getPoints();
			
			LOG.info("Total Points to revert = "+ totalPoints);
			
			do
			{
			loyaltyDTO = tableDetailsDAO.getLoyaltyProfile(list.get(0).getLoyaltyID());
			if(updateLoyaltyProfile(loyaltyDTO, totalPoints))
				break;
			else
				counter++;
			}while(counter<10);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{}
	}
	
	private boolean updateLoyaltyProfile(LoyaltyProfileTabDTO loyaltyId,double totalPoints)
	{
		RedeemPointsDAO dao = null;
		Long parentId ;
		Query query ;
		Session session = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		Transaction trx = null;
		boolean flag=false;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			dao = new RedeemPointsDAO();
			parentId = dao.findParent(loyaltyId.getLoyaltyID());
			query = session.createSQLQuery("UPDATE "+tableInfoDAO.getLoyaltyProfileDBTable(parentId.toString())+" SET REWARD_POINTS=? WHERE LOYALTY_ID=? AND COUNTER=?");
			query.setParameter(0, loyaltyId.getRewardPoints()+totalPoints);
			query.setParameter(1, parentId);
			query.setParameter(2, loyaltyId.getCounter());
			if(query.executeUpdate()>0)
				flag=true;
			trx.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			session = null;
			dao = null;
			parentId =null;
			query =null;
			tableInfoDAO = null;
			trx = null;
		}
		return flag;
	}

	

	

	
	
}
