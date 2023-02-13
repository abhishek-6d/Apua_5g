/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class CommonRespDTO {

	private String txnId = null;
	private String timeStamp = null;
	private String statusCode = null;
	private String statusDescription = null;
	private String msisdn = null;
	private Parameters paramList = null;
	
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public Parameters getParamList() {
		return paramList;
	}
	public void setParamList(Parameters paramList) {
		this.paramList = paramList;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
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
	


}

