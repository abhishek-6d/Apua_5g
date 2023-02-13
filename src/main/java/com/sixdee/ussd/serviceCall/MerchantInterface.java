/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.Data;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantRedemption;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantRedemptionResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantRedemptionDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ResponseDTO;

/**
 * @author Bhavya
 *
 */
public class MerchantInterface implements WSCallInter {

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
		String merchantId = null;
		ArrayList<Parameters> values = null;
		String statusCode = null;
		String statusDesc = null;
		Data[] data = null;
		int langId=0;
		
		try {
		
			data = new Data[2];
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
				if (param.getId().equalsIgnoreCase("MerchantId")) {
					merchantId = param.getValue();
					logger.debug("Transaction id ["+transactionId+"]::Session Id ["+ussdRequestDTO.getSessionId()+"] ::Merchant id"+merchantId);
				}
				if (param.getId().equalsIgnoreCase("MerchantType")) {
					if(param.getValue().equalsIgnoreCase("promo")){
						promouser = true;
					}
				}
				if (param.getId().equalsIgnoreCase("PromoCode")) {
					if(param.getValue().equalsIgnoreCase("false")){
						logger.info("PromoCode is false in the request");
						promoCode = false;
					}
				}
				
			}
           
			
			
			
			merchantRedemptionDTO.setTransactionId(transactionId);
			merchantRedemptionDTO.setTimestamp(timeStamp);
			merchantRedemptionDTO.setSubscriberNumber(msisdn);
			merchantRedemptionDTO.setChannel(channel);
			merchantRedemptionDTO.setMerchantId(merchantId);
			
			if(promouser){
				Data datas = new Data();
				datas.setName("TYPE");
				datas.setValue("PROMO");
				data[0]=datas;
			}
			if(!promoCode){
				logger.info("Promocode false");
				Data datas1 = new Data();
				datas1.setName("PROMO_CODE");
				datas1.setValue("FALSE");
				data[1]=datas1;
			}else{
				logger.info("Promocode true");
				Data datas1 = new Data();
				datas1.setName("PROMO_CODE");
				datas1.setValue("TRUE");
				data[1]=datas1;
			}
			if(promouser){
				Data datas = new Data();
				datas.setName("LANGUAGE_ID");
				datas.setValue(langId+"");
				data[2]=datas;
			}
			
			logger.info("The data array after inserting");
			/*for(int i=0;i<data.length;i++){
				logger.info("Name---->"+data[i].getName());
				logger.info("Value---->"+data[i].getValue());
			}*/
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
