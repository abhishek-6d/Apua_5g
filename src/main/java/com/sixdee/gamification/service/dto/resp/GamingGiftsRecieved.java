/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

/**
 * @author rahul.kr
 *
 */
public class GamingGiftsRecieved {

	private String nationalId = null;
	private String nationalIdType = null;
	private String loyaltyId = null;
	private String winnerMDN = null;
	private String gameId = null;
	private String gameName = null;
	private String gameType = null;   
	private MileStoneAchievementDTO mileStoneAchievementDTO = null;
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
	public String getWinnerMDN() {
		return winnerMDN;
	}
	public void setWinnerMDN(String winnerMDN) {
		this.winnerMDN = winnerMDN;
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
	public MileStoneAchievementDTO getMileStoneAchievementDTO() {
		return mileStoneAchievementDTO;
	}
	public void setMileStoneAchievementDTO(MileStoneAchievementDTO mileStoneAchievementDTO) {
		this.mileStoneAchievementDTO = mileStoneAchievementDTO;
	}
	
	
	
}
