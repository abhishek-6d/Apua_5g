/**
 * 
 */
package com.sixdee.ussd.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.ussd.dto.ConfigParmeterDTO;

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
public class ConfigParameterDAO {
 
	Logger logger = Logger.getLogger(ConfigParameterDAO.class);
	
	public HashMap<String, String> getConfigDetails(){

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, String> configMap = new HashMap<String, String>();
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(ConfigParmeterDTO.class);
			for(ConfigParmeterDTO conf : (List<ConfigParmeterDTO>)criteria.list()){
				logger.info("Parameter Name ["+conf.getParameterName()+"] Parameter Value ["+conf.getParameterValue()+"]");
				configMap.put(conf.getParameterName(), conf.getParameterValue());
			}
			//transaction.commit();
			transaction = null;
			
		}catch (HibernateException e) {
			
			logger.error("Exception in loading cache",e);
		}catch (Exception e) {
			logger.error("Exception in loading cache",e);
			
		}
		finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		return configMap;
	
	}
}
