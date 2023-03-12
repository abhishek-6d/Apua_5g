package com.sixdee.imp.bo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.CommonUtilDAO;
import com.sixdee.imp.dao.ServiceManagementDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.GeneralManagementCdrDto;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.ServiceManagementDTO;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.imp.util.CommonServiceConstants;
import com.sixdee.imp.utill.Param;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.util.constant.SystemConstants;
import com.sixdee.lms.util.selections.CDRCommandID;

public class ServiceManagermentBL extends BOCommon {
	private final static Logger logger = Logger.getLogger(ServiceManagermentBL.class);
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/**
	 * This Service is created to perform the ServiceManagement operation
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => ServiceManagermentBL :: Method => buildProcess()");
		CommonUtil commonUtil = null;
		ServiceManagementDTO serviceManagementDTO = (ServiceManagementDTO) genericDTO.getObj();
		GeneralManagementCdrDto generalManagementCdrDto = new GeneralManagementCdrDto();
		String requestId = null;
		String key="";
		 String ruleTrigger = null;
		long t1=0;
		try {
			logger.info("ServiceIdentifier:"+serviceManagementDTO.getServiceIdentifier());
			commonUtil=new CommonUtil();
			requestId = serviceManagementDTO.getTransactionId();
			 switch(serviceManagementDTO.getServiceIdentifier()) {
		      case CommonServiceConstants.numberTermination: 
		    	  if(serviceManagementDTO.getIsSoftDelete()!=null && !serviceManagementDTO.getIsSoftDelete().equalsIgnoreCase(""))
		    	  {
		    	   t1=System.currentTimeMillis();
		    	  serviceManagementDTO= numberTerminationService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
		    		  key = "SERVICE_MANAGEMENT_IS_SOFT_DELETE_REQ";
		    		  serviceManagementDTO = customValidation(key, serviceManagementDTO);
		    		  logger.info("Please pass IsSoftDelete data tag in request transaction Id "+ serviceManagementDTO.getTransactionId());
		    	  }
		        break;
		      case CommonServiceConstants.mnp: 
		    	  if(serviceManagementDTO.getIsPortIn()!=null && !serviceManagementDTO.getIsPortIn().equalsIgnoreCase(""))
		    	  {
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= mnpService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
		    		 key = "SERVICE_MANAGEMENT_IS_PORT_IN_REQ";
		    		 serviceManagementDTO = customValidation(key, serviceManagementDTO);
		    		 logger.info("Please pass IsPortIn data tag in request transaction Id "+ serviceManagementDTO.getTransactionId()); 
		    	  }
		        break;
		      case CommonServiceConstants.changeOfOwnership: 
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= changeOfOwnerShipService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		        break;
		      case CommonServiceConstants.msisdnChange:
		    	  if(serviceManagementDTO.getChangeMsisdn()!=null && !serviceManagementDTO.getChangeMsisdn().equalsIgnoreCase(""))
		    	  {
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= msisdnChangeService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
		    		 key = "SERVICE_MANAGEMENT_CHANGE_MSISDN_REQ";
			    	 serviceManagementDTO = customValidation(key, serviceManagementDTO);
			    	 logger.info("Please pass ChangeMsisdn data tag in request transaction Id "+ serviceManagementDTO.getTransactionId()); 
		    	  }
		        break;
		      case CommonServiceConstants.ratePlanChange: 
		    	  if(serviceManagementDTO.getNewRatePlan()!=null && !serviceManagementDTO.getNewRatePlan().equalsIgnoreCase(""))
		    	  {
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= ratePlanChangeService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
		    	    key = "SERVICE_MANAGEMENT_RATE_PLAN_REQ";
				    serviceManagementDTO = customValidation(key, serviceManagementDTO);
				    logger.info("Please pass NewRatePlan data tag in request transaction Id "+ serviceManagementDTO.getTransactionId()); 
		    	  }
		        break;
		      case CommonServiceConstants.serviceMigration:
				if (serviceManagementDTO.getNewAccountNumber() != null && serviceManagementDTO.getAccountType() != null
						&& serviceManagementDTO.getNewContractType() != null
						&& serviceManagementDTO.getOldAccountNumber() != null
						&& serviceManagementDTO.getOldContractType() != null
						&& !serviceManagementDTO.getNewAccountNumber().equalsIgnoreCase("")
						&& !serviceManagementDTO.getAccountType().equalsIgnoreCase("")
						&& !serviceManagementDTO.getNewContractType().equalsIgnoreCase("")
						&& !serviceManagementDTO.getOldAccountNumber().equalsIgnoreCase("")
						&& !serviceManagementDTO.getOldContractType().equalsIgnoreCase("")) {
					t1=System.currentTimeMillis();
					serviceManagementDTO = updateServiceMigration(serviceManagementDTO);
					logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
				} else {
					key = "SERVICE_MANAGEMENT_SERVICE_MIGRATION_FIELD_REQ";
				    serviceManagementDTO = customValidation(key, serviceManagementDTO);
				    logger.info("Please pass NewAccountNumber, AccountType , NewContractType ,OldAccountNumber,OldContractType data tag in request transaction Id "+ serviceManagementDTO.getTransactionId());
				}
		        break;
		      case CommonServiceConstants.mobileAppRegistration: 
		    	  if(serviceManagementDTO.getReferreeNumber()!=null && !serviceManagementDTO.getReferreeNumber().equalsIgnoreCase(""))
		    	  {
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= mobileAppIntegrationService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
			    	  key = "SERVICE_MANAGEMENT_SERVICE_MOBILE_APP_INTEGRATION";
					  serviceManagementDTO = customValidation(key, serviceManagementDTO);
					  logger.info("Please pass ReferreeNumber data tag in request transaction Id "+ serviceManagementDTO.getTransactionId()); 
			    }
		        break;
		      case CommonServiceConstants.fraudManagement:
		    	  if(serviceManagementDTO.getFraudStatus()!=null && !serviceManagementDTO.getFraudStatus().equalsIgnoreCase(""))
		    	  {
		    		  logger.info("fraudManagement inside");
		    		  if(serviceManagementDTO.getFraudStatus().equalsIgnoreCase(CommonServiceConstants.fraudInput1) || serviceManagementDTO.getFraudStatus().equalsIgnoreCase(CommonServiceConstants.fraudInput2))
		    		  {
		    			  if(serviceManagementDTO.getFraudStatus().equalsIgnoreCase(CommonServiceConstants.fraudInput1))
		    				  serviceManagementDTO.setFraudStatus(Cache.getConfigParameterMap().get("SERVICE_MANAGEMENT_FRAUD_STATUS_CHECKED").getParameterValue());
		    			  else
		    				  serviceManagementDTO.setFraudStatus(Cache.getConfigParameterMap().get("SERVICE_MANAGEMENT_FRAUD_STATUS_RESUMED").getParameterValue());
		    			  
		    			  	t1=System.currentTimeMillis();
		    			  	serviceManagementDTO= updateFraudStatus(serviceManagementDTO);
		    			  	logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    		  }else
		    		  {
		    			  key = "SERVICE_MANAGEMENT_SERVICE_FRAUD_KEYWORD_VAL";
						  serviceManagementDTO = customValidation(key, serviceManagementDTO);
						  logger.info("Please pass the proper input ( Checked or Resumed )in FraudStatus data tag in request transaction Id "+ serviceManagementDTO.getTransactionId()); 
		    		  }
		    	  }else
		    	  {
		    		 key = "SERVICE_MANAGEMENT_SERVICE_FRAUD_STATUS";
					 serviceManagementDTO = customValidation(key, serviceManagementDTO);
					 logger.info("Please pass FraudStatus data tag in request transaction Id "+ serviceManagementDTO.getTransactionId());   
		    	  }
		        break;
		      case CommonServiceConstants.blacklistManagement: 
		    	  if(serviceManagementDTO.getFraudStatus()!=null && !serviceManagementDTO.getFraudStatus().equalsIgnoreCase(""))
		    	  {
		    		  if(serviceManagementDTO.getFraudStatus().equalsIgnoreCase(CommonServiceConstants.fraudInput1) || serviceManagementDTO.getFraudStatus().equalsIgnoreCase(CommonServiceConstants.fraudInput2))
		    		  {
		    			  if(serviceManagementDTO.getFraudStatus().equalsIgnoreCase(CommonServiceConstants.fraudInput1))
		    				  serviceManagementDTO.setFraudStatus(Cache.getConfigParameterMap().get("SERVICE_MANAGEMENT_BLACKLIST_STATUS_CHECKED").getParameterValue());
		    			  else
		    				  serviceManagementDTO.setFraudStatus(Cache.getConfigParameterMap().get("SERVICE_MANAGEMENT_BLACKLIST_STATUS_RESUMED").getParameterValue());
		    			  
		    			  	t1=System.currentTimeMillis();
		    			  	serviceManagementDTO= updateBlacklistStatus(serviceManagementDTO);
		    			  	logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    		  }else
		    		  {
		    			  key = "SERVICE_MANAGEMENT_SERVICE_FRAUD_KEYWORD_VAL";
						  serviceManagementDTO = customValidation(key, serviceManagementDTO);
						  logger.info("Please pass the proper input ( Checked or Resumed )in FraudStatus data tag in request transaction Id "+ serviceManagementDTO.getTransactionId()); 
		    		  }
		    	  }else
		    	  {
		    		 key = "SERVICE_MANAGEMENT_SERVICE_FRAUD_STATUS";
					 serviceManagementDTO = customValidation(key, serviceManagementDTO);
					 logger.info("Please pass FraudStatus data tag in request transaction Id "+ serviceManagementDTO.getTransactionId());   
		    	  }
		        break;
		      case CommonServiceConstants.newActivation:
		    	 /* t1=System.currentTimeMillis();
		    	  newCustomerActivation = new NewCustomerActivationBO();
		    	  serviceManagementDTO = newCustomerActivation.executeServiceProcess(serviceManagementDTO);
		    	 // serviceManagementDTO= newActivationService(serviceManagementDTO);
		    	  generalManagementCdrDto.setDescription("New Activation CustomerProfile");
		    	  commonCdrforServices(generalManagementCdrDto,serviceManagementDTO);
		    	  ruleTrigger = RuleTriggerConstants.newActivation;*/
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		        break;
		      case CommonServiceConstants.updateTelecoAdmin: 
		    	  if(serviceManagementDTO.getParentMsisdn()!=null && !serviceManagementDTO.getParentMsisdn().equalsIgnoreCase(""))
		    	  {
		    		  serviceManagementDTO.setParentMsisdn(commonUtil.discardCountryCodeIfExists(serviceManagementDTO.getParentMsisdn()));
		    		  serviceManagementDTO.setMoNumber(commonUtil.discardCountryCodeIfExists(serviceManagementDTO.getMoNumber()));
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= updateTelecoAdminService(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
		    		  key = "SERVICE_MANAGEMENT_CHANGE_MSISDN_OWNERSHIP";
					  serviceManagementDTO = customValidation(key, serviceManagementDTO);
					  logger.info("Please pass PARENT_MSISDN data tag in request transaction Id "+ serviceManagementDTO.getTransactionId());
		    	  }
		        break;
		        
		      case CommonServiceConstants.updateContactNumber: 
		    	  if(serviceManagementDTO.getOldContactNumber()!=null || serviceManagementDTO.getNewContactNumber()!=null)
		    	  {
		    	  t1=System.currentTimeMillis();
		    	  serviceManagementDTO= updateContactNumber(serviceManagementDTO);
		    	  logger.info("Total time has time taken for the service TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - t1));
		    	  }else
		    	  {
		    		  key = "SERVICE_MANAGEMENT_SERVICE_CONTACT_NUMBER";
					  serviceManagementDTO = customValidation(key, serviceManagementDTO);
					  logger.info("Please pass NewcontactNumber OR (NewcontactNumber,OldcontactNumber) data tags in request transaction Id "+ serviceManagementDTO.getTransactionId());
		    	  }
		        break;
		      default: logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" Service identified "+serviceManagementDTO.getServiceIdentifier());
		    }
			 logger.info("responseCode:"+serviceManagementDTO.getStatusCode()+"responseDescription"+ serviceManagementDTO.getStatusDesc());
			 genericDTO.setObj(serviceManagementDTO);
			// checkIfRuleExecuted(requestId,ruleTrigger,serviceManagementDTO);
		} catch (Exception e) {
			logger.error("Exception  occured",e);
            genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GENERAL_MANAGEMENT_SERVICE_SYSTEM_FAILURE", serviceManagementDTO.getTransactionId());
           
        
			
		}finally{
			if((key=serviceManagementDTO.getResponseKey())!=null){
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
			
			commonUtil=null;
		}
			return genericDTO;
		}
	
