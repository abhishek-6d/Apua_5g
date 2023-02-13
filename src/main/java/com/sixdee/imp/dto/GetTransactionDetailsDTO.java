package com.sixdee.imp.dto;

/**
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * <td>Version</td>
 * </tr>
 * <tr>
 * <td>May 06, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * <td>1.0.0</td>
 * </tr>
 * </table>
 * </p>
 */

import java.io.Serializable;

public class GetTransactionDetailsDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loyaltyId=null;
	private String msisdn = null;
	private String subscriberNumber = null;
	private int noOfLastTransactions = 0;
	private String fromDate = null;
	private String endDate = null;
	private int noOfMonths = 0;
	private int offset = 0;
	private int limit = 0;
	private int rowCount = 0;
	private boolean isAdsl = false;
	private int pin = 0;
	private String langId = null;
	private Object obj = null;

	
	public String getLoyaltyId() {
		return loyaltyId;
	}

	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}

	private String statusId;

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getLangId() {
		return langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public boolean isAdsl() {
		return isAdsl;
	}

	public void setAdsl(boolean isAdsl) {
		this.isAdsl = isAdsl;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public int getNoOfLastTransactions() {
		return noOfLastTransactions;
	}

	public void setNoOfLastTransactions(int noOfLastTransactions) {
		this.noOfLastTransactions = noOfLastTransactions;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getNoOfMonths() {
		return noOfMonths;
	}

	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
