package com.sixdee.imp.service.httpcall.dto;

public class SubscriberRequest 
{
	private String requestId;
	private String msisdn;
	private String keyWord;
	private String featureId;
	private String timeStamp;
	private SubscriberDataSet dataSet;
	private String status = null;
	private String statusDesc = null;
	private boolean  isSync= false;
	private Object obj = null;
	
	

	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public boolean isSync() {
		return isSync;
	}
	public void setSync(boolean isSync) {
		this.isSync = isSync;
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
	public SubscriberDataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(SubscriberDataSet dataSet) {
		this.dataSet = dataSet;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getKeyword() {
		return keyWord;
	}
	public void setKeyword(String keyword) {
		this.keyWord = keyword;
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
