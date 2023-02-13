package com.sixdee.imp.dto;

import java.util.Date;

public class LoyaltyTranSummaryMonthlyDTO {

	private Long loyaltyId;
	private int rewardPoints;
	private int statusPoints;
	private Date createDate;
	private int redeemPoints;
	private int expiryRewardPoints=0;
	private int expiryStatusPoints;
	private Date expiryDateReward;
	private Date expiryDateStatus;
	private int partitionIndex;
	
	public int getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public int getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(int statusPoints) {
		this.statusPoints = statusPoints;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(int redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	public int getExpiryRewardPoints() {
		return expiryRewardPoints;
	}
	public void setExpiryRewardPoints(int expiryRewardPoints) {
		this.expiryRewardPoints = expiryRewardPoints;
	}
	public int getExpiryStatusPoints() {
		return expiryStatusPoints;
	}
	public void setExpiryStatusPoints(int expiryStatusPoints) {
		this.expiryStatusPoints = expiryStatusPoints;
	}
	public Date getExpiryDateReward() {
		return expiryDateReward;
	}
	public void setExpiryDateReward(Date expiryDateReward) {
		this.expiryDateReward = expiryDateReward;
	}
	public Date getExpiryDateStatus() {
		return expiryDateStatus;
	}
	public void setExpiryDateStatus(Date expiryDateStatus) {
		this.expiryDateStatus = expiryDateStatus;
	}
	public int getPartitionIndex() {
		return partitionIndex;
	}
	public void setPartitionIndex(int partitionIndex) {
		this.partitionIndex = partitionIndex;
	}
	public Long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}


}
