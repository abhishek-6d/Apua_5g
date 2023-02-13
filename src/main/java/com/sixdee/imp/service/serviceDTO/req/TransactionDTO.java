package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class TransactionDTO 
{
	
	private String moNumber;
	private String subscriberNumber;
	private String fromDate;
	private String toDate;
	private int noOfMonths;
	private int noOfLastTransaction;
	private long loyaltyID;
	private String transactionId;
	private String timestamp;
	private Data[] data;
	private String channel;
	private int offSet;
	private int limit;
	private int pin;
	private String languageID;
	
	/**
	 * @return the languageID
	 */
	public String getLanguageID() {
		return languageID;
	}
	/**
	 * @param languageID the languageID to set
	 */
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
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
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public String getMoNumber() {
		return moNumber;
	}
	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
	}
	public int getNoOfLastTransaction() {
		return noOfLastTransaction;
	}
	public void setNoOfLastTransaction(int noOfLastTransaction) {
		this.noOfLastTransaction = noOfLastTransaction;
	}
	public int getNoOfMonths() {
		return noOfMonths;
	}
	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
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
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
}
