/**
 * 
 */
package com.sixdee.imp.simulator.dto;

import java.util.Date;

import com.sixdee.imp.dto.AccountNumberTabDTO;

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
public class AlterationAccountNumber {

	private String accountNumber;
	private Long loyaltyID;
	private Double points;
	private Integer statusID;
	private Date statusUpdatedDate;
	private Long counter;
	private Long id;
	private int processStatus = 100;
	
	public AlterationAccountNumber() {
		// TODO Auto-generated constructor stub
	}
	
	public AlterationAccountNumber(AccountNumberTabDTO account) {
		this.accountNumber = account.accountNumber;
		this.loyaltyID = account.loyaltyID;
		this.id = account.getId();
		this.points = account.points;
		this.statusID = account.statusID;
		this.statusUpdatedDate = account.statusUpdatedDate;
		this.counter = account.getCounter();
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
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
	public Long getCounter() {
		return counter;
	}
	public void setCounter(Long counter) {
		this.counter = counter;
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
	
	
}
