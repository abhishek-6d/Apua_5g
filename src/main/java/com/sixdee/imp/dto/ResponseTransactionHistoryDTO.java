/**
 * 
 */
package com.sixdee.imp.dto;

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
public class ResponseTransactionHistoryDTO {
	
	private String Date = null;
	private String subscriberNumber = null;
	private String activity = null;
	private double previousRewardPoints = 0;
	private double previousStatusPoints = 0;
	private double rewardPoint = 0;
	private double statusPoint = 0;
	private int currentRewardPoints = 0;
	private int currentStatusPoints = 0;
	private Long destLoyaltyID = null;
	private Long accountNumber;
	private String channel = null;
	private Integer packageId;
	private String type=null;
	private String expiryDate;
	private String expiryPoints;


	
	
	public Long getDestLoyaltyID() {
		return destLoyaltyID;
	}
	public void setDestLoyaltyID(Long destLoyaltyID) {
		this.destLoyaltyID = destLoyaltyID;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public int getCurrentRewardPoints() {
		return currentRewardPoints;
	}
	public void setCurrentRewardPoints(int currentRewardPoints) {
		this.currentRewardPoints = currentRewardPoints;
	}
	public int getCurrentStatusPoints() {
		return currentStatusPoints;
	}
	public void setCurrentStatusPoints(int currentStatusPoints) {
		this.currentStatusPoints = currentStatusPoints;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public double getPreviousRewardPoints() {
		return previousRewardPoints;
	}
	public void setPreviousRewardPoints(double previousRewardPoints) {
		this.previousRewardPoints = previousRewardPoints;
	}
	public double getPreviousStatusPoints() {
		return previousStatusPoints;
	}
	public void setPreviousStatusPoints(double previousStatusPoints) {
		this.previousStatusPoints = previousStatusPoints;
	}
	public double getRewardPoint() {
		return rewardPoint;
	}
	public void setRewardPoint(double rewardPoint) {
		this.rewardPoint = rewardPoint;
	}
	public double getStatusPoint() {
		return statusPoint;
	}
	public void setStatusPoint(double statusPoint) {
		this.statusPoint = statusPoint;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getExpiryPoints() {
		return expiryPoints;
	}
	public void setExpiryPoints(String expiryPoints) {
		this.expiryPoints = expiryPoints;
	}
}
