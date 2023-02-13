package com.sixdee.imp.dto;

import java.util.Date;

public class SubscriberTransactionHistroyDTO {

	private int id;
	private long subscriberNumber;
	private int serviceID;
	private Date startDate;
	private Date endDate;
	private Date activationDate;
	private int serviceAmt;
	private String transactionType;
	private String serviceResponse;
	private Date date;
	
	
	/**
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * 
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the activationDate
	 */
	public Date getActivationDate() {
		return activationDate;
	}
	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the serviceAmt
	 */
	public int getServiceAmt() {
		return serviceAmt;
	}
	/**
	 * @param serviceAmt the serviceAmt to set
	 */
	public void setServiceAmt(int serviceAmt) {
		this.serviceAmt = serviceAmt;
	}
	/**
	 * @return the serviceID
	 */
	public int getServiceID() {
		return serviceID;
	}
	/**
	 * @param serviceID the serviceID to set
	 */
	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}
	/**
	 * @return the serviceResponse
	 */
	public String getServiceResponse() {
		return serviceResponse;
	}
	/**
	 * @param serviceResponse the serviceResponse to set
	 */
	public void setServiceResponse(String serviceResponse) {
		this.serviceResponse = serviceResponse;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the subscriberNumber
	 */
	public Long getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * @param subscriberNumber the subscriberNumber to set
	 */
	public void setSubscriberNumber(Long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public void setSubscriberNumber(long subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	
	
	
}
