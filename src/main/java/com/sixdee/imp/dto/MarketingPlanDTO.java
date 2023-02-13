package com.sixdee.imp.dto;

public class MarketingPlanDTO 
{
	private int marketingPlanId;
	private String marketingPlanName;
	private String marketingPlanDesc;
	private String marketingPlanObj;
	private String smsKeyword;
	private String dialCode;
	
	public String getDialCode() {
		return dialCode;
	}
	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}
	public String getSmsKeyword() {
		return smsKeyword;
	}
	public void setSmsKeyword(String smsKeyword) {
		this.smsKeyword = smsKeyword;
	}
	public String getMarketingPlanDesc() {
		return marketingPlanDesc;
	}
	public void setMarketingPlanDesc(String marketingPlanDesc) {
		this.marketingPlanDesc = marketingPlanDesc;
	}
	public int getMarketingPlanId() {
		return marketingPlanId;
	}
	public void setMarketingPlanId(int marketingPlanId) {
		this.marketingPlanId = marketingPlanId;
	}
	public String getMarketingPlanName() {
		return marketingPlanName;
	}
	public void setMarketingPlanName(String marketingPlanName) {
		this.marketingPlanName = marketingPlanName;
	}
	public String getMarketingPlanObj() {
		return marketingPlanObj;
	}
	public void setMarketingPlanObj(String marketingPlanObj) {
		this.marketingPlanObj = marketingPlanObj;
	}
}
