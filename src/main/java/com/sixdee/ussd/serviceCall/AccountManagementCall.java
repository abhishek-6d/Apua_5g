/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountLineDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountStatusDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.CreateLoyaltyAccount;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.CreateLoyaltyAccountResponse;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.ResponseDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.ViewAccounts;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.ViewAccountsResponse;

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
public class AccountManagementCall implements WSCallInter {

	private static final Logger logger = Logger.getLogger(AccountManagementCall.class);
	@SuppressWarnings("unchecked")	
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,UssdRequestDTO ussdRequestDTO,String responseFromUser) {
		UssdResponseDTO ussdResponseDTO = null;
		AccountManagementStub accountManagementStub= null;
		ServiceList reccomendations = null;
		CreateLoyaltyAccount createLoyaltyAccount = null;
		AccountDTO accountDTO = null;
		String[] registerNumbers = new String[1] ;
		ViewAccounts viewAccounts = null;
		String serviceName = null;
		int serviceId = 0;
		ArrayList<Service> recoList = new ArrayList<Service>();
		
		try{
			//ArrayList<String> serviceList = childServices.get(keywordMappingDTO.getServiceId());
			serviceId = serviceRequestDTO.getKeyWordId();
			serviceName = serviceRequestDTO.getServiceName();
			accountManagementStub = new AccountManagementStub(serviceRequestDTO.getUrl());
			accountDTO = new AccountDTO();
			accountDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			accountDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			//logger.info(responseFromUser);
			accountDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			accountDTO.setChannel(ussdRequestDTO.getChannel());
			
			registerNumbers[0] = responseFromUser;
			
			
			if(serviceName.equalsIgnoreCase("CreateAccount")){
				accountDTO.setRegisterNumbers(registerNumbers);
				createLoyaltyAccount = new CreateLoyaltyAccount();
				createLoyaltyAccount.setAccountDTO(accountDTO);
			
				CreateLoyaltyAccountResponse createAccountResponse = accountManagementStub.createLoyaltyAccount(createLoyaltyAccount);
				ResponseDTO response = createAccountResponse.get_return();
				Service reco = new Service();
				
				reco.setMenuIndex(""+1);
				reco.setMessageText("Account has been created succesfully . You will recieve Notification shortly");
				//reco.setNextService(nextService);
				
				recoList.add(reco);
				ServiceList serviceList = new ServiceList(recoList);
				ussdResponseDTO = new UssdResponseDTO();
				ussdResponseDTO.setServiceList(serviceList);
				ussdResponseDTO = new UssdResponseDTO();
				ussdResponseDTO.setEos(true);
				ussdResponseDTO.setStatus(response.getStatusCode());
			}else if(serviceName.equalsIgnoreCase("ViewAccount")){
				accountDTO.setRegisterNumbers(registerNumbers);
				viewAccounts = new ViewAccounts();
				viewAccounts.setAccountDTO(accountDTO);
				
				ViewAccountsResponse viewResponse = accountManagementStub.viewAccounts(viewAccounts);
				AccountLineDTO accountLineDTO = viewResponse.get_return();
				
				ussdResponseDTO  =new UssdResponseDTO();
				int i =0;
				String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
				if(accountLineDTO.getStatusCode().equals("SC0000")){
					for(AccountStatusDTO accountStatusDTO :accountLineDTO.getSubscriberNumbers()){
					
					Service reco = new Service();
					reco.setMenuIndex(""+(++i));
					reco.setMessageText(accountStatusDTO.getSubscriberNumber());
					reco.setNextService(nextService);
					
					recoList.add(reco);

				}
				}else{
					//ArrayList<Service> recoList = new ArrayList<Service>();
					
					Service reco = new Service();
					
					reco.setMenuIndex(""+(++i));
					reco.setMessageText(accountLineDTO.getStatusDescription()!=null?accountLineDTO.getStatusDescription():"Unknown Failure");
					reco.setNextService(nextService);
					
					recoList.add(reco);
				
				}
				reccomendations = new ServiceList();
				reccomendations.setServices(recoList);
				ussdResponseDTO.setServiceList(reccomendations);
			
			}	
				
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
			
		}
		return ussdResponseDTO;
	}
 
}
