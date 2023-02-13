package com.sixdee.imp.dto;

import java.util.Calendar;

public class LoyaltyExpiryDTO {

	private Long id;
	private Long loyaltyID;
	private Double preRewardPoints = 0.0;
	private Double preStatusPoints = 0.0;
	private Double curRewardPoints = 0.0;
	private Double curStatusPoints = 0.0;
	private Double expiryRewardPoints = 0.0;
	private Double expiryStatusPoints = 0.0;
	private Integer preTierId;
	private Integer curTierId;
	private String serverId;
	private String createTime;
	
	private Calendar[] rewardQuater;
	private Calendar[] statusQuater;
	
	private int repeatCounter;
	private String status = "SC1111";
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//sb.append("For Loyalty Id = ");
		sb.append(loyaltyID);
		sb.append("|");
		sb.append(preRewardPoints);
		sb.append("|");
		sb.append(curRewardPoints);
		sb.append("|");
		sb.append(preStatusPoints);
		sb.append("|");
		sb.append(curStatusPoints);
		sb.append("|");

		sb.append(preTierId);
		sb.append("|");
		
		sb.append(curTierId);
		sb.append("|");
		
		//sb.append(" Reward points = ");
		sb.append(expiryRewardPoints);
		sb.append("|");
		//sb.append(" status points = ");
		sb.append(expiryStatusPoints);
		sb.append("|");
		sb.append(createTime);
		sb.append("|");
		sb.append(status);
		//sb.append(" Expired Successfully!!! ");
		return sb.toString();
	}
	public int getRepeatCounter() {
		return repeatCounter;
	}
	public void setRepeatCounter(int repeatCounter) {
		this.repeatCounter = repeatCounter;
	}
	 
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Double getCurRewardPoints() {
		return curRewardPoints;
	}
	public void setCurRewardPoints(Double curRewardPoints) {
		this.curRewardPoints = curRewardPoints;
	}
	public Double getCurStatusPoints() {
		return curStatusPoints;
	}
	public void setCurStatusPoints(Double curStatusPoints) {
		this.curStatusPoints = curStatusPoints;
	}
	public Integer getCurTierId() {
		return curTierId;
	}
	public void setCurTierId(Integer curTierId) {
		this.curTierId = curTierId;
	}
	public Double getExpiryRewardPoints() {
		return expiryRewardPoints;
	}
	public void setExpiryRewardPoints(Double expiryRewardPoints) {
		this.expiryRewardPoints = expiryRewardPoints;
	}
	public Double getExpiryStatusPoints() {
		return expiryStatusPoints;
	}
	public void setExpiryStatusPoints(Double expiryStatusPoints) {
		this.expiryStatusPoints = expiryStatusPoints;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public Double getPreRewardPoints() {
		return preRewardPoints;
	}
	public void setPreRewardPoints(Double preRewardPoints) {
		this.preRewardPoints = preRewardPoints;
	}
	public Double getPreStatusPoints() {
		return preStatusPoints;
	}
	public void setPreStatusPoints(Double preStatusPoints) {
		this.preStatusPoints = preStatusPoints;
	}
	public Integer getPreTierId() {
		return preTierId;
	}
	public void setPreTierId(Integer preTierId) {
		this.preTierId = preTierId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Calendar[] getRewardQuater() {
		return rewardQuater;
	}
	public void setRewardQuater(Calendar[] rewardQuater) {
		this.rewardQuater = rewardQuater;
	}
	public Calendar[] getStatusQuater() {
		return statusQuater;
	}
	public void setStatusQuater(Calendar[] statusQuater) {
		this.statusQuater = statusQuater;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
