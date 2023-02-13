package com.sixdee.imp.dto;

/**
 * 
 * @author Paramesh
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
 * <td>April 26,2013 04:24:47 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class RewardPointsCalculationDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private Integer subscriberType;
	private Long loyaltyID;
	private int rewardPointsCategory;
	private Double volume;
	private Double originalVolume;
	private Double requestRewardPoints;
	private Double requestStatusPoints;
	private boolean pointsCalculation;
	private boolean isSubscriber;
	private boolean isADSL;
	private boolean isAccount;
	private boolean isStatusPoints;
	
	private Integer profileTierID;
	private Integer nextTierID;
	private Double profileRewardPoints;
	private Double profileStatusoints;
	private double calculatedRewardPoints;
	private double calculatedStatusPoints;
	private String defaultLanguage;
	private Double dayWiseRewardPoints;
	
	
	private Double balanceAftrPayment;
	private String paymentChannel;
	
	
	
	private String collectionCenterID;
	private String serviceType;
	private String actualPaymentDate;
	private String regionID;
	private String paymentAgencyID;
	private String invoiceNumber;
	private String paymentMethodID;
	private String transactionType;
	private String sysCreationDate;
	private String sysUpdateDate;
	private String desc;
	
	
	public String getCollectionCenterID() {
		return collectionCenterID;
	}
	public void setCollectionCenterID(String collectionCenterID) {
		this.collectionCenterID = collectionCenterID;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getActualPaymentDate() {
		return actualPaymentDate;
	}
	public void setActualPaymentDate(String actualPaymentDate) {
		this.actualPaymentDate = actualPaymentDate;
	}
	public String getRegionID() {
		return regionID;
	}
	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}
	public String getPaymentAgencyID() {
		return paymentAgencyID;
	}
	public void setPaymentAgencyID(String paymentAgencyID) {
		this.paymentAgencyID = paymentAgencyID;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getPaymentMethodID() {
		return paymentMethodID;
	}
	public void setPaymentMethodID(String paymentMethodID) {
		this.paymentMethodID = paymentMethodID;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getSysCreationDate() {
		return sysCreationDate;
	}
	public void setSysCreationDate(String sysCreationDate) {
		this.sysCreationDate = sysCreationDate;
	}
	public String getSysUpdateDate() {
		return sysUpdateDate;
	}
	public void setSysUpdateDate(String sysUpdateDate) {
		this.sysUpdateDate = sysUpdateDate;
	}
	private int testNumber;
	
	//Ebill Feature
	private boolean isEbill;
	
	/**
	 * @return the isEbill
	 */
	public boolean isEbill() {
		return isEbill;
	}
	/**
	 * @param isEbill the isEbill to set
	 */
	public void setEbill(boolean isEbill) {
		this.isEbill = isEbill;
	}
	public int getTestNumber() {
		return testNumber;
	}
	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}
	public Double getDayWiseRewardPoints() {
		return dayWiseRewardPoints;
	}
	public void setDayWiseRewardPoints(Double dayWiseRewardPoints) {
		this.dayWiseRewardPoints = dayWiseRewardPoints;
	}
	public Integer getSubscriberType() {
		return subscriberType;
	}
	public void setSubscriberType(Integer subscriberType) {
		this.subscriberType = subscriberType;
	}
	public Double getOriginalVolume() {
		return originalVolume;
	}
	public void setOriginalVolume(Double originalVolume) {
		this.originalVolume = originalVolume;
	}
	public Integer getNextTierID() {
		return nextTierID;
	}
	public void setNextTierID(Integer nextTierID) {
		this.nextTierID = nextTierID;
	}
	public String getDefaultLanguage() {
		return defaultLanguage;
	}
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	public double getCalculatedRewardPoints() {
		return calculatedRewardPoints;
	}
	public void setCalculatedRewardPoints(double calculatedRewardPoints) {
		this.calculatedRewardPoints = calculatedRewardPoints;
	}
	public double getCalculatedStatusPoints() {
		return calculatedStatusPoints;
	}
	public void setCalculatedStatusPoints(double calculatedStatusPoints) {
		this.calculatedStatusPoints = calculatedStatusPoints;
	}
	public boolean isAccount() {
		return isAccount;
	}
	public void setAccount(boolean isAccount) {
		this.isAccount = isAccount;
	}
	public boolean isADSL() {
		return isADSL;
	}
	public void setADSL(boolean isADSL) {
		this.isADSL = isADSL;
	}
	public boolean isStatusPoints() {
		return isStatusPoints;
	}
	public void setStatusPoints(boolean isStatusPoints) {
		this.isStatusPoints = isStatusPoints;
	}
	public boolean isSubscriber() {
		return isSubscriber;
	}
	public void setSubscriber(boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public boolean isPointsCalculation() {
		return pointsCalculation;
	}
	public void setPointsCalculation(boolean pointsCalculation) {
		this.pointsCalculation = pointsCalculation;
	}
	public Double getProfileRewardPoints() {
		return profileRewardPoints;
	}
	public void setProfileRewardPoints(Double profileRewardPoints) {
		this.profileRewardPoints = profileRewardPoints;
	}
	public Double getProfileStatusoints() {
		return profileStatusoints;
	}
	public void setProfileStatusoints(Double profileStatusoints) {
		this.profileStatusoints = profileStatusoints;
	}
	public Integer getProfileTierID() {
		return profileTierID;
	}
	public void setProfileTierID(Integer profileTierID) {
		this.profileTierID = profileTierID;
	}
	public Double getRequestRewardPoints() {
		return requestRewardPoints;
	}
	public void setRequestRewardPoints(Double requestRewardPoints) {
		this.requestRewardPoints = requestRewardPoints;
	}
	public Double getRequestStatusPoints() {
		return requestStatusPoints;
	}
	public void setRequestStatusPoints(Double requestStatusPoints) {
		this.requestStatusPoints = requestStatusPoints;
	}
	public int getRewardPointsCategory() {
		return rewardPointsCategory;
	}
	public void setRewardPointsCategory(int rewardPointsCategory) {
		this.rewardPointsCategory = rewardPointsCategory;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getBalanceAftrPayment() {
		return balanceAftrPayment;
	}
	public void setBalanceAftrPayment(Double balanceAftrPayment) {
		this.balanceAftrPayment = balanceAftrPayment;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
	
	
	 
	
	

}
