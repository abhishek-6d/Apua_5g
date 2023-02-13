package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.dto.DeleteSummaryDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.NotificationModuleDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TerminationServiceDTO;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>APR 19, 2013</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

public class DeleteAccountDAO 
{
	Logger logger = Logger.getLogger(DeleteAccountDAO.class);
	Date date=new Date();
	TableDetailsDAO tabDAO= new TableDetailsDAO();
	TableInfoDAO tableInfoDAO = new TableInfoDAO();
	LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
	DeleteSummaryDTO deleteSmryDTO=new DeleteSummaryDTO();
	String reqTxnId="";
	public DeleteAccountDAO(String txnId)
	{
		this.reqTxnId=txnId;
	}

	public boolean deleteSubscriberTableDetails(Long subscriberNumber,Long loyaltyID, String statusid,boolean flag) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		Query query = null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getSubscriberNumberDBTable(subscriberNumber+"");		
			logger.info("TABLE NAME::"+tableName);
			logger.info("NUMBER::"+subscriberNumber);
			transaction=session.beginTransaction();
			if(statusid!=null && statusid.equals("6"))
			{
				if(!flag)
				{
					sql="DELETE FROM "+tableName+"  where SUBSCRIBER_NUMBER = :number";
					query = session.createSQLQuery(sql);				
					query.setParameter("number",subscriberNumber);
				}
				else
				{
					sql="DELETE FROM "+tableName+"  where LOYALTY_ID = :loyaltyID";
					query = session.createSQLQuery(sql);				
					query.setParameter("loyaltyID",loyaltyID);
				}
				
				int rowCount = query.executeUpdate();
				logger.info("Hard Deleted ::Rows affected: " + rowCount);
				if(rowCount>0)
					done=true;	
			}
			else
			{
				if(!flag)
				{
					sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where SUBSCRIBER_NUMBER = :number";
					query = session.createSQLQuery(sql);
					query.setParameter("statusId",statusid);
					query.setParameter("updateDate",date);
					query.setParameter("number",subscriberNumber);
				}
				else
				{
					sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID = :loyaltyID";
					query = session.createSQLQuery(sql);
					query.setParameter("statusId",statusid);
				    query.setParameter("updateDate",date);
				    query.setParameter("loyaltyID",loyaltyID);
				}
				int rowCount = query.executeUpdate();
				logger.info("Soft Deleted ::Rows affected: " + rowCount);
		
				if(rowCount>0)
					done=true;	
			}
			transaction.commit();
			
			//########### For Notification##############################
			
			if(done)
			{
				// SMS Notification
				NotificationModuleDTO moduleDTO=new NotificationModuleDTO();
				
				sql="from LOYALTY_PROFILE_ENTITY_0 where loyaltyID=:loyaltyID";
				query=session.createQuery(sql);
				query.setParameter("loyaltyID", loyaltyID);
				LoyaltyProfileTabDTO loyaltyProfileTabDTO=(LoyaltyProfileTabDTO)query.uniqueResult();
				
				if(loyaltyProfileTabDTO!=null){
					
					
					//moduleDTO.setServiceId(NotificationTokens.deleteLoyaltyTemplateID);
					//moduleDTO.setMessageId(NotificationTokens.deleteLoyaltyMsgID);
					moduleDTO.setLanguageId(loyaltyProfileTabDTO.getDefaultLanguage()!=null && !loyaltyProfileTabDTO.getDefaultLanguage().trim().equalsIgnoreCase("")?loyaltyProfileTabDTO.getDefaultLanguage():Cache.getCacheMap().get("DEFAULT_LANGUAGEID"));
					if(loyaltyProfileTabDTO.getEmailID()!=null)
					{
						moduleDTO.setEmailID(loyaltyProfileTabDTO.getEmailID());
						moduleDTO.setEmail(true);
					}else if(loyaltyProfileTabDTO.getContactNumber()!=null)
					{
						moduleDTO.setMdn(loyaltyProfileTabDTO.getContactNumber());
						moduleDTO.setSMS(true);
					}
					
				}
				
				
				//NotificationThreadInitiator.notificationThreadPool.addTask(moduleDTO);
				moduleDTO=null;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}		
		return done;		
	}
	
	public boolean deleteLoyaltySubscriberTableDetails(Long loyaltyID, String statusid) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getSubscriberNumberDBTable(loyaltyID+"");		
			logger.info("TABLE NAME::"+tableName);
			logger.info("NUMBER::"+loyaltyID);
			transaction=session.beginTransaction();
			if(statusid!=null && statusid.equals("6"))
			{
				sql="DELETE FROM "+tableName+"  where LOYALTY_ID = :number";
				Query query = session.createSQLQuery(sql);				
				query.setParameter("number",loyaltyID);
			    int rowCount = query.executeUpdate();
			    logger.info("Hard Deleted ::Rows affected: " + rowCount);
			    if(rowCount>0)
			    	done=true;		
			}
			else
			{
			    sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID = :number";
				Query query = session.createSQLQuery(sql);
				if(statusid!=null)
			        query.setParameter("statusId",statusid);
				else
					query.setParameter("statusId",3);
		        query.setParameter("updateDate",date);
		        query.setParameter("number",loyaltyID);
		        int rowCount = query.executeUpdate();
		        logger.info("Soft Deleted ::Rows affected: " + rowCount);
			
			if(rowCount>0)
				done=true;	
			}
			transaction.commit();		
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}		
		return done;		
	}
	
	public boolean deleteLoyaltyAccountTableDetails(Long loyaltyID, String statusid) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getAccountNumberDBTable(loyaltyID+"");		
			logger.info("TABLE NAME::"+tableName);
			logger.info("NUMBER::"+loyaltyID);
			transaction=session.beginTransaction();
			if(statusid!=null && statusid.equals("6"))
			{
				sql="DELETE FROM "+tableName+"  where LOYALTY_ID = :number";
				Query query = session.createSQLQuery(sql);				
				query.setParameter("number",loyaltyID);
			    int rowCount = query.executeUpdate();
			    logger.info("Hard Deleted ::Rows affected: " + rowCount);
			    if(rowCount>0)
			    	done=true;		
			}
			else
			{
			    sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID = :number";
				Query query = session.createSQLQuery(sql);
				if(statusid!=null)
			        query.setParameter("statusId",statusid);
				else
					query.setParameter("statusId",3);
		        query.setParameter("updateDate",date);
		        query.setParameter("number",loyaltyID);
		        int rowCount = query.executeUpdate();
		        logger.info("Soft Deleted ::Rows affected: " + rowCount);
		        if(rowCount>0)
				done=true;	
			}
			transaction.commit();		
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}		
		return done;		
	}
	
	public boolean deleteNationalTableDetails(Long loyaltyID,Long subscriberNumber, String statusid) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getNationalNumberDBTable(subscriberNumber+"");		
			logger.info("TABLE NAME::"+tableName);
			logger.info("NUMBER::"+subscriberNumber);
			transaction=session.beginTransaction();
			if(statusid!=null && statusid.equals("6"))
			{
				sql="DELETE FROM "+tableName+"  where LOYALTY_ID = :loyaltyID";
				Query query = session.createSQLQuery(sql);				
				query.setParameter("loyaltyID",loyaltyID);
			    int rowCount = query.executeUpdate();
			    logger.info("Hard Deleted ::Rows affected: " + rowCount);
			    if(rowCount>0)
			    	done=true;		
			}
			else
			{
			    sql="UPDATE "+tableName+" set STATUS_ID = :statusId  where LOYALTY_ID = :loyaltyID";
				Query query = session.createSQLQuery(sql);
				if(statusid!=null)
			        query.setParameter("statusId",statusid);
				else
					query.setParameter("statusId",3);
		        query.setParameter("loyaltyID",loyaltyID);
		        int rowCount = query.executeUpdate();
		        logger.info("Soft Deleted ::Rows affected: " + rowCount);
			if(rowCount>0)
				done=true;	
			}
			transaction.commit();		
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}		
		return done;		
	}

	public boolean deleteAccountTableDetails(Long subscriberNumber,Long loyaltyID,String accountNum, String statusid) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getAccountNumberDBTable(subscriberNumber+"");
			transaction=session.beginTransaction();
			if(statusid!=null && statusid.equals("6"))
			{
				if(accountNum!=null)
				{
					sql="DELETE FROM "+tableName+" where ACCOUNT_NO = :accId and LOYALTY_ID =:loyaltyID";
					Query query = session.createSQLQuery(sql);				
					query.setParameter("accId",accountNum); 
					query.setParameter("loyaltyID",loyaltyID);
			        int rowCount = query.executeUpdate();
			        logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
						done=true;
				}
				else
				{
					sql="DELETE FROM "+tableName+" where LOYALTY_ID =:loyaltyID";
					Query query = session.createSQLQuery(sql);				
					query.setParameter("loyaltyID",loyaltyID);
			        int rowCount = query.executeUpdate();
			        logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
						done=true;
				}
			}
			else
			{
			sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID = :loyaltyID";
			Query query = session.createSQLQuery(sql);
			
		        query.setParameter("statusId",statusid);
		        query.setParameter("updateDate",date);
		        query.setParameter("loyaltyID",loyaltyID);
		        int rowCount = query.executeUpdate();
		        logger.info("Soft Deleted ::Rows affected: " + rowCount);
		        if(rowCount>0)
		        	done=true;		
			}
			transaction.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
		
	}
	
	

	public boolean deleteLoyaltyTableDetails(Long subscriberNumber,Long loyaltyID, String statusid,String channel,Double points) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getLoyaltyProfileDBTable(loyaltyID+"");
			
			transaction=session.beginTransaction();
			logger.info("TABLE NAME::"+tableName);
			logger.info("STATUS_ID::"+statusid);
			logger.info("NUMBER::"+subscriberNumber);
			if(statusid!=null && statusid.equals("6"))
			{
					sql="DELETE FROM "+tableName+" where LOYALTY_ID = :loyaltyId";
					Query query = session.createSQLQuery(sql);				
					query.setParameter("loyaltyId",loyaltyID);
			        int rowCount = query.executeUpdate();
			        logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
					{
		        	done=true;	
			        deleteSubscriberTableDetails(subscriberNumber,loyaltyID, statusid,true);
			        deleteAccountTableDetails(subscriberNumber, loyaltyID, null, statusid);
			        deleteNationalTableDetails(loyaltyID, subscriberNumber, statusid);
					}
			}
			else
			{
				sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID =:loyaltyId";
				Query query = session.createSQLQuery(sql);				
			    query.setParameter("statusId",statusid);
		        query.setParameter("updateDate",date);
		        query.setParameter("loyaltyId",loyaltyID);
		        int rowCount = query.executeUpdate();
		        logger.info("Soft Deleted ::Rows affected: " + rowCount);		
			if(rowCount>0)
				done=true;		
			}
			transaction.commit();	
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
		
	}	
	
