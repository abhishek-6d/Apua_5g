package com.sixdee.imp.dto;

/**
 * 
 * @author Rahul K K
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
 * <td>July 10,2013 04:00:18 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class RedeemIRDTO extends CommonVO implements Serializable {


	/**
	 * 
	 */
	private String subscriberNumber = null;
	private String mode 			= null;
	private String offerId			= null;
	private boolean isTransfer = false;
	private boolean isRefReqd  = false; 
	
	
	
	public boolean isRefReqd() {
		return isRefReqd;
	}
	public void setRefReqd(boolean isRefReqd) {
		this.isRefReqd = isRefReqd;
	}
	public boolean isTransfer() {
		return isTransfer;
	}
	public void setTransfer(boolean isTransfer) {
		this.isTransfer = isTransfer;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	

}
