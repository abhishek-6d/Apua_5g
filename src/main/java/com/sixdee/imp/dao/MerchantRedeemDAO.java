package com.sixdee.imp.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.MerchantRedeemDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

/**
 * 
 * @author Athul Gopal
 * @version 1.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%"><b>Date</b></td>
 *          <td width="20%"><b>Author</b></td>
 *          </tr>
 *          <tr>
 *          <td>April 21,2015 11:42:09 AM</td>
 *          <td>Athul Gopal</td>
 *          </tr>
 *          </table>
 *          </p>
 */

public class MerchantRedeemDAO {

	private static Logger logger = Logger.getLogger(MerchantRedeemDAO.class);

	TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
	CommonUtil commonUtil = new CommonUtil();
	LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;

	public void getSubsciberInfo(MerchantRedeemDTO merchantRedeemDTO) throws CommonException {

		try {

			long start = System.currentTimeMillis();
			SubscriberNumberTabDTO subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(merchantRedeemDTO.getSubscriberNumber()));

			if (subscriberNumberTabDTO != null) {

				logger.info(" get subscriber: --------------------------------->" + (System.currentTimeMillis() - start) + "      " + subscriberNumberTabDTO.getAccountTypeName() + "    " + subscriberNumberTabDTO.getAccountTypeId());

				if (Cache.accountTypeMap.get(subscriberNumberTabDTO.getAccountTypeId() + "_" + merchantRedeemDTO.getLanguageID()) != null) {

					merchantRedeemDTO.setSubscriberType(Cache.accountTypeMap.get(subscriberNumberTabDTO.getAccountTypeId() + "_" + merchantRedeemDTO.getLanguageID()));
					merchantRedeemDTO.setAccountTypeId(subscriberNumberTabDTO.getAccountTypeId().toString());
				}

				if (subscriberNumberTabDTO.getStatusID() != 1) {
					throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantRedeemDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());
				}
				merchantRedeemDTO.setLoyaltyID(subscriberNumberTabDTO.getLoyaltyID().toString());
			}

			if (merchantRedeemDTO.getLoyaltyID() == null) {
				AccountNumberTabDTO accountNumberTabDTO = tableDetailsDAO.getAccountNumberDetails(merchantRedeemDTO.getSubscriberNumber());
				if (accountNumberTabDTO != null) {

					if (accountNumberTabDTO.getStatusID() != 1)
						throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantRedeemDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());

					merchantRedeemDTO.setLoyaltyID(accountNumberTabDTO.getLoyaltyID().toString());

				}
			}

			if (merchantRedeemDTO.getLoyaltyID() != null) {

				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(Long.parseLong(merchantRedeemDTO.getLoyaltyID()));

			}

			if (loyaltyProfileTabDTO == null) {
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(Long.parseLong(merchantRedeemDTO.getSubscriberNumber()));
			}

			if (loyaltyProfileTabDTO == null) {

				logger.info(merchantRedeemDTO.getTransactionId() + ": Loyalty ID not exist for Subsriber Number :" + merchantRedeemDTO.getSubscriberNumber());
				throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + merchantRedeemDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());

			}

			if (loyaltyProfileTabDTO.getStatusID() != 1)
				throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantRedeemDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());

			merchantRedeemDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID().toString());
			merchantRedeemDTO.setTierId(loyaltyProfileTabDTO.getTierId().toString());
			merchantRedeemDTO.setTierName(Cache.tierLanguageInfoMap.get(merchantRedeemDTO.getTierId() + "_" + merchantRedeemDTO.getLanguageID()));

		} catch (CommonException e) {
			logger.info(merchantRedeemDTO.getTransactionId() + ": Service Failed :" + merchantRedeemDTO.getSubscriberNumber());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(merchantRedeemDTO.getTransactionId() + ": Service Failed :" + merchantRedeemDTO.getSubscriberNumber());
			throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());
		}

	}// getSubsciberInfo

	public void merchantRedeem(GenericDTO genericDTO) {

		try {

			MerchantRedeemDTO merchantRedeemDTO = (MerchantRedeemDTO) genericDTO.getObj();

			merchantRedeemDTO.setChannelId(Cache.channelDetails.get(merchantRedeemDTO.getChannel()));

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// merchantRedeem

	public boolean writeCDR(GenericDTO genericDTO) {
		
		MerchantRedeemDTO merchantRedeemDTO = (MerchantRedeemDTO) genericDTO.getObj();
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		try {

			// TRANSACTION,TIMESTAMP,SUBSCRIBER_NUMBER,MERCHANT_ID,DISCOUNT,CHANNEL,ACCOUNT_TYPE_ID,TIER_ID,LOYALTY_ID,BRANCH_ID,STATUS_CODE,STATUS,,,,

			String logWriter = merchantRedeemDTO.getTransactionId() + "|" + df.format(new Date()) + "|" + merchantRedeemDTO.getSubscriberNumber() + "|" + 
							   merchantRedeemDTO.getMerchantId() + "|" + merchantRedeemDTO.getDiscount() + "|" + 
					
							   (merchantRedeemDTO.getChannelId()!=null?merchantRedeemDTO.getChannelId():"") + "|" + 
							   (merchantRedeemDTO.getAccountTypeId()!=null?merchantRedeemDTO.getAccountTypeId():"") + "|" + 
							   (merchantRedeemDTO.getTierId()!=null&&!merchantRedeemDTO.getTierId().trim().equalsIgnoreCase("null")?merchantRedeemDTO.getTierId():"") + "|" + 
							   (merchantRedeemDTO.getLoyaltyID()!=null?merchantRedeemDTO.getLoyaltyID():"") + "|" +
							   (merchantRedeemDTO.getBranchId()!=null&&!merchantRedeemDTO.getBranchId().trim().equalsIgnoreCase("null")?merchantRedeemDTO.getBranchId():"") + "|" +
							   
							   genericDTO.getStatusCode() + "|" + genericDTO.getStatus() + "|" + "|" + "|" + "|" + "|";

			logger.warn(logWriter);

		} catch (Exception e) {
			
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());
			e.printStackTrace();
		}

		return true;
	}// writeCDR

}//
