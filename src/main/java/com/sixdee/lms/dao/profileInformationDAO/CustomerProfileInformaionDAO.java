/**
 * 
 */
package com.sixdee.lms.dao.profileInformationDAO;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.CustomerProfileTabDTO;

/**
 * @author rahul.kr
 *
 */
public class CustomerProfileInformaionDAO {
	
	private static final Logger logger = Logger.getLogger("CustomerProfileInformaionDAO");

	public List<CustomerProfileTabDTO> getCustomerProfileInfoBasedOnMsisdn(String requestId,String whereClause,Object key){
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		List<CustomerProfileTabDTO> customerProfileList = null;
		long st = System.currentTimeMillis();
		try{
			sql = " from CustomerProfileTabDTO where "+whereClause;
			logger.info("RequestId "+sql);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(sql);
			query.setParameter("key", key);
			customerProfileList = query.list();
			transaction.commit();
		}catch (Exception e) {
			logger.error("RequestId : "+requestId+" SQL "+sql+" QueryExecution failed ",e);
			if(transaction != null)
				transaction.rollback();
		}
		finally{
			logger.info("RequestId : "+requestId+" Key "+key+" Time took for completing Query"
					+ " "+sql+" is "+(System.currentTimeMillis()-st));
			transaction = null;
			if(session != null)
				session.close();
		}
		return customerProfileList;
	}

	
}
