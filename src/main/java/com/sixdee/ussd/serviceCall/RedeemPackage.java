/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.PointManagementStub;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.Data;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.RedeemDTO;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.RedeemPoints;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.RedeemPointsResponse;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.ResponseDTO;

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
public class RedeemPackage implements WSCallInter{

	Logger logger = Logger.getLogger(RedeemPackage.class.getClass());
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		Service service = null;
		ServiceList serviceList = null;
		ArrayList<Service> serviceOffered = null;
		ArrayList<Parameters> paramList = null;
		
		PointManagementStub pointManagementStub = null;
		RedeemPoints redeemPoints = null;
		RedeemDTO redeemDTO = null;
		String lineNo = null;
		String packId = null;
		String area = null;
		String location = null;
		int i = 0;
		try{
			logger.info("Calling LMS Redemption service ["+serviceRequestDTO.getUrl()+"] for Request ["+ussdRequestDTO.getTransactionId()+"]");
			pointManagementStub = new PointManagementStub(serviceRequestDTO.getUrl());
			redeemPoints = new RedeemPoints();
			redeemDTO = new RedeemDTO();
			redeemDTO.setMoNumber(ussdRequestDTO.getMsisdn());
			for(Parameters param : ussdRequestDTO.getServiceList().getServices().get(0).getParamList()){
				if(param.getId().trim().equalsIgnoreCase("TYPE")){
					lineNo = param.getValue().trim();
					
					ussdRequestDTO.setLangId(Integer.parseInt(lineNo.substring(lineNo.indexOf("_")+1,lineNo.length())));
					lineNo = lineNo.substring(0,lineNo.indexOf("_"));
					i++;
				}else if(param.getId().trim().equalsIgnoreCase("parent")){
					packId = param.getValue().trim();
					i++;
				}else if(param.getId().trim().equalsIgnoreCase("Area")){
					/*
					 * In case of unicode , there was an issue faced
					 * when request was coming back from ng , on xstream conversion unicode was
					 * getting converted to arabic . 
					 * To Avoid this we decided to replace #& with ABC when sending to ng and on the request
					 * to convert vice versa
					 */
					area = param.getValue().replaceAll("ABC", "&#");
					i++;
				}else if(param.getId().trim().equalsIgnoreCase("Location")){
					location = param.getValue().replaceAll("ABC", "&#");
					i++;
				}
				if(i==4)
					break;
			}
			logger.info("Number on which redemption is doing ["+lineNo+"] Pack ID to redeem ["+packId+"]");
			redeemDTO.setLineNumber(lineNo);
			//redeemDTO.setPin(param);
			redeemDTO.setPackID(Integer.parseInt(packId));
			redeemDTO.setChannel(ussdRequestDTO.getChannel()!=null?ussdRequestDTO.getChannel():"USSD");
			redeemDTO.setTransactionID(ussdRequestDTO.getTransactionId());
			if (area != null && location != null) {
				logger.debug("Adding area and location information");
				Data[] d = new Data[2];
				Data areaInfo = new Data();
				areaInfo.setName("Area");
				areaInfo.setValue(area);
				Data locationInfo = new Data();
				locationInfo.setName("Location");
				locationInfo.setValue(location);
				d[0] = areaInfo;
				d[1] = locationInfo;
				redeemDTO.setData(d);
			}
			redeemPoints.setRedeemDTO(redeemDTO);
		
			RedeemPointsResponse redeemPointsResponse= pointManagementStub.redeemPoints(redeemPoints);
			ResponseDTO responseDTO= redeemPointsResponse.get_return();
		
			
			ussdResponseDTO = new UssdResponseDTO();
			ussdResponseDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			ussdResponseDTO.setTimeStamp(ussdRequestDTO.getTimeStamp());
			ussdResponseDTO.setMsisdn(ussdRequestDTO.getMsisdn());
			ussdResponseDTO.setEos(true);
			service = new Service();
			service.setMessageText(responseDTO.getStatusDescription());
			serviceOffered = new ArrayList<Service>();
			serviceOffered.add(service);
			paramList = new ArrayList<Parameters>();
			Parameters parameters = new Parameters();
			parameters.setId("LanguageId");
			parameters.setValue(ussdRequestDTO.getLangId()+"");
			paramList.add(parameters);
			service.setParamList(paramList);
		
			serviceList = new ServiceList();
			serviceList.setServices(serviceOffered);
			ussdResponseDTO.setServiceList(serviceList);
			ussdResponseDTO.setStatus(responseDTO.getStatusCode());
			ussdResponseDTO.setStatusDesc(responseDTO.getStatusDescription());
		}
		finally{
			lineNo = null;
			packId = null;
			pointManagementStub = null;
			redeemDTO = null;
			redeemPoints = null;
			response = null;
			serviceOffered = null;
			serviceList = null;service = null;
			serviceRequestDTO = null;
			ussdRequestDTO = null;
			paramList = null;
		}
		return ussdResponseDTO;
	}

	public static void main(String[] args) {
		String abc = "9902390347_11";
		System.out.println(abc.substring(0,abc.indexOf("_")));
		System.out.println(abc.substring(abc.indexOf("_")+1,abc.length()));
	}
}
