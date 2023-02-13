package com.sixdee.imp.dto;

public class TierInfoDTO {

	private int tierId;
	private String tierName;
	private Integer minValue;
	private Integer maxValue;
	private Double welcomeRewardPoints;
	private Double welcomeStatusPoints;
	
	private Double tierRewardPoints;
	
	 
	

	public Double getTierRewardPoints() {
		return tierRewardPoints;
	}




	public void setTierRewardPoints(Double tierRewardPoints) {
		this.tierRewardPoints = tierRewardPoints;
	}




	/**
	 * @return the maxValue
	 */
	public Integer getMaxValue() {
		return maxValue;
	}




	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}




	/**
	 * @return the minValue
	 */
	public Integer getMinValue() {
		return minValue;
	}




	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}




	 




	public Double getWelcomeRewardPoints() {
		return welcomeRewardPoints;
	}




	public void setWelcomeRewardPoints(Double welcomeRewardPoints) {
		this.welcomeRewardPoints = welcomeRewardPoints;
	}




	public Double getWelcomeStatusPoints() {
		return welcomeStatusPoints;
	}




	public void setWelcomeStatusPoints(Double welcomeStatusPoints) {
		this.welcomeStatusPoints = welcomeStatusPoints;
	}




	/**
	 * @return the tierId
	 */
	public int getTierId() {
		return tierId;
	}




	/**
	 * @param tierId the tierId to set
	 */
	public void setTierId(int tierId) {
		this.tierId = tierId;
	}




	/**
	 * @return the tierName
	 */
	public String getTierName() {
		return tierName;
	}




	/**
	 * @param tierName the tierName to set
	 */
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}




	@Override
	public String toString() {
		return " [ Tier Name: "+tierName+" Min Value "+minValue+"  Max Value "+maxValue+"  ] ";
	}
	
	
	
}//TierInfoDTO
