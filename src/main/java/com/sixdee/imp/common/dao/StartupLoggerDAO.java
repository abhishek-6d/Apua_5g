/**
 * 
 */
package com.sixdee.imp.common.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.startup.StartupLoggerDTO;

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
 * <td>January 28, 2014</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class StartupLoggerDAO {

	private static final Logger logger = Logger.getLogger(StartupLoggerDAO.class);
	 
	public void auditStartup(StartupLoggerDTO startupLogger){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(startupLogger);
			transaction.commit();
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
			startupLogger = null;
		}
	}
}
