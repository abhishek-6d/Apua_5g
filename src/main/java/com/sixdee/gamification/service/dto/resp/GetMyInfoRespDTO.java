/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import java.util.ArrayList;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class GetMyInfoRespDTO extends CommonRespDTO{

	private String accountId = null;
	private String nationalId = null;
	private String nationalIdType = null;
	private String loyaltyId = null;
	private byte[] profileImage = null;

 	private String profileImagePath = null;
	private ArrayList<byte[]> achievementImages = null;
	private String achievementImagePath = null;
	private Games gameInformation = null;
	private Parameters param = null;
	
	
	public String getAchievementImagePath() {
		return achievementImagePath;
	}
	public void setAchievementImagePath(String achievementImagePath) {
		this.achievementImagePath = achievementImagePath;
	}
	public ArrayList<byte[]> getAchievementImages() {
		return achievementImages;
	}
	public void setAchievementImages(ArrayList<byte[]> achievementImages) {
		this.achievementImages = achievementImages;
	}
	public String getProfileImagePath() {
		return profileImagePath;
	}
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	public byte[] getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public Parameters getParam() {
		return param;
	}
	public void setParam(Parameters param) {
		this.param = param;
	}
	public Games getGameInformation() {
		return gameInformation;
	}
	public void setGameInformation(Games gameInformation) {
		this.gameInformation = gameInformation;
	}
	
	
	
	
}
