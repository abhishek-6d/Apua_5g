package com.sixdee.imp.dao;

/**
 * 
 * @author Paramesh
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
 * <td>April 26,2013 04:24:47 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.common.util.TestNumberUtil;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.AppMsisdnMappingDTO;
import com.sixdee.imp.dto.ApplicationCodesDTO;
import com.sixdee.imp.dto.CategoryDetailsDTO;
import com.sixdee.imp.dto.ConfigureParameterDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.PostPaidInstantDTO;
import com.sixdee.imp.dto.RewardPointsCalculationDTO;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.imp.service.ReServices.DAO.TierAndBonusCalculationDAO;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.util.selections.CDRCommandID;

public class RewardPointsCalculationDAO {

	private static Logger logger = Logger.getLogger(RewardPointsCalculationDAO.class);

	SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("ddMMyyyyHHmmss");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public GenericDTO calculateRewardPointsStep1(GenericDTO genericDTO) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		TableDetailsDAO tableDetailsDAO = null;
		boolean flag = false;
		RewardPointsCalculationDTO calculationDTO = (RewardPointsCalculationDTO) genericDTO.getObj();
		CommonUtil commonUtil = new CommonUtil();
		tableDetailsDAO = new TableDetailsDAO();
		Object object = null;
		String logFile = "";
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		int isTestNumber = 0;
		try {

			calculationDTO.setDefaultLanguage(Cache.defaultLanguageID);
			calculationDTO.setOriginalVolume(calculationDTO.getVolume());

			logFile = "" + calculationDTO.getTransactionId() + "|" + dateFormat.format(new Date()) + "|"
					+ calculationDTO.getSubscriberNumber() + "|" + calculationDTO.getRewardPointsCategory() + "|"
					+ calculationDTO.getVolume() + "|";

			// Find the loyaltyID for requested Subscriber
			if (commonUtil.isItChar(calculationDTO.getSubscriberNumber())) {
				ADSLTabDTO tabDTO = tableDetailsDAO.getADSLDetails(calculationDTO.getSubscriberNumber());
				if (tabDTO != null) {
					System.out.println(tabDTO.getStatusID() + "                            ");
					if (!calculationDTO.isStatusPoints() && tabDTO.getStatusID() != 1)
						throw new CommonException(
								"ADSL Number : " + calculationDTO.getSubscriberNumber() + " is not in active state");

					calculationDTO.setLoyaltyID(tabDTO.getLoyaltyID());
					calculationDTO.setSubscriberType(tabDTO.getAccountTypeId());
					calculationDTO.setADSL(true);
					object = tabDTO;
				}
			} else {
				long start = System.currentTimeMillis();
				SubscriberNumberTabDTO subscriberNumberTabDTO = tableDetailsDAO
						.getSubscriberNumberDetails(Long.parseLong(calculationDTO.getSubscriberNumber()));
				logger.info(
						" get subscriber: --------------------------------->" + (System.currentTimeMillis() - start));
				if (subscriberNumberTabDTO != null) {
					if (!calculationDTO.isStatusPoints() && subscriberNumberTabDTO.getStatusID() != 1) {
						throw new CommonException(
								Cache.getServiceStatusMap().get("REW_SUB_INACTIVE_"
										+ calculationDTO.getDefaultLanguage()).getStatusCode(),
								commonUtil.getStatusDescription(Cache.getServiceStatusMap()
										.get("REW_SUB_INACTIVE_" + calculationDTO.getDefaultLanguage()).getStatusDesc(),
										new String[] { NotificationTokens.subscriberNumber },
										new String[] { calculationDTO.getSubscriberNumber() }));
					}
					calculationDTO.setLoyaltyID(subscriberNumberTabDTO.getLoyaltyID());
					calculationDTO.setSubscriberType(subscriberNumberTabDTO.getAccountTypeId());
					calculationDTO.setSubscriber(true);
					object = subscriberNumberTabDTO;
				}

				if (calculationDTO.getLoyaltyID() == null) {
					AccountNumberTabDTO accountNumberTabDTO = tableDetailsDAO
							.getAccountNumberDetails(calculationDTO.getSubscriberNumber() + "");
					if (accountNumberTabDTO != null) {

						if (!calculationDTO.isStatusPoints() && accountNumberTabDTO.getStatusID() != 1)
							throw new CommonException(
									Cache.getServiceStatusMap()
											.get("REW_ACCNO_INACTIVE_"
													+ calculationDTO.getDefaultLanguage())
											.getStatusCode(),
									commonUtil
											.getStatusDescription(
													Cache.getServiceStatusMap()
															.get("REW_ACCNO_INACTIVE_"
																	+ calculationDTO.getDefaultLanguage())
															.getStatusDesc(),
													new String[] { NotificationTokens.accountNumber },
													new String[] { calculationDTO.getSubscriberNumber() }));

						calculationDTO.setLoyaltyID(accountNumberTabDTO.getLoyaltyID());

						List<LoyaltyRegisteredNumberTabDTO> list = (List<LoyaltyRegisteredNumberTabDTO>) tableDetailsDAO
								.getLoyaltyRegisteredNumberDetails(accountNumberTabDTO.getLoyaltyID(),
										accountNumberTabDTO.getAccountNumber());

						calculationDTO.setSubscriberType(list.get(0).getAccountTypeId());
						calculationDTO.setAccount(true);
						object = accountNumberTabDTO;

						list.clear();
						list = null;

					}
				}

				if (calculationDTO.getLoyaltyID() == null) {
					loyaltyProfileTabDTO = tableDetailsDAO
							.getLoyaltyProfile(Long.parseLong(calculationDTO.getSubscriberNumber()));
					if (loyaltyProfileTabDTO != null) {

						if (!calculationDTO.isStatusPoints() && loyaltyProfileTabDTO.getStatusID() != 1)
							throw new CommonException(
									Cache.getServiceStatusMap()
											.get("REW_LOYALTYID_INACTIVE_" + calculationDTO.getDefaultLanguage())
											.getStatusCode(),
									commonUtil.getStatusDescription(
											Cache.getServiceStatusMap()
													.get("REW_LOYALTYID_INACTIVE_"
															+ calculationDTO.getDefaultLanguage())
													.getStatusDesc(),
											new String[] { NotificationTokens.loyaltyID },
											new String[] { calculationDTO.getSubscriberNumber() }));

						calculationDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
					}
				}
			}

			if (calculationDTO.getLoyaltyID() == null) {

				logger.info(calculationDTO.getTransactionId() + ": Loyalty ID not exist for Subsriber Number :"
						+ calculationDTO.getSubscriberNumber());
				throw new CommonException(
						Cache.getServiceStatusMap().get("REW_SUB_INVALID_" + calculationDTO.getDefaultLanguage())
								.getStatusCode(),
						commonUtil.getStatusDescription(
								Cache.getServiceStatusMap()
										.get("REW_SUB_INVALID_" + calculationDTO.getDefaultLanguage()).getStatusDesc(),
								new String[] { NotificationTokens.subscriberNumber },
								new String[] { calculationDTO.getSubscriberNumber() }));

			}

			isTestNumber = TestNumberUtil.isTestNumber(calculationDTO.getSubscriberNumber());
			calculationDTO.setTestNumber(isTestNumber); // Test Number Flag
			calculateRewardPointsStep2(genericDTO, object);

			genericDTO = settingStatus("REW_SUCCESS", calculationDTO, genericDTO);

			logFile += calculationDTO.getLoyaltyID() + "|" + calculationDTO.getCalculatedRewardPoints() + "|"
					+ calculationDTO.getCalculatedStatusPoints() + "|" + calculationDTO.getProfileTierID() + "|"
					+ calculationDTO.getNextTierID() + "|"
					+ Cache.getServiceStatusMap().get("REW_SUCCESS_" + calculationDTO.getDefaultLanguage())
							.getStatusCode()
					+ "|'"
					+ Cache.getServiceStatusMap().get("REW_SUCCESS_" + calculationDTO.getDefaultLanguage())
							.getStatusDesc()
					+ "'|||" + (calculationDTO.getSubscriberType() == null ? "" : calculationDTO.getSubscriberType());

		} catch (Exception e) {
			e.printStackTrace();

			if (e instanceof CommonException) {
				CommonException exception = (CommonException) e;
				genericDTO.setStatusCode(exception.getStatusCode());
				genericDTO.setStatus(exception.getLocalizedMessage());
				calculationDTO.setStatusCode(exception.getStatusCode());
				logFile += calculationDTO.getLoyaltyID() + "|" + calculationDTO.getCalculatedRewardPoints() + "|"
						+ calculationDTO.getCalculatedStatusPoints() + "|" + calculationDTO.getProfileTierID() + "|"
						+ calculationDTO.getNextTierID() + "|" + exception.getStatusCode() + "|'"
						+ e.getLocalizedMessage() + "'|||"
						+ (calculationDTO.getSubscriberType() == null ? "" : calculationDTO.getSubscriberType());
			} else {
				/*
				 * genericDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_FAIL_" +
				 * calculationDTO.getDefaultLanguage()).getStatusCode());
				 * genericDTO.setStatus(Cache.getServiceStatusMap().get("REW_FAIL_" +
				 * calculationDTO.getDefaultLanguage()).getStatusDesc());
				 */
				try {
					genericDTO = settingStatus("REW_FAIL", calculationDTO, genericDTO);
				} catch (CommonException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logFile += calculationDTO.getLoyaltyID() + "|" + calculationDTO.getCalculatedRewardPoints() + "|"
						+ calculationDTO.getCalculatedStatusPoints() + "|" + calculationDTO.getProfileTierID() + "|"
						+ calculationDTO.getNextTierID() + "|"
						+ Cache.getServiceStatusMap().get("REW_FAIL_" + calculationDTO.getDefaultLanguage())
								.getStatusCode()
						+ "|'" + e.getLocalizedMessage() + "'|||"
						+ (calculationDTO.getSubscriberType() == null ? "" : calculationDTO.getSubscriberType());
			}
		} finally {

			logFile += "|" + calculationDTO.getChannel() + "|" + isTestNumber + "||||";
			buildingCdrDtoAndWritingCdr(calculationDTO, genericDTO);
			// logger.fatal(logFile);
			tableDetailsDAO = null;
			object = null;
			dateFormat = null;
			calculationDTO = null;
			commonUtil = null;
			logFile = null;
			loyaltyProfileTabDTO = null;
		}
		return genericDTO;

	}// calculateRewardPointsStep1

	public void calculateRewardPointsStep2(GenericDTO genericDTO, Object object) throws CommonException {
		TierUpgradationDAO tierUpgradationDAO = null;
		RewardPointsCalculationDTO calculationDTO = (RewardPointsCalculationDTO) genericDTO.getObj();
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		Session session = null;
		Transaction transaction = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		ADSLTabDTO tabDTO = null;
		AccountNumberTabDTO accountNumberTabDTO = null;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		RegisterAcctDAO registerAcctDAO = new RegisterAcctDAO();
		TableInfoDAO infoDAO = new TableInfoDAO();
		CommonUtil commonUtil = new CommonUtil();
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		LanguageDAO languageDAO = null;
		boolean isCodeSent = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<String, String> loyaltyTransactionMap = null;
		LoyalityCommonTransaction loyalityCommonTransaction = null;
		TierAndBonusCalculationDAO tierAndBonusCalculationDAO = null;
		Calendar c = null;
		ConfigureParameterDTO configParmeterDTO = null;
		Date expiryDate = null;
		String exp = null;
		long threshold = Long.parseLong(Cache.getConfigParameterMap().get("REWARD_POINTS_THRESHOLD") != null
				? Cache.getConfigParameterMap().get("REWARD_POINTS_THRESHOLD").getParameterValue()
				: "-1");

		try {
			loyalityCommonTransaction = new LoyalityCommonTransaction();
			loyaltyTransactionMap = new HashMap<String, String>();// added S
			session = HiberanteUtil.getSessionFactory().openSession();

			int loopCounter = 0;

			while (loopCounter < 10) {

				calculationDTO.setVolume(calculationDTO.getOriginalVolume());

				loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
				/*
				 * loyaltyTransactionTabDTO.setReqTransactionID(calculationDTO.getTransactionId(
				 * )); loyaltyTransactionTabDTO.setVolume(calculationDTO.getOriginalVolume());
				 */

				loyaltyTransactionMap.put(LoyalityTransactionConstants.reqTransactionID,
						calculationDTO.getTransactionId());// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.volume,
						String.valueOf(calculationDTO.getOriginalVolume()));// added S

				if (calculationDTO.isSubscriber()) {
					if (loopCounter == 0)
						subscriberNumberTabDTO = (SubscriberNumberTabDTO) object;
					else
						subscriberNumberTabDTO = tableDetailsDAO
								.getSubscriberNumberDetails(Long.parseLong(calculationDTO.getSubscriberNumber()));
					loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber,
							String.valueOf(subscriberNumberTabDTO.getSubscriberNumber()));// added S
					loyaltyTransactionMap.put(LoyalityTransactionConstants.accountNumber,
							String.valueOf(subscriberNumberTabDTO.getAccountNumber()));// added S
					// loyaltyTransactionTabDTO.setSubscriberNumber("" +
					// subscriberNumberTabDTO.getSubscriberNumber());
					logger.info(calculationDTO.getTransactionId() + ": Subscriber Number : "
							+ calculationDTO.getSubscriberNumber() + " Points : " + subscriberNumberTabDTO.getPoints());
				} else if (calculationDTO.isAccount()) {
					if (loopCounter == 0)
						accountNumberTabDTO = (AccountNumberTabDTO) object;
					else
						accountNumberTabDTO = tableDetailsDAO
								.getAccountNumberDetails(calculationDTO.getSubscriberNumber());

					logger.info(calculationDTO.getTransactionId() + ": Account Number : "
							+ calculationDTO.getSubscriberNumber() + " Points : " + accountNumberTabDTO.getPoints());

					// loyaltyTransactionTabDTO.setAccountNumber(accountNumberTabDTO.getAccountNumber());
				} else if (calculationDTO.isADSL()) {
					if (loopCounter == 0)
						tabDTO = (ADSLTabDTO) object;
					else
						tabDTO = tableDetailsDAO.getADSLDetails(calculationDTO.getSubscriberNumber());
					loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber, tabDTO.getADSLNumber());// added
																														// S
					// loyaltyTransactionTabDTO.setSubscriberNumber(tabDTO.getADSLNumber());
					logger.info(calculationDTO.getTransactionId() + ": ADSL Number : "
							+ calculationDTO.getSubscriberNumber() + " Points : " + tabDTO.getPoints());
				} else {
					// loyaltyTransactionTabDTO.setSubscriberNumber("");
					loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber, "");// added S
				}

				long start = System.currentTimeMillis();

				// Fetching loyalty IDS
				List<Long> loyaltyIds = tableDetailsDAO.getReleateIds(calculationDTO.getLoyaltyID());

				calculationDTO.setLoyaltyID(loyaltyIds.get(0));

				logger.info(calculationDTO.getTransactionId() + ": Parent Loyalty ID " + calculationDTO.getLoyaltyID());

				// Update on Parent Loyalty ID
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(calculationDTO.getLoyaltyID());

				// LoyaltyProfileTabDTO
				// loyaltyProfileTabDTO=getLoyaltyProfileDetails1(calculationDTO.getLoyaltyID());
				System.out.println("get loyalty profile  ------------ >" + (System.currentTimeMillis() - start));
				if (loyaltyProfileTabDTO == null) {
					logger.info(calculationDTO.getTransactionId() + ": Loyalty ID exist Loyalty ID : "
							+ calculationDTO.getLoyaltyID() + ", but record not exist in table ");
					throw new CommonException(
							Cache.getServiceStatusMap().get("REW_SUB_INVALID_"
									+ calculationDTO.getDefaultLanguage()).getStatusCode(),
							commonUtil.getStatusDescription(Cache.getServiceStatusMap()
									.get("REW_SUB_INVALID_" + calculationDTO.getDefaultLanguage()).getStatusDesc(),
									new String[] { NotificationTokens.subscriberNumber },
									new String[] { calculationDTO.getSubscriberNumber() }));
				}

				calculationDTO.setDefaultLanguage(Cache.defaultLanguageID);

				if (!calculationDTO.isStatusPoints() && loyaltyProfileTabDTO.getStatusID() != 1) {
					logger.info(
							calculationDTO.getTransactionId() + ": Loyalty ID : " + loyaltyProfileTabDTO.getLoyaltyID()
									+ "  Status : " + Cache.getStatusMap().get(loyaltyProfileTabDTO.getStatusID()));
					// throw new
					// CommonException("Loyalty ID : "+loyaltyProfileTabDTO.getLoyaltyID()+" Status
					// : "+Cache.getStatusMap().get(loyaltyProfileTabDTO.getStatusID()));
					throw new CommonException(Cache.getServiceStatusMap()
							.get("REW_LOYALTYID_INACTIVE_" + calculationDTO.getDefaultLanguage()).getStatusCode(),
							commonUtil
									.getStatusDescription(
											Cache.getServiceStatusMap()
													.get("REW_LOYALTYID_INACTIVE_"
															+ calculationDTO.getDefaultLanguage())
													.getStatusDesc(),
											new String[] { NotificationTokens.loyaltyID },
											new String[] { "" + loyaltyProfileTabDTO.getLoyaltyID() }));

				}

				String curDate = dateFormat.format(new Date());

				if (loyaltyProfileTabDTO.getPointsCreditedDate() == null)
					loyaltyProfileTabDTO.setPointsCreditedDate(new Date());

				String profiledate = dateFormat.format(loyaltyProfileTabDTO.getPointsCreditedDate());

				logger.info(calculationDTO.getTransactionId() + ": Before, LoyaltyID Number : "
						+ loyaltyProfileTabDTO.getLoyaltyID() + " Points : " + loyaltyProfileTabDTO.getRewardPoints()
						+ " Tier ID " + loyaltyProfileTabDTO.getTierId() + " Last Reward Date " + profiledate
						+ " DayWiseRewardPoints " + loyaltyProfileTabDTO.getDayWiseRewardPoints());

				/*
				 * loyaltyTransactionTabDTO.setPreRewardPoints(loyaltyProfileTabDTO.
				 * getRewardPoints());
				 * loyaltyTransactionTabDTO.setPreStatusPoints(loyaltyProfileTabDTO.
				 * getStatusPoints());
				 */

				loyaltyTransactionMap.put(LoyalityTransactionConstants.preRewardPoints,
						String.valueOf(loyaltyProfileTabDTO.getRewardPoints()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.preStatusPoints,
						String.valueOf(loyaltyProfileTabDTO.getStatusPoints()));// added S

				calculationDTO.setProfileTierID(loyaltyProfileTabDTO.getTierId());
				calculationDTO.setNextTierID(loyaltyProfileTabDTO.getTierId());
				calculationDTO.setProfileRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
				calculationDTO.setProfileStatusoints(loyaltyProfileTabDTO.getStatusPoints());
				calculationDTO.setCalculatedRewardPoints(0);
				calculationDTO.setCalculatedStatusPoints(0);
				calculationDTO.setDayWiseRewardPoints(loyaltyProfileTabDTO.getDayWiseRewardPoints());

				double rewardPoints = 0;
				double statusPoints = 0;
				double currentRewardPoints = loyaltyProfileTabDTO.getRewardPoints();
				pointsCalculation(loyaltyProfileTabDTO, calculationDTO);

				calculationDTO.setNextTierID(loyaltyProfileTabDTO.getTierId());

				if (!calculationDTO.isStatusPoints())
					rewardPoints = calculationDTO.getCalculatedRewardPoints();
				else
					calculationDTO.setCalculatedRewardPoints(0);

				statusPoints = calculationDTO.getCalculatedStatusPoints();

				logger.info(calculationDTO.getTransactionId() + ": Calculated Reward Points " + rewardPoints);
				logger.info(calculationDTO.getTransactionId() + ": Calculated Status Points " + statusPoints);

				/*
				 * if (rewardPoints <= 0 && statusPoints <= 0) { break; }
				 */

				if (!curDate.equalsIgnoreCase(profiledate)) {
					calculationDTO.setDayWiseRewardPoints(0.0);
				}

				logger.info(calculationDTO.getTransactionId() + ": After, LoyaltyID Number : "
						+ loyaltyProfileTabDTO.getLoyaltyID() + " Points : " + loyaltyProfileTabDTO.getRewardPoints()
						+ " Tier ID " + loyaltyProfileTabDTO.getTierId() + " Last Reward Date " + profiledate
						+ " DayWiseRewardPoints " + calculationDTO.getDayWiseRewardPoints());

				curDate = null;
				profiledate = null;

				calculationDTO.setDayWiseRewardPoints(calculationDTO.getDayWiseRewardPoints() + rewardPoints);

				if (threshold > -1) {
					if (calculationDTO.getDayWiseRewardPoints() > threshold) {
						logger.info(
								calculationDTO.getTransactionId() + " : Loyalty ID : " + calculationDTO.getLoyaltyID()
										+ " , Total Reward Points " + calculationDTO.getDayWiseRewardPoints()
										+ " Has crosss the configured threshold values,  Threshold : " + threshold);

						insertLoyaltyThresholdDetails(loyaltyProfileTabDTO, calculationDTO, threshold, rewardPoints);

						throw new CommonException("SC2000",
								"Reward Points has cross the configured Threshold value :" + threshold);
					}
				}

				if (!calculationDTO.isStatusPoints())
					loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints,
							String.valueOf(loyaltyProfileTabDTO.getRewardPoints()));// added S
				// loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints());

				loyaltyTransactionMap.put(LoyalityTransactionConstants.curStatusPoints,
						String.valueOf(loyaltyProfileTabDTO.getStatusPoints()));// added S
				// loyaltyTransactionTabDTO.setCurStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
				loyaltyTransactionTabDTO = loyalityCommonTransaction.loyaltyTransactionSetter(loyaltyTransactionTabDTO,
						loyaltyTransactionMap);// added S

				if (loyaltyTransactionTabDTO.getCurRewardPoints() == null
						|| loyaltyTransactionTabDTO.getCurRewardPoints() == 0)
					loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints,
							String.valueOf(loyaltyTransactionTabDTO.getPreRewardPoints()));// added S
				// loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyTransactionTabDTO.getPreRewardPoints());

				if (loyaltyTransactionTabDTO.getCurStatusPoints() == null
						|| loyaltyTransactionTabDTO.getCurStatusPoints() == 0)
					loyaltyTransactionMap.put(LoyalityTransactionConstants.curStatusPoints,
							String.valueOf(loyaltyTransactionTabDTO.getPreStatusPoints()));// added S
				// loyaltyTransactionTabDTO.setCurStatusPoints(loyaltyTransactionTabDTO.getPreStatusPoints());

				/*
				 * loyaltyTransactionTabDTO.setRewardPoints(rewardPoints);
				 * loyaltyTransactionTabDTO.setStatusPoints(statusPoints);
				 * loyaltyTransactionTabDTO.setChannel(calculationDTO.getChannel());
				 * loyaltyTransactionTabDTO.setPreTierId(calculationDTO.getProfileTierID());
				 * loyaltyTransactionTabDTO.setCurTierId(calculationDTO.getNextTierID());
				 * loyaltyTransactionTabDTO.setServerId(Cache.cacheMap.get("SERVER_ID"));
				 */
				loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints,
						String.valueOf(loyaltyProfileTabDTO.getRewardPoints()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.preRewardPoints,
						String.valueOf(currentRewardPoints));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.rewardPoints,
						String.valueOf(calculationDTO.getRequestRewardPoints()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.reqTransactionID,
						String.valueOf(calculationDTO.getTransactionId()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.statusPoints,
						String.valueOf(loyaltyProfileTabDTO.getStatusPoints()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.channel,
						String.valueOf(Cache.channelDescDetails.get(calculationDTO.getChannel())));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.preTierId,
						String.valueOf(calculationDTO.getProfileTierID()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.curTierId,
						String.valueOf(calculationDTO.getNextTierID()));// added S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.serverId, Cache.cacheMap.get("SERVER_ID"));// added
																													// S
				loyaltyTransactionMap.put(LoyalityTransactionConstants.statusDescription, Cache.getConfigParameterMap()
						.get("MANUAL_POINT_ADJUSTMENT_TXN_STATUS_DESC").getParameterValue());

				start = System.currentTimeMillis();
				Query query = null;

				transaction = session.beginTransaction();

				if (!calculationDTO.isStatusPoints()) {

					if (calculationDTO.isSubscriber()) {
						logger.info("test reward points " + rewardPoints);
						String sql = " UPDATE  "
								+ infoDAO.getSubscriberNumberDBTable(calculationDTO.getSubscriberNumber())
								+ " SET POINTS=POINTS+?,COUNTER=?,POINTS_UPDATED_DATE=? "
								+ " WHERE SUBSCRIBER_NUMBER=? AND COUNTER=?";
						query = session.createSQLQuery(sql).setParameter(0, rewardPoints)
								.setParameter(1,
										(subscriberNumberTabDTO.getCounter() >= Integer.MAX_VALUE ? 1
												: subscriberNumberTabDTO.getCounter() + 1))
								.setParameter(2, new Date())
								.setParameter(3, subscriberNumberTabDTO.getSubscriberNumber())
								.setParameter(4, subscriberNumberTabDTO.getCounter());

						System.out.println("get updatioh SQL --------------- >" + (System.currentTimeMillis() - start));

					} else if (calculationDTO.isAccount()) {
						String sql = " UPDATE " + infoDAO.getAccountNumberDBTable(calculationDTO.getSubscriberNumber())
								+ " SET POINTS=POINTS+?,COUNTER=? " + " WHERE ACCOUNT_NO=? AND COUNTER=? ";
						query = session.createSQLQuery(sql).setParameter(0, rewardPoints)
								.setParameter(1,
										(accountNumberTabDTO.getCounter() >= Integer.MAX_VALUE ? 1
												: accountNumberTabDTO.getCounter() + 1))
								.setParameter(2, accountNumberTabDTO.getAccountNumber())
								.setParameter(3, accountNumberTabDTO.getCounter());

					} else if (calculationDTO.isADSL()) {
						String sql = " UPDATE " + infoDAO.getADSLNumberDBTable(calculationDTO.getSubscriberNumber())
								+ " SET POINTS=POINTS+?,COUNTER=? " + " WHERE ADSL_NO=? AND COUNTER=?";

						query = session.createSQLQuery(sql).setParameter(0, rewardPoints)
								.setParameter(1,
										(tabDTO.getCounter() >= Integer.MAX_VALUE ? 1 : tabDTO.getCounter() + 1))
								.setParameter(2, tabDTO.getADSLNumber()).setParameter(3, tabDTO.getCounter());

					}
				}

				start = System.currentTimeMillis();

				String sql = " UPDATE " + infoDAO.getLoyaltyProfileDBTable(calculationDTO.getLoyaltyID() + "")
						+ " SET REWARD_POINTS=REWARD_POINTS+?,TIER_POINTS=TIER_POINTS+? ,STATUS_POINTS=STATUS_POINTS+?,"
						+ " TIER_ID=?,COUNTER=?,TIER_UPDATED_DATE=?,POINTS_CREDITED_DATE=?,DAYWISE_REWARD_POINTS=? "
						+ " WHERE LOYALTY_ID=? AND COUNTER=?";
				int index = 0;
				Query query1 = session.createSQLQuery(sql).setParameter(index++, rewardPoints)
						.setParameter(index++, rewardPoints).setParameter(index++, statusPoints)
						.setParameter(index++, loyaltyProfileTabDTO.getTierId())
						.setParameter(index++,
								(loyaltyProfileTabDTO.getCounter() >= Integer.MAX_VALUE ? 1
										: loyaltyProfileTabDTO.getCounter() + 1))
						.setParameter(index++, loyaltyProfileTabDTO.getTierUpdatedDate())
						.setParameter(index++, new Date())
						.setParameter(index++, calculationDTO.getDayWiseRewardPoints())
						.setParameter(index++, loyaltyProfileTabDTO.getLoyaltyID())
						.setParameter(index++, loyaltyProfileTabDTO.getCounter());

				int i = query == null ? 1 : query.executeUpdate();

				int j = query1.executeUpdate();

				if (i > 0 && j > 0) {
					/*
					 * loyaltyTransactionTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
					 * loyaltyTransactionTabDTO.setStatusID(calculationDTO.isStatusPoints() ?
					 * LoyaltyTransactionStatus.statusPointsOnlyAdded :
					 * LoyaltyTransactionStatus.rewardsPointsAdded);
					 * loyaltyTransactionTabDTO.setTestNumber(calculationDTO.getTestNumber()); //
					 * Test // Number // flag
					 */
					loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID,
							String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));// added S
					loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId,
							String.valueOf(
									calculationDTO.isStatusPoints() ? LoyaltyTransactionStatus.statusPointsOnlyAdded
											: LoyaltyTransactionStatus.manuallyRewardsPointAdded));// added S
					loyaltyTransactionMap.put(LoyalityTransactionConstants.testNumber,
							String.valueOf(calculationDTO.getTestNumber()));// added S
					loyaltyTransactionMap.put(LoyalityTransactionConstants.description, calculationDTO.getDesc());

					tierAndBonusCalculationDAO = new TierAndBonusCalculationDAO();

					c = Calendar.getInstance();
					configParmeterDTO = Cache.configParameterMap.get("TIER_POINTS_VALIDITY_DAYS");
					if (configParmeterDTO != null && configParmeterDTO.getParameterType() != null
							&& !configParmeterDTO.getParameterValue().equalsIgnoreCase(""))
						c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(configParmeterDTO.getParameterValue())); // number
																												// of
																												// days
																												// to
																												// add
					expiryDate = c.getTime();
					exp = sdf.format(expiryDate);
					logger.info(">>>exp>>" + exp);
					// ---ExpiryDate
					loyaltyTransactionMap.put(LoyalityTransactionConstants.expiryDate, exp);
					loyaltyTransactionTabDTO = loyalityCommonTransaction
							.loyaltyTransactionSetter(loyaltyTransactionTabDTO, loyaltyTransactionMap);// added S
					registerAcctDAO.insertLoyaltyTransactionDetails(session, loyaltyTransactionTabDTO);
					// -- ExpiryDate
					expiryDate = sdf.parse(exp);
					String expiryQuarter = ((c.get(Calendar.MONTH) / 3) + 1) + "" + c.get(Calendar.YEAR) + "";
					logger.info("EXPIRY date and quarter :: " + expiryDate + " :: " + expiryQuarter);
					TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO = null;
					tierAndBonusPointDetailsDTO = tierAndBonusCalculationDAO.checkInTableFortierAndBonusPointDetails(
							expiryDate, subscriberNumberTabDTO.getSubscriberNumber() + "",
							loyaltyProfileTabDTO.getLoyaltyID());

					if (tierAndBonusPointDetailsDTO != null) {

						tierAndBonusPointDetailsDTO.setTierCreateDate(new Date());
						tierAndBonusPointDetailsDTO
								.setTierPoints(tierAndBonusPointDetailsDTO.getTierPoints() + rewardPoints);
						tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()
								+ tierAndBonusPointDetailsDTO.getBonusPoints());

						tierAndBonusCalculationDAO.updateTierAndBonusDEtails(tierAndBonusPointDetailsDTO);

					} else {
						tierAndBonusPointDetailsDTO = new TierAndBonusPointDetailsDTO();
						tierAndBonusPointDetailsDTO.setMsisdn(subscriberNumberTabDTO.getSubscriberNumber() + "");
						tierAndBonusPointDetailsDTO.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID() + "");

						tierAndBonusPointDetailsDTO.setTierCreateDate(new Date());
						tierAndBonusPointDetailsDTO.setTierPoints(rewardPoints);

						tierAndBonusPointDetailsDTO.setExpiryDate(expiryDate);
						tierAndBonusPointDetailsDTO.setExpiryQuarter(expiryQuarter);
						tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()
								+ tierAndBonusPointDetailsDTO.getBonusPoints());
						tierAndBonusCalculationDAO.insetTierAndBonusDetails(tierAndBonusPointDetailsDTO);
					}
					transaction.commit();

					System.out.println("get updatioh --------------- >" + (System.currentTimeMillis() - start));
					break;

				} else {
					transaction.rollback();
				}

				loyaltyTransactionTabDTO = null;

				loopCounter++;

			} // while

			if (loopCounter >= 10) {
				logger.info(calculationDTO.getTransactionId() + ": System is busy.");
				throw new CommonException(
						Cache.getServiceStatusMap().get("REW_FAIL_" + calculationDTO.getDefaultLanguage())
								.getStatusCode(),
						Cache.getServiceStatusMap().get("REW_FAIL_" + calculationDTO.getDefaultLanguage())
								.getStatusDesc());

			}

			// Notification for Tier Upgrade Time
			try {
				if (calculationDTO.getProfileTierID() < calculationDTO.getNextTierID()) {

					logger.info(calculationDTO.getTransactionId() + ": Tier Got Upgraded for Loyalty ID:  "
							+ calculationDTO.getLoyaltyID() + " From " + calculationDTO.getProfileTierID() + " to "
							+ calculationDTO.getNextTierID());
					if (loyaltyProfileTabDTO.getContactNumber() != null
							&& !loyaltyProfileTabDTO.getContactNumber().trim().equals("")) {
						languageDAO = new LanguageDAO();
						loyaltyProfileTabDTO
								.setDefaultLanguage(languageDAO.getLanguageID(loyaltyProfileTabDTO.getContactNumber()));
						// NotificationPanel notificationPanel = new NotificationPanel();
						// notificationPanel.rewardPointsTierUpgradeNotification(loyaltyProfileTabDTO,
						// calculationDTO);
						isCodeSent = checkCodeIsSent(loyaltyProfileTabDTO.getLoyaltyID());
						logger.info("TransactionId:" + calculationDTO.getTransactionId() + "ISCODESENT:" + isCodeSent);
						if (!isCodeSent) {
							logger.info("TransactionId:" + calculationDTO.getTransactionId()
									+ "Code we are sending for first time");
							ApplicationCodesDTO applicationCodesDTO = getCodeForCurrentTier(
									calculationDTO.getTransactionId(), calculationDTO.getLoyaltyID(),
									calculationDTO.getNextTierID(), "101");
							if (applicationCodesDTO != null) {
								// Map in table msisdn and app
								auditAppMsisdnMapping(calculationDTO.getTransactionId(), loyaltyProfileTabDTO,
										applicationCodesDTO, loyaltyProfileTabDTO.getContactNumber());
								// notificationPanel.tierUpdgradeAppInfoNotification(loyaltyProfileTabDTO,
								// calculationDTO, applicationCodesDTO);
								updateLoyaltyProfile(loyaltyProfileTabDTO);
							}

						}

						// notificationPanel = null;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(calculationDTO.getTransactionId() + ": ", e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(calculationDTO.getTransactionId() + ": ", e);

			if (transaction != null)
				transaction.rollback();

			if (e instanceof CommonException)
				throw (CommonException) e;

			throw new CommonException(
					Cache.getServiceStatusMap().get("REW_FAIL_" + calculationDTO.getDefaultLanguage()).getStatusCode(),
					Cache.getServiceStatusMap().get("REW_FAIL_" + calculationDTO.getDefaultLanguage()).getStatusDesc());

		} finally {
			subscriberNumberTabDTO = null;
			tabDTO = null;
			accountNumberTabDTO = null;
			infoDAO = null;
			registerAcctDAO = null;
			loyaltyTransactionTabDTO = null;
			tableDetailsDAO = null;
			isCodeSent = false;
			if (session != null)
				session.close();
			session = null;
			loyaltyProfileTabDTO = null;
			languageDAO = null;
			dateFormat = null;
		}

	}// /calculateRewardPointsStep2

	public void updateLoyaltyProfile(LoyaltyProfileTabDTO loyaltyProfileTabDTO) {
		Session session = null;
		String sql = null;
		Query query = null;
		// int flag=0;
		try {
			logger.info("update loyalty profile");
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "UPDATE LOYALTY_PROFILE_0 SET CODESENT=:value WHERE LOYALTY_ID=:loyaltyId";
			query = session.createSQLQuery(sql);
			query.setParameter("value", 1);
			query.setParameter("loyaltyId", loyaltyProfileTabDTO.getLoyaltyID());
			query.executeUpdate();
			logger.info("updated loyalty profile");

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			session = null;
		}

	}

	public boolean checkCodeIsSent(Long loyaltyID) {
		Session session = null;
		String sql = null;
		Query query = null;
		boolean isCodeSent = false;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "SELECT CODESENT FROM LOYALTY_PROFILE_0 WHERE LOYALTY_ID=:loyaltyId";
			query = session.createSQLQuery(sql);
			query.setParameter("loyaltyId", loyaltyID);
			List list = query.list();
			if (!list.isEmpty()) {
				int flag = Integer.parseInt(list.get(0) + "");
				if (flag == 1) {
					isCodeSent = true;
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			session = null;
		}
		return isCodeSent;
	}

	public void auditAppMsisdnMapping(String txnId, LoyaltyProfileTabDTO loyaltyProfileTabDTO,
			ApplicationCodesDTO applicationCodesDTO, String msisdn) {
		TierUpgradationDAO tierUpgradationDAO = null;
		AppMsisdnMappingDTO appMsisdnMapping = null;
		try {
			appMsisdnMapping = new AppMsisdnMappingDTO();
			appMsisdnMapping.setAppId(applicationCodesDTO.getAppId());
			appMsisdnMapping.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID());
			appMsisdnMapping.setTierId(loyaltyProfileTabDTO.getTierId());
			appMsisdnMapping.setPassCode(applicationCodesDTO.getAppCode());
			appMsisdnMapping.setMsisdn(msisdn);

			tierUpgradationDAO = new TierUpgradationDAO();
			tierUpgradationDAO.mapAppToMsisdn(txnId, appMsisdnMapping);
		} finally {
			tierUpgradationDAO = null;
		}
	}

	public ApplicationCodesDTO getCodeForCurrentTier(String txnId, long loyaltyId, int nextTierID, String appId) {
		TierUpgradationDAO tierUpgradationDAO = null;
		ArrayList<ApplicationCodesDTO> applicationCodeList = null;
		ApplicationCodesDTO applicationCodesDTO = null;
		String appCode = null;
		boolean isCodeBlocked = false;
		int retryCount = 0;
		try {
			tierUpgradationDAO = new TierUpgradationDAO();
			do {
				applicationCodeList = tierUpgradationDAO.getCodeForCurrentTier(txnId, loyaltyId, nextTierID, appId);
				if (applicationCodeList == null || applicationCodeList.size() == 0) {
					logger.info("Service : PointCalculation -- Transaction ID :" + txnId + " LoyaltyId " + loyaltyId
							+ " There is no code" + " mapped/remaining for this tier " + nextTierID);
					/*
					 * Since there is no codes remaining, we are assuming that code is blocked and
					 * loop can be exit
					 */
					isCodeBlocked = true;
				} else {
					applicationCodesDTO = applicationCodeList.get(0);
					appCode = applicationCodesDTO.getAppCode();
					logger.debug("Service : PointCalculation -- Transaction ID :" + txnId + " LoyaltyId :" + loyaltyId
							+ " Fetched code :" + appCode + " for Tier :" + nextTierID);
					int updateCount = tierUpgradationDAO.blockCodeForTier(txnId, loyaltyId, appCode, nextTierID);
					if (updateCount > 0) {
						isCodeBlocked = true;
					} else {
						retryCount++;
					}

				}
			} while (!isCodeBlocked && retryCount < 10);

		} catch (Exception e) {
			logger.error("Service : PointCalculation -- Transaction ID :" + txnId + " LoyaltyId :" + loyaltyId + " "
					+ "Exception in selecting appCodes ");
		} finally {
			tierUpgradationDAO = null;
			applicationCodeList = null;
		}
		return applicationCodesDTO;
	}

	public HashMap<String, String> bonusPointDetails() {
		Session session = null;
		Transaction transaction = null;
		RewardPointsCalculationDTO calculationDTO = new RewardPointsCalculationDTO();
		HashMap<String, String> bonusDetailsMap = new HashMap<String, String>();

		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			// transaction=session.beginTransaction();

			String sql = " SELECT NAME,BONUS_STATUS_POINTS,BONUS_REWARD_POINTS FROM  LMS_CFG_BONUS_POINTS";

			Query query = session.createSQLQuery(sql);

			logger.info("SQL IS" + sql);

			List<Object[]> list = query.list();

			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					String bonusvalue = obj[1] + ":" + obj[2];
					bonusDetailsMap.put((String) obj[0], bonusvalue);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (session != null)
				session.close();
			session = null;
		}
		return bonusDetailsMap;

	}

	public void pointsCalculation(LoyaltyProfileTabDTO loyaltyProfileTabDTO, RewardPointsCalculationDTO calculationDTO)
			throws CommonException {
		logger.info("calculationDTO reward points" + calculationDTO.getRequestRewardPoints() + "::\n" + calculationDTO.getProfileRewardPoints());
		// Override the configured points,if points are coming in request
		if ((calculationDTO.getRequestRewardPoints() != null && calculationDTO.getRequestRewardPoints() > 0)||(calculationDTO.getRequestRewardPoints() != null && calculationDTO.getRequestRewardPoints() < 0) || (calculationDTO.getRequestStatusPoints() != null && calculationDTO.getRequestStatusPoints() > 0)) {

		
			if (calculationDTO.getRequestRewardPoints() != null && calculationDTO.getRequestRewardPoints() > 0)
			{
				loyaltyProfileTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints() + calculationDTO.getRequestRewardPoints());
				
				calculationDTO.setCalculatedRewardPoints(calculationDTO.getRequestRewardPoints());
			}else {
				loyaltyProfileTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints() + calculationDTO.getRequestRewardPoints());
				
				calculationDTO.setCalculatedRewardPoints(calculationDTO.getRequestRewardPoints());
			}
			

			if (calculationDTO.getRequestStatusPoints() != null && calculationDTO.getRequestStatusPoints() > 0) {
				
			
				loyaltyProfileTabDTO.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints() + calculationDTO.getRequestStatusPoints());
				calculationDTO.setCalculatedStatusPoints(calculationDTO.getRequestStatusPoints());
			}
			
			
			
			logger.info("Calculated reward points" + calculationDTO.getRequestRewardPoints()+"rewardPoint:"+loyaltyProfileTabDTO.getRewardPoints());
			//checkTierUpgration(loyaltyProfileTabDTO, calculationDTO);
			return;
		}


		// Unit Calculation
		
		if (Cache.rewardPointsCategoryMap.get(calculationDTO.getRewardPointsCategory()) == null) {
			logger.info(calculationDTO.getTransactionId() + ": Invalid Reward Points Category:"
					+ calculationDTO.getRewardPointsCategory());
			throw new CommonException("Invalid Reward Points Category");
		}

		boolean isUnitCalculation = Cache.rewardPointsCategoryMap
				.get(new Integer(calculationDTO.getRewardPointsCategory())).getUnitsCalculation().equalsIgnoreCase("N")
						? false
						: true;
		logger.info(calculationDTO.getTransactionId() + ": Unit Calculation " + isUnitCalculation);
		// If volume is null or no unit calculation ,means can add points
		// directly
		if (calculationDTO.getVolume() == null || isUnitCalculation == false) {

			if (Cache.tierCategoryDetailsMap.get(loyaltyProfileTabDTO.getTierId()) == null) {
				logger.info(calculationDTO.getTransactionId() + ": Tier Category Details Not Configured");
				throw new CommonException("Tier Category Details Not defind");
			}
			logger.info("loyaltyProfileTabDTO.getTierId()" + loyaltyProfileTabDTO.getTierId()
					+ "calculationDTO.getRewardPointsCategory()>>>>>>>>>>>>>>>"
					+ calculationDTO.getRewardPointsCategory());
			List<CategoryDetailsDTO> list = Cache.tierCategoryDetailsMap.get(loyaltyProfileTabDTO.getTierId())
					.get(new Integer(calculationDTO.getRewardPointsCategory()));

			if (list == null || list.size() == 0) {
				logger.info(calculationDTO.getTransactionId() + ": Tier Category Details Not defind");
				throw new CommonException("Tier Category Details Not defind");
			}

			logger.info(calculationDTO.getTransactionId() + ": Tier ID :" + loyaltyProfileTabDTO.getTierId()
					+ "  Category ID :" + calculationDTO.getRewardPointsCategory() + "   Details :" + list);

			for (CategoryDetailsDTO detailsDTO : list) {
				if (calculationDTO.getVolume() != null
						&& (detailsDTO.getMinValue() != null || detailsDTO.getMaxValue() != null)) {
					if (checkBetween(calculationDTO.getVolume(), detailsDTO.getMinValue(), detailsDTO.getMaxValue())) {
						loyaltyProfileTabDTO
								.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints() + detailsDTO.getRewardPoints());
						loyaltyProfileTabDTO
								.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints() + detailsDTO.getStatusPoints());
						calculationDTO.setCalculatedRewardPoints(detailsDTO.getRewardPoints());
						calculationDTO.setCalculatedStatusPoints(detailsDTO.getStatusPoints());
						checkTierUpgration(loyaltyProfileTabDTO, calculationDTO);

						return;
					}
				} else {
					loyaltyProfileTabDTO
							.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints() + detailsDTO.getRewardPoints());
					loyaltyProfileTabDTO
							.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints() + detailsDTO.getStatusPoints());
					calculationDTO.setCalculatedRewardPoints(detailsDTO.getRewardPoints());
					calculationDTO.setCalculatedStatusPoints(detailsDTO.getStatusPoints());
					checkTierUpgration(loyaltyProfileTabDTO, calculationDTO);

					return;
				}

			}

		}

		if (isUnitCalculation) {
			while (calculationDTO.getVolume() > 0) {

				if (Cache.tierCategoryDetailsMap.get(loyaltyProfileTabDTO.getTierId()) == null) {
					logger.info(calculationDTO.getTransactionId() + ": Tier Category Details Not Configured");
					throw new CommonException("Tier Category Details Not defind");
				}

				List<CategoryDetailsDTO> list = Cache.tierCategoryDetailsMap.get(loyaltyProfileTabDTO.getTierId())
						.get(new Integer(calculationDTO.getRewardPointsCategory()));

				System.out.println(list);

				if (list == null || list.size() == 0) {
					logger.info(calculationDTO.getTransactionId() + ": Tier Category Details Not Configured");
					throw new CommonException("Tier Category Details Not Configured");
				}

				logger.info(calculationDTO.getTransactionId() + ": Tier ID :" + loyaltyProfileTabDTO.getTierId()
						+ "  Category ID :" + calculationDTO.getRewardPointsCategory() + "   Volume : "
						+ calculationDTO.getVolume());

				TierInfoDTO tierInfoDTO = Cache.tierInfoMap.get(loyaltyProfileTabDTO.getTierId());

				for (CategoryDetailsDTO detailsDTO : list) {

					if (detailsDTO.getMinValue() != null && detailsDTO.getMinValue() > calculationDTO.getVolume()) {

						calculationDTO.setVolume(0.0);

					} else if (detailsDTO.getMinValue() != null || detailsDTO.getMaxValue() != null) {

						// checking min and max value

						if (checkBetween(calculationDTO.getVolume(), detailsDTO.getMinValue(),
								detailsDTO.getMaxValue())) {

							if (tierInfoDTO.getMaxValue() != null) {

								calculateRequiredVolume(tierInfoDTO.getMaxValue(), loyaltyProfileTabDTO, detailsDTO,
										calculationDTO);

								break;

							} else {
								calculateFullVolume(loyaltyProfileTabDTO, detailsDTO, calculationDTO);

							}
						}
					} else {

						if (tierInfoDTO.getMaxValue() != null) {

							calculateRequiredVolume(tierInfoDTO.getMaxValue(), loyaltyProfileTabDTO, detailsDTO,
									calculationDTO);
							break;

						} else {
							calculateFullVolume(loyaltyProfileTabDTO, detailsDTO, calculationDTO);
						}

					}
				}

			}

		} else {
			// No Unit Calculation

		}

	}// pointsCalculation

	public void calculateRequiredVolume(Integer tierMaxValue, LoyaltyProfileTabDTO loyaltyProfileTabDTO,
			CategoryDetailsDTO detailsDTO, RewardPointsCalculationDTO calculationDTO) {
		Double requiredVolume = Math
				.ceil((tierMaxValue - loyaltyProfileTabDTO.getStatusPoints()) / detailsDTO.getStatusPoints());
		if (requiredVolume < 0) {
			loyaltyProfileTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints()
					+ (calculationDTO.getVolume() * detailsDTO.getRewardPoints()));
			loyaltyProfileTabDTO.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints()
					+ (calculationDTO.getVolume() * detailsDTO.getStatusPoints()));

			logger.info(calculationDTO.getTransactionId() + ": Required Volume required Volume  "
					+ calculationDTO.getVolume() + " Total Reward Points " + loyaltyProfileTabDTO.getRewardPoints()
					+ "   Status Points " + loyaltyProfileTabDTO.getStatusPoints());

			calculationDTO.setCalculatedRewardPoints(calculationDTO.getCalculatedRewardPoints()
					+ ((calculationDTO.getVolume() * detailsDTO.getRewardPoints())));
			calculationDTO.setCalculatedStatusPoints(calculationDTO.getCalculatedStatusPoints()
					+ ((calculationDTO.getVolume() * detailsDTO.getStatusPoints())));

			logger.info(calculationDTO.getTransactionId() + ": Required Volume Tier Before "
					+ loyaltyProfileTabDTO.getTierId());
			checkTierUpgration(loyaltyProfileTabDTO, calculationDTO);
			logger.info(calculationDTO.getTransactionId() + ": Required Volume Tier After "
					+ loyaltyProfileTabDTO.getTierId());

			calculationDTO.setVolume(0.0);

			return;
		}

		if (calculationDTO.getVolume() < requiredVolume) {
			requiredVolume = calculationDTO.getVolume();
		}

		loyaltyProfileTabDTO.setRewardPoints(
				loyaltyProfileTabDTO.getRewardPoints() + (requiredVolume * detailsDTO.getRewardPoints()));
		loyaltyProfileTabDTO.setStatusPoints(
				loyaltyProfileTabDTO.getStatusPoints() + (requiredVolume * detailsDTO.getStatusPoints()));

		logger.info(calculationDTO.getTransactionId() + ": Required Volume required Volume  " + requiredVolume
				+ " Total Reward Points " + loyaltyProfileTabDTO.getRewardPoints() + "   Status Points "
				+ loyaltyProfileTabDTO.getStatusPoints());

		calculationDTO.setCalculatedRewardPoints(
				calculationDTO.getCalculatedRewardPoints() + ((requiredVolume * detailsDTO.getRewardPoints())));
		calculationDTO.setCalculatedStatusPoints(
				calculationDTO.getCalculatedStatusPoints() + ((requiredVolume * detailsDTO.getStatusPoints())));

		logger.info(calculationDTO.getTransactionId() + ": Required Volume Tier Before "
				+ loyaltyProfileTabDTO.getTierId());
		checkTierUpgration(loyaltyProfileTabDTO, calculationDTO);
		logger.info(
				calculationDTO.getTransactionId() + ": Required Volume Tier After " + loyaltyProfileTabDTO.getTierId());

		calculationDTO.setVolume(calculationDTO.getVolume() - requiredVolume);

	}// calculateRequiredVolume

	public void calculateFullVolume(LoyaltyProfileTabDTO loyaltyProfileTabDTO, CategoryDetailsDTO detailsDTO,
			RewardPointsCalculationDTO calculationDTO) {
		loyaltyProfileTabDTO.setRewardPoints(
				loyaltyProfileTabDTO.getRewardPoints() + (calculationDTO.getVolume() * detailsDTO.getRewardPoints()));
		loyaltyProfileTabDTO.setStatusPoints(
				loyaltyProfileTabDTO.getStatusPoints() + (calculationDTO.getVolume() * detailsDTO.getStatusPoints()));
		logger.info(calculationDTO.getTransactionId() + ": Full Volume Total Reward Points "
				+ loyaltyProfileTabDTO.getRewardPoints() + "  Total Status Points "
				+ loyaltyProfileTabDTO.getStatusPoints());

		calculationDTO.setCalculatedRewardPoints(calculationDTO.getCalculatedRewardPoints()
				+ ((calculationDTO.getVolume() * detailsDTO.getRewardPoints())));
		calculationDTO.setCalculatedStatusPoints(calculationDTO.getCalculatedStatusPoints()
				+ ((calculationDTO.getVolume() * detailsDTO.getStatusPoints())));

		logger.info(
				calculationDTO.getTransactionId() + ": Full Volume Tier Before " + loyaltyProfileTabDTO.getTierId());
		checkTierUpgration(loyaltyProfileTabDTO, calculationDTO);
		logger.info(calculationDTO.getTransactionId() + ": Full Volume Tier After " + loyaltyProfileTabDTO.getTierId());
		calculationDTO.setVolume(0.0);

	}// calculateFullVolume

	public boolean checkBetween(Double volume, Double minValue, Double maxValue) {

		return volume != null
				? (minValue != null ? minValue <= volume : true) && (maxValue != null ? volume <= maxValue : true)
				: false;

	}// checkBetween

	public void checkTierUpgration(LoyaltyProfileTabDTO loyaltyProfileTabDTO,
			RewardPointsCalculationDTO calculationDTO) {

		TableDetailsDAO tableDetailsDAO = null;
		Iterator<Integer> iterator = Cache.getTierInfoMap().keySet().iterator();

		Double statusPoints = loyaltyProfileTabDTO.getStatusPoints();

		while (iterator.hasNext()) {
			TierInfoDTO infoDTO = Cache.getTierInfoMap().get(iterator.next());

			if (infoDTO.getMinValue() == null && infoDTO.getMaxValue() == null)
				continue;

			if ((infoDTO.getMinValue() != null ? infoDTO.getMinValue() <= statusPoints : true)
					&& (infoDTO.getMaxValue() != null ? statusPoints < infoDTO.getMaxValue() : true)) {
				logger.info(calculationDTO.getTransactionId() + ": Got Tier For this Status Points :" + statusPoints
						+ " Min Value :" + infoDTO.getMinValue() + "  Max Value :" + infoDTO.getMaxValue()
						+ "   Tier ID : " + infoDTO.getTierId());
				if (infoDTO.getTierId() > loyaltyProfileTabDTO.getTierId()) {// Check for not to downgrade the tier
					logger.info(calculationDTO.getTransactionId() + ": Tier is going to upgrade for LoyaltyID "
							+ loyaltyProfileTabDTO.getLoyaltyID() + " from  " + loyaltyProfileTabDTO.getTierId()
							+ " to   " + infoDTO.getTierId());
					loyaltyProfileTabDTO.setTierId(infoDTO.getTierId());
				} else
					logger.info(calculationDTO.getTransactionId() + ": Tier remains same for LoyaltyID  "
							+ loyaltyProfileTabDTO.getLoyaltyID());
			}

		}

		if (!loyaltyProfileTabDTO.getTierId().equals(calculationDTO.getProfileTierID())) {
			loyaltyProfileTabDTO.setTierUpdatedDate(new Date());

			// ***************Changed By Deepak **************************\\
			// Checking if the upgraded Tier Id is AL_FUQ,
			// If So Calling Provision for activating Pack ID configured in
			// System.Properties
			try {
				tableDetailsDAO = new TableDetailsDAO();
				logger.info(calculationDTO.getProfileTierID() + "########Got Tier ID After Tire Change is###########"
						+ loyaltyProfileTabDTO.getTierId());
				if (loyaltyProfileTabDTO.getTierId() == 4) {
					DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
					LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO = null;

					loyaltyRegisteredNumberTabDTO = tableDetailsDAO.getLoyaltyRegisteredNumberDetails(
							loyaltyProfileTabDTO.getLoyaltyID(), loyaltyProfileTabDTO.getContactNumber());

					String callType = Cache.getCacheMap()
							.get("CALL_TYPE_" + loyaltyRegisteredNumberTabDTO.getAccountTypeId()) != null
									? Cache.getCacheMap()
											.get("CALL_TYPE_" + loyaltyRegisteredNumberTabDTO.getAccountTypeId())
									: "1";

					logger.info("###########CALL TYPE IS ############" + callType);

					String subsNumber = null;
					subsNumber = calculationDTO.getSubscriberNumber();
					String noToSendConfirm = calculationDTO.getSubscriberNumber();
					String requestMDN = null;
					String contactMDN = loyaltyProfileTabDTO.getContactNumber();
					if (Integer.parseInt(calculationDTO.getChannel()) > 2) {
						// other than SMS and USSD
						noToSendConfirm = loyaltyProfileTabDTO.getContactNumber();
					}
					if (calculationDTO.getSubscriberNumber() != null) {
						requestMDN = calculationDTO.getSubscriberNumber();
					}
					if ((loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 200
							|| loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 201))
						if (calculationDTO.getSubscriberNumber() != null) {
							noToSendConfirm = calculationDTO.getSubscriberNumber();
						} else
							noToSendConfirm = loyaltyProfileTabDTO.getContactNumber();

					Set<String> notificationNumberList = new HashSet<String>();
					logger.info("requestMDN" + requestMDN + "subsNumber" + subsNumber + "contactMDN" + contactMDN);
					notificationNumberList.add(requestMDN);
					notificationNumberList.add(subsNumber);
					notificationNumberList.add(contactMDN);
					notificationNumberList.remove("");
					notificationNumberList.remove(null);
					loyaltyProfileTabDTO.setContactNumberSet(notificationNumberList);

				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	}// checkTierUpgration

	// Updated code Sajith k s ***start
	public int checkTierUpgrationBasedOnDate(Double statusPoints, LoyaltyProfileTabDTO loyaltyprofile) {
		logger.info("checkTierUpgrationBasedOnDate");

		int numberOfYears = 1;
		int currentTierId;
		int newTierId;
		currentTierId = loyaltyprofile.getTierId();
		long daysInMiliseconds = 86400000;
		long numberOfDaysInYear = 365;
		Date tierUpdatedDate = loyaltyprofile.getTierUpdatedDate();
		DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date currentDate = new Date();
		df.format(currentDate);
		Calendar a = Calendar.getInstance();
		Calendar b = Calendar.getInstance();
		a.setTime(tierUpdatedDate);
		b.setTime(currentDate);
		logger.info("Last tier updated date >>>>" + tierUpdatedDate);
		logger.info("current date >>>>" + currentDate);
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
				|| (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
			diff--;
		}

		// long
		// differenceInDays=(currentDate.getTime()-tierUpdatedDate.getTime())/daysInMiliseconds;

		Iterator<Integer> iterator = Cache.getTierInfoMap().keySet().iterator();

		logger.info("checkTierUpgrationBasedOnDate  going to while loop");
		while (iterator.hasNext()) {
			logger.info("checkTierUpgrationBasedOnDate   inside while loop with new logic");
			TierInfoDTO infoDTO = Cache.getTierInfoMap().get(iterator.next());

			if (infoDTO.getMinValue() == null && infoDTO.getMaxValue() == null)
				continue;

			if ((infoDTO.getMinValue() != null ? infoDTO.getMinValue() <= statusPoints : true)
					&& (infoDTO.getMaxValue() != null ? statusPoints < infoDTO.getMaxValue() : true)) {
				logger.info(" Status Points :" + statusPoints + " Min Value :" + infoDTO.getMinValue() + "  Max Value :"
						+ infoDTO.getMaxValue() + "   Tier ID : " + infoDTO.getTierId());
				newTierId = infoDTO.getTierId();
				logger.info("currentTierId---->" + currentTierId);
				logger.info("new tierId----->" + newTierId);
				if (newTierId < currentTierId) {
					logger.info("Tier Down gradation");
					if (diff < numberOfYears) {
						logger.info("less than an year, so new tier ID is ---->" + loyaltyprofile.getTierId());
						return loyaltyprofile.getTierId();
					}
				}
				return infoDTO.getTierId();
			}

		}

		return 1;

	}// checkTierUpgration
		// Updated code Sajith k s ***end

	public int checkTierUpgration(Double statusPoints) {
		Iterator<Integer> iterator = Cache.getTierInfoMap().keySet().iterator();

		while (iterator.hasNext()) {
			TierInfoDTO infoDTO = Cache.getTierInfoMap().get(iterator.next());

			if (infoDTO.getMinValue() == null && infoDTO.getMaxValue() == null)
				continue;

			if ((infoDTO.getMinValue() != null ? infoDTO.getMinValue() <= statusPoints : true)
					&& (infoDTO.getMaxValue() != null ? statusPoints < infoDTO.getMaxValue() : true)) {
				logger.info(" Status Points :" + statusPoints + " Min Value :" + infoDTO.getMinValue() + "  Max Value :"
						+ infoDTO.getMaxValue() + "   Tier ID : " + infoDTO.getTierId());
				return infoDTO.getTierId();
			}

		}

		return 1;

	}// checkTierUpgration

	public Double[] pointsCalculation(int tierId, RewardPointsCalculationDTO calculationDTO) throws CommonException {
		// 0 position for Reward Points
		// 1 Position for Satus Points
		Double[] points = new Double[2];

		if (Cache.tierCategoryDetailsMap.get(calculationDTO.getRewardPointsCategory()) == null) {
			logger.info(calculationDTO.getTransactionId() + ": Invalid Reward Points Category");
			throw new CommonException("Invalid Reward Points Category");
		}

		// Override the configured points,if points are coming in request
		if (calculationDTO.getRequestRewardPoints() != null && calculationDTO.getRequestStatusPoints() != null) {
			points[0] = calculationDTO.getRequestRewardPoints();
			points[1] = calculationDTO.getRequestStatusPoints();
			return points;
		}

		if (Cache.tierCategoryDetailsMap.get(new Integer(tierId)) == null) {
			logger.info(calculationDTO.getTransactionId() + ": Tier Category Details Not Configured");
			throw new CommonException("Tier Category Details Not defind");
		}

		List<CategoryDetailsDTO> list = Cache.tierCategoryDetailsMap.get(new Integer(tierId))
				.get(new Integer(calculationDTO.getRewardPointsCategory()));

		if ((list == null || list.size() == 0) && calculationDTO.getRequestRewardPoints() == null
				&& calculationDTO.getRequestStatusPoints() == null) {
			logger.info(calculationDTO.getTransactionId() + ": Tier Category Details Not defind");
			throw new CommonException("Tier Category Details Not defind");
		}

		logger.info(calculationDTO.getTransactionId() + ": Tier ID :" + tierId + "  Category ID :"
				+ calculationDTO.getRewardPointsCategory() + "   Details :" + list);

		boolean isUnitCalculation = Cache.rewardPointsCategoryMap
				.get(new Integer(calculationDTO.getRewardPointsCategory())).getUnitsCalculation().equalsIgnoreCase("N")
						? false
						: true;

		for (CategoryDetailsDTO detailsDTO : list) {
			// checking min and max value
			if (calculationDTO.getVolume() != null
					&& (detailsDTO.getMinValue() != null || detailsDTO.getMaxValue() != null)) {
				if (checkBetween(calculationDTO.getVolume(), detailsDTO.getMinValue(), detailsDTO.getMaxValue())) {

					points[0] = isUnitCalculation ? calculationDTO.getVolume() * detailsDTO.getRewardPoints()
							: detailsDTO.getRewardPoints();
					points[1] = isUnitCalculation ? calculationDTO.getVolume() * detailsDTO.getStatusPoints()
							: detailsDTO.getStatusPoints();
					break;
				}
			} else {
				points[0] = detailsDTO.getRewardPoints();
				points[1] = detailsDTO.getStatusPoints();
				break;
			}
		}

		if (points[0] == null || points[1] == null) {
			logger.info(calculationDTO.getTransactionId()
					+ ": Request Volume is not macthing with configuration Volume :" + calculationDTO.getVolume());
			throw new CommonException("Request Volume is not macthing with configuration Volume");
		}
		return points;
	}// pointsCalculation

	public void insertLoyaltyThresholdDetails(LoyaltyProfileTabDTO profileTabDTO,
			RewardPointsCalculationDTO calculationDTO, long threshold, double rewardPoints) {
		Session session = null;
		Transaction transaction = null;
		String sql = null;
		Query query = null;
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			sql = "insert into LOYALTY_THRESHOLD_DETAILS (ID,LOYALTY_ID,SUBSCRIBER_NUMBER,ACCOUNT_NUMBER,VOLUME,REQ_REWARD_POINTS,PONITS_GIVEN,THRESHOLD,EARN_POINTS)"
					+ "values (LOYALTY_THRESHOLD_DETAILS_SEQ.nextVal,?,?,?,?,?,?,?,?)";
			query = session.createSQLQuery(sql).setParameter(0, profileTabDTO.getLoyaltyID())
					.setParameter(1, calculationDTO.isSubscriber() ? calculationDTO.getSubscriberNumber() : null)
					.setParameter(2, calculationDTO.isAccount() ? calculationDTO.getSubscriberNumber() : null)
					.setParameter(3,
							calculationDTO.getOriginalVolume() == null ? 0.0 : calculationDTO.getOriginalVolume())
					.setParameter(4,
							calculationDTO.getRequestRewardPoints() == null ? 0.0
									: calculationDTO.getRequestRewardPoints())
					.setParameter(5,
							calculationDTO.getDayWiseRewardPoints() == null ? 0.0
									: calculationDTO.getDayWiseRewardPoints())
					.setParameter(6, threshold).setParameter(7, rewardPoints);

			query.executeUpdate();

			transaction.commit();

		} catch (Exception e) {
			logger.info(calculationDTO.getTransactionId() + " ", e);
			if (transaction != null)
				transaction.rollback();

		} finally {
			if (session != null)
				session.close();
			session = null;
			transaction = null;
			sql = null;
			query = null;
		}

	}// insertLoyaltyThresholdDetails

	public void notifyARBORSystem(RewardPointsCalculationDTO rewardPointsCalculationDTO) {

		String soapXML = "";
		String URL = Cache.getCacheMap().get("EBILL_NOTIFY_URL");
		try {
			soapXML = getRequestXML(rewardPointsCalculationDTO);
			sendRequest(URL, soapXML);
			logger.info("Calling ARBOR System Over...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			URL = null;
		}
	}

	private String getRequestXML(RewardPointsCalculationDTO rewardPointsCalculationDTO) {

		DateFormat dfOman = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		StringBuilder sb = new StringBuilder();
		sb.append(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:arb=\"http://omantel.om/ArborService\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<arb:addSMSHistoryInCRMRequest>");
		sb.append("<OT_EAI_HEADER>");
		sb.append("<MsgFormat>INQ</MsgFormat>");
		sb.append("<MsgVersion>1.0</MsgVersion>");
		sb.append("<RequestorId>LMS</RequestorId>");
		sb.append("<RequestorChannelId>W</RequestorChannelId>");
		sb.append("<RequestorUserId>LMS</RequestorUserId>");
		sb.append("<RequestorLanguage>E</RequestorLanguage>");
		sb.append("<RequestorSecurityInfo></RequestorSecurityInfo>");
		sb.append("<EaiReference></EaiReference>");
		sb.append("<ReturnCode>0000</ReturnCode>");
		sb.append("</OT_EAI_HEADER>");
		sb.append("<Request>");
		sb.append("<ReferenceNo>" + dfOman.format(Calendar.getInstance().getTime()) + "</ReferenceNo>");
		if (rewardPointsCalculationDTO.getSubscriberNumber().length() == 11)
			sb.append("<CustMobileNo>" + rewardPointsCalculationDTO.getSubscriberNumber().substring(3)
					+ "</CustMobileNo>");
		else
			sb.append("<CustMobileNo>" + rewardPointsCalculationDTO.getSubscriberNumber() + "</CustMobileNo>");
		sb.append("<SmsStatus>2</SmsStatus>");
		sb.append("<SmsMessage>" + Cache.getCacheMap().get("EBILL_NOTIFY_MSG") + "</SmsMessage>");
		sb.append("<CrmSMSDesc>LMSMakasibPoints</CrmSMSDesc>");
		sb.append("<LanguageCode>1</LanguageCode>");
		sb.append("<Title>Omantel</Title>");
		sb.append("<Source>LMS</Source>");
		sb.append("</Request>");
		sb.append("</arb:addSMSHistoryInCRMRequest>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		logger.info(sb.toString());
		return sb.toString();
	}

	public void sendRequest(String respUrl, String xmlString) throws Exception {
		URL url = null;
		HttpURLConnection urlConn = null;
		OutputStreamWriter out = null;
		InputStream in = null;
		SAXBuilder builder = null;
		Document document = null;
		Element rootNode = null;
		ElementFilter filter = null;
		IteratorIterable<Element> it = null;
		Element ele = null;
		String code = "";
		String desc = "";

		try {
			logger.info("URL=" + respUrl);
			url = new URL(respUrl);
			urlConn = (HttpURLConnection) url.openConnection();

			urlConn.setRequestMethod("POST");
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setConnectTimeout(10000);
			out = new OutputStreamWriter(urlConn.getOutputStream());
			long l1 = System.currentTimeMillis();
			out.write(xmlString);
			out.flush();

			in = urlConn.getInputStream();

			logger.info("Time taken : " + (System.currentTimeMillis() - l1));

			builder = new SAXBuilder();
			document = (Document) builder.build(in);
			rootNode = document.getRootElement();

			logger.info(rootNode.getName());

			filter = new ElementFilter("ReturnMessage");

			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info(ele.getName());
				logger.info(ele.getValue().trim());
				desc = ele.getValue().trim();
				ele = null;
			}

			filter = new ElementFilter("ReturnCode");
			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info(ele.getName());
				logger.info(ele.getValue().trim());
				code = ele.getValue().trim();
				ele = null;
			}
			in.close();

			logger.info("RESPONSE CODE:" + code);
			logger.info("RESPONSE DESC:" + desc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
					out = null;
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlConn != null) {
				try {
					urlConn.disconnect();
					urlConn = null;
				} catch (Exception e) {
				}
			}
			url = null;
			builder = null;
			document = null;
			rootNode = null;
		}
	}

	public boolean insertIntoPrepaidInstantTable(RewardPointsCalculationDTO rewardPointsCalculationDTO) {
		Session session = null;
		Transaction tr = null;
		boolean flag = false;
		PostPaidInstantDTO postPaidInstantDTO = new PostPaidInstantDTO();
		logger.info("Class => RewardPointsCalculationDAO :: Inside Prepaid Method");
		try {
			session = HiberanteUtilRule.getSessionFactory().openSession();
			tr = session.beginTransaction();

			String date = rewardPointsCalculationDTO.getActualPaymentDate().substring(0, 8);
			postPaidInstantDTO.setActualPaymentDate(dateFormat1.parse(date));
			logger.info("Class => RewardPointsCalculationDAO :: PAymnt Agency ID"
					+ rewardPointsCalculationDTO.getPaymentAgencyID());
			postPaidInstantDTO.setCollectionCenterID(rewardPointsCalculationDTO.getPaymentAgencyID());
			postPaidInstantDTO.setMsisdn(rewardPointsCalculationDTO.getSubscriberNumber());
			postPaidInstantDTO.setVolume(rewardPointsCalculationDTO.getVolume() + "");
			if (rewardPointsCalculationDTO.getServiceType().equalsIgnoreCase("A"))
				postPaidInstantDTO.setServiceType("GSM-Arbor BP");
			else if (rewardPointsCalculationDTO.getServiceType().equalsIgnoreCase("I"))
				postPaidInstantDTO.setServiceType("Internet-Portal Infranet");
			else if (rewardPointsCalculationDTO.getServiceType().equalsIgnoreCase("T"))
				postPaidInstantDTO.setServiceType("Fixed-TIMS");
			postPaidInstantDTO.setActualPaymentTimeStamp(
					dateFormat2.parse(rewardPointsCalculationDTO.getActualPaymentDate()) + "");
			session.save(postPaidInstantDTO);
			tr.commit();
			flag = true;

		} catch (Exception e) {
			e.printStackTrace();
			if (tr != null) {
				tr.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}

		}

		return flag;

	}

	public GenericDTO settingStatus(String key, RewardPointsCalculationDTO calculationDTO, GenericDTO genericDTO)
			throws CommonException {
		ServiceStatusDTO actionServiceDetailsDTO = null;
		try {
			logger.info(
					">>>>>>>>>>>>>>>>Language id" + calculationDTO.getDefaultLanguage() + ">>>>>>>>>>>>>jkey" + key);
			if ((actionServiceDetailsDTO = Cache.getServiceStatusMap()
					.get(key + "_" + calculationDTO.getDefaultLanguage().trim())) != null) {
				genericDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
				genericDTO.setStatus(actionServiceDetailsDTO.getStatusDesc());
			} else {
				logger.warn(" Service : -- Transaction ID : " + calculationDTO.getTransactionId() + " MoNumber "
						+ calculationDTO.getSubscriberNumber() + ""
						+ " No Key Defined in Service Status table for Key :- " + key);
				genericDTO.setStatusCode("SC0001");
				genericDTO.setStatus(key);
			}
		} catch (Exception e) {
			logger.info("Transaction id " + calculationDTO.getTransactionId() + " exception " + e.getMessage());
			e.printStackTrace();
		}
		return genericDTO;
	}

	public CDRInformationDTO buildingCdrDtoAndWritingCdr(RewardPointsCalculationDTO calculationDTO,
			GenericDTO genericDTO) {
		CDRInformationDTO cdrInformationDTO = null;
		try {
			cdrInformationDTO = new CDRInformationDTO();
			cdrInformationDTO.setCommandId(CDRCommandID.RewardPointCaluculation.getId());
			cdrInformationDTO.setTransactionId(calculationDTO.getTransactionId());
			cdrInformationDTO.setStatusCode(genericDTO.getStatusCode());
			cdrInformationDTO.setStatusDescription(genericDTO.getStatus());
			cdrInformationDTO.setChannelID(calculationDTO.getChannel());
			cdrInformationDTO.setSubscriberNumber(calculationDTO.getSubscriberNumber());
			cdrInformationDTO.setLoyaltyId(calculationDTO.getLoyaltyID() + "");
			cdrInformationDTO.setTierPoints(calculationDTO.getRequestRewardPoints());

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
		}
		return cdrInformationDTO;
	}

	/*
	 * public boolean insertDayWiseEntry() { Date expiryDate = null; Calendar c
	 * =null; ConfigureParameterDTO configParmeterDTO = null;
	 * TierAndBonusCalculationDAO tierAndBonusCalculationDAO = null;
	 * TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO = null; String exp
	 * =null; try { c = Calendar.getInstance(); tierAndBonusCalculationDAO= new
	 * TierAndBonusCalculationDAO(); configParmeterDTO =
	 * Cache.configParameterMap.get("BONUS_POINTS_VALIDITY_DAYS");
	 * if(configParmeterDTO!=null && configParmeterDTO.getParameterType()!=null &&
	 * !configParmeterDTO.getParameterValue().equalsIgnoreCase(""))
	 * c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(configParmeterDTO.
	 * getParameterValue())); // number of days to add expiryDate = c.getTime(); exp
	 * = sdf.format(expiryDate); expiryDate = sdf.parse(exp);
	 * tierAndBonusPointDetailsDTO =
	 * tierAndBonusCalculationDAO.checkInTableFortierAndBonusPointDetails(expiryDate
	 * ,subscriberNumberTabDTO.getSubscriberNumber()+"",loyaltyProfileTabDTO.
	 * getLoyaltyID());
	 * 
	 * }catch(Exception e) {
	 * 
	 * } return false; }
	 */
}
