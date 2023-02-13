package com.sixdee.imp.dto;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 03,2013 11:27:12 AM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class UpdateAccountDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msisdn;
	private String dedicatedActId;
	private String amountChanrged;
	private String freeUnits;
	private String validity;
	private String factor;
	private String extendExpiry;
	private int operation;
	private String transactionId;
	
	public String getAmountChanrged() {
		return amountChanrged;
	}
	public void setAmountChanrged(String amountChanrged) {
		this.amountChanrged = amountChanrged;
	}
	public String getDedicatedActId() {
		return dedicatedActId;
	}
	public void setDedicatedActId(String dedicatedActId) {
		this.dedicatedActId = dedicatedActId;
	}
	public String getExtendExpiry() {
		return extendExpiry;
	}
	public void setExtendExpiry(String extendExpiry) {
		this.extendExpiry = extendExpiry;
	}
	public String getFactor() {
		return factor;
	}
	public void setFactor(String factor) {
		this.factor = factor;
	}
	public String getFreeUnits() {
		return freeUnits;
	}
	public void setFreeUnits(String freeUnits) {
		this.freeUnits = freeUnits;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public int getOperation() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = operation;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}

}
