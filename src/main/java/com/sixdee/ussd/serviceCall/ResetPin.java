/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ForgotPassword;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ForgotPasswordResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ResponseDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.UserProfileDTO;

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
public class ResetPin implements WSCallInter {

	private static final Logger logger = Logger.getLogger(ResetPin.class);
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO =  null;
		UserProfileManagementStub userProfileManagementStub = null;
		ForgotPassword forgotPassword = null;
		ForgotPasswordResponse forgotPasswordResponse = null;
		UserProfileDTO userProfileDTO = null;
		ResponseDTO responseDTO = null;
		Service respService = null;
		String url = null;
		String msisdn = null;
		String sessionId = null;
		String requestId = null; 
		String channel = null;
		long timeout = 0;
		ArrayList<Service> serviceList = null;
		ServiceClient serviceClient = null;
		Options options = null;
		ServiceList services = null;
		String userAcceptance = null;
		String userAcceptanceArabic = null;
		String userInputMenu = null;
		HashMap<String, String> ussdMap = null;
		boolean isAccepted = false;
		int langId = 0;
		String[] pathTraversals = null;
		String backOnNa = null;
		try{
			logger.info("in RESET CLASS==================");
			ussdMap = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
			url = serviceRequestDTO.getUrl();
			msisdn = ussdRequestDTO.getMsisdn();
			sessionId = ussdRequestDTO.getSessionId();
			requestId = ussdRequestDTO.getTransactionId();
			timeout = serviceRequestDTO.getTimeout();
			userInputMenu = ussdRequestDTO.getTraversalPath();
			langId= serviceRequestDTO.getLangId();
			if(userInputMenu.endsWith("UI")){
				
				userAcceptance = ussdMap.get("USER_ACCEPTANCE_ENG");
				userAcceptanceArabic = ussdMap.get("USER_ACCEPTANCE_OTH");
				logger.info("in USER CLASS=================="+userAcceptance+"====="+userAcceptanceArabic);
				if(response.equalsIgnoreCase(userAcceptance)||response.equalsIgnoreCase(userAcceptanceArabic)){
					isAccepted = true;
				}
			}
			logger.info("in ACCEPTED=================="+isAccepted);
			if(isAccepted){
				logger.info("RequestId ["+requestId+"] SessionId ["+sessionId+"] Message : Request recieved for Reset Pin , Calling url "+serviceRequestDTO.getUrl()+" for timeout "+timeout);
				channel = ussdRequestDTO.getChannel();
				long t1 = System.currentTimeMillis();
				userProfileManagementStub = new UserProfileManagementStub(url);
				serviceClient = userProfileManagementStub._getServiceClient();
				options = serviceClient.getOptions();
				options.setTimeOutInMilliSeconds(timeout>0?timeout:30000);
				//userProfileManagementStub.
				forgotPassword= new ForgotPassword();
				userProfileDTO = new UserProfileDTO();
				userProfileDTO.setChannel(channel);
				userProfileDTO.setTransactionID(requestId);
				//userProfileDTO.set
				userProfileDTO.setSubscriberNumber(msisdn);
				forgotPassword.setUserProfileDTO(userProfileDTO);
				forgotPasswordResponse = userProfileManagementStub.forgotPassword(forgotPassword);
				responseDTO=forgotPasswordResponse.get_return();
				//responseDTO.get
				long t2 = System.currentTimeMillis();
				logger.info("RequestId ["+requestId+"] SessionId ["+sessionId+"] Message : Request processed with in "+(t2-t1)+" seconds");
				ussdResponseDTO = new UssdResponseDTO();
				respService = new Service();
				respService.setMenuIndex(1);
				respService.setMessageText(responseDTO.getStatusDescription());
			}else{
				backOnNa = ussdMap.get("BACK_ON_NA");
				if(backOnNa!=null && backOnNa.equalsIgnoreCase("N")){
					respService = new Service();
					respService.setMenuIndex(1);
					respService.setMessageText(ussdMap.get("USER_NOT_ACCEPTED_"+langId));
				}else{
					GenerateServiceCall generateServiceCall = new GenerateServiceCall();
					pathTraversals = userInputMenu.split("/");
					ussdResponseDTO = new UssdResponseDTO();
					ussdResponseDTO = generateServiceCall.makeStaticResponse(ussdResponseDTO, Integer.parseInt(pathTraversals[0]), langId, false);
				}
			}
			if(respService!=null){
				serviceList = new ArrayList<Service>();
				serviceList.add(respService);
				services = new ServiceList();
				services.setServices(serviceList);
				ussdResponseDTO.setServiceList(services);
				ussdResponseDTO.setEos(true);
				
			}else{
				
			}
	
		}catch(Exception e){
			logger.error("RequestId ["+requestId+"] SessionId ["+sessionId+"] Message : Exception occured ",e);
			ussdResponseDTO.setStatus("SC0004");
			ussdResponseDTO.setStatusDesc(StatusCodes.SC0004);
			ussdResponseDTO.setEos(true);
		}finally{
			backOnNa = null;
			channel = null;
			forgotPassword = null;
			forgotPasswordResponse = null;
			msisdn = null;
			options = null;
			pathTraversals = null;
			requestId = null;
			response = null;
			responseDTO = null;
			respService = null;
			serviceClient = null;
			serviceList = null;
			serviceRequestDTO = null;
			services = null;
			sessionId = null;
			//timeout = null;
			url = null;
			userAcceptance = null;
			userAcceptanceArabic = null;
			userInputMenu = null;
			userProfileDTO = null;
			userProfileManagementStub = null;
			ussdMap = null;
			ussdRequestDTO = null;
		}
		return ussdResponseDTO;
	}
	public static void main(String[] args) {
		System.out.println();
		String[] abc = "52_32_24UI".split("_");
		System.out.println(abc[0]);
		System.out.println();
	}

}
