package com.sixdee.imp.request;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.ServiceManagementDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.ServiceManagementRequestDTO;
import com.sixdee.imp.util.CommonServiceConstants;



@SuppressWarnings("deprecation")
public class ServiceManagermentReqAssm extends ReqAssmGUICommon {
	
	
	private final static Logger logger = Logger.getLogger(ServiceManagermentReqAssm.class);
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		ServiceManagementDTO serviceManagementDTO = null;
		CommonUtil commonUtil=null;
		HashMap<String, String> dataSetMap=null;
		logger.info(" Class ==> ServiceManagermentReqAssm :: Method ==> buildAssembleGUIReq ");
		try{
			commonUtil=new CommonUtil();
			serviceManagementDTO=new ServiceManagementDTO();
			ServiceManagementRequestDTO generalServiceMangagmentRequestDTO=(ServiceManagementRequestDTO)genericDTO.getObj();
			
			/*String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());*/
			
			/*if(generalServiceMangagmentRequestDTO.getMoNumber()!=null && generalServiceMangagmentRequestDTO.getMoNumber().length()==subscriberSize)
				serviceManagementDTO.setMoNumber(subscriberCountryCode+generalServiceMangagmentRequestDTO.getMoNumber());
			else*/
				serviceManagementDTO.setMoNumber(generalServiceMangagmentRequestDTO.getMoNumber());
				serviceManagementDTO.setData(generalServiceMangagmentRequestDTO.getData());
			if(generalServiceMangagmentRequestDTO.getData()!=null)
			{

				serviceManagementDTO.setData(generalServiceMangagmentRequestDTO.getData());
				dataSetMap = convertDataToMap(serviceManagementDTO);
				if (dataSetMap != null)
					serviceManagementDTO.setDataMap(dataSetMap);

			}
			if(generalServiceMangagmentRequestDTO.getChannel()!=null && !generalServiceMangagmentRequestDTO.getChannel().equalsIgnoreCase(""))
				serviceManagementDTO.setChannel(generalServiceMangagmentRequestDTO.getChannel());
			
			if(generalServiceMangagmentRequestDTO.getLanguageId()!=null && !generalServiceMangagmentRequestDTO.getLanguageId().equalsIgnoreCase(""))
				serviceManagementDTO.setLanguageId(generalServiceMangagmentRequestDTO.getLanguageId());
			
			serviceManagementDTO.setServiceIdentifier(generalServiceMangagmentRequestDTO.getServiceIdentifier());
			serviceManagementDTO.setTransactionId(generalServiceMangagmentRequestDTO.getTransactionID());
			serviceManagementDTO.setTimestamp(generalServiceMangagmentRequestDTO.getTimestamp());
			
			logger.info("Service Identifier "+generalServiceMangagmentRequestDTO.getServiceIdentifier());
			
			 switch(generalServiceMangagmentRequestDTO.getServiceIdentifier()) {
		      case CommonServiceConstants.numberTermination: 
		    	  if(dataSetMap!=null && dataSetMap.containsKey(CommonServiceConstants.softDeleteParam))
		    	  serviceManagementDTO.setIsSoftDelete(dataSetMap.get(CommonServiceConstants.softDeleteParam));
		        break;
		      case CommonServiceConstants.mnp: 
		    	  if(dataSetMap!=null && dataSetMap.containsKey(CommonServiceConstants.isPortInParam))
		    	  serviceManagementDTO.setIsPortIn(dataSetMap.get(CommonServiceConstants.isPortInParam));
		        break;
		      case CommonServiceConstants.changeOfOwnership: 
		    	  logger.info("Change of OwnerShip Request Recieved {} "+generalServiceMangagmentRequestDTO.getServiceIdentifier());
		        break;
		      case CommonServiceConstants.msisdnChange:
		    	  if(dataSetMap!=null && dataSetMap.containsKey(CommonServiceConstants.changeMsisdnParam))
		    	  serviceManagementDTO.setChangeMsisdn(dataSetMap.get(CommonServiceConstants.changeMsisdnParam));
		        break;
		      case CommonServiceConstants.ratePlanChange: 
		    	  if(dataSetMap!=null)
		    	  {
		    	  if(dataSetMap.containsKey(CommonServiceConstants.oldRatePlanParam))
		    	  serviceManagementDTO.setOldRatePlan(dataSetMap.get(CommonServiceConstants.oldRatePlanParam));
		    	  
		    	  if(dataSetMap.containsKey(CommonServiceConstants.newRatePlanParam))
		    	  serviceManagementDTO.setNewRatePlan(dataSetMap.get(CommonServiceConstants.newRatePlanParam));
		    	  }
		        break;
		      case CommonServiceConstants.serviceMigration:
		    	  if(dataSetMap!=null)
		    	  {
		    	  if(dataSetMap.containsKey(CommonServiceConstants.oldContractTypeParam))
		    	  serviceManagementDTO.setOldContractType(dataSetMap.get(CommonServiceConstants.oldContractTypeParam));
		    	  
		    	  if(dataSetMap.containsKey(CommonServiceConstants.newContractTypeParam))
		    	  serviceManagementDTO.setNewContractType(dataSetMap.get(CommonServiceConstants.newContractTypeParam));
		    	  
		    	  if(dataSetMap.containsKey(CommonServiceConstants.oldAccountNumberParam))
		    	  serviceManagementDTO.setOldAccountNumber(dataSetMap.get(CommonServiceConstants.oldAccountNumberParam));
		    	  
		    	  if(dataSetMap.containsKey(CommonServiceConstants.newAccountNumberParam))
		    	  serviceManagementDTO.setNewAccountNumber(dataSetMap.get(CommonServiceConstants.newAccountNumberParam));

		    	  if(dataSetMap.containsKey(CommonServiceConstants.accountTypeParam))
		    	  serviceManagementDTO.setAccountType(dataSetMap.get(CommonServiceConstants.accountTypeParam));
		    	  }
		        break;
		      case CommonServiceConstants.mobileAppRegistration: 
		    	  if(dataSetMap!=null && dataSetMap.containsKey(CommonServiceConstants.referreNumberParam))
		    	  serviceManagementDTO.setReferreeNumber(dataSetMap.get(CommonServiceConstants.referreNumberParam));
		        break;
		      case CommonServiceConstants.fraudManagement: 
		    	  if(dataSetMap!=null && dataSetMap.containsKey(CommonServiceConstants.fraudStatusParam))
		    	  serviceManagementDTO.setFraudStatus(dataSetMap.get(CommonServiceConstants.fraudStatusParam));
		    	  
		    	  if(dataSetMap.containsKey(CommonServiceConstants.CustomerReferenceNumber))
			    	  serviceManagementDTO.setCustomerReferenceNumber(dataSetMap.get(CommonServiceConstants.CustomerReferenceNumber));
		        break;
		      case CommonServiceConstants.blacklistManagement: 
		    	  if(dataSetMap!=null && dataSetMap.containsKey(CommonServiceConstants.fraudStatusParam))
		    	  serviceManagementDTO.setFraudStatus(dataSetMap.get(CommonServiceConstants.fraudStatusParam));
		        break;
		      case CommonServiceConstants.newActivation: 
		    	  logger.info("TransactionId "+generalServiceMangagmentRequestDTO.getTransactionID() +" Request Reached for NewActivation");
		        break;
		      case CommonServiceConstants.updateTelecoAdmin: 
		    	  if(dataSetMap!=null)
		    	  {
		    	  if(dataSetMap.containsKey(CommonServiceConstants.parentMsisn))
		    	  serviceManagementDTO.setParentMsisdn(dataSetMap.get(CommonServiceConstants.parentMsisn));
		    	  }
		        break;
		      case CommonServiceConstants.updateContactNumber: 
		    	  if(dataSetMap!=null)
		    	  {
		    	  if(dataSetMap.containsKey(CommonServiceConstants.oldContactNumber))
		    	  serviceManagementDTO.setOldContactNumber(dataSetMap.get(CommonServiceConstants.oldContactNumber));
		    	  
		    	  if(dataSetMap.containsKey(CommonServiceConstants.newContactNumber))
		    	  serviceManagementDTO.setNewContactNumber(dataSetMap.get(CommonServiceConstants.newContactNumber));
		    	  }
		    	  
		        break;
		      default: logger.info("Service identified");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(serviceManagementDTO);
			serviceManagementDTO = null;
		}
		return genericDTO;
	}

	public HashMap<String, String> convertDataToMap(ServiceManagementDTO serviceManagementDTO)
	{
		CommonUtil commonUtil=null;
		HashMap<String, String> dataSetMap = null;
		try {
			commonUtil=new CommonUtil();
			dataSetMap=commonUtil.convertingDataToMap(serviceManagementDTO.getData());
		} catch (Exception e) {
			logger.info("Transaction id " + serviceManagementDTO.getTransactionId());
		}finally
		{
			commonUtil=null;
		}
		return dataSetMap;
	}
}
