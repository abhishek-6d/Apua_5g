/**
 * 
 */
package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

/**
 * @author NITHIN
 *
 */
public class ChangeLanguageDTO {
	
	private String subscriberNumber;
	private String transactionID;
	private String timestamp;
	private String channel;
	private Data[] data;
	private int oldLanguageID;
	private int newLanguageID;
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
	 * @return the transactionID
	 */
	public String getTransactionID() {
		return transactionID;
	}
	/**
	 * @param transactionID the transactionID to set
	 */
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
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
	 * @return the oldLanguageID
	 */
	public int getOldLanguageID() {
		return oldLanguageID;
	}
	/**
	 * @param oldLanguageID the oldLanguageID to set
	 */
	public void setOldLanguageID(int oldLanguageID) {
		this.oldLanguageID = oldLanguageID;
	}
	/**
	 * @return the newLanguageID
	 */
	public int getNewLanguageID() {
		return newLanguageID;
	}
	/**
	 * @param newLanguageID the newLanguageID to set
	 */
	public void setNewLanguageID(int newLanguageID) {
		this.newLanguageID = newLanguageID;
	}
	

}
