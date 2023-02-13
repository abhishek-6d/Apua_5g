package com.sixdee.imp.dto;

import java.util.Date;

public class TierAndBonusPointDetailsDTO {
	
	private Date expiryDate;
	private String msisdn;
	private String loyaltyId;
	private Double tierPoints=0.0;
	private Double bonusPoints=0.0;
	private Double totalPoints;
	private Date tierCreateDate;
	private Date bonusCreateDate;
	private String expiryQuarter;
	private int id;
	
	
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public Double getTierPoints() {
		return tierPoints;
	}
	public void setTierPoints(Double tierPoints) {
		this.tierPoints = tierPoints;
	}
	public Double getBonusPoints() {
		return bonusPoints;
	}
	public void setBonusPoints(Double bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
	public Double getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}
	public Date getTierCreateDate() {
		return tierCreateDate;
	}
	public void setTierCreateDate(Date tierCreateDate) {
		this.tierCreateDate = tierCreateDate;
	}
	public Date getBonusCreateDate() {
		return bonusCreateDate;
	}
	public void setBonusCreateDate(Date bonusCreateDate) {
		this.bonusCreateDate = bonusCreateDate;
	}
	public String getExpiryQuarter() {
		return expiryQuarter;
	}
	public void setExpiryQuarter(String expiryQuarter) {
		this.expiryQuarter = expiryQuarter;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	

}
