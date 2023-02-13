package com.sixdee.imp.simulator;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.simulator.dto.DuplicateNationalIdInfo;

public class SingleNationalIDRemoval extends Thread{

	
	private static final Logger logger = Logger.getLogger(SingleNationalIDRemoval.class);
	public void run(){
		//boolean flag = true;
		logger.info("Started Activity For Single National ids");
		try {
			ArrayList<DuplicateNationalIdInfo> duplicateInfoList = getDuplicateNationalIds();
			for(DuplicateNationalIdInfo dup : duplicateInfoList){
				String actNId = dup.getActualNationalId();
				String nId = dup.getProposedNationalId();
				logger.info("Erradicating Preciding 0s from LMS "+actNId+" with "+nId);
				long t1 =System.currentTimeMillis();
				int count = revertToOriginalNationalId(actNId,nId);
				long t2 =System.currentTimeMillis();
				
				if(count == 0){
					logger.info("Updation Failed "+actNId+" Time Take "+(t2-t1));
				}else{
					updateStatusAsCompleted(dup);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Exception ",e);
		}
	}
	private void updateStatusAsCompleted(DuplicateNationalIdInfo dup) {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		int count = 0;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			dup.setStatus(200);
			session.update(dup);
			transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
	
	}
	private int revertToOriginalNationalId(String actNId, String nId) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		int count = 0;
		try{
			sql = "Update NATIONAL_NUMBER_ENTITY_0 set NATIONAL_ID=:nId where NATIONAL_ID = :actNId";
			logger.info(sql+" :nId="+nId+" :actNId="+actNId);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(sql);
			query.setParameter("nId", nId);
			query.setParameter("actNId", actNId);
			count = query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}
		return count;
	}
	private ArrayList<DuplicateNationalIdInfo> getDuplicateNationalIds() throws Exception {



		ArrayList<DuplicateNationalIdInfo> nationalNumberList = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		try{
		//	logger.info("Loyalty Id "+loyaltyId);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(DuplicateNationalIdInfo.class);
			criteria.add(Restrictions.eq("status", 100));
			criteria.add(Restrictions.eq("actualLines", 0));
		//	criteria.add(Restrictions.like("loyaltyID", loyaltyId));
			nationalNumberList = (ArrayList<DuplicateNationalIdInfo>) criteria.list();
			transaction.commit();
	
//			criteria.set
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw e;
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return nationalNumberList;
	
	
	
	}
	
}
