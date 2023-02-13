package com.sixdee.imp.dto;

import java.util.List;
import java.util.Map;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class CreateAccountDTO extends CommonVO {

	private Long moNumber;
	private List<String> registerNumbers;
	private List<String> OriginalRegisterNumbers;
	private boolean isAllAccounts;
	private boolean isCreate;
	private Long loyaltyID;
	private Map<String,SubscriberDetailsDTO> subscriberAccountMaps;
	private List<Data> dataSet;
	private Integer statusID;
	private Integer tierId;
	private String notifyMobileNumber;
	private String languageID;
	private Double rewardPointsVolume;
	private Integer rewardPointsCategoryID;
	private Double rewardStatusPoints;
	private Integer preTierID;
	private Integer curTierID;
	private int retryCounter=1;
	private boolean isRetry;
	private String cdrType;
	private boolean isFailureNotify;
	private boolean isPrimaryNumber;
	
	private String WelcomeTierPoints;
	private String WelcomeBonusPoints;
	
	
	

	 

	public boolean isPrimaryNumber() {
		return isPrimaryNumber;
	}

	public void setPrimaryNumber(boolean isPrimaryNumber) {
		this.isPrimaryNumber = isPrimaryNumber;
	}

	public Integer getCurTierID() {
		return curTierID;
	}

	public void setCurTierID(Integer curTierID) {
		this.curTierID = curTierID;
	}

	public Integer getPreTierID() {
		return preTierID;
	}

	public void setPreTierID(Integer preTierID) {
		this.preTierID = preTierID;
	}

	public Double getRewardStatusPoints() {
		return rewardStatusPoints;
	}

	public void setRewardStatusPoints(Double rewardStatusPoints) {
		this.rewardStatusPoints = rewardStatusPoints;
	}

	public boolean isFailureNotify() {
		return isFailureNotify;
	}

	public void setFailureNotify(boolean isFailureNotify) {
		this.isFailureNotify = isFailureNotify;
	}

	
	 

	public String getCdrType() {
		return cdrType;
	}

	public void setCdrType(String cdrType) {
		this.cdrType = cdrType;
	}

	public boolean isRetry() {
		return isRetry;
	}

	public void setRetry(boolean isRetry) {
		this.isRetry = isRetry;
	}

	public List<String> getOriginalRegisterNumbers() {
		return OriginalRegisterNumbers;
	}

	public void setOriginalRegisterNumbers(List<String> originalRegisterNumbers) {
		OriginalRegisterNumbers = originalRegisterNumbers;
	}

	public int getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(int retryCounter) {
		this.retryCounter = retryCounter;
	}

	public Integer getRewardPointsCategoryID() {
		return rewardPointsCategoryID;
	}

	public void setRewardPointsCategoryID(Integer rewardPointsCategoryID) {
		this.rewardPointsCategoryID = rewardPointsCategoryID;
	}

	public Double getRewardPointsVolume() {
		return rewardPointsVolume;
	}

	public void setRewardPointsVolume(Double rewardPointsVolume) {
		this.rewardPointsVolume = rewardPointsVolume;
	}

	public String getLanguageID() {
		return languageID;
	}

	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}

	public Integer getTierId() {
		return tierId;
	}

	public void setTierId(Integer tierId) {
		this.tierId = tierId;
	}

	public String getNotifyMobileNumber() {
		return notifyMobileNumber;
	}

	public void setNotifyMobileNumber(String notifyMobileNumber) {
		this.notifyMobileNumber = notifyMobileNumber;
	}

	public Integer getStatusID() {
		return statusID;
	}

	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}

	
	public List<Data> getDataSet() {
		return dataSet;
	}

	public void setDataSet(List<Data> dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @return the subscriberAccountMaps
	 */
	public Map<String,SubscriberDetailsDTO> getSubscriberAccountMaps() {
		return subscriberAccountMaps;
	}

	/**
	 * @param subscriberAccountMaps the subscriberAccountMaps to set
	 */
	public void setSubscriberAccountMaps(Map<String,SubscriberDetailsDTO> subscriberAccountMaps) {
		this.subscriberAccountMaps = subscriberAccountMaps;
	}

	/**
	 * @return the loyaltyID
	 */
	public Long getLoyaltyID() {
		return loyaltyID;
	}

	/**
	 * @param loyaltyID the loyaltyID to set
	 */
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}

	/**
	 * @return the isAllAccounts
	 */
	public boolean isAllAccounts() {
		return isAllAccounts;
	}

	/**
	 * @param isAllAccounts the isAllAccounts to set
	 */
	public void setAllAccounts(boolean isAllAccounts) {
		this.isAllAccounts = isAllAccounts;
	}

	/**
	 * @return the moNumber
	 */
	public Long getMoNumber() {
		return moNumber;
	}

	/**
	 * @param moNumber the moNumber to set
	 */
	public void setMoNumber(Long moNumber) {
		this.moNumber = moNumber;
	}

	/**
	 * @return the registerNumbers
	 */
	public List<String> getRegisterNumbers() {
		return registerNumbers;
	}

	/**
	 * @param registerNumbers the registerNumbers to set
	 */
	public void setRegisterNumbers(List<String> registerNumbers) {
		this.registerNumbers = registerNumbers;
	}

	/**
	 * @return the isCreate
	 */
	public boolean isCreate() {
		return isCreate;
	}

	/**
	 * @param isCreate the isCreate to set
	 */
	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	public String getWelcomeTierPoints() {
		return WelcomeTierPoints;
	}

	public void setWelcomeTierPoints(String welcomeTierPoints) {
		WelcomeTierPoints = welcomeTierPoints;
	}

	public String getWelcomeBonusPoints() {
		return WelcomeBonusPoints;
	}

	public void setWelcomeBonusPoints(String welcomeBonusPoints) {
		WelcomeBonusPoints = welcomeBonusPoints;
	}


	
	
	
}
