/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.imp.dto.parser.in.Parameter;

/**
 * @author rahul.kr
 *
 */
public class MileStoneAchievementDTO {

	private String mileStoneId = null;
	private String mileStoneDesc = null;
	private String badgeAchieved = null;
	private String awardsAchieved = null;
	private String points = null;
	private Parameter param = null;
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
	public String getBadgeAchieved() {
		return badgeAchieved;
	}
	public void setBadgeAchieved(String badgeAchieved) {
		this.badgeAchieved = badgeAchieved;
	}
	public String getAwardsAchieved() {
		return awardsAchieved;
	}
	public void setAwardsAchieved(String awardsAchieved) {
		this.awardsAchieved = awardsAchieved;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public Parameter getParam() {
		return param;
	}
	public void setParam(Parameter param) {
		this.param = param;
	}
	
	
}
