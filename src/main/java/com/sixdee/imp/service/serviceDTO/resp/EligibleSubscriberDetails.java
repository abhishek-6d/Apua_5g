package com.sixdee.imp.service.serviceDTO.resp;

public class EligibleSubscriberDetails 
{
	private String marketingPlanName;
	private String marketingPlanDesc;
	private String marketingPlanObj;
	private int marketingPlanId;
	private String amountToSubscribe;
	private String dialCode;
	private String smsKeyword;
	
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
	public String getAmountToSubscribe() {
		return amountToSubscribe;
	}
	public void setAmountToSubscribe(String amountToSubscribe) {
		this.amountToSubscribe = amountToSubscribe;
	}
	public int getMarketingPlanId() {
		return marketingPlanId;
	}
	public void setMarketingPlanId(int marketingPlanId) {
		this.marketingPlanId = marketingPlanId;
	}
	
}
