/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class AchievementInfoDTO {

	/*
	 * 1 - Badge
	 * 2 - Award
	 * 3 - Avatar
	 */
	private int achievemntType = 0;
	private String achievementImagePath = null;
	private byte[] achievementImage = null;
	private int achievementRank = 0;
	
	private Parameters parameters = null;
	private int validityPeriod = 0;
	/*
	 * 0 - Hours
	 * 1 - Days
	 */
	private String validityType = null;
	private String expiryPeriod = null;
	
	
	
	
	public byte[] getAchievementImage() {
		return achievementImage;
	}
	public void setAchievementImage(byte[] achievementImage) {
		this.achievementImage = achievementImage;
	}
	public int getAchievementRank() {
		return achievementRank;
	}
	public void setAchievementRank(int achievementRank) {
		this.achievementRank = achievementRank;
	}
	public int getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	public String getValidityType() {
		return validityType;
	}
	public void setValidityType(String validityType) {
		this.validityType = validityType;
	}
	public String getExpiryPeriod() {
		return expiryPeriod;
	}
	public void setExpiryPeriod(String expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}
	
	public String getAchievementImagePath() {
		return achievementImagePath;
	}
	public void setAchievementImagePath(String achievementImagePath) {
		this.achievementImagePath = achievementImagePath;
	}

	public int getAchievemntType() {
		return achievemntType;
	}
	public void setAchievemntType(int achievemntType) {
		this.achievemntType = achievemntType;
	}
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	
}
