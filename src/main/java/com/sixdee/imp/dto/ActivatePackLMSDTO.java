package com.sixdee.imp.dto;

/**
 * 
 * @author Somesh
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
 * <td>April 23,2013 09:49:31 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class ActivatePackLMSDTO extends CommonVO implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private String promoName;
	private int packID;
	private String startDate;
	private String endDate;
	private String activationDate;
	private String serviceAmt;
	private String transactionType;
	private String serviceResponse;
	
	public int getPackID() {
		return packID;
	}
	public void setPackID(int packID) {
		this.packID = packID;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getServiceAmt() {
		return serviceAmt;
	}
	public void setServiceAmt(String serviceAmt) {
		this.serviceAmt = serviceAmt;
	}
	public String getServiceResponse() {
		return serviceResponse;
	}
	public void setServiceResponse(String serviceResponse) {
		this.serviceResponse = serviceResponse;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
