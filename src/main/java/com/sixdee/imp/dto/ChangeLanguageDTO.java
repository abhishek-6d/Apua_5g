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
 * <td>September 05,2013 07:34:59 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class ChangeLanguageDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	
	private int oldLanguageID;
	
	private int newLanguageID;
	
	private String languageID;
	
	/**
	 * @return the languageID
	 */
	public String getLanguageID() {
		return languageID;
	}
	/**
	 * @param languageID the languageID to set
	 */
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
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
	 * @return the oldLanguageID
	 */
	public int getOldLanguageID() {
		return oldLanguageID;
	}
	/**
	 * @param oldLanguageID the oldLanguageID to set
	 */
	public void setOldLanguageID(int oldLanguageID) {
		this.oldLanguageID = oldLanguageID;
	}
	/**
	 * @return the newLanguageID
	 */
	public int getNewLanguageID() {
		return newLanguageID;
	}
	/**
	 * @param newLanguageID the newLanguageID to set
	 */
	public void setNewLanguageID(int newLanguageID) {
		this.newLanguageID = newLanguageID;
	}
}
