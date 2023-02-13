/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.InstantReserveDTO;

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
public class IRStatusUpdationDAO {

	private static final Logger logger = Logger.getLogger(IRStatusUpdationDAO.class);

	
	public void updateRewardStatus(String tableName, long id, int i) {
		Session session = null;
		Transaction transaction = null;
		InstantReserveDTO instantReserveDTO = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			instantReserveDTO = (InstantReserveDTO) session.get(tableName,id);
			instantReserveDTO.setStatus(i);
			instantReserveDTO.setUpdateDate(new Date());
			session.update(instantReserveDTO);
			transaction.commit();
			
		}catch(HibernateException e){
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
		}
	}

	
}
