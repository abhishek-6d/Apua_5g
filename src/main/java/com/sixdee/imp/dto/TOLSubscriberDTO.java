package com.sixdee.imp.dto;

import java.util.Date;

public class TOLSubscriberDTO {

	private String requestId;
	private String status = null;
	private String statusDesc = null;
	private long scheduleID;
	private long subscriberNumber;
	private int counter;
	private String actionType;
	private Date date;
	private int credit;
	private int debit;
	private String creditStatus;
	private String debitStatus;
	

	
	public String getActionType() {
		return actionType;
	}



	public void setActionType(String actionType) {
		this.actionType = actionType;
	}



	public int getCounter() {
		return counter;
	}



	public void setCounter(int counter) {
		this.counter = counter;
	}



	public int getCredit() {
		return credit;
	}



	public void setCredit(int credit) {
		this.credit = credit;
	}



	public String getCreditStatus() {
		return creditStatus;
	}



	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public int getDebit() {
		return debit;
	}



	public void setDebit(int debit) {
		this.debit = debit;
	}



	public String getDebitStatus() {
		return debitStatus;
	}



	public void setDebitStatus(String debitStatus) {
		this.debitStatus = debitStatus;
	}



	public String getRequestId() {
		return requestId;
	}



	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}



	public long getScheduleID() {
		return scheduleID;
	}



	public void setScheduleID(long scheduleID) {
		this.scheduleID = scheduleID;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getStatusDesc() {
		return statusDesc;
	}



	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}



	public long getSubscriberNumber() {
		return subscriberNumber;
	}



	public void setSubscriberNumber(long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}



	@Override
	public String toString() {
		return "REQ ID "+requestId+" : Schedule ID ="+scheduleID+" , MDN ="+subscriberNumber+", Credit="+credit+", Debit="+debit+", Credit Status="+creditStatus+", Debit Status="+debitStatus+", Counter="+counter;
	}
	
	
	
}
