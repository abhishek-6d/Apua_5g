/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.ussd.dto.MessageTemplateDTO;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.TierLanguageMappingDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetUserProfile;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetUserProfileResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.UserDTO;
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
public class GetUserProfileCall implements WSCallInter{

	private static final Logger logger = Logger.getLogger(GetUserProfileCall.class);
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) {
		UserProfileManagementStub userProfileManagementStub = null;
		GetUserProfile getUserProfile = null;
		GetUserProfileResponse getUserProfileResponse = null;
		UserDTO userDTO = null;
		UserProfileDTO userProfileDTO = null;
		Service reccomendation = null;
		ArrayList<Service> recoList = null;
		ArrayList<Parameters> paramList = null;
		ServiceList reccomendations = null;
		UssdResponseDTO ussdResponseDTO  = new UssdResponseDTO();
		HashMap<String, String> tokenMap = null;
		HashMap<String, TierLanguageMappingDTO> tierMappingDTO = null;
		
		String message = null;
		TierInfoDTO tierInfoDTO = null;
		String statusPoints = null;
		int langId = 1;
		TierLanguageMappingDTO tierLanguageMappingDTO = null;
		try{
			userProfileDTO = new UserProfileDTO();
			recoList = new ArrayList<Service>();
			reccomendations = new ServiceList();
			userProfileDTO.setTransactionID(ussdRequestDTO.getTransactionId());
			userProfileDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			userProfileDTO.setChannel("USSD");
			userProfileDTO.setSubscriberNumber(ussdRequestDTO.getMsisdn());
//			userProfileDTO.setPin(Integer.parseInt(response));
			getUserProfile = new GetUserProfile();
			getUserProfile.setUserProfileDTO(userProfileDTO);
			logger.info("Calling GetUserProfile for TransactionId ["+ussdRequestDTO.getTransactionId()+"] traversal path reached ["+ussdRequestDTO.getTraversalPath()+"] URL ["+serviceRequestDTO.getUrl()+"]");
			userProfileManagementStub  =new UserProfileManagementStub(serviceRequestDTO.getUrl());
			getUserProfileResponse = userProfileManagementStub.getUserProfile(getUserProfile);
			ussdResponseDTO = new UssdResponseDTO();
			userDTO = getUserProfileResponse.get_return();
			logger.info("TransactionId ["+ussdRequestDTO.getTransactionId()+"] TraversalPath ["+ussdRequestDTO.getTraversalPath()+"] Message : Recieved Response ["+userDTO.getStatus()+"] ");
			langId = ussdRequestDTO.getLangId();
			ussdResponseDTO.setStatus(userDTO.getStatusCode());
			ussdResponseDTO.setStatusDesc(userDTO.getStatusDescription());
			String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
			if(ussdResponseDTO.getStatus().equalsIgnoreCase("SC0000")){
			//	tokenMap = new Converter().dtoToMapConverter(userDTO);
				statusPoints = userDTO.getStatusPoints();
				tokenMap = new HashMap<String, String>();
				tokenMap.put("ACCOUNT_NUMBER",userDTO.getAccountNumber());
				//tokenMap.
				Double rewPoints = Double.parseDouble(userDTO.getRewardPoints());
				tokenMap.put("REWARD_POINTS",rewPoints.intValue()+"");
				tokenMap.put("CATEGORY", userDTO.getCategory());
				Double statPoints = Double.parseDouble(statusPoints);
				tokenMap.put("STATUS_POINTS", statPoints.intValue()+"");
				tierMappingDTO = (HashMap<String, TierLanguageMappingDTO>) AppCache.util.get("TIER_LANGUAGE");
				tokenMap.put("TIER_NAME", tierMappingDTO.get(userDTO.getTier()+"_"+langId).getTierName());
				tokenMap.put("TIER_UPDATE_DATE", userDTO.getTierUpdateDate());
				tokenMap.put("STATUS_UPDATE_DATE", userDTO.getStatusUpdateDate());
				tierInfoDTO = Cache.tierInfoMap.get(Integer.parseInt(userDTO.getTier()));
				if(tierInfoDTO.getMaxValue()!=null)
					tokenMap.put("POINTS_TO_NEXT_TIER", ((tierInfoDTO.getMaxValue()+1)-(statPoints.intValue()))+"");
				tierInfoDTO = null;
				tierLanguageMappingDTO = tierMappingDTO.get((Integer.parseInt(userDTO.getTier()))+1+"_"+langId);
				tokenMap.put("NEXT_TIER",tierLanguageMappingDTO !=null? tierLanguageMappingDTO.getTierName():null);
				
				message = formMessage(serviceRequestDTO.getKeyWordId(),ussdRequestDTO.getLangId(),tokenMap);
				reccomendation = new Service();
				reccomendation.setMenuIndex(""+1);
				
				reccomendation.setMessageText(message);
			
			if(nextService!=null)
				reccomendation.setNextService(nextService);
			paramList = new ArrayList<Parameters>();
			Parameters param1 = new Parameters();
			param1.setId("LanguageId");
			param1.setValue(ussdRequestDTO.getLangId() + "");
			paramList.add(param1);
			reccomendation.setParamList(paramList);
			}else{
				reccomendation = new Service();
				reccomendation.setMenuIndex(""+1);
				reccomendation.setMessageText(userDTO.getStatusDescription());
			
			}
			recoList.add(reccomendation);
			reccomendations.setServices(recoList);
			ussdResponseDTO.setServiceList(reccomendations);
			ussdResponseDTO.setEos(true);
			
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			ussdResponseDTO.setStatus("SC0004");
			ussdResponseDTO.setStatusDesc(StatusCodes.SC0004);
			
		}finally{
			userProfileManagementStub = null;
		}
		return ussdResponseDTO;
		
	}
	private String formMessage(int serviceId, int langId,HashMap<String, String> tokenMap) {

		StringBuffer message = null;
		String replaceTag = null;
		String startDelimiter = ((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("S_DELIMITER");
		String endDelimiter   = ((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("E_DELIMITER");
		MessageTemplateDTO messageTemplateDTO = null;
			
		try{
			//processMap1 = processMap;
			//logger.info(processMap);
			logger.info(((HashMap<String,MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).keySet());
			//logger.info("SC0000_"+serviceId+"_"+langId);
			if(tokenMap.get("NEXT_TIER")!=null)
				message = new StringBuffer(((HashMap<String,MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).get("SC0000_"+serviceId+"_"+langId).getTemplateMessage());
			else
				message = new StringBuffer(((HashMap<String,MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).get("SC0006_"+serviceId+"_"+langId).getTemplateMessage());
			
			
			if (message != null) {
				
				int index = message.indexOf(startDelimiter);
				int endIndex = message.indexOf(endDelimiter, index + 1);
				while (index != -1) {
					replaceTag = message.substring(index + 1, endIndex)
							.toUpperCase();
					// String rp = messageMap.get(replaceTag);
				
					message.replace(index, endIndex + 1, tokenMap
							.get(replaceTag) != null ? tokenMap
							.get(replaceTag) : "");
					index = message.indexOf(startDelimiter);
					endIndex = message.indexOf(endDelimiter, index + 1);
				}

			}
			
		}catch (Exception e) {
			logger.error("Exception",e);
		}finally{
				
		}
		return message.toString();
	
	}

	
	public UserDTO getUserDetails(UssdRequestDTO ussdRequestDTO,String serviceUrl){
		UserProfileDTO userProfileDTO = null;
		GetUserProfile getUserProfile = null;
		UserProfileManagementStub userProfileManagementStub = null;
		GetUserProfileResponse getUserProfileResponse = null;
		UserDTO userDTO = null;
		try{
		userProfileDTO = new UserProfileDTO();
			userProfileDTO.setTransactionID(ussdRequestDTO.getTransactionId());
			userProfileDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			userProfileDTO.setChannel("USSD");
			userProfileDTO.setSubscriberNumber(ussdRequestDTO.getMsisdn());
//			userProfileDTO.setPin(Integer.parseInt(response));
			getUserProfile = new GetUserProfile();
			getUserProfile.setUserProfileDTO(userProfileDTO);
			logger.info("Calling GetUserProfile for TransactionId ["+ussdRequestDTO.getTransactionId()+"] traversal path reached ["+ussdRequestDTO.getTraversalPath()+"] URL ["+serviceUrl+"]");
			userProfileManagementStub  =new UserProfileManagementStub(serviceUrl);
			getUserProfileResponse = userProfileManagementStub.getUserProfile(getUserProfile);
			userDTO = getUserProfileResponse.get_return();
		} catch (AxisFault e) {
			logger.error("Axis exception occured , Please contact BL Support team ",e);
		} catch (RemoteException e) {
			logger.error("Remote exception occured , Most probably can be of service is down , if not please contacl BL Support Team");
		}catch (Exception e) {
			logger.error("Critical Exception occured ",e);
		}finally{
			userProfileDTO = null;
			ussdRequestDTO = null;
			getUserProfile = null;
			userProfileManagementStub = null;
			getUserProfileResponse = null;
		}
		return userDTO; 
	}
	
}
