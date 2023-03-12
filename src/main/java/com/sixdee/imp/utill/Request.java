package com.sixdee.imp.utill;




public class Request 
{
	
	private String requestId;
	private String msisdn;
	private String timeStamp;
	private String keyWord;
	private String scheduleId;
	private DataSet dataSet;
	
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
	public DataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	
	
}
