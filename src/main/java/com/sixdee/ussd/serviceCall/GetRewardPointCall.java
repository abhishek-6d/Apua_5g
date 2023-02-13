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
import com.sixdee.ussd.webserviceHandler.PointManagementStub;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.GetPointDetails;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.GetPointDetailsResponse;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.PointInfoDTO;

/**
 * @author NITHIN KUNJAPPAN
 *
 */
public class GetRewardPointCall implements WSCallInter{
	
	private static final Logger logger = Logger.getLogger(GetRewardPointCall.class);

	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		PointManagementStub pointManagementStub = null;
		com.sixdee.ussd.webserviceHandler.PointManagementStub.PointDetailsDTO pointDetailsDTO = null;
		GetPointDetails getPointDetails = null;
		GetPointDetailsResponse getPointDetailsResp=null;
		PointInfoDTO pointInfoDto=null;
		Service reccomendation = null;
		ArrayList<Service> recoList = null;
		ArrayList<Parameters> paramList = null;
		ServiceList reccomendations = null;
		UssdResponseDTO ussdResponseDTO  = new UssdResponseDTO();
		String message = null;
		int langId = 1;
		try{
			pointDetailsDTO = new com.sixdee.ussd.webserviceHandler.PointManagementStub.PointDetailsDTO();
			recoList = new ArrayList<Service>();
			reccomendations = new ServiceList();
			pointDetailsDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			pointDetailsDTO.setChannel("USSD");
			pointDetailsDTO.setSubscriberNumber(ussdRequestDTO.getMsisdn());
			getPointDetails = new GetPointDetails();
			getPointDetails.setPointDetailsDTO(pointDetailsDTO);
			logger.info("Calling RewardPointAPI for TransactionId ["+ussdRequestDTO.getTransactionId()+"] traversal path reached ["+ussdRequestDTO.getTraversalPath()+"] URL ["+serviceRequestDTO.getUrl()+"]");
			pointManagementStub  =new PointManagementStub(serviceRequestDTO.getUrl());
			getPointDetailsResp = pointManagementStub.getPointDetails(getPointDetails);
			ussdResponseDTO = new UssdResponseDTO();
			pointInfoDto = getPointDetailsResp.get_return();
			if(pointInfoDto.getData()!=null)		
			{
				int i=0;
				while(i<pointInfoDto.getData().length)
				{
					if(pointInfoDto.getData()[i]!=null && pointInfoDto.getData()[i].getName()!=null && pointInfoDto.getData()[i].getName().equalsIgnoreCase("MESSAGE"))
					message=pointInfoDto.getData()[i].getValue();
					logger.info("Message ::"+message);
					i+=1;
				}
			}
			logger.info("TransactionId ["+ussdRequestDTO.getTransactionId()+"] TraversalPath ["+ussdRequestDTO.getTraversalPath()+"] Message : Recieved Response ["+pointInfoDto.getStatusDescription()+"] ");
			langId = ussdRequestDTO.getLangId();
			ussdResponseDTO.setStatus(pointInfoDto.getStatusCode());
			ussdResponseDTO.setStatusDesc(pointInfoDto.getStatusDescription());
			String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
			if(ussdResponseDTO.getStatus().equalsIgnoreCase("SC0000")){
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
				reccomendation.setMessageText(pointInfoDto.getStatusDescription());
			
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
			pointManagementStub = null;
		}
		return ussdResponseDTO;
	}
}
