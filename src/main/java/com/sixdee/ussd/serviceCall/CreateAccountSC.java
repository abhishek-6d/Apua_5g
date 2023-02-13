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
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.CreateLoyaltyAccount;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.CreateLoyaltyAccountResponse;
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
public class CreateAccountSC implements WSCallInter {
	
	private Logger logger = Logger.getLogger(CreateAccountSC.class);
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String responseFromUser) throws Exception {

		UssdResponseDTO ussdResponseDTO = null;
		AccountManagementStub accountManagementStub= null;
		CreateLoyaltyAccount createLoyaltyAccount = null;
		AccountDTO accountDTO = null;
		String[] registerNumbers = new String[1] ;
		ArrayList<Service> recoList = new ArrayList<Service>();
		Service reco = null;
		ArrayList<Parameters> paramList = null;
		
		try{
			//ArrayList<String> serviceList = childServices.get(keywordMappingDTO.getServiceId());
		//	if(logger.isDebugEnabled())
			if(responseFromUser.contains("(")){
				responseFromUser = responseFromUser.substring(0, responseFromUser.indexOf("(")).trim();
			}
			try{
			Long.parseLong(responseFromUser);	
			if(!responseFromUser.equalsIgnoreCase(ussdRequestDTO.getMsisdn())){
				registerNumbers[0] = responseFromUser;
				
			}
			}catch(NumberFormatException ne){
				logger.info("Exception occured Which implies All option is selected by user "+ne);
				registerNumbers[0] = "ALL";
			}
			logger.info("Transaction id ["+ussdRequestDTO.getTransactionId()+"] Calling CreateAccount for ["+serviceRequestDTO.getUrl()+"] Response ["+responseFromUser+"]");
			accountManagementStub = new AccountManagementStub(serviceRequestDTO.getUrl());
			accountDTO = new AccountDTO();
			accountDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			accountDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			//logger.info(responseFromUser);
			accountDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			accountDTO.setChannel(ussdRequestDTO.getChannel());
			accountDTO.setRegisterNumbers(registerNumbers);
			
			
			//	accountDTO.setRegisterNumbers(registerNumbers);
				createLoyaltyAccount = new CreateLoyaltyAccount();
				createLoyaltyAccount.setAccountDTO(accountDTO);
			
				CreateLoyaltyAccountResponse createAccountResponse = accountManagementStub.createLoyaltyAccount(createLoyaltyAccount);
				ResponseDTO response = createAccountResponse.get_return();
					 reco = new Service();

					reco.setMenuIndex(""+1);
					//messageTemplateDTO = messageMap.get(response.getStatusCode()+"_"+serviceRequestDTO.getServiceId());
					
					reco.setMessageText(response.getStatusDescription());
					//reco.setNextService(nextService);
					paramList = new ArrayList<Parameters>();
					Parameters parameters = new Parameters();
					parameters.setId("LanguageId");
					parameters.setValue(ussdRequestDTO.getLangId()+"");
					paramList.add(parameters);
					reco.setParamList(paramList);
					recoList.add(reco);
				ServiceList serviceList = new ServiceList(recoList);
				ussdResponseDTO = new UssdResponseDTO();
				ussdResponseDTO.setServiceList(serviceList);
				ussdResponseDTO.setEos(true);
				ussdResponseDTO.setStatus(response.getStatusCode());
				
				
			/*
			accountDTO = new AccountDTO();
			accountDTO.setTransactionId(ussdRequestDTO.getRequestId());
			accountDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			accountDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			//accountDTO.setPin(ussdRequestDTO.ge)
			accountDTO.setChannel("USSD");

			accountDTO.setRegisterNumbers(registerNumbers);
			//transactionManagementSoap11BindingStub.
			//accountManagementSoap11BindingStub.
			//ResponseDTO responseDTO = accountManagementSoap11BindingStub
			accountManagementStub.createLoyaltyAccount(createLoyaltyAccount2)
			ussdResponseDTO = new UssdResponseDTO();
			ussdResponseDTO.setRequestId(responseDTO.getTranscationId());
			ussdResponseDTO.setMsisdn(ussdRequestDTO.getMsisdn());
			reccomendations = new Reccomendations();
			ussdResponseDTO.setReccomendations(reccomendations);
			ArrayList<Reccomendation> recoList = new ArrayList<Reccomendation>();
			Data[] dataList= responseDTO.getData();
			if(responseDTO.getStatusCode().trim().equals("SC0000")){
				int i = 0;
				while(i<dataList.length){
					Data data = dataList[i];
					data.getName();
				}
			}
 			ussdResponseDTO.setStatus(responseDTO.getStatusCode());
		*/}catch (Exception e) {
			// TODO: handle exception
			logger.error("Error occured", e);
			ussdResponseDTO.setStatus("SC0004");
			ussdResponseDTO.setStatusDesc(StatusCodes.SC0004);
			ussdResponseDTO.setEos(true);
			
			
		}finally{
			accountManagementStub = null;
		}
		return ussdResponseDTO;
	
	}

	
	public static void main(String[] args) {
		System.out.println("9902390349(ACTIVE)".substring(0, "9902390349(ACTIVE)".indexOf("(")));
	}
}
