/**
 * 
 */
package com.sixdee.imp.simulator.dto;

import java.util.Date;

import com.sixdee.imp.dto.SubscriberNumberTabDTO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class AlterationSubscriberNumber {

	private int processStatus = 100;
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
	
	public AlterationSubscriberNumber() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(Long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public Long getLoyaltyID() {
		return loyaltyID;
	}

	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Integer getStatusID() {
		return statusID;
	}

	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}

	public Date getStatusUpdatedDate() {
		return statusUpdatedDate;
	}

	public void setStatusUpdatedDate(Date statusUpdatedDate) {
		this.statusUpdatedDate = statusUpdatedDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}

	public Integer getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Integer accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	
	
	public void convertSubsToThis(SubscriberNumberTabDTO subscriberNumberTabDTO){
		loyaltyID = subscriberNumberTabDTO.loyaltyID;
		subscriberNumber = subscriberNumberTabDTO.subscriberNumber;
		accountCategory = subscriberNumberTabDTO.accountCategory;
		accountNumber = subscriberNumberTabDTO.accountNumber;
		accountTypeId = subscriberNumberTabDTO.getAccountTypeId();
		accountTypeName = subscriberNumberTabDTO.getAccountTypeName();
		createDate = subscriberNumberTabDTO.getCreateDate();
		id = subscriberNumberTabDTO.getId();
		points = subscriberNumberTabDTO.points;
		//processStatus = subscriberNumberTabDTO.g
		statusID = subscriberNumberTabDTO.statusID;
		statusUpdatedDate = subscriberNumberTabDTO.statusUpdatedDate;
		this.counter = subscriberNumberTabDTO.getCounter();
		
	}
}
