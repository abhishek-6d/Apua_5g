/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.AccountNumberTabDTO;

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
public class AccountNumberDAO {
	
	private static final Logger logger = Logger.getLogger(AccountNumberDAO.class);
	
	public boolean deleteAccount(String txnId, String tableName , Session modifySession, Transaction modifyTransaction, long loyaltyId , String accountTableName, String accountNumber,AccountNumberTabDTO accountNumberTabDTO ) throws CommonException{
		boolean deleted = false;
		try{
			modifySession = modifySession!=null?modifySession:HiberanteUtil.getSessionFactory().openSession();
			modifyTransaction = modifyTransaction!=null?modifyTransaction:modifySession.beginTransaction();
			//modifySession.update(loyaltyLinkedTable,loyaltyLinkedInfo);
			logger.info("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" AccountNumber "+accountNumber+" Starting Deletion");
			modifySession.update(accountTableName,accountNumberTabDTO);
			//transaction.commit();
			deleted = true;
		}catch(HibernateException e){
			logger.error("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Account Number : "+accountNumber+" Hibernate Exception occured ",e);
			
			throw new CommonException(e.getMessage());
			
		}catch (Exception e) {
			logger.error("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Account Number : "+accountNumber+"  Exception occured ",e);
			throw new CommonException(e.getMessage());
	
		}finally{
			accountTableName = null;
			accountNumberTabDTO = null;
		}
		return deleted;
	}
	
	
	public ArrayList<AccountNumberTabDTO> getAccountDetails(String txnId, String tableName , long loyaltyId , String accountNumber) throws CommonException{

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null; 
		ArrayList<AccountNumberTabDTO> accountNumberTabDTOs = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			logger.info("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" AccountNumber "+accountNumber+" Getting Account Details");
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("accountNumber", accountNumber));
			accountNumberTabDTOs = (ArrayList<AccountNumberTabDTO>) criteria.list();
			transaction.commit();
			//deleted = true;
		}catch(HibernateException e){
			logger.error("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Account Number : "+accountNumber+" Hibernate Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw new CommonException(e.getMessage());
			
		}catch (Exception e) {
			logger.error("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Account Number : "+accountNumber+"  Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw new CommonException(e.getMessage());
	
		}finally{

			if(session!=null){
				session.close();
			}
		}
		return accountNumberTabDTOs;
	
	}

}
