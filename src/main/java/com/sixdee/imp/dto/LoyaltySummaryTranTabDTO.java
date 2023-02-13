package com.sixdee.imp.dto;

import java.util.Date;


public class LoyaltySummaryTranTabDTO  
{
	private Long loyaltyID;                          
	private Date createTime;
	private Double rewardPoints;
	private Double statusPoints;
	private Double redeemPoints;
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public Double getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(Double redeemPoints) {
		this.redeemPoints = redeemPoints;
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
	
	
	 
	
	
}
