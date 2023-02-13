/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.MessageTemplateDTO;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.RERequestDataSet;
import com.sixdee.ussd.dto.parser.RERequestHeader;
import com.sixdee.ussd.dto.parser.REResponseHeader;
import com.sixdee.ussd.dto.parser.ReResponseParameter;
import com.sixdee.ussd.dto.parser.in.Parameter;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.ThirdPartyCall;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.util.parser.ProvisionRequestParser;
import com.sixdee.ussd.util.parser.ProvisionResponseParser;

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
public class ProvisioningCall implements WSCallInter{

	
	private Logger logger = Logger.getLogger(ProvisioningCall.class); 
	
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		ArrayList<Parameter> paramList = null;
		Parameter parameter = null;
		String reqXml = null;
		ThirdPartyCall tpCall = null;
		RERequestHeader provReqHead = null;
		RERequestDataSet provReqDataSet = null;
		ArrayList<ReResponseParameter> provParamList = null;
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		HashMap<String, String> ussdMap=null;
		//Parameters param = null;
		try{
			ussdResponseDTO = new UssdResponseDTO();
			provReqHead = new RERequestHeader();
			provReqHead.setRequestId(ussdRequestDTO.getTransactionId());
			provReqHead.setKeyWord("REDEEM_PACKAGE");
			provReqHead.setMsisdn(ussdRequestDTO.getMsisdn());
			provReqHead.setTimeStamp(sdf.format(new Date()));
			ussdMap = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
			ReResponseParameter provReqParam = new ReResponseParameter();
			provReqParam.setId(ussdMap.get("COMPONENT_TAG")!=null ? ussdMap.get("COMPONENT_TAG"):"Component_Id");
			provReqParam.setValue(AppCache.appName);
			provParamList = new ArrayList<ReResponseParameter>();
			provParamList.add(provReqParam);
			
			
			paramList = new ArrayList<Parameter>();
			int i = 0;
			for(Parameters service : ussdRequestDTO.getServiceList().getServices().get(0).getParamList()){
				if(service.getId().trim().equalsIgnoreCase("parent")){
					i++;
				
					 provReqParam = new ReResponseParameter();
					provReqParam.setId(ussdMap.get("PACKAGE_TAG")!=null ? ussdMap.get("PACKAGE_TAG"):"Pack_Id");
					provReqParam.setValue(service.getValue());
					provParamList.add(provReqParam);
				}if(service.getId().trim().equalsIgnoreCase("TYPE")){
					i++;					 
					provReqParam = new ReResponseParameter();
					provReqParam.setId(ussdMap.get("TYPE_TAG")!=null ? ussdMap.get("TYPE_TAG"):"TYPE");
					provReqParam.setValue(service.getValue());
					provParamList.add(provReqParam);
				}
				if(i==2)
					break;
			}
			provReqDataSet = new RERequestDataSet();
			provReqDataSet.setResponseParam(provParamList);
			provReqHead.setDataSet(provReqDataSet);
			reqXml = ProvisionRequestParser.getInstanceReqStream().toXML(provReqHead);
			logger.info("Calling Provisioning module ["+serviceRequestDTO.getUrl()+"] ["+reqXml+"] for RequestId ["+ussdRequestDTO.getTransactionId()+"] ");
			tpCall = new ThirdPartyCall();
			String responseXml = tpCall.makeThirdPartyCall(reqXml, serviceRequestDTO.getUrl(), 5000);
			
			logger.info("Response recieved "+responseXml);
			String statusCode = "FC0004";
			if(responseXml == null){
				 REResponseHeader reResponseHeader = (REResponseHeader) ProvisionResponseParser.getInstanceReqStream().fromXML(responseXml);
				 statusCode = reResponseHeader.getStatus();
			}
			MessageTemplateDTO 	messageTemplateDTO = ((HashMap<String, MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).get(statusCode+"_"+serviceRequestDTO.getKeyWordId()+"_"+ussdRequestDTO.getLangId());
			
			Service service = new Service();
			
			service.setMessageText(messageTemplateDTO.getTemplateMessage());
			ArrayList<Service> serviceList = new ArrayList<Service>();
			serviceList.add(service);
			ServiceList sList = new ServiceList();
			sList.setServices(serviceList);
			ussdResponseDTO.setServiceList(sList);
			ussdResponseDTO.setEos(true);
			ussdResponseDTO.setStatus("SC0000");
			ussdResponseDTO.setStatusDesc("SUCCESS");
			
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}
		finally{
			ussdResponseDTO.setEos(true);
			ussdRequestDTO = null;
			serviceRequestDTO = null;
			response = null;
		}
		return ussdResponseDTO;
	}

}
