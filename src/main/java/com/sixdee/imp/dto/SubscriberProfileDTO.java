package com.sixdee.imp.dto;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>May 14,2013 06:02:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.Date;


public class SubscriberProfileDTO extends CommonVO implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	  private int serialNo;
	  private Long subscriberNumber;
	  private int serviceId;
	  private String serviceName;
	  private String ruleName;
	  private Date startDate;
	  private Date endDate;
	  private Date dailyDate;
	  private int dailyCount;
	  private Date weeklyDate;
	  private int weeklyCount;
	  private Date monthlyDate;
	  private int monthlyCount;
	  private Long counter;
	  private String reqXML;
	  private String status;
	  
	  
	  
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
	public String getReqXML() {
		return reqXML;
	}
	public void setReqXML(String reqXML) {
		this.reqXML = reqXML;
	}
	/**
	 * @return the counter
	 */
	public Long getCounter() {
		return counter;
	}
	/**
	 * @param counter the counter to set
	 */
	public void setCounter(Long counter) {
		this.counter = counter;
	}
	/**
	 * @return the dailyCount
	 */
	public int getDailyCount() {
		return dailyCount;
	}
	/**
	 * @param dailyCount the dailyCount to set
	 */
	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}
	/**
	 * @return the dailyDate
	 */
	public Date getDailyDate() {
		return dailyDate;
	}
	/**
	 * @param dailyDate the dailyDate to set
	 */
	public void setDailyDate(Date dailyDate) {
		this.dailyDate = dailyDate;
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
	 * @return the monthlyCount
	 */
	public int getMonthlyCount() {
		return monthlyCount;
	}
	/**
	 * @param monthlyCount the monthlyCount to set
	 */
	public void setMonthlyCount(int monthlyCount) {
		this.monthlyCount = monthlyCount;
	}
	/**
	 * @return the monthlyDate
	 */
	public Date getMonthlyDate() {
		return monthlyDate;
	}
	/**
	 * @param monthlyDate the monthlyDate to set
	 */
	public void setMonthlyDate(Date monthlyDate) {
		this.monthlyDate = monthlyDate;
	}
	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}
	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	/**
	 * @return the serialNo
	 */
	public int getSerialNo() {
		return serialNo;
	}
	/**
	 * @param serialNo the serialNo to set
	 */
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	/**
	 * @return the serviceId
	 */
	public int getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
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
	 * @return the weeklyCount
	 */
	public int getWeeklyCount() {
		return weeklyCount;
	}
	/**
	 * @param weeklyCount the weeklyCount to set
	 */
	public void setWeeklyCount(int weeklyCount) {
		this.weeklyCount = weeklyCount;
	}
	/**
	 * @return the weeklyDate
	 */
	public Date getWeeklyDate() {
		return weeklyDate;
	}
	/**
	 * @param weeklyDate the weeklyDate to set
	 */
	public void setWeeklyDate(Date weeklyDate) {
		this.weeklyDate = weeklyDate;
	}
	  
	
	
	  
	  
	  
}
