package com.sixdee.imp.hbmdto;

import java.io.Serializable;
import java.util.Set;

public class MileStoneMasterDTO implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int milestoneId;
  private int milestoneValidity;
  private String startDate;
  private String endDate;
   private Set<MileStoneLanguageMapperDTO> milestoneMapper;
 
 
public int getMilestoneId() {
	return milestoneId;
}
public void setMilestoneId(int milestoneId) {
	this.milestoneId = milestoneId;
}
public int getMilestoneValidity() {
	return milestoneValidity;
}
public void setMilestoneValidity(int milestoneValidity) {
	this.milestoneValidity = milestoneValidity;
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
public Set<MileStoneLanguageMapperDTO> getMilestoneMapper() {
	return milestoneMapper;
}
public void setMilestoneMapper(Set<MileStoneLanguageMapperDTO> milestoneMapper) {
	this.milestoneMapper = milestoneMapper;
}
  
  
}
