package com.sixdee.imp.bo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.RewardExpiryDAO;
import com.sixdee.imp.dto.LoyaltyTranSummaryMonthlyDTO;
import com.sixdee.imp.threadpool.ThreadInitiator;

public class RewardExpiryBL {
	private Logger logger = Logger.getLogger(RewardExpiryBL.class);
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public void expiryPoints() {

		logger.info("Starting RewardPoints Expiry");
		RewardExpiryDAO rewardExpiryDAO = null;
		List<LoyaltyTranSummaryMonthlyDTO> list = null;
		int expiryPoints = 0;
		try {
			rewardExpiryDAO = new RewardExpiryDAO();
			String months = Cache.getConfigParameterMap().get("REWARD_POINTS_EXPIRY_PERIOD").getParameterValue().trim();
			int expiryMonths = Integer.parseInt(months);
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			cal.add(Calendar.MONTH, -expiryMonths);
			// String partationIndex = sdf.format(cal.getTime());
			/*
			 * logger.info("Expiry Months:" + expiryMonths + "partation Index:"
			 * + partationIndex);
			 */
			list = rewardExpiryDAO.getExpiryPoints();
			/*
			 * logger.info("Number of LoyaltyIds with partition index" +
			 * partationIndex + "::" + list.size());
			 */
			for (LoyaltyTranSummaryMonthlyDTO loyaltyTranSummaryMonthlyDTO : list) {
				expiryPoints = loyaltyTranSummaryMonthlyDTO.getRewardPoints();
				logger.info("Expirying " + expiryPoints + " points for loyalityID :" + loyaltyTranSummaryMonthlyDTO.getLoyaltyId());
				logger.debug("Adding to ThreadPool:" + loyaltyTranSummaryMonthlyDTO.getLoyaltyId());
				//ThreadInitiator.rewardExpiryPool.addTask(loyaltyTranSummaryMonthlyDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("" + e);
		}
	}

	public int startExpiry(LoyaltyTranSummaryMonthlyDTO dto) {
		logger.info(" LoyaltyId:" + dto.getLoyaltyId() + "points to be expired:" + dto.getRewardPoints());
		RewardExpiryDAO rewardExpiryDAO = null;
		float currentPoints = 0;
		float pointsAfterExpiry = 0;
		int flag = 0;
		try {
			rewardExpiryDAO = new RewardExpiryDAO();
			currentPoints = rewardExpiryDAO.getCurrentRewardPoints(dto.getLoyaltyId());
			logger.info(" points before expiry:" + currentPoints + "For loyaltyId:" + dto.getLoyaltyId());
			pointsAfterExpiry = ((currentPoints - dto.getRewardPoints()) < 0 ? 0 : currentPoints - dto.getRewardPoints());
			logger.info(" points after expiry:" + pointsAfterExpiry + "For loyaltyId:" + dto.getLoyaltyId());
			// logger.warn(dto.getLoyaltyId()+"|"+currentPoints+"|"+pointsAfterExpiry+"|"+dto.getRewardPoints());
			flag = rewardExpiryDAO.updateRewardpoints(pointsAfterExpiry, dto);
			if (flag > 0) {
				logger.info("expiry sucessful for lotaltyId:" + dto.getLoyaltyId());
				rewardExpiryDAO.writeCdr("SC0000", "SUCCESS", dto);
				int flag1 = rewardExpiryDAO.updateLoyaltyTranSummaryMonthly(dto.getLoyaltyId(), dto.getRewardPoints(), dto.getPartitionIndex());
				if (flag1 > 0)
					logger.info("Updated EXPIR_REWARD_POINTS in LOYALTY_TRAN_SUMMARY_MONTHLY with Rewardpoints for LOYALTYID " + dto.getLoyaltyId());
				rewardExpiryDAO.updateLoyaltyTransactionTable(dto, pointsAfterExpiry, currentPoints);

			} else  {
				
				if (Cache.getCacheMap().get("ACTUAL_REWARD_EXPIRY").equalsIgnoreCase("true")){
					logger.info("failed expiry for loyaltyId:" + dto.getLoyaltyId());
					rewardExpiryDAO.writeCdr("SC0001", "FAILED", dto);
				}
				

			}

		} catch (Exception e) {
			logger.error("LoyaltyId:" + dto.getLoyaltyId() + "Exception:" + e);
			e.printStackTrace();
		}
		return 0;

	}

}
