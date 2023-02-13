package com.sixdee.imp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.OnmRedemptionTransactionDTO;
import com.sixdee.imp.dto.RedemptionDTO;


public class ReserveAndCommitDAO {
	
	private static final Logger logger=LogManager.getLogger(ReserveAndCommitDAO.class);

	public boolean checkonmRedemptionDetails(String transactionId, String loyaltyId) {
		
		Session session=null;
		Transaction txn=null;
		Long t1=System.currentTimeMillis();
		Boolean ispresent=false;
		String tableName=null;
		
		try {
			
			session=HiberanteUtil.getSessionFactory().openSession();
			txn=session.beginTransaction();
			
			tableName=getOnmTransactionTableName(loyaltyId+"");
			
			String hql="from " + tableName + " where loyaltyId=:loyaltyId and transactionId=:transactionId";
			
			Query query=session.createQuery(hql);
			query.setParameter("loyaltyId", loyaltyId);
			query.setParameter("transactionId", transactionId);
			List result=query.list();
			if(result!=null && result.size()>0) {
				logger.debug(" list size :: "+result.size());
				ispresent=true;
			}
			txn.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		
		
		}
		
		
		
		
		
		
		
		
		return ispresent;
	}

	private String getOnmTransactionTableName(String loyaltyID) {
	

		String tablePrefix="LMS_ONM_REDEEM_TXN";
		//logger.info(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH"));
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
			tableName="_"+loyaltyID.substring(loyaltyID.length()-index);
		//logger.info(tablePrefix+tableName);
		return tablePrefix+tableName;

	
		//return "LMS_ONM_REDEEM_TXN_"+String.format("%02d",Calendar.getInstance().get(Calendar.MONTH)+1);
	}

