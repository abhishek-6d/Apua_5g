package com.sixdee.imp.simulator;

import java.util.Date;

public class LoyaltyLineRemoveHistoryDTO {

	private Long id;
	private Long loyaltyId;
	private String lineNumber;
	private String accountNumber;
	private Integer accountType;
	private String accountCategory;
	private Double lineRewardPoints=0.0;
	private Double accountRewardPoints=0.0;
	private Double lineStatusPoints=0.0;
	private Date lineAddedDate;
	
	
	
	
	public Double getAccountRewardPoints() {
		return accountRewardPoints;
	}
	public void setAccountRewardPoints(Double accountRewardPoints) {
		this.accountRewardPoints = accountRewardPoints;
	}
	public String getAccountCategory() {
		return accountCategory;
	}
	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getLineAddedDate() {
		return lineAddedDate;
	}
	public void setLineAddedDate(Date lineAddedDate) {
		this.lineAddedDate = lineAddedDate;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Double getLineRewardPoints() {
		return lineRewardPoints;
	}
	public void setLineRewardPoints(Double lineRewardPoints) {
		this.lineRewardPoints = lineRewardPoints;
	}
	public Double getLineStatusPoints() {
		return lineStatusPoints;
	}
	public void setLineStatusPoints(Double lineStatusPoints) {
		this.lineStatusPoints = lineStatusPoints;
	}
	public Long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	
	
}
