package com.sixdee.imp.dto;

/**
 * 
 * @author Paramesh
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
 * <td>July 25,2013 03:12:10 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class PointDetailsDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private String defaultLanguage;
	private Long loyaltyID;
	private String fromDate;
	private String toDate;
	private int noOfMonths;
	private Data[] data;
	private int offSet;
	private int limit;
	private int pin;
	private Date calculatedFromDate;
	private Date calculatedToDate;
	private int rowCount;
	private List<PointDetailsInfoDTO> expiryPointsList;
	private String rewardPoints;
	private List<Data> dataSet;
	
	
	
	
	
	public List<Data> getDataSet() {
		return dataSet;
	}
	public void setDataSet(List<Data> dataSet) {
		this.dataSet = dataSet;
	}
	public String getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public List<PointDetailsInfoDTO> getExpiryPointsList() {
		return expiryPointsList;
	}
	public void setExpiryPointsList(List<PointDetailsInfoDTO> expiryPointsList) {
		this.expiryPointsList = expiryPointsList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public Date getCalculatedFromDate() {
		return calculatedFromDate;
	}
	public void setCalculatedFromDate(Date calculatedFromDate) {
		this.calculatedFromDate = calculatedFromDate;
	}
	public Date getCalculatedToDate() {
		return calculatedToDate;
	}
	public void setCalculatedToDate(Date calculatedToDate) {
		this.calculatedToDate = calculatedToDate;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	 
	public String getDefaultLanguage() {
		return defaultLanguage;
	}
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public int getNoOfMonths() {
		return noOfMonths;
	}
	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	 
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
	

}
