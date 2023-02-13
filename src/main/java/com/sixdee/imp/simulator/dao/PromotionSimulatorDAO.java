package com.sixdee.imp.simulator.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.simulator.dto.PromotionSubscriberOutPutdto;

public class PromotionSimulatorDAO {
	Logger logger = Logger.getLogger(PromotionSimulatorDAO.class);
	
	/*
	 * public List<String> getSubscrbersList(){
	 * logger.info("**** Inside getSubscrbersList()"); Session session =
	 * HiberanteUtil.getSessionFactory().openSession(); Transaction tx = null;
	 * List<String> subsList = new ArrayList<String>(); Connection conn = null;
	 * PreparedStatement pstmt = null; ResultSet rs = null; try{ tx =
	 * session.beginTransaction(); conn = session.connection(); String sql =
	 * "SELECT * FROM HBB_CUST_CRM"; logger.info(sql); pstmt =
	 * conn.prepareStatement(sql); rs = pstmt.executeQuery(); while(rs.next()){
	 * subsList.add(rs.getString(1).trim()); }
	 * 
	 * tx.commit(); }catch(Exception e){
	 * logger.error("exception in getSubscrbersList()====> "+e); if(tx!=null)
	 * tx.rollback(); } finally{ session.close(); } return subsList; }
	 */
	
	
	public void insertPromoInfo(PromotionSubscriberOutPutdto outPutdto){
		logger.info("Inside insertPromoInfo()");
		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(outPutdto);
			tx.commit();
			
		}catch(Exception e){
			logger.error("exception in insertPromoInfo()====> "+e);
			if(tx!=null)
				tx.rollback();
		}
		finally{
			session.close();
		}
		
	}
	
}
