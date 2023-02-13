/**
 * 
 */
package com.sixdee.lms.bo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sixdee.arch.exception.CommonException;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.EncrptPassword;
import com.sixdee.imp.common.util.LoyaltyRandomNumber;
import com.sixdee.imp.dao.CommonUtilDAO;
import com.sixdee.imp.dao.ServiceManagementDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.service.ReServices.BL.TierAndBonusPointCalculationBL;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.imp.util.CommonServiceConstants;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.util.CollectionsControllerUtil;
import com.sixdee.lms.util.LoyaltyAccountCreationUtil;
import com.sixdee.lms.util.RandomCreditCardNumberGenerator;
import com.sixdee.lms.util.ResponseSetter;
import com.sixdee.lms.util.constant.SystemConstants;
import com.sixdee.lms.util.selections.CDRCommandID;

/**
 * @author rahul.kr
 *
 */
public class GenerateLoyaltyAccountBO implements BusinessLogics{

	private static final Logger logger = LogManager.getLogger("GenerateLoyaltyAccountBO");
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		RERequestHeader reRequestHeader = null;
		REResponseHeader reResponseHeader = null;
		REResponseDataSet reResponseDataSet = null;
		ArrayList<ReResponseParameter> reResponseParameters = null;
		String requestId = null;
		try{
			reResponseHeader = new REResponseHeader();
			reRequestHeader = (RERequestHeader) genericDTO.getObj();
			requestId = reRequestHeader.getRequestId();
			reResponseHeader = generateLoyaltyAccount(requestId,reRequestHeader.getDataSet(),reResponseHeader,reRequestHeader);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("RequestId "+requestId+" Exception occuers ",e);
			reResponseHeader.setStatus("SC0002");
			reResponseHeader.setStatusDesc("Failed to create  account");
		}
		finally{
			reResponseDataSet = new REResponseDataSet();
			reResponseParameters = new ArrayList<ReResponseParameter>();
			reResponseParameters.addAll(reRequestHeader.getDataSet().getResponseParam());
			reResponseParameters.removeIf((ReResponseParameter param) -> param.getId().equalsIgnoreCase(SystemConstants.RESP_CODE));
			reResponseParameters.removeIf((ReResponseParameter param) -> param.getId().equalsIgnoreCase(SystemConstants.RESP_MESSAGE));
			reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_CODE,reResponseHeader.getStatus()));
			reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_MESSAGE,reResponseHeader.getStatusDesc()));
			if(reResponseHeader.getStatus()!=null && reResponseHeader.getStatus().equalsIgnoreCase("SC0002")){
				reResponseParameters.add(new ReResponseParameter("ERROR_CODE","01"));
			}
			reResponseDataSet.setParameterList(reResponseParameters);
			reResponseHeader.setDataSet(reResponseDataSet);
			reRequestHeader = null;
			genericDTO.setObj(reResponseHeader);
			
			
		}
		return genericDTO;
	}
	
	
	private REResponseHeader generateLoyaltyAccount(String requestId, RERequestDataSet dataSet,REResponseHeader reResponseHeader,RERequestHeader reRequestHeader) throws Exception {
		HashMap<String,String> customerDetails = null;
		CollectionsControllerUtil collectionsControllerUtil = null;
		String loyaltyId = null;
		ResponseSetter responseSetter=null;
		String key=null;
		try{
			/*
			 * Fetch CustomerProfile & compare values. If discrepancy update the same
			 */
			responseSetter=new ResponseSetter();
			collectionsControllerUtil = new CollectionsControllerUtil();
			customerDetails = collectionsControllerUtil.convertListToMap(dataSet.getResponseParam());
			responseSetter.setLanguageId("1");
			responseSetter.setTransactionId(requestId);
			
			/*subscriberInfoList=alreadyRegisteredValidation(requestId,customerDetails.get(SystemConstants.MSISDN).trim());
			
			if(subscriberInfoList!=null && subscriberInfoList.size()>0)
			{
				key="CUSTOMER_ALREADY_REGISTERED_";
				responseSetter.customeValidation(key, responseSetter);
				throw new CommonException();
			}*/
			
			if(customerDetails!=null && customerDetails.containsKey(SystemConstants.ACCOUNT_CATEGORY_TYPE) && customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE)!=null)
			{
				if(customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE).equalsIgnoreCase(SystemConstants.postpaidCustomerType))
						{
				if(customerDetails.containsKey(SystemConstants.isParent.toUpperCase()))
				{
					
					if(customerDetails.get(SystemConstants.isParent.toUpperCase())!=null && customerDetails.get(SystemConstants.isParent.toUpperCase()).trim().equalsIgnoreCase("1"))
					{
						if(customerDetails.get(SystemConstants.serviceIdStatus.toUpperCase())!=null && customerDetails.get(SystemConstants.serviceIdStatus.toUpperCase()).trim().equalsIgnoreCase("1"))
						{
							logger.info("Customer is eligible to register in Loyalty");
						}
						else
						{
							key="CUSTOMER_NOT_ACTIVE_";
							responseSetter = responseSetter.customeValidation(key, responseSetter);
							throw new CommonException();	
						}

						} else {
							key="CUSTOMER_NOT_PARENT_";
							responseSetter = responseSetter.customeValidation(key, responseSetter);
							throw new CommonException();
						}
					} else {
						key="CUSTOMER_PARENT_DETAILS_MISSING_";
						responseSetter = responseSetter.customeValidation(key, responseSetter);
						throw new CommonException();
					}
				} else {
					logger.info("Transaction id " + requestId + " Prepaid account registration");
				}
			} else {
				key = "CONNECTION_TYPE_INFO_NOT_CAME_BILLING_SYSTEM_";
				responseSetter = responseSetter.customeValidation(key, responseSetter);
				throw new CommonException();
			}

			/*
			if(customerDetails.get(SystemConstants.A_PARTY_MSISDN)!=null){
				customerProfileList = queryUtil.getCustomerProfileDetails
						(requestId, customerDetails.get(SystemConstants.A_PARTY_MSISDN), CustomerProfileQueryType.Msisdn);
				logger.info(">>>>>>>>>>>>>>>>MSISDN in main "+customerProfileList.get(0).getAccountNo()+">>> Input"+customerDetails.get(SystemConstants.ACCOUNT_NUM));
				if(!customerProfileList.get(0).getAccountNo().equalsIgnoreCase(customerDetails.get(SystemConstants.ACCOUNT_NUM))){
					//not same national ID,Account Creation Failed
					statusCode="SC0005";
					throw new CommonException("National ID is different");
				}
			}*/
				
			Request request = null;
			String tierId = "1"; //Default value 1 
			logger.debug("Customer Details Map "+customerDetails);
			
			//Commenting code as not required for Apua
			
			
			/*if(customerDetails.get(SystemConstants.ACCOUNT_TYPE).equals(SystemConstants.PREPAID)){
				customerProfileList = queryUtil.getCustomerProfileDetails
					(requestId, customerDetails.get(SystemConstants.MSISDN), CustomerProfileQueryType.Msisdn);
			   request = getRuleRequest(requestId,customerDetails.get(SystemConstants.MSISDN),"prepaidAccount");
			}else if(customerDetails.get(SystemConstants.ACCOUNT_TYPE).equals(SystemConstants.POSTPAID) 
					&& customerDetails.get(SystemConstants.CATEGORY).equals(SystemConstants.Business)){
				customerProfileList = queryUtil.getCustomerProfileDetails
						(requestId, customerDetails.get(SystemConstants.CRN), CustomerProfileQueryType.CRN);
			}else{
				customerProfileList = queryUtil.getCustomerProfileDetails
						(requestId, customerDetails.get(SystemConstants.ACCOUNT_NUM), CustomerProfileQueryType.AccountNumber);	
			   request = getRuleRequest(requestId,customerDetails.get(SystemConstants.ACCOUNT_NUM),"postpaidResidentialAccount");
			}
			*//**
			 * Need to think about how to do validation here 
			 * 1. First do customer profile validation
			 * 2. Then for accountNumber
			 *//*
			if(customerProfileList!=null)
				validateCustomerProfile(customerDetails,customerProfileList); 
			
			
			if(request!=null){
			GenericDTO genericDTO = new GenericDTO();
			 genericDTO.setObj(request);
			 genericDTO = new OnlineRuleInitiatorBO().executeBusinessProcess(genericDTO);
			 Response response = (Response) genericDTO.getObj();
			 if(response!=null){
				 if(response.getRespCode().equalsIgnoreCase("SC0000")){
				if(response.getDataSets()!=null){
					DataSets dataSets = response.getDataSets();
					ArrayList<DataSet> dataSet2 = dataSets.getDataSet1();
					logger.info(">>>list size>>>>"+dataSet2.size());
					ArrayList<Param> paramList =dataSet2.get(0).getParameter1();
					logger.info(">>>paramlist size>>"+paramList.size());
					for(Param p:paramList){
						logger.info(">>>>>"+p.getId());
						if(p.getId().equalsIgnoreCase("TIER_ID")){
							tierId=p.getValue();
							break;
						}
					}
				}
				 }
			 }
			}*/
			
			
			//Commenting code as not required for Apua END
			
			
			logger.info(">>>>tier id returned >>>>"+tierId);
			loyaltyId = createLoyaltyAccount(requestId,customerDetails,tierId);
			if(loyaltyId!=null&& !loyaltyId.equalsIgnoreCase(""))
			{
			logger.info("TransactionId "+requestId+" LoyaltyId "+loyaltyId);
			key="CREATE_ACCOUNT_SUCCESS_";
			responseSetter=responseSetter.customeValidation(key, responseSetter);
			reResponseHeader.setStatus(responseSetter.getStatusCode());
			reResponseHeader.setStatusDesc(responseSetter.getStatusDescription());
			}
			else
			{
				logger.info("TransactionId "+requestId+" Failed due to loyaltyId not generated");
				key="CREATE_ACCOUNT_FAILED_";
				responseSetter=responseSetter.customeValidation(key, responseSetter);
				reResponseHeader.setStatus(responseSetter.getStatusCode());
				reResponseHeader.setStatusDesc(responseSetter.getStatusDescription());
				throw new CommonException();
			}
			//Making entry in Customer Profile table 
			if(customerDetails!=null && loyaltyId!=null)
			{
				logger.info("Customer Profile Entry while creating Registration");
				List<CustomerProfileTabDTO> customerProfileTabDTOs=null;
				ServiceManagementDAO serviceManagementDAO=null;
				customerProfileTabDTOs=customerProfileDtoBuilder(customerDetails,Long.parseLong(loyaltyId));
				
				if(customerProfileTabDTOs!=null)
				{
					boolean status=false;
					serviceManagementDAO=new ServiceManagementDAO();
					for(CustomerProfileTabDTO customerProfileTabDTO:customerProfileTabDTOs){
						logger.info(">>>>>>>>>>>>>>> MSISDN "+customerProfileTabDTO.getMsisdn());
					 status=serviceManagementDAO.insertNewActicationDetails(customerProfileTabDTO);
					}
					logger.info("Insertion or updatiion status "+status);
					if ((Cache.getConfigParameterMap().get("WELCOME_POINTS_ENABLE")).getParameterValue() != null && (Cache.getConfigParameterMap().get("WELCOME_POINTS_ENABLE")).getParameterValue().trim().equalsIgnoreCase("Y")) {
			            boolean welcomePointAddStatus = addingWelcomePointsOnRegistration(reRequestHeader, customerDetails);
			            logger.info("Welcome points status " + welcomePointAddStatus);
			          }
				}
			}
			else
			{
				logger.debug("Customer Details Map befor cusotmerProfileDetails map is null");
			}
		}
		catch (CommonException e) {
			e.printStackTrace();
			reResponseHeader.setStatus(responseSetter.getStatusCode());
			reResponseHeader.setStatusDesc(responseSetter.getStatusDescription());
		}catch (Exception e) {
			logger.error("RequestID "+requestId+" Exception ",e);
			throw e;
		}finally{
			customerDetails = null;
			collectionsControllerUtil = null;
		}
		return reResponseHeader;
	}


	private Request getRuleRequest(String requestId, String msisdn,
			String keyword) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		Request request = new Request();
		request.setRequestId(requestId);
		request.setTimeStamp(sdf.format(d));
		request.setKeyWord(keyword);
		request.setMsisdn(msisdn);
		DataSet dataSet = new DataSet();
		ArrayList<Param> parameter1 = new ArrayList<>();
		parameter1.add(new Param(SystemConstants.MSISDN,msisdn));
		dataSet.setParameter1(parameter1);
		request.setDataSet(dataSet);
		return request;
	}


	private String createLoyaltyAccount(String requestId,HashMap<String, String> customerDetails, String tierId) throws ParseException {
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredMDNList = null;
		ArrayList<AccountNumberTabDTO> accountNumberList = null;
		LoyaltyAccountCreationUtil loyaltyAccountCreationUtil = null;
		Object[] objects= null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		int accountTypeId = 0;
		String loyaltyId = null;
		CDRInformationDTO cdrInformationDTO=null;
		boolean isAccountCreated = false;
		try{
			loyaltyProfileTabDTO = generateLoyaltyProfile(requestId,customerDetails,tierId);
			accountTypeId = getAccountTypeId(requestId,customerDetails);
			objects = generateLoyaltyRegisteredMDNs(customerDetails,loyaltyProfileTabDTO.getLoyaltyID(),accountTypeId);
			subscriberNumberList = (ArrayList<SubscriberNumberTabDTO>) objects[0];
			loyaltyRegisteredMDNList = (ArrayList<LoyaltyRegisteredNumberTabDTO>) objects[1];
			accountNumberList = generateAccountList( customerDetails,loyaltyProfileTabDTO.getLoyaltyID());
			//logger.info("RequestId "+requestId+" Pin Created "+loyaltyProfileTabDTO.getPin());
			loyaltyAccountCreationUtil = new LoyaltyAccountCreationUtil();
			isAccountCreated = loyaltyAccountCreationUtil.createAccount(requestId,customerDetails.get(SystemConstants.CHANNEL)!=null?Cache.channelDetails.get(customerDetails.get(SystemConstants.CHANNEL)):"",loyaltyProfileTabDTO, loyaltyRegisteredMDNList, accountNumberList,subscriberNumberList);
			loyaltyId = String.valueOf(loyaltyProfileTabDTO.getLoyaltyID());
			//loyaltyProfileTabDTO.setEncryptPin();
			//tableDet
		}catch(Exception e){
			logger.error("Exception occured",e);
		}
		finally{
			logger.debug(loyaltyProfileTabDTO);
		    	 cdrInformationDTO=new CDRInformationDTO();
		    	 
		    	 cdrInformationDTO.setTransactionId(requestId);
		    	 cdrInformationDTO.setCommandId(CDRCommandID.Registration.getId());
		    	 cdrInformationDTO.setChannelID(customerDetails.get(SystemConstants.CHANNEL)!=null?Cache.channelDetails.get(customerDetails.get(SystemConstants.CHANNEL)):"");
		    	 cdrInformationDTO.setLoyaltyId(loyaltyId);
		    	 cdrInformationDTO.setSubscriberNumber(customerDetails.get(SystemConstants.MSISDN));
		    	 cdrInformationDTO.setAccountNumber(customerDetails.get(SystemConstants.ACCOUNT_NUM));
		    	 cdrInformationDTO.setSubscriberType(customerDetails.get(SystemConstants.ACCOUNT_TYPE));
		    	 if(isAccountCreated){
		    	 cdrInformationDTO.setStatusCode("SC0000");
		    	 cdrInformationDTO.setStatusDescription("Success");
		    	 }else{
		    		 cdrInformationDTO.setStatusCode("SC0001");
			    	 cdrInformationDTO.setStatusDescription("Failure");
		    	 }
		    	 cdrInformationDTO.setTierPoints(0);
		    	 cdrInformationDTO.setBonusPoints(0);
		    	 cdrInformationDTO.setPreviousTier(1);
		    	 cdrInformationDTO.setCurrentTier(loyaltyProfileTabDTO.getTierId());
		    	 cdrInformationDTO.setField2("0");
		    	 cdrInformationDTO.setField3("0");
		    	 
					// logger.info(cdrInformationDTO.toString());
					// logger.warn(cdrInformationDTO.toString());
					CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
					
					loyaltyProfileTabDTO = null;
					loyaltyRegisteredMDNList = null;
					accountNumberList = null;
					loyaltyAccountCreationUtil = null;
					objects = null;
					subscriberNumberList = null;
					cdrInformationDTO = null;
				
		}
		return loyaltyId;
	}


	private int getAccountTypeId(String requestId, HashMap<String, String> customerDetails) {
		int accountTypeId = 0;
		if (customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE) != null && customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE).equalsIgnoreCase(SystemConstants.prepaidCustomerType)) {
			accountTypeId=1;
		}else{
			accountTypeId=2;
		}
		return accountTypeId;
	}


	private ArrayList<AccountNumberTabDTO> generateAccountList(HashMap<String, String> customerDetails,
			Long loyaltyID) {
		ArrayList<AccountNumberTabDTO> accountNumberList = null;
		AccountNumberTabDTO accountNumberTabDTO = null;
		try{
			accountNumberList = new ArrayList<>();
			for(String key : customerDetails.keySet()){
				if(key.startsWith("ACCOUNT_NUM_") || key.startsWith(SystemConstants.ACCOUNT_NUM) || key.startsWith("ASA_")){
					accountNumberTabDTO = getAccountNumberDetails(customerDetails,loyaltyID,key.startsWith("ASA"),key);
					accountNumberList.add(accountNumberTabDTO);
					//accountNumberTabDTO.set
				}
			}
		}finally{
			
		}
		return accountNumberList;
	}


	private AccountNumberTabDTO getAccountNumberDetails(HashMap<String, String> customerDetails, Long loyaltyID,boolean isASA, String key) {
		AccountNumberTabDTO accountNumberTabDTO = null;
		try{
			accountNumberTabDTO = new AccountNumberTabDTO();
			accountNumberTabDTO.setAccountNumber(customerDetails.get(key));
			accountNumberTabDTO.setLoyaltyID(loyaltyID);
			accountNumberTabDTO.setIsAsa(isASA?1:0);
		}finally{
			
		}
		return accountNumberTabDTO;
	}
	
	private Object[] generateLoyaltyRegisteredMDNs(HashMap<String, String> customerDetails, Long loyaltyID, int accountTypeId) {
		LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredNumberList = null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		String msisdn = null;
		long accountNum = 0l;
		Object[] objects = null;
		try{
			loyaltyRegisteredNumberList = new ArrayList<>();
			subscriberNumberList = new ArrayList<>();
			loyaltyRegisteredNumberTabDTO = getLoyatltyRegisterdDetails(customerDetails, SystemConstants.ACCOUNT_NUM, loyaltyID, accountTypeId);
			loyaltyRegisteredNumberList.add(loyaltyRegisteredNumberTabDTO);
			
			subscriberNumberTabDTO = getSubscriberNumberDetails(customerDetails, SystemConstants.MSISDN, loyaltyID, accountTypeId);
			subscriberNumberList.add(subscriberNumberTabDTO);
			
			for(String key : customerDetails.keySet()){
				if(key.startsWith("MSISDN_")){
					loyaltyRegisteredNumberTabDTO = getLoyatltyRegisterdDetails(customerDetails,key,loyaltyID,accountTypeId);
					logger.info("Loyalty registered number details "+loyaltyRegisteredNumberTabDTO.getLinkedNumber()+
							" loyaltyId "+loyaltyID+" Account number "+loyaltyRegisteredNumberTabDTO.getAccountNumber());
					if(!customerDetails.get(SystemConstants.MSISDN).equals(loyaltyRegisteredNumberTabDTO.getLinkedNumber()))
					loyaltyRegisteredNumberList.add(loyaltyRegisteredNumberTabDTO);
					subscriberNumberTabDTO = getSubscriberNumberDetails(customerDetails,key,loyaltyID,accountTypeId);
					logger.info("MSISDN>>>>>>>>"+customerDetails.get(SystemConstants.MSISDN));
					logger.debug("Subscriber Number details "+subscriberNumberTabDTO.getSubscriberNumber()+" Account Number "+subscriberNumberTabDTO.getAccountNumber());
					if(!customerDetails.get(SystemConstants.MSISDN).equals(subscriberNumberTabDTO.getSubscriberNumber().toString()))
					subscriberNumberList.add(subscriberNumberTabDTO);
				}
			}

		}finally{
			objects = new Object[2];
			objects[0]=subscriberNumberList;
			objects[1] = loyaltyRegisteredNumberList;
		}
		return objects;
	}

