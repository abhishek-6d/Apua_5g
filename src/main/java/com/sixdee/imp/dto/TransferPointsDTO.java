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
 * <td>April 24,2013 04:37:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class TransferPointsDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String subscriberNumber;
	private String destinationSubscriberNumber;
	private Long transferPoints;
	private boolean isTransfer;
	private Long fromLoyaltyId;
	private Long toLoyaltyId;
	private String channel;
	private Integer pin;
	private String subLanguageID;
	
	
	
	public String getSubLanguageID() {
		return subLanguageID;
	}
	public void setSubLanguageID(String subLanguageID) {
		this.subLanguageID = subLanguageID;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getDestinationSubscriberNumber() {
		return destinationSubscriberNumber;
	}
	public void setDestinationSubscriberNumber(String destinationSubscriberNumber) {
		this.destinationSubscriberNumber = destinationSubscriberNumber;
	}
	public Long getFromLoyaltyId() {
		return fromLoyaltyId;
	}
	public void setFromLoyaltyId(Long fromLoyaltyId) {
		this.fromLoyaltyId = fromLoyaltyId;
	}
	public boolean isTransfer() {
		return isTransfer;
	}
	public void setTransfer(boolean isTransfer) {
		this.isTransfer = isTransfer;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public Long getToLoyaltyId() {
		return toLoyaltyId;
	}
	public void setToLoyaltyId(Long toLoyaltyId) {
		this.toLoyaltyId = toLoyaltyId;
	}
	public Long getTransferPoints() {
		return transferPoints;
	}
	public void setTransferPoints(Long transferPoints) {
		this.transferPoints = transferPoints;
	}
	
	
	
	
	
	

}
