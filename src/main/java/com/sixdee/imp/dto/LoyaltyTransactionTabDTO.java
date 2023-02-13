package com.sixdee.imp.dto;

import java.util.Date;


public class LoyaltyTransactionTabDTO  
{
	private Long id;
	private Long loyaltyID;                          
	private Long destLoyaltyID;                          
	private String subscriberNumber;
	private String accountNumber;
	private Double preRewardPoints;
	private Double curRewardPoints;
	private Double preStatusPoints;
	private Double curStatusPoints;
	public Integer statusID;
	private String channel;
	private Date createTime;
	private Integer packageId;
	private Double rewardPoints;
	private Double statusPoints;
	private Double redeemPoint;
	private Integer monthIndex;
	private String voucherOrderID;
	private Double oldPoints;
	private Integer preTierId;
	private Integer curTierId;
	private String serverId;
	private int testNumber;
	private Double volume;
	private String reqTransactionID;
	private int pointType;
	private String statusDesc;
	private Date expiryDate;
	private Double expiryPoints=0.00;
	private String description;
	private Date txnRemovalDate;
	
	public Date getTxnRemovalDate() {
		return txnRemovalDate;
	}
	public void setTxnRemovalDate(Date txnRemovalDate) {
		this.txnRemovalDate = txnRemovalDate;
	}
	public int getPointType() {
		return pointType;
	}
	public void setPointType(int pointType) {
		this.pointType = pointType;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	//Sajith ks sts 618 ****start
	private String area;
	private String location;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	//Sajith ks sts 618 ****end
	
	
	
	public String getReqTransactionID() {
		return reqTransactionID;
	}
	public void setReqTransactionID(String reqTransactionID) {
		this.reqTransactionID = reqTransactionID;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Integer getCurTierId() {
		return curTierId;
	}
	public void setCurTierId(Integer curTierId) {
		this.curTierId = curTierId;
	}
	public Integer getPreTierId() {
		return preTierId;
	}
	public void setPreTierId(Integer preTierId) {
		this.preTierId = preTierId;
	}
	public Double getOldPoints() {
		return oldPoints;
	}
	public void setOldPoints(Double oldPoints) {
		this.oldPoints = oldPoints;
	}
	public String getVoucherOrderID() {
		return voucherOrderID;
	}
	public void setVoucherOrderID(String voucherOrderID) {
		this.voucherOrderID = voucherOrderID;
	}
	/**
	 * @return the monthIndex
	 */
	public Integer getMonthIndex() {
		return monthIndex;
	}
	/**
	 * @param monthIndex the monthIndex to set
	 */
	public void setMonthIndex(Integer monthIndex) {
		this.monthIndex = monthIndex;
	}
	public Double getRedeemPoint() {
		return redeemPoint;
	}
	public void setRedeemPoint(Double redeemPoint) {
		this.redeemPoint = redeemPoint;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
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
	 * @return the destLoyaltyID
	 */
	public Long getDestLoyaltyID() {
		return destLoyaltyID;
	}
	/**
	 * @param destLoyaltyID the destLoyaltyID to set
	 */
	public void setDestLoyaltyID(Long destLoyaltyID) {
		this.destLoyaltyID = destLoyaltyID;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	 
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the curRewardPoints
	 */
	public Double getCurRewardPoints() {
		return curRewardPoints;
	}
	/**
	 * @param curRewardPoints the curRewardPoints to set
	 */
	public void setCurRewardPoints(Double curRewardPoints) {
		this.curRewardPoints = curRewardPoints;
	}
	/**
	 * @return the curStatusPoints
	 */
	public Double getCurStatusPoints() {
		return curStatusPoints;
	}
	/**
	 * @param curStatusPoints the curStatusPoints to set
	 */
	public void setCurStatusPoints(Double curStatusPoints) {
		this.curStatusPoints = curStatusPoints;
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
	 * @return the preRewardPoints
	 */
	public Double getPreRewardPoints() {
		return preRewardPoints;
	}
	/**
	 * @param preRewardPoints the preRewardPoints to set
	 */
	public void setPreRewardPoints(Double preRewardPoints) {
		this.preRewardPoints = preRewardPoints;
	}
	/**
	 * @return the preStatusPoints
	 */
	public Double getPreStatusPoints() {
		return preStatusPoints;
	}
	/**
	 * @param preStatusPoints the preStatusPoints to set
	 */
	public void setPreStatusPoints(Double preStatusPoints) {
		this.preStatusPoints = preStatusPoints;
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
	public Double getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public Double getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(Double statusPoints) {
		this.statusPoints = statusPoints;
	}
	public int getTestNumber() {
		return testNumber;
	}
	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Double getExpiryPoints() {
		return expiryPoints;
	}
	public void setExpiryPoints(Double expiryPoints) {
		this.expiryPoints = expiryPoints;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