private SubscriberNumberTabDTO getSubscriberNumberDetails(HashMap<String, String> customerDetails, String key,Long loyaltyID, int accountTypeId) {
			String accountKey = null;
			SubscriberNumberTabDTO subscriberNumberTabDTO = new SubscriberNumberTabDTO();
			subscriberNumberTabDTO.setLoyaltyID(loyaltyID);
			subscriberNumberTabDTO.setSubscriberNumber(Long.parseLong(customerDetails.get(key)));
			if(!key.equals(SystemConstants.MSISDN)){
				accountKey = customerDetails.get("ACCOUNT_NUM_"+customerDetails.get(key.substring(key.indexOf("_"), key.lastIndexOf("_"))));
			}else{
				accountKey = SystemConstants.ACCOUNT_NUM;
			}
			if(customerDetails.get(accountKey)!=null){
				subscriberNumberTabDTO.setAccountNumber((customerDetails.get(accountKey)));
			}
			subscriberNumberTabDTO.setAccountCategory(customerDetails.get(SystemConstants.CATEGORY));
			subscriberNumberTabDTO.setAccountTypeId(Integer.valueOf(customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE)));
			return subscriberNumberTabDTO;
			}


	private LoyaltyRegisteredNumberTabDTO getLoyatltyRegisterdDetails(HashMap<String, String> customerDetails,
			String key, Long loyaltyID, int accountTypeId) {
		logger.info(">>>>>>>>>>KEY" + key + " >>>>>>>>>>" + customerDetails.get(key) + " >>>>>>>>>>>>>>"+ customerDetails.get(SystemConstants.MSISDN));
		LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO = new LoyaltyRegisteredNumberTabDTO();
		loyaltyRegisteredNumberTabDTO.setLoyaltyID(loyaltyID);
		if (key.equalsIgnoreCase(SystemConstants.ACCOUNT_NUM)) {
			loyaltyRegisteredNumberTabDTO.setAccountNumber(customerDetails.get(key));
			loyaltyRegisteredNumberTabDTO.setLinkedNumber(customerDetails.get(SystemConstants.MSISDN));
		} else if (key.contains(SystemConstants.MSISDN)) {
			loyaltyRegisteredNumberTabDTO.setAccountNumber(key.split("_")[2]);
			loyaltyRegisteredNumberTabDTO.setLinkedNumber(key.split("_")[1]);
		}
		loyaltyRegisteredNumberTabDTO.setAccountTypeId(Integer.valueOf(customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE)));

		return loyaltyRegisteredNumberTabDTO;
	}

