package com.sixdee.imp.dto;

public class PointDetailsInfoDTO {

	private String expiryDate;
	private String rewardPoints;
	private String statusPoints;
	
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(String statusPoints) {
		this.statusPoints = statusPoints;
	}
	
	
	
	@Override
	public String toString() {
		return "Expiry Date : "+expiryDate+"  Reward Points : "+rewardPoints+"  Status Points : "+statusPoints;
	}
	 
	
	
	
}
