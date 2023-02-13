package com.sixdee.imp.hbmdto;

public class MileStoneLanguageMapperDTO {
    private int id;
    private int languageId;
    private String milestoneName;
    private String milestoneDesc;
    private String milestoneImage;
    private int milestoneId;
    private MileStoneMasterDTO mileStoneMaster;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public String getMilestoneName() {
		return milestoneName;
	}
	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}
	public String getMilestoneDesc() {
		return milestoneDesc;
	}
	public void setMilestoneDesc(String milestoneDesc) {
		this.milestoneDesc = milestoneDesc;
	}
	public String getMilestoneImage() {
		return milestoneImage;
	}
	public void setMilestoneImage(String milestoneImage) {
		this.milestoneImage = milestoneImage;
	}
	public int getMilestoneId() {
		return milestoneId;
	}
	public void setMilestoneId(int milestoneId) {
		this.milestoneId = milestoneId;
	}
	public MileStoneMasterDTO getMileStoneMaster() {
		return mileStoneMaster;
	}
	public void setMileStoneMaster(MileStoneMasterDTO mileStoneMaster) {
		this.mileStoneMaster = mileStoneMaster;
	}
    
    
}
