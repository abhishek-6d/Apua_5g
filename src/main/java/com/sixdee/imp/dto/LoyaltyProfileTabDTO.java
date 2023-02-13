package com.sixdee.imp.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoyaltyProfileTabDTO {
	
	
	private Long id;
	private Long loyaltyID;
	private String crn = null;
	private String pin;
	private String encryptPin;
	private String accountNumber;
	private String firstName;
	private String lastName;
	private String arbicFirstName;
	private String arbicLastName;

	private String gender;
	private String contactNumber;
	private Date dateOfBirth;

	private String emailID;
	private String address;
	private String arbicAddress;
	private String vipCode;
	private String occupation;
	private String category;
	private String industry;
	private String typeOfForm;
	private String zodiacSign;
	private String tarrot;
	private String distributor;
	private String privilegeMember;
	private String faxNumber;
	private String blockSubscriber;
	private String myOfferDetails;
	private String defaultLanguage;
	private List<NationalNumberTabDTO> custIdList;
	private String custID;
	public Integer statusID;
	private Date statusUpdatedDate;
	private Date createTime;
	private Integer tierId;
	private Date tierUpdatedDate;
	private double statusPoints=0;
	private double rewardPoints;
	private double tierPoints;
	private double bonusPoints=0;
	private int counter=0;
	private String notifySubscriberNumber;
	private Date pointsCreditedDate;
	private double dayWiseRewardPoints;
	private String nationality;
	private String nationality_Id;
	private String nationality_Id_Type;
	private Set<String> contactNumberSet;
	private String customerPhoneType;
	private int isHierarchyBillingActivated = 0;
	private int isBlackListed = 0;
	private int isBusiness = 0;
	private String ratePlan;
	private String expiry_date;
	private String accountType;
	private String telecomAdmin;
	private String isParent;
	private double reservePoints=0;
	private String fraud;
	private String isRoyalFamily;
	private Date creditPointDate;
	private String codesent;
	
	
	public String getNationality_Id() {
		return nationality_Id;
	}

	public String getNationality_Id_Type() {
		return nationality_Id_Type;
	}

	public void setNationality_Id(String nationality_Id) {
		this.nationality_Id = nationality_Id;
	}

	public void setNationality_Id_Type(String nationality_Id_Type) {
		this.nationality_Id_Type = nationality_Id_Type;
	}

	public int getIsBusiness() {
		return isBusiness;
	}

	public void setIsBusiness(int isBusiness) {
		this.isBusiness = isBusiness;
	}

	public int getIsBlackListed() {
		return isBlackListed;
	}

	public void setIsBlackListed(int isBlackListed) {
		this.isBlackListed = isBlackListed;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getIsHierarchyBillingActivated() {
		return isHierarchyBillingActivated;
	}

	public void setIsHierarchyBillingActivated(int isHierarchyBillingActivated) {
		this.isHierarchyBillingActivated = isHierarchyBillingActivated;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Double getDayWiseRewardPoints() {
		return dayWiseRewardPoints;
	}

	public void setDayWiseRewardPoints(Double dayWiseRewardPoints) {
		this.dayWiseRewardPoints = dayWiseRewardPoints;
	}

	public Date getPointsCreditedDate() {
		return pointsCreditedDate;
	}

	public void setPointsCreditedDate(Date pointsCreditedDate) {
		this.pointsCreditedDate = pointsCreditedDate;
	}

	public String getNotifySubscriberNumber() {
		return notifySubscriberNumber;
	}

	public void setNotifySubscriberNumber(String notifySubscriberNumber) {
		this.notifySubscriberNumber = notifySubscriberNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArbicAddress() {
		return arbicAddress;
	}

	public void setArbicAddress(String arbicAddress) {
		this.arbicAddress = arbicAddress;
	}

	public String getArbicFirstName() {
		return arbicFirstName;
	}

	public void setArbicFirstName(String arbicFirstName) {
		this.arbicFirstName = arbicFirstName;
	}

	public String getArbicLastName() {
		return arbicLastName;
	}

	public void setArbicLastName(String arbicLastName) {
		this.arbicLastName = arbicLastName;
	}

	public String getGender() {
		return defaultLanguage;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getEncryptPin() {
		return encryptPin;
	}

	public void setEncryptPin(String encryptPin) {
		this.encryptPin = encryptPin;
	}

	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * @param industry
	 *            the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * @return the typeOfForm
	 */
	public String getTypeOfForm() {
		return typeOfForm;
	}

	/**
	 * @param typeOfForm
	 *            the typeOfForm to set
	 */
	public void setTypeOfForm(String typeOfForm) {
		this.typeOfForm = typeOfForm;
	}

	/**
	 * @return the zodiacSign
	 */
	public String getZodiacSign() {
		return zodiacSign;
	}

	/**
	 * @param zodiacSign
	 *            the zodiacSign to set
	 */
	public void setZodiacSign(String zodiacSign) {
		this.zodiacSign = zodiacSign;
	}

	/**
	 * @return the tarrot
	 */
	public String getTarrot() {
		return tarrot;
	}

	/**
	 * @param tarrot
	 *            the tarrot to set
	 */
	public void setTarrot(String tarrot) {
		this.tarrot = tarrot;
	}

	/**
	 * @return the distributor
	 */
	public String getDistributor() {
		return distributor;
	}

	/**
	 * @param distributor
	 *            the distributor to set
	 */
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	/**
	 * @return the privilegeMember
	 */
	public String getPrivilegeMember() {
		return privilegeMember;
	}

	/**
	 * @param privilegeMember
	 *            the privilegeMember to set
	 */
	public void setPrivilegeMember(String privilegeMember) {
		this.privilegeMember = privilegeMember;
	}

	/**
	 * @return the faxNumber
	 */
	public String getFaxNumber() {
		return faxNumber;
	}

	/**
	 * @param faxNumber
	 *            the faxNumber to set
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	/**
	 * @return the blockSubscriber
	 */
	public String getBlockSubscriber() {
		return blockSubscriber;
	}

	/**
	 * @param blockSubscriber
	 *            the blockSubscriber to set
	 */
	public void setBlockSubscriber(String blockSubscriber) {
		this.blockSubscriber = blockSubscriber;
	}

	/**
	 * @return the myOfferDetails
	 */
	public String getMyOfferDetails() {
		return myOfferDetails;
	}

	/**
	 * @param myOfferDetails
	 *            the myOfferDetails to set
	 */
	public void setMyOfferDetails(String myOfferDetails) {
		this.myOfferDetails = myOfferDetails;
	}

	/**
	 * @return the tierUpdatedDate
	 */
	public Date getTierUpdatedDate() {
		return tierUpdatedDate;
	}

	/**
	 * @param tierUpdatedDate
	 *            the tierUpdatedDate to set
	 */
	public void setTierUpdatedDate(Date tierUpdatedDate) {
		this.tierUpdatedDate = tierUpdatedDate;
	}

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @param counter
	 *            the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * @return the rewardPoints
	 */

	/**
	 * @return the tierId
	 */
	public Integer getTierId() {
		return tierId;
	}

	/**
	 * @param tierId
	 *            the tierId to set
	 */
	public void setTierId(Integer tierId) {
		this.tierId = tierId;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the statusUpdatedDate
	 */
	public Date getStatusUpdatedDate() {
		return statusUpdatedDate;
	}

	/**
	 * @param statusUpdatedDate
	 *            the statusUpdatedDate to set
	 */
	public void setStatusUpdatedDate(Date statusUpdatedDate) {
		this.statusUpdatedDate = statusUpdatedDate;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the statusID
	 */
	public Integer getStatusID() {
		return statusID;
	}

	/**
	 * @param statusID
	 *            the statusID to set
	 */
	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}

	/**
	 * @return the loyaltyID
	 */
	public Long getLoyaltyID() {
		return loyaltyID;
	}

	/**
	 * @param loyaltyID
	 *            the loyaltyID to set
	 */
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin
	 *            the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
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
	 * @param category
	 *            the category to set
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
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the defaultLanguage
	 */
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @param defaultLanguage
	 *            the defaultLanguage to set
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
	 * @param emailID
	 *            the emailID to set
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
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<NationalNumberTabDTO> getCustIdList() {
		return custIdList;
	}

	public void setCustIdList(List<NationalNumberTabDTO> custIdList) {
		this.custIdList = custIdList;
	}

	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @param occupation
	 *            the occupation to set
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
	 * @param vipCode
	 *            the vipCode to set
	 */
	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}

	/**
	 * @return the rewardPoints
	 */
	public Double getRewardPoints() {
		return rewardPoints;
	}

	/**
	 * @param rewardPoints
	 *            the rewardPoints to set
	 */
	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	/**
	 * @return the statusPoints
	 */


	/**
	 * @param statusPoints
	 *            the statusPoints to set
	 */
	

	public Set<String> getContactNumberSet() {
		if (contactNumberSet == null) {
			contactNumberSet = new HashSet<String>();
		}
		return contactNumberSet;
	}

	public double getStatusPoints() {
		return statusPoints;
	}

	public void setStatusPoints(double statusPoints) {
		this.statusPoints = statusPoints;
	}

	public void setContactNumberSet(Set<String> contactNumberSet) {
		this.contactNumberSet = contactNumberSet;
	}
	

	public String getCustomerPhoneType() {
		return customerPhoneType;
	}

	public void setCustomerPhoneType(String customerPhoneType) {
		this.customerPhoneType = customerPhoneType;
	}

	@Override
	public String toString() {
		return "LoyaltyProfileTabDTO [contactNumberSet=" + contactNumberSet + "]";
	}

	public Double getTierPoints() {
		return tierPoints;
	}

	public void setTierPoints(Double tierPoints) {
		this.tierPoints = tierPoints;
	}

	public Double getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(Double bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public String getRatePlan() {
		return ratePlan;
	}

	public void setRatePlan(String ratePlan) {
		this.ratePlan = ratePlan;
	}

	public void setRewardPoints(double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public void setTierPoints(double tierPoints) {
		this.tierPoints = tierPoints;
	}

	public void setBonusPoints(double bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public void setDayWiseRewardPoints(double dayWiseRewardPoints) {
		this.dayWiseRewardPoints = dayWiseRewardPoints;
	}

	public String getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getTelecomAdmin() {
		return telecomAdmin;
	}

	public void setTelecomAdmin(String telecomAdmin) {
		this.telecomAdmin = telecomAdmin;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public double getReservePoints() {
		return reservePoints;
	}

	public void setReservePoints(double reservePoints) {
		this.reservePoints = reservePoints;
	}

	public String getFraud() {
		return fraud;
	}

	public void setFraud(String fraud) {
		this.fraud = fraud;
	}

	public String getIsRoyalFamily() {
		return isRoyalFamily;
	}

	public void setIsRoyalFamily(String isRoyalFamily) {
		this.isRoyalFamily = isRoyalFamily;
	}

	public Date getCreditPointDate() {
		return creditPointDate;
	}

	public void setCreditPointDate(Date creditPointDate) {
		this.creditPointDate = creditPointDate;
	}

	public String getCodesent() {
		return codesent;
	}

	public void setCodesent(String codesent) {
		this.codesent = codesent;
	}

}
