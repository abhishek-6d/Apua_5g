package com.sixdee.imp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.MerchantRedeemDTO;
import com.sixdee.imp.dto.UserprofileDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.ChangePasswordDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantDetailsDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedeemReqDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedemptionDTO;
import com.sixdee.imp.service.serviceDTO.req.ReferralRequestDTO;
import com.sixdee.imp.service.serviceDTO.req.ServiceReqDTO;
import com.sixdee.imp.service.serviceDTO.req.SubscriberLanguageDTO;
import com.sixdee.imp.service.serviceDTO.req.UserProfileDTO;
import com.sixdee.imp.service.serviceDTO.resp.MerchantDiscountDTO;
import com.sixdee.imp.service.serviceDTO.resp.MerchantInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.MerchantRedeemResDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.UserDTO;
import com.sixdee.imp.service.serviceDTO.resp.VoucherPromoDTO;

public class UserProfileManagement {
	private static final Logger logger = Logger.getLogger(UserProfileManagement.class);

	// CommonUtil commonUtil=new CommonUtil();
	// LanguageDAO languageDAO = new LanguageDAO();
	// SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
	// boolean check=false;
	public UserDTO getUserProfile(UserProfileDTO userProfileDTO) {
		CommonUtil commonUtil = new CommonUtil();
		LanguageDAO languageDAO = new LanguageDAO();
		SubscriberListCheckDAO subslistDAO = null;
		boolean check = false;
		UserDTO profileDTO = null;
		String langId = null;
		LMSWebServiceAdapter adapter = null;
		UserprofileDTO dto = null;
		Data[] datas = null;
		Data data = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		GenericDTO genericDTO;
		try {

			// logger.info("INSIDE SERVICE CLASS");
			subslistDAO = new SubscriberListCheckDAO();
			subscriberNumber = userProfileDTO.getSubscriberNumber();
			txnId = userProfileDTO.getTransactionID();
			logger.info("Service : GetUserProfile  -- TRANSACTION ID : " + txnId + " SUBSCRIBER NUMBER: " + subscriberNumber + " Request Recieved in System");
			logger.debug("PIN::" + userProfileDTO.getPin());
			logger.debug("CHANNEL::" + userProfileDTO.getChannel());
			logger.debug("Language ID ::" + userProfileDTO.getLanguageId());

			langId = Cache.defaultLanguageID;
			boolean valid = false;

			profileDTO = new UserDTO();

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				profileDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				profileDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());

			}

			if (userProfileDTO.getLanguageId() == null || userProfileDTO.getLanguageId().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				userProfileDTO.setLanguageId(langId);
			} else {
				langId = userProfileDTO.getLanguageId();
			}

