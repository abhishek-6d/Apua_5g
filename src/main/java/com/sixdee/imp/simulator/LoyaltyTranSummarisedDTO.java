package com.sixdee.imp.simulator;

import java.io.Serializable;
import java.util.Date;

public class LoyaltyTranSummarisedDTO implements Serializable
{
	private long loyaltyId;
	private double rewardPoint;
	private double statusPoint;
	private double redeemPoint;
	private	Date createDate;
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public double getRedeemPoint() {
		return redeemPoint;
	}
	public void setRedeemPoint(double redeemPoint) {
		this.redeemPoint = redeemPoint;
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
}
