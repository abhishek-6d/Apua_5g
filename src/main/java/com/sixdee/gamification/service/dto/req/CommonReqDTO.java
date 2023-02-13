/**
 * 
 */
package com.sixdee.gamification.service.dto.req;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class CommonReqDTO {

	private String txnId = null;
	private String timeStamp = null;
	private String channel = null;
	private String msisdn = null;
	private Parameters paramList = null;
	
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
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
	
	


}

