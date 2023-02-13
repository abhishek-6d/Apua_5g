package com.sixdee.imp.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sixdee.imp.service.httpcall.dto.SubscriberDataSet;

public class TOLSubscriberCheckDTO {

	private String requestId;
	private String keyWord;
	private String featureId;
	private String timeStamp;
	private String status = null;
	private String statusDesc = null;
	private long scheduleID;
	private long subscriberNumber;
	private int counter;
	private int interval;
	private String actionType;
	private Date date;
	private SubscriberDataSet dataSet;
	private int credit;
	private int debit;
	private String creditStatus;
	private String debitStatus;
	
	
	
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public int getDebit() {
		return debit;
	}
	public void setDebit(int debit) {
		this.debit = debit;
	}
	public String getDebitStatus() {
		return debitStatus;
	}
	public void setDebitStatus(String debitStatus) {
		this.debitStatus = debitStatus;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the keyWord
	 */
	public String getKeyWord() {
		return keyWord;
	}
	/**
	 * @param keyWord the keyWord to set
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}
	/**
	 * @param featureId the featureId to set
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc() {
		return statusDesc;
	}
	/**
	 * @param statusDesc the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	/**
	 * @return the dataSet
	 */
	public SubscriberDataSet getDataSet() {
		return dataSet;
	}
	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(SubscriberDataSet dataSet) {
		this.dataSet = dataSet;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public long getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(long scheduleID) {
		this.scheduleID = scheduleID;
	}
	public long getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	
	
	@Override
	public String toString() {
		return "REQ ID "+requestId+" : Schedule ID ="+scheduleID+" , MDN ="+subscriberNumber+", Credit="+credit+", Debit="+debit+", Credit Status="+creditStatus+", Debit Status="+debitStatus+", Counter="+counter;
	}
	
	
	
}
