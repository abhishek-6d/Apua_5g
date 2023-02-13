package com.sixdee.imp.dto;

import java.util.Date;

public class SubscriberNumberTabDTO {

	public Long subscriberNumber;
	public Long loyaltyID;
	public String accountNumber;
	public String accountCategory;
	public Double points;
	public Integer statusID;
	public Date statusUpdatedDate;
	public Date createDate;
	private Long counter;
	private Integer accountTypeId;
	private String accountTypeName;
	private Long id;
	public Date pointsUpdatedDate;
	public Long refNumber;
	
	
	 
	 
	public Date getPointsUpdatedDate() {
		return pointsUpdatedDate;
	}
	public void setPointsUpdatedDate(Date pointsUpdatedDate) {
		this.pointsUpdatedDate = pointsUpdatedDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAccountCategory() {
		return accountCategory;
	}
	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	/**
	 * @return the subscriberNumber
	 */
	public Long getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * @param subscriberNumber the subscriberNumber to set
	 */
	public void setSubscriberNumber(Long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	
	public Long getRefNumber() {
		return refNumber;
	}
	public void setRefNumber(Long refNumber) {
		this.refNumber = refNumber;
	}
	 
		
}//class