			if (userProfileDTO.getData() != null && userProfileDTO.getData().length > 0) {
				List<Data> list = new ArrayList<Data>(Arrays.asList(userProfileDTO.getData()));
				logger.info("the size of the list---->"+list.size());
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null && list.get(i).getName() != null && !list.get(i).getName().trim().equals("") && list.get(i).getName().equalsIgnoreCase("CUST_ID"))
						valid = true;
				}

			}
			
			logger.info("The value of valid---->"+valid);

			if (!valid && commonUtil.isItChar(subscriberNumber)) {
				logger.info("ADSL Checking");
				profileDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				profileDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return profileDTO;
			}

			if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
				if (userProfileDTO.getChannel().equalsIgnoreCase("SMS") || userProfileDTO.getChannel().equalsIgnoreCase("LMS_WEB") || userProfileDTO.getChannel().equalsIgnoreCase("USSD")) {
					check = subslistDAO.checkSubscriber(subscriberNumber);
					if (check) {
					} else {
						profileDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
						profileDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						return profileDTO;
					}

				}
			}

			adapter = new LMSWebServiceAdapter();
			genericDTO = (GenericDTO) adapter.callFeature("Userprofile", userProfileDTO);

			// UserDTO profileDTO=new UserDTO();
			profileDTO.setTimestamp(userProfileDTO.getTimestamp());
			profileDTO.setTranscationId(txnId);
			
			if (genericDTO == null) {
				logger.info("##Failure###");
				// profileDTO.setStatusCode("SC1000");//profileDTO.setStatusDescription("FAILURE");

				profileDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_FAIL_" + langId).getStatusCode());
				profileDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_USER_PROFILE_FAIL_" + langId).getStatusDesc());
				logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getStatusDescription());
				return profileDTO;
			}
			

			else if (genericDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
                    logger.info("##Success###");
				dto = (UserprofileDTO) genericDTO.getObj();
				// profileDTO.setStatusCode("SC0000");//profileDTO.setStatusDescription("SUCCESS");
				/*profileDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + langId).getStatusCode());
				profileDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + langId).getStatusDesc());
				*/
				profileDTO.setStatusCode("SC0000");
				profileDTO.setStatusDescription("SUCCESS");
				logger.info("dto.isDealOfDay()>>>>>>>>" + dto.isDealOfDay());
				logger.info("isActive()>>>>>>>>" + dto.isActive());
				if(dto.isDealOfDay()){
					datas = new Data[2];
					data = new Data();
					data.setName("IS_DOD_ELIGIBLE");
					if(dto.isActive())
					data.setValue("YES");
					else
					data.setValue("NO");	
					datas[0] = data;
					
					data = new Data();
					data.setName("DOD_ACCESS_CODE");
					if(dto.isActive())
					data.setValue(dto.getPromoCode());
					datas[1] = data;
					profileDTO.setData(datas);
				}
				else{
				logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getStatusDescription());
				profileDTO.setFirstName(dto.getFirstName());
				profileDTO.setLastName(dto.getLastName());
				profileDTO.setLoyaltyID(dto.getLoyaltyID() + "");
				// profileDTO.setAccountNumber(dto.getAccountNumber()!=null?dto.getAccountNumber()+"":null);
				profileDTO.setContactNumber(dto.getContactNumber());
				// profileDTO.setDateOfBirth(dto.getDateOfBirth());
				profileDTO.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth() + "" : null);
				profileDTO.setAddress(dto.getAddress());
				profileDTO.setEmailID(dto.getEmailID());
				profileDTO.setVipCode(dto.getVipCode());
				profileDTO.setCategory(dto.getCategory());
				profileDTO.setOccupation(dto.getOccupation());
				profileDTO.setIndustry(dto.getIndustry());
				profileDTO.setDefaultLanguage(dto.getDefaultLanguage());
				profileDTO.setNationalID(dto.getNationalID());
				logger.info(">>>status>>>"+dto.getStatusID());
				if(dto.getStatusID()==1)
				profileDTO.setStatus("Active");
				else{
					profileDTO.setStatus("InActive");
				}
				int i=0;
				datas = new Data[4];
				// Setting StatusId
				//datas = new Data[20];
				/*if (dto.getExpiryDate() != null && dto.getExpiryRewardPoint() != null)
					datas = new Data[11];
				else
					datas = new Data[8];
				
				data = new Data();
				data.setName("StatusId");
				data.setValue("" + dto.getStatusID());
				datas[i] = data;
				i++;
				
				data = new Data();
				data.setName("CREATE_TIME");
				data.setValue("" + dto.getCreateTime());
				datas[i] = data;
				i++;
				
				data = new Data();
				data.setName("NEXT_TIER");
				data.setValue(dto.getNextTierName());
				datas[i] = data;
				i++;
				
				data = new Data();
				data.setName("POINTS_TO_NEXT_TIER");
				data.setValue(dto.getPointsToNextTier());
				datas[i] = data;
				i++;

				logger.info("CREATE TIME::" + dto.getCreateTime());
				
				logger.info("EXPIRY DATE::" + dto.getExpiryDate());
				logger.info("EXPIRY REWARD POINTS::" + dto.getExpiryRewardPoint());
*/
				//if (dto.getExpiryDate() != null && dto.getExpiryRewardPoint() != null) {
					logger.info("INSIDE DATA TAG::");
					data = new Data();
					data.setName("EXPIRY_DATE");
					data.setValue("2018-09-30");
					datas[i] = data;
					i++;
				//}
				data = new Data();
				data.setName("EXPIRY_POINTS");
				data.setValue("0");
				datas[i] = data;
				i++;
				
				data = new Data();
				data.setName("TIER_POINTS");
				data.setValue("" + dto.getTierpoints());
				datas[i] = data;
				i++;
				
				data = new Data();
				data.setName("BONUS_POINTS");
				data.setValue("" + dto.getBonuspoints());
				datas[i] = data;
				i++;
				
               
				

				profileDTO.setData(datas);
				logger.info("SIZE OF DATA TAG::" + profileDTO.getData().length);

				// display Active_InActive
				//profileDTO.setStatus(dto.getStatusDesc());
				// profileDTO.setStatusUpdateDate(dto.getStatusUpdatedDate().toString());
				// profileDTO.setStatusId(dto.getStatusID());
				profileDTO.setStatusUpdateDate(dto.getStatusUpdatedDate() != null ? dto.getStatusUpdatedDate() + "" : "");
				// profileDTO.setTier(dto.getTierId().toString());
				profileDTO.setTier(dto.getTierName() != null ? dto.getTierName() + "" : null);
				// profileDTO.setTierUpdateDate(dto.getTierUpdatedDate().toString());
				profileDTO.setTierUpdateDate(dto.getTierUpdatedDate() != null ? dto.getTierUpdatedDate() + "" : "");
				// profileDTO.setStatusPoints(dto.getStatusPoints().toString());
				profileDTO.setStatusPoints(dto.getStatusPoints() + "");
				// profileDTO.setRewardPoints(dto.getRewardPoints().toString());
				profileDTO.setRewardPoints(dto.getRewardPoints() + "");
				profileDTO.setTiername(dto.getTierName());
				}
			}

			else if (genericDTO.getStatusCode().equalsIgnoreCase("SC1000") && genericDTO.getStatus() != null) {

				// profileDTO.setStatusCode("SC1000");
				profileDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_FAIL_" + langId).getStatusCode());
				profileDTO.setStatusDescription(genericDTO.getStatus());
				logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getStatusDescription());

			} else if (genericDTO.getStatusCode().equalsIgnoreCase("SC0001") && genericDTO.getStatus() != null) {

				// profileDTO.setStatusCode("SC1000");
				//profileDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + langId).getStatusCode());
				profileDTO.setStatusCode(genericDTO.getStatusCode());
				profileDTO.setStatusDescription(genericDTO.getStatus());
				logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getStatusDescription());
			} else {
				logger.info("Failure");
				if(genericDTO.getStatus()==null && genericDTO.getStatusCode()==null){
				profileDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + langId).getStatusCode());
				profileDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + langId).getStatusDesc());
				}
				else{
					profileDTO.setStatusCode(genericDTO.getStatusCode());
					profileDTO.setStatusDescription(genericDTO.getStatus());
				}
				logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getStatusDescription());
			}

			logger.info("OUTSIDE SERVICE CLASS");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Service : GetUserProfile - Transaction ID : " + txnId + "Exception= ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;
			subslistDAO = null;
			langId = null;
			adapter = null;
			dto = null;
			datas = null;
			data = null;
			genericDTO=null;
		}
		return profileDTO;

	}

	public ResponseDTO forgotPassword(UserProfileDTO userProfileDTO) {
		CommonUtil commonUtil = new CommonUtil();
		LanguageDAO languageDAO = new LanguageDAO();
		SubscriberListCheckDAO subslistDAO = new SubscriberListCheckDAO();
		boolean check = false;
		ResponseDTO responseDTO = null;
		String langId = null;
		LMSWebServiceAdapter adapter = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		try {
			txnId = userProfileDTO.getTransactionID();
			subscriberNumber = userProfileDTO.getSubscriberNumber();
			logger.info("Service : ForgotPassword -- Transaction ID : " + txnId + "Subscriber Number : " + subscriberNumber + " Request recieved in System");
			logger.debug("Pin : " + userProfileDTO.getPin());
			logger.debug("Channel : " + userProfileDTO.getChannel());
			logger.debug("Language ID : " + userProfileDTO.getLanguageId());

			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(userProfileDTO.getTimestamp());

			if (userProfileDTO.getLanguageId() == null || userProfileDTO.getLanguageId().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				userProfileDTO.setLanguageId(langId);
			} else {
				langId = userProfileDTO.getLanguageId().trim();
			}

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (subscriberNumber != null && commonUtil.isItChar(subscriberNumber)) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (userProfileDTO.getChannel() == null || userProfileDTO.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
				if (userProfileDTO.getChannel().equalsIgnoreCase("SMS") || userProfileDTO.getChannel().equalsIgnoreCase("LMS_WEB") || userProfileDTO.getChannel().equalsIgnoreCase("USSD")) {
					check = subslistDAO.checkSubscriber(subscriberNumber);
					if (check) {
					} else {
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						return responseDTO;
					}

				}
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("ForgotPassword", userProfileDTO);

			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("FGPWD_FAIL_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("FGPWD_FAIL_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("FGPWD_FAIL_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (genericDTO.getStatusCode() != null) {

				responseDTO.setStatusCode(genericDTO.getStatusCode());
				responseDTO.setStatusDescription(genericDTO.getStatus());

			} else {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("FGPWD_FAIL_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("FGPWD_FAIL_" + langId).getStatusDesc());
			}
		} catch (Exception e) {
			logger.error("Service : ForgotPassword -- Transaction ID : " + txnId + " Exception = ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;
			subslistDAO = null;
			langId = null;
			adapter = null;
		}

		return responseDTO;

	}// forgotPassword

	public ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO) {
		CommonUtil commonUtil = new CommonUtil();
		LanguageDAO languageDAO = new LanguageDAO();
		SubscriberListCheckDAO subslistDAO = new SubscriberListCheckDAO();
		boolean check = false;
		String langId = null;
		LMSWebServiceAdapter adapter = null;

		ResponseDTO responseDTO = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		try {
			txnId = changePasswordDTO.getTransactionID();
			subscriberNumber = changePasswordDTO.getSubscriberNumber();
			logger.info("Service : Change_Password -- Transaction ID : " + txnId + " Subscriber Number : " + subscriberNumber);
			logger.debug("Old Pin : " + changePasswordDTO.getOldPin() + " New Pin : " + changePasswordDTO.getNewPin() + " Confirm New Pin : " + changePasswordDTO.getConfirmPin() + " Channel : " + changePasswordDTO.getChannel() + " Language ID : " + changePasswordDTO.getLanguageId());

			langId = (changePasswordDTO.getLanguageId() != null && !changePasswordDTO.getLanguageId().trim().equals("") ? changePasswordDTO.getLanguageId() : Cache.defaultLanguageID);

			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(changePasswordDTO.getTimestamp());

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (changePasswordDTO.getLanguageId() == null || changePasswordDTO.getLanguageId().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				changePasswordDTO.setLanguageId(langId);
			}

			if (subscriberNumber != null && commonUtil.isItChar(subscriberNumber)) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (changePasswordDTO.getChannel() == null || changePasswordDTO.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
				if (changePasswordDTO.getChannel().equalsIgnoreCase("SMS") || changePasswordDTO.getChannel().equalsIgnoreCase("LMS_WEB") || changePasswordDTO.getChannel().equalsIgnoreCase("USSD")) {
					check = subslistDAO.checkSubscriber(subscriberNumber);
					if (check) {
					} else {
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						return responseDTO;
					}

				}
			}

			if (changePasswordDTO.getOldPin() <= 0) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHPWD_OLD_PIN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHPWD_OLD_PIN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHPWD_OLD_PIN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (changePasswordDTO.getNewPin() <= 0) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("PIN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("PIN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("PIN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if ((changePasswordDTO.getOldPin() + "").length() != Integer.parseInt(Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue())) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("PIN_MIZ_SIZE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("PIN_MIZ_SIZE_" + langId).getStatusDesc(), new String[] { NotificationTokens.loyaltyPinSize }, new String[] { Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue() }));
				logger.info(commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("PIN_MIZ_SIZE_" + langId).getStatusDesc(), new String[] { NotificationTokens.loyaltyPinSize }, new String[] { Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue() }));
				return responseDTO;
			}

			if ((changePasswordDTO.getNewPin() + "").length() != Integer.parseInt(Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue())) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("PIN_MIZ_SIZE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("PIN_MIZ_SIZE_" + langId).getStatusDesc(), new String[] { NotificationTokens.loyaltyPinSize }, new String[] { Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue() }));
				logger.info(commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("PIN_MIZ_SIZE_" + langId).getStatusDesc(), new String[] { NotificationTokens.loyaltyPinSize }, new String[] { Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue() }));
				return responseDTO;
			}

			if (changePasswordDTO.getOldPin() == changePasswordDTO.getNewPin()) {

				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHPWD_OLDPIN_NEWPIN_SAME_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHPWD_OLDPIN_NEWPIN_SAME_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHPWD_OLDPIN_NEWPIN_SAME_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (changePasswordDTO.getNewPin() != changePasswordDTO.getConfirmPin()) {

				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHPWD_NEW_CONFIRM_MISMATCH_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHPWD_NEW_CONFIRM_MISMATCH_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHPWD_NEW_CONFIRM_MISMATCH_" + langId).getStatusDesc());
				return responseDTO;
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("ChangePassword", changePasswordDTO);

			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHPWD_FAIL_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHPWD_FAIL_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHPWD_FAIL_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (genericDTO.getStatusCode() != null) {

				responseDTO.setStatusCode(genericDTO.getStatusCode());
				responseDTO.setStatusDescription(genericDTO.getStatus());

			} else {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHPWD_FAIL_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHPWD_FAIL_" + langId).getStatusDesc());
			}
		} catch (Exception e) {
			logger.info("Exception = ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;
			subslistDAO = null;
			langId = null;
			adapter = null;

		}

		return responseDTO;

	}// changePassword

	public ResponseDTO changeSubscriberLanguage(SubscriberLanguageDTO subscriberLanguageDTO) {

		CommonUtil commonUtil = new CommonUtil();
		LanguageDAO languageDAO = new LanguageDAO();
		SubscriberListCheckDAO subslistDAO = new SubscriberListCheckDAO();
		boolean check = false;
		String langId = null;
		LMSWebServiceAdapter adapter = null;

		ResponseDTO responseDTO = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		try {
			txnId = subscriberLanguageDTO.getTransactionId();
			subscriberNumber = subscriberLanguageDTO.getMoNumber();
			logger.info("Service : ChangeSubscriberLanguage -- Transaction ID : " + txnId + " Subscriber Number : " + subscriberNumber + " Request Recieved in System");
			logger.debug("Channel : " + subscriberLanguageDTO.getChannel() + " Language ID : " + subscriberLanguageDTO.getLanguageID() + " Change Language ID : " + subscriberLanguageDTO.getChangeLanguageID());

			langId = (subscriberLanguageDTO.getLanguageID() != null && !subscriberLanguageDTO.getLanguageID().trim().equals("") ? subscriberLanguageDTO.getLanguageID() : Cache.defaultLanguageID);

			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(subscriberLanguageDTO.getTimestamp());

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (subscriberLanguageDTO.getLanguageID() == null || subscriberLanguageDTO.getLanguageID().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				subscriberLanguageDTO.setLanguageID(langId);
			}

			if (subscriberNumber != null && commonUtil.isItChar(subscriberNumber)) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (subscriberLanguageDTO.getChannel() == null || subscriberLanguageDTO.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
				if (subscriberLanguageDTO.getChannel().equalsIgnoreCase("SMS") || subscriberLanguageDTO.getChannel().equalsIgnoreCase("LMS_WEB") || subscriberLanguageDTO.getChannel().equalsIgnoreCase("USSD")) {
					check = subslistDAO.checkSubscriber(subscriberNumber);
					if (check) {
					} else {
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						return responseDTO;
					}

				}
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("ChangeLanguage", subscriberLanguageDTO);

			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHPWD_FAIL_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (genericDTO.getObj() != null) {

				responseDTO.setStatusCode(genericDTO.getStatusCode());
				responseDTO.setStatusDescription(genericDTO.getStatus());
				responseDTO = (ResponseDTO) genericDTO.getObj();

			} else {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
			}
		} catch (Exception e) {
			logger.error("Transaction ID : " + txnId + " Exception = ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;
			subslistDAO = null;
			langId = null;
			adapter = null;
		}

		return responseDTO;

	}

	public ResponseDTO startService(ServiceReqDTO serviceReqDTO) {
		CommonUtil commonUtil = new CommonUtil();
		SubscriberListCheckDAO subslistDAO = new SubscriberListCheckDAO();
		boolean check = false;
		String langId = Cache.defaultLanguageID;
		LMSWebServiceAdapter adapter = null;

		ResponseDTO responseDTO = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		try {
			txnId = serviceReqDTO.getTransactionId();
			subscriberNumber = serviceReqDTO.getSubscriberNumber();
			logger.info(" Service : startService -- Transaction ID : " + txnId + " Subscriber Number : " + subscriberNumber + " Request Recieved in System");
			logger.debug("Channel : " + serviceReqDTO.getChannel());

			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(serviceReqDTO.getTimestamp());

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (subscriberNumber != null && commonUtil.isItChar(subscriberNumber)) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (serviceReqDTO.getChannel() == null || serviceReqDTO.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			/*
			 * if(serviceReqDTO.getServiceName()==null || serviceReqDTO.getServiceName().equalsIgnoreCase("") ) { responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SERVICE_REQ_"+langId).getStatusCode()); responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SERVICE_REQ_"+langId).getStatusDesc()); logger.info(Cache.getServiceStatusMap().get("SERVICE_REQ_"+langId).getStatusDesc()); return responseDTO; }
			 */

			if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
				if (serviceReqDTO.getChannel().equalsIgnoreCase("SMS") || serviceReqDTO.getChannel().equalsIgnoreCase("LMS_WEB") || serviceReqDTO.getChannel().equalsIgnoreCase("USSD")) {
					check = subslistDAO.checkSubscriber(subscriberNumber);
					if (check) {
					} else {
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						return responseDTO;
					}

				}
			}

			if (serviceReqDTO.getData() != null && serviceReqDTO.getData()[0] != null) {
				if (serviceReqDTO.getData()[0].getName() != null && !serviceReqDTO.getData()[0].getName().equalsIgnoreCase("") && serviceReqDTO.getData()[0].getName().equalsIgnoreCase("LanguageID")) {
					logger.info("Requested Language Id = " + serviceReqDTO.getData()[0].getValue());
				} else {
					logger.info("Default Language Id = " + Cache.defaultLanguageID);
				}
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("StartService", serviceReqDTO);

			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (genericDTO.getObj() != null) {

				/*
				 * responseDTO.setStatusCode(genericDTO.getStatusCode()); responseDTO.setStatusDescription(genericDTO.getStatus());
				 */
				responseDTO = (ResponseDTO) genericDTO.getObj();
				logger.info("responseDTO statusCode:" + responseDTO.getStatusCode());
				logger.info("responseDTO statusDesc:" + responseDTO.getStatusDescription());

			} else {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
			}
		} catch (Exception e) {
			logger.error("Transaction ID : " + txnId + " Exception = ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			subslistDAO = null;
			langId = null;
			adapter = null;
		}

		return responseDTO;

	}
	
	public ResponseDTO terminationService(ServiceReqDTO serviceReqDTO) {
		CommonUtil commonUtil = new CommonUtil();
		SubscriberListCheckDAO subslistDAO = new SubscriberListCheckDAO();
		boolean check = false;
		String langId = Cache.defaultLanguageID;
		LMSWebServiceAdapter adapter = null;

		ResponseDTO responseDTO = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		try {
			txnId = serviceReqDTO.getTransactionId();
			subscriberNumber = serviceReqDTO.getSubscriberNumber();
			logger.info(" Service : terminationService -- Transaction ID : " + txnId + " Subscriber Number : " + subscriberNumber + " Request Recieved in System");
			logger.debug("Channel : " + serviceReqDTO.getChannel());

			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(serviceReqDTO.getTimestamp());

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (subscriberNumber != null && commonUtil.isItChar(subscriberNumber)) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (serviceReqDTO.getChannel() == null || serviceReqDTO.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			/*
			 * if(serviceReqDTO.getServiceName()==null || serviceReqDTO.getServiceName().equalsIgnoreCase("") ) { responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SERVICE_REQ_"+langId).getStatusCode()); responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SERVICE_REQ_"+langId).getStatusDesc()); logger.info(Cache.getServiceStatusMap().get("SERVICE_REQ_"+langId).getStatusDesc()); return responseDTO; }
			 */

			if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
				if (serviceReqDTO.getChannel().equalsIgnoreCase("SMS") || serviceReqDTO.getChannel().equalsIgnoreCase("LMS_WEB") || serviceReqDTO.getChannel().equalsIgnoreCase("USSD")) {
					check = subslistDAO.checkSubscriber(subscriberNumber);
					if (check) {
					} else {
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
						return responseDTO;
					}

				}
			}

			if (serviceReqDTO.getData() != null && serviceReqDTO.getData()[0] != null) {
				if (serviceReqDTO.getData()[0].getName() != null && !serviceReqDTO.getData()[0].getName().equalsIgnoreCase("") && serviceReqDTO.getData()[0].getName().equalsIgnoreCase("LanguageID")) {
					logger.info("Requested Language Id = " + serviceReqDTO.getData()[0].getValue());
				} else {
					logger.info("Default Language Id = " + Cache.defaultLanguageID);
				}
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("TerminationService", serviceReqDTO);

			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (genericDTO.getObj() != null) {

				/*
				 * responseDTO.setStatusCode(genericDTO.getStatusCode()); responseDTO.setStatusDescription(genericDTO.getStatus());
				 */
				responseDTO = (ResponseDTO) genericDTO.getObj();
				logger.info("responseDTO statusCode:" + responseDTO.getStatusCode());
				logger.info("responseDTO statusDesc:" + responseDTO.getStatusDescription());

			} else {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LANG_FAILURE_" + langId).getStatusDesc());
			}
		} catch (Exception e) {
			logger.error("Transaction ID : " + txnId + " Exception = ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			subslistDAO = null;
			langId = null;
			adapter = null;
		}

		return responseDTO;

	}

	public MerchantDiscountDTO getMerchantDiscountDetails(MerchantDetailsDTO detailsDTO) {

		MerchantDiscountDTO discountDTO = null;
		LanguageDAO languageDAO = new LanguageDAO();
		LMSWebServiceAdapter adapter = null;

		String subscriberNumber = detailsDTO.getSubscriberNumber();
		String merchantId = detailsDTO.getMerchantId();
		String langId = Cache.defaultLanguageID;

		boolean valid = false;

		try {

			discountDTO = new MerchantDiscountDTO();
			discountDTO.setTimestamp(detailsDTO.getTimestamp());
			discountDTO.setTranscationId(detailsDTO.getTransactionId());

			logger.info(" --------------------------   In 'getmerchantDiscountDetails' service --------------------------------- ");
			logger.info("Service : GetUserProfile  -- TRANSACTION ID : " + detailsDTO.getTransactionId() + " SUBSCRIBER NUMBER: " + detailsDTO.getSubscriberNumber() + " Request Recieved in System");
			logger.debug("PIN::" + detailsDTO.getPin());
			logger.debug("CHANNEL::" + detailsDTO.getChannel());
			logger.debug("Language ID ::" + detailsDTO.getLanguageID());
			logger.debug("MERCHANT ID ::" + detailsDTO.getMerchantId());

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {

				discountDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_" + langId).getStatusCode());
				discountDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_" + langId).getStatusDesc());

				return discountDTO;

			}

			if (merchantId == null || merchantId.trim().equalsIgnoreCase("")) {

				discountDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_ID_" + langId).getStatusCode());
				discountDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_ID_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_ID_" + langId).getStatusDesc());

				return discountDTO;

			}

			if (detailsDTO.getLanguageID() == null || detailsDTO.getLanguageID().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				detailsDTO.setLanguageID(langId);
			} else {
				langId = detailsDTO.getLanguageID();
			}

			if (detailsDTO.getData() != null && detailsDTO.getData().length > 0) {
				List<Data> list = new ArrayList<Data>(Arrays.asList(detailsDTO.getData()));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null && list.get(i).getName() != null && !list.get(i).getName().trim().equals("") && list.get(i).getName().equalsIgnoreCase("CUST_ID"))
						valid = true;
				}

			}

			/*if (!valid && commonUtil.isItChar(subscriberNumber)) {
				logger.info("ADSL Checking");
				discountDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + langId).getStatusCode());
				discountDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + langId).getStatusDesc());

				return discountDTO;
			}*/

			// //////////////////////////////CALL FRAMEWORK//////////////////////////////////////////
			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("MerchantDiscount", detailsDTO);


			if (genericDTO == null) {

				logger.info("##Failure###");
				// profileDTO.setStatusCode("SC1000");//profileDTO.setStatusDescription("FAILURE");

				discountDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusCode());
				discountDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + "DESCRIPTION>>>>>>>>" + discountDTO.getStatusDescription());
				return discountDTO;
			}

			com.sixdee.imp.dto.MerchantDiscountDTO dataDTO = (com.sixdee.imp.dto.MerchantDiscountDTO) genericDTO.getObj();
			
			
			
			if(genericDTO.getStatusCode()!=null)
			{
				discountDTO.setStatusCode(genericDTO.getStatusCode());
				discountDTO.setStatusDescription(genericDTO.getStatus());
			}else{
				discountDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusCode());
				discountDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusDesc());
			}

			if (dataDTO != null) {

				discountDTO.setDiscoutValue(dataDTO.getDiscount());
				discountDTO.setMerchantId(dataDTO.getMerchantId());
				discountDTO.setSubscriberNumber(dataDTO.getSubscriberNumber());
				discountDTO.setSubscriberType(dataDTO.getSubscriberType() + "");
				discountDTO.setTierId(dataDTO.getTierId());
				discountDTO.setTierName(dataDTO.getTierName());
				logger.info(detailsDTO.getTransactionId() + " : " + "DESCRIPTION>>>>>>>>" + discountDTO.getStatusDescription());

			} 

		} catch (Exception e) {

			discountDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusCode());
			discountDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusDesc());
			logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + langId).getStatusDesc());
			logger.info(detailsDTO.getTransactionId() + " : " + "DESCRIPTION>>>>>>>>" + discountDTO.getStatusDescription());
			logger.info(detailsDTO.getTransactionId() + " : " ,e);

		}

		return discountDTO;

	}// getmerchantDiscountDetails

	public MerchantRedeemResDTO getMerchantDiscountRedeem(MerchantRedeemReqDTO detailsDTO) {

		MerchantRedeemResDTO redeemResDTO = new MerchantRedeemResDTO();
		LanguageDAO languageDAO = new LanguageDAO();
		LMSWebServiceAdapter adapter = null;

		String subscriberNumber = detailsDTO.getSubscriberNumber();
		String merchantId = detailsDTO.getMerchantId();
		String langId = Cache.defaultLanguageID;

		try {

			logger.info(" --------------------------   In 'getmerchantDiscountDetails' service --------------------------------- ");
			logger.info("Service : GetUserProfile  -- TRANSACTION ID : " + detailsDTO.getTransactionId() + " SUBSCRIBER NUMBER: " + detailsDTO.getSubscriberNumber() + " Request Recieved in System");
			logger.debug("PIN::" + detailsDTO.getPin());
			logger.debug("CHANNEL::" + detailsDTO.getChannel());
			logger.debug("Language ID ::" + detailsDTO.getLanguageID());

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {

				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_REDEEM_" + langId).getStatusDesc());

				return redeemResDTO;

			}

			if (merchantId == null || merchantId.trim().equalsIgnoreCase("")) {

				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_ID_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_ID_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_ID_REDEEM_" + langId).getStatusDesc());

				return redeemResDTO;

			}

			if (detailsDTO.getDiscount() == null || detailsDTO.getDiscount().trim().equalsIgnoreCase("")) {

				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_DIS_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_DIS_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_DIS_REDEEM_" + langId).getStatusDesc());

				return redeemResDTO;

			}

			if (detailsDTO.getLanguageID() == null || detailsDTO.getLanguageID().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				detailsDTO.setLanguageID(langId);
			} else {
				langId = detailsDTO.getLanguageID();
			}

			/*if (detailsDTO.getData() != null && detailsDTO.getData().length > 0) {
				List<Data> list = new ArrayList<Data>(Arrays.asList(detailsDTO.getData()));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null && list.get(i).getName() != null && !list.get(i).getName().trim().equals("") && list.get(i).getName().equalsIgnoreCase("CUST_ID"))
						valid = true;
				}

			}

			if (!valid && commonUtil.isItChar(subscriberNumber)) {
				logger.info("ADSL Checking");
				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_REDEEM_" + langId).getStatusDesc());

				return redeemResDTO;
			}*/

			// //////////////////////////////CALL FRAMEWORK//////////////////////////////////////////
			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("MerchantRedeem", detailsDTO);

			redeemResDTO.setTimestamp(detailsDTO.getTimestamp());
			redeemResDTO.setTranscationId(detailsDTO.getTransactionId());

			if (genericDTO == null) {

				logger.info("##Failure###");
				// profileDTO.setStatusCode("SC1000");//profileDTO.setStatusDescription("FAILURE");

				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + "DESCRIPTION>>>>>>>>" + redeemResDTO.getStatusDescription());
				return redeemResDTO;
			}

			MerchantRedeemDTO dataDTO = (MerchantRedeemDTO) genericDTO.getObj();

			if (dataDTO != null && (genericDTO.getStatusCode() == null || (genericDTO.getStatusCode() != null && genericDTO.getStatusCode().trim().equalsIgnoreCase("SC0000")))) {

				redeemResDTO.setDiscoutValue(dataDTO.getDiscount());
				redeemResDTO.setMerchantId(dataDTO.getMerchantId());
				redeemResDTO.setSubscriberNumber(dataDTO.getSubscriberNumber());
				redeemResDTO.setSubscriberType(dataDTO.getSubscriberType() + "");
				redeemResDTO.setTierName(dataDTO.getTierName());
				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + "DESCRIPTION>>>>>>>>" + redeemResDTO.getStatusDescription());

			} else {

				redeemResDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_REDEEM_" + langId).getStatusCode());
				redeemResDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_REDEEM_" + langId).getStatusDesc());
				logger.info(detailsDTO.getTransactionId() + " : " + "DESCRIPTION>>>>>>>>" + redeemResDTO.getStatusDescription());

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return redeemResDTO;

	}// getMerchantDiscountRedeem
	
	public ResponseDTO referralService(ReferralRequestDTO referralReqDTO)
	{
		CommonUtil commonUtil = new CommonUtil();
		String langId = null;
		LMSWebServiceAdapter adapter = null;
		ResponseDTO responseDTO = null;
		long t1 = System.currentTimeMillis();
		try {
			logger.info("Service : ReferralService -- Transaction ID : " + referralReqDTO.getTransactionId()+" Request Recieved in System");
			logger.debug("Channel : " + referralReqDTO.getChannel());
			logger.info("Referrer Number : " + referralReqDTO.getReferrer() +" Referee Number : " + referralReqDTO.getReferee() + " Referral Date"+referralReqDTO.getReferralDate());

			langId = Cache.defaultLanguageID;

			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(referralReqDTO.getTransactionId());
			responseDTO.setTimestamp(referralReqDTO.getTimestamp());

			if (referralReqDTO.getReferrer() == null || referralReqDTO.getReferrer().trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REFERRER_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REFERRER_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REFERRER_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (referralReqDTO.getReferrer() != null && commonUtil.isItChar(referralReqDTO.getReferrer())) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REFERRER_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REFERRER_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REFERRER_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}
			
			if (referralReqDTO.getReferee() == null || referralReqDTO.getReferee().trim().equals("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REFEREE_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REFEREE_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REFEREE_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}
			
			if (referralReqDTO.getReferrer() != null && commonUtil.isItChar(referralReqDTO.getReferee())) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REFEREE_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REFEREE_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REFEREE_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}
			
			if (referralReqDTO.getChannel() == null || referralReqDTO.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("ReferralService", referralReqDTO);

			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REFERRAL_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REFERRAL_FAILURE_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REFERRAL_FAILURE_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (genericDTO.getObj() != null) {

				responseDTO.setStatusCode(genericDTO.getStatusCode());
				responseDTO.setStatusDescription(genericDTO.getStatus());
				responseDTO = (ResponseDTO) genericDTO.getObj();

			} else {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REFERRAL_FAILURE_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REFERRAL_FAILURE_" + langId).getStatusDesc());
			}
		} catch (Exception e) {
			logger.error("Transaction ID : " + referralReqDTO.getTransactionId() + " Exception = ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + referralReqDTO.getTransactionId() + " Request Leaving System , Processing Time " + (t2 - t1));
			langId = null;
			adapter = null;
		}

		return responseDTO;
	}//referralService
	
	@SuppressWarnings("unused")
	public ResponseDTO getMerchantRedemption(MerchantRedemptionDTO merchantRedemption)
	{
		VoucherPromoDTO voucherPromoDTO=null;
		CommonUtil commonUtil = new CommonUtil();
		String langId=null;
		LanguageDAO languageDAO = new LanguageDAO();
		String subscriberNumber = null;
		String txnId = null;
		LMSWebServiceAdapter adapter = null;
		ResponseDTO responseDTO = new ResponseDTO(); 
		long t1 = System.currentTimeMillis();
		logger.info("Inside getMerchantRedemption()");
	
		try {
			txnId=merchantRedemption.getTransactionId();
			subscriberNumber = merchantRedemption.getSubscriberNumber();
			voucherPromoDTO=new VoucherPromoDTO();
			logger.info("Service : ReferralService -- Transaction ID : " + txnId+" Request Recieved in System");
			logger.debug("Channel : " + merchantRedemption.getChannel());
			logger.info("Subscriber Number : " + subscriberNumber +" Merchant ID : " + merchantRedemption.getMerchantId());

				voucherPromoDTO.setTranscationId(merchantRedemption.getTransactionId());	
				voucherPromoDTO.setTimestamp(merchantRedemption.getTimestamp());
				
				if (merchantRedemption.getLanguageId() == null || merchantRedemption.getLanguageId().trim().equals("")) {
					langId = languageDAO.getLanguageID(subscriberNumber);
					merchantRedemption.setLanguageId(langId);
				} else {
					langId = merchantRedemption.getLanguageId().trim();
				}
				
				
			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (subscriberNumber != null && commonUtil.isItChar(subscriberNumber)) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
				return responseDTO;
			}

			if (merchantRedemption.getChannel() == null || merchantRedemption.getChannel().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}
			
			if (merchantRedemption.getMerchantId() == null || merchantRedemption.getMerchantId().equalsIgnoreCase("")) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("MERCHANT_REQ_" + langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("MERCHANT_REQ_" + langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("MERCHANT_REQ_" + langId).getStatusDesc());
				return responseDTO;
			}

			
			
			/*if (merchantRedemption.getData() != null && merchantRedemption.getData().length > 0) {
				List<Data> list = new ArrayList<Data>(Arrays.asList(merchantRedemption.getData()));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null && list.get(i).getName() != null && !list.get(i).getName().trim().equals("") && list.get(i).getName().equalsIgnoreCase("TYPE"))
						valid = true;
					type= list.get(i).getValue();
					
				}
				if (valid == false) {
					//voucherPromoDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusCode());
					//voucherPromoDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
					//logger.info(Cache.getServiceStatusMap().get("CHN_REQ_" + langId).getStatusDesc());
					return voucherPromoDTO;
				}
				
			}*/
				
				adapter = new LMSWebServiceAdapter();
				GenericDTO genericDTO=new GenericDTO();
				//if(type.equalsIgnoreCase("PROMO") && valid){
				
				/*//sajith k s modified **** start
				RequestProcessDTO processDTO=new RequestProcessDTO();
				processDTO.setFeatureName("VoucherPromo");
				processDTO.setObject(merchantRedemption);
			
				// Adding to Thread pool
				ThreadInitiator.requestProcessThreadPoolForMerchantRedemption.addTask(processDTO);*/
				//sajith k s modified **** end
				
				genericDTO = (GenericDTO) adapter.callFeature("VoucherPromo", merchantRedemption); //sajith k s modified
				
				
				//}
				
				responseDTO = (ResponseDTO) genericDTO.getObj();
				logger.info("BEFORE RETIURING RESPONSE : " +responseDTO.getStatusCode()+"DESC"+responseDTO.getStatusDescription());

				responseDTO.setStatusCode(responseDTO.getStatusCode());
				responseDTO.setStatusDescription(responseDTO.getStatusDescription());
				

				if (responseDTO.getStatusCode() != null) {

					//responseDTO.setStatusCode(responseDTO.getStatusCode());
					//responseDTO.setStatusDescription(responseDTO.getStatusDescription());

				} else {
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("VOUCHER_PROMO_FAIL_" + langId).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("VOUCHER_PROMO_FAIL_" + langId).getStatusDesc());
				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Service : getMerchantRedemption - Transaction ID :"+txnId,e);
			e.printStackTrace();
			
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID FOR MERCHANT REDEMPTION:" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

		}
		
		return responseDTO;
		
		
	}//getMerchantPromo
	
	public MerchantInfoDTO getMerchantPackages(MerchantDetailsDTO merchantDTO)
	{
		DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
		
		LanguageDAO languageDAO=null;
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO=null;
		String langId=null;
		MerchantInfoDTO merchantInfoDTO = null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
		
			languageDAO=new LanguageDAO();
			txnId=merchantDTO.getTransactionId();
			logger.info("Service : GetMerchantPackages - Transaction ID : "+txnId+" subscriber number : "+merchantDTO.getSubscriberNumber()+" Request Recieved in System");
			//logger.info("transaction id "+packageDTO.getTranscationId());
			logger.debug("channel "+merchantDTO.getChannel());
			
			if(merchantDTO.getMerchantId()!=null && !merchantDTO.getMerchantId().equalsIgnoreCase("0"))
			{
			if(merchantDTO.getSubscriberNumber() == null || merchantDTO.getSubscriberNumber().equalsIgnoreCase(""))
			{
				merchantInfoDTO = new MerchantInfoDTO();
				merchantInfoDTO.setTimestamp(merchantDTO.getTimestamp());
				merchantInfoDTO.setTranscationId(merchantDTO.getTransactionId());
				merchantInfoDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusCode());
				merchantInfoDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				return merchantInfoDTO;
			}
			}
			langId=Cache.defaultLanguageID;
			if (merchantDTO.getLanguageID() == null || merchantDTO.getLanguageID().trim().equals("")) {
				langId = languageDAO.getLanguageID(merchantDTO.getSubscriberNumber());
				merchantDTO.setLanguageID(langId);
			} else {
				langId = merchantDTO.getLanguageID();
			}
	
			if(merchantDTO.getChannel() == null || merchantDTO.getChannel().equalsIgnoreCase(""))
			{
				merchantInfoDTO = new MerchantInfoDTO();
				merchantInfoDTO.setTimestamp(merchantDTO.getTimestamp());
				merchantInfoDTO.setTranscationId(merchantDTO.getTransactionId());
				merchantInfoDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+langId).getStatusCode());
				merchantInfoDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+langId).getStatusDesc());
				return merchantInfoDTO;
			}
			
			adapter = new LMSWebServiceAdapter();
			//Calling feature 
			genericDTO = adapter.callFeature("GetMerchants", merchantDTO);
			merchantInfoDTO = (MerchantInfoDTO)genericDTO.getObj();
		/*	PackageDetailsDTO[] packs=merchantInfoDTO.getPackages();
			for(int i=0;i<packs.length;i++)
			{
				logger.info("CATEGORY NAME:"+packs[i].getCategory());
				logger.info("TYPE ID:"+packs[i].getTypeId());
				logger.info("TYPE NAME:"+packs[i].getTypeName());
				logger.info("SUB PACKAGES:"+packs[i].getSubPackageDetails());
				PackageDetailsDTO[] subPacks=packs[i].getSubPackageDetails();
				logger.info(subPacks.length);
				for(int j=0;j<subPacks.length;j++)
				{
				logger.info("SUB CATEGORY NAME:"+subPacks[j].getCategory());
				logger.info("SUB PACKAGE NAME:"+subPacks[j].getPackageName());
				}
			}*/
			merchantInfoDTO.setTranscationId(merchantDTO.getTransactionId());
			merchantInfoDTO.setTimestamp(df.format(new Date()));
			merchantInfoDTO.setStatusCode(genericDTO.getStatusCode());
			merchantInfoDTO.setStatusDescription(genericDTO.getStatus());
		
		}catch (Exception e) {
			logger.error("Service : GetMerchantPackage - Transaction ID :"+txnId,e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" Subscriber Number :"+merchantDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			languageDAO=null;
			adapter=null;
			genericDTO=null;
			merchantDTO=null;
		}
		
		return merchantInfoDTO;
	
	}

}//
