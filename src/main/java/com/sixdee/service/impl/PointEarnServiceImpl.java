package com.sixdee.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.RewardPointsCalculationBL;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyResponseDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.RequestPoint;
import com.sixdee.imp.dto.ResponsesDTO;
import com.sixdee.imp.dto.RewardPointsCalculationDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.serviceDTO.req.RedeemDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.service.PointEarnService;
import com.sixdee.ussd.util.AppCache;

public class PointEarnServiceImpl implements PointEarnService {
	private final static Logger logger = Logger.getLogger(PointEarnServiceImpl.class);
	boolean check = false;

	public LoyaltyResponseDTO adjustPoints(String subscriberNumber, Map<String, String> headers,
			RequestPoint pointrequest,HttpServletResponse servletResponse) {
		LoyaltyResponseDTO response = null;
		SubscriberNumberTabDTO subscriberNumberDetailsDTO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		CDRInformationDTO cdrInformationDTO = null;
		boolean adjusted = false;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		int loyaltyTransactionStatus = 0;
		Long t1 = System.currentTimeMillis();
		String txnId = null;
		String channel = null;
		String LungId = null;
		Double requestedPoints = null;
		TableDetailsDAO tableDetailsDAO = null;
		String desc = null;
		double addPoints = 0.0;
		double subPoints = 0.0;
		CommonUtil commonUtil = null;
		String phoneNumber = null;
		RewardPointsCalculationBL rewardPointsCalculationBL = null;
		RewardPointsCalculationDTO redeemDTO = null;
		GenericDTO genericDTO = null;
		ResponseDTO responseDTO = null;
		String statusPoint = null;
		String isSubscriber = null;
		boolean flag = false;
		try {
			commonUtil = new CommonUtil();
			response = new LoyaltyResponseDTO();
			redeemDTO = new RewardPointsCalculationDTO();
			genericDTO = new GenericDTO();
			rewardPointsCalculationBL = new RewardPointsCalculationBL();

			if (subscriberNumber != null && headers != null) {
				for (String key : headers.keySet()) {
					if (key.equalsIgnoreCase("X_CORRELATION_ID"))
						txnId = headers.get(key);
					if (key.equalsIgnoreCase("CHANNEL"))
						channel = headers.get(key);
					if (key.equalsIgnoreCase("X_LANGUAGE"))
						LungId = headers.get(key);
				}
				statusPoint = Cache.cacheMap.get("STATUS_POINT");
				isSubscriber = Cache.cacheMap.get("IS_SUBSCRIBER");

				if (statusPoint.equalsIgnoreCase("TRUE")) {
					redeemDTO.setStatusPoints(true);
				} else {
					redeemDTO.setStatusPoints(false);
				}

				if (isSubscriber.equalsIgnoreCase("TRUE")) {
					redeemDTO.setSubscriber(true);
				} else {
					redeemDTO.setSubscriber(true);
				}
				logger.info(" RequestId:" + txnId + "before Action RequestPoints " + pointrequest.getPoints());
				if (pointrequest.getAction().equalsIgnoreCase("add")) {
					redeemDTO.setRequestRewardPoints(Double.parseDouble(pointrequest.getPoints()));
				} else if (pointrequest.getAction().equalsIgnoreCase("subtract")) {
					redeemDTO.setRequestRewardPoints(-Double.parseDouble(pointrequest.getPoints()));
				} else {
					response = new LoyaltyResponseDTO();
					response.setRespCode("400");
					response.setRespDesc("Action not valid ");
					servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
					logger.info(" RequestId:" + txnId + " Action not valid " + pointrequest.getAction());
				}

				logger.info(
						" RequestId:" + txnId + "Afeter Action RequestPoints " + redeemDTO.getRequestRewardPoints());

				redeemDTO.setRewardPointsCategory(Integer.parseInt(Cache.cacheMap.get("REWARD_POINT_CATEGORY")));
				redeemDTO.setTransactionId(txnId);
				if (subscriberNumber != null) {
					redeemDTO.setSubscriberNumber((commonUtil.discardCountryCodeIfExists(subscriberNumber)));
				}

				redeemDTO.setChannel(channel);
				redeemDTO.setPointsCalculation(true);
				genericDTO.setObj(redeemDTO);
				genericDTO = rewardPointsCalculationBL.buildProcess(genericDTO);

				if (genericDTO.getStatusCode() != null && genericDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
					response = new LoyaltyResponseDTO();
					response.setRespCode("SC0000");
					response.setRespDesc("Points Adjusted Successfully");
					servletResponse.setStatus(HttpStatus.SC_OK);
				} else {
					response.setRespCode("SC0001");
					response.setRespDesc("fail");
					servletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				}

			} else {
				response = new LoyaltyResponseDTO();
				response.setRespCode("SC0241");
				response.setRespDesc("Mandatory Parameters Not Found");
				servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
			}
			long t3 = System.currentTimeMillis();
			response.setRequestId(txnId);
			response.setTimestamp(t3);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception", e);
			/*
			 * if (responseDTO == null) responseDTO = new ResponseDTO();
			 * responseDTO.setStatusCode("SC0000");
			 * responseDTO.setStatusDescription("Redeemed successfully");
			 */
		} finally {

			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + redeemDTO.getSubscriberNumber()
					+ " Request Leaving System , Processing Time " + (t2 - t1));

			genericDTO = null;

			redeemDTO = null;

		}

