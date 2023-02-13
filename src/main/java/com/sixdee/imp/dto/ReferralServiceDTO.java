package com.sixdee.imp.dto;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>May 21,2015 07:10:21 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class ReferralServiceDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String transactionId;
	private String timestamp;
	private String channel;
	private Data[] data;
	
	private String referrer;
	private String referee;
	private String referralDate;
	private String languageId;
	
	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}
	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
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
	
	public String toString()
	{
		//return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", referenceNo,requestTime,marketingPlanId,marketingPlanName,scheduleId,scheduleName,actionKey,amount,clientReference,msisdn,offerName,returnCode,returnMsg,cdrType);
		return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s",transactionId,timestamp,channel,referrer,referee,referralDate,languageId);
	}
	

}
