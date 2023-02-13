package com.sixdee.imp.bo;



import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dao.ExpiryPointsDAO;
import com.sixdee.imp.dto.LmsOnmDayWiseBean;



/**
 * @author rahul.krishnan
 *
 */
public class ConsumePointsBL {
	private static Logger logger = Logger.getLogger(ConsumePointsBL.class);
	
	public long consumeMyPoints(String requestId, String loyaltyId, long points, boolean isTierPointOnly) {
		ExpiryPointsDAO expirePointDAO = null;
		ArrayList<LmsOnmDayWiseBean> lmsonmList = null;
		long consumedPoints = 0;
		boolean processingCompleted = true;
		long startTime = System.currentTimeMillis();
		try {
			synchronized (ConsumePointsBL.class) {
				expirePointDAO = new ExpiryPointsDAO();
				do {
					lmsonmList = expirePointDAO.getFirstExpiringPoint(loyaltyId);
					if (!lmsonmList.isEmpty()) {
						for (LmsOnmDayWiseBean lmsOnmDayWiseBean : lmsonmList) {
							long usedPointFromTxn = consumedPoints + lmsOnmDayWiseBean.getTierPoints() <= points
									? lmsOnmDayWiseBean.getTierPoints()
									: points - consumedPoints;
							if (expirePointDAO.updateExpiringPoint(lmsOnmDayWiseBean, usedPointFromTxn) == 1) {
								consumedPoints = consumedPoints + usedPointFromTxn;
								if (consumedPoints == points) {
									processingCompleted = false;
									break;
								}
							}
						}
					} else {
						/*
						 * processing completed because of all transactions completed
						 */
						processingCompleted = false;
					}

				} while (processingCompleted);
			}
		} catch (Exception e) {
			logger.error("RequestId " + requestId + " Exception in consume points " + e);
		} finally {
			logger.info("RequestId " + requestId + " Points Redeemed/Transferred " + points + ": PointsExpiredFromTxn "
							+ consumedPoints + " Timetook : " + (System.currentTimeMillis() - startTime) +" Thread "+Thread.currentThread().getName());
		}
		return consumedPoints;
	}

}
