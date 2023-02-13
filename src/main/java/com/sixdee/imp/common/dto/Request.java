package com.sixdee.imp.common.dto;

import java.util.List;

public class Request 
{
	private String requestId;
	private String msisdn;
	private String keyword;
	private String featureId;
	private String timeStamp;
	private List<Param> dataSet;
	
	public List<Param> getDataSet() {
		return dataSet;
	}
	public void setDataSet(List<Param> dataSet) {
		this.dataSet = dataSet;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
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
}
