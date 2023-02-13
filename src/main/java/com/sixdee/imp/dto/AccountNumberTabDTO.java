package com.sixdee.imp.dto;

import java.util.Date;

public class AccountNumberTabDTO {

	public String accountNumber ;
	public Long loyaltyID;
	public Double points = 0D;
	public Integer statusID = 1;
	public Date statusUpdatedDate;
	private Long counter = 0L;
	private Long id;
	private int isAsa = 0;
	
	
	public int getIsAsa() {
		return isAsa;
	}
	public void setIsAsa(int isAsa) {
		this.isAsa = isAsa;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	
		
}//class
