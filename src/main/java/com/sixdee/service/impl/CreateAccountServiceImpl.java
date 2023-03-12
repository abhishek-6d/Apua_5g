package com.sixdee.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.CreateAccountBL;
import com.sixdee.imp.bo.DeleteAccountBL;
import com.sixdee.imp.bo.UserprofileBL;
import com.sixdee.imp.bo.ViewAccountBL;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.CreateAccountDTO;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.dto.ExpiringPoints;
import com.sixdee.imp.dto.LoyaltyBalance;
import com.sixdee.imp.dto.ProfileInfo;
import com.sixdee.imp.dto.ResponsesDTO;
import com.sixdee.imp.dto.UserResponseDTO;
import com.sixdee.imp.dto.UserprofileDTO;
import com.sixdee.imp.dto.ViewAccountDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.resp.AccountLineDTO;
import com.sixdee.imp.service.serviceDTO.resp.AccountStatusDTO;
import com.sixdee.imp.service.serviceDTO.resp.UserDTO;
import com.sixdee.service.CreateAccountService;

public class CreateAccountServiceImpl implements CreateAccountService {

	boolean check = false;
	private Logger logger = LogManager.getLogger(CreateAccountServiceImpl.class);

	public ResponsesDTO createAccount(String phoneNumber, Map<String, String> headers,HttpServletResponse servletResponse) {
		ResponsesDTO responseDTO = new ResponsesDTO();
		CommonUtil commonUtil = null;
		LanguageDAO languageDAO = null;
		String lanID = "1";
		GenericDTO genericDTO = null;
		SubscriberListCheckDAO subslistDAO = null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		CreateAccountDTO request = null;
		String requestId = null;
		CreateAccountBL createAccountBL = null;
		String regNumber = "";
		
		try {

			genericDTO = new GenericDTO();

			commonUtil = new CommonUtil();
			languageDAO = new LanguageDAO();
			subslistDAO = new SubscriberListCheckDAO();
			request = new CreateAccountDTO();

			if (phoneNumber != null && headers != null) {
				for (String key : headers.keySet()) {
					if (key.equalsIgnoreCase("X_CORRELATION_ID"))
						txnId = headers.get(key);
					if (key.equalsIgnoreCase("CHANNEL"))
						request.setChannel(headers.get(key));
					if (key.equalsIgnoreCase("X_LANGUAGE"))
						request.setLanguageID(headers.get(key));
				}
				request.setTransactionId(txnId);
				
				logger.info("requestId:{} channel:{} MobNumber:{} defaultlanguage:{}" , request.getTransactionId(), request.getChannel(),request.getMoNumber(),Cache.cacheMap.get("DEFAULT_LANGUAGE_ID"));

				String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
				Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
				if(phoneNumber!=null&&(phoneNumber.length()==subscriberSize)) {
					request.setMoNumber(Long.valueOf(subscriberCountryCode+phoneNumber));
				}else {
					request.setMoNumber(Long.valueOf(phoneNumber));
				}
					
				
					
				
				
				responseDTO.setTranscationId(request.getTransactionId());

				if (request.getMoNumber() != null && !request.getMoNumber().equals("")) {
					if (request.getLanguageID() == null || request.getLanguageID().trim().equals(""))

						request.setLanguageID(Cache.cacheMap.get("DEFAULT_LANGUAGE_ID"));
				}

				createAccountBL = new CreateAccountBL();
				genericDTO.setObj(request);
				genericDTO = createAccountBL.buildProcess(genericDTO);

				request=(CreateAccountDTO)genericDTO.getObj();
				

				if (request.getStatusCode().equalsIgnoreCase("SC0000")) {
					responseDTO.setCode(Cache.getServiceStatusMap().get("LOYALTY_SUCCESS_" + request.getLanguageID())
							.getStatusCode());
					responseDTO.setReason(Cache.getServiceStatusMap().get("LOYALTY_SUCCESS_" + request.getLanguageID())
							.getStatusDesc());
					servletResponse.setStatus(HttpStatus.SC_OK);
					
				}else {
					responseDTO.setCode(
							Cache.getServiceStatusMap().get("LOYALTY_FAIL_" + request.getLanguageID()).getStatusCode());
					responseDTO.setReason(
							Cache.getServiceStatusMap().get("LOYALTY_FAIL_" + request.getLanguageID()).getStatusDesc());
					logger.info(request.getTransactionId() + ": "
							+ Cache.getServiceStatusMap().get("LOYALTY_FAIL_" + request.getLanguageID()).getStatusDesc());
					servletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				}

			} else {
				responseDTO = new ResponsesDTO();
				responseDTO.setCode("500");
				responseDTO.setReason("Missing Mandatory Parameters");
				servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
			}
			responseDTO.setTranscationId(txnId);
			//responseDTO.setTimestamp(System.currentTimeMillis());

		} catch (Exception e) {
			logger.info(request.getTransactionId() + ": ", e);
			responseDTO.setCode(Cache.getServiceStatusMap().get("LOYALTY_FAIL_" + lanID).getStatusCode());
			responseDTO.setReason(Cache.getServiceStatusMap().get("LOYALTY_FAIL_" + lanID).getStatusDesc());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + request.getMoNumber()
					+ " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;

			lanID = null;
			request = null;
			genericDTO = null;
			subslistDAO = null;
		}

		return responseDTO;
	}

