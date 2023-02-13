/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class GamingHistoryDTO {

	private String nationalId = null;
	private String nationalIdType = null;
	private String loyaltyId = null;
	private String moNumber = null;
	private String gameId = null;
	private String gameName = null;
	private String gameType = null;   
	private String activity = null;
	private String spendValue = "0";
	private String spendType = null;
	private String earnedValue = "0";
	private String earnedType = null;
	private String mileStoneAchieved = null;
	private String description = null;
	private Parameters parameters = null;
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
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public String getMoNumber() {
		return moNumber;
	}
	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getSpendValue() {
		return spendValue;
	}
	public void setSpendValue(String spendValue) {
		this.spendValue = spendValue;
	}
	public String getSpendType() {
		return spendType;
	}
	public void setSpendType(String spendType) {
		this.spendType = spendType;
	}
	public String getEarnedValue() {
		return earnedValue;
	}
	public void setEarnedValue(String earnedValue) {
		this.earnedValue = earnedValue;
	}
	public String getEarnedType() {
		return earnedType;
	}
	public void setEarnedType(String earnedType) {
		this.earnedType = earnedType;
	}
	public String getMileStoneAchieved() {
		return mileStoneAchieved;
	}
	public void setMileStoneAchieved(String mileStoneAchieved) {
		this.mileStoneAchieved = mileStoneAchieved;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	
	
	
}
