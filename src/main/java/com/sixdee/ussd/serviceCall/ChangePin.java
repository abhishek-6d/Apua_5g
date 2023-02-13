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
public class ChangePin implements WSCallInter {

	
	private Logger logger = Logger.getLogger(ChangePin.class);
	/* (non-Javadoc)
	 * @see com.sixdee.ussd.util.inter.WSCallInter#callService(com.sixdee.ussd.dto.ServiceRequestDTO, com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO, java.lang.String)
	 */
	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		ServiceList serviceList = null;
		Service service = null;
		String pin = null;
		Service respService = null;
		ServiceList respMenu = null;
		ArrayList<Service> respServicesList = null;
		ArrayList<Parameters> respParamList = null;
		Parameters param1 = null;
		Parameters param2 = null;
		
		String nextService = null;
		try{

			ussdResponseDTO = new UssdResponseDTO();
			ussdResponseDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			ussdResponseDTO.setTimeStamp(ussdRequestDTO.getTimeStamp());
			ussdResponseDTO.setApplication("LMS");
			ussdResponseDTO.setMsisdn(ussdRequestDTO.getMsisdn());
			ussdResponseDTO.setSessionId(ussdRequestDTO.getSessionId());
			ussdResponseDTO.setStarCode(ussdRequestDTO.getStarCode());
			ussdResponseDTO.setTraversalPath(ussdRequestDTO.getTraversalPath()+"/"+serviceRequestDTO.getKeyWordId());
			serviceList = ussdRequestDTO.getServiceList();
			service = serviceList.getServices().get(0);
			for(Parameters param : service.getParamList()){
				if(param.getId().equalsIgnoreCase("USERDATA")){
					pin = param.getValue();
					break;
				}
			}
			logger.info("Pin recieved "+pin);			
			nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
			respService = new Service();
			respService.setMenuIndex(""+1);
			respService.setMessageText(serviceRequestDTO.getMenuDesc());
			respService.setNextService(nextService);
			respService.setDefaultOption(1);
			respParamList = new ArrayList<Parameters>();
			param1 = new Parameters();
			param1.setId("Pin");
			param1.setValue(pin);
			respParamList.add(param1);
			param2 = new Parameters();
			param2.setId("LanguageId");
			param2.setValue(serviceRequestDTO.getLangId()+"");
			respParamList.add(param2);
			respMenu = new ServiceList();
			respService.setParamList(respParamList);
			respServicesList = new ArrayList<Service>();
			respServicesList.add(respService);
			respMenu.setServices(respServicesList);
			ussdResponseDTO.setServiceList(respMenu);
			

		}catch(Exception e){
			logger.error("Exception occured ",e);
		}finally{
			nextService = null;
			param1 = null;
			param2 = null;
			pin = null;
			respParamList = null;
			respService = null;
			respServicesList = null;
			service = null;
			serviceList = null;
			serviceRequestDTO = null;
			ussdRequestDTO = null;
		}
		return ussdResponseDTO;
	}

}
