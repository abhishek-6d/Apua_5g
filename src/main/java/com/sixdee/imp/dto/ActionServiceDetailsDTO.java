package com.sixdee.imp.dto;

import java.util.Date;

public class ActionServiceDetailsDTO {

	private int serviceID;
	private String serviceName;
	private String actionName;
	private Integer actionType;
	private int noOfTimes;
	private Date startDate;
	private Date endDate;
	private String repeat;
	private Integer parentID;
	private Integer validity;
	private String validityType;
	private String startTime;
	private String endTime;
	private String everyDay;
	private boolean isParent ;
	
	
	
	 
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	/**
	 * @return the everyDay
	 */
	public String getEveryDay() {
		return everyDay;
	}
	/**
	 * @param everyDay the everyDay to set
	 */
	public void setEveryDay(String everyDay) {
		this.everyDay = everyDay;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the validity
	 */
	public Integer getValidity() {
		return validity;
	}
	/**
	 * @param validity the validity to set
	 */
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	/**
	 * @return the validityType
	 */
	public String getValidityType() {
		return validityType;
	}
	/**
	 * @param validityType the validityType to set
	 */
	public void setValidityType(String validityType) {
		this.validityType = validityType;
	}
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}
	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
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
	 * @return the noOfTimes
	 */
	public int getNoOfTimes() {
		return noOfTimes;
	}
	/**
	 * @param noOfTimes the noOfTimes to set
	 */
	public void setNoOfTimes(int noOfTimes) {
		this.noOfTimes = noOfTimes;
	}
	/**
	 * @return the parentID
	 */
	public Integer getParentID() {
		return parentID;
	}
	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}
	/**
	 * @return the repeat
	 */
	public String getRepeat() {
		return repeat;
	}
	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(String repeat) {
		this.repeat = repeat;
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
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
	 * @return the actionType
	 */
	public Integer getActionType() {
		return actionType;
	}
	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}
	 
	
	
	
	
}
