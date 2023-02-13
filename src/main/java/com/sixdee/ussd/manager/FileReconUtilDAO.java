/**
 * 
 */
package com.sixdee.ussd.manager;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.ussd.dto.TransactionHistoryDTO;

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
public class FileReconUtilDAO {

	Logger logger = Logger.getLogger(FileReconUtilDAO.class);
	public void processMessage(TransactionHistoryDTO notify, int stage) {
		switch(stage){
		case 1: saveAudit(notify);
		break;
		case 2:updateAudit(notify);
			break;
		
		}
	}

	private void updateAudit(TransactionHistoryDTO notify) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(notify);
			transaction.commit();
			transaction = null;
		}catch(Exception e){
			logger.error("Exception occured ",e);
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

	private void saveAudit(TransactionHistoryDTO notify) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(notify);
			transaction.commit();
			transaction = null;
		}catch(Exception e){
			logger.error("Exception occured ",e);
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

}
