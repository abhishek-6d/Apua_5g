package com.sixdee.imp.response;

import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.DataSets;

public class ResponseDTO {

	private String clientTxnId;
	private String msisdn;
	private String timeStamp;
	private String responseCode;
	private String responseFlag;
	private DataSet dataSets;
	private String scheduleId;
	public String getClientTxnId() {
		return clientTxnId;
	}
	public void setClientTxnId(String clientTxnId) {
		this.clientTxnId = clientTxnId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseFlag() {
		return responseFlag;
	}
	public void setResponseFlag(String responseFlag) {
		this.responseFlag = responseFlag;
	}
	public DataSet getDataSets() {
		return dataSets;
	}
	public void setDataSets(DataSet dataSets) {
		this.dataSets = dataSets;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	@Override
	public String toString() {
		return "ResponseDTO [clientTxnId=" + clientTxnId + ", msisdn=" + msisdn + ", timeStamp=" + timeStamp
				+ ", responseCode=" + responseCode + ", responseFlag=" + responseFlag + ", dataSets=" + dataSets
				+ ", scheduleId=" + scheduleId + "]";
	}
	
	
	
}