/*	private ArrayList<SubscriberNumberTabDTO> generateSubscriberNumberList(HashMap<String, String> customerDetails, Long loyaltyID) {
		SubscriberNumberTabDTO loyaltyRegisteredNumberTabDTO = null;
		//ArrayList<SubscriberNumberTabDTO> loyaltyRegisteredNumberList = null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		String msisdn = null;
		try{
			loyaltyRegisteredNumberList = new ArrayList<>();
			subscriberNumberList = new ArrayList<>();
			for(String key : customerDetails.keySet()){
				if(key.startsWith("MSISDN_")){
					loyaltyRegisteredNumberTabDTO = new LoyaltyRegisteredNumberTabDTO();
					subscriberNumberTabDTO = new SubscriberNumberTabDTO();
					loyaltyRegisteredNumberTabDTO.setLoyaltyID(loyaltyID);
					loyaltyRegisteredNumberTabDTO.setLinkedNumber((msisdn=customerDetails.get(key)));
					//subscriberNumberTabDTO.setSubscriberNumber(Long.parseLong(msisdn));
					loyaltyRegisteredNumberTabDTO.setAccountNumber(Long.parseLong
						(customerDetails.get("ACCOUNT_NUM_"+customerDetails.get(key))));
					//subscriberNumberTabDTO.setAccountNumber(accountNumber);
					loyaltyRegisteredNumberList.add(loyaltyRegisteredNumberTabDTO);
				}
			}
		}finally{
			
		}
		return loyaltyRegisteredNumberList;
	}
*/
	private LoyaltyProfileTabDTO generateLoyaltyProfile(String requestId, HashMap<String, String> customerDetails, String tierId) throws ParseException {
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = new LoyaltyProfileTabDTO();
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		//DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");//local setup
		try{
			loyaltyProfileTabDTO.setLoyaltyID(Long.parseLong(RandomCreditCardNumberGenerator.generateOoredooCardNumber()));
			//loyaltyProfileTabDTO.set
			loyaltyProfileTabDTO.setFirstName(customerDetails.get(SystemConstants.FNAME));
			loyaltyProfileTabDTO.setLastName(customerDetails.get(SystemConstants.LNAME));
			//loyaltyProfileTabDTO.setCrn(customerDetails.get(SystemConstants.CRN));
			loyaltyProfileTabDTO.setAddress(customerDetails.get(SystemConstants.PERM_ADDRESS));
			loyaltyProfileTabDTO.setAccountNumber(customerDetails.get(SystemConstants.ACCOUNT_NUM));
			loyaltyProfileTabDTO.setContactNumber(customerDetails.get(SystemConstants.PRIMARY_CONTACT_NUMBER));
			//loyaltyProfileTabDTO.setArbicFirstName(customerDetails.get(SystemConstants.ARA_FNAME));
			//loyaltyProfileTabDTO.setArbicLastName(customerDetails.get(SystemConstants.ARA_LNAME));
			//loyaltyProfileTabDTO.setArbicAddress(customerDetails.get(SystemConstants.ARA_ADDRESS));
			loyaltyProfileTabDTO.setCategory(customerDetails.get(SystemConstants.CATEGORY));
			if(customerDetails.get(SystemConstants.DOB).length()>5)
				loyaltyProfileTabDTO.setDateOfBirth(format.parse(customerDetails.get(SystemConstants.DOB)));
			loyaltyProfileTabDTO.setEmailID(customerDetails.get(SystemConstants.EMAIL));
		    loyaltyProfileTabDTO.setNationality(customerDetails.get(SystemConstants.NATIONALITY_ID));
			loyaltyProfileTabDTO.setRatePlan(customerDetails.get(SystemConstants.BASE_RATE_PLAN));
			loyaltyProfileTabDTO.setGender(customerDetails.get(SystemConstants.GENDER));
			loyaltyProfileTabDTO.setNationality_Id(customerDetails.get(SystemConstants.NATIONALITY_ID));
			loyaltyProfileTabDTO.setNationality_Id_Type(customerDetails.get(SystemConstants.NATIONALITY_ID_TYPE));
			loyaltyProfileTabDTO.setEncryptPin(new EncrptPassword().encryptPassword(new LoyaltyRandomNumber().getPIN()));
			if(customerDetails.containsKey(SystemConstants.isParent.toUpperCase()))
			{
				logger.info("IsParentDetails in loyaltyProfileTabSetter "+customerDetails.get(SystemConstants.isParent.toUpperCase()));
				loyaltyProfileTabDTO.setIsParent(customerDetails.get(SystemConstants.isParent.toUpperCase()));
				logger.info(loyaltyProfileTabDTO.getIsParent()+" IsParentDetails in loyaltyProfileTabSetter "+customerDetails.get(SystemConstants.isParent.toUpperCase()));
			}
			/*if(customerDetails.get(SystemConstants.Business)!=null)
				loyaltyProfileTabDTO.setIsBusiness(1);
			else
				loyaltyProfileTabDTO.setIsBusiness(0);*/
			loyaltyProfileTabDTO.setAccountType(customerDetails.get(SystemConstants.ACCOUNT_TYPE));
			logger.info(loyaltyProfileTabDTO.getAccountType()+" AccountType in loyaltyProfileTabSetter "+customerDetails.get(SystemConstants.ACCOUNT_TYPE));
			loyaltyProfileTabDTO.setStatusID(1);
		/*
		 * Call rule for getting tier id
		 */
			if(tierId!=null){
				logger.debug("Tier id set from rule "+tierId);
				loyaltyProfileTabDTO.setTierId(Integer.parseInt(tierId));
			}
			else{
				logger.debug("Tier id set auto as none");
			   loyaltyProfileTabDTO.setTierId(1);
			}//in case of tier id null or none need to set as 1
			/*Object[] obj=generateLoyaltyRegisteredMDNs(customerDetails, loyaltyProfileTabDTO.getLoyaltyID(),accountTypeId);
			loyaltyRegisteredNumberTabDTOs = (ArrayList<LoyaltyRegisteredNumberTabDTO>) obj[0];
			accountNumberTabDTOList = generateAccountList(customerDetails, loyaltyProfileTabDTO.getLoyaltyID());*/
			//generateAccountList(customerDetails, loyaltyProfileTabDTO.getLoyaltyID());
			
			logger.info("LoyaltyId generated "+loyaltyProfileTabDTO.getLoyaltyID()+"");
		} catch (NumberFormatException | CommonException e) {
			logger.error("RequestID "+requestId+" Exception ",e);
			
		}finally{
			
		}
		return loyaltyProfileTabDTO;
		}


	private boolean validateCustomerProfile(HashMap<String, String> customerDetails,
			List<CustomerProfileTabDTO> customerProfileList) {
		//customerProfileList.parallelStream().
		boolean isUpdatedRecord = false;
		for(CustomerProfileTabDTO customerProfile : customerProfileList){
			if(customerDetails.get(SystemConstants.MSISDN)!=null && customerDetails.get(SystemConstants.MSISDN) 
					.equals(customerProfile.getMsisdn())){
				isUpdatedRecord=validateCompleteProfile(customerDetails,customerProfile);
				break;
			}else{
				validateAccountDetails(customerDetails,customerProfile);;
			}
		}
		return isUpdatedRecord;
	}


	private boolean validateCompleteProfile(HashMap<String, String> customerDetails,
			CustomerProfileTabDTO customerProfile) {
		// TODO Auto-generated method stub
		boolean flag = true;
		return flag;
	}


	private void validateAccountDetails(HashMap<String, String> customerDetails,
			CustomerProfileTabDTO customerProfile) {
		
	}


	public void callGetBasicSubscriberInformation(LoyaltyProfileTabDTO loyaltyProfileTabDTO, long moNumber) {
		
	}
	
	public List<CustomerProfileTabDTO> customerProfileDtoBuilder(HashMap<String,String> customerDetails,Long loyaltyid)
	{
		List<CustomerProfileTabDTO> customerProfileTabDTOs=new ArrayList<CustomerProfileTabDTO>();
		CustomerProfileTabDTO customerProfileTabDTO=null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		logger.info("CustomerProfileDtoBuilder ");
		try
		{
			
			customerProfileTabDTO= new CustomerProfileTabDTO();
			if(customerDetails.containsKey(SystemConstants.FNAME))
				customerProfileTabDTO.setFirstName(customerDetails.get(SystemConstants.FNAME));
			else if(customerDetails.containsKey(CommonServiceConstants.customerNameParam))
				customerProfileTabDTO.setFirstName(customerDetails.get(CommonServiceConstants.customerNameParam));
			
			if(customerDetails.containsKey(SystemConstants.LNAME))
				customerProfileTabDTO.setLastName(customerDetails.get(SystemConstants.LNAME));
			if(customerDetails.containsKey(SystemConstants.CRN)) 
				customerProfileTabDTO.setCrn(customerDetails.get(SystemConstants.CRN));
			else if(customerDetails.containsKey(CommonServiceConstants.crnParam))
				customerProfileTabDTO.setCrn(customerDetails.get(CommonServiceConstants.crnParam));				
			
			if(customerDetails.containsKey(SystemConstants.ACCOUNT_NUM))
				customerProfileTabDTO.setAccountNo(customerDetails.get(SystemConstants.ACCOUNT_NUM));
			else if(customerDetails.containsKey(CommonServiceConstants.newAccountNumberParam))
				customerProfileTabDTO.setAccountNo(customerDetails.get(CommonServiceConstants.newAccountNumberParam));				
			
			if(customerDetails.containsKey(SystemConstants.PERM_ADDRESS))
				customerProfileTabDTO.setAddress(customerDetails.get(SystemConstants.PERM_ADDRESS));
			if(customerDetails.containsKey(SystemConstants.PRIMARY_CONTACT_NUMBER));
				customerProfileTabDTO.setContactNumber(customerDetails.get(SystemConstants.PRIMARY_CONTACT_NUMBER));
			if(customerDetails.containsKey(SystemConstants.CATEGORY))
				customerProfileTabDTO.setCategory((customerDetails.get(SystemConstants.CATEGORY)));
			
			if(customerDetails.containsKey(SystemConstants.ACCOUNT_TYPE))
				customerProfileTabDTO.setAccountType(customerDetails.get(SystemConstants.ACCOUNT_TYPE));
			if(customerDetails.containsKey(SystemConstants.ACCOUNT_CATEGORY_TYPE))
				customerProfileTabDTO.setAccountCategoryType(customerDetails.get(SystemConstants.ACCOUNT_CATEGORY_TYPE));
			if(customerDetails.containsKey(SystemConstants.ARA_FNAME))
				customerProfileTabDTO.setArabicFirstName(customerDetails.get(SystemConstants.ARA_FNAME));
			if(customerDetails.containsKey(SystemConstants.ARA_LNAME))
				customerProfileTabDTO.setArabicLastName(customerDetails.get(SystemConstants.ARA_LNAME));		
			if(customerDetails.containsKey(SystemConstants.ARA_ADDRESS))
				customerProfileTabDTO.setArabicAddress(customerDetails.get(SystemConstants.ARA_ADDRESS));
			if(customerDetails.containsKey(SystemConstants.ARPU_BAND))
				customerProfileTabDTO.setArpuBand(customerDetails.get(SystemConstants.ARPU_BAND));
			if(customerDetails.containsKey(SystemConstants.TELECO_ADMIN))
				customerProfileTabDTO.setTelecoAdmin(customerDetails.get(SystemConstants.TELECO_ADMIN));	
			if(customerDetails.containsKey(SystemConstants.BASE_RATE_PLAN))
				customerProfileTabDTO.setBaseRatePlan(customerDetails.get(SystemConstants.BASE_RATE_PLAN));		
			if(customerDetails.containsKey(SystemConstants.BILL_CYCLE))
				customerProfileTabDTO.setBillCycle(customerDetails.get(SystemConstants.BILL_CYCLE));	
			if(customerDetails.containsKey(SystemConstants.CONTRACT))
				customerProfileTabDTO.setContract(customerDetails.get(SystemConstants.CONTRACT));
			else if(customerDetails.containsKey(CommonServiceConstants.newContractTypeParam))
				customerProfileTabDTO.setContract(customerDetails.get(CommonServiceConstants.newContractTypeParam));
			
			if(customerDetails.containsKey(SystemConstants.CREDIT_LIMIT))
				customerProfileTabDTO.setCreditLimit(customerDetails.get(SystemConstants.CREDIT_LIMIT));
			if(customerDetails.containsKey(SystemConstants.CREDIT_CLASS))
				customerProfileTabDTO.setCreditClass(customerDetails.get(SystemConstants.CREDIT_CLASS));
			if(customerDetails.containsKey(SystemConstants.CUSTOMER_SEGMENT))
				customerProfileTabDTO.setCustomerSegment(customerDetails.get(SystemConstants.CUSTOMER_SEGMENT));
			else if(customerDetails.containsKey(CommonServiceConstants.customerSegmentParam))
				customerProfileTabDTO.setCustomerSegment(customerDetails.get(CommonServiceConstants.customerSegmentParam));
				
			if(customerDetails.containsKey(SystemConstants.DOB))
				customerProfileTabDTO.setDob(customerDetails.get(SystemConstants.DOB));
			if(customerDetails.containsKey(SystemConstants.EMAIL))
				customerProfileTabDTO.setEmailId(customerDetails.get(SystemConstants.EMAIL));		
			if(customerDetails.containsKey(SystemConstants.GENDER))
				customerProfileTabDTO.setGender(customerDetails.get(SystemConstants.GENDER));
			if(customerDetails.containsKey(SystemConstants.LANGUAGE))
				customerProfileTabDTO.setDefaultLanguage(customerDetails.get(SystemConstants.LANGUAGE));
			if(customerDetails.containsKey(SystemConstants.FRAUD_STATUS))
				customerProfileTabDTO.setStatusId(customerDetails.get(SystemConstants.FRAUD_STATUS));
			if(customerDetails.containsKey(SystemConstants.FREQ_USED_LOCATION))
				customerProfileTabDTO.setFrequentlyUsedLocation(customerDetails.get(SystemConstants.FREQ_USED_LOCATION));
			if(customerDetails.containsKey(SystemConstants.WEEKEND_LOCATION))
				customerProfileTabDTO.setWeekendLocation(customerDetails.get(SystemConstants.WEEKEND_LOCATION));
			if(customerDetails.containsKey(SystemConstants.LINE_TYPE))
				customerProfileTabDTO.setLineType(customerDetails.get(SystemConstants.LINE_TYPE));
			if(customerDetails.containsKey(SystemConstants.MOBILE_APP_SUBSCRIPTION_STATUS));
				customerProfileTabDTO.setMobileAppSubscriptionStatus(customerDetails.get(SystemConstants.MOBILE_APP_SUBSCRIPTION_STATUS));
			if(customerDetails.containsKey(SystemConstants.MSISDN))
				customerProfileTabDTO.setMsisdn(customerDetails.get(SystemConstants.MSISDN));
			if(customerDetails.containsKey(SystemConstants.NATIONALITY_ID))
				customerProfileTabDTO.setNationalId(customerDetails.get(SystemConstants.NATIONALITY_ID));
			if(customerDetails.containsKey(SystemConstants.NATIONALITY))
				customerProfileTabDTO.setNationality(customerDetails.get(SystemConstants.NATIONALITY));
		   if(customerDetails.containsKey(SystemConstants.NATIONALITY_ID_TYPE))
				customerProfileTabDTO.setNationalIdType(customerDetails.get(SystemConstants.NATIONALITY_ID_TYPE));
		   if(customerDetails.containsKey(SystemConstants.ACTIVATION_DATE))
			   customerProfileTabDTO.setActivationDate(format.parse(customerDetails.get(SystemConstants.ACTIVATION_DATE)));
		   
		   if(customerDetails.containsKey(SystemConstants.isParent.toUpperCase()))
			   customerProfileTabDTO.setIsParent(customerDetails.get(SystemConstants.isParent.toUpperCase()));
		   if(customerDetails.containsKey(SystemConstants.profileId.toUpperCase()))
			   customerProfileTabDTO.setProfileId(customerDetails.get(SystemConstants.profileId.toUpperCase()));
				
				if(loyaltyid!=0)
					customerProfileTabDTO.setLoyaltyId(loyaltyid);	
				
				customerProfileTabDTO.setStatusId("1");
				customerProfileTabDTOs.add(customerProfileTabDTO);
	
		}catch(Exception e)
		{
			logger.error("Exception  "+e.getMessage());
		}
		return customerProfileTabDTOs;
	}

	public List<CustomerProfileTabDTO> customerProfileDtoBuilder(HashMap<String, String> customerDetails) {
		return customerProfileDtoBuilder(customerDetails, 0l);
	}
	
	public ArrayList<SubscriberNumberTabDTO> alreadyRegisteredValidation(String requestId,String msisdn)
	{
		ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
		CommonUtilDAO commonUtilDao=null;
		String subscriberTableName=null;
		TableInfoDAO tableInfoDAO=null;
		try
		{
			tableInfoDAO= new TableInfoDAO();
			commonUtilDao = new CommonUtilDAO();
			subscriberTableName = tableInfoDAO.getSubscriberNumberTable(msisdn);
			subscriberInfoList=commonUtilDao.getSubscriberInformation(requestId, subscriberTableName, msisdn);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Transaction id "+requestId+"Exception "+e.getMessage());
		}
		return subscriberInfoList;
	}
	
	public boolean addingWelcomePointsOnRegistration(RERequestHeader createAccountRequestHeader, HashMap<String, String> customerDetails) {
	    GenericDTO genericDTO = null;
	    boolean addingPointsStatus = false;
	    RERequestHeader requestHeader = null;
	    RERequestDataSet dataSet = null;
	    ArrayList<ReResponseParameter> reResponseParameters = null;
	    REResponseHeader reResponseHeader = null;
	    TierAndBonusPointCalculationBL tierAndBonusPointCalculationBL = null;
	    try {
	      genericDTO = new GenericDTO();
	      tierAndBonusPointCalculationBL = new TierAndBonusPointCalculationBL();
	      reResponseParameters = new ArrayList<>();
	      dataSet = new RERequestDataSet();
	      requestHeader = new RERequestHeader();
	      requestHeader.setRequestId(createAccountRequestHeader.getRequestId());
	      requestHeader.setTimeStamp(createAccountRequestHeader.getTimeStamp());
	      requestHeader.setMsisdn(customerDetails.get("CONTACT_NUMBER"));
	      reResponseParameters.add(new ReResponseParameter("MSISDN", customerDetails.get("CONTACT_NUMBER")));
	      reResponseParameters.add(new ReResponseParameter("IS_TIER_CALCULATION", "1"));
	      if (((String)customerDetails.get("ACCOUNT_TYPE")).equalsIgnoreCase("POSTPAID")) {
	        reResponseParameters.add(new ReResponseParameter("VOLUME_TAG", Cache.getConfigParameterMap().get("POSTPAID_WELCOME_POINTS").getParameterValue()));
	        reResponseParameters.add(new ReResponseParameter("EXPIRY_DAYS", Cache.getConfigParameterMap().get("POSTPAID_WELCOME_POINTS_EXPIRY_DAYS").getParameterValue()));
	        reResponseParameters.add(new ReResponseParameter("POINT_MULTIPLIER", Cache.getConfigParameterMap().get("POSTPAID_WELCOME_POINTS_MULTIPLIER").getParameterValue()));
	      } else {
	        reResponseParameters.add(new ReResponseParameter("VOLUME_TAG", Cache.getConfigParameterMap().get("PREPAID_WELCOME_POINTS").getParameterValue()));
	        reResponseParameters.add(new ReResponseParameter("EXPIRY_DAYS", Cache.getConfigParameterMap().get("PREPAID_WELCOME_POINTS_EXPIRY_DAYS").getParameterValue()));
	        reResponseParameters.add(new ReResponseParameter("POINT_MULTIPLIER", Cache.getConfigParameterMap().get("PREPAID_WELCOME_POINTS_MULTIPLIER").getParameterValue()));
	      } 
	        reResponseParameters.add(new ReResponseParameter("CHANNEL_ID", customerDetails.get("CHANNEL")));
			dataSet.setResponseParam(reResponseParameters);
			requestHeader.setDataSet(dataSet);
			genericDTO.setObj(requestHeader);
			genericDTO = tierAndBonusPointCalculationBL.executeBusinessProcess(genericDTO);
			reResponseHeader = (REResponseHeader) genericDTO.getObj();
			if (reResponseHeader != null && reResponseHeader.getStatus() != null
					&& reResponseHeader.getStatus().equalsIgnoreCase("SC0000"))
				addingPointsStatus = true;
		} catch (Exception e) {
	      logger.info("TransactionId " + createAccountRequestHeader.getRequestId() + " While Adding Welcome Points Exception " + e.getMessage());
	      e.printStackTrace();
	    } 
	    return addingPointsStatus;
	  }
	
	public static void main(String arg[]) throws ParseException
	{
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String date="11/14/2002";
		System.out.println(format.parse(date));
		
	}
}
