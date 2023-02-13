package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class RewardPointsDTO {

	private String subscriberNumber;
	private Integer rewardPointsCategory;
	private Double volume;
	private Double rewardPoints;
	private Double statusPoints;
	private String transactionID;
	private String timestamp;
	private Data[] data;
	private String channel;
	private Boolean isStatusPointsOnly=false;
	private int pin;
	
	
	
	
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	/**
	 * @return the isStatusPointsOnly
	 */
	public Boolean isStatusPointsOnly() {
		return isStatusPointsOnly;
	}
	/**
	 * @param isStatusPointsOnly the isStatusPointsOnly to set
	 */
	public void setStatusPointsOnly(Boolean isStatusPointsOnly) {
		this.isStatusPointsOnly = isStatusPointsOnly;
	}
	/**
	 * @return the rewardPoints
	 */
	public Double getRewardPoints() {
		return rewardPoints;
	}
	/**
	 * @param rewardPoints the rewardPoints to set
	 */
	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	/**
	 * @return the statusPoints
	 */
	public Double getStatusPoints() {
		return statusPoints;
	}
	/**
	 * @param statusPoints the statusPoints to set
	 */
	public void setStatusPoints(Double statusPoints) {
		this.statusPoints = statusPoints;
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
	 * @return the rewardPointsCategory
	 */
	public Integer getRewardPointsCategory() {
		return rewardPointsCategory;
	}
	/**
	 * @param rewardPointsCategory the rewardPointsCategory to set
	 */
	public void setRewardPointsCategory(Integer rewardPointsCategory) {
		this.rewardPointsCategory = rewardPointsCategory;
	}
	/**
	 * @return the volume
	 */
	public Double getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
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
	
	
	
	
}
