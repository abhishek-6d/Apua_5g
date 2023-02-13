package com.sixdee.imp.service.serviceDTO.resp;

public class SubscriberHistoryDTO 
{
	private String msisdn;
	private String actionKey;
	private String marketingPlan;
	private String promotionName;
	private String promotionDate;
	private String message;
	private String amount;
	private String validity;
	private String bonus;
	private String chargingIndicator;
	private String typeOfAction;
	private String microSegmentId;
	private String balanceDeduction;
	private String marketingPlanDesc;
	private int marketingPlanId;
	
	public int getMarketingPlanId() {
		return marketingPlanId;
	}
	public void setMarketingPlanId(int marketingPlanId) {
		this.marketingPlanId = marketingPlanId;
	}
	public String getActionKey() {
		return actionKey;
	}
	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBalanceDeduction() {
		return balanceDeduction;
	}
	public void setBalanceDeduction(String balanceDeduction) {
		this.balanceDeduction = balanceDeduction;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getChargingIndicator() {
		return chargingIndicator;
	}
	public void setChargingIndicator(String chargingIndicator) {
		this.chargingIndicator = chargingIndicator;
	}
	public String getMarketingPlan() {
		return marketingPlan;
	}
	public void setMarketingPlan(String marketingPlan) {
		this.marketingPlan = marketingPlan;
	}
	public String getMarketingPlanDesc() {
		return marketingPlanDesc;
	}
	public void setMarketingPlanDesc(String marketingPlanDesc) {
		this.marketingPlanDesc = marketingPlanDesc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMicroSegmentId() {
		return microSegmentId;
	}
	public void setMicroSegmentId(String microSegmentId) {
		this.microSegmentId = microSegmentId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getPromotionDate() {
		return promotionDate;
	}
	public void setPromotionDate(String promotionDate) {
		this.promotionDate = promotionDate;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	public String getTypeOfAction() {
		return typeOfAction;
	}
	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
}
