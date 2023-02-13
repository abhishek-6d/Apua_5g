package com.sixdee.imp.service.serviceDTO.req;

public class LoyaltyTransactionsDTO {
	
	private String transactionId = null;
	private String timeStamp = null;
	private String msisdn = null;
	private String subsNumber = null;
	private int channel = 0;
	private int lastNTransactions = 0;
	private String fromDate = null;
	private String endDate = null;
	private int nMonths = 0;
	private int offset = 0;
	private int limit = 0;
	
	
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getSubsNumber() {
		return subsNumber;
	}
	public void setSubsNumber(String subsNumber) {
		this.subsNumber = subsNumber;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public int getLastNTransactions() {
		return lastNTransactions;
	}
	public void setLastNTransactions(int lastNTransactions) {
		this.lastNTransactions = lastNTransactions;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getnMonths() {
		return nMonths;
	}
	public void setnMonths(int nMonths) {
		this.nMonths = nMonths;
	}
	
	
}
