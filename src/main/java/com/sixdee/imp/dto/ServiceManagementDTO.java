package com.sixdee.imp.dto;

import java.io.Serializable;
import java.util.HashMap;

public class ServiceManagementDTO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String 		moNumber  	=null;
	private String		timestamp			=null;
	private String 		channel				=null;
	private String 		languageId			=null;
	private int    		serviceIdentifier 	=0;
	private String isSoftDelete =null;
	private String isPortIn=null;
	private String changeMsisdn=null; 
	private String oldRatePlan=null; 
	private String newRatePlan=null;
	private String oldContractType=null;
	private String newContractType=null;
	private String oldAccountNumber=null;
	private String newAccountNumber=null; 
	private String accountType=null; 
	private String referreeNumber=null; 
	private String fraudStatus=null;
	private String crn=null;
	private String lineType=null; 
	private String customerSegment=null; 
	private String asa=null;
	private String nationalId=null; 
	private String nationalIdType=null; 
	private String nationality=null; 
	private String accountCategoryType=null; 
	private String customerName=null; 
	private String oldTelecoAdmin=null;  
	private String newTelecoAdmin=null;
	private HashMap<String,String> dataMap = null;
	private String responseKey = null;
	private String loyaltyId=null;
	private String customerReferenceNumber=null; 
	private String oldContactNumber=null;  
	private String newContactNumber=null;
	private String parentMsisdn=null;
	
	
	
	public String getOldContactNumber() {
		return oldContactNumber;
	}
	public String getNewContactNumber() {
		return newContactNumber;
	}
	public void setOldContactNumber(String oldContactNumber) {
		this.oldContactNumber = oldContactNumber;
	}
	public void setNewContactNumber(String newContactNumber) {
		this.newContactNumber = newContactNumber;
	}
	public String getCustomerReferenceNumber() {
		return customerReferenceNumber;
	}
	public void setCustomerReferenceNumber(String customerReferenceNumber) {
		this.customerReferenceNumber = customerReferenceNumber;
	}
	public String getResponseKey() {
		return responseKey;
	}
	public void setResponseKey(String responseKey) {
		this.responseKey = responseKey;
	}
	public HashMap<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(HashMap<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	public String getMoNumber() {
		return moNumber;
	}
	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
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
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public int getServiceIdentifier() {
		return serviceIdentifier;
	}
	public void setServiceIdentifier(int serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}
	public String getIsSoftDelete() {
		return isSoftDelete;
	}
	public void setIsSoftDelete(String isSoftDelete) {
		this.isSoftDelete = isSoftDelete;
	}
	public String getIsPortIn() {
		return isPortIn;
	}
	public void setIsPortIn(String isPortIn) {
		this.isPortIn = isPortIn;
	}

	public String getChangeMsisdn() {
		return changeMsisdn;
	}
	public void setChangeMsisdn(String changeMsisdn) {
		this.changeMsisdn = changeMsisdn;
	}
	public String getOldRatePlan() {
		return oldRatePlan;
	}
	public void setOldRatePlan(String oldRatePlan) {
		this.oldRatePlan = oldRatePlan;
	}
	public String getNewRatePlan() {
		return newRatePlan;
	}
	public void setNewRatePlan(String newRatePlan) {
		this.newRatePlan = newRatePlan;
	}
	public String getOldContractType() {
		return oldContractType;
	}
	public void setOldContractType(String oldContractType) {
		this.oldContractType = oldContractType;
	}
	public String getNewContractType() {
		return newContractType;
	}
	public void setNewContractType(String newContractType) {
		this.newContractType = newContractType;
	}
	public String getOldAccountNumber() {
		return oldAccountNumber;
	}
	public void setOldAccountNumber(String oldAccountNumber) {
		this.oldAccountNumber = oldAccountNumber;
	}
	public String getNewAccountNumber() {
		return newAccountNumber;
	}
	public void setNewAccountNumber(String newAccountNumber) {
		this.newAccountNumber = newAccountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getReferreeNumber() {
		return referreeNumber;
	}
	public void setReferreeNumber(String referreeNumber) {
		this.referreeNumber = referreeNumber;
	}
	public String getFraudStatus() {
		return fraudStatus;
	}
	public void setFraudStatus(String fraudStatus) {
		this.fraudStatus = fraudStatus;
	}
	public String getCrn() {
		return crn;
	}
	public void setCrn(String crn) {
		this.crn = crn;
	}
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getCustomerSegment() {
		return customerSegment;
	}
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}
	public String getAsa() {
		return asa;
	}
	public void setAsa(String asa) {
		this.asa = asa;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public String getNationalIdType() {
		return nationalIdType;
	}
	public void setNationalIdType(String nationalIdType) {
		this.nationalIdType = nationalIdType;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getAccountCategoryType() {
		return accountCategoryType;
	}
	public void setAccountCategoryType(String accountCategoryType) {
		this.accountCategoryType = accountCategoryType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getOldTelecoAdmin() {
		return oldTelecoAdmin;
	}
	public void setOldTelecoAdmin(String oldTelecoAdmin) {
		this.oldTelecoAdmin = oldTelecoAdmin;
	}
	public String getNewTelecoAdmin() {
		return newTelecoAdmin;
	}
	public void setNewTelecoAdmin(String newTelecoAdmin) {
		this.newTelecoAdmin = newTelecoAdmin;
	}
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public String getParentMsisdn() {
		return parentMsisdn;
	}
	public void setParentMsisdn(String parentMsisdn) {
		this.parentMsisdn = parentMsisdn;
	}
	@Override
	public String toString() {
		return "[moNumber=" + moNumber + ", transactionId=" + getTransactionId() + ", timestamp="
				+ timestamp + ", channel=" + channel + ", languageId=" + languageId + ", serviceIdentifier="
				+ serviceIdentifier + ", isSoftDelete=" + isSoftDelete + ", isPortIn=" + isPortIn + ", changeMsisdn=" + changeMsisdn + ", oldRatePlan=" + oldRatePlan + ", newRatePlan="
				+ newRatePlan + ", oldContractType=" + oldContractType + ", newContractType=" + newContractType
				+ ", oldAccountNumber=" + oldAccountNumber + ", newAccountNumber=" + newAccountNumber + ", accountType="
				+ accountType + ", referreeNumber=" + referreeNumber + ", fraudStatus=" + fraudStatus + ", crn=" + crn
				+ ", lineType=" + lineType + ", customerSegment=" + customerSegment + ", asa=" + asa + ", nationalId="
				+ nationalId + ", nationalIdType=" + nationalIdType + ", nationality=" + nationality
				+ ", accountCategoryType=" + accountCategoryType + ", customerName=" + customerName
				+ ", oldTelecoAdmin=" + oldTelecoAdmin + ", newTelecoAdmin=" + newTelecoAdmin + "]";
	}
	
	public static void main(String arg[])
	{
		ServiceManagementDTO service=new ServiceManagementDTO();
		System.out.println(service.toString());
	}
	
}
