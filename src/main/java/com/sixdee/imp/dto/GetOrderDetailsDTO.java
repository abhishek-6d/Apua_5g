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
 * <td>May 29,2013 12:11:41 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class GetOrderDetailsDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msisdn = null;
	private String subscriberNumber = null;
	private int noOfLastTransactions = 0;
	private String fromDate = null;
	private String endDate = null;
	private int noOfMonths = 0;
	private int offset = 0;
	private int limit = 0;
	private int rowCount = 0;
	private String langId = null;
	private Object obj = null;
	private boolean isLuluVoucher=false;
	
	/**
	 * @return the isLuluVoucher
	 */
	public boolean isLuluVoucher() {
		return isLuluVoucher;
	}
	/**
	 * @param isLuluVoucher the isLuluVoucher to set
	 */
	public void setLuluVoucher(boolean isLuluVoucher) {
		this.isLuluVoucher = isLuluVoucher;
	}
	public String getLangId() {
		return langId;
	}
	public void setLangId(String langId) {
		this.langId = langId;
	}
	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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
	 * @return the noOfLastTransactions
	 */
	public int getNoOfLastTransactions() {
		return noOfLastTransactions;
	}
	/**
	 * @param noOfLastTransactions the noOfLastTransactions to set
	 */
	public void setNoOfLastTransactions(int noOfLastTransactions) {
		this.noOfLastTransactions = noOfLastTransactions;
	}
	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the noOfMonths
	 */
	public int getNoOfMonths() {
		return noOfMonths;
	}
	/**
	 * @param noOfMonths the noOfMonths to set
	 */
	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}
	/**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	/**
	 * @return the obj
	 */
	public Object getObj() {
		return obj;
	}
	/**
	 * @param obj the obj to set
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}
	

}
