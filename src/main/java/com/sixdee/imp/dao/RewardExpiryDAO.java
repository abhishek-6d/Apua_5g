package com.sixdee.imp.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dto.LoyaltyTranSummaryMonthlyDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;

public class RewardExpiryDAO {
	private Logger logger = Logger.getLogger(RewardExpiryDAO.class);
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public List<LoyaltyTranSummaryMonthlyDTO> getExpiryPoints() {
		logger.info("getting expiry points from LOYALTY_TRAN_SUMMARY_MONTHLY");
		List<LoyaltyTranSummaryMonthlyDTO> list = null;
		int points = 0;
		Session session = null;
		String sql = null;
		LoyaltyTranSummaryMonthlyDTO dto = null;
		try {
			
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "SELECT LOYALTY_ID,SUM(REWARD_POINTS) FROM LOYALTY_TRAN_SUMMARY_MONTHLY WHERE  PARTITION_INDEX IN ("+Cache.getCacheMap().get("REWARD_POINTS_PARTITION_INDEX")+") GROUP by LOYALTY_ID ";
			Query query = session.createSQLQuery(sql);
			// query.setParameter("partitionIndex", partitionIndex);
			List<Object[]> l = query.list();
			list = new ArrayList<LoyaltyTranSummaryMonthlyDTO>();
			for (Object[] o : l) {
				dto = new LoyaltyTranSummaryMonthlyDTO();
				dto.setLoyaltyId(Long.parseLong(o[0].toString()));
				dto.setRewardPoints(Integer.parseInt(o[1].toString()));
				// dto.setPartitionIndex(Integer.parseInt(o[2].toString()));
				list.add(dto);
				dto = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" Exception Occured while getting points from LOYALTY_TRAN_SUMMARY_MONTHLY table " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return list;

	}

	public void updateLoyaltyTransactionTable(LoyaltyTranSummaryMonthlyDTO dto, float pointsAfterExpiry, float currentPoints) {

		Session session = null;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		TableInfoDAO tableInfoDAO = null;
		Transaction tx = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			logger.info("Came to updateLoyaltyTransactionTable for LoyaltyId:" + dto.getLoyaltyId());
			tx = session.beginTransaction();
			tableInfoDAO = new TableInfoDAO();
			loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
			loyaltyTransactionTabDTO.setLoyaltyID(dto.getLoyaltyId());
			loyaltyTransactionTabDTO.setCurRewardPoints(Double.parseDouble(String.valueOf(pointsAfterExpiry)));
			loyaltyTransactionTabDTO.setPreRewardPoints(Double.parseDouble(String.valueOf(currentPoints)));
			loyaltyTransactionTabDTO.setReqTransactionID(dto.getLoyaltyId() + "");
			loyaltyTransactionTabDTO.setRewardPoints(Double.parseDouble(String.valueOf(dto.getRewardPoints())));
			loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.rewardPointsExpired);
			loyaltyTransactionTabDTO.setCreateTime(new Date());
			loyaltyTransactionTabDTO.setServerId(Cache.cacheMap.get("SERVER_ID").toString());
			session.save(tableInfoDAO.getLoyaltyTransactionTable(dto.getLoyaltyId().toString()), loyaltyTransactionTabDTO);
			tx.commit();
			logger.info("Updated updateLoyaltyTransactionTable for LoyaltyId:" + dto.getLoyaltyId());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.error("Exception occured " + e + "LoyaltyId" + dto.getLoyaltyId());
		} finally {
			if (tx != null)
				tx = null;
			if (session != null)
				session.close();
		}

	}

	public int updateLoyaltyTranSummaryMonthly(Long loyaltyId, int rewardPoints, int partationIndex) {
		Session session = null;
		String sql = null;
		Transaction tx = null;
		int flag = 0;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "update loyalty_tran_summary_monthly set EXPIR_REWARD_POINTS=:points,REWARD_POINTS=:rewardpoints where loyalty_id=:loyaltyId and PARTITION_INDEX in ("+Cache.getCacheMap().get("REWARD_POINTS_PARTITION_INDEX")+")";
			Query query = session.createSQLQuery(sql);
			query.setParameter("points", rewardPoints);
			query.setParameter("rewardpoints", 0);
			query.setParameter("loyaltyId", loyaltyId);
			// query.setParameter("p", partationIndex);
			tx = session.beginTransaction();
			flag = query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			logger.error("For LoyaltyId:" + loyaltyId + "Exceptoin occured while updating LoyaltyTranSummaryMonthly " + e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;

	}

	public float getCurrentRewardPoints(Long loyaltyId) {
		Session session = null;
		String sql = null;
		float currentPoints = 0;
		
		logger.info(" Loyalty ID FOr Debug>>>>>>>>>>>"+loyaltyId);
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "SELECT REWARD_POINTS FROM LOYALTY_PROFILE_0 WHERE LOYALTY_ID=:loyaltyId ";
			Query query1 = session.createSQLQuery(sql);
			query1.setParameter("loyaltyId", loyaltyId);
			List<Object[]> list = query1.list();
			if (list.size() > 0) {
				String points = list.get(0) + "";
				currentPoints = Float.parseFloat(points);
			}
			
			logger.info(loyaltyId+"::Loyalty ID :: Current Points ::"+currentPoints);
			
		} catch (Exception e) {
			logger.error(" loyaltyId:" + loyaltyId + "Exception: " + e);
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
		return currentPoints;
	}

	public int updateRewardpoints(float pointsAfterExpiry, LoyaltyTranSummaryMonthlyDTO dto) {
		Session session = null;
		String sql = null;
		int flag = 0;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "UPDATE LOYALTY_PROFILE_0 SET REWARD_POINTS=:rewardsPoints WHERE LOYALTY_ID=:loyaltyId ";
			Query query = session.createSQLQuery(sql);
			query.setParameter("rewardsPoints", pointsAfterExpiry);
			query.setParameter("loyaltyId", dto.getLoyaltyId());
			if (Cache.getCacheMap().get("ACTUAL_REWARD_EXPIRY").equalsIgnoreCase("true")) {
				flag = query.executeUpdate();
				writeCdr("SC0000", "SUCCESS", dto);
			} else {
				writeCdr("SC0000", "SUCCESS", dto);
			}
		} catch (Exception e) {
			logger.error(" loyaltyId:" + dto.getLoyaltyId() + "Exception: " + e);
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
		return flag;

	}

	public void writeCdr(String statusCode, String statusDescription, LoyaltyTranSummaryMonthlyDTO dto) {
		logger.fatal(String.format("%s|%s|||||||||%s|%s|%s||||||||||||", dto.getLoyaltyId(), df.format(new Date()), statusCode, statusDescription, dto.getRewardPoints()

		));

	}

}