/*	private void deleteLoyaltyRegisteredNumberDetails(Long subscriberNumber,Long loyaltyID,String channel,Double points) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			
			System.out.println("In deleteAccountDAO>>>>>>>>>>>>>>deleteSubscriberTableDetails");
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID+"");	
			System.out.println("TABLE NAME"+tableName);
			System.out.println("subscriberNumber"+subscriberNumber);
			transaction=session.beginTransaction();			
			sql="DELETE FROM "+tableName+"  where LINKED_NO = :number AND LOYALTY_ID =:loyaltyId";
			Query query = session.createSQLQuery(sql);				
			query.setParameter("number",subscriberNumber);
			query.setParameter("loyaltyId",loyaltyID);
			int rowCount = query.executeUpdate();
			System.out.println("Hard Deleted ::Rows affected: " + rowCount);
			logger.info(sql);		
			if(rowCount>0)
				done=true;		
			if(done)
				insertTransactionDetails(subscriberNumber,loyaltyID,channel,points);
			transaction.commit();		
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}		
	}*/

	private void insertTransactionDetails(Long subscriberNumber,Long loyaltyID,String channel,Double points,int status) {

		Session session = null;
		Transaction transaction = null;
		HashMap<String, String> loyaltyTransactionMap=null;
		LoyalityCommonTransaction loyalityCommonTransaction=null;
		try{
			logger.info("Inserting into LOYALTY_TRANSACTION Table ::"+tableInfoDAO.getLoyaltyTransactionTable(loyaltyID.toString()));
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
			loyalityCommonTransaction=new LoyalityCommonTransaction();
			/*loyaltyTransactionTabDTO.setLoyaltyID(loyaltyID);
			loyaltyTransactionTabDTO.setChannel(channel);
			loyaltyTransactionTabDTO.setRewardPoints(points);
			loyaltyTransactionTabDTO.setStatusID(status);
			loyaltyTransactionTabDTO.setSubscriberNumber(""+subscriberNumber);
			loyaltyTransactionTabDTO.setCreateTime(new Date());
			loyaltyTransactionTabDTO.setServerId(Cache.getCacheMap().get("SERVER_ID"));
			loyaltyTransactionTabDTO.setReqTransactionID(reqTxnId);*/
			
    		loyaltyTransactionMap=new HashMap<String, String>();//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID, String.valueOf(loyaltyID));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.channel,channel);//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.rewardPoints, String.valueOf(points));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId, String.valueOf(status));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber, String.valueOf(subscriberNumber));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.createTime, String.valueOf(new Date()));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.serverId, Cache.getCacheMap().get("SERVER_ID"));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.reqTransactionID, reqTxnId);//added S
    		
    		loyaltyTransactionTabDTO=loyalityCommonTransaction.loyaltyTransactionSetter(loyaltyTransactionTabDTO, loyaltyTransactionMap);//added S
			
			
			session.save(tableInfoDAO.getLoyaltyTransactionTable(loyaltyID.toString()),loyaltyTransactionTabDTO);
			transaction.commit();
			transaction = null;
		}catch (Exception e) {
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}finally{
			if(session != null){
				session.close();
				session = null;
			}
				
		}
	}
	
	private void insertDeleteSummaryInfo(DeleteAccountDTO dto,DeleteSummaryDTO deleteSmryDTO) {

		Session session = null;
		Transaction transaction = null;
		Date today=new Date();
		try{
			logger.info("Inserting into DELETE_SUMMARY_INFO Table >> LOYALTY ID::"+deleteSmryDTO.getLoyaltyID()+"|DATE::"+date+"|SUBSCRIBER NO::"+dto.getMoNumber()+"|ACCOUNT NO::"+deleteSmryDTO.getAccountNo()+"|ACCOUNT TYPE ID::"+deleteSmryDTO.getAccountTypeID()+"|TIER ID::"+deleteSmryDTO.getTierID()+"|CATEGORY::"+deleteSmryDTO.getCategory()+"|REWARD POINTS::"+deleteSmryDTO.getRewardPoints()+"|STATUS POINTS::"+deleteSmryDTO.getStatusPoints()+"|CHANNEL::"+dto.getChannel()+"|STATUS_ID::"+dto.getStatusId()+"|SERVER_ID::"+Cache.getCacheMap().get("SERVER_ID"));
			
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			deleteSmryDTO.setDeleteDate(today);
			deleteSmryDTO.setSubscriberNumber(""+dto.getMoNumber());
			deleteSmryDTO.setChannel(dto.getChannel());
			deleteSmryDTO.setDeleteKey(dto.getDeleteKey());
			deleteSmryDTO.setServerID(Cache.getCacheMap().get("SERVER_ID"));
			if(dto.getStatusId()!=null&&dto.getStatusId().equals("6"))
			{	
				deleteSmryDTO.setRemarks("Hard Deleted");
				deleteSmryDTO.setStatusID(Integer.parseInt(dto.getStatusId()));
			}
			else if(dto.getStatusId()!=null&&dto.getStatusId().equals("3"))
			{	
				deleteSmryDTO.setRemarks("Soft Deleted");
				deleteSmryDTO.setStatusID(Integer.parseInt(dto.getStatusId()));
			}
			else if(dto.getStatusId()!=null&&dto.getStatusId().equals("1"))
			{	
				deleteSmryDTO.setRemarks("Activated");
				
				deleteSmryDTO.setStatusID(Integer.parseInt(dto.getStatusId()));
			}
			else if(dto.getStatusId()!=null&&dto.getStatusId().equals("4"))
			{	
				deleteSmryDTO.setRemarks("InActivated");
				deleteSmryDTO.setStatusID(Integer.parseInt(dto.getStatusId()));
			}
			else
			{	
				deleteSmryDTO.setRemarks("Soft Deleted");
				deleteSmryDTO.setStatusID(3);
			}
			loyaltyTransactionTabDTO.setCreateTime(new Date());
			session.save(deleteSmryDTO);
			transaction.commit();
			transaction = null;
		}catch (Exception e) {
			e.printStackTrace();
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}finally{
			if(session != null){
				session.close();
				session = null;
			}
				
		}
	}

	private void insertDeleteSummaryInfo(TerminationServiceDTO dto,DeleteSummaryDTO deleteSmryDTO) {

		Session session = null;
		Transaction transaction = null;
		Date today=new Date();
		try{
			logger.info("Inserting into DELETE_SUMMARY_INFO Table >> LOYALTY ID::"+deleteSmryDTO.getLoyaltyID()+"|DATE::"+date+"|SUBSCRIBER NO::"+dto.getSubscriberNumber()+"|ACCOUNT NO::"+deleteSmryDTO.getAccountNo()+"|ACCOUNT TYPE ID::"+deleteSmryDTO.getAccountTypeID()+"|TIER ID::"+deleteSmryDTO.getTierID()+"|CATEGORY::"+deleteSmryDTO.getCategory()+"|REWARD POINTS::"+deleteSmryDTO.getRewardPoints()+"|STATUS POINTS::"+deleteSmryDTO.getStatusPoints()+"|CHANNEL::"+dto.getChannel()+"|STATUS_ID::6|SERVER_ID::"+Cache.getCacheMap().get("SERVER_ID"));
			
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			deleteSmryDTO.setDeleteDate(today);
			deleteSmryDTO.setSubscriberNumber(dto.getSubscriberNumber());
			deleteSmryDTO.setChannel(dto.getChannel());
			deleteSmryDTO.setDeleteKey(dto.getDeleteKey());
			deleteSmryDTO.setServerID(Cache.getCacheMap().get("SERVER_ID"));
				deleteSmryDTO.setRemarks("Hard Deleted");
				deleteSmryDTO.setStatusID(6);
			
			loyaltyTransactionTabDTO.setCreateTime(new Date());
			session.save(deleteSmryDTO);
			transaction.commit();
			transaction = null;
		}catch (Exception e) {
			e.printStackTrace();
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}finally{
			if(session != null){
				session.close();
				session = null;
			}
				
		}
	}

	public boolean deleteADSLTableDetails(String adslNumber,String statusid, String accountnumber) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			String tableName=tableInfoDAO.getADSLNumberDBTable(""+adslNumber);
			logger.info("ADSL NUMBER::"+adslNumber);
			transaction=session.beginTransaction();
			if(statusid!=null && statusid.equals("6"))
			{
				sql="DELETE FROM "+tableName+" where ADSL_NO = :loyaltyId";
				Query query = session.createSQLQuery(sql);				
			    query.setParameter("loyaltyId",adslNumber);
			    int rowCount = query.executeUpdate();
			    logger.info("Hard Deleted ::Rows affected: " + rowCount);
			    if(rowCount>0)
					done=true;		
			}
			else
			{
				sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where ADSL_NO = :loyaltyId";
				Query query = session.createSQLQuery(sql);
				if(statusid!=null)
					query.setParameter("statusId",statusid);
				else
					query.setParameter("statusId",3);
		        query.setParameter("updateDate",date);
		        query.setParameter("loyaltyId",adslNumber);
		        int rowCount = query.executeUpdate();
		        logger.info("Soft Deleted ::Rows affected: " + rowCount);
		        if(rowCount>0)
					done=true;		
			}
			
			transaction.commit();	
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
		
	}	
	
	public int checkLoyaltyIDRegisteredNumberDetails(Long loyaltyID,String subNum)
	{
		Session session=null;
		Transaction trx = null;
		int resultCount =0;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			String tableName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyID+"");
			
			Criteria criteria=session.createCriteria(tableName);
	        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        criteria.add(Restrictions.eq("loyaltyID",loyaltyID));
	        criteria.setProjection(Projections.rowCount());
	        criteria.uniqueResult();
	        resultCount = criteria.list().size();
			
			trx.commit();
			
		}catch (Exception e) {
			trx.rollback();
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		logger.info("Count::"+resultCount);
		return resultCount;
		
	}//getLoyaltyRegisteredNumberDetails
	
	public int checkLoyaltyRegisteredActiveDetails(Long loyaltyID,String accNum,String subNum)
	{
		Session session=null;
		Transaction trx = null;
		int resultCount=0;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			String tableName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyID+"");
			logger.info("TABLENAME::"+tableName);
			logger.info("LOYALTY ID::"+loyaltyID);
			logger.info("ACC NUM::"+accNum);
			
			Criteria criteria=session.createCriteria(tableName);
		    criteria.add(Restrictions.eq("loyaltyID",loyaltyID)); 
		    criteria.add(Restrictions.eq("statusID",1)); 
		    criteria.setProjection(Projections.rowCount());
		    criteria.uniqueResult();
	        resultCount = criteria.list().size();
		    trx.commit();
			
		}catch (Exception e) {
			trx.rollback();
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		logger.info("COUNT::"+resultCount);
		return resultCount;
		
	}//getLoyaltyRegisteredNumberDetails
	
	public int checkLoyaltyRegisteredNumberDetails(Long loyaltyID,String accNum,String subNum)
	{
		Session session=null;
		Transaction trx = null;
		int resultCount=0;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			String tableName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyID+"");
			Criteria criteria=session.createCriteria(tableName);
	        criteria.add(Restrictions.eq("loyaltyID",loyaltyID)); 
	        criteria.setProjection(Projections.rowCount());
	        criteria.uniqueResult();
	        resultCount = criteria.list().size();
			trx.commit();
			
		}catch (Exception e) {
			trx.rollback();
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		logger.info("COUNT::"+resultCount);
		
		return resultCount;
		
	}//getLoyaltyRegisteredNumberDetails
	
	
	public boolean deleteLoyaltyRegNumberTableDetails(DeleteAccountDTO dto,SubscriberNumberTabDTO subDto,AccountNumberTabDTO accDto,ADSLTabDTO adslDto,int cnt,int option) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		Query query = null;
		LoyaltyProfileTabDTO loyaltyProfileDTO=null;
		String tableName=null;
		try{
		session=HiberanteUtil.getSessionFactory().openSession();
		if(option==1)
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+subDto.getLoyaltyID());
		else if(option==2)
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+accDto.getLoyaltyID());
		else
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+adslDto.getLoyaltyID());
		if(subDto!=null)
		{
		loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(subDto.getLoyaltyID());
		logger.info("SUBSCRIBER NUMBER::"+dto.getMoNumber());
		deleteSmryDTO.setLoyaltyID(subDto.getLoyaltyID());
		deleteSmryDTO.setAccountNo(subDto.getAccountNumber());
		deleteSmryDTO.setAccountTypeID(subDto.getAccountTypeId());
		deleteSmryDTO.setCategory(loyaltyProfileDTO.getCategory());
		deleteSmryDTO.setTierID(loyaltyProfileDTO.getTierId());
		deleteSmryDTO.setRewardPoints(loyaltyProfileDTO.getRewardPoints());
		deleteSmryDTO.setStatusPoints(loyaltyProfileDTO.getStatusPoints());
		transaction=session.beginTransaction();
			if(dto.getStatusId()!=null && dto.getStatusId().equals("6"))
			{
				sql="DELETE FROM "+tableName+"  where LINKED_NO=:linkedNumber AND LOYALTY_ID=:loyaltyId";
				query = session.createSQLQuery(sql);				
				query.setParameter("linkedNumber",dto.getMoNumber());
				query.setParameter("loyaltyId",subDto.getLoyaltyID());
		        int rowCount = query.executeUpdate();
		        logger.info("Hard Deleted ::Rows affected: " + rowCount);
		        if(rowCount>0)
		        {	
		        	transaction.commit();		
		        	done=true;	
		         	insertTransactionDetails(dto.getMoNumber(), subDto.getLoyaltyID(), dto.getChannel(), subDto.getPoints(),6);
		         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
		         	deleteSubscriberTableDetails(dto.getMoNumber(),subDto.getLoyaltyID(),dto.getStatusId(),false);
		         	deleteAccountTableDetails(dto.getMoNumber(), subDto.getLoyaltyID(), null, dto.getStatusId());
		            if(cnt==1)
		        	 	deleteLoyaltyTableDetails(dto.getMoNumber(), subDto.getLoyaltyID(), dto.getStatusId(), dto.getChannel(), subDto.getPoints());
		            else if(dto.isPrimary())
		            	updatePrimaryNum(subDto.getLoyaltyID(),dto.getNumberList());
		        }
			}
			else
			{
				sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID=:loyaltyId AND LINKED_NO=:linkedNumber";
				query = session.createSQLQuery(sql);
				query.setParameter("statusId",dto.getStatusId());
				query.setParameter("updateDate",date);
				query.setParameter("loyaltyId",subDto.getLoyaltyID());
				query.setParameter("linkedNumber",dto.getMoNumber());
				int rowCount = query.executeUpdate();
				logger.info("Soft Deleted ::Rows affected: " + rowCount);
				logger.info(sql);		
				if(rowCount>0)
				{
					done=true;	
					transaction.commit();
					insertTransactionDetails(dto.getMoNumber(), subDto.getLoyaltyID(), dto.getChannel(), subDto.getPoints(),Integer.parseInt(dto.getStatusId()));
					insertDeleteSummaryInfo(dto,deleteSmryDTO);
					int count=checkLoyaltyRegisteredActiveDetails(subDto.getLoyaltyID(),subDto.getAccountNumber(),""+dto.getMoNumber());
					logger.info("ACTIVE COUNT::"+count);
					deleteSubscriberTableDetails(dto.getMoNumber(),subDto.getLoyaltyID(), dto.getStatusId(),false);
					if(count==0 || ( dto.getStatusId()!=null && !dto.getStatusId().equals("3")))	
					{
					deleteSubscriberTableDetails(dto.getMoNumber(),subDto.getLoyaltyID(), dto.getStatusId(),true);
					done=tabDAO.updateLoyalProfileDetails(subDto.getLoyaltyID(),new String[]{"STATUS_ID","STATUS_UPDATED_DATE"},new Object[]{ dto.getStatusId()!=null? dto.getStatusId():3,new Date()});
					deleteAccountTableDetails(dto.getMoNumber(), subDto.getLoyaltyID(), null, dto.getStatusId());
			        deleteNationalTableDetails(subDto.getLoyaltyID(), dto.getMoNumber(), dto.getStatusId());
					}
					 else if(dto.isPrimary())
				           updatePrimaryNum(subDto.getLoyaltyID(),dto.getNumberList());
						
				}
			}
			
		}
		else if(accDto!=null)
		{
			loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(accDto.getLoyaltyID());
			logger.info("ACCOUNT NUMBER::"+dto.getMoNumber());
			deleteSmryDTO.setLoyaltyID(accDto.getLoyaltyID());
			deleteSmryDTO.setAccountNo(accDto.getAccountNumber());
			deleteSmryDTO.setAccountTypeID(0);
			deleteSmryDTO.setCategory(loyaltyProfileDTO.getCategory());
			deleteSmryDTO.setTierID(loyaltyProfileDTO.getTierId());
			deleteSmryDTO.setRewardPoints(loyaltyProfileDTO.getRewardPoints());
			deleteSmryDTO.setStatusPoints(loyaltyProfileDTO.getStatusPoints());
			transaction=session.beginTransaction();
			int rowCount =0;
				if(dto.getStatusId()!=null && dto.getStatusId().equals("6"))
				{
						sql="DELETE FROM "+tableName+"  where ACCOUNT_NO =:accNum and LOYALTY_ID=:loyaltyId" ;
						query = session.createSQLQuery(sql);
						query.setParameter("accNum",dto.getMoNumber());
						query.setParameter("loyaltyId",accDto.getLoyaltyID());
						rowCount=query.executeUpdate();
						logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
			        {	
			        	done=true;	
			        	transaction.commit();
			         	insertTransactionDetails(dto.getMoNumber(), accDto.getLoyaltyID(), dto.getChannel(), accDto.getPoints(),6);
			         	insertDeleteSummaryInfo(dto, deleteSmryDTO);
			         	deleteAccountTableDetails(dto.getMoNumber(), accDto.getLoyaltyID(), accDto.getAccountNumber(), dto.getStatusId());
			            if(cnt==1)
			        	 	deleteLoyaltyTableDetails(dto.getMoNumber(), accDto.getLoyaltyID(), dto.getStatusId(), dto.getChannel(), accDto.getPoints());
			            else if(dto.isPrimary())
			            	updatePrimaryNum(accDto.getLoyaltyID(),dto.getNumberList());
			        }
				}
				else
				{
					sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where ACCOUNT_NO=:accNumber and LOYALTY_ID=:loyaltyID ";
					query = session.createSQLQuery(sql);
					query.setParameter("statusId",dto.getStatusId());
					query.setParameter("updateDate",date);
					query.setParameter("accNumber",dto.getMoNumber());
					query.setParameter("loyaltyID",accDto.getLoyaltyID());
					rowCount=query.executeUpdate();
					logger.info("Soft Deleted:Rows affected: " + rowCount);
					if(rowCount>0)
					{
						done=true;	
						transaction.commit();
						insertTransactionDetails(dto.getMoNumber(), accDto.getLoyaltyID(), dto.getChannel(), accDto.getPoints(),Integer.parseInt(dto.getStatusId()));
						insertDeleteSummaryInfo(dto,deleteSmryDTO);
						int count=checkLoyaltyRegisteredActiveDetails(accDto.getLoyaltyID(),accDto.getAccountNumber(),""+dto.getMoNumber());
						logger.info("ACTIVE COUNT::"+count);
						deleteAccountTableDetails(dto.getMoNumber(), accDto.getLoyaltyID(), accDto.getAccountNumber(),  dto.getStatusId() );
						if(count==0 ||  (dto.getStatusId()!=null&& !dto.getStatusId().equals("3")))	
							done=tabDAO.updateLoyalProfileDetails(accDto.getLoyaltyID(),new String[]{"STATUS_ID","STATUS_UPDATED_DATE"},new Object[]{ dto.getStatusId()!=null? dto.getStatusId():3,new Date()});
				        deleteNationalTableDetails(accDto.getLoyaltyID(), dto.getMoNumber(), dto.getStatusId());
		
					}
				}
		}
		else if(adslDto!=null)
		{
			loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(adslDto.getLoyaltyID());
			logger.info("LOYALTY NUMBER::"+dto.getMoNumber());
			deleteSmryDTO.setLoyaltyID(adslDto.getLoyaltyID());
			deleteSmryDTO.setAccountNo(adslDto.getAccountNumber());
			deleteSmryDTO.setAccountTypeID(adslDto.getAccountTypeId());
			deleteSmryDTO.setCategory(loyaltyProfileDTO.getCategory());
			deleteSmryDTO.setTierID(loyaltyProfileDTO.getTierId());
			deleteSmryDTO.setRewardPoints(loyaltyProfileDTO.getRewardPoints());
			deleteSmryDTO.setStatusPoints(loyaltyProfileDTO.getStatusPoints());
			transaction=session.beginTransaction();
				if(dto.getStatusId()!=null && dto.getStatusId().equals("6"))
				{
					sql="DELETE FROM "+tableName+"  where LINKED_NO=:linkedNumber and (LOYALTY_ID=:loyaltyId or ACCOUNT_NO =:accNum)";
					query = session.createSQLQuery(sql);				
					query.setParameter("linkedNumber",dto.getMoNumber());
					query.setParameter("loyaltyId",adslDto.getLoyaltyID());
					query.setParameter("accNum",adslDto.getAccountNumber());
			        int rowCount = query.executeUpdate();
			        logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
			        {	
			        	done=true;	
			         	insertTransactionDetails(dto.getMoNumber(), adslDto.getLoyaltyID(), dto.getChannel(), adslDto.getPoints(),6);
			         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
			         	deleteADSLTableDetails(""+dto.getMoNumber(), dto.getStatusId(),adslDto.getAccountNumber());
			            if(cnt==1)
			        	 	deleteLoyaltyTableDetails(dto.getMoNumber(), adslDto.getLoyaltyID(), dto.getStatusId(), dto.getChannel(), adslDto.getPoints());
			            else if(dto.isPrimary())
			            	updatePrimaryNum(adslDto.getLoyaltyID(),dto.getNumberList());
			        }
				}
				else
				{
					sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID=:loyaltyId AND LINKED_NO=:linkedNumber";
					query = session.createSQLQuery(sql);
					query.setParameter("statusId",dto.getStatusId());
					query.setParameter("updateDate",date);
					query.setParameter("loyaltyId",adslDto.getLoyaltyID());
					query.setParameter("linkedNumber",dto.getMoNumber());
					int rowCount = query.executeUpdate();
					System.out.println("Rows affected: " + rowCount);
					logger.info(sql);		
					if(rowCount>0)
					{
						done=true;	
						transaction.commit();	
						insertTransactionDetails(dto.getMoNumber(), adslDto.getLoyaltyID(), dto.getChannel(), adslDto.getPoints(),Integer.parseInt(dto.getStatusId()));
						insertDeleteSummaryInfo(dto,deleteSmryDTO);
						int count=checkLoyaltyRegisteredActiveDetails(adslDto.getLoyaltyID(),adslDto.getAccountNumber(),""+dto.getMoNumber());
						logger.info("ACTIVE COUNT::"+count);
						deleteADSLTableDetails(""+dto.getMoNumber(), dto.getStatusId(),adslDto.getAccountNumber());
						if(count==0 || ( dto.getStatusId()!=null && !dto.getStatusId().equals("3")))	
						{
						done=tabDAO.updateLoyalProfileDetails(adslDto.getLoyaltyID(),new String[]{"STATUS_ID","STATUS_UPDATED_DATE"},new Object[]{ dto.getStatusId()!=null? dto.getStatusId():3,new Date()});
						deleteAccountTableDetails(dto.getMoNumber(), adslDto.getLoyaltyID(), null, dto.getStatusId());
				        deleteNationalTableDetails(adslDto.getLoyaltyID(), dto.getMoNumber(), dto.getStatusId());
						}
					}
				}
		}
			
			}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
	
	}
	public boolean deleteLoyaltyRegNumberTableDetails(TerminationServiceDTO dto,SubscriberNumberTabDTO subDto,AccountNumberTabDTO accDto,ADSLTabDTO adslDto,int cnt,int option) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		Query query = null;
		LoyaltyProfileTabDTO loyaltyProfileDTO=null;
		String tableName=null;
		try{
		session=HiberanteUtil.getSessionFactory().openSession();
		if(option==1)
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+subDto.getLoyaltyID());
		else if(option==2)
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+accDto.getLoyaltyID());
		else
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+adslDto.getLoyaltyID());
		if(subDto!=null)
		{
		loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(subDto.getLoyaltyID());
		logger.info("SUBSCRIBER NUMBER::"+dto.getSubscriberNumber());
		deleteSmryDTO.setLoyaltyID(subDto.getLoyaltyID());
		deleteSmryDTO.setAccountNo(subDto.getAccountNumber());
		deleteSmryDTO.setAccountTypeID(subDto.getAccountTypeId());
		deleteSmryDTO.setCategory(loyaltyProfileDTO.getCategory());
		deleteSmryDTO.setTierID(loyaltyProfileDTO.getTierId());
		deleteSmryDTO.setRewardPoints(loyaltyProfileDTO.getRewardPoints());
		deleteSmryDTO.setStatusPoints(loyaltyProfileDTO.getStatusPoints());
		transaction=session.beginTransaction();
			
				sql="DELETE FROM "+tableName+"  where LINKED_NO=:linkedNumber AND LOYALTY_ID=:loyaltyId";
				query = session.createSQLQuery(sql);				
				query.setParameter("linkedNumber",dto.getSubscriberNumber());
				query.setParameter("loyaltyId",subDto.getLoyaltyID());
		        int rowCount = query.executeUpdate();
		        logger.info("Hard Deleted ::Rows affected: " + rowCount);
		        if(rowCount>0)
		        {	
		        	transaction.commit();		
		        	done=true;	
		         	insertTransactionDetails(Long.parseLong(dto.getSubscriberNumber()), subDto.getLoyaltyID(), dto.getChannel(), subDto.getPoints(),6);
		         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
		         	deleteSubscriberTableDetails(Long.parseLong(dto.getSubscriberNumber()),subDto.getLoyaltyID(),"6",false);
		            if(cnt==1)
		        	 	deleteLoyaltyTableDetails(Long.parseLong(dto.getSubscriberNumber()), subDto.getLoyaltyID(), "6", dto.getChannel(), subDto.getPoints());
		            else if(dto.isPrimary())
		            	updatePrimaryNum(subDto.getLoyaltyID(),dto.getNumberList());
		        }
			
		}
		else if(accDto!=null)
		{
			loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(accDto.getLoyaltyID());
			logger.info("ACCOUNT NUMBER::"+dto.getSubscriberNumber());
			deleteSmryDTO.setLoyaltyID(accDto.getLoyaltyID());
			deleteSmryDTO.setAccountNo(accDto.getAccountNumber());
			deleteSmryDTO.setAccountTypeID(0);
			deleteSmryDTO.setCategory(loyaltyProfileDTO.getCategory());
			deleteSmryDTO.setTierID(loyaltyProfileDTO.getTierId());
			deleteSmryDTO.setRewardPoints(loyaltyProfileDTO.getRewardPoints());
			deleteSmryDTO.setStatusPoints(loyaltyProfileDTO.getStatusPoints());
			transaction=session.beginTransaction();
			int rowCount =0;
				
						sql="DELETE FROM "+tableName+"  where ACCOUNT_NO =:accNum and LOYALTY_ID=:loyaltyId" ;
						query = session.createSQLQuery(sql);
						query.setParameter("accNum",dto.getSubscriberNumber());
						query.setParameter("loyaltyId",accDto.getLoyaltyID());
						rowCount=query.executeUpdate();
						logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
			        {	
			        	done=true;	
			        	transaction.commit();
			         	insertTransactionDetails(Long.parseLong(dto.getSubscriberNumber()), accDto.getLoyaltyID(), dto.getChannel(), accDto.getPoints(),6);
			         	insertDeleteSummaryInfo(dto, deleteSmryDTO);
			         	deleteAccountTableDetails(Long.parseLong(dto.getSubscriberNumber()), accDto.getLoyaltyID(), accDto.getAccountNumber(),"6");
			            if(cnt==1)
			        	 	deleteLoyaltyTableDetails(Long.parseLong(dto.getSubscriberNumber()), accDto.getLoyaltyID(), "6", dto.getChannel(), accDto.getPoints());
			            else if(dto.isPrimary())
			            	updatePrimaryNum(accDto.getLoyaltyID(),dto.getNumberList());
			        }
				
		}
		else if(adslDto!=null)
		{
			loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(adslDto.getLoyaltyID());
			logger.info("LOYALTY NUMBER::"+dto.getSubscriberNumber());
			deleteSmryDTO.setLoyaltyID(adslDto.getLoyaltyID());
			deleteSmryDTO.setAccountNo(adslDto.getAccountNumber());
			deleteSmryDTO.setAccountTypeID(adslDto.getAccountTypeId());
			deleteSmryDTO.setCategory(loyaltyProfileDTO.getCategory());
			deleteSmryDTO.setTierID(loyaltyProfileDTO.getTierId());
			deleteSmryDTO.setRewardPoints(loyaltyProfileDTO.getRewardPoints());
			deleteSmryDTO.setStatusPoints(loyaltyProfileDTO.getStatusPoints());
			transaction=session.beginTransaction();
			
					sql="DELETE FROM "+tableName+"  where LINKED_NO=:linkedNumber and (LOYALTY_ID=:loyaltyId or ACCOUNT_NO =:accNum)";
					query = session.createSQLQuery(sql);				
					query.setParameter("linkedNumber",dto.getSubscriberNumber());
					query.setParameter("loyaltyId",adslDto.getLoyaltyID());
					query.setParameter("accNum",adslDto.getAccountNumber());
			        int rowCount = query.executeUpdate();
			        logger.info("Hard Deleted ::Rows affected: " + rowCount);
			        if(rowCount>0)
			        {	
			        	done=true;	
			         	insertTransactionDetails(Long.parseLong(dto.getSubscriberNumber()), adslDto.getLoyaltyID(), dto.getChannel(), adslDto.getPoints(),6);
			         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
			         	deleteADSLTableDetails(dto.getSubscriberNumber(), "6",adslDto.getAccountNumber());
			            if(cnt==1)
			        	 	deleteLoyaltyTableDetails(Long.parseLong(dto.getSubscriberNumber()), adslDto.getLoyaltyID(),"6", dto.getChannel(), adslDto.getPoints());
			            else if(dto.isPrimary())
			            	updatePrimaryNum(adslDto.getLoyaltyID(),dto.getNumberList());
			        }
				
		}
			
			}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
	
	}
	
      private void updatePrimaryNum(Long loyaltyID, List<LoyaltyRegisteredNumberTabDTO> numberList) {
    	  
    	Transaction transaction=null;		
  		Session session=null;
  		String sql=null;
  		Query query = null;
  		String tableName=null;
  		String contactNumber=null;
  		String prepaid=null;
  		String postpaid=null;
  		String pstn=null;
  		String pstnBasic=null;
  		String others=null;
			try{
				session=HiberanteUtil.getSessionFactory().openSession();
				transaction=session.beginTransaction();
				
				for(LoyaltyRegisteredNumberTabDTO loyalDTO:numberList)
				{
					if(loyalDTO.getAccountTypeId()==14)
						prepaid=loyalDTO.getLinkedNumber();
					else if(loyalDTO.getAccountTypeId()==9)
						postpaid=loyalDTO.getLinkedNumber();
					else if(loyalDTO.getAccountTypeId()==200)
						pstn=loyalDTO.getLinkedNumber();
					else if(loyalDTO.getAccountTypeId()==201)
						pstnBasic=loyalDTO.getLinkedNumber();
					else
						others="0";
				}
				
				logger.info("PREPAID:::"+prepaid+":::POSTPAID:::"+postpaid+":::PSTN:::"+pstn+":::PSTN BASIC:::"+pstnBasic+":::OTHERS:::"+others);
				
				if(prepaid!=null)
					contactNumber=prepaid;
				else if(postpaid!=null)
					contactNumber=postpaid;
				else if(pstn!=null)
					contactNumber=pstn;
				else if(pstnBasic!=null)
					contactNumber=pstnBasic;
				else
					contactNumber=others;
				logger.info("NEW CONTACT NUMBER:::"+contactNumber);
				tableName=tableInfoDAO.getLoyaltyProfileDBTable(""+loyaltyID);
  				sql="UPDATE "+tableName+" set CONTACT_NUMBER = :contactNumber where LOYALTY_ID=:loyaltyId";
  				query = session.createSQLQuery(sql);
  				query.setParameter("contactNumber",contactNumber);
  				query.setParameter("loyaltyId",loyaltyID);
  				int rowCount = query.executeUpdate();
  				logger.info(sql);	
  				logger.info("Primary Number updated: " + rowCount);
  				if(rowCount>0)
  					transaction.commit();
				}
				catch (Exception e) {
					logger.error("Exception Occured::"+e.getLocalizedMessage());
				}
  		finally{
  			
  		}
  		
      }

	public boolean deleteLoyaltyIDRegNumberTableDetails(List<LoyaltyRegisteredNumberTabDTO> numberList,DeleteAccountDTO dto,LoyaltyProfileTabDTO loyaltyDTO,int cnt,int option) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		Query query =null;
		boolean all=false;
		int rowCount =0;
		try{
		session=HiberanteUtil.getSessionFactory().openSession();
		String tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+loyaltyDTO.getLoyaltyID());
		logger.info("NUMBER::"+dto.getMoNumber());
		logger.info("LOYALTY ID::"+loyaltyDTO.getLoyaltyID());
		deleteSmryDTO.setLoyaltyID(loyaltyDTO.getLoyaltyID());
		deleteSmryDTO.setCategory(loyaltyDTO.getCategory());
		deleteSmryDTO.setTierID(loyaltyDTO.getTierId());
		deleteSmryDTO.setRewardPoints(loyaltyDTO.getRewardPoints());
		deleteSmryDTO.setStatusPoints(loyaltyDTO.getStatusPoints());
		transaction=session.beginTransaction();
			if(dto.getStatusId()!=null && dto.getStatusId().equals("6"))
			{
				if(dto.getMoNumber().equals(loyaltyDTO.getLoyaltyID()))
				{
					if(dto.getPin().equalsIgnoreCase(loyaltyDTO.getPin()))
					{
						sql="DELETE FROM "+tableName+"  where LOYALTY_ID=:loyaltyId";
						query = session.createSQLQuery(sql);				
						query.setParameter("loyaltyId",loyaltyDTO.getLoyaltyID());
						rowCount = query.executeUpdate();
						logger.info("Hard Deleted ::Rows affected: " + rowCount);
						all=true;
					}
					else
					{
						dto.setValidate(1);
						dto.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+dto.getLangId()).getStatusCode());
						dto.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+dto.getLangId()).getStatusDesc());
						throw new CommonException(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+dto.getLangId()).getStatusDesc());
					}
				}
		        if(rowCount>0)
		        {	
		        	done=true;	
		        	for(LoyaltyRegisteredNumberTabDTO tab:numberList)
		        	{
		         	insertTransactionDetails(Long.parseLong(tab.getLinkedNumber()), loyaltyDTO.getLoyaltyID(), dto.getChannel(), loyaltyDTO.getRewardPoints(),6);
		         	dto.setMoNumber(Long.parseLong(tab.getLinkedNumber()));
		         	deleteSmryDTO.setAccountNo(tab.getAccountNumber());
		    		deleteSmryDTO.setAccountTypeID(tab.getAccountTypeId());
		         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
		        	}
		         	if(all)
		        	deleteLoyaltyTableDetails(dto.getMoNumber(), loyaltyDTO.getLoyaltyID(),dto.getStatusId(),dto.getChannel(), loyaltyDTO.getRewardPoints());
		        }
			}
			else
			{
				sql="UPDATE "+tableName+" set STATUS_ID = :statusId ,STATUS_UPDATED_DATE =:updateDate where LOYALTY_ID=:loyaltyId";
				query = session.createSQLQuery(sql);
				query.setParameter("statusId",dto.getStatusId());
				query.setParameter("updateDate",date);
				query.setParameter("loyaltyId",loyaltyDTO.getLoyaltyID());
				rowCount = query.executeUpdate();
				logger.info("Soft Deleted:Rows affected: " + rowCount);
				if(rowCount>0)
				{
					done=true;	
					transaction.commit();	
					for(LoyaltyRegisteredNumberTabDTO tab:numberList)
		        	{
		         	insertTransactionDetails(Long.parseLong(tab.getLinkedNumber()),loyaltyDTO.getLoyaltyID(), dto.getChannel(), loyaltyDTO.getRewardPoints(),Integer.parseInt(dto.getStatusId()));
		         	dto.setMoNumber(Long.parseLong(tab.getLinkedNumber()));
		        	deleteSmryDTO.setAccountNo(tab.getAccountNumber());
		    		deleteSmryDTO.setAccountTypeID(tab.getAccountTypeId());
		         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
		        	}
					int count=checkLoyaltyRegisteredActiveDetails(loyaltyDTO.getLoyaltyID(),null,""+dto.getMoNumber());
					if(count==0 || ( dto.getStatusId()!=null&& !dto.getStatusId().equalsIgnoreCase("3")))	
					{
					done=tabDAO.updateLoyalProfileDetails(loyaltyDTO.getLoyaltyID(),new String[]{"STATUS_ID","STATUS_UPDATED_DATE"},new Object[]{dto.getStatusId()!=null?dto.getStatusId():3,new Date()});
					deleteSubscriberTableDetails(dto.getMoNumber(),loyaltyDTO.getLoyaltyID(), dto.getStatusId(),true);
					deleteAccountTableDetails(dto.getMoNumber(), loyaltyDTO.getLoyaltyID(), null, dto.getStatusId());
			        deleteNationalTableDetails(loyaltyDTO.getLoyaltyID(), dto.getMoNumber(), dto.getStatusId());
					}
				}
			}
			
			}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
	
	}
	public boolean deleteLoyaltyIDRegNumberTableDetails(List<LoyaltyRegisteredNumberTabDTO> numberList,TerminationServiceDTO dto,LoyaltyProfileTabDTO loyaltyDTO,int cnt,int option) {
		Transaction transaction=null;		
		Session session=null;
		boolean done=false;
		String sql=null;
		Query query =null;
		boolean all=false;
		int rowCount =0;
		try{
		session=HiberanteUtil.getSessionFactory().openSession();
		String tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(""+loyaltyDTO.getLoyaltyID());
		logger.info("NUMBER::"+dto.getSubscriberNumber());
		logger.info("LOYALTY ID::"+loyaltyDTO.getLoyaltyID());
		deleteSmryDTO.setLoyaltyID(loyaltyDTO.getLoyaltyID());
		deleteSmryDTO.setCategory(loyaltyDTO.getCategory());
		deleteSmryDTO.setTierID(loyaltyDTO.getTierId());
		deleteSmryDTO.setRewardPoints(loyaltyDTO.getRewardPoints());
		deleteSmryDTO.setStatusPoints(loyaltyDTO.getStatusPoints());
		transaction=session.beginTransaction();
			
				if(dto.getSubscriberNumber().equals(loyaltyDTO.getLoyaltyID()))
				{
					
						sql="DELETE FROM "+tableName+"  where LOYALTY_ID=:loyaltyId";
						query = session.createSQLQuery(sql);				
						query.setParameter("loyaltyId",loyaltyDTO.getLoyaltyID());
						rowCount = query.executeUpdate();
						logger.info("Hard Deleted ::Rows affected: " + rowCount);
						all=true;
					
				}
		        if(rowCount>0)
		        {	
		        	done=true;	
		        	for(LoyaltyRegisteredNumberTabDTO tab:numberList)
		        	{
		         	insertTransactionDetails(Long.parseLong(tab.getLinkedNumber()), loyaltyDTO.getLoyaltyID(), dto.getChannel(), loyaltyDTO.getRewardPoints(),6);
		         	dto.setSubscriberNumber(tab.getLinkedNumber());
		         	deleteSmryDTO.setAccountNo(tab.getAccountNumber());
		    		deleteSmryDTO.setAccountTypeID(tab.getAccountTypeId());
		         	insertDeleteSummaryInfo(dto,deleteSmryDTO);
		        	}
		         	if(all)
		        	deleteLoyaltyTableDetails(Long.parseLong(dto.getSubscriberNumber()), loyaltyDTO.getLoyaltyID(),"6",dto.getChannel(), loyaltyDTO.getRewardPoints());
		        }
			
			
			
			}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return done;
	
	}
	public List<LoyaltyRegisteredNumberTabDTO> getLinkedNumbers(Long loyaltyID,Long subNumber) {
		Session session=null;	
		Transaction transaction = null;
		String sql="";
		String tableName="";
		List<LoyaltyRegisteredNumberTabDTO> loyaltyRegDTO=null;
		LoyaltyRegisteredNumberTabDTO loyalDTO=null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID+"");
			sql="SELECT LINKED_NO,ACCOUNT_TYPE from "+tableName+" WHERE LOYALTY_ID=? AND LINKED_NO<>?";
			
			logger.info("Query:>>"+sql);
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,loyaltyID);
			query.setParameter(1,subNumber);
			
			logger.info("LOYALTY ID::"+loyaltyID);
			
			List<Object[]> list=query.list();
			loyaltyRegDTO=new ArrayList<LoyaltyRegisteredNumberTabDTO>();
			for(Object[] obj:list)
			{			
				loyalDTO=new LoyaltyRegisteredNumberTabDTO();
				loyalDTO.setLinkedNumber(obj[0]==null?"0":obj[0].toString());
				loyalDTO.setAccountTypeId(obj[1]==null?0:Integer.parseInt(obj[1].toString()));
				loyaltyRegDTO.add(loyalDTO);			
			}	
			transaction.commit();

		}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(transaction != null)
					transaction.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
		return loyaltyRegDTO;
	}
	
	public Long getPrimaryNumber(Long loyaltyID) {
		Session session=null;	
		Transaction tx = null;
		Criteria criteria=null;
		List finallist=null;
		Long cntNum = null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			long t1=System.currentTimeMillis();
			
			String tableName=tableInfoDAO.getLoyaltyProfileTable(loyaltyID+"");
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("loyaltyID", loyaltyID));
			criteria.setProjection(Projections.property("contactNumber"));
			finallist=criteria.list();
			
			for(int i=0;i<finallist.size();i++)
				cntNum=Long.parseLong(finallist.get(0)+"");
			tx.commit();
			long t2 = System.currentTimeMillis();
			logger.info("Time Taken for Executing DB operation "+(t2-t1));
		
			}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(tx != null)
					tx.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
			
			logger.info("PRIMARY NUMBER:::"+cntNum);
		return cntNum;
	}

	public List<LoyaltyRegisteredNumberTabDTO> getLineNumbers(Long loyaltyID) {
		Session session=null;	
		Transaction transaction = null;
		String sql="";
		String tableName="";
		List<LoyaltyRegisteredNumberTabDTO> loyaltyRegDTO=null;
		LoyaltyRegisteredNumberTabDTO loyalDTO=null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			tableName=tableInfoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID+"");
			sql="SELECT LINKED_NO,ACCOUNT_TYPE from "+tableName+" WHERE LOYALTY_ID=? ";
			
			logger.info("Query:>>"+sql);
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,loyaltyID);			
			logger.info("LOYALTY ID::"+loyaltyID);
			
			List<Object[]> list=query.list();
			loyaltyRegDTO=new ArrayList<LoyaltyRegisteredNumberTabDTO>();
			for(Object[] obj:list)
			{			
				loyalDTO=new LoyaltyRegisteredNumberTabDTO();
				loyalDTO.setLinkedNumber(obj[0]==null?"0":obj[0].toString());
				loyalDTO.setAccountTypeId(obj[1]==null?0:Integer.parseInt(obj[1].toString()));
				loyaltyRegDTO.add(loyalDTO);			
			}	
			transaction.commit();
		}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(transaction != null)
					transaction.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
		return loyaltyRegDTO;
	}
	
}
