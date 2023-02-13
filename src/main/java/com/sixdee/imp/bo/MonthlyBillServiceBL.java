package com.sixdee.imp.bo;

/**
 * 
 * @author @jith
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>January 06,2016 10:55:45 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.GetTransactionDetailsDAO;
import com.sixdee.imp.dao.MonthlyBillServiceDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.MonthlyBillServiceDTO;

public class MonthlyBillServiceBL extends BOCommon {

	/**
	 * This method is called from BOCommon. Access the DAO object is to create
	 * CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */

	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => MonthlyBillServiceBL :: Method => buildProcess()");

		MonthlyBillServiceDAO monthlyBillServiceDAO;
		MonthlyBillServiceDTO monthlyBillServiceDTO = (MonthlyBillServiceDTO) genericDTO.getObj();
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		String subscriber = null;
		String tableName = null;
		String loyaltyId = null;
		String langId = null;
		GetTransactionDetailsDAO getTransactionDetailsDAO = new GetTransactionDetailsDAO();
		TableDetailsDAO detailsDAO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		LanguageDAO languageDAO = new LanguageDAO();
		CommonUtil commonUtil = new  CommonUtil();
		try {
			logger.info("Transaction id -" + monthlyBillServiceDTO.getTranscationId() + " Channel -" + monthlyBillServiceDTO.getChannel() + " Msisdn -" + monthlyBillServiceDTO.getSubscriberNo() + " AccountNo -" + monthlyBillServiceDTO.getAccountNo());

			logger.info("=======ACCOUNT NO::::::::" + monthlyBillServiceDTO.getAccountNo());
			monthlyBillServiceDAO = new MonthlyBillServiceDAO();
			subscriber = monthlyBillServiceDTO.getAccountNo();
			if (subscriber != null)
				tableName = tableInfoDAO.getSubscriberNumberTable(subscriber);
			logger.info("=======TABLE NAME:::::::" + tableName);
			loyaltyId = getTransactionDetailsDAO.getLoyaltyId(tableName, subscriber);
			logger.info("Transaction id -" + monthlyBillServiceDTO.getTranscationId() + " The loyaltyID----> -" + loyaltyId);

			langId = languageDAO.getLanguageID(subscriber);
			if (langId == null)
				langId = Cache.defaultLanguageID;

			detailsDAO = new TableDetailsDAO();
			if (loyaltyId == null) {
				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "LOYALITY_NOT_" + langId, monthlyBillServiceDTO.getTranscationId());
				monthlyBillServiceDTO.setStatusCode(genericDTO.getStatusCode());
				monthlyBillServiceDTO.setStatusDescription(genericDTO.getStatus());

			} else {
				loyaltyProfileTabDTO = detailsDAO.getLoyaltyProfile(Long.parseLong(loyaltyId));
				monthlyBillServiceDTO.setAccountNo(monthlyBillServiceDTO.getAccountNo());
				monthlyBillServiceDTO.setLoyalityId(loyaltyProfileTabDTO.getLoyaltyID() + "");
				monthlyBillServiceDTO.setTierName(Cache.getTierInfoMap().get(loyaltyProfileTabDTO.getTierId()).getTierName());
				monthlyBillServiceDTO.setClosingBalance(loyaltyProfileTabDTO.getRewardPoints() + "");
				monthlyBillServiceDTO.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints() + "");
				monthlyBillServiceDTO.setStatmentDate(sdf.format(new Date()));

				monthlyBillServiceDTO = monthlyBillServiceDAO.getRewardPointDetails(monthlyBillServiceDTO, loyaltyProfileTabDTO);
				monthlyBillServiceDTO = monthlyBillServiceDAO.getRedeemPointDetails(monthlyBillServiceDTO, loyaltyProfileTabDTO);
				logger.info("Transaction id -" + monthlyBillServiceDTO.getTranscationId() + "CLOSING BALANCE" + Double.parseDouble(monthlyBillServiceDTO.getClosingBalance()));
				logger.info("Transaction id -" + monthlyBillServiceDTO.getTranscationId() + "CLOSING getPointsRedeemed" + Double.parseDouble(monthlyBillServiceDTO.getPointsRedeemed()));
				logger.info("Transaction id -" + monthlyBillServiceDTO.getTranscationId() + "CLOSING getPointsEarned" + Double.parseDouble(monthlyBillServiceDTO.getPointsEarned()));

				monthlyBillServiceDTO.setOpeningBalance((Double.parseDouble(monthlyBillServiceDTO.getClosingBalance()) +
						(monthlyBillServiceDTO.getPointsRedeemed() != null ? (Double.parseDouble(monthlyBillServiceDTO.getPointsRedeemed())) : 0)) -
						(monthlyBillServiceDTO.getPointsEarned() != null ? (Double.parseDouble(monthlyBillServiceDTO.getPointsEarned())) : 0) + "");
				logger.info("Transaction id -" + monthlyBillServiceDTO.getTranscationId() + "OPENING BALANCE" + (monthlyBillServiceDTO.getOpeningBalance()));

				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "MONTHLY_API_" + langId, monthlyBillServiceDTO.getTranscationId());
				monthlyBillServiceDTO.setStatusCode(genericDTO.getStatusCode());
				monthlyBillServiceDTO.setStatusDescription(genericDTO.getStatus());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "MONTHLY_API_NOT_" + langId, monthlyBillServiceDTO.getTranscationId());
			monthlyBillServiceDTO.setStatusCode(genericDTO.getStatusCode());
			monthlyBillServiceDTO.setStatusDescription(genericDTO.getStatus());

			e.printStackTrace();
		} finally {

		}

		return genericDTO;
	}
}
