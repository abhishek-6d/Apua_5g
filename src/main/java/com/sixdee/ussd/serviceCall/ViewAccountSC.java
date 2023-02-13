/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
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
public class ViewAccountSC implements WSCallInter {

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
		Data[] data = null;
		ViewAccounts viewAccounts = null;
		ArrayList<Service> recoList = new ArrayList<Service>();
		ServiceList serviceList = null;
		Service reco= null;
		Parameters param = null;
		ArrayList<Parameters> paramList = null;
		boolean isAllActive = true;
		String status = null;
		String subsNum = null;
		HashMap<String, String> ussdMap = null;
		int i =0;
		int j = 0;
		String langId = null;

		try{
			//ArrayList<String> serviceList = childServices.get(keywordMappingDTO.getServiceId());
			ussdMap = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
			
			logger.info("Transaction id ["+ussdRequestDTO.getTransactionId()+"] Calling ViewAccount for ["+serviceRequestDTO.getUrl()+"] Response ["+responseFromUser+"]");
			langId = ussdRequestDTO.getLangId()+"";
			if(ussdRequestDTO.getTraversalPath()==null || !(ussdRequestDTO.getTraversalPath().endsWith("_UI"))){
				responseFromUser = ussdRequestDTO.getMsisdn();
			}
			accountManagementStub = new AccountManagementStub(serviceRequestDTO.getUrl());
			accountDTO = new AccountDTO();
			accountDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			accountDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			//logger.info(responseFromUser);
			accountDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			accountDTO.setChannel(ussdRequestDTO.getChannel());
			
			registerNumbers[0] = responseFromUser;
			
			
				accountDTO.setRegisterNumbers(registerNumbers);
				viewAccounts = new ViewAccounts();
				viewAccounts.setAccountDTO(accountDTO);
				
				ViewAccountsResponse viewResponse = accountManagementStub.viewAccounts(viewAccounts);
				AccountLineDTO accountLineDTO = viewResponse.get_return();
				ussdResponseDTO  =new UssdResponseDTO();
				String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
					logger.debug((accountLineDTO.getSubscriberNumbers()).length+"____________________________"+(accountLineDTO.getSubscriberNumbers()));
					
					if(accountLineDTO.getStatusCode().equalsIgnoreCase("SC0000")){
						if(accountLineDTO.getSubscriberNumbers()==null){
							throw new CommonException("No data For user");
						}
						int length = accountLineDTO.getSubscriberNumbers().length;
						data = accountLineDTO.getData(); 
						if(data != null){
							for(Data d : data){
								if(d!=null){
									logger.debug(d.getName());
									if(d.getName().equalsIgnoreCase("MESSAGE")){
										logger.info("Message Tag is coming which implies this is an subscriber with more than 10 lines"+d.getValue());
										reco = new Service();
										reco.setMenuIndex(""+(++i));
										reco.setNextService("1");
										reco.setDefaultOption(1);
										reco.setMessageText(d.getValue());
										param = new Parameters();
										param.setId("LanguageId");
										param.setValue(langId);
										paramList = new ArrayList<Parameters>();
										paramList.add(param);
										reco.setParamList(paramList);
										recoList.add(reco);
										break;
									}
							}
						}}
						for(AccountStatusDTO accountStatusDTO :accountLineDTO.getSubscriberNumbers()){
							++j;
								logger.info(" Subscriber Number which is  ["+accountStatusDTO.getSubscriberNumber()+"] for Request Id ["+ussdRequestDTO.getTransactionId()+"] Status ["+accountStatusDTO.getStatus()+"]");
								status = accountStatusDTO.getStatus();
								subsNum = accountStatusDTO.getSubscriberNumber();
								if(isAllActive &&status !=null && !(Cache.getStatusMap().get("1_"+ussdRequestDTO.getLangId()).equalsIgnoreCase(status))){
									isAllActive = false;
								}
								++i;							
								reco = new Service();
								boolean isNum = true;
								reco.setMenuIndex(""+i);
								try{
									Long.parseLong(subsNum);
								}catch (NumberFormatException e) {
									isNum = false;
								}
								if(subsNum.equalsIgnoreCase("ALL") || isNum == false){
									reco.setMessageText(subsNum);
								}else{
									reco.setMessageText(subsNum+"("+status+")");
								}
								reco.setNextService(nextService);
								
								param = new Parameters();
								param.setId("TYPE");
								param.setValue(accountStatusDTO.getTypeId()+"");
								param = new Parameters();
								param.setId("LanguageId");
								param.setValue(langId);
								
								paramList = new ArrayList<Parameters>();
								paramList.add(param);
								reco.setParamList(paramList);
								recoList.add(reco);
								
							//	logger.info("Subscriber Number which is  active ["+accountStatusDTO.getSubscriberNumber()+"] ["+accountStatusDTO.getStatus()+"] for Request Id ["+ussdRequestDTO.getTransactionId()+"]");
							
						}

						
					
						if(isAllActive){
							ussdResponseDTO.setEos(false);
						}
						if(i==0){
							reco = new Service();
							reco.setMenuIndex(""+(++i));
							//reco.setMessageText(((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("NO_LINES_ADD_"+ussdRequestDTO.getLangId()));
							reco.setMessageText(accountLineDTO.getStatusDescription());
							recoList.add(reco);
							param = new Parameters();
							param.setId("LanguageId");
							param.setValue(ussdRequestDTO.getLangId()+"");
							
							paramList = new ArrayList<Parameters>();
							paramList.add(param);
							reco.setParamList(paramList);
							ussdResponseDTO.setEos(true);
						}else if(!isAllActive){
							
						}
			} else {
				 reco = new Service();
					
					reco.setMenuIndex(""+1);
					//messageTemplateDTO = messageMap.get(accountLineDTO.getStatusCode()+"_"+serviceRequestDTO.getServiceId());
					reco.setMessageText(accountLineDTO.getStatusDescription());
					recoList.add(reco);
					ussdResponseDTO.setEos(true);
			}
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
			if(!ussdResponseDTO.isEos()){
				Service backOption = new Service();
				backOption.setMenuIndex(""+((++i)));
				  backOption.setMessageText(ussdMap.get("Previous_Menu_String_"+ussdRequestDTO.getLangId()));
				  backOption.setNextService(ussdMap.get("Back2First_Service"));
				  param = new Parameters();
					param.setId("LanguageId");
					param.setValue(ussdRequestDTO.getLangId()+"");
					
					paramList = new ArrayList<Parameters>();
					paramList.add(param);
					backOption.setParamList(paramList);
				  recoList.add(backOption);
			}
			accountManagementStub = null;
		}
		return ussdResponseDTO;
	
	}

}
