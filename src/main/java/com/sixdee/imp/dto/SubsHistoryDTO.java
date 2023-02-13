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
 * <td>September 13,2013 12:46:40 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sixdee.imp.service.serviceDTO.resp.SubscriberHistoryDTO;


public class SubsHistoryDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msisdn;
	private Date fromDate;
	private Date toDate;
	private String month;
	private String transactionId;
	private String channel;
	private Set<Integer> marketingPlanIds;
	private List<SubscriberHistoryDTO> subscriberList;
	
	public Set<Integer> getMarketingPlanIds() {
		return marketingPlanIds;
	}
	public void setMarketingPlanIds(Set<Integer> marketingPlanIds) {
		this.marketingPlanIds = marketingPlanIds;
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
 
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public List<SubscriberHistoryDTO> getSubscriberList() {
		return subscriberList;
	}
	public void setSubscriberList(List<SubscriberHistoryDTO> subscriberList) {
		this.subscriberList = subscriberList;
	}

}
