package com.sixdee.imp.dto;

/**
 * 
 * @author Himanshu
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
 * <td>October 05,2015 04:03:20 PM</td>
 * <td>Himanshu</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.List;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class TerminationServiceDTO extends CommonVO implements Serializable {

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
	
	private int languageId;
	private int accountType;
	private boolean isPrimary;
	private List<LoyaltyRegisteredNumberTabDTO> numberList;
	private String deleteKey;
	

	public String getDeleteKey() {
		return deleteKey;
	}

	public void setDeleteKey(String deleteKey) {
		this.deleteKey = deleteKey;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public List<LoyaltyRegisteredNumberTabDTO> getNumberList() {
		return numberList;
	}

	public void setNumberList(List<LoyaltyRegisteredNumberTabDTO> numberList) {
		this.numberList = numberList;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Data[] getData() {
		return data;
	}

	public void setData(Data[] data) {
		this.data = data;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	
	
	

}
