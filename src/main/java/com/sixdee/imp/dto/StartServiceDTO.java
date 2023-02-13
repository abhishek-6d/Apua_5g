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
 * <td>January 02,2015 07:09:43 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class StartServiceDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String transactionId;
	private String timestamp;
	private String channel;
	private Data[] data;
	
	private String serviceName;
	private String subscriberNumber;
	
	private String nationality;	
	private String dob;
	private String nationalId;
	private String englishFirstName;
	private String arbicFirstName;
	private String customerPhoneType;
	private String gender;
	
	private int languageId;

	private int accountType;
	public boolean portLn=false;
	
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public boolean isPortLn() {
		return portLn;
	}
	public void setPortLn(boolean portLn) {
		this.portLn = portLn;
	}
	/**
	 * @return the nationalId
	 */
	public String getNationalId() {
		return nationalId;
	}
	/**
	 * @param nationalId the nationalId to set
	 */
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}
	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the data
	 */
	public Data[] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Data[] data) {
		this.data = data;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
	public String getEnglishFirstName() {
		return englishFirstName;
	}
	public void setEnglishFirstName(String englishFirstName) {
		this.englishFirstName = englishFirstName;
	}
	public String getArbicFirstName() {
		return arbicFirstName;
	}
	public void setArbicFirstName(String arbicFirstName) {
		this.arbicFirstName = arbicFirstName;
	}
	public String getCustomerPhoneType() {
		return customerPhoneType;
	}
	public void setCustomerPhoneType(String customerPhoneType) {
		this.customerPhoneType = customerPhoneType;
	}
	
	
	

}
