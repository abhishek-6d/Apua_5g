package com.sixdee.imp.service.serviceDTO.resp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sixdee.imp.dto.LoyaltyBalance;
import com.sixdee.imp.dto.ProfileInfo;


public class UserDTO extends ResponseDTO  
{
	
	private String loyaltyID;                          
	                 
	private String firstName;                          
	private String lastName;
	private String accountNumber ;
	private String contactNumber;
	private String dateOfBirth;
	                    
	private String  emailID;                           
	private String address;
	private String vipCode;    
	private String occupation;                         
	private String category;
	private String industry;
	private String defaultLanguage;
	private String nationalID;
	private String status;
	private String statusUpdateDate;
	private String tier;
	private String tierUpdateDate;
	private String statusPoints;
	private String rewardPoints;
	private String tiername;
	private String businessClubMembership;

	
	
	/*private Integer statusId;
	
	
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer integer) {
		this.statusId = integer;
	}*/
	public String getTiername() {
		return tiername;
	}
	public String getBusinessClubMembership() {
		return businessClubMembership;
	}
	public void setBusinessClubMembership(String businessClubMembership) {
		this.businessClubMembership = businessClubMembership;
	}
	public void setTiername(String tiername) {
		this.tiername = tiername;
	}
	public String getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(String statusPoints) {
		this.statusPoints = statusPoints;
	}
	public String getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusUpdateDate() {
		return statusUpdateDate;
	}
	public void setStatusUpdateDate(String statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getTierUpdateDate() {
		return tierUpdateDate;
	}
	public void setTierUpdateDate(String tierUpdateDate) {
		this.tierUpdateDate = tierUpdateDate;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param date the dateOfBirth to set
	 */
	public void setDateOfBirth(String date) {
		this.dateOfBirth = date;
	}
	/**
	 * @return the defaultLanguage
	 */
	public String getDefaultLanguage() {
		return defaultLanguage;
	}
	/**
	 * @param defaultLanguage the defaultLanguage to set
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return emailID;
	}
	/**
	 * @param emailID the emailID to set
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}
	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the loyaltyID
	 */
	public String getLoyaltyID() {
		return loyaltyID;
	}
	/**
	 * @param loyaltyID the loyaltyID to set
	 */
	public void setLoyaltyID(String loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	/**
	 * @return the nationalID
	 */
	public String getNationalID() {
		return nationalID;
	}
	/**
	 * @param nationalID the nationalID to set
	 */
	public void setNationalID(String nationalID) {
		this.nationalID = nationalID;
	}
	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}
	/**
	 * @param occupation the occupation to set
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	/**
	 * @return the vipCode
	 */
	public String getVipCode() {
		return vipCode;
	}
	/**
	 * @param vipCode the vipCode to set
	 */
	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}
	
	
}
