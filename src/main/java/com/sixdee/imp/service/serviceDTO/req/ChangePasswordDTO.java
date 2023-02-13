package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class ChangePasswordDTO 
{
	
	private String subscriberNumber;
	private String transactionID;
	private String timestamp;
	private String channel;
	private Data[] data;
	private int oldPin;
	private int newPin;
	private int confirmPin;
	private String languageId;
	
	 
	
	
	
	public int getConfirmPin() {
		return confirmPin;
	}
	public void setConfirmPin(int confirmPin) {
		this.confirmPin = confirmPin;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public int getNewPin() {
		return newPin;
	}
	public void setNewPin(int newPin) {
		this.newPin = newPin;
	}
	public int getOldPin() {
		return oldPin;
	}
	public void setOldPin(int oldPin) {
		this.oldPin = oldPin;
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
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

}
