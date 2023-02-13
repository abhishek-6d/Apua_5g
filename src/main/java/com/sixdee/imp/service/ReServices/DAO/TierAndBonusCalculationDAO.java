package com.sixdee.imp.service.ReServices.DAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LmsOnmPointSnapshotDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;
import com.sixdee.imp.service.ReServices.BL.TierAndBonusPointCalculationBL;

public class TierAndBonusCalculationDAO {
	private static final Logger logger = Logger.getLogger(TierAndBonusPointCalculationBL.class);
	
	public void insetTierAndBonusDetails(
			TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO) throws Exception{
		Session session=null;
		Transaction transaction=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			logger.info("=>>>>>>>>>>>>>>>>SAVING");
			session.save(tierAndBonusPointDetailsDTO);
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}finally{ 
			try {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}

			} catch (Exception e) {
				throw e;
			}
		}
	
	}
	public void updateTierAndBonusDEtails(
			TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO) throws Exception{
		logger.info("***** updateTierAndBonusDEtails*****");
		Session session=null;
		String hql=null;
		Transaction transaction=null;
		Query query =null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			hql="UPDATE TierAndBonusPointDetailsDTO SET tierPoints = :tierPoints, bonusPoints =:bonusPoints, "
					+ "totalPoints=:totalPoints,tierCreateDate=:tierCreateDate,bonusCreateDate=:bonusCreateDate "
					+ "WHERE expiryDate =:expiryDate AND loyaltyId=:loyaltyId AND msisdn=:msisdn";
			query=session.createQuery(hql);
			query.setParameter("tierPoints", tierAndBonusPointDetailsDTO.getTierPoints());
			query.setParameter("bonusPoints", tierAndBonusPointDetailsDTO.getBonusPoints());
			query.setParameter("totalPoints", tierAndBonusPointDetailsDTO.getTotalPoints());
			query.setParameter("tierCreateDate", tierAndBonusPointDetailsDTO.getTierCreateDate());
			query.setParameter("bonusCreateDate", tierAndBonusPointDetailsDTO.getBonusCreateDate());
			query.setParameter("expiryDate", tierAndBonusPointDetailsDTO.getExpiryDate());
			query.setParameter("loyaltyId", tierAndBonusPointDetailsDTO.getLoyaltyId());
			query.setParameter("msisdn", tierAndBonusPointDetailsDTO.getMsisdn());
			
			if(query.executeUpdate()>0){
				transaction.commit();
				logger.info(">>successfully updated TierAndBonusPointDetailsDTO");
			}else{
				logger.info(">>updation failed>>");
				throw new Exception("UPDATION FAILED");
			}
		
		}
		catch(HibernateException he)
		{
			logger.error("Exception in updateTierAndBonusDEtails()",he);
			if(transaction!=null){
				transaction.rollback();
				
			}
			throw he;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception in updateLoyaltyProfile()",e);
			throw e;
		}
		finally{
			if(session!=null && session.isOpen()){
				session.close();
				session=null;
			}
			
		}
		
	}
	public TierAndBonusPointDetailsDTO checkInTableFortierAndBonusPointDetails(
			Date expiryDate, String msisdn, Long loyaltyID) throws Exception{
		Session session = null;
		TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO=null;
		//Transaction trx = null;
		
		try {
			logger.info("Inside checkInTableFortierAndBonusPointDetails >>>>loyalityId>>>>"+loyaltyID+">>>>msisdn>>>"+msisdn+">>>>>expiryDate>>>>"+expiryDate);
			session=HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();
			Criteria criteria=session.createCriteria(TierAndBonusPointDetailsDTO.class);
			criteria.add(Restrictions.eq("loyaltyId",loyaltyID+""));
			criteria.add(Restrictions.eq("msisdn",msisdn));
			criteria.add(Restrictions.eq("expiryDate",expiryDate));
			
			List<TierAndBonusPointDetailsDTO> list =criteria.list();
			logger.info(">>>>list size>>>>>"+list.size());
			if(list.size()>0){
				
				tierAndBonusPointDetailsDTO =list.get(0);
				
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
			session = null;
		}
		return tierAndBonusPointDetailsDTO;
	}
	public boolean updateLoyaltyProfile(
			LoyaltyProfileTabDTO loyaltyProfileTabDTO) throws Exception{
		logger.info("***** updateLoyaltyProfile*****");
		Session session=null;
		String hql=null;
		boolean flag=false;
		Transaction transaction=null;
		Query query =null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			//String profileTable = infoDAO.getLoyaltyProfileDBTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
			hql="UPDATE LOYALTY_PROFILE_ENTITY_0 SET rewardPoints = :rewardPoints, counter=counter+1, tierPoints=:tierPoints,bonusPoints=:bonusPoints WHERE loyaltyID =:loyaltyId AND counter=:counter";
			query=session.createQuery(hql);
			query.setParameter("rewardPoints", loyaltyProfileTabDTO.getRewardPoints());
			query.setParameter("loyaltyId", loyaltyProfileTabDTO.getLoyaltyID());
			query.setParameter("counter", loyaltyProfileTabDTO.getCounter());
			query.setParameter("tierPoints", loyaltyProfileTabDTO.getTierPoints());
			query.setParameter("bonusPoints", loyaltyProfileTabDTO.getBonusPoints());
			if(query.executeUpdate()>0){
				transaction.commit();
				flag=true;
			}else{
				flag=false;
			}
		
		}
		catch(HibernateException he)
		{
			flag=false;
			if(transaction!=null){
				transaction.rollback();
			}
			throw he;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			throw e;
		}
		finally{
			if(session!=null && session.isOpen()){
				session.close();
				session=null;
			}
			
		}
		return flag;
	}
	public boolean inserTransaction(
			LoyaltyTransactionTabDTO loyaltyTransactionTabDTO) {
		boolean flag = false;
		String tableName = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Session session = null;
		Transaction transaction = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			tableName = infoDAO.getLoyaltyTransactionTable("" + loyaltyTransactionTabDTO.getLoyaltyID());
			logger.info(">>>table name>>>" + tableName);
			session.save(tableName, loyaltyTransactionTabDTO);
			flag = true;
			logger.info(">>transaction inserted>>>");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			throw e;

		} finally {
			transaction.commit();
			infoDAO = null;

			if (session.isOpen()) {
				session.close();
				transaction = null;
			}
		}
		return flag;
	}
	
	public void updatePointSnapshotDTO(
			LmsOnmPointSnapshotDTO lmsOnmPointSnapshotDTO) {
		Session session=null;
		Transaction transaction=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			logger.info("=>>>>>>>>>>>>>>>>SAVING");
			session.update(lmsOnmPointSnapshotDTO);
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}finally{ 
			try {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}

			} catch (Exception e) {
				throw e;
			}
		}
		
	}
	public void insertIntoPointSnapshotDTO(
			LmsOnmPointSnapshotDTO lmsOnmPointSnapshotDTO) {
		Session session=null;
		Transaction transaction=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			logger.info("=>>>>>>>>>>>>>>>>SAVING");
			session.save(lmsOnmPointSnapshotDTO);
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}finally{ 
			try {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}

			} catch (Exception e) {
				throw e;
			}
		}
		
	}


}
