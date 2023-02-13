package com.sixdee.imp.dto;

import java.io.Serializable;
import java.util.Date;

public class LoyaltyRegisteredNumberTabDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long loyaltyID;
	private String linkedNumber;
	private String accountNumber;
	private Integer statusID;
	private Date statusUpdatedDate;
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
	 * @return the linkedNumber
	 */
	public String getLinkedNumber() {
		return linkedNumber;
	}
	/**
	 * @param linkedNumber the linkedNumber to set
	 */
	public void setLinkedNumber(String linkedNumber) {
		this.linkedNumber = linkedNumber;
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
	
	
	
	
}//LoyaltyRegisteredNumberTabDTO
