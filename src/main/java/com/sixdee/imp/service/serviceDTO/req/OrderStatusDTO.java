package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class OrderStatusDTO  
{
	
	private String subscriberNumber;
	private String[] orderIds;
	private int statusId;
	private String timestamp;
	private String transactionId;
	private Data[] data;
	private String channel;
	private int pin;
	private String languageID;
	
	
	
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the data
	 */
	public Data[] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Data[] data) {
		this.data = data;
	}
	/**
	 * @return the orderIds
	 */
	public String[] getOrderIds() {
		return orderIds;
	}
	/**
	 * @param orderIds the orderIds to set
	 */
	public void setOrderIds(String[] orderIds) {
		this.orderIds = orderIds;
	}
	/**
	 * @return the statusId
	 */
	public int getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the subscriberNumber
	 */
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * @param subscriberNumber the subscriberNumber to set
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
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
