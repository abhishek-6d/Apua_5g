package com.sixdee.imp.dto;

import java.util.Date;

public class LmsOnmPointSnapshotDTO {
	
	private long loyaltyId;
	private double openingBalance;
	private double closingBalance;
	private Date createDate;
	private Date updateDate;
	private String isCdr;
	
	
	
	
	public String getIsCdr() {
		return isCdr;
	}
	public void setIsCdr(String isCdr) {
		this.isCdr = isCdr;
	}
	public double getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}
	public double getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}
	public long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	

}
