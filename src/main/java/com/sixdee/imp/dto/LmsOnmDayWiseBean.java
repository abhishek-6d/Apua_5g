package com.sixdee.imp.dto;

import java.util.Date;


public class LmsOnmDayWiseBean {
	private Date expiryDate = null;
	private String loyaltyId = null;
	private String msisdn = null;
	private long tierPoints = 0;
	private long bonusPoints = 0;
	private long totalPoints = 0;
	private long usedTierPoints = 0;
	private long usedBonusPoints = 0;
	private Date tierUpdateDate = null;
	private Date bonusUpdateDate = null;
	private String expiryQuarter = null;
	private long id;

	public long getUsedTierPoints() {
		return usedTierPoints;
	}

	public void setUsedTierPoints(long usedTierPoints) {
		this.usedTierPoints = usedTierPoints;
	}

	public long getUsedBonusPoints() {
		return usedBonusPoints;
	}

	public void setUsedBonusPoints(long usedBonusPoints) {
		this.usedBonusPoints = usedBonusPoints;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getLoyaltyId() {
		return loyaltyId;
	}

	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}

	public long getTierPoints() {
		return tierPoints;
	}

	public void setTierPoints(long tierPoints) {
		this.tierPoints = tierPoints;
	}

	public long getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(long bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(long totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Date getTierUpdateDate() {
		return tierUpdateDate;
	}

	public void setTierUpdateDate(Date tierUpdateDate) {
		this.tierUpdateDate = tierUpdateDate;
	}

	public Date getBonusUpdateDate() {
		return bonusUpdateDate;
	}

	public void setBonusUpdateDate(Date bonusUpdateDate) {
		this.bonusUpdateDate = bonusUpdateDate;
	}

	public String getExpiryQuarter() {
		return expiryQuarter;
	}

	public void setExpiryQuarter(String expiryQuarter) {
		this.expiryQuarter = expiryQuarter;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public String toString() {
		return "LmsOnmDayWiseBean [expiryDate=" + expiryDate + ", loyaltyId=" + loyaltyId + ", msisdn=" + msisdn
				+ ", tierPoints=" + tierPoints + ", bonusPoints=" + bonusPoints + ", totalPoints=" + totalPoints
				+ ", usedTierPoints=" + usedTierPoints + ", usedBonusPoints=" + usedBonusPoints + ", tierUpdateDate="
				+ tierUpdateDate + ", bonusUpdateDate=" + bonusUpdateDate + ", expiryQuarter=" + expiryQuarter + ", id="
				+ id + "]";
	}

}
