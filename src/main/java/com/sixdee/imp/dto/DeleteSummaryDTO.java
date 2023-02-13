/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;

/**
 * @author NITHIN KUNJAPPAN
 *
 */
public class DeleteSummaryDTO {
	
	
	private Long id;
	private Long loyaltyID;      
	private Date deleteDate;
	private String subscriberNumber;
	public Integer statusID;
	public Integer tierID;
	public String accountNo;
	public Integer accountTypeID;
	public String category;
	private Double statusPoints;
	private Double rewardPoints; 
	private String channel;
	private String remarks;
	private String serverID;
	private String deleteKey;
	
	
	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the accountTypeID
	 */
	public Integer getAccountTypeID() {
		return accountTypeID;
	}
	/**
	 * @param accountTypeID the accountTypeID to set
	 */
	public void setAccountTypeID(Integer accountTypeID) {
		this.accountTypeID = accountTypeID;
	}
	/**
	 * @return the tierID
	 */
	public Integer getTierID() {
		return tierID;
	}
	/**
	 * @param tierID the tierID to set
	 */
	public void setTierID(Integer tierID) {
		this.tierID = tierID;
	}
	/**
	 * @return the statusPoints
	 */
	public Double getStatusPoints() {
		return statusPoints;
	}
	/**
	 * @param statusPoints the statusPoints to set
	 */
	public void setStatusPoints(Double statusPoints) {
		this.statusPoints = statusPoints;
	}
	/**
	 * @return the rewardPoints
	 */
	public Double getRewardPoints() {
		return rewardPoints;
	}
	/**
	 * @param rewardPoints the rewardPoints to set
	 */
	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	/**
	 * @return the deleteKey
	 */
	public String getDeleteKey() {
		return deleteKey;
	}
	/**
	 * @param deleteKey the deleteKey to set
	 */
	public void setDeleteKey(String deleteKey) {
		this.deleteKey = deleteKey;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the loyaltyID
	 */
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	/**
	 * @param loyaltyID the loyaltyID to set
	 */
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	/**
	 * @return the deleteDate
	 */
	public Date getDeleteDate() {
		return deleteDate;
	}
	/**
	 * @param deleteDate the deleteDate to set
	 */
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	/**
	 * @return the subscriberNumber
	 */
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * @param subscriberNumber the subscriberNumber to set
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	/**
	 * @return the statusID
	 */
	public Integer getStatusID() {
		return statusID;
	}
	/**
	 * @param statusID the statusID to set
	 */
	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
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
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the serverID
	 */
	public String getServerID() {
		return serverID;
	}
	/**
	 * @param serverID the serverID to set
	 */
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	

	
	

}
