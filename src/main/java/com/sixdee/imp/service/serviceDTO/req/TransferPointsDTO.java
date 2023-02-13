package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class TransferPointsDTO 
{
	
	private String subscriberNumber;
	private int points;
	private String destSubscriberNumber;
	private String transactionID;
	private String timestamp;
	private Data[] data;
	private String channel;
	private Integer pin;
	private String languageID;
	private boolean isPointsReqd ;
	
	
	
	
	
	
	
	
	
	public boolean isPointsReqd() {
		return isPointsReqd;
	}
	public void setPointsReqd(boolean isPointsReqd) {
		this.isPointsReqd = isPointsReqd;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
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
	public String getDestSubscriberNumber() {
		return destSubscriberNumber;
	}
	public void setDestSubscriberNumber(String destSubscriberNumber) {
		this.destSubscriberNumber = destSubscriberNumber;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
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
