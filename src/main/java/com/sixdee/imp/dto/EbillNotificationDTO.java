package com.sixdee.imp.dto;

/**
 * 
 * @author Himanshu chaudhary
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>October 15,2015 05:08:47 PM</td>
 * <td>Himanshu chaudhary</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class EbillNotificationDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private String accountNumber;
	private String date;
	private String customerType;
	private String customerNumber;
	private Long loyaltyID;
	private boolean isNotSubscriber= false;
	private boolean isNotAccount = false;
	private boolean isCount = false;
	private String requestDate;
	private String customerPrimaryNumer;
	private String transitionId;
	
	
	public boolean isCount() {
		return isCount;
	}
	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}
	public String getTransitionId() {
		return transitionId;
	}
	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}
	public String getCustomerPrimaryNumer() {
		return customerPrimaryNumer;
	}
	public void setCustomerPrimaryNumer(String customerPrimaryNumer) {
		this.customerPrimaryNumer = customerPrimaryNumer;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public boolean isNotSubscriber() {
		return isNotSubscriber;
	}
	public void setNotSubscriber(boolean isNotSubscriber) {
		this.isNotSubscriber = isNotSubscriber;
	}
	public boolean isNotAccount() {
		return isNotAccount;
	}
	public void setNotAccount(boolean isNotAccount) {
		this.isNotAccount = isNotAccount;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	

}
