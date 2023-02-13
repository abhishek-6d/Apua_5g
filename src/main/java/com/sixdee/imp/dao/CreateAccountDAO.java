package com.sixdee.imp.dao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;

public class CreateAccountDAO {

	private Logger logger=Logger.getLogger(CreateAccountDAO.class);
	//private static SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	

	public boolean isLoyaltyIdExists(String generatedNumber) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		boolean isExists = false;
		try{
			
			sql = "from LoyaltyProfileTabDTO where loyaltyId=:loyaltyId";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(sql);
			query.setParameter("loyaltyId", generatedNumber);
			if(query.list().size()>0)
				isExists = true;
		}catch (Exception e) {
			logger.error("Exception occured",e);
		}
		return isExists;
	}
}
