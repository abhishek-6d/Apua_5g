/**
 * 
 */
package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class ReferralRequestDTO {
	
	private String transactionId;
	private String timestamp;
	private String channel;
	private Data[] data;
	private String referralDate;
	private String referrer;
	private String referee;	
	
	/**
	 * @return the referralDate
	 */
	public String getReferralDate() {
		return referralDate;
	}
	/**
	 * @param referralDate the referralDate to set
	 */
	public void setReferralDate(String referralDate) {
		this.referralDate = referralDate;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the data
	 */
	public Data[] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Data[] data) {
		this.data = data;
	}
	/**
	 * @return the referrer
	 */
	public String getReferrer() {
		return referrer;
	}
	/**
	 * @param referrer the referrer to set
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	/**
	 * @return the referee
	 */
	public String getReferee() {
		return referee;
	}
	/**
	 * @param referee the referee to set
	 */
	public void setReferee(String referee) {
		this.referee = referee;
	}
	

}
