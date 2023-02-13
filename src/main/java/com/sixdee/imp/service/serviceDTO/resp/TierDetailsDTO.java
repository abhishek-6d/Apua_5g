package com.sixdee.imp.service.serviceDTO.resp;

public class TierDetailsDTO extends ResponseDTO 
{
	private String currentTierName;
	private String validTill;
	private String pointsToUpradeTier;
	public String getCurrentTierName() {
		return currentTierName;
	}
	public void setCurrentTierName(String currentTierName) {
		this.currentTierName = currentTierName;
	}
	public String getPointsToUpradeTier() {
		return pointsToUpradeTier;
	}
	public void setPointsToUpradeTier(String pointsToUpradeTier) {
		this.pointsToUpradeTier = pointsToUpradeTier;
	}
	public String getValidTill() {
		return validTill;
	}
	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}
	
}
