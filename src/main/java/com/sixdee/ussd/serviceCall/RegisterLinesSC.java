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
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountLineDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountStatusDTO;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.Data;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.GetRegisteredLines;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.GetRegisteredLinesResponse;

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
public class RegisterLinesSC implements WSCallInter {


	private Logger logger =Logger.getLogger(ViewAccountSC.class);
	/* (non-Javadoc)
	 * @see com.sixdee.ussd.util.inter.WSCallInter#callService(com.sixdee.ussd.dto.ServiceRequestDTO, com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO, java.lang.String)
	 */
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String responseFromUser) throws Exception {

		UssdResponseDTO ussdResponseDTO = null;
		AccountManagementStub accountManagementStub= null;
		AccountDTO accountDTO = null;
		String[] registerNumbers = new String[1] ;
		GetRegisteredLines viewAccounts = null;
		ArrayList<Service> recoList = new ArrayList<Service>();
		ServiceList serviceList = null;
		Service reco= null;
		Parameters param = null;
		ArrayList<Parameters> paramList = null;
		HashMap<String, String> ussdMap = null;
		boolean isEos = false;
		try{
			//ArrayList<String> serviceList = childServices.get(keywordMappingDTO.getServiceId());
			logger.info("Transaction id ["+ussdRequestDTO.getTransactionId()+"] Calling ViewAccount for ["+serviceRequestDTO.getUrl()+"] Response ["+responseFromUser+"]");
			/*if(ussdRequestDTO.getTraversalPath()==null || !(ussdRequestDTO.getTraversalPath().endsWith("_UI"))){
				responseFromUser = ussdRequestDTO.getMsisdn();
			}*/
			accountManagementStub = new AccountManagementStub(serviceRequestDTO.getUrl());
			accountDTO = new AccountDTO();
			accountDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			accountDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			//logger.info(responseFromUser);
			accountDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			accountDTO.setChannel(ussdRequestDTO.getChannel());
			try{
				Long.parseLong(responseFromUser);
			}catch (NumberFormatException e) {
				logger.info("Transacation Id :- "+ussdRequestDTO.getTransactionId()+" Session Id :- "+ussdRequestDTO.getSessionId()+" Message :- [Request recieved because of Back Option "+responseFromUser+"]");
				responseFromUser = ussdRequestDTO.getMsisdn();
			}
			registerNumbers[0] = responseFromUser;
			
			
				accountDTO.setRegisterNumbers(registerNumbers);
				viewAccounts = new GetRegisteredLines();
				viewAccounts.setAccountDTO(accountDTO);
				
				GetRegisteredLinesResponse viewResponse = accountManagementStub.getRegisteredLines(viewAccounts);
				AccountLineDTO accountLineDTO = viewResponse.get_return();
				ussdResponseDTO  =new UssdResponseDTO();
				int i =0;
				String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
					//logger.debug((accountLineDTO.getSubscriberNumbers()).length+"____________________________"+(accountLineDTO.getSubscriberNumbers()));
				
					if(accountLineDTO.getStatusCode().equalsIgnoreCase("SC0000")){
						if(accountLineDTO.getSubscriberNumbers()==null || accountLineDTO.getSubscriberNumbers().length==0){
							Service reccomendation = new Service();
							String messageText = null;
							for(Data d : accountLineDTO.getData()){
								if(d!=null && d.getName().equalsIgnoreCase("MESSAGE")){
									messageText = d.getValue();
									reccomendation.setMessageText(messageText);
									reccomendation.setDefaultOption(1);
									Parameters param1 = new Parameters();
									param1.setId("LanguageId");
									param1.setValue(ussdRequestDTO.getLangId()+"");
									paramList = new ArrayList<Parameters>();
									paramList.add(param1);
									recoList.add(reccomendation);
									ussdResponseDTO.setEos(true);
								}
							}
								
						
						}else{
						Service reccomendation = new Service();
						//reccomendation.setMenuIndex(""+1);
						logger.info("SELECT_LINES_HEADER_"+ussdRequestDTO.getLangId());
						reccomendation.setMessageText(((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("SELECT_LINES_HEADER_"+ussdRequestDTO.getLangId()));
						reccomendation.setDefaultOption(1);
						reccomendation.setNextService("1");
					
						recoList.add(reccomendation);
						
					
						for(AccountStatusDTO accountStatusDTO :accountLineDTO.getSubscriberNumbers()){
					
							reco = new Service();
							reco.setMenuIndex(""+(++i));
							reco.setMessageText(accountStatusDTO.getSubscriberNumber());
							reco.setNextService(nextService);
						
							param = new Parameters();
							param.setId("TYPE");
							param.setValue(accountStatusDTO.getTypeId()+"");
							paramList = new ArrayList<Parameters>();
							paramList.add(param);
							Parameters param1 = new Parameters();
							param1.setId("LanguageId");
							param1.setValue(ussdRequestDTO.getLangId()+"");
							paramList = new ArrayList<Parameters>();
							paramList.add(param);
							paramList.add(param1);
							
							reco.setParamList(paramList);
							recoList.add(reco);
						}
						
						
						ussdMap = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
						  Service backOption = new Service();
//						  backOption.setMenuIndex(""+ussdMap.get("Previous_Menu_Index"));
						  backOption.setMenuIndex(""+(++i));

						  backOption.setMessageText(ussdMap.get("Previous_Menu_String_"+ussdRequestDTO.getLangId()));
						  backOption.setNextService(ussdMap.get("Back2First_Service"));
						  recoList.add(backOption);
						}
			} else {
				 reco = new Service();
					
					reco.setMenuIndex(""+1);
					//messageTemplateDTO = messageMap.get(accountLineDTO.getStatusCode()+"_"+serviceRequestDTO.getServiceId());
					reco.setMessageText(accountLineDTO.getStatusDescription());
					Parameters param1 = new Parameters();
					param1.setId("LanguageId");
					param1.setValue(ussdRequestDTO.getLangId()+"");
					paramList = new ArrayList<Parameters>();
					paramList.add(param1);
					reco.setParamList(paramList);
					recoList.add(reco);
					
					ussdResponseDTO.setEos(true);
			}
					if(nextService == null)
						ussdResponseDTO.setEos(true);
					serviceList = new ServiceList();
					serviceList.setServices(recoList);
					ussdResponseDTO.setServiceList(serviceList);
					ussdResponseDTO.setStatus(accountLineDTO.getStatusCode());
					ussdResponseDTO.setStatusDesc(accountLineDTO.getStatusDescription());
	
				
		}catch (Exception e) {
			logger.error("Error occured", e);
			ussdResponseDTO.setStatus("SC0004");
			ussdResponseDTO.setStatusDesc(StatusCodes.SC0004);
			ussdResponseDTO.setEos(true);
			
		}finally{
			accountManagementStub = null;
		}
		return ussdResponseDTO;
	
	}


}
