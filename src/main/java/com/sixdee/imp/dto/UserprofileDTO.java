package com.sixdee.imp.dto;

/**
 * 
 * @author Geevan
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
 * <td>May 11,2013 08:47:22 AM</td>
 * <td>Geevan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.Date;


public class UserprofileDTO extends CommonVO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private Long loyaltyID;                          
	private int pin;
	//private Long accountNumber;
	private String firstName;                          
	private String lastName;
	private String contactNumber;
	private String dateOfBirth;
	private String  emailID;                           
	private String address    ;
	private String vipCode;    
	private String occupation;                         
	private String category;
	private String industry;
	private String defaultLanguage;
	private String nationalID;
	public Integer statusID;
	private String statusUpdatedDate;
	private Date createTime;
	private Integer tierId;
	private String tierUpdatedDate;
	private int statusPoints;
	private int rewardPoints;
	private int tierpoints;
	private int bonuspoints;
	private Long counter;
	private String tierName;
	private boolean isValidate;
	private boolean isNationalID;
	private String languageId;
	
	private String expiryDate;
	private String expiryRewardPoint;
	
	private boolean isIdentifier;
	private boolean isActive;
	private boolean isDealOfDay;
	private String userName;
	private String passWord;
	private String promoCode;

	private String nextTierName;
	private String pointsToNextTier;
	private String accountType;
	
	
	
	
	public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public String getNextTierName() {
		return nextTierName;
	}


	public void setNextTierName(String nextTierName) {
		this.nextTierName = nextTierName;
	}


	public String getPointsToNextTier() {
		return pointsToNextTier;
	}


	public void setPointsToNextTier(String pointsToNextTier) {
		this.pointsToNextTier = pointsToNextTier;
	}


	public String getPromoCode() {
		return promoCode;
	}


	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}


	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public boolean isDealOfDay() {
		return isDealOfDay;
	}


	public void setDealOfDay(boolean isDealOfDay) {
		this.isDealOfDay = isDealOfDay;
	}


	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}


	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}


	/**
	 * @return the expiryRewardPoint
	 */
	public String getExpiryRewardPoint() {
		return expiryRewardPoint;
	}


	/**
	 * @param expiryRewardPoint the expiryRewardPoint to set
	 */
	public void setExpiryRewardPoint(String expiryRewardPoint) {
		this.expiryRewardPoint = expiryRewardPoint;
	}


	/**
	 * @return the isNationalID
	 */
	public boolean isNationalID() {
		return isNationalID;
	}


	/**
	 * @param isNationalID the isNationalID to set
	 */
	public void setNationalID(boolean isNationalID) {
		this.isNationalID = isNationalID;
	}


	public String getLanguageId() {
		return languageId;
	}


	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}


	/**
	 * @return the isValidate
	 */
	public boolean isValidate() {
		return isValidate;
	}


	/**
	 * @param isValidate the isValidate to set
	 */
	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}


	public String getTierName() {
		return tierName;
	}


	public void setTierName(String tierName) {
		this.tierName = tierName;
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


	/*public Long getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}*/


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getEmailID() {
		return emailID;
	}


	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getVipCode() {
		return vipCode;
	}


	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}


	public String getOccupation() {
		return occupation;
	}


	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getIndustry() {
		return industry;
	}


	public void setIndustry(String industry) {
		this.industry = industry;
	}


	public String getDefaultLanguage() {
		return defaultLanguage;
	}


	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}


	public String getNationalID() {
		return nationalID;
	}


	public void setNationalID(String nationalID) {
		this.nationalID = nationalID;
	}


	public Integer getStatusID() {
		return statusID;
	}


	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}


	public String getStatusUpdatedDate() {
		return statusUpdatedDate;
	}


	public void setStatusUpdatedDate(String statusUpdatedDate) {
		this.statusUpdatedDate = statusUpdatedDate;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Integer getTierId() {
		return tierId;
	}


	public void setTierId(Integer tierId) {
		this.tierId = tierId;
	}


	public String getTierUpdatedDate() {
		return tierUpdatedDate;
	}


	public void setTierUpdatedDate(String tierUpdatedDate) {
		this.tierUpdatedDate = tierUpdatedDate;
	}


	public int getStatusPoints() {
		return statusPoints;
	}


	public void setStatusPoints(int statusPoints) {
		this.statusPoints = statusPoints;
	}


	public int getRewardPoints() {
		return rewardPoints;
	}


	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}


	public Long getCounter() {
		return counter;
	}


	public void setCounter(Long counter) {
		this.counter = counter;
	}


	public String getSubscriberNumber() {
		return subscriberNumber;
	}


	public void setSubscriberNumber(String string) {
		this.subscriberNumber = string;
	}


	public boolean isIdentifier() {
		return isIdentifier;
	}


	public void setIdentifier(boolean isIdentifier) {
		this.isIdentifier = isIdentifier;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassWord() {
		return passWord;
	}


	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}


	public int getTierpoints() {
		return tierpoints;
	}


	public void setTierpoints(int tierpoints) {
		this.tierpoints = tierpoints;
	}


	public int getBonuspoints() {
		return bonuspoints;
	}


	public void setBonuspoints(int bonuspoints) {
		this.bonuspoints = bonuspoints;
	}
	
	
}