	public ResponsesDTO deleteLoyaltyAccount(String phoneNumber, Map<String, String> headers,HttpServletResponse servletResponse) {
		CommonUtil commonUtil = null;
		String lanId = Cache.defaultLanguageID;
		ResponsesDTO responseDTO = null;
		GenericDTO genericDTO = null;
		long t1 = System.currentTimeMillis();
		String txnId = null;
		String lanID = "1";
		DeleteAccountDTO accountDTO = null;
		DeleteAccountBL deleteAccountBL = null;
		String status=null;
		try {
			genericDTO = new GenericDTO();
			accountDTO = new DeleteAccountDTO();
			responseDTO = new ResponsesDTO();
			commonUtil = new CommonUtil();
			if (phoneNumber != null && headers != null) {
				for (String key : headers.keySet()) {
					if (key.equalsIgnoreCase("X_CORRELATION_ID"))
						txnId = (headers.get(key));
					if (key.equalsIgnoreCase("X_LANGUAGE"))
						accountDTO.setLangId(headers.get(key));
					if (key.equalsIgnoreCase("CHANNEL"))
						accountDTO.setChannel(headers.get(key));
				}
				accountDTO.setTransactionId(txnId);
				status=Cache.cacheMap.get("DELETE_ACCOUNT_STATUS");
				accountDTO.setStatus(status);
				if(phoneNumber!=null){
					accountDTO.setMoNumber(Long.valueOf(commonUtil.discardCountryCodeIfExists(phoneNumber)));
				}
				
				accountDTO.setDelete(true);
				if (accountDTO != null && accountDTO.getLangId() == null
						&& accountDTO.getLangId().equalsIgnoreCase("")) {

					accountDTO.setLangId(Cache.cacheMap.get("DEFAULT_LANGUAGE_ID"));
				}

				if (accountDTO != null && accountDTO.getTransactionId() != null
						&& !accountDTO.getTransactionId().equalsIgnoreCase("")) {
					if (accountDTO.getChannel() != null && !accountDTO.getChannel().equalsIgnoreCase("")) {
						if (accountDTO.getMoNumber() != null) {
							logger.info("TransactionId " + txnId + " MO Number " + accountDTO.getMoNumber()+ " Channel : " + accountDTO.getChannel() + " Request Recieved in System ");

							deleteAccountBL = new DeleteAccountBL();
							genericDTO.setObj(accountDTO);
							genericDTO = deleteAccountBL.buildProcess(genericDTO);

							if (genericDTO == null) {
								responseDTO.setCode(
										Cache.getServiceStatusMap().get("DELETE_FAIL_" + lanID).getStatusCode());
								responseDTO.setReason(
										Cache.getServiceStatusMap().get("DELETE_FAIL_" + lanID).getStatusDesc());

								return responseDTO;
							}

							if (genericDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
								responseDTO.setCode(
										Cache.getServiceStatusMap().get("DELETE_SUCCESS_" + lanID).getStatusCode());
								responseDTO.setReason(
										Cache.getServiceStatusMap().get("DELETE_SUCCESS_" + lanID).getStatusDesc());
								responseDTO.setStatus("Registered subscriber status-"+status);
								responseDTO.setMessage("Loyalty account deleted succesfully");
								servletResponse.setStatus(HttpStatus.SC_OK);
							}else {
								responseDTO.setCode(genericDTO.getStatusCode());
								responseDTO.setReason(genericDTO.getStatus());
								responseDTO.setReason("Subscriber Not Registered");
								responseDTO.setMessage("Please Pass Valid Subscriber");
								servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
							}
						
							
						} else {
							responseDTO = new ResponsesDTO();
							responseDTO.setReason("Authenticated value not passing");
							responseDTO.setMessage("Please pass uthenticated value");
							responseDTO.setCode("500");
							responseDTO.setReason("Missing Mandatory Parameters");
							servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
						}

					} else {
						responseDTO = new ResponsesDTO();
						responseDTO.setReason("Authenticated value not passing");
						responseDTO.setMessage("Please pass uthenticated value");
						responseDTO.setCode("500");
						responseDTO.setReason("Missing Mandatory Parameters");
					}

				} else {
					responseDTO = new ResponsesDTO();
					responseDTO.setReason("Authenticated value not passing");
					responseDTO.setMessage("Please pass uthenticated value");
					responseDTO.setCode("500");
					responseDTO.setReason("Missing Mandatory Parameters");
				}
			} else {
				responseDTO = new ResponsesDTO();
				responseDTO.setReason("Authenticated value not passing");
				responseDTO.setMessage("Please pass uthenticated value");
				responseDTO.setCode("500");
				responseDTO.setReason("Missing Mandatory Parameters");
				servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
			}
			logger.info("TransactionId " + txnId + " Delete Account Status Description"
					+ responseDTO.getReason());
		} catch (Exception e) {
			logger.error("TransactionId" + txnId + " Exception e" + e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + accountDTO.getMoNumber()
					+ " Request Leaving System , Processing Time " + (t2 - t1));
			commonUtil = null;

			accountDTO = null;
			genericDTO = null;
		}
		return responseDTO;

	}

	

	

