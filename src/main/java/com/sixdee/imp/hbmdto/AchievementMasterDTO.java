package com.sixdee.imp.hbmdto;

import java.io.Serializable;
import java.util.Set;

public class AchievementMasterDTO implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int achievmentId;
  private int achievementValidity;
  private String achievementType;
  private String startDate;
  private String endDate;
   private Set<AchievementLanguageMapperDTO> achievementLanguageMapperDTO;
   
   
   
public int getAchievmentId() {
	return achievmentId;
}
public void setAchievmentId(int achievmentId) {
	this.achievmentId = achievmentId;
}
public int getAchievementValidity() {
	return achievementValidity;
}
public void setAchievementValidity(int achievementValidity) {
	this.achievementValidity = achievementValidity;
}
public String getAchievementType() {
	return achievementType;
}
public void setAchievementType(String achievementType) {
	this.achievementType = achievementType;
}
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public String getEndDate() {
	return endDate;
}
public void setEndDate(String endDate) {
	this.endDate = endDate;
}
public Set<AchievementLanguageMapperDTO> getAchievementLanguageMapperDTO() {
	return achievementLanguageMapperDTO;
}
public void setAchievementLanguageMapperDTO(Set<AchievementLanguageMapperDTO> achievementLanguageMapperDTO) {
	this.achievementLanguageMapperDTO = achievementLanguageMapperDTO;
}
 
 
  
}