		return response;

	}

	/*
	 * public LoyaltyResponseDTO adjustPoint(String subscriberNumber, Map<String,
	 * String> headers, RequestPoint pointrequest) { LoyaltyResponseDTO response =
	 * null; SubscriberNumberTabDTO subscriberNumberDetailsDTO = null;
	 * LoyaltyProfileTabDTO loyaltyProfileTabDTO = null; CDRInformationDTO
	 * cdrInformationDTO = null; boolean adjusted = false; LoyaltyTransactionTabDTO
	 * loyaltyTransactionTabDTO = null; int loyaltyTransactionStatus = 0; Long t1 =
	 * System.currentTimeMillis(); String txnId = null; String channel = null;
	 * String LungId = null; Double requestedPoints = null; TableDetailsDAO
	 * tableDetailsDAO = null; String desc = null; double addPoints = 0.0; double
	 * subPoints = 0.0; CommonUtil commonUtil = null; String phoneNumber = null; try
	 * { commonUtil = new CommonUtil(); tableDetailsDAO = new TableDetailsDAO();
	 * response = new LoyaltyResponseDTO();
	 * 
	 * if (subscriberNumber != null && headers != null) { for (String key :
	 * headers.keySet()) { if (key.equalsIgnoreCase("X_CORRELATION_ID")) txnId =
	 * headers.get(key); if (key.equalsIgnoreCase("CHANNEL")) channel =
	 * headers.get(key); if (key.equalsIgnoreCase("X_LANGUAGE")) LungId =
	 * headers.get(key); }
	 * 
	 * response = new LoyaltyResponseDTO(); cdrInformationDTO = new
	 * CDRInformationDTO(); requestedPoints =
	 * Double.parseDouble(pointrequest.getPoints());
	 * cdrInformationDTO.setTransactionId(txnId);
	 * 
	 * cdrInformationDTO.setChannelID(channel); cdrInformationDTO.setCommandId(13);
	 * if (subscriberNumber != null) { phoneNumber =
	 * commonUtil.discardCountryCodeIfExists(subscriberNumber);
	 * cdrInformationDTO.setSubscriberNumber(phoneNumber); }
	 * 
	 * if ((phoneNumber != null) && !(phoneNumber.equalsIgnoreCase(""))) {
	 * subscriberNumberDetailsDTO =
	 * tableDetailsDAO.getSubscriberNumberDetails(Long.valueOf(phoneNumber)); if
	 * (subscriberNumberDetailsDTO != null) { loyaltyProfileTabDTO = tableDetailsDAO
	 * .getLoyaltyProfileDetails(subscriberNumberDetailsDTO.getLoyaltyID());
	 * 
	 * if (loyaltyProfileTabDTO != null) {
	 * cdrInformationDTO.setTierPoints(loyaltyProfileTabDTO.getRewardPoints());
	 * 
	 * if ((pointrequest != null) && (pointrequest.getAction()) != null) { if
	 * ((Integer.valueOf(pointrequest.getPoints()) > 0)) { if
	 * (pointrequest.getAction().equalsIgnoreCase("add")) { addPoints =
	 * loyaltyProfileTabDTO.getRewardPoints() + requestedPoints;
	 * logger.info(" RequestId:" + txnId + " Action Addition:" +
	 * pointrequest.getAction() + "addpoint:" + addPoints); adjusted =
	 * tableDetailsDAO.adjustPoints(txnId, loyaltyProfileTabDTO.getLoyaltyID(),
	 * true, addPoints); if (adjusted) { loyaltyTransactionStatus = 2;
	 * cdrInformationDTO.setStatusDescription("Points Added"); }
	 * 
	 * } else if (pointrequest.getAction().equalsIgnoreCase("subtract")) {
	 * 
	 * if (requestedPoints <= loyaltyProfileTabDTO.getRewardPoints()) { subPoints =
	 * loyaltyProfileTabDTO.getRewardPoints() - requestedPoints;
	 * logger.info(" RequestId:" + txnId + " Action Subtraction:" +
	 * pointrequest.getAction() + "after subtracted point" + subPoints);
	 * 
	 * adjusted = tableDetailsDAO.adjustPoints(txnId,
	 * loyaltyProfileTabDTO.getLoyaltyID(), false, subPoints);
	 * 
	 * if (adjusted) { loyaltyTransactionStatus = 3;
	 * cdrInformationDTO.setStatusDescription("Points Subtracted"); } } else {
	 * response = new LoyaltyResponseDTO(); response.setRespCode("500");
	 * response.setRespDesc("Enough points not there"); }
	 * 
	 * } else { response = new LoyaltyResponseDTO(); response.setRespCode("400");
	 * response.setRespDesc("Action not valid "); logger.info(" RequestId:" + txnId
	 * + " Action not valid " + pointrequest.getAction());
	 * 
	 * } if (adjusted) { loyaltyProfileTabDTO = tableDetailsDAO
	 * .getLoyaltyProfileDetails(subscriberNumberDetailsDTO.getLoyaltyID());
	 * cdrInformationDTO.setTierPoints(loyaltyProfileTabDTO.getRewardPoints());
	 * response.setRequestId(txnId); response.setTimestamp(t1 -
	 * System.currentTimeMillis()); response.setRespCode("SC0000");
	 * response.setRespDesc("Points Adjusted Successfully");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0000");
	 * cdrInformationDTO.setStatusDescription("Points Adjusted Successfully"); }
	 * else { if (pointrequest.getAction() != null &&
	 * pointrequest.getAction().equalsIgnoreCase("subtract") && requestedPoints >
	 * loyaltyProfileTabDTO.getRewardPoints()) { logger.info(" Existing points:" +
	 * loyaltyProfileTabDTO.getRewardPoints() + " Points request for subtraction:" +
	 * pointrequest.getPoints() + " more can cann't be subtracted");
	 * response.setRequestId(txnId); response.setTimestamp(t1 -
	 * System.currentTimeMillis()); response.setRespCode("SC0249");
	 * response.setRespDesc("You cannot subtract more than existing points");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0249");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc()); } else {
	 * response.setRequestId(txnId); response.setTimestamp(t1 -
	 * System.currentTimeMillis()); response.setRespCode("SC0248");
	 * response.setRespDesc("Points Adjustment Failed");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0248");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc()); }
	 * 
	 * } } else {
	 * 
	 * response.setRespCode("SC0247");
	 * response.setRespDesc("Please enter valid points");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0247");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc()); } } else {
	 * response.setRespCode("SC0246"); response.setRespDesc("Not A Valid Action ");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0001");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc()); }
	 * 
	 * } else { response = new LoyaltyResponseDTO(); response.setRespCode("SC0243");
	 * response.setRespDesc("Not A Valid Loyalty Account ");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0243");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc());
	 * 
	 * } } else { response = new LoyaltyResponseDTO();
	 * response.setRespCode("SC0247");
	 * response.setRespDesc("Subscriber Not found -FAILURE"); //
	 * response.setMessage("Msisdn Does Not Exist!!"); //
	 * response.setStatus("Msisdn Does Not Exist!!");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0242");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc());
	 * 
	 * }
	 * 
	 * } else { response = new LoyaltyResponseDTO(); response.setRespCode("SC0241");
	 * response.setRespDesc("Mandatory Parameters Not Found");
	 * 
	 * cdrInformationDTO.setStatusCode("SC0241");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc());
	 * 
	 * }
	 * 
	 * } else { response = new LoyaltyResponseDTO(); response.setRespCode("SC0241");
	 * response.setRespDesc("Mandatory Parameters Not Found");
	 * 
	 * } response.setRequestId(txnId);
	 * 
	 * } catch (Exception e) { logger.error("Exception:", e); e.printStackTrace();
	 * 
	 * response.setRespCode("SC0245"); response.setRespDesc("FAILURE"); //
	 * response.setMessage("FAILURE"); // response.setStatus("FAILURE");
	 * cdrInformationDTO.setStatusCode("SC0245");
	 * cdrInformationDTO.setStatusDescription(response.getRespDesc()); } finally {
	 * if (adjusted) { loyaltyTransactionTabDTO =
	 * tableDetailsDAO.setTransactionParameters(loyaltyProfileTabDTO.getLoyaltyID(),
	 * null, phoneNumber, loyaltyProfileTabDTO.getRewardPoints(), 0.0,
	 * loyaltyTransactionStatus, channel, null, requestedPoints, 0.0,
	 * loyaltyProfileTabDTO.getTierId(), txnId, response.getRespDesc());
	 * tableDetailsDAO.insertLoyaltyTransaction(loyaltyTransactionTabDTO); }
	 * CDRLoggerUtil.flushFatalCDR(cdrInformationDTO); } return response;
	 * 
	 * }
	 */

}
