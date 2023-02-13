package com.sixdee.imp.service;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.ChurnPredictionSummaryDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.ChurnPredictionSummaryRequestDTO;
import com.sixdee.imp.service.serviceDTO.req.ServiceManagementRequestDTO;
import com.sixdee.imp.service.serviceDTO.resp.ChurnPredictionSummaryResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.ServiceManagementResponseDTO;

public class GeneralManagement {
	
	private final static Logger logger = Logger.getLogger(GeneralManagement.class);

	// CommonUtil commonUtil=new CommonUtil();
	// LanguageDAO languageDAO = new LanguageDAO();
	// SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
	// boolean check=false;
	public ChurnPredictionSummaryResponseDTO churnPredictionSummary(ChurnPredictionSummaryRequestDTO predictionSummaryDTO) {
		CommonUtil commonUtil = new CommonUtil();
		LanguageDAO languageDAO = new LanguageDAO();
		SubscriberListCheckDAO subslistDAO = new SubscriberListCheckDAO();
		boolean check = false;
		ChurnPredictionSummaryResponseDTO churnPredictionSummaryResponseDTO = null;
		String langId = null;
		LMSWebServiceAdapter adapter = null;
		ChurnPredictionSummaryDTO dto = null;
		Data[] datas = null;
		Data data = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		try {

			// logger.info("INSIDE SERVICE CLASS");
			subscriberNumber = predictionSummaryDTO.getSubscriberNumber();
			txnId = predictionSummaryDTO.getTransactionID();
			logger.info("Service : GeneralManagement  -- TRANSACTION ID : " + txnId + " SUBSCRIBER NUMBER: " + subscriberNumber + " Request Recieved in System");
			logger.debug("CHANNEL::" + predictionSummaryDTO.getChannel());
			logger.debug("Language ID ::" + predictionSummaryDTO.getLanguageId());

			langId = Cache.defaultLanguageID;
			boolean valid = false;

			churnPredictionSummaryResponseDTO = new ChurnPredictionSummaryResponseDTO();

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				churnPredictionSummaryResponseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				churnPredictionSummaryResponseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());

			}

			if (predictionSummaryDTO.getLanguageId() == null || predictionSummaryDTO.getLanguageId().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				predictionSummaryDTO.setLanguageId(langId);
			} else {
				langId = predictionSummaryDTO.getLanguageId();
			}

			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("ChurnPredictionSummary", predictionSummaryDTO);

			// UserDTO profileDTO=new UserDTO();
			churnPredictionSummaryResponseDTO.setTimestamp(predictionSummaryDTO.getTimestamp());
			churnPredictionSummaryResponseDTO.setTranscationId(txnId);

			if (genericDTO == null) {
				logger.info("##Failure###");
				// profileDTO.setStatusCode("SC1000");//profileDTO.setStatusDescription("FAILURE");

				churnPredictionSummaryResponseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHURN_PREDICTION_FAILURE_" + langId).getStatusCode());
				churnPredictionSummaryResponseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHURN_PREDICTION_FAILURE_" + langId).getStatusDesc());
				logger.info("DESCRIPTION>>>>>>>>" + churnPredictionSummaryResponseDTO.getStatusDescription());
				return churnPredictionSummaryResponseDTO;
			}

			else if (genericDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
				logger.info("##Success###");
				dto = (ChurnPredictionSummaryDTO) genericDTO.getObj();
				churnPredictionSummaryResponseDTO.setMsisdn(dto.getSubscriberNo());
				churnPredictionSummaryResponseDTO.setRangeID(dto.getRangeId());
				churnPredictionSummaryResponseDTO.setChurnCategory(dto.getChurnCategory());
				churnPredictionSummaryResponseDTO.setChurnChance(dto.getChurnChance());
				churnPredictionSummaryResponseDTO.setChurnStatus(dto.getChurnStatus());
				churnPredictionSummaryResponseDTO.setArpuBand(dto.getArpuBand());
				
				churnPredictionSummaryResponseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHURN_PREDICTION_SUCCESS_" + langId).getStatusCode());
				churnPredictionSummaryResponseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHURN_PREDICTION_SUCCESS_" + langId).getStatusDesc());
				
			}

