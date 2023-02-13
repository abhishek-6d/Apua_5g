package com.sixdee.imp.dto;

import java.util.Date;

public class ADSLTabDTO {

	public String ADSLNumber;
	public Long loyaltyID;
	public String accountNumber;
	public Integer statusID;
	public Date statusUpdatedDate;
	public Double points;
	private Long counter;
	private Integer accountTypeId;
	private String accountTypeName;
	
	
	
	
	 
	 
	 
	/**
	 * @return the accountTypeId
	 */
	public Integer getAccountTypeId() {
		return accountTypeId;
	}
	/**
	 * @param accountTypeId the accountTypeId to set
	 */
	public void setAccountTypeId(Integer accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	/**
	 * @return the accountTypeName
	 */
	public String getAccountTypeName() {
		return accountTypeName;
	}
	/**
	 * @param accountTypeName the accountTypeName to set
	 */
	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}
	/**
	 * @return the counter
	 */
	public Long getCounter() {
		return counter;
	}
	/**
	 * @param counter the counter to set
	 */
	public void setCounter(Long counter) {
		this.counter = counter;
	}
	/**
	 * @return the points
	 */
	public Double getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(Double points) {
		this.points = points;
	}
	/**
	 * @return the statusUpdatedDate
	 */
	public Date getStatusUpdatedDate() {
		return statusUpdatedDate;
	}
	/**
	 * @param statusUpdatedDate the statusUpdatedDate to set
	 */
	public void setStatusUpdatedDate(Date statusUpdatedDate) {
		this.statusUpdatedDate = statusUpdatedDate;
	}
	 
	/**
	 * @return the statusID
	 */
	public Integer getStatusID() {
		return statusID;
	}
	/**
	 * @param statusID the statusID to set
	 */
	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the loyaltyID
	 */
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	/**
	 * @param loyaltyID the loyaltyID to set
	 */
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	/**
	 * @return the aDSLNumber
	 */
	public String getADSLNumber() {
		return ADSLNumber;
	}
	/**
	 * @param number the aDSLNumber to set
	 */
	public void setADSLNumber(String number) {
		ADSLNumber = number;
	}
	 
	 
	
		
}//class
