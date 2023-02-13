package com.sixdee.imp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

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
 * <td>June 20,2013 04:39:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */




public class TransferLineDAO {
	
	private Logger logger = Logger.getLogger(TransferLineDAO.class);
	public boolean pointTransfer(SubscriberNumberTabDTO aSubscriberNumberDTO,long loyaltyId){
		boolean isTransfered = false;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			aSubscriberNumberDTO.setLoyaltyID(loyaltyId);
			session.update(aSubscriberNumberDTO);
		//	session.update(bSubscriberNumberDTO);
			transaction.commit();
			isTransfered =true;
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}
		finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		return isTransfered;
	}

	
	public boolean lineRegistTransfer(LoyaltyRegisteredNumberTabDTO donorDTO,String loyaltyRegTable){
		boolean isTransfered = false;
		Session session = null;
		Transaction transaction = null;
		Query hqlQuery = null;
		String hql = null;
		try{
			hql = "Update "+loyaltyRegTable+" set loyaltyID = :lid where linkedNumber = :lNo";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			hqlQuery = session.createQuery(hql);
			hqlQuery.setParameter("lid", donorDTO.getLoyaltyID());
			hqlQuery.setParameter("lNo", donorDTO.getLinkedNumber());
			//session.update(loyaltyRegTable,donorDTO);
			int i =hqlQuery.executeUpdate();
		//	session.update(bSubscriberNumberDTO);
			transaction.commit();
			if(i>0)
				isTransfered =true;
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}
		finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		return isTransfered;
	}
	
	public boolean lineTransfer(String txnId, String tableName, SubscriberNumberTabDTO donorDTO,SubscriberNumberTabDTO recieverDTO){
		boolean isTransfered = false;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			donorDTO.setLoyaltyID(recieverDTO.getLoyaltyID());
			donorDTO.setPoints(0.0);
			session.update(tableName,donorDTO);
		//	session.update(bSubscriberNumberDTO);
			transaction.commit();
			isTransfered =true;
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}
		finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		return isTransfered;
	}
	
	public void updateAccountTable(String aTable,String bTable,SubscriberNumberTabDTO aSubscriberNumberDTO, SubscriberNumberTabDTO bSubscriberNumberDTO) {

		boolean isTransfered = false;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		List<AccountNumberTabDTO> accNumberList = null;
		AccountNumberTabDTO account = null;
		boolean isFlag = false;
		boolean isDelete = false;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(AccountNumberTabDTO.class);
			criteria.add(Restrictions.eq("accountNumber", aSubscriberNumberDTO.getAccountNumber()));
			accNumberList =  criteria.list();
			for(AccountNumberTabDTO accountNumberTabDTO : accNumberList ){
				if(bSubscriberNumberDTO.getAccountNumber()==null){
					session.delete(accountNumberTabDTO);
					isDelete = true;
					isFlag = true;
				}else{
					accountNumberTabDTO.setAccountNumber(bSubscriberNumberDTO.getAccountNumber());
					isFlag = true;
				}
			}
			if(!isFlag){
				if(bSubscriberNumberDTO.getAccountNumber()!=null){
					account = new AccountNumberTabDTO();
					account.setAccountNumber(bSubscriberNumberDTO.getAccountNumber());
					account.setLoyaltyID(bSubscriberNumberDTO.getLoyaltyID());
					account.setPoints(bSubscriberNumberDTO.getPoints());
					account.setStatusID(bSubscriberNumberDTO.getStatusID());
					session.save(bTable,account);
				}
			}else{
				if(!isDelete)
					session.update(bTable,aSubscriberNumberDTO);
			}
		//	session.update(bSubscriberNumberDTO);
			transaction.commit();
			isTransfered =true;
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
				transaction = null;
			}
		}
		finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		
	}
	
	
}