/*	private GenericDTO checkIfRuleExecuted(String requestId, String ruleTrigger,String rootProfile, ServiceManagementDTO serviceManagementDTO) {
		// TODO Auto-generated method stub
		GenericDTO genericDTO = null;
		RuleCommonUtil ruleCommonUtil = null;
		Request request = null;
		HashMap<String,String> dataMap = null;
		OnlineRuleInitiatorBO onlineRuleInitiatorBO = null;
		try{
			ruleCommonUtil = new RuleCommonUtil();
			request = ruleCommonUtil.getRuleRequest(requestId, serviceManagementDTO.getTimestamp(), 
					rootProfile, ruleTrigger);
			if((dataMap=serviceManagementDTO.getDataMap())!=null)
				request.getDataSet().getParameter1().addAll(getRuleParameters(dataMap));
			genericDTO = new GenericDTO();
			genericDTO.setObj(request);
			onlineRuleInitiatorBO = new OnlineRuleInitiatorBO();
			genericDTO = onlineRuleInitiatorBO.executeBusinessProcess(genericDTO);
		}finally{
			
		}
		return genericDTO;
	}*/

	
	
	
	private ArrayList<Param> getRuleParameters(HashMap<String,String> dataMap){
		ArrayList<Param> paramList = new ArrayList<Param>();;
		for(String key : dataMap.keySet()){
			Param p = new Param();
			p.setId(key);
			p.setValue(dataMap.get(key));
			paramList.add(p);
		}
		return paramList;
	}

	public ServiceManagementDTO msisdnChangeService(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO  serviceManagementDAO=null;
        TableInfoDAO tableInfoDAO=null;
        CommonUtilDAO commonUtilDAO=null;
        SubscriberNumberTabDTO subscriberInfoDTO = null;
        ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
        long loyaltyId=0;
        String loyaltyTableName=null;
        String loyaltyRegisteredTableName=null;
        String subscriberTableName=null;
        GeneralManagementCdrDto cdrDto=null;
        CDRInformationDTO cdrInformationDTO=null;
        StringBuilder sbuilder =null;
        String key=null;
        List<LoyaltyProfileTabDTO> loyaltyProfileList=null;
        try{
        
		tableInfoDAO = new TableInfoDAO();
		cdrInformationDTO= new CDRInformationDTO();
		sbuilder= new StringBuilder("");
		cdrDto= new GeneralManagementCdrDto();
		serviceManagementDAO=new ServiceManagementDAO();
		commonUtilDAO=new CommonUtilDAO();
	  	boolean subscriberTabUpdate=false,loyaltyTabUpdate = false,loyaltyRegisteredTabUpdate=false,customerProfileTabStatus=false;
	    subscriberTableName = tableInfoDAO.getSubscriberNumberTable(serviceManagementDTO.getMoNumber());
		subscriberInfoList = commonUtilDAO.getSubscriberInformation(serviceManagementDTO.getTransactionId(),subscriberTableName,serviceManagementDTO.getMoNumber());
		if(subscriberInfoList != null && subscriberInfoList.size()!=0){
			subscriberInfoDTO = subscriberInfoList.get(0);
			loyaltyId = subscriberInfoDTO.getLoyaltyID();
			loyaltyTableName=tableInfoDAO.getLoyaltyProfileTable(loyaltyId+"");
			sbuilder.append(serviceManagementDTO.getChangeMsisdn());
			cdrInformationDTO.setLoyaltyId(String.valueOf(loyaltyId));
			loyaltyRegisteredTableName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyId+"");
			String oldMsisdn=serviceManagementDTO.getMoNumber();
			String newMsisdn=serviceManagementDTO.getChangeMsisdn();
			subscriberTabUpdate=serviceManagementDAO.updateMsisdnChangeSubsriberTab(oldMsisdn,newMsisdn,subscriberTableName,serviceManagementDTO);
			logger.info("subscriberTableName "+subscriberTableName +"status is "+subscriberTabUpdate);
			if(subscriberTabUpdate)
			{
				loyaltyProfileList=getLoyaltyProfileMsisdnCheck(serviceManagementDTO,loyaltyTableName);
				if(loyaltyProfileList!=null && loyaltyProfileList.size()>0)
				{
					loyaltyTabUpdate=serviceManagementDAO.updateMsisdnChangeloyaltyUpdateTab(oldMsisdn,newMsisdn,loyaltyTableName,serviceManagementDTO);
					logger.info("loyaltyTableName "+loyaltyTableName +"status is "+loyaltyTabUpdate);
				}
			}
			if(subscriberTabUpdate)
			{
			loyaltyRegisteredTabUpdate=serviceManagementDAO.updateMsisdnChangeloyaltyRegisteredTab(oldMsisdn,newMsisdn,loyaltyRegisteredTableName,serviceManagementDTO);
			logger.info("loyaltyRegisteredTableName "+loyaltyRegisteredTableName +"status is "+loyaltyRegisteredTabUpdate);
			}else
			{
				subscriberTabUpdate=serviceManagementDAO.updateMsisdnChangeSubsriberTab(newMsisdn,oldMsisdn,subscriberTableName,serviceManagementDTO);
				logger.info("Reverse subscriberTabUpdate "+loyaltyRegisteredTableName +"status is "+subscriberTabUpdate +"newMsisdn "+ newMsisdn + "oldMsisdn "+oldMsisdn);
			}
			if(loyaltyRegisteredTabUpdate)
			{
				customerProfileTabStatus=serviceManagementDAO.updateCustomerProfileMsisdnChange(oldMsisdn,newMsisdn,serviceManagementDTO);
				logger.info("CustomerProfile Msisdn Change status is "+customerProfileTabStatus);
				logger.info("All tables has updated the new msisdn");
				 key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				 serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
			else
			{
				subscriberTabUpdate=serviceManagementDAO.updateMsisdnChangeSubsriberTab(newMsisdn,oldMsisdn,subscriberTableName,serviceManagementDTO);
				if(loyaltyTabUpdate)
				loyaltyTabUpdate=serviceManagementDAO.updateMsisdnChangeloyaltyUpdateTab(newMsisdn,oldMsisdn,loyaltyTableName,serviceManagementDTO);
				logger.info("all Reverse subscriberTabUpdate "+loyaltyRegisteredTableName +"status is "+subscriberTabUpdate +"newMsisdn "+ newMsisdn + "oldMsisdn "+oldMsisdn);
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
		}else
		{
			key = "SERVICE_MANAGEMENT_SERVICE_NOT_REGISTERD";
			serviceManagementDTO = customValidation(key, serviceManagementDTO);
			logger.info("TransactionId : "+serviceManagementDTO.getTransactionId()+" SubscriberNumber : "+serviceManagementDTO.getMoNumber()+" Message : This subscriber is not registered in loyalty platform .");
		}
        }catch(Exception e)
        {
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
        	e.printStackTrace();
        }
        finally{
 			if(cdrDto==null)
 				cdrDto=new GeneralManagementCdrDto();
 			cdrDto.setDescription("Msisdn Change service");
 			//commonCdrforServices(cdrDto,serviceManagementDTO);
 			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.msisdnChange).toString());
 			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
 			cdrInformationDTO=null;
 			tableInfoDAO = null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
 			commonUtilDAO=null;
 		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO numberTerminationService(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO  serviceManagementDAO=null;
		CDRInformationDTO cdrInformationDTO=null;
        TableInfoDAO tableInfoDAO=null;
        CommonUtilDAO commonUtilDAO=null;
        GeneralManagementCdrDto cdrDto=null;
        SubscriberNumberTabDTO subscriberInfoDTO = null;
        ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
        long loyaltyId=0;
        String loyaltyTableName=null;
        String loyaltyRegisteredTableName=null;
        String subscriberTableName=null;
        CustomerProfileTabDTO customerProfileTabDTO=null;
        boolean customerProfileStatus=false;
        StringBuilder sbuilder =null;
        String key=null;
     try{
        cdrInformationDTO= new CDRInformationDTO();
		tableInfoDAO = new TableInfoDAO();
		sbuilder= new StringBuilder("");
		cdrDto= new GeneralManagementCdrDto();
		customerProfileTabDTO=new CustomerProfileTabDTO();
		serviceManagementDAO=new ServiceManagementDAO();
		commonUtilDAO=new CommonUtilDAO();
	  	sbuilder.append(serviceManagementDTO.getIsSoftDelete());
	    subscriberTableName = tableInfoDAO.getSubscriberNumberTable(serviceManagementDTO.getMoNumber());
		   Date currentDate = new Date();
	       Calendar c = Calendar.getInstance();
	       c.setTime(currentDate);
	       c.add(Calendar.DATE, Integer.valueOf(Cache.getConfigParameterMap().get("SERVICE_MANAGEMENT_NUMBER_TERMINATION_EXPIRY_DAYS").getParameterValue()));
	       Date currentDateExpiryDays = c.getTime();
		if(serviceManagementDTO.getIsSoftDelete()!=null && serviceManagementDTO.getIsSoftDelete().equalsIgnoreCase("true"))
		{
		subscriberInfoList = commonUtilDAO.getSubscriberInformation(serviceManagementDTO.getTransactionId(),subscriberTableName,serviceManagementDTO.getMoNumber());
		if(subscriberInfoList != null && subscriberInfoList.size()!=0){
			subscriberInfoDTO = subscriberInfoList.get(0);
			loyaltyId = subscriberInfoDTO.getLoyaltyID();
			loyaltyRegisteredTableName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyId+"");
			cdrInformationDTO.setLoyaltyId(String.valueOf(loyaltyId));
			int numberTerminationStatus=Integer.valueOf(Cache.getConfigParameterMap().get("SERVICE_MANAGEMENT_NUMBER_TERMINATION_STATUS").getParameterValue());
			customerProfileStatus=commonServiceUpdation(serviceManagementDTO,numberTerminationStatus,currentDateExpiryDays,subscriberTableName,loyaltyRegisteredTableName,loyaltyId);
			if(customerProfileStatus)
			{
				logger.info("All tables has updated the new msisdn");
				 key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				 serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
			else
			{
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
		}else
		{
			logger.info("TransactionId : "+serviceManagementDTO.getTransactionId()+" SubscriberNumber : "+serviceManagementDTO.getMoNumber()+" Message : This subscriber is not registered in loyalty platform .");
			key = "SERVICE_MANAGEMENT_SERVICE_NOT_REGISTERD";
			serviceManagementDTO = customValidation(key, serviceManagementDTO);
		}
        }else
        {
        	serviceManagementDTO.setStatusCode("SC0002");
        	serviceManagementDTO.setStatusDesc("Service having missing mandatory params or not proper input for isSoftDelete");
        }
        }catch(Exception e)
        {
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
        	logger.info("Exception is "+e);
        }
        finally{
 			if(cdrDto==null)
 				cdrDto=new GeneralManagementCdrDto();
 			cdrDto.setDescription("Number Termination Service");
 			commonCdrforServices(cdrDto,serviceManagementDTO);
 			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.numberTermination).toString());
 			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
 			cdrInformationDTO=null;
 			tableInfoDAO = null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
 			commonUtilDAO=null;
 		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO changeOfOwnerShipService(ServiceManagementDTO serviceManagementDTO)
	{
        GeneralManagementCdrDto cdrDto=null;
        CDRInformationDTO cdrInformationDTO=null;
        try{
        
		cdrInformationDTO= new CDRInformationDTO();
		cdrDto= new GeneralManagementCdrDto();
		serviceManagementDTO=deletingDataFromRegistrationAndPointsTables(serviceManagementDTO,cdrInformationDTO);
			if(serviceManagementDTO!=null && serviceManagementDTO.getLoyaltyId()!=null)
			cdrInformationDTO.setLoyaltyId(serviceManagementDTO.getLoyaltyId());
        }catch(Exception e)
        {
        	logger.info("Exception is "+e);
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
        }
        finally{
     			if(cdrDto==null)
     				cdrDto=new GeneralManagementCdrDto();
     			cdrDto.setDescription("Change of OwnderShip Service");
     			//commonCdrforServices(cdrDto,serviceManagementDTO);
     			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+", Service Id - "+CommonServiceConstants.changeOfOwnership);
     			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
     			cdrInformationDTO=null;
     			cdrDto= null;
     		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO mnpService(ServiceManagementDTO serviceManagementDTO)
	{
		logger.info("Transaction id "+serviceManagementDTO.getTransactionId());
        GeneralManagementCdrDto cdrDto=null;
        CDRInformationDTO cdrInformationDTO=null;
        StringBuilder sbuilder =null;
        String key=null;
        try{
			cdrInformationDTO = new CDRInformationDTO();
			sbuilder = new StringBuilder("");
			sbuilder.append(serviceManagementDTO.getIsPortIn());
			cdrDto = new GeneralManagementCdrDto();
			if (serviceManagementDTO.getIsPortIn().equalsIgnoreCase("true")) {
			} else if (serviceManagementDTO.getIsPortIn().equalsIgnoreCase("false")) {
				//serviceManagementDTO = deletingDataFromRegistrationAndPointsTables(serviceManagementDTO);
				if(serviceManagementDTO!=null && serviceManagementDTO.getLoyaltyId()!=null)
				cdrInformationDTO.setLoyaltyId(serviceManagementDTO.getLoyaltyId());
			}else
			{
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
		} catch (Exception e) {
			serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED " + e.getMessage());
			e.printStackTrace();
			logger.info("Exception is " + e);
		}
        finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Mnp operation");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(" Service Id - "+CommonServiceConstants.mnp).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			sbuilder= null;
 			cdrDto= null;
		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO ratePlanChangeService(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO  serviceManagementDAO=null;
        TableInfoDAO tableInfoDAO=null;
        GeneralManagementCdrDto cdrDto=null;
        CommonUtilDAO commonUtilDAO=null;
        SubscriberNumberTabDTO subscriberInfoDTO = null;
        ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
        long loyaltyId=0;
        String subscriberTableName=null;
        String loyaltyTableName=null;
        CDRInformationDTO cdrInformationDTO=null;
        StringBuilder sbuilder =null;
        String key=null;
        try{
		tableInfoDAO = new TableInfoDAO();
		cdrInformationDTO= new CDRInformationDTO();
		sbuilder= new StringBuilder("");
		cdrDto= new GeneralManagementCdrDto();
		serviceManagementDAO=new ServiceManagementDAO();
		commonUtilDAO=new CommonUtilDAO();
	  	boolean customerProfileTable=false;
	  	boolean loyaltyProfileTabStatus=false;
	    subscriberTableName = tableInfoDAO.getSubscriberNumberTable(serviceManagementDTO.getMoNumber());
		subscriberInfoList = commonUtilDAO.getSubscriberInformation(serviceManagementDTO.getTransactionId(),subscriberTableName,serviceManagementDTO.getMoNumber());
		if(subscriberInfoList != null && subscriberInfoList.size()!=0){
			subscriberInfoDTO = subscriberInfoList.get(0);
			loyaltyId = subscriberInfoDTO.getLoyaltyID();
			cdrInformationDTO.setLoyaltyId(String.valueOf(loyaltyId));
			loyaltyTableName=tableInfoDAO.getLoyaltyProfileTable(loyaltyId+"");
			loyaltyProfileTabStatus=serviceManagementDAO.updateloyaltyProfileBaseRatePlan(loyaltyTableName,loyaltyId,serviceManagementDTO);
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" loyaltyProfileTabStatus "+loyaltyProfileTabStatus);
			sbuilder.append(serviceManagementDTO.getNewRatePlan()+","+serviceManagementDTO.getOldRatePlan());
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" LoyaltyId "+loyaltyId);
			customerProfileTable=serviceManagementDAO.updateNewRatePlanTab(serviceManagementDTO);
			if(customerProfileTable)
			{
				logger.info("Transaction id "+serviceManagementDTO.getTransactionId() +" RatePlan updated successfully in customerProfile Tab "+serviceManagementDTO.getNewRatePlan());
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
				
			}
			else
			{
				logger.info("Transaction id "+serviceManagementDTO.getTransactionId() +" RatePlan updated Failed in customerProfile Tab "+serviceManagementDTO.getNewRatePlan());
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
		
		}else
		{
			key = "SERVICE_MANAGEMENT_SERVICE_NOT_REGISTERD";
			serviceManagementDTO = customValidation(key, serviceManagementDTO);
			logger.info("TransactionId : "+serviceManagementDTO.getTransactionId()+" SubscriberNumber : "+serviceManagementDTO.getMoNumber()+" Message : This subscriber is not registered in loyalty platform .");
		}
        }catch(Exception e)
        {
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
        	logger.info("Exception is "+e);
        	e.printStackTrace();
        }
        finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Rate plan change Service");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.ratePlanChange).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			tableInfoDAO = null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
 			commonUtilDAO=null;
		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO mobileAppIntegrationService(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO  serviceManagementDAO=null;
        TableInfoDAO tableInfoDAO=null;
        CommonUtilDAO commonUtilDAO=null;
        String subscriberTableName=null;
        GeneralManagementCdrDto cdrDto=null;
        CDRInformationDTO cdrInformationDTO=null;
        StringBuilder sbuilder =null;
        String key=null;
        try{
		tableInfoDAO = new TableInfoDAO();
		cdrDto = new GeneralManagementCdrDto();
		cdrInformationDTO= new CDRInformationDTO();
		sbuilder= new StringBuilder("");
		serviceManagementDAO=new ServiceManagementDAO();
		commonUtilDAO=new CommonUtilDAO();
	  	boolean subscriberTabUpdate=false;
	  	boolean customerProfileTabUpdate=false;
	    subscriberTableName = tableInfoDAO.getSubscriberNumberTable(serviceManagementDTO.getMoNumber());
	    String referreNumber=serviceManagementDTO.getReferreeNumber();
	    String moNumber=serviceManagementDTO.getMoNumber();
	    sbuilder.append(referreNumber);
	    
	    subscriberTabUpdate=serviceManagementDAO.updateMobileAppIntegrationSubscriberTab(referreNumber,subscriberTableName,serviceManagementDTO,moNumber);
	    logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+"subscriberTableName update status "+subscriberTabUpdate);
	    if(subscriberTabUpdate)
	    {
	    	customerProfileTabUpdate=serviceManagementDAO.updateMobileAppIntegrationCustomerProfileTab(serviceManagementDTO);
	    	logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" customerProfileTableName update status "+customerProfileTabUpdate +"Referree Num"+serviceManagementDTO.getReferreeNumber() +"MoNumber"+serviceManagementDTO.getMoNumber());
	    }
	    if(customerProfileTabUpdate)
	    {
	    	logger.info("Updated all the tables for mobileAppNotification");
	    	key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
	    	serviceManagementDTO = customValidation(key, serviceManagementDTO);
	    }
	    else
	    {
	    	key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
	    	serviceManagementDTO = customValidation(key, serviceManagementDTO);
	    	subscriberTabUpdate=serviceManagementDAO.updateMobileAppIntegrationSubscriberTab(moNumber,subscriberTableName,serviceManagementDTO,referreNumber);
	    	logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+"subscriberTableName Reverser update status due to failed "+subscriberTabUpdate);
	    	
	    }
        }catch(Exception e)
        {
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
        	logger.info("Exception is "+e);
        }
        finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Mobile App Integration");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.mobileAppRegistration).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			tableInfoDAO = null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
 			commonUtilDAO=null;
		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO updateTelecoAdminService(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO  serviceManagementDAO=null;
        TableInfoDAO tableInfoDAO=null;
        GeneralManagementCdrDto cdrDto=null;
        CommonUtilDAO commonUtilDAO=null;
        SubscriberNumberTabDTO subscriberInfoDTO = null;
        ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
        long loyaltyId=0;
        String subscriberTableName=null;
        String loyaltyTableName=null;
        String loyaltyRegisteredTabName=null;
        CDRInformationDTO cdrInformationDTO=null;
        StringBuilder sbuilder =null;
        String key=null;
        try{
		tableInfoDAO = new TableInfoDAO();
		cdrInformationDTO= new CDRInformationDTO();
		sbuilder= new StringBuilder("");
		cdrDto= new GeneralManagementCdrDto();
		serviceManagementDAO=new ServiceManagementDAO();
		commonUtilDAO=new CommonUtilDAO();
	  	boolean customerProfileTable=false;
	  	boolean loyaltyProfileTabStatus=false;
	  	boolean subscriberTabUpdateStatus=false;
	  	boolean loyatlyRegisterdNumberTabStatus=false;
	    subscriberTableName = tableInfoDAO.getSubscriberNumberTable(serviceManagementDTO.getMoNumber());
		subscriberInfoList = commonUtilDAO.getSubscriberInformation(serviceManagementDTO.getTransactionId(),subscriberTableName,serviceManagementDTO.getMoNumber());
		if(subscriberInfoList != null && subscriberInfoList.size()!=0){
			subscriberInfoDTO = subscriberInfoList.get(0);
			loyaltyId = subscriberInfoDTO.getLoyaltyID();
			cdrInformationDTO.setLoyaltyId(String.valueOf(loyaltyId));
			loyaltyTableName=tableInfoDAO.getLoyaltyProfileTable(loyaltyId+"");
			loyaltyRegisteredTabName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyId+"");
			subscriberTabUpdateStatus=serviceManagementDAO.updateChildToParentSubscriberTab(subscriberTableName,serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getMoNumber(),serviceManagementDTO.getTransactionId());
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" SubscriberTabUpdateStatus "+subscriberTabUpdateStatus);
			sbuilder.append(serviceManagementDTO.getParentMsisdn()+",");
			if(subscriberTabUpdateStatus)
			{
			loyaltyProfileTabStatus=serviceManagementDAO.updateChildToParentLoyaltyProfileTab(loyaltyId,loyaltyTableName,serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getMoNumber(),serviceManagementDTO.getTransactionId());
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" LoyaltyProfileTabStatus "+loyaltyProfileTabStatus);
			}
			if(loyaltyProfileTabStatus)
			{
			loyatlyRegisterdNumberTabStatus=serviceManagementDAO.updateChildToParentLoyaltyRegisteredNumberTab(loyaltyId,loyaltyRegisteredTabName,serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getMoNumber(),serviceManagementDTO.getTransactionId());
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" LoyaltyRegisteredTabStatus "+loyatlyRegisterdNumberTabStatus);
			}
			else
			{
			subscriberTabUpdateStatus=serviceManagementDAO.updateChildToParentSubscriberTab(subscriberTableName,serviceManagementDTO.getMoNumber(),serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getTransactionId());
			}
			
			if(loyatlyRegisterdNumberTabStatus)
			{
			customerProfileTable=serviceManagementDAO.updateChildToParentCustomerProfileTab(serviceManagementDTO);
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" customerProfileTableUpdateStatus "+customerProfileTable);
			}else
			{
				subscriberTabUpdateStatus=serviceManagementDAO.updateChildToParentSubscriberTab(subscriberTableName,serviceManagementDTO.getMoNumber(),serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getTransactionId());
				loyaltyProfileTabStatus=serviceManagementDAO.updateChildToParentLoyaltyProfileTab(loyaltyId,loyaltyTableName,serviceManagementDTO.getMoNumber(),serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getTransactionId());
			}
			if(customerProfileTable)
			{
				logger.info("Transaction id "+serviceManagementDTO.getTransactionId() +" ParentMsisdn updated successfully in customerProfile Tab "+serviceManagementDTO.getNewRatePlan());
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
				
			}
			else
			{
				subscriberTabUpdateStatus=serviceManagementDAO.updateChildToParentSubscriberTab(subscriberTableName,serviceManagementDTO.getMoNumber(),serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getTransactionId());
				loyaltyProfileTabStatus=serviceManagementDAO.updateChildToParentLoyaltyProfileTab(loyaltyId,loyaltyTableName,serviceManagementDTO.getMoNumber(),serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getTransactionId());
				loyatlyRegisterdNumberTabStatus=serviceManagementDAO.updateChildToParentLoyaltyRegisteredNumberTab(loyaltyId,loyaltyRegisteredTabName,serviceManagementDTO.getMoNumber(),serviceManagementDTO.getParentMsisdn(),serviceManagementDTO.getTransactionId());
				logger.info("Transaction id "+serviceManagementDTO.getTransactionId() +"Reverting in all the tables "+serviceManagementDTO.getParentMsisdn());
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
		
		}else
		{
			key = "SERVICE_MANAGEMENT_SERVICE_NOT_REGISTERD";
			serviceManagementDTO = customValidation(key, serviceManagementDTO);
			logger.info("TransactionId : "+serviceManagementDTO.getTransactionId()+" SubscriberNumber : "+serviceManagementDTO.getMoNumber()+" Message : This subscriber is not registered in loyalty platform .");
		}
        }catch(Exception e)
        {
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
        	logger.info("Exception is "+e);
        	e.printStackTrace();
        }
        finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Change Msisndn child to Parent Service");
			//commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.updateTelecoAdmin).toString()+" , parentMsisdn "+serviceManagementDTO.getParentMsisdn());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			tableInfoDAO = null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
 			commonUtilDAO=null;
		}
		return serviceManagementDTO;
	}
	
	
	public ServiceManagementDTO updateFraudStatus(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO serviceManagementDAO = null;
		boolean updateFraudStatus = false;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		GeneralManagementCdrDto cdrDto=null;
		CDRInformationDTO cdrInformationDTO=null;
		StringBuilder sbuilder =null;
		String key=null;
		try {
			cdrDto =new GeneralManagementCdrDto();
			cdrInformationDTO= new CDRInformationDTO();
			sbuilder= new StringBuilder("");
			serviceManagementDAO = new ServiceManagementDAO();
			customerProfileTabDTO=new CustomerProfileTabDTO();
			customerProfileTabDTO.setStatusId(serviceManagementDTO.getFraudStatus());
			customerProfileTabDTO.setMsisdn(serviceManagementDTO.getMoNumber());
			customerProfileTabDTO.setCustomerRefNumber(Integer.parseInt(serviceManagementDTO.getCustomerReferenceNumber()));
			sbuilder.append(serviceManagementDTO.getFraudStatus());
			updateFraudStatus = serviceManagementDAO.updateFraudStatusCustomerProfileTab(customerProfileTabDTO);
			if (updateFraudStatus) {
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			} else {
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}

		} catch (Exception e) {
			serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
			logger.info("Exception is " + e);
		}
		finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Update Fraud status");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.fraudManagement).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
		}
		return serviceManagementDTO;
	}
	public ServiceManagementDTO updateContactNumber(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO serviceManagementDAO = null;
		boolean updateContactNumber = false;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		GeneralManagementCdrDto cdrDto=null;
		CDRInformationDTO cdrInformationDTO=null;
		StringBuilder sbuilder =null;
		String key=null;
		try {
			cdrDto =new GeneralManagementCdrDto();
			cdrInformationDTO= new CDRInformationDTO();
			sbuilder= new StringBuilder("");
			serviceManagementDAO = new ServiceManagementDAO();
			sbuilder.append(serviceManagementDTO.getNewContactNumber());
			updateContactNumber = serviceManagementDAO.updateContactNumberCustomerProfileTab(serviceManagementDTO);
			if (updateContactNumber) {
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			} else {
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}

		} catch (Exception e) {
			serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
			logger.info("Exception is " + e);
		}
		finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Update Fraud status");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.fraudManagement).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO updateServiceMigration(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO serviceManagementDAO = null;
		boolean updateServiceMigration = false;
		GeneralManagementCdrDto cdrDto=null;
		CDRInformationDTO cdrInformationDTO=null;
		StringBuilder sbuilder =null;
		String key=null;
		try {
			cdrDto =new GeneralManagementCdrDto();
			cdrInformationDTO= new CDRInformationDTO();
			sbuilder= new StringBuilder("");
			serviceManagementDAO = new ServiceManagementDAO();
			sbuilder.append(serviceManagementDTO.getNewAccountNumber() + "," + serviceManagementDTO.getAccountType()
					+ "," + serviceManagementDTO.getNewContractType() + "," + serviceManagementDTO.getOldAccountNumber()
					+ "," + serviceManagementDTO.getOldContractType());
			updateServiceMigration = serviceManagementDAO.updateServiceMigrationDetailsCustomerProfile(serviceManagementDTO);
			if (updateServiceMigration) {
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			} else {
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}

		} catch (Exception e) {
			serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
			logger.info("Exception is " + e);
		}
		finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Update Service Migration ");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.serviceMigration).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO updateBlacklistStatus(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO serviceManagementDAO = null;
		boolean updateFraudStatus = false;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		GeneralManagementCdrDto cdrDto=null;
		CDRInformationDTO cdrInformationDTO=null;
		StringBuilder sbuilder =null;
		String key=null;
		try {
			cdrDto =new GeneralManagementCdrDto();
			cdrInformationDTO= new CDRInformationDTO();
			sbuilder= new StringBuilder("");
			serviceManagementDAO = new ServiceManagementDAO();
			customerProfileTabDTO=new CustomerProfileTabDTO();
			customerProfileTabDTO.setStatusId(serviceManagementDTO.getFraudStatus());
			customerProfileTabDTO.setMsisdn(serviceManagementDTO.getMoNumber());
			sbuilder.append(serviceManagementDTO.getFraudStatus());
			updateFraudStatus = serviceManagementDAO.updateBlacklistStatusCustomerProfileTab(customerProfileTabDTO);
			if (updateFraudStatus) {
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			} else {
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}

		} catch (Exception e) {
			serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
			logger.info("Exception is " + e);
		}
		finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("Update Fraud status");
			commonCdrforServices(cdrDto,serviceManagementDTO);
			cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc()+" "+sbuilder.append(", Service Id - "+CommonServiceConstants.fraudManagement).toString());
			buildingServiceManagementCdrDtoAndWritingCdr(cdrInformationDTO,serviceManagementDTO);
			cdrInformationDTO=null;
 			sbuilder= null;
 			cdrDto= null;
 			serviceManagementDAO=null;
		}
		return serviceManagementDTO;
	}
	
	public ServiceManagementDTO newActivationService(ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO serviceManagementDAO = null;
		boolean newActivation = false;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		GeneralManagementCdrDto cdrDto=null;
		String key=null;
		try {
			serviceManagementDAO = new ServiceManagementDAO();
			cdrDto= new GeneralManagementCdrDto();
			customerProfileTabDTO=new CustomerProfileTabDTO();
			customerProfileTabDTO.setContract(serviceManagementDTO.getNewContractType());
			customerProfileTabDTO.setAccountNo((serviceManagementDTO.getNewAccountNumber()));
			customerProfileTabDTO.setCrn(serviceManagementDTO.getCrn());
			customerProfileTabDTO.setLineType(serviceManagementDTO.getLineType());
			customerProfileTabDTO.setCustomerSegment(serviceManagementDTO.getCustomerSegment());
			customerProfileTabDTO.setAsa(serviceManagementDTO.getAsa());
			customerProfileTabDTO.setNationalId(serviceManagementDTO.getNationalId());
			customerProfileTabDTO.setNationalIdType(serviceManagementDTO.getNationalIdType());
			customerProfileTabDTO.setNationality(serviceManagementDTO.getNationality());
			customerProfileTabDTO.setAccountCategoryType(serviceManagementDTO.getAccountCategoryType());
			customerProfileTabDTO.setFirstName(serviceManagementDTO.getCustomerName());
			customerProfileTabDTO.setAccountType(serviceManagementDTO.getAccountType());
			customerProfileTabDTO.setContract(serviceManagementDTO.getNewContractType());
			customerProfileTabDTO.setMsisdn(serviceManagementDTO.getMoNumber());
			newActivation = serviceManagementDAO.insertNewActicationDetails(customerProfileTabDTO);
			if (newActivation) {
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			} else {
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}

		} catch (Exception e) {
			serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
			logger.info("Exception is " + e);
		}
		finally{
			if(cdrDto==null)
				cdrDto=new GeneralManagementCdrDto();
			cdrDto.setDescription("New Activation CustomerProfile");
			commonCdrforServices(cdrDto,serviceManagementDTO);
 			cdrDto= null;
 			serviceManagementDAO=null;
 			customerProfileTabDTO=null;
		}
		return serviceManagementDTO;
	}
	
	public void commonCdrforServices(GeneralManagementCdrDto generalManagementCdrDto,ServiceManagementDTO serviceManagementDTO)
	{
		ServiceManagementDAO serviceManagementDAO = null;

		try {
			serviceManagementDAO = new ServiceManagementDAO();
			generalManagementCdrDto.setRequestDate(new Date());
			generalManagementCdrDto.setTransactionId(serviceManagementDTO.getTransactionId());
			generalManagementCdrDto.setMsisdn(serviceManagementDTO.getMoNumber());
			generalManagementCdrDto.setService(String.valueOf(serviceManagementDTO.getServiceIdentifier()));
			generalManagementCdrDto.setStatus(serviceManagementDTO.getStatusCode());
			generalManagementCdrDto.setDataInfo(serviceManagementDTO.toString());
			serviceManagementDAO.cdrGeneralManagementServices(generalManagementCdrDto);

		} catch (Exception e) {
			logger.info("Exception while writing cdr for General Management Services " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public ServiceManagementDTO customValidation(String key,ServiceManagementDTO serviceManagementDTO) throws CommonException
	{
		ServiceStatusDTO actionServiceDetailsDTO = null;
		if((actionServiceDetailsDTO = Cache.getServiceStatusMap().get(key))!=null){
			serviceManagementDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
			serviceManagementDTO.setStatusDesc(actionServiceDetailsDTO.getStatusDesc());
		}else
		{
			logger.warn(" Service : CreateLoyalty -- Transaction ID : "+serviceManagementDTO.getTransactionId()+" MoNumber "+serviceManagementDTO.getMoNumber()+""+ " No Key Defined in Service Status table for Key :- "+key);
			serviceManagementDTO.setStatusCode("SC0001");
			serviceManagementDTO.setStatusDesc(key);	
		}
		return serviceManagementDTO;
	}
	
	public CDRInformationDTO buildingServiceManagementCdrDtoAndWritingCdr(CDRInformationDTO cdrInformationDTO,ServiceManagementDTO serviceManagementDTO)
	{
		try {
			if(cdrInformationDTO!=null)
			{
			
			cdrInformationDTO.setCommandId(CDRCommandID.UpdateAccount.getId());
			cdrInformationDTO.setTransactionId(serviceManagementDTO.getTransactionId());
			//cdrInformationDTO.setTimeStamp(serviceManagementDTO.getTimestamp());
			cdrInformationDTO.setStatusCode(serviceManagementDTO.getStatusCode());
			//cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc());
			cdrInformationDTO.setSubscriberNumber(serviceManagementDTO.getMoNumber());
			cdrInformationDTO.setField7(serviceManagementDTO.getParentMsisdn());
			if(serviceManagementDTO.getChannel()!=null)
			{
				cdrInformationDTO.setChannelID(serviceManagementDTO.getChannel());
			}
			}else
			{   
				cdrInformationDTO = new CDRInformationDTO();
				cdrInformationDTO.setCommandId(CDRCommandID.UpdateAccount.getId());	
				cdrInformationDTO.setTransactionId(serviceManagementDTO.getTransactionId());
				cdrInformationDTO.setStatusCode(serviceManagementDTO.getStatusCode());
				cdrInformationDTO.setStatusDescription(serviceManagementDTO.getStatusDesc());
				cdrInformationDTO.setSubscriberNumber(serviceManagementDTO.getMoNumber());
				if(serviceManagementDTO.getChannel()!=null)
				{
					cdrInformationDTO.setChannelID(serviceManagementDTO.getChannel());
				}
			}
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		finally{
			CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
		}
		return cdrInformationDTO;
	}
	
	public boolean commonServiceUpdation(ServiceManagementDTO serviceManagementDTO,int statusId, Date expiryDate, String subscriberTableName, String loyaltyRegisteredTableName,long loyaltyId)
	{
		CommonUtilDAO commonUtilDAO = null;
		boolean subscriberTabUpdate = false;
		boolean loyaltyRegisteredTabUpdate = false;
		boolean customerProfileStatus = false;
		ServiceManagementDAO serviceManagementDAO = null;
		try {
			commonUtilDAO = new CommonUtilDAO();
			serviceManagementDAO = new ServiceManagementDAO();
			subscriberTabUpdate = serviceManagementDAO.updateCommonLoyaltyProfileSubscirberOperation(statusId,
					subscriberTableName, expiryDate, loyaltyId);
			if (subscriberTabUpdate) {
				logger.info("Transaction id " + serviceManagementDTO.getTransactionId()
						+ " Subscriber Table has updated " + subscriberTableName);
				loyaltyRegisteredTabUpdate = serviceManagementDAO.updateCommonLoyaltyProfileSubscirberOperation(
						statusId, loyaltyRegisteredTableName, expiryDate, loyaltyId);
			} else {
				logger.info("Transaction id " + serviceManagementDTO.getTransactionId()
						+ " Updation failed in Subscriber Table " + subscriberTableName);
			}
			if (loyaltyRegisteredTabUpdate) {
				logger.info("Transaction id " + serviceManagementDTO.getTransactionId()
						+ "LoyaltyRegistered Table has updated " + loyaltyRegisteredTableName);
				customerProfileStatus = serviceManagementDAO.updateNumberTerminationCustomerProfileTab(statusId,
						expiryDate, serviceManagementDTO.getMoNumber());
			} else {
				logger.info("Transaction id " + serviceManagementDTO.getTransactionId()
						+ " Updation failed in LoyaltyRegistered Table " + loyaltyRegisteredTableName);
				logger.info("Transaction id " + serviceManagementDTO.getTransactionId()
						+ " Updation failed in CustomerProfile Table " + loyaltyRegisteredTableName);
			}
			logger.info("Transaction id "+ serviceManagementDTO.getTransactionId()+" Status of update in CustomerProfile "+customerProfileStatus);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Transaction id " + serviceManagementDTO.getTransactionId() + "Exception " + e.getMessage());
		}
		return customerProfileStatus;
	}
	
	
	public List<LoyaltyProfileTabDTO> getLoyaltyProfileMsisdnCheck(ServiceManagementDTO serviceManagementDTO,String tableName)
	{
		TableDetailsDAO tableDetailsDAO = null;
		List<LoyaltyProfileTabDTO> loyaltyProfilelist = null;
		try {
			tableDetailsDAO = new TableDetailsDAO();
			logger.info("Fetching loyaltyProfile Information based on contactNumber ");
			loyaltyProfilelist = tableDetailsDAO.getLoyaltyProfileInfoBasedOnMsisdn(serviceManagementDTO, tableName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception is " + e);
		} finally {

		}
		return loyaltyProfilelist;
	}
	
	public static void main(String arg[])
	{
		List<LoyaltyProfileTabDTO> loyaltyInformationDetails=null;
		loyaltyInformationDetails=new ArrayList<LoyaltyProfileTabDTO>();
		System.out.println("Size "+loyaltyInformationDetails.size());
	}
	
	
	public CustomerProfileTabDTO buildingCustomerProfileDto(HashMap<String,String> customerDetails)
	{
		CustomerProfileTabDTO customerProfileTabDTO= null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			customerProfileTabDTO = new CustomerProfileTabDTO();
			if (customerDetails.containsKey(SystemConstants.FNAME))
				customerProfileTabDTO.setFirstName(customerDetails.get(SystemConstants.FNAME));
			if (customerDetails.containsKey(SystemConstants.LNAME))
				customerProfileTabDTO.setLastName(customerDetails.get(SystemConstants.LNAME));
			if (customerDetails.containsKey(SystemConstants.CRN))
				customerProfileTabDTO.setCrn(customerDetails.get(SystemConstants.CRN));
			if (customerDetails.containsKey(SystemConstants.ACCOUNT_NUM))
				customerProfileTabDTO.setAccountNo(customerDetails.get(SystemConstants.ACCOUNT_NUM));
			if (customerDetails.containsKey(SystemConstants.PERM_ADDRESS))
				customerProfileTabDTO.setAddress(customerDetails.get(SystemConstants.PERM_ADDRESS));
			if (customerDetails.containsKey(SystemConstants.PRIMARY_CONTACT_NUMBER))
				customerProfileTabDTO.setContactNumber(customerDetails.get(SystemConstants.PRIMARY_CONTACT_NUMBER));
			if (customerDetails.containsKey(SystemConstants.CATEGORY))
				customerProfileTabDTO.setCategory((customerDetails.get(SystemConstants.CATEGORY)));

			if (customerDetails.containsKey(SystemConstants.ACCOUNT_TYPE)) {
				customerProfileTabDTO.setAccountType(customerDetails.get(SystemConstants.ACCOUNT_TYPE));
				if (Cache.accountCategoryType.get(customerDetails.get(SystemConstants.ACCOUNT_TYPE)) != null) {
					customerProfileTabDTO.setAccountCategoryType(
							Cache.accountCategoryType.get(customerDetails.get(SystemConstants.ACCOUNT_TYPE)));
				}
			}
			if (customerDetails.containsKey(SystemConstants.ARA_FNAME))
				customerProfileTabDTO.setArabicFirstName(customerDetails.get(SystemConstants.ARA_FNAME));
			if (customerDetails.containsKey(SystemConstants.ARA_LNAME))
				customerProfileTabDTO.setArabicLastName(customerDetails.get(SystemConstants.ARA_LNAME));
			if (customerDetails.containsKey(SystemConstants.ARA_ADDRESS))
				customerProfileTabDTO.setArabicAddress(customerDetails.get(SystemConstants.ARA_ADDRESS));
			if (customerDetails.containsKey(SystemConstants.ARPU_BAND))
				customerProfileTabDTO.setArpuBand(customerDetails.get(SystemConstants.ARPU_BAND));
			if (customerDetails.containsKey(SystemConstants.TELECO_ADMIN))
				customerProfileTabDTO.setTelecoAdmin(customerDetails.get(SystemConstants.TELECO_ADMIN));
			if (customerDetails.containsKey(SystemConstants.BASE_RATE_PLAN))
				customerProfileTabDTO.setBaseRatePlan(customerDetails.get(SystemConstants.BASE_RATE_PLAN));
			if (customerDetails.containsKey(SystemConstants.BILL_CYCLE))
				customerProfileTabDTO.setBillCycle(customerDetails.get(SystemConstants.BILL_CYCLE));
			if (customerDetails.containsKey(SystemConstants.CONTRACT))
				customerProfileTabDTO.setContract(customerDetails.get(SystemConstants.CONTRACT));
			if (customerDetails.containsKey(SystemConstants.CREDIT_LIMIT))
				customerProfileTabDTO.setCreditLimit(customerDetails.get(SystemConstants.CREDIT_LIMIT));
			if (customerDetails.containsKey(SystemConstants.CREDIT_CLASS))
				customerProfileTabDTO.setCreditClass(customerDetails.get(SystemConstants.CREDIT_CLASS));
			if (customerDetails.containsKey(SystemConstants.CUSTOMER_SEGMENT))
				customerProfileTabDTO.setCustomerSegment(customerDetails.get(SystemConstants.CUSTOMER_SEGMENT));
			if (customerDetails.containsKey(SystemConstants.DOB))
				customerProfileTabDTO.setDob(customerDetails.get(SystemConstants.DOB));
			if (customerDetails.containsKey(SystemConstants.EMAIL))
				customerProfileTabDTO.setEmailId(customerDetails.get(SystemConstants.EMAIL));
			if (customerDetails.containsKey(SystemConstants.GENDER))
				customerProfileTabDTO.setGender(customerDetails.get(SystemConstants.GENDER));
			if (customerDetails.containsKey(SystemConstants.LANGUAGE))
				customerProfileTabDTO.setDefaultLanguage(customerDetails.get(SystemConstants.LANGUAGE));
			if (customerDetails.containsKey(SystemConstants.FRAUD_STATUS))
				customerProfileTabDTO.setStatusId(customerDetails.get(SystemConstants.FRAUD_STATUS));
			if (customerDetails.containsKey(SystemConstants.FREQ_USED_LOCATION))
				customerProfileTabDTO.setFrequentlyUsedLocation(customerDetails.get(SystemConstants.FREQ_USED_LOCATION));
			if (customerDetails.containsKey(SystemConstants.WEEKEND_LOCATION))
				customerProfileTabDTO.setWeekendLocation(customerDetails.get(SystemConstants.WEEKEND_LOCATION));
			if (customerDetails.containsKey(SystemConstants.LINE_TYPE))
				customerProfileTabDTO.setLineType(customerDetails.get(SystemConstants.LINE_TYPE));
			if (customerDetails.containsKey(SystemConstants.MOBILE_APP_SUBSCRIPTION_STATUS))
				customerProfileTabDTO.setMobileAppSubscriptionStatus(customerDetails.get(SystemConstants.MOBILE_APP_SUBSCRIPTION_STATUS));
			if (customerDetails.containsKey(SystemConstants.MSISDN))
				customerProfileTabDTO.setMsisdn(customerDetails.get(SystemConstants.MSISDN));
			if (customerDetails.containsKey(SystemConstants.NATIONALITY_ID))
				customerProfileTabDTO.setNationalId(customerDetails.get(SystemConstants.NATIONALITY_ID));
			if (customerDetails.containsKey(SystemConstants.NATIONALITY))
				customerProfileTabDTO.setNationality(customerDetails.get(SystemConstants.NATIONALITY));
			if (customerDetails.containsKey(SystemConstants.NATIONALITY_ID_TYPE))
				customerProfileTabDTO.setNationalIdType(customerDetails.get(SystemConstants.NATIONALITY_ID_TYPE));
			if (customerDetails.containsKey(SystemConstants.ACTIVATION_DATE))
				customerProfileTabDTO.setActivationDate(format.parse(customerDetails.get(SystemConstants.ACTIVATION_DATE)));

		} catch (Exception e) {
			logger.info("Exception e " + e.getMessage());
			e.printStackTrace();
		}
		return customerProfileTabDTO;
	}
	
	public ServiceManagementDTO deletingDataFromRegistrationAndPointsTables(ServiceManagementDTO serviceManagementDTO,CDRInformationDTO cdrInformationDTO)
	{
		ServiceManagementDAO  serviceManagementDAO=null;
        TableInfoDAO tableInfoDAO=null;
        CommonUtilDAO commonUtilDAO=null;
        SubscriberNumberTabDTO subscriberInfoDTO = null;
        ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
        long loyaltyId=0;
        String loyaltyTableName=null;
        String loyaltyRegisteredTableName=null;
        String subscriberTableName=null;
        String key=null;
        List<LoyaltyProfileTabDTO> loyaltyInformationDetails=null;
        HashMap<String,String> customerDetailsMap=null;
        CustomerProfileTabDTO customerProfileTabDTO=null;
        try{
		tableInfoDAO = new TableInfoDAO();
		serviceManagementDAO=new ServiceManagementDAO();
		commonUtilDAO=new CommonUtilDAO();
	  	boolean subscriberTabUpdate=false,loyaltyTabUpdate = false,loyaltyRegisteredTabUpdate=false,customerProfileTabUpdate=false,accountNumberTabStatus = false,onmDayWiseStatus=false,transactionStatus=false;
	    subscriberTableName = tableInfoDAO.getSubscriberNumberTable(serviceManagementDTO.getMoNumber());
		subscriberInfoList = commonUtilDAO.getSubscriberInformation(serviceManagementDTO.getTransactionId(),subscriberTableName,serviceManagementDTO.getMoNumber());
		if(subscriberInfoList != null && subscriberInfoList.size()!=0){
			subscriberInfoDTO = subscriberInfoList.get(0);
			loyaltyId = subscriberInfoDTO.getLoyaltyID();
			loyaltyTableName=tableInfoDAO.getLoyaltyProfileTable(loyaltyId+"");
			serviceManagementDTO.setLoyaltyId(loyaltyId+"");
			loyaltyRegisteredTableName=tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyId+"");
			subscriberTabUpdate=serviceManagementDAO.updateSubscirberChangeOfOwnderShip(serviceManagementDTO.getMoNumber(),subscriberTableName,serviceManagementDTO);
			if(subscriberTabUpdate)
			{
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId() +" Subscriber Table Update status "+subscriberTabUpdate);
			loyaltyRegisteredTabUpdate=serviceManagementDAO.updateLoyaltyRegisterChangeOfOwnderShip(serviceManagementDTO.getMoNumber(),loyaltyRegisteredTableName,serviceManagementDTO,loyaltyId);
			}
			loyaltyInformationDetails=getLoyaltyProfileMsisdnCheck(serviceManagementDTO,loyaltyTableName);
			if(loyaltyInformationDetails!=null && loyaltyRegisteredTabUpdate)
			{
				if(loyaltyInformationDetails.size()==1)
				{
					if(loyaltyInformationDetails.get(0).getTierPoints()!=null)
					cdrInformationDTO.setTierPoints(loyaltyInformationDetails.get(0).getTierPoints());
					if(loyaltyInformationDetails.get(0).getAccountType()!=null)
					cdrInformationDTO.setSubscriberType(loyaltyInformationDetails.get(0).getAccountType());					
					loyaltyTabUpdate=serviceManagementDAO.updateLoyaltyChangeOfOwnderShip(serviceManagementDTO.getMoNumber(),loyaltyTableName,serviceManagementDTO,loyaltyId);
				}
			}
			if(loyaltyRegisteredTabUpdate)
			{
				accountNumberTabStatus=serviceManagementDAO.deleteAccountTableDetails(serviceManagementDTO,loyaltyId);
				//transactionStatus=serviceManagementDAO.deleteTransactionsFromTable(serviceManagementDTO, loyaltyId);
				onmDayWiseStatus=serviceManagementDAO.deleteDayWisePointInfoTable(serviceManagementDTO, loyaltyId);
				customerProfileTabUpdate=serviceManagementDAO.deleteChangeOfOwnderShipCustomerProfileTab(serviceManagementDTO,loyaltyId);
			logger.info("Transaction Id "+serviceManagementDTO.getTransactionId() +" updated information in customerProfile tab update "+customerProfileTabUpdate +"Account Number Tab "+accountNumberTabStatus+" LoyaltyTransacionTab "+transactionStatus+" DayWiseTabStatus"+onmDayWiseStatus);
			}else
			{
				logger.info("Transaction Id "+serviceManagementDTO.getTransactionId() +" updated information in loyaltyRegistered Tab "+customerProfileTabUpdate);
			}
			logger.info("Transaction Id "+serviceManagementDTO.getTransactionId() +" Deletion from the table subscriberTableName"+subscriberTabUpdate +" loyaltyRegisteredTabUpdate "+loyaltyRegisteredTabUpdate+" loyaltyProfile "+loyaltyTabUpdate);
			if(customerProfileTabUpdate)
			{
				logger.info("All tables has updated the new msisdn");
				key = "SERVICE_MANAGEMENT_SERVICE_SUCCESS";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
			else
			{
				key = "SERVICE_MANAGEMENT_SERVICE_FAILURE_REVERT";
				serviceManagementDTO = customValidation(key, serviceManagementDTO);
			}
		}else
		{
			logger.info("TransactionId : "+serviceManagementDTO.getTransactionId()+" SubscriberNumber : "+serviceManagementDTO.getMoNumber()+" Message : This subscriber is not registered in loyalty platform .");
			key = "SERVICE_MANAGEMENT_SERVICE_NOT_REGISTERD";
			serviceManagementDTO = customValidation(key, serviceManagementDTO);
		}
        }catch(Exception e)
        {
        	logger.info("Exception is "+e);
        	serviceManagementDTO.setStatusCode("SC1000");
			serviceManagementDTO.setStatusDesc("FAILED "+e.getMessage());
			e.printStackTrace();
        }
        finally{
        	tableInfoDAO = null;
 			serviceManagementDAO=null;
 			commonUtilDAO=null;
        }
		return serviceManagementDTO;
	}
}
