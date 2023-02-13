package com.sixdee.imp.bo;

/**
 * 
 * @author Rahul K K
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
 * <td>May 06,2013 06:00:07 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.GetTransactionDetailsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.ResponseTransactionHistoryDTO;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.util.GeneralProcesses;
import com.sixdee.lms.bo.BusinessCustomerProfileUtil;
import com.sixdee.lms.util.constant.SystemConstants;

public class GetTransactionDetailsBO extends BOCommon {

	/**
	 * This method is called from BOCommon. Access the DAO object is to create
	 * CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */

	private static final Logger logger = Logger.getLogger(GetTransactionDetailsBO.class);

	@Override
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => GetTransactionDetailsBL :: Method => buildProcess()");
		ArrayList<ResponseTransactionHistoryDTO> respHistory = null;
		GetTransactionDetailsDTO getTransactionDetailsDTO = null;
		
		boolean isValidTrans = false;
		try {
			getTransactionDetailsDTO = (GetTransactionDetailsDTO) genericDTO.getObj();
			if (genericDTO.getStatusCode().equals("SC0000")) {
				isValidTrans = true;
			}
			logger.info(">>sub num in bo>>" + getTransactionDetailsDTO.getSubscriberNumber());
			getTransactionDetailsDTO = getTransactionDetails(getTransactionDetailsDTO, isValidTrans);
			// getTransactionDetailsDTO.setObj(respHistory);
			// getTransactionDetailsDTO.setRowCount(20);
			genericDTO.setObj(getTransactionDetailsDTO);
			//genericDTO.setStatusCode("SC0000");
		} catch (CommonException e) {
			logger.error(e.getMessage());
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured ", e);
			if (genericDTO.getStatus().equals("SC0000")) {
				genericDTO.setStatusCode("SC1000");
			} else {
				throw new CommonException(e.getMessage());
			}
			// genericDTO.setStatus("System Error");
			throw new CommonException();
		} finally {
			getTransactionDetailsDTO = null;
		}

		return genericDTO;
	}

	private GetTransactionDetailsDTO getTransactionDetails(GetTransactionDetailsDTO getTransactionDetailsDTO,
			boolean isValidTrans) throws CommonException {
		ArrayList<ResponseTransactionHistoryDTO> respHistory = null;
		ArrayList<LoyaltyTransactionTabDTO> loyaltyTransactionsList = null;
		GetTransactionDetailsDAO getTransactionDetailsDAO = null;
		GeneralProcesses generalProcess = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		TableInfoDAO tableInfoDAO = null;
		TableDetailsDAO tableDetailsDAO = null;
		String loyaltyId = null;
		String subscriber = null;
		String tableName = null;
		String fromDate = null;
		String endDate = null;
		String statusId = null;
		int noOfRows = 0;
		String langId = null;
		String type = "0";
		ServiceStatusDTO actionServiceDetailsDTO = null;
		ArrayList<Integer> statusIds = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO=new SubscriberNumberTabDTO();
		CustomerProfileTabDTO customerProfileTabDTO = null;
		BusinessCustomerProfileUtil businessCustomerProfileUtil = new BusinessCustomerProfileUtil();
		try {
			logger.info(">>sub num in method>>" + getTransactionDetailsDTO.getSubscriberNumber());
			generalProcess = new GeneralProcesses();
			getTransactionDetailsDAO = new GetTransactionDetailsDAO();
			subscriber = getTransactionDetailsDTO.getSubscriberNumber();
			tableInfoDAO = new TableInfoDAO();
			tableDetailsDAO=new TableDetailsDAO();
			langId = getTransactionDetailsDTO.getLangId();
			/*
			 * // tableName = generalProcess.identifyTable( //
			 * Cache.cacheMap.get("SUBS_LOYALTY_TABLE_PREFIX"),subscriber);
			 * 
			 * if (getTransactionDetailsDTO.isAdsl()) { tableName =
			 * tableInfoDAO.getADSLNumberTable(subscriber); } else { tableName =
			 * tableInfoDAO.getSubscriberNumberTable(subscriber); } loyaltyId =
			 * getTransactionDetailsDAO.getLoyaltyId(tableName, subscriber);
			 * 
			 * loyaltyId = loyaltyId != null ? loyaltyId : subscriber;
			 * 
			 * logger.info("Subscriber [" + subscriber + "]  Loyalty Id [" + loyaltyId +
			 * "] Channel [" + getTransactionDetailsDTO.getChannel() + "] pin [" +
			 * getTransactionDetailsDTO.getPin() + "]");
			 * 
			 * 
			 * 2 cases here 1. if Channel = sms/ussd/ivr last n noOf Transaction 2. If
			 * Channel = wap 3 Subcases can come 2.1 if fromDate and endDate not equals to
			 * null get the n transactions from offset k 2.2 if 2.1 is false and lastNMonths
			 * is true then get the n transactions from offset k 2.3 if 2.1 and 2.2 is false
			 * then get last n transactions from offset k and limit n
			 * 
			 * // tableInfoDAO = new TableInfoDAO();
			 * 
			 * tableDetailsDAO = new TableDetailsDAO(); loyaltyProfileTabDTO =
			 * tableDetailsDAO.getLoyaltyProfile(Long.parseLong(loyaltyId));
			 * customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(subscriber); if
			 * (customerProfileTabDTO == null) { throw new CommonException(
			 * Cache.getServiceStatusMap().get("GET_ORDER_NO_LOYALTY_ID_" +
			 * langId).getStatusDesc());
			 * 
			 * } else { String result = businessCustomerProfileUtil
			 * .checkTelecomAdmin(getTransactionDetailsDTO.getTransactionId(), subscriber);
			 * logger.info(">>request id>>" + getTransactionDetailsDTO.getTransactionId() +
			 * " result::" + result); if (result != null) if
			 * (result.equalsIgnoreCase(SystemConstants.NO_ACCOUNT_FOUND)) {
			 * getTransactionDetailsDTO.setStatusCode(Cache.getServiceStatusMap()
			 * .get("NO_LOYALTY_ID_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusCode());
			 * getTransactionDetailsDTO.setStatusDesc(Cache.getServiceStatusMap()
			 * .get("NO_LOYALTY_ID_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); throw new
			 * CommonException(Cache.getServiceStatusMap() .get("NO_LOYALTY_ID_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); } else if
			 * (!(result.equalsIgnoreCase(SystemConstants.TELCO_ADMIN) ||
			 * result.equalsIgnoreCase(SystemConstants.NOT_BUSINESS_CUSTOMER))) {
			 * getTransactionDetailsDTO.setStatusCode(Cache.getServiceStatusMap()
			 * .get("NOT_TELCO_ADMIN_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusCode());
			 * getTransactionDetailsDTO.setStatusDesc(Cache.getServiceStatusMap()
			 * .get("NOT_TELCO_ADMIN_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); throw new
			 * CommonException(Cache.getServiceStatusMap() .get("NOT_TELCO_ADMIN_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); } if
			 * (customerProfileTabDTO.getStatusId() != null &&
			 * customerProfileTabDTO.getStatusId().equalsIgnoreCase("5")) {
			 * 
			 * getTransactionDetailsDTO.setStatusCode(Cache.getServiceStatusMap()
			 * .get("CUSTOMER_IN_SOFT_DELETE_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusCode());
			 * getTransactionDetailsDTO.setStatusDesc(Cache.getServiceStatusMap()
			 * .get("CUSTOMER_IN_SOFT_DELETE_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); throw new
			 * CommonException(Cache.getServiceStatusMap() .get("CUSTOMER_IN_SOFT_DELETE_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); } else if
			 * (customerProfileTabDTO.getStatusId() != null &&
			 * customerProfileTabDTO.getStatusId().equalsIgnoreCase("10")) {
			 * 
			 * getTransactionDetailsDTO.setStatusCode(Cache.getServiceStatusMap()
			 * .get("CUSTOMER_IS_IN_FRAUD_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusCode());
			 * getTransactionDetailsDTO.setStatusDesc(Cache.getServiceStatusMap()
			 * .get("CUSTOMER_IS_IN_FRAUD_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); throw new
			 * CommonException(Cache.getServiceStatusMap() .get("CUSTOMER_IS_IN_FRAUD_" +
			 * getTransactionDetailsDTO.getLangId()).getStatusDesc()); } }
			 * 
			 * logger.debug("Loyalty Language [" + langId + "] for Loyalty [" +
			 * loyaltyProfileTabDTO.getLoyaltyID() + "]");
			 * 
			 * if (!isValidTrans) { throw new CommonException(
			 * Cache.getServiceStatusMap().get("GET_TRANS_PARAM_MISS_" +
			 * langId).getStatusDesc());
			 * 
			 * } if (getTransactionDetailsDTO.getChannel().equalsIgnoreCase("sms")) {
			 * 
			 * // loyaltyProfileTabDTO = //
			 * tableDetailsDAO.getLoyaltyProfile(Long.parseLong(loyaltyId));
			 * 
			 * if (!loyaltyProfileTabDTO.getPin().equals(getTransactionDetailsDTO.getPin() +
			 * "")) {
			 * 
			 * throw new CommonException(
			 * Cache.getServiceStatusMap().get("GET_TRANS_PIN_FAILED_" +
			 * langId).getStatusDesc()); } }
			 */
//---------------------------------------------------------------------------------------------
			fromDate = getTransactionDetailsDTO.getFromDate();
			endDate = getTransactionDetailsDTO.getEndDate();
			statusId = getTransactionDetailsDTO.getStatusId();
			
			loyaltyId=getTransactionDetailsDTO.getLoyaltyId();
			if(loyaltyId==null) {
				subscriberNumberTabDTO=tableDetailsDAO.getSubscriberNumberDetails(Long.valueOf(getTransactionDetailsDTO.getSubscriberNumber()),null);
				if(subscriberNumberTabDTO!=null) {
					loyaltyId=String.valueOf(subscriberNumberTabDTO.getLoyaltyID());
				}
				else {
					actionServiceDetailsDTO = Cache.getServiceStatusMap().get("NO_LOYALTY DETAILS__1");
					getTransactionDetailsDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
					getTransactionDetailsDTO.setStatusDesc(actionServiceDetailsDTO.getStatusDesc());
				}
				
			}
			
			
			
			logger.info("...............statusId......................" + statusId + "..............channel........"
					+ getTransactionDetailsDTO.getChannel()+"...........lunguage......."+langId);

			if (statusId != null && !(statusId.equalsIgnoreCase("")) && !(statusId.equalsIgnoreCase("0"))) {
				logger.info(">>>status id>>>" + statusId);

				statusIds = getTransactionDetailsDAO.getStatusIds(statusId, getTransactionDetailsDTO.getChannel());
			}

			tableName = tableInfoDAO.getLoyaltyTransactionTable(loyaltyId);

			// getTransactionDetailsDAO.getCountOfTrans(tableName,loyaltyId);

			if ((fromDate != null && !(fromDate.trim().equals("")))
					&& (endDate != null && !(endDate.trim().equals("")))) {
				logger.info("From Date [" + fromDate + "] and End Date " + endDate
						+ " is not null . so going to find transaciton with in that period ["
						+ getTransactionDetailsDTO.getTransactionId() + "]");
				noOfRows = getTransactionDetailsDAO.countTransactions(tableName, loyaltyId, fromDate, endDate,
						statusIds);
				loyaltyTransactionsList = getTransactionDetailsDAO.getTransactions(tableName, loyaltyId, fromDate,
						endDate, getTransactionDetailsDTO.getOffset(), getTransactionDetailsDTO.getLimit(), statusIds);
			} else if (getTransactionDetailsDTO.getNoOfMonths() != 0) {
				logger.info("Retriving transactions for last  " + getTransactionDetailsDTO.getNoOfMonths() + "]");
				Date previousDate = getPreviousMonth(getTransactionDetailsDTO.getNoOfMonths());
				noOfRows = getTransactionDetailsDAO.countTransactions(tableName, loyaltyId, previousDate, statusIds);

				loyaltyTransactionsList = getTransactionDetailsDAO.getTransactions(tableName, loyaltyId, previousDate,
						getTransactionDetailsDTO.getOffset(), getTransactionDetailsDTO.getLimit(), statusIds);
			} else if (getTransactionDetailsDTO.getNoOfLastTransactions() != 0) {

				loyaltyTransactionsList = getTransactionDetailsDAO.getLastNTransactions(tableName, loyaltyId,
						getTransactionDetailsDTO.getNoOfLastTransactions() != 0
								? getTransactionDetailsDTO.getNoOfLastTransactions()
								: 1,
						getTransactionDetailsDTO.getOffset(), statusIds);
				noOfRows = loyaltyTransactionsList != null ? loyaltyTransactionsList.size() : 0;

				logger.info("Getting transactions for LoyaltyID [" + loyaltyId + "] and List Size is"
						+ loyaltyTransactionsList.size());

			}
			if (loyaltyTransactionsList != null && (loyaltyTransactionsList.size() != 0)) {
				respHistory = new ArrayList<ResponseTransactionHistoryDTO>();
				ResponseTransactionHistoryDTO respTransHistory = null;
				for (LoyaltyTransactionTabDTO loyaltyTransactions : loyaltyTransactionsList) {

					respTransHistory = new ResponseTransactionHistoryDTO();
					respTransHistory.setDate(sdf.format(loyaltyTransactions.getCreateTime()));
					// logger.debug("Create Date ["+loyaltyTransactions.getCreateDate()+"]");
					if (loyaltyTransactions.getSubscriberNumber() != null) {
						respTransHistory.setSubscriberNumber(loyaltyTransactions.getSubscriberNumber() + "");
					} else {
						respTransHistory.setSubscriberNumber("" + loyaltyTransactions.getLoyaltyID());
					}
					if (loyaltyTransactions.getExpiryDate() != null)
						respTransHistory.setExpiryDate(String.valueOf(loyaltyTransactions.getExpiryDate()));
					if (loyaltyTransactions.getExpiryPoints() != null)
						respTransHistory.setExpiryPoints(
								String.valueOf(loyaltyTransactions.getExpiryPoints()).replaceAll(".0*$", ""));
					logger.info("TxnId " + getTransactionDetailsDTO.getTransactionId() + " table reward points "
							+ loyaltyTransactions.getRewardPoints());
					if (loyaltyTransactions.getStatusID() == 8) {

						logger.info("TXN CHANNEL:" + getTransactionDetailsDTO.getChannel());

						if (getTransactionDetailsDTO.getChannel().equalsIgnoreCase("USSD")
								|| getTransactionDetailsDTO.getChannel().equalsIgnoreCase("SMS")) {
							String value = getTransactionDetailsDAO.getPackageName(loyaltyTransactions.getPackageId(),
									langId);
							String[] datas = value.split(":");
							String status = Cache.loyaltyStatusMap
									.get(loyaltyTransactions.getStatusID() + "_" + langId);
							// respTransHistory.setActivity(status + " " + datas[0]);
							respTransHistory.setActivity(loyaltyTransactions.getStatusDesc());
							if (loyaltyTransactions.getPointType() == 0)
								type = Cache.getConfigParameterMap().get("TXN_STATUS8_USSD_SMS_POINT_TYPE_0")
										.getParameterValue();
							else if (loyaltyTransactions.getPointType() == 1)
								type = Cache.getConfigParameterMap().get("TXN_STATUS8_USSD_SMS_POINT_TYPE_1")
										.getParameterValue();
							// respTransHistory.setType(status+"-"+type);
							respTransHistory.setType(type + "-" + loyaltyTransactions.getStatusDesc());

							if (datas.length > 1 && datas[1] != null && !datas[1].equalsIgnoreCase("")) {
								respTransHistory.setRewardPoint(Double.parseDouble(datas[1]));
							} else {
								respTransHistory.setRewardPoint(loyaltyTransactions.getRewardPoints());
							}
						} else {
							logger.info("IN WEB DESC:" + getTransactionDetailsDTO.getChannel());
							String value = getTransactionDetailsDAO
									.getPackageNameDetails(loyaltyTransactions.getPackageId(), langId);

							String status = Cache.loyaltyStatusMap
									.get(loyaltyTransactions.getStatusID() + "_" + langId);
							// respTransHistory.setActivity(status + " " + datas[1]+ "
							// "+datas[0]+(loyaltyTransactions.getArea()!=null?("-"+loyaltyTransactions.getArea()):"")+(loyaltyTransactions.getLocation()!=null?",":"")+(loyaltyTransactions.getLocation()!=null?loyaltyTransactions.getLocation():""));
							respTransHistory.setActivity(loyaltyTransactions.getStatusDesc());
							if (loyaltyTransactions.getPointType() == 0)
								type = Cache.getConfigParameterMap().get("TXN_STATUS8_POINT_TYPE_0")
										.getParameterValue();
							else if (loyaltyTransactions.getPointType() == 1)
								type = Cache.getConfigParameterMap().get("TXN_STATUS8_POINT_TYPE_1")
										.getParameterValue();
							// respTransHistory.setType(status+"-"+type);
							respTransHistory.setType(type + "-" + loyaltyTransactions.getStatusDesc());
							respTransHistory.setRewardPoint(loyaltyTransactions.getRewardPoints());

						}
					} else {
						/*
						 * respTransHistory.setActivity(Cache.loyaltyStatusMap
						 * .get(loyaltyTransactions.getStatusID() + "_" + langId));
						 */
						respTransHistory.setActivity(loyaltyTransactions.getStatusDesc());
						if (loyaltyTransactions.getPointType() == 0)
							type = Cache.getConfigParameterMap().get("TXN_STATUS_POINT_TYPE_0").getParameterValue();
						else if (loyaltyTransactions.getPointType() == 1)
							type = Cache.getConfigParameterMap().get("TXN_STATUS_POINT_TYPE_1").getParameterValue();
						respTransHistory
								.setType(Cache.loyaltyStatusMap.get(loyaltyTransactions.getStatusID() + "_" + langId)
										+ "-" + type);
						respTransHistory.setRewardPoint(loyaltyTransactions.getRewardPoints().intValue());
					}
					logger.info("Rewards Point [" + respTransHistory.getRewardPoint() + "]");
					respTransHistory.setPreviousRewardPoints(loyaltyTransactions.getPreRewardPoints().intValue());
					respTransHistory.setPreviousStatusPoints(loyaltyTransactions.getCurStatusPoints().intValue());
					respTransHistory.setStatusPoint(loyaltyTransactions.getStatusPoints().intValue());
					respTransHistory.setCurrentRewardPoints(loyaltyTransactions.getCurRewardPoints().intValue());
					respTransHistory.setCurrentStatusPoints(loyaltyTransactions.getCurStatusPoints().intValue());

					// logger.debug("status points "+respTransHistory.getStatusPoint()+"]");
					respHistory.add(respTransHistory);
					getTransactionDetailsDTO.setRowCount(noOfRows);
				}
				logger.info(">>type>>" + respTransHistory.getType());
				actionServiceDetailsDTO = Cache.getServiceStatusMap().get("GET_TRANS_LIST_1");
				getTransactionDetailsDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
				getTransactionDetailsDTO.setStatusDesc(actionServiceDetailsDTO.getStatusDesc());
				
			} else {
				logger.info("No Transaction Found for User");
				actionServiceDetailsDTO = Cache.getServiceStatusMap().get("GET_TRANS_NO_TRANS_1");
				getTransactionDetailsDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
				getTransactionDetailsDTO.setStatusDesc(actionServiceDetailsDTO.getStatusDesc());
				
			}

			
		} /*
			 * catch (Exception e) { logger.error("Exception occured ",e); throw new
			 * CommonException("SC0004"); }
			 */catch (ParseException e) {
			logger.error("Parsing Exception", e);

			if (e.getMessage() == null) {
				CommonException c = new CommonException();

				
			}
			
			
		} finally {
			getTransactionDetailsDTO.setObj(respHistory);
		}
		return getTransactionDetailsDTO;
	}

	private Date getPreviousMonth(int noOfMonths) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -noOfMonths);
		return calendar.getTime();
	}

	public static void main(String[] args) {
		Date d = new Date();
		System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(d));
	}

}
