package com.sixdee.imp.dto;

public class CategoryDetailsDTO {

	private int tierID;
	private int categoryID;
	private Double minValue;
	private Double maxValue;
	private Double rewardPoints;
	private Double statusPoints;
	
	
	
	
	/**
	 * @return the categoryID
	 */
	public int getCategoryID() {
		return categoryID;
	}
	/**
	 * @param categoryID the categoryID to set
	 */
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	/**
	 * @return the maxValue
	 */
	public Double getMaxValue() {
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * @return the minValue
	 */
	public Double getMinValue() {
		return minValue;
	}
	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	/**
	 * @return the rewardPoints
	 */
	public Double getRewardPoints() {
		return rewardPoints;
	}
	/**
	 * @param rewardPoints the rewardPoints to set
	 */
	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	/**
	 * @return the statusPoints
	 */
	public Double getStatusPoints() {
		return statusPoints;
	}
	/**
	 * @param statusPoints the statusPoints to set
	 */
	public void setStatusPoints(Double statusPoints) {
		this.statusPoints = statusPoints;
	}
	/**
	 * @return the tierID
	 */
	public int getTierID() {
		return tierID;
	}
	/**
	 * @param tierID the tierID to set
	 */
	public void setTierID(int tierID) {
		this.tierID = tierID;
	}
	@Override
	public String toString() {
		return "CategoryDetailsDTO [tierID=" + tierID + ", categoryID=" + categoryID + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", rewardPoints=" + rewardPoints + ", statusPoints=" + statusPoints + "]";
	}
	
	

	
	
}