	@Override
	public UserResponseDTO getUserProfile(String subscriberNumber, Map<String, String> headers,HttpServletResponse servletResponse) {
		CommonUtil commonUtil = new CommonUtil();
		LanguageDAO languageDAO = new LanguageDAO();
		SubscriberListCheckDAO subslistDAO = null;
		boolean check = false;
		UserResponseDTO profileDTO = null;
		String langId = null;
		UserprofileBL userprofileBL = null;
		UserprofileDTO dto = null;
		Data[] datas = null;
		Data data = null;
		String txnId = null;
		String channel = null;

		long t1 = System.currentTimeMillis();
		GenericDTO genericDTO;
		UserprofileDTO userProfileDTO = null;
		List<LoyaltyBalance> loyaltyBalanceList = null;
		List<ProfileInfo> profileInfo = null;
		try {

			
			genericDTO = new GenericDTO();
			userProfileDTO = new UserprofileDTO();
			// logger.info("INSIDE SERVICE CLASS");
			subslistDAO = new SubscriberListCheckDAO();

			if (subscriberNumber != null && headers != null) {
				for (String key : headers.keySet()) {
					if (key.equalsIgnoreCase("X_CORRELATION_ID"))
						txnId = (headers.get(key));
					if (key.equalsIgnoreCase("X_LANGUAGE"))
						userProfileDTO.setLanguageId(headers.get(key));
					if (key.equalsIgnoreCase("CHANNEL"))
						channel = (headers.get(key));
				}
				if(subscriberNumber!=null){
					userProfileDTO.setSubscriberNumber(commonUtil.discardCountryCodeIfExists(subscriberNumber));
				}
			
				logger.info("Service : GetUserProfile  -- TRANSACTION ID : " + txnId + " SUBSCRIBER NUMBER: "+ subscriberNumber + " Request Recieved in System");
				
				
				boolean valid = false;

				profileDTO = new UserResponseDTO();

				if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
					profileDTO.setCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
					profileDTO.setReason(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());

				}

				if (userProfileDTO.getLanguageId() == null || userProfileDTO.getLanguageId().trim().equals("")) {
					langId = Cache.cacheMap.get("DEFAULT_LANGUAGE_ID");
					userProfileDTO.setLanguageId(langId);
				} 

				if (userProfileDTO.getData() != null && userProfileDTO.getData().length > 0) {
					List<Data> list = new ArrayList<Data>(Arrays.asList(userProfileDTO.getData()));
					
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i) != null && list.get(i).getName() != null
								&& !list.get(i).getName().trim().equals("")
								&& list.get(i).getName().equalsIgnoreCase("CUST_ID"))
							valid = true;
					}

				}

				

				if (!valid && commonUtil.isItChar(subscriberNumber)) {
					logger.info("ADSL Checking");
					profileDTO.setCode(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusCode());
					profileDTO.setReason(
							Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_" + langId).getStatusDesc());
					return profileDTO;
				}

				if (Cache.getCacheMap().get("IS_WHITELIST_REQD") != null
						&& Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true")) {
					if (userProfileDTO.getChannel().equalsIgnoreCase("SMS")
							|| userProfileDTO.getChannel().equalsIgnoreCase("LMS_WEB")
							|| userProfileDTO.getChannel().equalsIgnoreCase("USSD")) {
						check = subslistDAO.checkSubscriber(subscriberNumber);
						if (check) {
						} else {
							profileDTO.setCode(
									Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusCode());
							profileDTO.setReason(
									Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
							logger.info(Cache.getServiceStatusMap().get("BLACKLIST_" + langId).getStatusDesc());
							return profileDTO;
						}

					}
				}
				userprofileBL = new UserprofileBL();
				genericDTO.setObj(userProfileDTO);
				genericDTO = userprofileBL.buildProcess(genericDTO);

				// UserDTO profileDTO=new UserDTO();

				profileDTO.setId(txnId);

				if (genericDTO == null) {
					logger.info("##Failure###");
					// profileDTO.setStatusCode("SC1000");//profileDTO.setReason("FAILURE");

					profileDTO.setCode(
							Cache.getServiceStatusMap().get("GET_USER_PROFILE_FAIL_" + langId).getStatusCode());
					profileDTO.setReason(
							Cache.getServiceStatusMap().get("GET_USER_PROFILE_FAIL_" + langId).getStatusDesc());
					logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getReason());
					return profileDTO;
				}

				else if (genericDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
					logger.info("##Success###");
					dto = (UserprofileDTO) genericDTO.getObj();
					// profileDTO.setStatusCode("SC0000");//profileDTO.setReason("SUCCESS");
					/*
					 * profileDTO.setStatusCode(Cache.getServiceStatusMap().get(
					 * "GET_USER_PROFILE_SUCCESS_" + langId).getStatusCode());
					 * profileDTO.setStatusDescription(Cache.getServiceStatusMap().get(
					 * "GET_USER_PROFILE_SUCCESS_" + langId).getStatusDesc());
					 */
					logger.info("Category"+dto.getCategory());
					profileDTO.setId(String.valueOf(dto.getLoyaltyID()));
					profileDTO.setLoyaltyTier(String.valueOf(dto.getTierpoints()));

					LoyaltyBalance loyaltyBalance = new LoyaltyBalance();
					loyaltyBalance.setId(String.valueOf(dto.getLoyaltyID()));
					loyaltyBalance.setBalance(String.valueOf(dto.getRewardPoints()));
					loyaltyBalance.setBonusPoints(String.valueOf(dto.getBonuspoints()));
					loyaltyBalance.setStatusPoints(String.valueOf(dto.getStatusPoints()));
					loyaltyBalance.setCategory(dto.getCategory());
					loyaltyBalance.setStatusUpdateDate(
							dto.getStatusUpdatedDate() != null ? dto.getStatusUpdatedDate() + "" : "");
					loyaltyBalance.setTierUpdateDate(
							dto.getTierUpdatedDate() != null ? dto.getTierUpdatedDate() + "" : "");
					
					
					ExpiringPoints expiringPoints = new ExpiringPoints();
					expiringPoints.setDate("2030-09-30");
					expiringPoints.setPoints("0");
					loyaltyBalance.setExpiringPoints(expiringPoints);
					loyaltyBalanceList = new ArrayList<LoyaltyBalance>();
					loyaltyBalanceList.add(loyaltyBalance);
					ProfileInfo profileInfoinformation = new ProfileInfo();
					profileInfoinformation.setFirstName(dto.getFirstName());
					profileInfoinformation.setLastName(dto.getLastName());
					profileInfoinformation.setContactNumber(dto.getContactNumber());
					profileInfoinformation.setAddress(dto.getAddress());
					profileInfo = new ArrayList<ProfileInfo>();
					profileInfo.add(profileInfoinformation);

					profileDTO.setProfileInfo(profileInfo);
					profileDTO.setLoyaltyBalances(loyaltyBalanceList);

					profileDTO.setCode("SC0000");
					profileDTO.setReason("SUCCESS");
					servletResponse.setStatus(HttpStatus.SC_OK);
					logger.info("dto.isDealOfDay()>>>>>>>>" + dto.isDealOfDay());
					logger.info("isActive()>>>>>>>>" + dto.toString());
					if (dto.isDealOfDay()) {
						/*
						 * datas = new Data[2]; data = new Data(); data.setName("IS_DOD_ELIGIBLE");
						 * if(dto.isActive()) data.setValue("YES"); else data.setValue("NO"); datas[0] =
						 * data;
						 * 
						 * data = new Data(); data.setName("DOD_ACCESS_CODE"); if(dto.isActive())
						 * data.setValue(dto.getPromoCode()); datas[1] = data;
						 * profileDTO.setData(datas);
						 */} else {
						/*
						 * logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getStatusDescription());
						 * profileDTO.setFirstName(dto.getFirstName());
						 * profileDTO.setLastName(dto.getLastName());
						 * profileDTO.setLoyaltyID(dto.getLoyaltyID() + ""); //
						 * profileDTO.setAccountNumber(dto.getAccountNumber()!=null?dto.getAccountNumber
						 * ()+"":null); profileDTO.setContactNumber(dto.getContactNumber()); //
						 * profileDTO.setDateOfBirth(dto.getDateOfBirth());
						 * profileDTO.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth()
						 * + "" : null); profileDTO.setAddress(dto.getAddress());
						 * profileDTO.setEmailID(dto.getEmailID());
						 * profileDTO.setVipCode(dto.getVipCode());
						 * profileDTO.setCategory(dto.getCategory());
						 * profileDTO.setOccupation(dto.getOccupation());
						 * profileDTO.setIndustry(dto.getIndustry());
						 * profileDTO.setDefaultLanguage(dto.getDefaultLanguage());
						 * profileDTO.setNationalID(dto.getNationalID());
						 * logger.info(">>>status>>>"+dto.getStatusID()); if(dto.getStatusID()==1)
						 * profileDTO.setStatus("Active"); else{ profileDTO.setStatus("InActive"); } int
						 * i=0; datas = new Data[4]; // Setting StatusId //datas = new Data[20]; if
						 * (dto.getExpiryDate() != null && dto.getExpiryRewardPoint() != null) datas =
						 * new Data[11]; else datas = new Data[8];
						 * 
						 * data = new Data(); data.setName("StatusId"); data.setValue("" +
						 * dto.getStatusID()); datas[i] = data; i++;
						 * 
						 * data = new Data(); data.setName("CREATE_TIME"); data.setValue("" +
						 * dto.getCreateTime()); datas[i] = data; i++;
						 * 
						 * data = new Data(); data.setName("NEXT_TIER");
						 * data.setValue(dto.getNextTierName()); datas[i] = data; i++;
						 * 
						 * data = new Data(); data.setName("POINTS_TO_NEXT_TIER");
						 * data.setValue(dto.getPointsToNextTier()); datas[i] = data; i++;
						 * 
						 * logger.info("CREATE TIME::" + dto.getCreateTime());
						 * 
						 * logger.info("EXPIRY DATE::" + dto.getExpiryDate());
						 * logger.info("EXPIRY REWARD POINTS::" + dto.getExpiryRewardPoint());
						 * 
						 * //if (dto.getExpiryDate() != null && dto.getExpiryRewardPoint() != null) {
						 * logger.info("INSIDE DATA TAG::"); data = new Data();
						 * data.setName("EXPIRY_DATE"); data.setValue("2018-09-30"); datas[i] = data;
						 * i++; //} data = new Data(); data.setName("EXPIRY_POINTS");
						 * data.setValue("0"); datas[i] = data; i++;
						 * 
						 * data = new Data(); data.setName("TIER_POINTS"); data.setValue("" +
						 * dto.getTierpoints()); datas[i] = data; i++;
						 * 
						 * data = new Data(); data.setName("BONUS_POINTS"); data.setValue("" +
						 * dto.getBonuspoints()); datas[i] = data; i++;
						 * 
						 * 
						 * 
						 * 
						 * profileDTO.setData(datas); logger.info("SIZE OF DATA TAG::" +
						 * profileDTO.getData().length);
						 * 
						 * // display Active_InActive //profileDTO.setStatus(dto.getStatusDesc()); //
						 * profileDTO.setStatusUpdateDate(dto.getStatusUpdatedDate().toString()); //
						 * profileDTO.setStatusId(dto.getStatusID());
						 * profileDTO.setStatusUpdateDate(dto.getStatusUpdatedDate() != null ?
						 * dto.getStatusUpdatedDate() + "" : ""); //
						 * profileDTO.setTier(dto.getTierId().toString());
						 * profileDTO.setTier(dto.getTierName() != null ? dto.getTierName() + "" :
						 * null); // profileDTO.setTierUpdateDate(dto.getTierUpdatedDate().toString());
						 * profileDTO.setTierUpdateDate(dto.getTierUpdatedDate() != null ?
						 * dto.getTierUpdatedDate() + "" : ""); //
						 * profileDTO.setStatusPoints(dto.getStatusPoints().toString());
						 * profileDTO.setStatusPoints(dto.getStatusPoints() + ""); //
						 * profileDTO.setRewardPoints(dto.getRewardPoints().toString());
						 * profileDTO.setRewardPoints(dto.getRewardPoints() + "");
						 * profileDTO.setTiername(dto.getTierName());
						 */}
				}

				else if (genericDTO.getStatusCode().equalsIgnoreCase("SC1000") && genericDTO.getStatus() != null) {

					// profileDTO.setStatusCode("SC1000");
					servletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					profileDTO.setCode(
							Cache.getServiceStatusMap().get("GET_USER_PROFILE_FAIL_" + langId).getStatusCode());
					profileDTO.setReason(genericDTO.getStatus());
					logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getReason());

				} else if (genericDTO.getStatusCode().equalsIgnoreCase("SC0001") && genericDTO.getStatus() != null) {

					// profileDTO.setStatusCode("SC1000");
					// profileDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" +
					// langId).getStatusCode());
					servletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					profileDTO.setCode(genericDTO.getStatusCode());
					profileDTO.setReason(genericDTO.getStatus());
					logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getReason());
				} else {
					logger.info("Failure");
					servletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					if (genericDTO.getStatus() == null && genericDTO.getStatusCode() == null) {
						profileDTO.setCode(
								Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + langId).getStatusCode());
						profileDTO.setReason(
								Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + langId).getStatusDesc());
					} else {
						profileDTO.setCode(genericDTO.getStatusCode());
						profileDTO.setReason(genericDTO.getStatus());
					}
					logger.info("DESCRIPTION>>>>>>>>" + profileDTO.getReason());
				}

				logger.info("OUTSIDE SERVICE CLASS");
			}

			else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Service : GetUserProfile - Transaction ID : " + txnId + "Exception= ", e);
		}

		finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber
					+ " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;
			subslistDAO = null;
			langId = null;
			dto = null;
			datas = null;
			data = null;
			genericDTO = null;
		}
		return profileDTO;

	}

}
