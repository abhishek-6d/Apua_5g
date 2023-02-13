package com.sixdee.imp.service.serviceDTO.resp;

public class MonthlyBillRespDTO extends ResponseDTO
{
	private String accountNo;
	private String loyalityId;
	private String tierName;
	private String openingBalance;
	private String pointsEarned;
	
	
	
	private String pointsRedeemed;
	private String closingBalance;
	private String statusPoints;
	private String statmentDate;
	private String billCycleStartDate;
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getLoyalityId() {
		return loyalityId;
	}
	public void setLoyalityId(String loyalityId) {
		this.loyalityId = loyalityId;
	}
	public String getTierName() {
		return tierName;
	}
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}
	public String getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
	}
	public String getPointsEarned() {
		return pointsEarned;
	}
	public void setPointsEarned(String pointsEarned) {
		this.pointsEarned = pointsEarned;
	}
	public String getPointsRedeemed() {
		return pointsRedeemed;
	}
	public void setPointsRedeemed(String pointsRedeemed) {
		this.pointsRedeemed = pointsRedeemed;
	}
	public String getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(String statusPoints) {
		this.statusPoints = statusPoints;
	}
	public String getStatmentDate() {
		return statmentDate;
	}
	public void setStatmentDate(String statmentDate) {
		this.statmentDate = statmentDate;
	}
	public String getBillCycleStartDate() {
		return billCycleStartDate;
	}
	public void setBillCycleStartDate(String billCycleStartDate) {
		this.billCycleStartDate = billCycleStartDate;
	}
	
	
	
	
	
}
