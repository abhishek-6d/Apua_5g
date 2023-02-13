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
 * <td>July 24,2013 03:58:55 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class ChangePasswordDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private Long loyaltyID;
	private String defaultLanguage;
	private int pin;
	private int oldPin;
	
	
	
	public int getOldPin() {
		return oldPin;
	}
	public void setOldPin(int oldPin) {
		this.oldPin = oldPin;
	}
	public String getDefaultLanguage() {
		return defaultLanguage;
	}
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
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
	
	
	
}
