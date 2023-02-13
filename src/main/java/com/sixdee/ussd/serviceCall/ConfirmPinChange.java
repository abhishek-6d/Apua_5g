/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.MessageTemplateDTO;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangePassword;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangePasswordDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangePasswordResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ResponseDTO;

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
public class ConfirmPinChange implements WSCallInter {

	private Logger logger = Logger.getLogger(ConfirmPinChange.class);
	/* (non-Javadoc)
	 * @see com.sixdee.ussd.util.inter.WSCallInter#callService(com.sixdee.ussd.dto.ServiceRequestDTO, com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO, java.lang.String)
	 */
	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		ServiceList serviceList = null;
		Service service = null;
		String pin = null;
		Service respService = null;
		ServiceList respMenu = null;
		ArrayList<Service> respServicesList = null;
		ArrayList<Parameters> respParamList = null;
		Parameters param1 = null;
		Parameters param2 = null;
		
		String nextService = null;
		UserProfileManagementStub userProfileManagementStub = null;
		ChangePassword changePassword = null;
		ChangePasswordDTO changePasswordDTO = null;
		ChangePasswordResponse changePasswordResponse = null;
		int newPin = 0;
		int cnfPin = 0;
		int oldPin = 0;
		boolean isValidPin = true;
		boolean isNumericalPin = true;
		HashMap<String,MessageTemplateDTO> propMap = null;
		try{
			logger.info("Transaction Id ["+ussdRequestDTO.getTransactionId()+"] Calling for ["+serviceRequestDTO.getUrl()+"]");
			propMap = (HashMap<String, MessageTemplateDTO>) AppCache.util.get("MESSAGE_TEMPLATE");
			ussdResponseDTO = new UssdResponseDTO();
				serviceList = ussdRequestDTO.getServiceList();
			service = serviceList.getServices().get(0);
			
			for(Parameters param : service.getParamList()){
				if(param.getId().equalsIgnoreCase("USERDATA")){
					try{
						cnfPin = Integer.parseInt(param.getValue());
					}catch(NumberFormatException ne){
						logger.error("Transaction Id ["+ussdRequestDTO.getTransactionId()+"] User Has to enter numerical for pin ["+param.getValue()+"]");
						isNumericalPin = false;
					}
					//break;
				}
				if(param.getId().equalsIgnoreCase("Pin")){
					try{
						
					newPin = Integer.parseInt(param.getValue());
					}catch(NumberFormatException ne){
						logger.error("Transaction Id ["+ussdRequestDTO.getTransactionId()+"] User Has to enter numerical for pin ["+param.getValue()+"]");
						isNumericalPin = false;
					}
					//break;
				}if(param.getId().equalsIgnoreCase("TYPE")){
					oldPin = Integer.parseInt(param.getValue());
			//		break;
				}
			}
			if(isNumericalPin && (newPin!=cnfPin)){
				logger.warn("Pin is not matching "+newPin+" Confirm Pin "+cnfPin+" "+(cnfPin==newPin));
				isValidPin = false;
			}
			if(!isNumericalPin){
				logger.warn("Pin is non numerical "+cnfPin);
				respService = new Service();
				respService.setMenuIndex(""+1);
				respService.setMessageText(propMap.get("SC0001_"+serviceRequestDTO.getKeyWordId()+"_"+serviceRequestDTO.getLangId()).getTemplateMessage());
			}else if(!isValidPin){
				respService = new Service();
				respService.setMenuIndex(""+1);
				respService.setMessageText(propMap.get("SC0002_"+serviceRequestDTO.getKeyWordId()+"_"+serviceRequestDTO.getLangId()).getTemplateMessage());
			}else{
			changePassword = new ChangePassword();
			changePasswordDTO = new ChangePasswordDTO();
			changePassword.setChangePasswordDTO(changePasswordDTO);
			changePasswordDTO.setChannel("USSD");
			changePasswordDTO.setTransactionID(ussdRequestDTO.getTransactionId());
			changePasswordDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			changePasswordDTO.setLanguageId(serviceRequestDTO.getLangId()+"");
			changePasswordDTO.setNewPin(newPin);
			changePasswordDTO.setConfirmPin(cnfPin);
			changePasswordDTO.setOldPin(oldPin);
			changePasswordDTO.setSubscriberNumber(ussdRequestDTO.getMsisdn());
			userProfileManagementStub = new UserProfileManagementStub(serviceRequestDTO.getUrl());
			changePasswordResponse = userProfileManagementStub.changePassword(changePassword);
			ResponseDTO changeResponse = changePasswordResponse.get_return();
			respService = new Service();
			respService.setMenuIndex(""+1);
			
			respService.setMessageText(changeResponse.getStatusDescription());
			}
			respServicesList = new ArrayList<Service>();
			respServicesList.add(respService);
			respMenu = new ServiceList();
			respMenu.setServices(respServicesList);
			ussdResponseDTO.setServiceList(respMenu);
		
			
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
		
	
		
	}
	return ussdResponseDTO;
	}

}
