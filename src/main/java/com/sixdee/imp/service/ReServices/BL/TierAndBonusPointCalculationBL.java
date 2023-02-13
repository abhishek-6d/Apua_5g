package com.sixdee.imp.service.ReServices.BL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.arch.exception.CommonException;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.UserprofileDAO;
import com.sixdee.imp.dto.ConfigureParameterDTO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LmsOnmPointSnapshotDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.service.ReServices.DAO.TierAndBonusCalculationDAO;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.util.constant.SystemConstants;
import com.sixdee.lms.util.selections.CDRCommandID;


public class TierAndBonusPointCalculationBL implements BusinessLogics{
	private static final Logger logger = Logger.getLogger(TierAndBonusPointCalculationBL.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat dateFormatExpiry = new SimpleDateFormat(Cache.getConfigParameterMap().get("NOTIFICATION_DATE_FORMAT").getParameterValue());
	
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {		
		// TODO Auto-generated method stub
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		String requestId = null;
		RERequestDataSet dataSet = null;
		ArrayList<ReResponseParameter> paramlist = null;
		String msisdn = null;
		Double volume = 0.0;
		Double pointMultiplier = 0.0;
		boolean isTierCalculation=false;
		boolean isBonusCalculation=false;
		int bonusPercentage = 0;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		Double calculatedTierPoints =0.0;
		Double calculatedBonusPoints =0.0;
		ConfigureParameterDTO configParmeterDTO = null;
		Date expiryDate = null;
		String exp =null;
		String notificationExpiryDateFormat=null;
		String expiryQuarter =null;
		boolean flag = false;
		TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO = null;
		TierAndBonusCalculationDAO tierAndBonusCalculationDAO = null; 
		REResponseHeader response =null;
		Double preRewardPoints =0.0;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		RERequestHeader request = null;
		CDRInformationDTO cdrInformationDTO = null;
		String billedAmount = null;
		String paidAmount = null;
		boolean isPostpaid = false;
		boolean isonTime = false;
		String txnId = null;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		LmsOnmPointSnapshotDTO lmsOnmPointSnapshotDTO  = null;
		 TierInfoDTO infoDTO = null;
		 String expiryDays = null;
		 String channel = null;
		 String accountNumber=null;
		 List<CustomerProfileTabDTO> customerProfileTabDtoList=null;
		try{
			tierAndBonusCalculationDAO = new TierAndBonusCalculationDAO();
			request = (RERequestHeader) genericDTO.getObj();
			if(request!=null){
				requestId = request.getRequestId();
				msisdn = request.getMsisdn();
				dataSet = request.getDataSet();
				if(dataSet!=null){
					paramlist = dataSet.getResponseParam();
					for(ReResponseParameter p : paramlist){
						if (p.getId().equalsIgnoreCase("MSISDN"))
							msisdn = p.getValue();
						else if (p.getId().equalsIgnoreCase("VOLUME_TAG"))
							volume = Double.parseDouble(p.getValue());
						else if (p.getId().equalsIgnoreCase("refillAmount"))
							volume = Double.parseDouble(p.getValue());
						else if (p.getId().equalsIgnoreCase("POINT_MULTIPLIER"))
							pointMultiplier = Double.parseDouble(p.getValue());
						else if (p.getId().equalsIgnoreCase("IS_TIER_CALCULATION")) {
							if (p.getValue().equalsIgnoreCase("1"))
								isTierCalculation = true;
							else
								isTierCalculation = false;
						}
						else if (p.getId().equalsIgnoreCase("IS_BONUS_CALCULATION")) {
							if (p.getValue().equalsIgnoreCase("1"))
								isBonusCalculation = true;
							else
								isBonusCalculation = false;
						}
						else if (p.getId().equalsIgnoreCase("IS_POSTPAID")) {
							if (p.getValue().equalsIgnoreCase("1"))
								isPostpaid = true;
							else
								isPostpaid = false;
						}
						else if (p.getId().equalsIgnoreCase("Is_OnTime")) {
							if (p.getValue().equalsIgnoreCase("1"))
								isonTime = true;
							else
								isonTime = false;
						}
						else if(p.getId().equalsIgnoreCase("TIER_POINTS_CALCULATED"))
							calculatedTierPoints =Double.parseDouble(p.getValue());
						else if(p.getId().equalsIgnoreCase("BONUS_PERCENTAGE"))
							bonusPercentage =Integer.parseInt(p.getValue());
						else if(p.getId().equalsIgnoreCase("INSTANT_BILLED_AMOUNT"))
							billedAmount = p.getValue();
						else if(p.getId().equalsIgnoreCase("INSTANT_PAYMENT_AMOUNT"))
							paidAmount = p.getValue();
						else if(p.getId().equalsIgnoreCase("EXPIRY_DAYS"))
							expiryDays = p.getValue();
						else if(p.getId().equalsIgnoreCase("TXN_ID"))
							txnId = p.getValue();
						else if(p.getId().equalsIgnoreCase("CHANNEL_ID"))
							channel = p.getValue();
						else if(p.getId().equalsIgnoreCase("ACCOUNT_ID"))
							accountNumber = p.getValue();
					}
					//Added in case of postpaid accountNumber start
					if (accountNumber != null && !accountNumber.equalsIgnoreCase("")) {
						logger.info("TransactionId " + requestId + " Accumulation Request based on AccountNumber Postpaid");
						customerProfileTabDtoList = tableDetailsDAO.getCustomerProfileInfoBasedonAccountNumber(accountNumber, requestId);
						if (customerProfileTabDtoList != null && customerProfileTabDtoList.size()>0) {
							msisdn = customerProfileTabDtoList.get(0).getContactNumber();
						} else {
							logger.info("TransactionId " + requestId + " accountNumber "+ accountNumber +"not found in loyaltyProfile");
							throw new CommonException();
						}
					} else {
						logger.info("TransactionId " + requestId + " Accumulation Request based on Msisdn Prepaid");
					}
					//Added in case of postpaid accountNumber end
					
					if(billedAmount!=null && paidAmount!=null){
						double ba = Double.parseDouble(billedAmount);
						double pa = Double.parseDouble(paidAmount);
						if(pa>ba){
							volume = ba;
						}else 
							volume = pa;
					}/*else{
					logger.info(">>>>VOLUME_TAG>>>"+volTag);
					if(volTag!=null){
						for(ReResponseParameter p : paramlist){
							if(p.getId().equalsIgnoreCase(volTag)){
								volume = Double.parseDouble(p.getValue());
								break;
							}
						}
					  }
					
					}*/
					logger.info(">>>channel ::"+channel+" >>volume>>>"+volume+"  isOntime::"+ isonTime +"isPostpaid:: "+isPostpaid);
					
				}
				if (msisdn.length() > 10 && msisdn.startsWith("1")) {
					msisdn = msisdn.replaceFirst("1", "");
				}
					long start = System.currentTimeMillis();
					if(isPostpaid){
					subscriberNumberTabDTO = tableDetailsDAO.getSubscriberProfile(msisdn);	
					}else{
				   subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(msisdn));
					}
					if(isonTime){
						calculatedTierPoints = volume;
						pointMultiplier = 1.0;
					}
				logger.info(" get subscriber: --------------------------------->" + (System.currentTimeMillis() - start));
				
				loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
				loyaltyTransactionTabDTO.setReqTransactionID(requestId);
				loyaltyTransactionTabDTO.setSubscriberNumber(msisdn);
				synchronized (TierAndBonusPointCalculationBL.class) {
				
				if(subscriberNumberTabDTO!=null)
				{
					loyaltyTransactionTabDTO.setChannel("-1");
					loyaltyTransactionTabDTO.setSubscriberNumber(subscriberNumberTabDTO.getSubscriberNumber()+"");
					loyaltyTransactionTabDTO.setLoyaltyID(subscriberNumberTabDTO.getLoyaltyID());
					loyaltyTransactionTabDTO.setAccountNumber(subscriberNumberTabDTO.getAccountNumber());
					//loyaltyTransactionTabDTO.setStatusDesc("Points Added");
					
						logger.info(">>>loayalty id>>>>"+subscriberNumberTabDTO.getLoyaltyID());
					loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(subscriberNumberTabDTO.getLoyaltyID());
					if(loyaltyProfileTabDTO!=null){
						
						 customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(subscriberNumberTabDTO.getSubscriberNumber()+"");
					if(customerProfileTabDTO!=null){
						logger.info(">>>status of customer>>"+customerProfileTabDTO.getStatusId());
						if(customerProfileTabDTO.getStatusId()!=null && !((customerProfileTabDTO.getStatusId().equalsIgnoreCase("5")) || (customerProfileTabDTO.getStatusId().equalsIgnoreCase("8")))){
							
						preRewardPoints = loyaltyProfileTabDTO.getRewardPoints();
						if(isBonusCalculation){
							loyaltyTransactionTabDTO.setPointType(1);
							logger.info(">>bonus point calculation>>>");
							logger.info(">>calculated points in req>>>"+calculatedTierPoints);
							/*if(volume>0.0 && pointMultiplier>0.0)
								calculatedTierPoints = volume*pointMultiplier;*/
							if(calculatedTierPoints>0.0){
								if(bonusPercentage>0){
								calculatedBonusPoints = (calculatedTierPoints * bonusPercentage)/100;
								calculatedBonusPoints=(double)calculatedBonusPoints.intValue();
								loyaltyProfileTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints()+calculatedBonusPoints);
								loyaltyProfileTabDTO.setBonusPoints(loyaltyProfileTabDTO.getBonusPoints()+calculatedBonusPoints);
								Calendar c = Calendar.getInstance();
								if(expiryDays!=null){
									c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(expiryDays));  // number of days to add
								}else{
								configParmeterDTO = Cache.configParameterMap.get("BONUS_POINTS_VALIDITY_DAYS");
							        if(configParmeterDTO!=null && configParmeterDTO.getParameterType()!=null && !configParmeterDTO.getParameterValue().equalsIgnoreCase("")) 
							        	c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(configParmeterDTO.getParameterValue()));  // number of days to add
								}
								  expiryDate = c.getTime();
							       exp = sdf.format(expiryDate);
							       notificationExpiryDateFormat=dateFormatExpiry.format(expiryDate);
							       logger.info(">>>exp>>"+exp);
							       expiryDate = sdf.parse(exp);
							       expiryQuarter = ((c.get(Calendar.MONTH)/3)+1)+""+c.get(Calendar.YEAR)+"";
							       logger.info("EXPIRY date and quarter :: "+expiryDate+" :: "+expiryQuarter);		   
							       
								}
							}
							loyaltyTransactionTabDTO.setRewardPoints(calculatedBonusPoints);
						}
						else if(isTierCalculation){
							loyaltyTransactionTabDTO.setPointType(0);
							logger.info(">>tier point calculation>>>");
							if(volume>0.0 && pointMultiplier>0.0)
								calculatedTierPoints = volume*pointMultiplier;
							logger.info(">>>loyaltyProfileTabDTO.getRewardPoints()"+loyaltyProfileTabDTO.getRewardPoints()+"loyaltyProfileTabDTO.getTierPoints()>>>>"+loyaltyProfileTabDTO.getTierPoints());
							if(calculatedTierPoints>0.0){
								calculatedTierPoints=(double)calculatedTierPoints.intValue();
								loyaltyProfileTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints()+calculatedTierPoints);
								loyaltyProfileTabDTO.setTierPoints(loyaltyProfileTabDTO.getTierPoints()+calculatedTierPoints);
								Calendar c = Calendar.getInstance();
								if(expiryDays!=null){
									c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(expiryDays));  // number of days to add
								}else{
								    configParmeterDTO = Cache.configParameterMap.get("TIER_POINTS_VALIDITY_DAYS");
							        if(configParmeterDTO!=null && configParmeterDTO.getParameterType()!=null && !configParmeterDTO.getParameterValue().equalsIgnoreCase("")) 
							        	c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(configParmeterDTO.getParameterValue()));  // number of days to add
								}  
								  expiryDate = c.getTime();
							       exp = sdf.format(expiryDate);
							       notificationExpiryDateFormat=dateFormatExpiry.format(expiryDate);
							       logger.info(">>>exp>>"+exp);
							       logger.info(">>>exp>>"+exp);
							       expiryDate = sdf.parse(exp);
							       
							       expiryQuarter = ((c.get(Calendar.MONTH)/3)+1)+""+c.get(Calendar.YEAR)+"";
							    logger.info("EXPIRY date and quarter :: "+expiryDate+" :: "+expiryQuarter);		   
							       
							}
							loyaltyTransactionTabDTO.setRewardPoints(calculatedTierPoints);
						}
						loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
						//loyaltyTransactionTabDTO.setRewardPoints(calculatedBonusPoints+calculatedTierPoints);
						logger.info(">>>>>requestid : "+requestId+
								" txnid ::"+txnId+
								" loyaltyid : "+ loyaltyProfileTabDTO.getLoyaltyID() +
								" calculatedTierPoints : "+ calculatedTierPoints +
								" calculatedBonusPoints :"+calculatedBonusPoints +
								" rewardpoints : "+loyaltyProfileTabDTO.getRewardPoints()+
								" profile tierPoints :"+loyaltyProfileTabDTO.getTierPoints()+
								" profile bonusPoints :"+loyaltyProfileTabDTO.getBonusPoints());
						
						
					flag =	tierAndBonusCalculationDAO.updateLoyaltyProfile(loyaltyProfileTabDTO);
					if(flag){
						logger.info(">>>updated loyalty profile going to insert/update in day wise tier and bonus table>>>");
						
						tierAndBonusPointDetailsDTO = tierAndBonusCalculationDAO.checkInTableFortierAndBonusPointDetails(expiryDate,subscriberNumberTabDTO.getSubscriberNumber()+"",loyaltyProfileTabDTO.getLoyaltyID());
						
						if(tierAndBonusPointDetailsDTO!=null){
							if(isBonusCalculation){
								tierAndBonusPointDetailsDTO.setBonusCreateDate(new Date());
								tierAndBonusPointDetailsDTO.setBonusPoints(tierAndBonusPointDetailsDTO.getBonusPoints()+calculatedBonusPoints);
								tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()+tierAndBonusPointDetailsDTO.getBonusPoints());
							
							}else 	if(isTierCalculation){
								tierAndBonusPointDetailsDTO.setTierCreateDate(new Date());
								tierAndBonusPointDetailsDTO.setTierPoints(tierAndBonusPointDetailsDTO.getTierPoints()+calculatedTierPoints);
								tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()+tierAndBonusPointDetailsDTO.getBonusPoints());
							}
							
							tierAndBonusCalculationDAO.updateTierAndBonusDEtails(tierAndBonusPointDetailsDTO);
								
						}else{
							logger.info(">>>no entry in table inserting new entry>>>");
							tierAndBonusPointDetailsDTO = new TierAndBonusPointDetailsDTO();
							tierAndBonusPointDetailsDTO.setMsisdn(subscriberNumberTabDTO.getSubscriberNumber()+"");
							tierAndBonusPointDetailsDTO.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID()+"");
							if(isBonusCalculation){
								tierAndBonusPointDetailsDTO.setBonusCreateDate(new Date());
								tierAndBonusPointDetailsDTO.setBonusPoints(calculatedBonusPoints);
							}
							else if(isTierCalculation){
							  tierAndBonusPointDetailsDTO.setTierCreateDate(new Date());
							  tierAndBonusPointDetailsDTO.setTierPoints(calculatedTierPoints);
							}
							tierAndBonusPointDetailsDTO.setExpiryDate(expiryDate);
							tierAndBonusPointDetailsDTO.setExpiryQuarter(expiryQuarter);
							tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()+tierAndBonusPointDetailsDTO.getBonusPoints());
							tierAndBonusCalculationDAO.insetTierAndBonusDetails(tierAndBonusPointDetailsDTO);
						}
						
					}
					
					
					if(customerProfileTabDTO!=null){
						
						logger.debug(">>>account type>>"+customerProfileTabDTO.getAccountType());
						if(customerProfileTabDTO.getAccountCategoryType().equalsIgnoreCase("2")){
							lmsOnmPointSnapshotDTO = tableDetailsDAO.getLmsOnmPOintSnapshotDTO(loyaltyProfileTabDTO.getLoyaltyID());
							  if(lmsOnmPointSnapshotDTO!=null){
								  if(isTierCalculation)
									  lmsOnmPointSnapshotDTO.setClosingBalance(lmsOnmPointSnapshotDTO.getOpeningBalance()+calculatedTierPoints);
								  else
									  lmsOnmPointSnapshotDTO.setClosingBalance(lmsOnmPointSnapshotDTO.getOpeningBalance()+calculatedBonusPoints);
									 lmsOnmPointSnapshotDTO.setUpdateDate(new Date()); 
									 lmsOnmPointSnapshotDTO.setIsCdr("0");
								  tierAndBonusCalculationDAO.updatePointSnapshotDTO(lmsOnmPointSnapshotDTO);
							  }else{
								  lmsOnmPointSnapshotDTO = new LmsOnmPointSnapshotDTO();
								  lmsOnmPointSnapshotDTO.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID());
								  if(isTierCalculation)
									  lmsOnmPointSnapshotDTO.setClosingBalance(calculatedTierPoints);
								  else
									  lmsOnmPointSnapshotDTO.setClosingBalance(calculatedBonusPoints);
								  lmsOnmPointSnapshotDTO.setOpeningBalance(preRewardPoints);
								  lmsOnmPointSnapshotDTO.setCreateDate(new Date());
								  lmsOnmPointSnapshotDTO.setIsCdr("0");
								  tierAndBonusCalculationDAO.insertIntoPointSnapshotDTO(lmsOnmPointSnapshotDTO);
							  }
						}
					}
				    response = new REResponseHeader();
					response.setRequestId(requestId);
					response.setMsisdn(msisdn);
					response.setTimeStamp(request.getTimeStamp());
					ReResponseParameter p = null;
					 if(isBonusCalculation){
					     p = new ReResponseParameter();
						p.setId("BONUS_POINTS_CALCULATED");
						p.setValue(calculatedBonusPoints+"");
						paramlist.add(p);
					}else if(isTierCalculation){
					 p = new ReResponseParameter();
					p.setId("TIER_POINTS_CALCULATED");
					p.setValue(calculatedTierPoints+"");
					paramlist.add(p);
					}
					 p = new ReResponseParameter();
					p.setId("EXPIRY_DATE");
					p.setValue(expiryDate+"");
					paramlist.add(p);
					
					p = new ReResponseParameter();
					p.setId(SystemConstants.POINTS);
					if(isonTime)
						p.setValue(calculatedBonusPoints+"");
					else
					     p.setValue((calculatedBonusPoints+calculatedTierPoints)+"");
					paramlist.add(p);
					
					p = new ReResponseParameter();
					p.setId(SystemConstants.TO_EMAIL);
					p.setValue(loyaltyProfileTabDTO.getEmailID());
					paramlist.add(p);
					//dataSet.setResponseParam(paramlist);
					REResponseDataSet reResponseDataSet = new REResponseDataSet();
					//dataSet.getResponseParam().addAll(paramlist);
					reResponseDataSet.setParameterList(paramlist);
					response.setDataSet(reResponseDataSet);
					response.setStatus("SC0000");
					response.setStatusDesc("SUCCESS");
				    loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.rewardsPointsAdded);
					loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("TIER_AND_BONUS_POINT_ACCU_TXN_DES").getParameterValue());
					loyaltyTransactionTabDTO.setExpiryDate(expiryDate);
					tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
					
					
					}else{
						response = new REResponseHeader();
						response.setRequestId(requestId);
						response.setMsisdn(msisdn);
						response.setTimeStamp(request.getTimeStamp());
						
						 if(customerProfileTabDTO.getStatusId().equalsIgnoreCase("5")){
							  logger.info("##FAILURE...Customer is in soft delete>>>");
							  response.setStatus(Cache.getServiceStatusMap().get("CUSTOMER_IN_SOFT_DELETE_1").getStatusCode());
							  response.setStatusDesc(Cache.getServiceStatusMap().get("CUSTOMER_IN_SOFT_DELETE_1").getStatusDesc());
							  ReResponseParameter p = new ReResponseParameter(); 
								p = new ReResponseParameter();
								p.setId("ERROR_CODE");
								p.setValue("45");
								paramlist.add(p);	  
						 }else if(customerProfileTabDTO.getStatusId().equalsIgnoreCase("10")){
								    logger.info("##FAILURE...Customer is Blacklisted>>");
								    response.setStatus(Cache.getServiceStatusMap().get("CUSTOMER_IS_IN_FRAUD_1").getStatusCode());
								    response.setStatusDesc(Cache.getServiceStatusMap().get("CUSTOMER_IS_IN_FRAUD_1").getStatusCode());
								    ReResponseParameter p = new ReResponseParameter(); 
									p = new ReResponseParameter();
									p.setId("ERROR_CODE");
									p.setValue("02");
									paramlist.add(p);	 
						 }
						 
							
							REResponseDataSet reResponseDataSet = new REResponseDataSet();
							//dataSet.getResponseParam().addAll(paramlist);
							reResponseDataSet.setParameterList(paramlist);
							response.setDataSet(reResponseDataSet);
					}
				}else{
						response = new REResponseHeader();
						response.setRequestId(requestId);
						response.setMsisdn(msisdn);
						response.setTimeStamp(request.getTimeStamp());
						ReResponseParameter p = new ReResponseParameter(); 
						p = new ReResponseParameter();
						p.setId("ERROR_CODE");
						p.setValue("09");
						paramlist.add(p);
						
						REResponseDataSet reResponseDataSet = new REResponseDataSet();
						//dataSet.getResponseParam().addAll(paramlist);
						reResponseDataSet.setParameterList(paramlist);
						response.setDataSet(reResponseDataSet);
						response.setStatus("SC0001");
						response.setStatusDesc("INVALID MSISDN");
					   }
					}else{
						logger.info(">>no loyalty profile>>>");
						response = new REResponseHeader();
						response.setRequestId(requestId);
						response.setMsisdn(msisdn);
						response.setTimeStamp(request.getTimeStamp());
						ReResponseParameter p = new ReResponseParameter(); 
						p = new ReResponseParameter();
						p.setId("ERROR_CODE");
						p.setValue("09");
						paramlist.add(p);
						
						REResponseDataSet reResponseDataSet = new REResponseDataSet();
						//dataSet.getResponseParam().addAll(paramlist);
						reResponseDataSet.setParameterList(paramlist);
						response.setDataSet(reResponseDataSet);
						response.setStatus("SC0001");
						response.setStatusDesc("INVALID MSISDN");
						
					}
					
				 
				}else{
					logger.info("TransactionId "+requestId+" wrong msisdn not exist in loyaltySystem");
					response = new REResponseHeader();
					response.setRequestId(requestId);
					response.setMsisdn(msisdn);
					response.setTimeStamp(request.getTimeStamp());
					ReResponseParameter p = new ReResponseParameter(); 
					p = new ReResponseParameter();
					p.setId("ERROR_CODE");
					p.setValue("09");
					paramlist.add(p);
					
					REResponseDataSet reResponseDataSet = new REResponseDataSet();
					//dataSet.getResponseParam().addAll(paramlist);
					reResponseDataSet.setParameterList(paramlist);
					response.setDataSet(reResponseDataSet);
					response.setStatus("SC0001");
					response.setStatusDesc("INVALID MSISDN");
					
				}
			}
			}
			
		}catch(CommonException e)
		{
			logger.info("TransactionId "+requestId+" accountNumber not exist in loyaltyProfile");
			response = new REResponseHeader();
			response.setRequestId(requestId);
			response.setMsisdn(msisdn);
			response.setTimeStamp(request.getTimeStamp());
			ReResponseParameter p = new ReResponseParameter(); 
			p = new ReResponseParameter();
			p.setId("ERROR_CODE");
			p.setValue("10");
			paramlist.add(p);
			
			REResponseDataSet reResponseDataSet = new REResponseDataSet();
			//dataSet.getResponseParam().addAll(paramlist);
			reResponseDataSet.setParameterList(paramlist);
			response.setDataSet(reResponseDataSet);
			response.setStatus("SC0001");
			response.setStatusDesc("INVALID ACCOUNT_NUMBER");
		}
		catch(Exception e){
			e.printStackTrace();
			response = new REResponseHeader();
			response.setRequestId(requestId);
			response.setMsisdn(msisdn);
			response.setTimeStamp(request.getTimeStamp());
			 ReResponseParameter p = null;
			 if(isBonusCalculation){
			     p = new ReResponseParameter();
				p.setId("BONUS_POINTS_CALCULATED");
				p.setValue(calculatedBonusPoints+"");
				paramlist.add(p);
			} else if (isTierCalculation) {
				p = new ReResponseParameter();
				p.setId("TIER_POINTS_CALCULATED");
				p.setValue(calculatedTierPoints + "");
				paramlist.add(p);
			}
			 p = new ReResponseParameter();
			 p.setId("EXPIRY_DATE");
			 p.setValue(expiryDate+"");
			 paramlist.add(p);
			
			 	p = new ReResponseParameter();
				p.setId("ERROR_CODE");
				p.setValue("03");
				paramlist.add(p);
			
			REResponseDataSet reResponseDataSet = new REResponseDataSet();
			//dataSet.getResponseParam().addAll(paramlist);
			reResponseDataSet.setParameterList(paramlist);
			response.setDataSet(reResponseDataSet);
				response.setStatus("SC0001");
				response.setStatusDesc("FAILED");
				
			
		}
		finally{
			if(isPostpaid){
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
				String logFiles = null;
				if(loyaltyProfileTabDTO!=null)
				   infoDTO =Cache.getTierInfoMap().get(loyaltyProfileTabDTO.getTierId());
				if(loyaltyProfileTabDTO!=null && customerProfileTabDTO!=null && infoDTO!=null && subscriberNumberTabDTO!=null){
					UserprofileDAO userprofileDAO = new UserprofileDAO();
					Date StartDate	= userprofileDAO.getFirstDayOfQuarter(new Date());
	                Date endDate  =  userprofileDAO.getLastdayOfQuarter(new Date());
	                logger.info(">>>start date >>>"+StartDate +"  >>endDate>>"+endDate);
	                Double expiryPoints = userprofileDAO.getPointsToBeExpired(StartDate,endDate,loyaltyProfileTabDTO.getLoyaltyID(),subscriberNumberTabDTO.getSubscriberNumber());
							 logFiles = loyaltyProfileTabDTO.getLoyaltyID()+","+customerProfileTabDTO.getAccountNo()+","+
						customerProfileTabDTO.getCustomerRefNumber()+","+lmsOnmPointSnapshotDTO.getOpeningBalance()+","+
									lmsOnmPointSnapshotDTO.getClosingBalance()+","+df.format(loyaltyProfileTabDTO.getCreateTime())+","+
									expiryPoints+","+df.format(endDate)+""+","+infoDTO.getTierName()+","+df.format(new Date());
				 CDRLoggerUtil.flushFatalCDR(org.apache.logging.log4j.LogManager.getLogger("BillingCDR"),logFiles);
				 lmsOnmPointSnapshotDTO.setOpeningBalance(lmsOnmPointSnapshotDTO.getClosingBalance());
				 lmsOnmPointSnapshotDTO.setClosingBalance(0);
				 lmsOnmPointSnapshotDTO.setUpdateDate(new Date()); 
				 lmsOnmPointSnapshotDTO.setIsCdr("1");
				  tierAndBonusCalculationDAO.updatePointSnapshotDTO(lmsOnmPointSnapshotDTO);
				}
			}
			
			 cdrInformationDTO=new CDRInformationDTO();
		        
		        cdrInformationDTO.setTransactionId(requestId);
		        //cdrInformationDTO.setTimeStamp(timeStamp);
		        cdrInformationDTO.setCommandId(CDRCommandID.PointAccumulation.getId());
		        cdrInformationDTO.setChannelID(channel);
		        if(loyaltyProfileTabDTO!=null){
		        	 cdrInformationDTO.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID()+"");
		        	 if(isBonusCalculation)
		        		 cdrInformationDTO.setBonusPoints(calculatedBonusPoints);
		        	 else if(isTierCalculation)	 
		        	      cdrInformationDTO.setTierPoints(calculatedTierPoints);
		        	 
				        
				        //cdrInformationDTO.setPreviousTier(loyaltyProfileTabDTO.get);
				        cdrInformationDTO.setCurrentTier(loyaltyProfileTabDTO.getTierId());
				        cdrInformationDTO.setAccountNumber(subscriberNumberTabDTO.getAccountNumber()+"");
				        cdrInformationDTO.setCurrentBalance(loyaltyProfileTabDTO.getRewardPoints()+"");
					    cdrInformationDTO.setOldBalance(preRewardPoints+"");
		        }
		       
		        if(subscriberNumberTabDTO!=null && subscriberNumberTabDTO.getSubscriberNumber()!=null)
		        {
		        cdrInformationDTO.setSubscriberNumber(subscriberNumberTabDTO.getSubscriberNumber()+"");
		        }
		        if(customerProfileTabDTO!=null)
		        cdrInformationDTO.setSubscriberType(customerProfileTabDTO.getAccountType());
		        cdrInformationDTO.setStatusCode(response.getStatus());
		        cdrInformationDTO.setStatusDescription(response.getStatusDesc());
		        cdrInformationDTO.setField1(volume+"");
			logger.warn(cdrInformationDTO.toString());
			CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
			//logger.warn(logFile);
			//SendingNotification 
			if(response!=null && response.getStatus()!=null && response.getStatus().equalsIgnoreCase("SC0000"))
			{
				pointAccumulatedNotification(loyaltyProfileTabDTO,msisdn,calculatedTierPoints+"",requestId,notificationExpiryDateFormat);
			}
			//
			genericDTO.setObj(response);
			tableDetailsDAO = null;
			requestId = null;
			dataSet = null;
			paramlist = null;
			msisdn = null;
			volume = 0.0;
			pointMultiplier = 0.0;
			isTierCalculation = false;
			isBonusCalculation = false;
			bonusPercentage = 0;
			subscriberNumberTabDTO = null;
			loyaltyProfileTabDTO = null;
			calculatedTierPoints = 0.0;
			calculatedBonusPoints = 0.0;
			configParmeterDTO = null;
			expiryDate = null;
			exp = null;
			notificationExpiryDateFormat = null;
			expiryQuarter = null;
			flag = false;
			tierAndBonusPointDetailsDTO = null;
			tierAndBonusCalculationDAO = null;
			response = null;
			preRewardPoints = 0.0;
			loyaltyTransactionTabDTO = null;
			request = null;
			cdrInformationDTO = null;
			billedAmount = null;
			paidAmount = null;
			isPostpaid = false;
			isonTime = false;
			txnId = null;
			customerProfileTabDTO = null;
			lmsOnmPointSnapshotDTO = null;
			infoDTO = null;
			expiryDays = null;
			channel = null;
			accountNumber = null;
			customerProfileTabDtoList = null;
			tableDetailsDAO = null;
		}
		return genericDTO;
	}
	
	public void pointAccumulatedNotification(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String msisdn,String calculatedTierPoints ,String transactionId,String expiryDate)
	{
		CommonUtil commonUtil = null;
		try {
			commonUtil = new CommonUtil();
			HashMap<String, String> map = new HashMap<>();
			map.put(SystemConstants.MSISDN,msisdn);
			map.put(SystemConstants.MSG_CAUSE, SystemConstants.pointAccumulateSuccess);
			map.put(SystemConstants.totalRewardPoints,(loyaltyProfileTabDTO.getRewardPoints()+"").replaceAll(".0*$",""));
			map.put(SystemConstants.calculatedPoints,calculatedTierPoints.replaceAll(".0*$",""));
			map.put(SystemConstants.pointCaluclationExpiryDate,expiryDate);
			commonUtil.generateNotifyRequest(transactionId, "NotifyCustomer",msisdn, map);
		} catch (Exception e) {
			logger.info("TransactionId "+msisdn+" Exception " + e.getMessage());
		}
		finally
		{
			commonUtil=null;
		}
	}
	
}
