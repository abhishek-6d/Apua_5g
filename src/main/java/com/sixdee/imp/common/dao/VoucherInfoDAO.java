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
 * @author NITHIN
 *
 */
public class VoucherInfoDAO {
	

	private static final Logger logger = Logger.getLogger(VoucherInfoDAO.class);
	public Map<String, String> getVoucherStatusInfo() {
		HashMap<String,String> voucherStatusInfo = new HashMap<String, String>();
		Session session = null;
		//Transaction transaction= null;
		Query query = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			query =session.createSQLQuery("select STATUS_CODE,LANGUAGE_ID,STATUS_DESCRIPTION from VOUCHER_STATUS");
			for(Object[] statusList : (List<Object[]>)query.list()){
				if(statusList != null){
					voucherStatusInfo.put(statusList[0]+"_"+statusList[1], (String)statusList[2]);
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
		System.out.println("SIZE OF MAP@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+voucherStatusInfo.size());
		return voucherStatusInfo;
	}



}
