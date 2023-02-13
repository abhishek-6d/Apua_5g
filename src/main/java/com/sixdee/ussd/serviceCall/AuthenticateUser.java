/**
 * 
 */
package com.sixdee.ussd.serviceCall;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AuthenticateDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AuthenticateSubscriber;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AuthenticateSubscriberResponse;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.ResponseDTO;

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
public class AuthenticateUser implements WSCallInter {
	
	private static final Logger logger = Logger.getLogger(AuthenticateUser.class);
	

	/* (non-Javadoc)
	 * @see com.sixdee.ussd.util.inter.WSCallInter#callService(int, java.lang.String, com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO, java.lang.String)
	 */
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws CommonException {
		UssdResponseDTO ussdResponseDTO = null;
		AccountManagementStub accountManagementStub = null;
		AuthenticateSubscriber authenticateSubscriber = null;
		AuthenticateDTO authenticateDTO = null;
		AuthenticateSubscriberResponse authenticateSubscriberResponse = null;
		ArrayList<Parameters> paramList = null;
		
		try{
			//"http://10.0.0.39:8082/LMS_BL_1.0.1/services/AccountManagement"
			logger.info("Transaction id ["+ussdRequestDTO.getTransactionId()+"] Calling Authenticate User for ["+((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("AUTH_URL")+"] Response ["+response+"]");
			accountManagementStub = new AccountManagementStub(((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("AUTH_URL")!=null?((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("AUTH_URL"):"127.0.0.1:18181/LMS/Services/AccountManagement");//((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("AUTHENTICATION_URL"));
			authenticateSubscriber = new AuthenticateSubscriber();
			authenticateDTO = new AuthenticateDTO();
			authenticateSubscriber.setAuthenticateDTO(authenticateDTO);
			authenticateDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			authenticateDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			authenticateDTO.setSubscriberNumber(ussdRequestDTO.getMsisdn());
			
			paramList = (ussdRequestDTO.getServiceList().getServices().get(0).getParamList());
			for(Parameters param : paramList){
				if(param.getId().equalsIgnoreCase("UserData")){
					response = param.getValue();
				}
			}
			try{
			authenticateDTO.setPin(Integer.parseInt(response));
			}catch (NumberFormatException e) {
				logger.error("Number Expected String came ",e);
				authenticateDTO.setPin(0);
			}
			authenticateDTO.setChannel("USSD");
			long l1 = System.currentTimeMillis();
			authenticateSubscriberResponse = accountManagementStub.authenticateSubscriber(authenticateSubscriber);
			ResponseDTO responseDTO = authenticateSubscriberResponse.get_return();
			ussdResponseDTO = new UssdResponseDTO();
			ussdResponseDTO.setStatus(responseDTO.getStatusCode());
			ussdResponseDTO.setStatusDesc(responseDTO.getStatusDescription());
			long l2 = System.currentTimeMillis();
			
			logger.info("Time Taken For Authentication Call "+(l2-l1));
			if(!(ussdResponseDTO.getStatus().equalsIgnoreCase("SC0000"))){
				logger.debug("Validation Failed for requestId "+ussdRequestDTO.getTransactionId());
				ServiceList reco = new ServiceList();
				Service service = new Service();
				service.setMenuIndex(""+1);
				service.setMessageText(responseDTO.getStatusDescription());
				ArrayList<Service> serviceList = new ArrayList<Service>();
				serviceList.add(service);
				reco.setServices(serviceList);
				paramList = new ArrayList<Parameters>();
				Parameters parameters = new Parameters();
				parameters.setId("LanguageId");
				parameters.setValue(ussdRequestDTO.getLangId()+"");
				paramList.add(parameters);
				Parameters param2 = new Parameters();
				param2.setId("TYPE");
				param2.setValue(response);
				paramList.add(param2);
				service.setParamList(paramList);
				ussdResponseDTO.setEos(true);
				ussdResponseDTO.setServiceList(reco);
			}else{
				logger.info("User Validation is Success");
			}
			
		}catch (NumberFormatException e) {
			logger.error("Exception occured ",e);
		}
		catch (Exception e) {
			logger.error("Exception occured",e);
		
			throw new CommonException(e.getMessage());
		}finally{
			accountManagementStub = null;
			ussdResponseDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			
			ussdResponseDTO.setTimeStamp(ussdRequestDTO.getTimeStamp());
			ussdResponseDTO.setApplication("LMS");
			ussdResponseDTO.setMsisdn(ussdRequestDTO.getMsisdn());
			ussdResponseDTO.setSessionId(ussdRequestDTO.getSessionId());
			ussdResponseDTO.setStarCode(ussdRequestDTO.getStarCode());
			ussdResponseDTO.setTraversalPath(ussdRequestDTO.getTraversalPath()+"/0");	
		}
		return ussdResponseDTO;
	}

}
