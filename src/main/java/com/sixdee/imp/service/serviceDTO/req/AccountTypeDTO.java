package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class AccountTypeDTO 
{
	
	private String transactionId;
	private String timestamp;
	private String channel;
	private String subscriberNumber;
	private Data[] data;
	private int pin;
	private String languageID;
	private Integer currentAccountType;
	private Integer newAccountType;
	private String accountNumber ;
	
	

	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	 
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	 
	public Integer getCurrentAccountType() {
		return currentAccountType;
	}
	public void setCurrentAccountType(Integer currentAccountType) {
		this.currentAccountType = currentAccountType;
	}
	public Integer getNewAccountType() {
		return newAccountType;
	}
	public void setNewAccountType(Integer newAccountType) {
		this.newAccountType = newAccountType;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	
	

}
