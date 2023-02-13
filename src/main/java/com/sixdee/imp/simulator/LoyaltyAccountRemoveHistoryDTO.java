package com.sixdee.imp.simulator;

import java.util.Date;

public class LoyaltyAccountRemoveHistoryDTO {

	private Long id;
	private Long loyaltyId;
	private Double loyaltyRewardPoints=0.0;
	private Double loyaltyStatusPoints=0.0;
	private Double refreshRewardPoints=0.0;
	private Double refreshStatusPoints=0.0;
	private Integer presentTierID;
	private Integer refreshTierID;
	private Integer loyaltyLinesCount;
	private Integer loyaltyLinesFixedCount;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public Integer getLoyaltyLinesCount() {
		return loyaltyLinesCount;
	}
	public void setLoyaltyLinesCount(Integer loyaltyLinesCount) {
		this.loyaltyLinesCount = loyaltyLinesCount;
	}
	public Integer getLoyaltyLinesFixedCount() {
		return loyaltyLinesFixedCount;
	}
	public void setLoyaltyLinesFixedCount(Integer loyaltyLinesFixedCount) {
		this.loyaltyLinesFixedCount = loyaltyLinesFixedCount;
	}
	public Double getLoyaltyRewardPoints() {
		return loyaltyRewardPoints;
	}
	public void setLoyaltyRewardPoints(Double loyaltyRewardPoints) {
		this.loyaltyRewardPoints = loyaltyRewardPoints;
	}
	public Double getLoyaltyStatusPoints() {
		return loyaltyStatusPoints;
	}
	public void setLoyaltyStatusPoints(Double loyaltyStatusPoints) {
		this.loyaltyStatusPoints = loyaltyStatusPoints;
	}
	public Integer getPresentTierID() {
		return presentTierID;
	}
	public void setPresentTierID(Integer presentTierID) {
		this.presentTierID = presentTierID;
	}
	public Double getRefreshRewardPoints() {
		return refreshRewardPoints;
	}
	public void setRefreshRewardPoints(Double refreshRewardPoints) {
		this.refreshRewardPoints = refreshRewardPoints;
	}
	public Double getRefreshStatusPoints() {
		return refreshStatusPoints;
	}
	public void setRefreshStatusPoints(Double refreshStatusPoints) {
		this.refreshStatusPoints = refreshStatusPoints;
	}
	public Integer getRefreshTierID() {
		return refreshTierID;
	}
	public void setRefreshTierID(Integer refreshTierID) {
		this.refreshTierID = refreshTierID;
	}
	
	
	
}
