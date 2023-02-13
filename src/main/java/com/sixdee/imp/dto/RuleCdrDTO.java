package com.sixdee.imp.dto;

import java.util.Date;

public class RuleCdrDTO 
{
	private int id;
	private String marketingPlanId;
	private String da;
	private String type;
	
	private String actionkey;
	private String scheduleName;
	private Date promotiondate;
	private String messageID;
	private String freebiesAmnt;
	private String freebieValdity;
	private String bonus;
	private String chargingIndicator;
	private String microSegment;
	private String status;
	private String balanceDeduction;
	private String cdrStatus;
	
	
	public String getActionkey() {
		return actionkey;
	}
	public void setActionkey(String actionkey) {
		this.actionkey = actionkey;
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
	public String getCdrStatus() {
		return cdrStatus;
	}
	public void setCdrStatus(String cdrStatus) {
		this.cdrStatus = cdrStatus;
	}
	public String getChargingIndicator() {
		return chargingIndicator;
	}
	public void setChargingIndicator(String chargingIndicator) {
		this.chargingIndicator = chargingIndicator;
	}
	public String getFreebiesAmnt() {
		return freebiesAmnt;
	}
	public void setFreebiesAmnt(String freebiesAmnt) {
		this.freebiesAmnt = freebiesAmnt;
	}
	public String getFreebieValdity() {
		return freebieValdity;
	}
	public void setFreebieValdity(String freebieValdity) {
		this.freebieValdity = freebieValdity;
	}
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public String getMicroSegment() {
		return microSegment;
	}
	public void setMicroSegment(String microSegment) {
		this.microSegment = microSegment;
	}
	public Date getPromotiondate() {
		return promotiondate;
	}
	public void setPromotiondate(Date promotiondate) {
		this.promotiondate = promotiondate;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDa() {
		return da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getMarketingPlanId() {
		return marketingPlanId;
	}
	public void setMarketingPlanId(String marketingPlanId) {
		this.marketingPlanId = marketingPlanId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
