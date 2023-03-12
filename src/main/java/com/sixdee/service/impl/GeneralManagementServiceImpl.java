package com.sixdee.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.ServiceManagermentBL;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.LoyaltyResponseDTO;
import com.sixdee.imp.dto.ServiceManagementDTO;
import com.sixdee.imp.service.GeneralManagement;

import com.sixdee.imp.service.serviceDTO.resp.ServiceManagementResponseDTO;
import com.sixdee.service.GeneralManagementService;

public class GeneralManagementServiceImpl implements GeneralManagementService {

	private final static Logger logger = Logger.getLogger(GeneralManagement.class);

	// CommonUtil commonUtil=new CommonUtil();
	// LanguageDAO languageDAO = new LanguageDAO();
	// SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
	// boolean check=false;

	public LoyaltyResponseDTO serviceManagement(String subscriberNumber, Map<String, String> headers,HttpServletResponse servletResponse) {
		LanguageDAO languageDAO = new LanguageDAO();
		LoyaltyResponseDTO managementResponseDTO = null;
		String langId = null;
		CommonUtil commonUtil = null;
		String txnId = null;
		GenericDTO genericDTO = new GenericDTO();

		long t1 = System.currentTimeMillis();
		ServiceManagementDTO managementRequestDTO = null;

		ServiceManagermentBL serviceManagermentBL = null;
		try {
			commonUtil = new CommonUtil();
			managementRequestDTO = new ServiceManagementDTO();
			managementResponseDTO = new LoyaltyResponseDTO();
			if (subscriberNumber != null && headers != null) {
				for (String key : headers.keySet()) {
					if (key.equalsIgnoreCase("X_CORRELATION_ID"))
						managementRequestDTO.setTransactionId(headers.get(key));
					if (key.equalsIgnoreCase("CHANNEL"))
						managementRequestDTO.setChannel(headers.get(key));
					if (key.equalsIgnoreCase("X_LANGUAGE"))
						managementRequestDTO.setLanguageId(headers.get(key));
				}
				if(subscriberNumber!=null){
					managementRequestDTO.setMoNumber((commonUtil.discardCountryCodeIfExists(subscriberNumber)));
				}
				
				txnId = managementRequestDTO.getTransactionId();
				logger.info("Service : GeneralManagement  TransactionID {} Request Recieved in System " + txnId+"SubscriberNumber:"+managementRequestDTO.getMoNumber());
			

				if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
					managementResponseDTO.setRespCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
					managementResponseDTO.setRespDesc(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());

				}

				if (managementRequestDTO.getLanguageId() == null
						|| managementRequestDTO.getLanguageId().trim().equals("")) {
					langId = Cache.cacheMap.get("DEFAULT_LANGUAGE_ID");
					managementRequestDTO.setLanguageId(langId);
				} else {
					langId = managementRequestDTO.getLanguageId();
				}

				serviceManagermentBL = new ServiceManagermentBL();
				int serviceIdentifier=Integer.valueOf(Cache.cacheMap.get("SERVICE_IDENTIFIER"));
				String fraudStatu=Cache.cacheMap.get("FRAUD_STATUS");
				String refNumber=Cache.cacheMap.get("REFERENCE_NUMBER");
				//Hard coded
				managementRequestDTO.setServiceIdentifier(3);
				managementRequestDTO.setFraudStatus("checked");
				managementRequestDTO.setReferreeNumber("8");
				genericDTO.setObj(managementRequestDTO);
				genericDTO = serviceManagermentBL.buildProcess(genericDTO);

				managementRequestDTO = (ServiceManagementDTO) genericDTO.getObj();
				long t3 = System.currentTimeMillis();

				managementResponseDTO.setRequestId(txnId);

				logger.info("Status code " + managementRequestDTO.getStatusCode());
				if (managementRequestDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
					logger.info("Status code " + genericDTO.getStatusCode());
					// managementRequestDTO = (ServiceManagementRequestDTO) genericDTO.getObj();
					managementResponseDTO.setRespCode(managementRequestDTO.getStatusCode());;
					managementResponseDTO.setRespDesc(managementRequestDTO.getStatusDesc());
					servletResponse.setStatus(HttpStatus.SC_OK);
				} else {
					managementResponseDTO=new LoyaltyResponseDTO();
					managementResponseDTO.setRespCode("SC001");
					managementResponseDTO.setRespDesc("FAILURE");
					servletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					
				}
				managementResponseDTO.setTimestamp(t3);
			} else {
				managementResponseDTO=new LoyaltyResponseDTO();
				managementResponseDTO.setRequestId(txnId);
				managementResponseDTO.setTimestamp(t1);
				managementResponseDTO.setRespCode("400");
				managementResponseDTO.setRespDesc("Missing Mandatory Parameters");
				servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
			}
			logger.info("Status code " + managementResponseDTO.getRespCode()+"responseDesc:"+managementResponseDTO.getRespDesc());
		}

		catch (Exception e) {
			logger.error(
					"Service : GeneralManagerment -ServiceManagerment - Transaction ID - {} Exception{}" + txnId + e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID - {} MoNumber {}  Request Leaving System , Processing Time {} " + txnId
					+ subscriberNumber + (t2 - t1));
			languageDAO = null;
			langId = null;
			serviceManagermentBL = null;
		}
		return managementResponseDTO;
	}

}
