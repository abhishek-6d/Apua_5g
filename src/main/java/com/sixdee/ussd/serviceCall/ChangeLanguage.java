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
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangeSubscriberLanguage;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangeSubscriberLanguageResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ResponseDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.SubscriberLanguageDTO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ChangeLanguage implements WSCallInter{

	private static Logger logger = Logger.getLogger(ChangeLanguage.class);
	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		UserProfileManagementStub userProfileManagementStub = null;
		ChangeSubscriberLanguage changeSubscriberLanguage = null;
		SubscriberLanguageDTO subscriberLanguageDTO = null;
		ChangeSubscriberLanguageResponse changeSubscriberLanguageResponse = null;
		ResponseDTO responseDTO = null;
		Service respService = null;
		ServiceList respServiceList = null;
		ArrayList<Parameters> paramList = null;
		ArrayList<Service> services = new ArrayList<Service>();
		Parameters parameter = null;
		int langId = 1;
		try{
			ussdResponseDTO = new UssdResponseDTO();
			logger.info("Transaction id ["+ussdRequestDTO.getTransactionId()+"] Request for change Language Calling ["+serviceRequestDTO.getUrl()+"]");
			userProfileManagementStub = new UserProfileManagementStub(serviceRequestDTO.getUrl());
			changeSubscriberLanguage = new ChangeSubscriberLanguage();
			subscriberLanguageDTO = new SubscriberLanguageDTO();
			subscriberLanguageDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			subscriberLanguageDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			subscriberLanguageDTO.setChannel("USSD");
			subscriberLanguageDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			//logger.debug("Language "+serviceRequestDTO.getLangId());
			for(Parameters param : ussdRequestDTO.getServiceList().getServices().get(0).getParamList()){
				if(param.getId().equalsIgnoreCase("LanguageId")){
					//objectList.add(param.getValue());
					langId = Integer.parseInt(param.getValue());
					break;
				}
			}
			subscriberLanguageDTO.setLanguageID(langId+"");
			subscriberLanguageDTO.setChangeLanguageID(langId==1?"11":"1");
			logger.debug(subscriberLanguageDTO.getChangeLanguageID());
			changeSubscriberLanguage.setSubscriberLanguageDTO(subscriberLanguageDTO);
			changeSubscriberLanguageResponse = userProfileManagementStub.changeSubscriberLanguage(changeSubscriberLanguage);
			responseDTO = changeSubscriberLanguageResponse.get_return();
			parameter = new Parameters();
			parameter.setId("LanguageId");
			parameter.setValue(subscriberLanguageDTO.getLanguageID());
			paramList = new ArrayList<Parameters>();
			paramList.add(parameter);
			respService = new Service();
			respService.setMessageText(responseDTO.getStatusDescription());
			respService.setMenuIndex(""+1);
			respService.setParamList(paramList);
			respServiceList = new ServiceList();
			services.add(respService);
	//		respServiceList.setServices(services);
			respServiceList.setServices(services);
			ussdResponseDTO.setServiceList(respServiceList);
			ussdResponseDTO.setStatus(responseDTO.getStatusCode());
			ussdResponseDTO.setStatusDesc(responseDTO.getStatusDescription());
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}finally{
			userProfileManagementStub = null;
			ussdResponseDTO.setEos(true);
			ussdResponseDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			
			ussdResponseDTO.setTimeStamp(ussdRequestDTO.getTimeStamp());
			ussdResponseDTO.setApplication("LMS");
			ussdResponseDTO.setMsisdn(ussdRequestDTO.getMsisdn());
			ussdResponseDTO.setSessionId(ussdRequestDTO.getSessionId());
			ussdResponseDTO.setStarCode(ussdRequestDTO.getStarCode());
			ussdResponseDTO.setTraversalPath(ussdRequestDTO.getTraversalPath()+"/"+serviceRequestDTO.getKeyWordId());
			ussdRequestDTO = null;
		}
		return ussdResponseDTO;
		
	}

}
