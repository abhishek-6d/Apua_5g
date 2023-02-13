/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;


/**
 * @author NITHIN
 *
 */
public class ProfileSubscriberLanguageDTO {
	
	private Long id;
	private String msisdn;
	private String languageId;
	private String msisdnActiveDate;
	private String whiteList;
	private String ldreg;
	private String userId;
	private String tempCol;
	private String englishName;
	private String arabicName;
	private String category;
	private String nationality;
	private String nationalId;
	private Date dob;
	private String status;
	private String portIn;

	private String gender;
	//StartService
	
	
	
	public Long getId() {
		return id;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getMsisdnActiveDate() {
		return msisdnActiveDate;
	}
	public void setMsisdnActiveDate(String msisdnActiveDate) {
		this.msisdnActiveDate = msisdnActiveDate;
	}
	public String getWhiteList() {
		return whiteList;
	}
	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}
	public String getLdreg() {
		return ldreg;
	}
	public void setLdreg(String ldreg) {
		this.ldreg = ldreg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTempCol() {
		return tempCol;
	}
	public void setTempCol(String tempCol) {
		this.tempCol = tempCol;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getArabicName() {
		return arabicName;
	}
	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPortIn() {
		return portIn;
	}
	public void setPortIn(String portIn) {
		this.portIn = portIn;
	}
	
	
	
	//Referral Service
	
	
}
