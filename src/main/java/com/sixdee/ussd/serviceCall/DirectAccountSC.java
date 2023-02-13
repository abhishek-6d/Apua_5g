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

public class DirectAccountSC implements WSCallInter {

	private Logger logger = Logger.getLogger(DirectAccountSC.class.getName());

	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO, UssdRequestDTO ussdRequestDTO, String responseFromUser) throws Exception 
	{
		UssdResponseDTO ussdResponseDTO = null;
		AccountManagementStub accountManagementStub = null;
		CreateLoyaltyAccount createLoyaltyAccount = null;
		AccountDTO accountDTO = null;
		String[] registerNumbers = new String[1];
		ArrayList<Service> recoList = new ArrayList<Service>();
		Service reco = null;
		ArrayList<Parameters> paramList = null;
		Parameters parameters = null;
		ServiceList serviceList = null;
		CreateLoyaltyAccountResponse createAccountResponse = null;
		ResponseDTO response = null;

		try 
		{
			logger.info("Transaction id [" + ussdRequestDTO.getTransactionId() + "] Calling CreateAccount for [" + serviceRequestDTO.getUrl() + "] With ALL Option");
			
			registerNumbers[0]="ALL";
			
			accountManagementStub = new AccountManagementStub(serviceRequestDTO.getUrl());
			accountDTO = new AccountDTO();
			accountDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			accountDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			accountDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			accountDTO.setChannel(ussdRequestDTO.getChannel());
			accountDTO.setRegisterNumbers(registerNumbers);

			createLoyaltyAccount = new CreateLoyaltyAccount();
			createLoyaltyAccount.setAccountDTO(accountDTO);

			createAccountResponse = accountManagementStub.createLoyaltyAccount(createLoyaltyAccount);
			response = createAccountResponse.get_return();
			
			reco = new Service();
			reco.setMenuIndex("" + 1);
			reco.setMessageText(response.getStatusDescription());

			paramList = new ArrayList<Parameters>();
			
			parameters = new Parameters();
			parameters.setId("LanguageId");
			parameters.setValue(ussdRequestDTO.getLangId() + "");
			paramList.add(parameters);
			
			reco.setParamList(paramList);
			
			recoList.add(reco);
			
			serviceList = new ServiceList(recoList);
			ussdResponseDTO = new UssdResponseDTO();
			ussdResponseDTO.setServiceList(serviceList);
			ussdResponseDTO.setEos(true);
			ussdResponseDTO.setStatus(response.getStatusCode());

		} 
		catch (Exception e) 
		{
			logger.error("Error occured", e);
			ussdResponseDTO.setStatus("SC0004");
			ussdResponseDTO.setStatusDesc(StatusCodes.SC0004);
			ussdResponseDTO.setEos(true);

		}
		finally
		{
			//accountManagementStub.
			//accountManagementStub.cleanup();
			accountManagementStub = null;
			createLoyaltyAccount = null;
			accountDTO = null;
			registerNumbers = null;
			recoList = null;
			reco = null;
			paramList = null;
			parameters = null;
			serviceList = null;
			
			createAccountResponse = null;
			response = null;
		}
		
		return ussdResponseDTO;
	}

	public static void main(String[] args) {
		System.out.println("9902390349(ACTIVE)".substring(0, "9902390349(ACTIVE)".indexOf("(")));
	}
}
