/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.parser.in.Parameter;
import com.sixdee.ussd.dto.KeywordMappingDTO;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;

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
public class GenerateServiceCall {

	private static final Logger logger = Logger.getLogger(GenerateServiceCall.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmSS");
	HashMap<Integer,ArrayList<String>> childServices = null;
	HashMap<String,ServiceRequestDTO> serviceMap  = null;
	public GenerateServiceCall() {
		childServices = (HashMap<Integer, ArrayList<String>>) AppCache.util.get("CHILD_RELATION");
		serviceMap = (HashMap<String, ServiceRequestDTO>) AppCache.util.get("SERVICE_CONFIG");
		
	}
	public UssdResponseDTO makeServiceCall(UssdRequestDTO ussdReqDTO, ArrayList<Object> objList){
		//ServiceResponseDTO serviceResponseDTO = null;
		HashMap<String,String> ussdManager = null;
		boolean isSuccess = true;
		boolean isUserInput =false;
		UssdResponseDTO ussdResponseDTO = null;
		KeywordMappingDTO keywordMappingDTO = null;
		ServiceRequestDTO serviceRequestDTO = null;
		int langId = 0;
		//boolean isBackOptionReqd = false;
		ServiceList serviceList = null;
		Service service = null;
		ArrayList<Service> services = null;
		String messageText = null;
		ArrayList<Parameters> parameters = null;
		boolean isBackOption = false;
		try{
			ussdManager = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
			keywordMappingDTO = (KeywordMappingDTO) objList.get(0);
			serviceList = ussdReqDTO.getServiceList();
			services = serviceList.getServices();
			service = services.get(0);
			messageText = service.getMessageText();
			parameters = service.getParamList();
			for(Parameters param : parameters){
				if(param.getId().equalsIgnoreCase("TYPE")){
					if(param.getValue().equalsIgnoreCase("B")){
						isBackOption = true;
					}
				}
			}
			langId = ussdReqDTO.getLangId();
			ussdResponseDTO  = makeCommonResponse(ussdReqDTO);
				//logger.debug("Call Method is  "+keywordMappingDTO.getServiceType());
			if(keywordMappingDTO.getServiceType().equalsIgnoreCase("S")){
				//Reccomendations reccomendations = new Reccomendations();
/*				serviceRequestDTO = serviceMap.get(keywordMappingDTO.getServiceId()+"");
				serviceRequestDTO.setKeyWordId(keywordMappingDTO.getId());
				serviceRequestDTO.setKeyword(keywordMappingDTO.getKeyWord());
*/				if(keywordMappingDTO.getBackOptionEnabled()==1)
					ussdResponseDTO = makeStaticResponse(ussdResponseDTO,keywordMappingDTO.getId(),langId,true);
				else
					ussdResponseDTO = makeStaticResponse(ussdResponseDTO,keywordMappingDTO.getId(),langId,false);

				}else if(keywordMappingDTO.getServiceType().trim().equalsIgnoreCase("D")){
					logger.info("in GENERATE CLASS==================");
					serviceRequestDTO = serviceMap.get(keywordMappingDTO.getServiceId()+"");
					serviceRequestDTO.setKeyWordId(keywordMappingDTO.getId());
					serviceRequestDTO.setKeyword(keywordMappingDTO.getKeyWord());
					serviceRequestDTO.setMenuDesc(keywordMappingDTO.getDesc());
					serviceRequestDTO.setLevel(keywordMappingDTO.getLevel());
					serviceRequestDTO.setLangId(keywordMappingDTO.getLangId());
					if(keywordMappingDTO.getBackOptionEnabled()==1)
						serviceRequestDTO.setBackOption(true);
					//if(logger.isDebugEnabled())
						logger.info("User Validation is enabled "+keywordMappingDTO.getAuthReqd());
					if(keywordMappingDTO.getAuthReqd()==1){
						boolean isAuthenticated = false;	
						//logger.info("Message Text "+messageText+" Comparable String "+ussdManager.get("Previous_Menu_String_"+ussdReqDTO.getLangId())+" Comaparison Result "+(messageText.equals(ussdManager.get("Previous_Menu_String_"+ussdReqDTO.getLangId()))));
						if(isBackOption || messageText.equals(ussdManager.get("Previous_Menu_String_"+ussdReqDTO.getLangId()))){
							isSuccess = true;
							isAuthenticated = true;
							logger.debug("RequestId "+ussdReqDTO.getTransactionId()+" Got back request");
						}else{
							//"PACK_ID"
						
						AuthenticateUser authenticateUser = new AuthenticateUser();
						ArrayList<Parameters> paramList = ussdReqDTO.getServiceList().getServices().get(0).getParamList();
						for(Parameters param: paramList){
							if(param.getId().equalsIgnoreCase("PACK_ID")){
								//isAuthenticated = true;
								isSuccess = true;
								break;
							}
						}
						UssdResponseDTO ussd = null;
						if(!isAuthenticated){
							 ussd = authenticateUser.callService(serviceRequestDTO, ussdReqDTO, (String)objList.get(1));
							 if(ussd.getStatus().equals("SC0000")){
								 logger.info("Valid User For Request ["+ussdReqDTO.getTransactionId()+"]");
								 objList.remove(1);
								 objList.add(ussdReqDTO.getMsisdn());
								 	//		isSuccess = true;
							} else {
								logger.info("InValid password For Request ["+ussdReqDTO.getTransactionId()+"]");
								
								ussdResponseDTO = ussd;
								ussdResponseDTO.setEos(true);
								isSuccess = false;
							}
						}
						}
					}
					if(isSuccess){
						UssdResponseDTO ussd = makeDyanamicResponse(serviceRequestDTO,ussdReqDTO,(String)objList.get(1));
						ussdResponseDTO.setServiceList(ussd.getServiceList());
						ussdResponseDTO.setStatus(ussd.getStatus());
						ussdResponseDTO.setStatusDesc(ussd.getStatusDesc());
						ussdResponseDTO.setEos(ussd.isEos());
						if(ussd.getTraversalPath()!=null)
							ussdResponseDTO.setTraversalPath(ussdResponseDTO.getTraversalPath()+"/"+ussd.getTraversalPath());
						//(ussd.getTraversalPath())!=null?ussdResponseDTO.setTraversalPath(ussdResponseDTO.getTraversalPath()+"_"+ussd.getTraversalPath():"";
					}
			}else if(keywordMappingDTO.getServiceType().equalsIgnoreCase("U")){
				if(keywordMappingDTO.getAuthReqd()==1){
					logger.info("Authenticating subscriber  For Request ["+ussdReqDTO.getTransactionId()+"]");
					AuthenticateUser authenticateUser = new AuthenticateUser();
					UssdResponseDTO ussd = authenticateUser.callService(serviceRequestDTO, ussdReqDTO, (String)objList.get(1));
					if(ussd.getStatus().equals("SC0000")){
						logger.info("Valid User For Request ["+ussdReqDTO.getTransactionId()+"]");
						
						ussdResponseDTO = makeUserInputReqdResponse(keywordMappingDTO,ussdResponseDTO,langId,(String)objList.get(1));
						//ussdResponseDTO.getServiceList().getServices().get(0).setParamList();
				//		isSuccess = true;
					}else{
						logger.info("InValid password For Request ["+ussdReqDTO.getTransactionId()+"]");
						
						ussdResponseDTO = ussd;
						ussdResponseDTO.setEos(true);
						isSuccess = false;
					}
				}else{
				
					ussdResponseDTO = makeUserInputReqdResponse(keywordMappingDTO,ussdResponseDTO,langId,service);
					//ussdResponseDTO.getServiceList().getServices().ad(ussdReqDTO.getServiceList());
					//Service s = ussdResponseDTO.getServiceList().getServices();
				}
			
				isUserInput = true;
			}else{/*
				if(serviceRequestDTO!=null && serviceRequestDTO.getCallReqd()==1){
					ussdResponseDTO = makeDyanamicResponse(serviceRequestDTO, ussdReqDTO, null);
					if(!(ussdResponseDTO.getStatus().equals("SC0000"))){
						isSuccess = false;
					}
				}
				if(isSuccess){
					if(keywordMappingDTO.getBackOptionEnabled()==1)
						ussdResponseDTO = makeStaticResponse(ussdResponseDTO, keywordMappingDTO.getId(),langId,true);
					else
						ussdResponseDTO = makeStaticResponse(ussdResponseDTO, keywordMappingDTO.getId(),langId,false);
					
				}
			*/}
			if(ussdReqDTO.getTraversalPath()!=null){
				ussdResponseDTO.setTraversalPath(ussdReqDTO.getTraversalPath()+"/"+keywordMappingDTO.getId()+(isUserInput?"_UI":""));
			}
			else
				ussdResponseDTO.setTraversalPath(keywordMappingDTO.getId()+(isUserInput?"_UI":""));
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			ussdResponseDTO.setStatus("FC0000");
			ussdResponseDTO.setStatusDesc("UNKNOWN_FAILURE");
			ussdResponseDTO.setEos(true);
			
		}
		return ussdResponseDTO;
	}
	private UssdResponseDTO makeUserInputReqdResponse(
			KeywordMappingDTO keywordMappingDTO, UssdResponseDTO ussdResponseDTO, int langId,Service service) throws Exception {
		ArrayList<Service> recoList = new ArrayList<Service>();
		ServiceList reccomendations = new ServiceList();
		Service reccomendation = new Service();
		ArrayList<Parameters> paramList = null;
	
		try{
			reccomendation.setMenuIndex(""+1);
			reccomendation.setMessageText(keywordMappingDTO.getResponseMenu());
			//logger.info(keywordMappingDTO.getResponseMenu());
			reccomendation.setNextService(keywordMappingDTO.getRespService());
			reccomendation.setDefaultOption(1);
			paramList = new ArrayList<Parameters>();
			Parameters param = new Parameters();
			param.setId("LanguageId");
			param.setValue(langId+"");
			paramList.add(param);
			if(service.getParamList()!=null)
			paramList.addAll(service.getParamList());
			//logger.info("Language")
			reccomendation.setParamList(paramList);
			recoList.add(reccomendation);
			reccomendations.setServices(recoList);
			ussdResponseDTO.setServiceList(reccomendations);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			throw e;
		}
		return ussdResponseDTO;
	}
	
	
	private UssdResponseDTO makeUserInputReqdResponse(
			KeywordMappingDTO keywordMappingDTO, UssdResponseDTO ussdResponseDTO, int langId,String pin) throws Exception {
		ArrayList<Service> recoList = new ArrayList<Service>();
		ServiceList reccomendations = new ServiceList();
		Service reccomendation = new Service();
		ArrayList<Parameters> paramList = null;
		try{
			reccomendation.setMenuIndex(""+1);
			reccomendation.setMessageText(keywordMappingDTO.getResponseMenu());
			//logger.info(keywordMappingDTO.getResponseMenu());
			reccomendation.setNextService(keywordMappingDTO.getRespService());
			reccomendation.setDefaultOption(1);
			paramList = new ArrayList<Parameters>();
			Parameters param = new Parameters();
			param.setId("LanguageId");
			param.setValue(langId+"");
			paramList.add(param);
			Parameters param1 = new Parameters();
			param1.setId("TYPE");
			param1.setValue(pin);
			paramList.add(param1);
			//logger.info("Language")
			reccomendation.setParamList(paramList);
			recoList.add(reccomendation);
			reccomendations.setServices(recoList);
			ussdResponseDTO.setServiceList(reccomendations);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			throw e;
		}
		return ussdResponseDTO;
	}
	
	public UssdResponseDTO makeStaticResponse(UssdResponseDTO ussdResponseDTO,int serviceId, int langId,boolean isBackEnabled) throws Exception {
		ArrayList<String> serviceList = childServices.get(serviceId);
		KeywordMappingDTO nextService = null;
		ArrayList <Service> recoList = null;
		ArrayList<Parameters> paramList = null;
		ServiceList reccomendations = null;
		HashMap<String, String> ussdMap = null;
		try{
			//ussdResponseDTO = new UssdResponseDTO();
			ussdMap = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
			if( serviceList != null){
				recoList = new ArrayList<Service>();
				int j = 0;
				for(String serviceName : serviceList){
					if(serviceName == null){
						logger.debug("Level1 Parent ["+serviceId+"]");
					}else{
						logger.debug(serviceName);
						nextService = ((HashMap<String, KeywordMappingDTO>) AppCache.util.get("KEYWORDS")).get(serviceName.toUpperCase().trim()+"_"+langId);
						Service reccomendation = new Service();
						//reccomendation.setApplication("USSD_BROWSER");
						reccomendation.setMenuIndex(""+(++j));
						reccomendation.setMessageText(nextService.getDesc());
						//if(childServices.get(nextService.getKeyWord())!=null)
							reccomendation.setNextService(nextService.getKeyWord());
							paramList = new ArrayList<Parameters>();
							Parameters param = new Parameters();
							param.setId("LanguageId");
							param.setValue(langId+"");
							paramList.add(param);
							//logger.info("Language")
							reccomendation.setParamList(paramList);
						//reccomendation.setTimeStamp(sdf.format(new Date()));
						recoList.add(reccomendation);
					}
				}
				if(isBackEnabled){
				  Service backOption = new Service();
				  backOption.setMenuIndex(""+(++j));
				  backOption.setMessageText(ussdMap.get("Previous_Menu_String"));
				  backOption.setNextService(ussdMap.get("Back2First_Service"));
				  recoList.add(backOption);
				}
				reccomendations = new ServiceList();
				reccomendations.setServices(recoList);
				ussdResponseDTO.setServiceList(reccomendations);
			}else{
			logger.debug("Service with No Child found LeafNode");
		}
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			throw e;
		}
		return ussdResponseDTO;
	}
	private UssdResponseDTO makeDyanamicResponse(
			ServiceRequestDTO serviceRequestDTO, UssdRequestDTO ussdReqDTO,
			String response) throws CommonException {
		String className = null;
		UssdResponseDTO ussdResponseDTO = null;
		try{
			className = serviceRequestDTO.getServiceClass();
			logger.info("Calling service class ["+className+"]");
		
			WSCallInter webServiceCaller = ((WSCallInter)Class.forName(className.trim()).newInstance());
			logger.info("****************************");
			ussdResponseDTO = webServiceCaller.callService(serviceRequestDTO,ussdReqDTO,response);
		
		}catch (Exception e) {
			logger.error("Exception occured ",e);
	//		throw new CommonException("Unknown Failure");
		}
		finally{
			
		}
		return ussdResponseDTO;
	}
	private UssdResponseDTO makeCommonResponse(UssdRequestDTO ussdReqDTO) {
		
		UssdResponseDTO ussdResponseDTO  =new UssdResponseDTO();
		ussdResponseDTO.setTimeStamp(ussdReqDTO.getTimeStamp());
		ussdResponseDTO.setSessionId(ussdReqDTO.getSessionId());
		ussdResponseDTO.setTransactionId(ussdReqDTO.getTransactionId());
		ussdResponseDTO.setMsisdn(ussdReqDTO.getMsisdn());
	
		ussdResponseDTO.setStatus("SC0000");
		ussdResponseDTO.setStatusDesc(StatusCodes.SC0000);
		return ussdResponseDTO;
	}
}
