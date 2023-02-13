package com.sixdee.imp.dao;

/**
 * 
 * @author Himanshu
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
 * <td>October 05,2015 04:03:20 PM</td>
 * <td>Himanshu</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtilProfile;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.dto.TerminationServiceDTO;

public class TerminationServiceDAO {

	public boolean updateProfileDetails(TerminationServiceDTO terminationServiceDTO){
		
		Logger logger = Logger.getLogger(TerminationServiceDAO.class);
		Session session = null;
		Transaction tx = null;
		
		TableInfoDAO infoDAO = new TableInfoDAO();
		Query query=null;
		String tableName=null;
		String sql="";
		boolean flag=false;
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();
			logger.info("SUB SCRIBER NO::::"+terminationServiceDTO.getSubscriberNumber());
			if(terminationServiceDTO.getAccountType()==14){
			tableName = infoDAO.getPrepaidProfileTableName(terminationServiceDTO.getSubscriberNumber());
			
			logger.info("Values to Update in ["+tableName+"] = MSISDN:["+terminationServiceDTO.getSubscriberNumber());
		    sql="UPDATE "+tableName+" set FIELD5 = :status  where FIELD2 = :subsNumber";    
			query = session.createSQLQuery(sql);
			query.setParameter("status", "D");
			query.setParameter("subsNumber", terminationServiceDTO.getSubscriberNumber());
			}
			else if (terminationServiceDTO.getAccountType()==9){
				tableName = infoDAO.getLanguagePostTableName(terminationServiceDTO.getSubscriberNumber());
				
				logger.info("Values to Update in ["+tableName+"] = MSISDN:["+terminationServiceDTO.getSubscriberNumber());
			    sql="UPDATE "+tableName+" set STATUS = :status  where MSISDN = :subsNumber";    
				query = session.createSQLQuery(sql);
				query.setParameter("status", "D");
				query.setParameter("subsNumber", terminationServiceDTO.getSubscriberNumber());
				
			}
			int x=query.executeUpdate();
			logger.info("Rows Updated:"+x);
			tx.commit();
			flag=true;
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Updating Status DB:" + finaltime);
			terminationServiceDTO.setStatusCode("SC0000");
			terminationServiceDTO.setStatusDesc("SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			terminationServiceDTO.setStatusCode("SC0001");
			terminationServiceDTO.setStatusDesc("FAILURE");
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}

		
		
	return flag;	
	}
}
