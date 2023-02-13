package com.sixdee.imp.utill.RuleEngine;




public class Request 
{
	
	private String requestId;
	private String msisdn;
	private String timeStamp;
	private String keyWord;
	private DataSets dataSets;
	
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
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public DataSets getDataSets() {
		return dataSets;
	}
	public void setDataSets(DataSets dataSets) {
		this.dataSets = dataSets;
	}
	
	
}
