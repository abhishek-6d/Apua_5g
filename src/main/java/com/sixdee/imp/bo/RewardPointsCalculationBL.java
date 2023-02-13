package com.sixdee.imp.bo;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.RewardPointsCalculationDAO;
import com.sixdee.imp.dto.RewardPointsCalculationDTO;

public class RewardPointsCalculationBL extends BOCommon {
	/**
	 * This method is called from BOCommon. Access the DAO object is to create
	 * CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => RewardPointsCalculationBL :: Method => buildProcess()");

		RewardPointsCalculationDAO rewardPointsCalculationDAO = new RewardPointsCalculationDAO();
		RewardPointsCalculationDTO rewardPointsCalculationDTO = (RewardPointsCalculationDTO) genericDTO.getObj();

		if (rewardPointsCalculationDTO.isPointsCalculation()
				&& Cache.getCacheMap().get("POINT_CALCULATION").equalsIgnoreCase("TRUE")) {
			rewardPointsCalculationDAO.calculateRewardPointsStep1(genericDTO);
		}

		if (rewardPointsCalculationDTO.getRewardPointsCategory() == 2) {
			logger.info("Class => RewardPointsCalculationBL :: Inside Prepaid Insert");
			rewardPointsCalculationDAO.insertIntoPrepaidInstantTable(rewardPointsCalculationDTO);
		}
		if (rewardPointsCalculationDTO.isEbill()) {
			logger.info("EBILL Feature gng to Notify ARBOR System...");
			if (genericDTO.getStatusCode() != null && genericDTO.getStatusCode().equalsIgnoreCase("SC0000"))
				rewardPointsCalculationDAO.notifyARBORSystem(rewardPointsCalculationDTO);
		}
		rewardPointsCalculationDAO = null;
		rewardPointsCalculationDTO = null;
		return genericDTO;
	}
}
