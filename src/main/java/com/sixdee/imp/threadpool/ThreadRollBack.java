package com.sixdee.imp.threadpool;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.ReserveAndCommitDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.OnmRedemptionTransactionDTO;
import com.sixdee.imp.dto.RedemptionDTO;

public class ThreadRollBack extends Thread {

	private static final Logger logger = LogManager.getLogger(ThreadRollBack.class);

	public void run() {
		OnmRedemptionTransactionDTO onmRedemptionTransaction = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		boolean isUpdate = false;
		List<RedemptionDTO> list = null;
		ReserveAndCommitDAO reserveAndCommitDAO = null;
		try {
			while(true) {
				logger.info(" Time ::"+Calendar.getInstance().getTime()+" ------Rollback Thread ------");
				reserveAndCommitDAO = new ReserveAndCommitDAO();
				list = reserveAndCommitDAO.getOnmTrasactionDetails(1);
				if(list !=null && list.size() >0) {
					for(RedemptionDTO redemptionDTO : list) {
						logger.info("Transaction Id ::"+redemptionDTO.getTransactionId()+" LoyaltyId ::"+redemptionDTO.getLoyaltyId());
						loyaltyProfileTabDTO = new LoyaltyProfileTabDTO();
						loyaltyProfileTabDTO.setLoyaltyID(Long.parseLong(redemptionDTO.getLoyaltyId()));
						loyaltyProfileTabDTO.setRewardPoints(Double.parseDouble(redemptionDTO.getPoints()));
						isUpdate = reserveAndCommitDAO.rollbackPointDetails(redemptionDTO.getTransactionId(), loyaltyProfileTabDTO);
						if(isUpdate) {
							onmRedemptionTransaction = new OnmRedemptionTransactionDTO();
							onmRedemptionTransaction.setStatus(3);
							onmRedemptionTransaction.setUpdateTime(new Date());
							onmRedemptionTransaction.setLoyaltyId(redemptionDTO.getLoyaltyId());
							reserveAndCommitDAO.updateOnmRedemptionTransaction(redemptionDTO.getTransactionId(), onmRedemptionTransaction);
						}
					}
				}
				Thread.sleep(Integer.parseInt(Cache.getCacheMap().get("ROLLBACK_THREAD_SLEEP")));
			}
		}catch(Exception e) {
			logger.info("Exception in Thread Rollback ::"+e);
			e.printStackTrace();
		}
	}
}
