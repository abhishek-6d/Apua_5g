package com.sixdee.imp.dto;

import java.util.Date;

public class RewardExpiryDetailsDTO {
	private Long loyaltyId;
	private Date time;
	private float pointsBeforeExpiry;
	private int pointsExpiring;
	private float pointsAfterExpiry;
	public Long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public float getPointsBeforeExpiry() {
		return pointsBeforeExpiry;
	}
	public void setPointsBeforeExpiry(float pointsBeforeExpiry) {
		this.pointsBeforeExpiry = pointsBeforeExpiry;
	}
	public int getPointsExpiring() {
		return pointsExpiring;
	}
	public void setPointsExpiring(int pointsExpiring) {
		this.pointsExpiring = pointsExpiring;
	}
	public float getPointsAfterExpiry() {
		return pointsAfterExpiry;
	}
	public void setPointsAfterExpiry(float pointsAfterExpiry) {
		this.pointsAfterExpiry = pointsAfterExpiry;
	}
	
	
	

}