	public Boolean updatePointDetails(String txnId, LoyaltyProfileTabDTO loyaltyProfileTabDTO) {
		   Session session=null;
	        String hql=null;
	        Transaction transaction=null;
	        Query query =null;
	        boolean isUpdate = false;
	        long t1 = System.currentTimeMillis();
		
		
		try {
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			String tableName = "LOYALTY_PROFILE_ENTITY_0";
			hql="UPDATE "+tableName+" SET statusUpdatedDate=:statusUpdatedDate,rewardPoints=rewardPoints-:rewardPoints,tierPoints=tierPoints-:tierPoints,reservePoints =reservePoints+:reservePoints "
					+ "WHERE loyaltyID=:loyaltyID ";
			query=session.createQuery(hql);
			query.setParameter("tierPoints", loyaltyProfileTabDTO.getReservePoints());
			query.setParameter("rewardPoints", loyaltyProfileTabDTO.getReservePoints());
			query.setParameter("reservePoints", loyaltyProfileTabDTO.getReservePoints());
			query.setParameter("statusUpdatedDate", loyaltyProfileTabDTO.getStatusUpdatedDate());
			query.setParameter("loyaltyID", loyaltyProfileTabDTO.getLoyaltyID());

			
			if(query.executeUpdate()>0){
                logger.debug(">>successfully updated loyaltyProfileTabDTO");
                isUpdate= true;
            }else{
                logger.info(">>updation failed>>");
              }
            transaction.commit();
            
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		 finally{
	            if(session!=null && session.isOpen()){
	                session.close();
	                session=null;
	            }
	            logger.info(" Transaction id :" + txnId + " Total Time To UpdatePointDetails :" +(System.currentTimeMillis()-t1));
	        }
		
		return isUpdate;
	}

	public String getLoyaltyProfileDBTable(String loyaltyNumber)
	{
		String tablePrefix="LOYALTY_PROFILE";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyNumber.substring(loyaltyNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}

	public boolean inserOnmRedemptionTransaction(OnmRedemptionTransactionDTO onmRedemptionTransactionDTO) {
		boolean flag=false;
		String tableName=null;
	
		Session session =null;
		Transaction transaction =null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			tableName=getOnmTransactionTableName(String.valueOf(onmRedemptionTransactionDTO.getLoyaltyId()));
			logger.info(">>>table name>>>"+tableName);
			session.save(tableName,onmRedemptionTransactionDTO);
			transaction.commit();
			flag=true;
			logger.info(">>transaction inserted>>>");
		}catch (Exception e) {
			if(transaction != null)
				transaction.rollback();
			flag=false;
			e.printStackTrace();
			throw e;

		}finally{

			

			if(session != null && session.isOpen() ){
				session.close();
				session=null;
			}
		}

		return flag;

	}

	public OnmRedemptionTransactionDTO getonmRedemptionDetails(String txnId, Long loyaltyID, int redeemPointReserveStatus) {
		OnmRedemptionTransactionDTO onmRedemptionTransactionDTO = null;
		Session session = null;
		Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		long t1 = System.currentTimeMillis();
		try {
			logger.info(" Transaction Id :" + txnId +" getonmRedemptionDetails with Loyaltyid :" +loyaltyID + " Status :" + redeemPointReserveStatus  );
			session=HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			String tablename = getOnmTransactionTableName(loyaltyID + "");
			String hql = " from " + tablename + " where loyaltyId =:loyaltyId and transactionId =:transactionId and status =:status ";
			
			Query query=session.createQuery(hql);
            query.setParameter("loyaltyId", String.valueOf(loyaltyID));
            query.setParameter("status", redeemPointReserveStatus);
            query.setParameter("transactionId", txnId);
            List results = query.list();
			
			if(results != null && results.size()>0){
				logger.debug(" list size :: "+results.size());
				onmRedemptionTransactionDTO = (OnmRedemptionTransactionDTO) results.get(0);
			}
			trx.commit();
		}catch(Exception e) {
			if(trx != null)
				e.printStackTrace();
		}finally {
			if(session != null && session.isOpen()) {
				session.close();
				session = null;
			}
			 logger.info(" Transaction id :" + txnId + " Total Time To getonmRedemptionDetails :" +(System.currentTimeMillis()-t1));
			infoDAO = null;
		}
		return onmRedemptionTransactionDTO;
	}

	public Boolean rollbackPointDetails(String txnId, LoyaltyProfileTabDTO loyaltyProfileTabDTO) {
        Session session=null;
        String hql=null;
        Transaction transaction=null;
        Query query =null;
        boolean isUpdate = false;
        long t1 = System.currentTimeMillis();
        try{
            
            session=HiberanteUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
        	//String tableName = getLoyaltyProfileDBTable(loyaltyProfileTabDTO.getLoyaltyID() + "");
        	String tableName = "LOYALTY_PROFILE_ENTITY_0";
            hql="UPDATE "+ tableName +" SET statusUpdatedDate=:statusUpdatedDate,rewardPoints=rewardPoints+:rewardPoints,tierPoints=tierPoints+:tierPoints,reservePoints =reservePoints-:reservePoints "
                    + "WHERE loyaltyID =:loyaltyID ";
            query=session.createQuery(hql);
            query.setParameter("tierPoints", loyaltyProfileTabDTO.getRewardPoints());
            query.setParameter("rewardPoints", loyaltyProfileTabDTO.getRewardPoints());
            query.setParameter("statusUpdatedDate", loyaltyProfileTabDTO.getStatusUpdatedDate());
            query.setParameter("reservePoints", loyaltyProfileTabDTO.getRewardPoints());
            query.setParameter("loyaltyID", loyaltyProfileTabDTO.getLoyaltyID());

            if(query.executeUpdate()>0){
                logger.debug(">>successfully updated PointDetailsTabDTO");
                isUpdate= true;
            }else{
                logger.info(">>updation failed>>");
              }
            transaction.commit();

        }
        catch(HibernateException he)
        {
            if(transaction!=null){
                transaction.rollback();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(session!=null && session.isOpen()){
                session.close();
                session=null;
            }
            logger.info(" Transaction id :" + txnId + " Total Time To UpdatePointDetails :" +(System.currentTimeMillis()-t1));
        }
        return isUpdate;
    }

	public Boolean updateOnmRedemptionTransaction(String txnId, OnmRedemptionTransactionDTO onmRedemptionTransactionDTO) {
		logger.info("***** updateOnmRedemptionTransaction*****");
		Session session = null;
		String hql = null;
		boolean flag = false;
		Transaction transaction = null;
		Query query = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		long t1 = System.currentTimeMillis();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String tablename = getOnmTransactionTableName(onmRedemptionTransactionDTO.getLoyaltyId() + "");
			hql = "UPDATE " + tablename + " SET status=:status, updateTime=:updateTime WHERE loyaltyId=:loyaltyId and transactionId=:transactionId";
			query = session.createQuery(hql);
			query.setParameter("status", onmRedemptionTransactionDTO.getStatus());
			query.setParameter("updateTime", new Date());
			query.setParameter("loyaltyId", onmRedemptionTransactionDTO.getLoyaltyId());
			query.setParameter("transactionId", txnId);

			if (query.executeUpdate() > 0) {
				transaction.commit();
				flag = true;
			} else {
				flag = false;
			}

		} catch (HibernateException he) {
			flag = false;
			if (transaction != null) {
				transaction.rollback();
			}
			throw he;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
			logger.info(" Transaction id :" + txnId + " Total Time To updateOnmRedemptionTransaction :" +(System.currentTimeMillis()-t1));

		}
		return flag;
	}

	public Boolean commitpointDetails(String txnId, LoyaltyProfileTabDTO loyaltyProfileTabDTO) {
        Session session=null;
        String hql=null;
        Transaction transaction=null;
        Query query =null;
        boolean isUpdate = false;
        long t1 = System.currentTimeMillis();
        try{
            logger.info(" Transaction Id :" + txnId +"commitpointDetails :: Points " + loyaltyProfileTabDTO.getRewardPoints() );
            session=HiberanteUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String tableName = "LOYALTY_PROFILE_ENTITY_0";
            hql="UPDATE "+tableName+" SET  statusUpdatedDate=:statusUpdatedDate,reservePoints =reservePoints-:reservePoints "
                    + "WHERE loyaltyID =:loyaltyID ";
            query=session.createQuery(hql);
            //query.setParameter("tierPoints", loyaltyProfileTabDTO.getReservePoints());
            query.setParameter("statusUpdatedDate", loyaltyProfileTabDTO.getStatusUpdatedDate());
            query.setParameter("reservePoints", loyaltyProfileTabDTO.getReservePoints());
            query.setParameter("loyaltyID", loyaltyProfileTabDTO.getLoyaltyID());

            if(query.executeUpdate()>0){
                logger.debug(">>successfully updated PointDetailsTabDTO");
                isUpdate= true;
            }else{
                logger.info(">>updation failed>>");
              }
            transaction.commit();

        }
        catch(HibernateException he)
        {
        	he.printStackTrace();
            if(transaction!=null){
                transaction.rollback();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(session!=null && session.isOpen()){
                session.close();
                session=null;
            }
            logger.info(" Transaction id :" + txnId + " Total Time To UpdatePointDetails :" +(System.currentTimeMillis()-t1));
        }
        return isUpdate;
    }
	

	public LoyaltyProfileTabDTO checkForPointDetails(Long loyaltyID) {
		Session session = null;
		Transaction trx = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO= null;

		try {
			logger.info("Inside checkForPointDetails For LoyaltyId ::"+loyaltyID );
			session=HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			Criteria criteria=session.createCriteria(LoyaltyProfileTabDTO.class);
			criteria.add(Restrictions.eq("loyaltyId",loyaltyID));
			
			List<LoyaltyProfileTabDTO> list =criteria.list();
			logger.info(" LoyaltyProfileTabDTO list size :: "+list.size());
			if(list.size()>0 && list!= null){

				loyaltyProfileTabDTO =list.get(0);

			}
			trx.commit();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {

			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
		return loyaltyProfileTabDTO;
	}
	
	@SuppressWarnings("unchecked")
	public List<RedemptionDTO> getOnmTrasactionDetails(int status) {

		Session session = null;
		Transaction transaction = null;
		Query query =null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSSSSSSSS");
		List<Object[]>list = null;
		RedemptionDTO redemptionDTO = null;
		List<RedemptionDTO>redemptionList = null;
		try {
			
			  redemptionList = new ArrayList<>();
              session = HiberanteUtil.getSessionFactory().openSession(); 
			  transaction =session.beginTransaction();
			  for(int i=1;i<=9;i++) {
			  query = session.createSQLQuery("SELECT LOYALTY_ID,TRANSACTION_ID,POINTS FROM LMS_ONM_REDEEM_TXN_"+i+" WHERE STATUS=:status "
			  		+ "AND CURRENT_TIMESTAMP-CREATE_TIME >=INTERVAL '"+Integer.parseInt(Cache.getConfigParameterMap().get("ROLLBACK_MINUTES").getParameterValue())+"' MINUTE");
			  query.setParameter("status", status);
			  //query.setParameter("minute", Cache.getConfigParameterMap().get("ROLLBACK_MINUTES").getParameterValue());
			  //query.setParameter("currentDate", new Date());
			  list = query.list();
			  if(list !=null && list.size() >0) {
				  for(Object[] obj:list) {
					  redemptionDTO = new RedemptionDTO();
					  if(obj[0] !=null)
					  redemptionDTO.setLoyaltyId(obj[0].toString());
					  if(obj[1] !=null)
					  redemptionDTO.setTransactionId(obj[1].toString());
					  if(obj[2] !=null)
					  redemptionDTO.setPoints(obj[2].toString());
					  redemptionList.add(redemptionDTO);
				  }
			  }
		}
			  logger.info("LIST String:"+redemptionList.toString()+" List :"+redemptionList);
			  transaction.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (session != null && session.isOpen())
				session.close();
			if(transaction !=null)
				transaction = null;
		}
		return redemptionList;
	}
	
}
