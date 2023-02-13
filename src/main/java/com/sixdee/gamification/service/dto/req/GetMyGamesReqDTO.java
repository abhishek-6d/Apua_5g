/**
 * 
 */
package com.sixdee.gamification.service.dto.req;

/**
 * @author rahul.kr
 *
 */
public class GetMyGamesReqDTO extends CommonReqDTO{

	private String msisdn = null;
	private String gameStatus = null;
	private String gameType = null;
	private int noOfGames = 0;
	private int offSet = 0;
	
	
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}
	public int getNoOfGames() {
		return noOfGames;
	}
	public void setNoOfGames(int noOfGames) {
		this.noOfGames = noOfGames;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}
	
	
}
