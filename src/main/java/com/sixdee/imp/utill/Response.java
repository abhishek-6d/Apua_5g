package com.sixdee.imp.utill;

import com.sixdee.imp.utill.DataSets;


public class Response {


	private String requestId;
	private String msisdn;
	private String timeStamp;
	private String respCode;
	private String respDesc;
	private DataSets dataSets;
	private DataSet dataSet;
	private String keyWord;
	
	 
	
	
	
	public DataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the dataSet
	 */
	
	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}
	public DataSets getDataSets() {
		return dataSets;
	}
	public void setDataSets(DataSets dataSets) {
		this.dataSets = dataSets;
	}
	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	/**
	 * @return the respDesc
	 */
	public String getRespDesc() {
		return respDesc;
	}
	/**
	 * @param respDesc the respDesc to set
	 */
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	
	
	
	
}
