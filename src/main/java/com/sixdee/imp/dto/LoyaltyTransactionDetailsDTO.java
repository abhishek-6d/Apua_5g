/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class LoyaltyTransactionDetailsDTO {

	private long loyaltyID = 0;
	private long subscriberNumber = 0;
	private long accountNumber = 0;
	private long preRewardPoints = 0;
	private long curRewardPoints = 0;
	private long preStatusPoints = 0;
	private long curStatusPoints = 0;
	private long voucherOrderId = 0;
	private Date createDate     = null;
	private long statusId  		= 0;
	private String channel      = null ;
	private long destLoyaltyId  = 0;
	private long id 			= 0;
	
	
	public long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public long getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public long getPreRewardPoints() {
		return preRewardPoints;
	}
	public void setPreRewardPoints(long preRewardPoints) {
		this.preRewardPoints = preRewardPoints;
	}
	public long getCurRewardPoints() {
		return curRewardPoints;
	}
	public void setCurRewardPoints(long curRewardPoints) {
		this.curRewardPoints = curRewardPoints;
	}
	public long getPreStatusPoints() {
		return preStatusPoints;
	}
	public void setPreStatusPoints(long preStatusPoints) {
		this.preStatusPoints = preStatusPoints;
	}
	public long getCurStatusPoints() {
		return curStatusPoints;
	}
	public void setCurStatusPoints(long curStatusPoints) {
		this.curStatusPoints = curStatusPoints;
	}
	public long getVoucherOrderId() {
		return voucherOrderId;
	}
	public void setVoucherOrderId(long voucherOrderId) {
		this.voucherOrderId = voucherOrderId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getStatusId() {
		return statusId;
	}
	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public long getDestLoyaltyId() {
		return destLoyaltyId;
	}
	public void setDestLoyaltyId(long destLoyaltyId) {
		this.destLoyaltyId = destLoyaltyId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
}
