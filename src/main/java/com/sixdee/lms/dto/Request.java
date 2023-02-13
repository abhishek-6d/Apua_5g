package com.sixdee.lms.dto;

import com.sixdee.imp.dto.parser.RERequestDataSet;

public class Request  {
	private String requestId   = null;
	private String timeStamp   = null;
	private String msisdn	   = null;
	private String keyWord	   = null;
	private String status 	   = null;
	private String statusDesc  = null;
	
	private RERequestDataSet dataSet    = null;
	
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public RERequestDataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(RERequestDataSet dataSet) {
		this.dataSet = dataSet;
	}


	
}
