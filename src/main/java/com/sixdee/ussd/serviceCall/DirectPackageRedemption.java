/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.Data;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantPackages;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantPackagesResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantRedemption;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantRedemptionResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantDetailsDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantInfoDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantPackageDetailsDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantRedemptionDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ResponseDTO;

/**
 * @author Bhavya
 *
 */
public class DirectPackageRedemption implements WSCallInter {

	private static final Logger logger = Logger.getLogger(MerchantInterface.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdee.ussd.util.inter.WSCallInter#callService(com.sixdee.ussd.dto
	 * .ServiceRequestDTO,
	 * com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO, java.lang.String)
	 */
	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		// TODO Auto-generated method stub

		UssdResponseDTO ussdResponseDTO = null;
		String transactionId = null;
		String timeStamp = null;
		String msisdn = null;
		String channel = null;
		String packageID = null;
		ArrayList<Parameters> values = null;
		String statusCode = null;
		String statusDesc = null;
		Data[] data = null;
		int langId=0;
		
		try {
		
			data = new Data[0];
			transactionId = ussdRequestDTO.getTransactionId();
			timeStamp = ussdRequestDTO.getTimeStamp();
			msisdn = ussdRequestDTO.getMsisdn();
			channel = ussdRequestDTO.getChannel();
			langId= ussdRequestDTO.getLangId();
			
			logger.info("Transaction id ["+transactionId+"]::Session Id ["+ussdRequestDTO.getSessionId()+"] Request recieved for Merchant ,calling url ["+serviceRequestDTO.getUrl()+"]");
			ussdResponseDTO  = new UssdResponseDTO();
			UserProfileManagementStub userProfileManagementStub = new UserProfileManagementStub(serviceRequestDTO.getUrl());
			GetMerchantRedemptionResponse getMerchantRedemptionResponse = null;
            GetMerchantRedemption getMerchantRedemption = new GetMerchantRedemption();
            MerchantRedemptionDTO merchantRedemptionDTO = new MerchantRedemptionDTO();
            ResponseDTO responseDTO = null;
            
            ArrayList<Service> serviceArray = new ArrayList<Service>();
            ServiceList serviceList = new ServiceList();

            
			
			Service service = ussdRequestDTO.getServiceList().getServices().get(0);
			values = service.getParamList();

			boolean promouser = false;
			boolean promoCode = true;
			for (Parameters param : values) {
				if (param.getId().equalsIgnoreCase("PackageId")) {
					packageID = param.getValue();
					logger.debug("Transaction id ["+transactionId+"]::Session Id ["+ussdRequestDTO.getSessionId()+"] ::Merchant id"+packageID);
				}
				
				
			}
           
			
			
			
			merchantRedemptionDTO.setTransactionId(transactionId);
			merchantRedemptionDTO.setTimestamp(timeStamp);
			merchantRedemptionDTO.setSubscriberNumber(msisdn);
			merchantRedemptionDTO.setChannel(channel);
			merchantRedemptionDTO.setMerchantId(packageID);
			
			if(promouser){
				Data datas = new Data();
				datas.setName("PackageRedemption");
				datas.setValue("true");
				data[0]=datas;
			}
			
			
			
			logger.info("The data array after inserting");
			
			merchantRedemptionDTO.setData(data);
			getMerchantRedemption.setMerchantRedemption(merchantRedemptionDTO);
		    getMerchantRedemptionResponse = userProfileManagementStub.getMerchantRedemption(getMerchantRedemption);
		    responseDTO = getMerchantRedemptionResponse.get_return();
		    responseDTO.getData();
		    statusCode = responseDTO.getStatusCode();
		    statusDesc = responseDTO.getStatusDescription();
		    
		  
		    
		    service.setMenuIndex("1");
		    service.setMessageText(statusDesc);
		    serviceArray.add(service);
		    serviceList.setServices(serviceArray);
		    ussdResponseDTO.setServiceList(serviceList);
		    logger.info("Transaction id ["+transactionId+"]::Session Id ["+ussdRequestDTO.getSessionId()+"]::Message text::"+statusDesc);
		    
		    
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Transaction id ["+transactionId+"]::Session Id ["+ussdRequestDTO.getSessionId()+"]Exception occured in try block]",e);
			
		}
		finally{
			
			    ussdResponseDTO.setEos(true);
			    ussdResponseDTO.setStatus(statusCode);
			    ussdResponseDTO.setStatusDesc(statusDesc);
		}

		return ussdResponseDTO;
	}

}