			else {
				logger.info("Failure");
				churnPredictionSummaryResponseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHURN_PREDICTION_FAILURE_" + langId).getStatusCode());
				churnPredictionSummaryResponseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHURN_PREDICTION_FAILURE_" + langId).getStatusDesc());
				logger.info("DESCRIPTION>>>>>>>>" + churnPredictionSummaryResponseDTO.getStatusDescription());
			}

			logger.info("OUTSIDE SERVICE CLASS");
		} catch (Exception e) {
			logger.error("Service : GetUserProfile - Transaction ID : " + txnId + "Exception= ", e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + subscriberNumber + " Request Leaving System , Processing Time " + (t2 - t1));

			commonUtil = null;
			languageDAO = null;
			subslistDAO = null;
			langId = null;
			adapter = null;
			dto = null;
			datas = null;
			data = null;
		}
		return churnPredictionSummaryResponseDTO;

	}
	
	public ServiceManagementResponseDTO serviceManagement(ServiceManagementRequestDTO managementRequestDTO) {
		LanguageDAO languageDAO = new LanguageDAO();
		ServiceManagementResponseDTO managementResponseDTO = null;
		String langId = null;
		LMSWebServiceAdapter adapter = null;
		String txnId = null;
		String subscriberNumber = null;
		long t1 = System.currentTimeMillis();
		
		ServiceManagementResponseDTO serviceManagementResponseDTO = null;
		try {
			managementResponseDTO = new ServiceManagementResponseDTO();
			subscriberNumber = managementRequestDTO.getMoNumber();
			txnId = managementRequestDTO.getTransactionID();
			logger.info("Service : GeneralManagement  TransactionID {} SubscriberNumber {} Request Recieved in System "+subscriberNumber+txnId);
			logger.debug("channel Id {} "+managementRequestDTO.getChannel());
			logger.debug("Language ID {} "+managementRequestDTO.getLanguageId());
			langId = Cache.defaultLanguageID;

			if (subscriberNumber == null || subscriberNumber.trim().equalsIgnoreCase("")) {
				managementResponseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusCode());
				managementResponseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_" + langId).getStatusDesc());

			}

			if (managementRequestDTO.getLanguageId() == null || managementRequestDTO.getLanguageId().trim().equals("")) {
				langId = languageDAO.getLanguageID(subscriberNumber);
				managementRequestDTO.setLanguageId(langId);
			} else {
				langId = managementRequestDTO.getLanguageId();
			}
			
			serviceManagementResponseDTO=new ServiceManagementResponseDTO();
			adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("CommonServiceManagerment", managementRequestDTO);

			serviceManagementResponseDTO=(ServiceManagementResponseDTO)genericDTO.getObj();
			serviceManagementResponseDTO.setTimestamp(managementRequestDTO.getTimestamp());
			serviceManagementResponseDTO.setTranscationId(txnId);

			logger.info("Status code "+serviceManagementResponseDTO.getStatusCode());
			if (serviceManagementResponseDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
				logger.info("Status code "+genericDTO.getStatusCode());
				//managementRequestDTO = (ServiceManagementRequestDTO) genericDTO.getObj();
				serviceManagementResponseDTO.setStatusCode(serviceManagementResponseDTO.getStatusCode());
				serviceManagementResponseDTO.setStatusDescription(serviceManagementResponseDTO.getStatusDescription());
			}
			else {
				logger.info("Status code "+serviceManagementResponseDTO.getStatusCode());
				logger.info("Transaction id {} failure "+txnId);
				serviceManagementResponseDTO.setStatusCode(serviceManagementResponseDTO.getStatusCode());
				serviceManagementResponseDTO.setStatusDescription(serviceManagementResponseDTO.getStatusDescription());
				logger.info("Transaction id {} Description{} "+txnId+serviceManagementResponseDTO.getStatusDescription());
			}
		
		} catch (Exception e) {
			logger.error("Service : GeneralManagerment -ServiceManagerment - Transaction ID - {} Exception{}"+txnId+ e);
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID - {} MoNumber {}  Request Leaving System , Processing Time {} "+txnId+subscriberNumber+(t2 - t1));
			languageDAO = null;
			langId = null;
			adapter = null;
		}
		return serviceManagementResponseDTO;
	}
	
}//
