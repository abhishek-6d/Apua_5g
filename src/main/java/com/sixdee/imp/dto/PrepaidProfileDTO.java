/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;


/**
 * @author NITHIN
 *
 */
public class PrepaidProfileDTO {
	
	private Long id;
	private String msisdn;
	private Date activationDate;
	private String languageId;
	private String keyword;
	private String status;
	//StartService
	private Date dob;
	private String nationality;
	private String nationalId;
	private String englishFirstName;
	private String arabicFirstName;
	private String customerPhoneType;

	private String gender;
	private String portIn;
	//Referral Service
	private String referrer;
	private Date referralDate;
	
	
	
	public String getPortIn() {
		return portIn;
	}
	public void setPortIn(String portIn) {
		this.portIn = portIn;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the referrer
	 */
	public String getReferrer() {
		return referrer;
	}
	/**
	 * @param referrer the referrer to set
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	/**
	 * @return the referralDate
	 */
	public Date getReferralDate() {
		return referralDate;
	}
	/**
	 * @param referralDate the referralDate to set
	 */
	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
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
	public Date getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	public String getEnglishFirstName() {
		return englishFirstName;
	}
	public void setEnglishFirstName(String englishFirstName) {
		this.englishFirstName = englishFirstName;
	}
	public String getArabicFirstName() {
		return arabicFirstName;
	}
	public void setArabicFirstName(String arabicFirstName) {
		this.arabicFirstName = arabicFirstName;
	}
	public String getCustomerPhoneType() {
		return customerPhoneType;
	}
	public void setCustomerPhoneType(String customerPhoneType) {
		this.customerPhoneType = customerPhoneType;
	}
	
}
