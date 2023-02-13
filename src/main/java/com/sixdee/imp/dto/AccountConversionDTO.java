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
 * <td>January 21,2014 12:55:09 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.HashMap;


public class AccountConversionDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber = null;  //Subscriber Number which has changed the category
	private String accountNumber = null;
	private int newTypeId  	= 0;     //TypeId of Subscriber Number before changing to new one
	
	private String newType          = null;  //Type of Subscriber Number before changing to new one
	private int currentTypeId		= 0;     //TypeId of Subscriber Number now
	private String currentType		= null;  //TypeId of Subscriber Number now
	private String activity = null;
	
	private String oldAccountNumber = null;
	
	private HashMap<String, String> optionsMap = null;
	
	
	
	
	
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public HashMap<String, String> getOptionsMap() {
		return optionsMap;
	}
	public void setOptionsMap(HashMap<String, String> optionsMap) {
		this.optionsMap = optionsMap;
	}
	public String getOldAccountNumber() {
		return oldAccountNumber;
	}
	public void setOldAccountNumber(String oldAccountNumber) {
		this.oldAccountNumber = oldAccountNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getNewTypeId() {
		return newTypeId;
	}
	public void setNewTypeId(int newTypeId) {
		this.newTypeId = newTypeId;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	
	public String getNewType() {
		return newType;
	}
	public void setNewType(String newType) {
		this.newType = newType;
	}
	public int getCurrentTypeId() {
		return currentTypeId;
	}
	public void setCurrentTypeId(int currentTypeId) {
		this.currentTypeId = currentTypeId;
	}
	public String getCurrentType() {
		return currentType;
	}
	public void setCurrentType(String currentType) {
		this.currentType = currentType;
	}
	
	
	
}
