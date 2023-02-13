/**
 * 
 */
package com.sixdee.imp.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;

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
public class LoyaltyInfoDAO {
	private static final Logger logger = Logger.getLogger(LoyaltyInfoDAO.class);
	public Map<String, String> getLoyaltyStatusInfo() {
		HashMap<String,String> loyalyStausInfo = new HashMap<String, String>();
		Session session = null;
		//Transaction transaction= null;
		Query query = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			query =session.createSQLQuery("select STATUS_ID,LANGUAGE_ID,STATUS_DESC from LOYALTY_STATUS_CODES");
			for(Object[] statusList : (List<Object[]>)query.list()){
				if(statusList != null){
					loyalyStausInfo.put(statusList[0]+"_"+statusList[1], (String)statusList[2]);
				}
			}
			//transaction.commit();
		}catch (Exception e) {
			/*
			 * if(transaction != null) transaction.rollback();
			 */
			logger.error("Exception occured ",e);
		}finally{
			if(session != null)
				session.close();
			//transaction = null;
			session = null;
			query = null;
		}
		return loyalyStausInfo;
	}

}
