package com.sixdee.lms.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.CommonUtil;

public class ResponseSetter {

	private static final Logger logger = LogManager.getLogger("ResponseSetter");
	
	private String statusCode=null;
	private String statusDescription=null;
	private String languageId=null;
	private String transactionId=null;
	
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public ResponseSetter customeValidation(String key, ResponseSetter responseSetter) throws CommonException {
		GenericDTO genericDTO = null;
		CommonUtil commonUtil = null;
		try {
			commonUtil = new CommonUtil();
			genericDTO = new GenericDTO();
			genericDTO = commonUtil.getStatusCodeDescription(genericDTO, key + responseSetter.getLanguageId(),
					responseSetter.getTransactionId());
			responseSetter.setStatusCode(genericDTO.getStatusCode());
			responseSetter.setStatusDescription(genericDTO.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Message " + e.getMessage());
		} finally {
			genericDTO = null;
			commonUtil = null;
		}
		return responseSetter;

	}
	
}
