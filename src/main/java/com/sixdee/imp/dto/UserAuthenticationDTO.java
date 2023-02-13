package com.sixdee.imp.dto;

import java.io.Serializable;


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
 * <td>April 24,2013 05:54:40 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */


public class UserAuthenticationDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	
	private String channel;
	
	private String pin;
	
	private String languageId;
	
	private boolean isADSLNumber;
	
	private boolean isAuthenticate;	
	
	private String languageID;
	
	
	
	

	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getLanguageID() {
		return languageID;
	}

	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public boolean isAuthenticate() {
		return isAuthenticate;
	}

	public void setAuthenticate(boolean isAuthenticate) {
		this.isAuthenticate = isAuthenticate;
	}

	public boolean isADSLNumber() {
		return isADSLNumber;
	}

	public void setADSLNumber(boolean isADSLNumber) {
		this.isADSLNumber = isADSLNumber;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

}
