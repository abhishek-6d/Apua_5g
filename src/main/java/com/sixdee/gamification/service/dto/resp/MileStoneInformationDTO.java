/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class MileStoneInformationDTO {

	private String mileStoneId = null;
	private String mileStoneName = null;
	private String mileStoneDesc = null;
	private Parameters parameters = null;
	private AchievementInfoDTO achievementDTO = null;
	
	
	public String getMileStoneName() {
		return mileStoneName;
	}
	public void setMileStoneName(String mileStoneName) {
		this.mileStoneName = mileStoneName;
	}
	public AchievementInfoDTO getAchievementDTO() {
		return achievementDTO;
	}
	public void setAchievementDTO(AchievementInfoDTO achievementDTO) {
		this.achievementDTO = achievementDTO;
	}
	public String getMileStoneId() {
		return mileStoneId;
	}
	public void setMileStoneId(String mileStoneId) {
		this.mileStoneId = mileStoneId;
	}
	public String getMileStoneDesc() {
		return mileStoneDesc;
	}
	public void setMileStoneDesc(String mileStoneDesc) {
		this.mileStoneDesc = mileStoneDesc;
	}
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	
			
}
