package com.sixdee.imp.dto;

import java.util.List;

public class UserResponseDTO {
	private String code;
	private String reason;
	private String id;
	private String loyaltyTier;
	List<LoyaltyBalance>loyaltyBalances;
	 List<ProfileInfo> profileInfo;
	 
	 
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoyaltyTier() {
		return loyaltyTier;
	}
	public void setLoyaltyTier(String loyaltyTier) {
		this.loyaltyTier = loyaltyTier;
	}
	public List<LoyaltyBalance> getLoyaltyBalances() {
		return loyaltyBalances;
	}
	public void setLoyaltyBalances(List<LoyaltyBalance> loyaltyBalances) {
		this.loyaltyBalances = loyaltyBalances;
	}
	public List<ProfileInfo> getProfileInfo() {
		return profileInfo;
	}
	public void setProfileInfo(List<ProfileInfo> profileInfo) {
		this.profileInfo = profileInfo;
	}  
	 
	 
	 
}
