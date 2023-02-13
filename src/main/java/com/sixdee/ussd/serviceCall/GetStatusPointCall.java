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
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.GetTierDetails;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.GetTierDetailsResponse;
import com.sixdee.ussd.webserviceHandler.AccountManagementStub.TierDetailsDTO;

/**
 * @author NITHIN KUNJAPPAN
 *
 */
public class GetStatusPointCall implements WSCallInter{
	
	private static final Logger logger = Logger.getLogger(GetStatusPointCall.class);

	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		AccountManagementStub accManagementStub = null;
		Service reccomendation = null;
		ArrayList<Service> recoList = null;
		ArrayList<Parameters> paramList = null;
		ServiceList reccomendations = null;
		UssdResponseDTO ussdResponseDTO  = new UssdResponseDTO();
		String message = null;
		int langId = 1;
		
		try{
			accManagementStub = new com.sixdee.ussd.webserviceHandler.AccountManagementStub(serviceRequestDTO.getUrl());
			recoList = new ArrayList<Service>();
			reccomendations = new ServiceList();
			GetTierDetails tierdetails=new GetTierDetails();
			AccountDTO dto =new AccountDTO();
			
			dto.setMoNumber(ussdRequestDTO.getMsisdn());		
			dto.setChannel("USSD");
			dto.setTimestamp(ussdRequestDTO.getTimeStamp());
			dto.setTransactionId(ussdRequestDTO.getTransactionId());
			
			tierdetails.setAccountDTO(dto);
			
			logger.info("Calling StatusPointAPI for TransactionId ["+ussdRequestDTO.getTransactionId()+"] traversal path reached ["+ussdRequestDTO.getTraversalPath()+"] URL ["+serviceRequestDTO.getUrl()+"]");
			GetTierDetailsResponse getTierDetailsResp=accManagementStub.getTierDetails(tierdetails);
			
			TierDetailsDTO details= getTierDetailsResp.get_return();
			ussdResponseDTO = new UssdResponseDTO();
			
			if(details.getData()!=null)		
			{
				int i=0;
				while(i<details.getData().length)
				{
					if(details.getData()[i]!=null && details.getData()[i].getName()!=null && details.getData()[i].getName().equalsIgnoreCase("MESSAGE"))
					message=details.getData()[i].getValue();
					logger.info("Message ::"+message);
					i+=1;
				}
			}
			logger.info("TransactionId ["+ussdRequestDTO.getTransactionId()+"] TraversalPath ["+ussdRequestDTO.getTraversalPath()+"] Message : Recieved Response ["+details.getStatusDescription()+"] ");
			langId = ussdRequestDTO.getLangId();
			ussdResponseDTO.setStatus(details.getStatusCode());
			ussdResponseDTO.setStatusDesc(details.getStatusDescription());
			
			String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
			if (ussdResponseDTO.getStatus().equalsIgnoreCase("SC0000")) {
				reccomendation = new Service();
				reccomendation.setMenuIndex("" + 1);

				reccomendation.setMessageText(message);

				if (nextService != null)
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
				reccomendation.setMessageText(details.getStatusDescription());
			
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
			accManagementStub = null;
		}
		return ussdResponseDTO;
		
	}
}
