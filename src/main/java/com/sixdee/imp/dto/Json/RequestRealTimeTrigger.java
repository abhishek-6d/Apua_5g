package com.sixdee.imp.dto.Json;

public class RequestRealTimeTrigger {
	private String transactionId;
	private String timeStamp;
	private String channel;
	private String msisdn;
	private String accountNumber;
	private String customerRefernceNumber ;
	private String keyword;
	private String fdn;
	private NotificationBox notificationBox;
	private Object obj = null;
	
	
	
	
	public String getCustomerRefernceNumber() {
		return customerRefernceNumber;
	}
	public void setCustomerRefernceNumber(String customerRefernceNumber) {
		this.customerRefernceNumber = customerRefernceNumber;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getTransactionID() {
		return transactionId;
	}
	public void setTransactionID(String transactionID) {
		this.transactionId = transactionID;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public NotificationBox getNotificationBox() {
		return notificationBox;
	}
	public void setNotificationBox(NotificationBox notificationBox) {
		this.notificationBox = notificationBox;
	}
	public String getFdn() {
		return fdn;
	}
	public void setFdn(String fdn) {
		this.fdn = fdn;
	}
	
	
	

}
