package com.sixdee.imp.dto;

import java.util.List;

public class LoyaltyCreationFailedDTO {

	private String transactionID;
	private List<String> subscriberNumber;
	private String channel;
	private Integer status;
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	 
	public List<String> getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(List<String> subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	
	@Override
	public String toString() {
		return "[Transaction ID : "+getTransactionID()+" Subscriber Number List : "+getSubscriberNumber()+" Channel : "+getChannel()+" Status ID : "+getStatus()+"]";
	}
	
}
