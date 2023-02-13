package com.sixdee.imp.dto;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 04,2013 07:46:17 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberDetails;


public class EligibleSubscriberDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transactionId;
	private String msisdn;
	private String channel;
	private String month;
	private List<EligibleSubscriberDetails> eligibleSubscriberList;
	private String userName;
	private String password;
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public List<EligibleSubscriberDetails> getEligibleSubscriberList() {
		return eligibleSubscriberList;
	}
	public void setEligibleSubscriberList(
			List<EligibleSubscriberDetails> eligibleSubscriberList) {
		this.eligibleSubscriberList = eligibleSubscriberList;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	

}
