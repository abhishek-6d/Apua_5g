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

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;

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
public class LoyaltyRegisteredNumberDAO {
	
	private static final Logger logger = Logger.getLogger(LoyaltyRegisteredNumberDAO.class);
	
	public ArrayList<LoyaltyRegisteredNumberTabDTO> getLinkedLines(String txnId,String tableName,String  loyaltyId) throws CommonException{
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredNumbers = null;
		try{
			logger.info("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Getting loyalty linked numbers from "+tableName);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			loyaltyRegisteredNumbers = (ArrayList<LoyaltyRegisteredNumberTabDTO>) criteria.list();
			transaction.commit();
		}catch(HibernateException e){
				logger.error("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Account Number :  Hibernate Exception occured ",e);
				if(transaction!=null){
					transaction.rollback();
				}
				throw new CommonException(e.getMessage());
				
			}catch (Exception e) {
				logger.error("Transaction ID : "+txnId+" LoyaltyId : "+loyaltyId+" Account Number :   Exception occured ",e);
				if(transaction!=null){
					transaction.rollback();
				}
				throw new CommonException(e.getMessage());
		
		} finally {

			if (session != null) {
				session.close();
			}
		}
		return loyaltyRegisteredNumbers;	
		}
	

}
