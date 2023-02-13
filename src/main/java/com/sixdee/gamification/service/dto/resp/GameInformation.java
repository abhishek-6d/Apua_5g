/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class GameInformation {

	private String gameId = null;
	private String gameName = null;
	/*
	 * 1- Question Answer Type(Survey Type)
	 * 2- Question Answer Type(Elimination Type)
	 * 3- Activity Type 
	 */
	private String gameType = null;   
	private String gameDesc = null;
	private MileStones mileStones = null;
	private String gameStartDate = null;
	private String gameEndDate = null;
	private String valuesEarned = "0";
	private String mileStoneAchieved = null;
	private String valueToNextMileStone = "0";
	
	/*
	 * Value should be yyyy/MM/dd HH:mm:ss
	 */
	private String mileStoneExpiryPeriod = null;
	/*
	 * value should be DD:HH:MM:ss
	 */
	//private String timeRemainingForMileStone = null;
	private String gameStartTime = null;
	private String gameEndTime = null;
	private String nextMileStone = null;
	/*
	 * 1- Nojoom Points
	 * 2- Independent Points
	 * 3- virtual currency
	 */
	private String rewardType = null;
	
	private Parameters parameters = null;
	
	private QuizMaster quizMaster = null;
	
	
	public MileStones getMileStones() {
		return mileStones;
	}
	public void setMileStones(MileStones mileStones) {
		this.mileStones = mileStones;
	}
	public QuizMaster getQuizMaster() {
		return quizMaster;
	}
	public void setQuizMaster(QuizMaster quizMaster) {
		this.quizMaster = quizMaster;
	}
	public String getGameStartTime() {
		return gameStartTime;
	}
	public void setGameStartTime(String gameStartTime) {
		this.gameStartTime = gameStartTime;
	}
	public String getGameEndTime() {
		return gameEndTime;
	}
	public void setGameEndTime(String gameEndTime) {
		this.gameEndTime = gameEndTime;
	}

	public String getMileStoneExpiryPeriod() {
		return mileStoneExpiryPeriod;
	}
	public void setMileStoneExpiryPeriod(String mileStoneExpiryPeriod) {
		this.mileStoneExpiryPeriod = mileStoneExpiryPeriod;
	}
	public String getValuesEarned() {
		return valuesEarned;
	}
	public void setValuesEarned(String valuesEarned) {
		this.valuesEarned = valuesEarned;
	}
	public String getMileStoneAchieved() {
		return mileStoneAchieved;
	}
	public void setMileStoneAchieved(String mileStoneAchieved) {
		this.mileStoneAchieved = mileStoneAchieved;
	}
	public String getValueToNextMileStone() {
		return valueToNextMileStone;
	}
	public void setValueToNextMileStone(String valueToNextMileStone) {
		this.valueToNextMileStone = valueToNextMileStone;
	}
	public String getNextMileStone() {
		return nextMileStone;
	}
	public void setNextMileStone(String nextMileStone) {
		this.nextMileStone = nextMileStone;
	}
	public String getRewardType() {
		return rewardType;
	}
	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
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
	public String getGameDesc() {
		return gameDesc;
	}
	public void setGameDesc(String gameDesc) {
		this.gameDesc = gameDesc;
	}

	public String getGameStartDate() {
		return gameStartDate;
	}
	public void setGameStartDate(String gameStartDate) {
		this.gameStartDate = gameStartDate;
	}
	public String getGameEndDate() {
		return gameEndDate;
	}
	public void setGameEndDate(String gameEndDate) {
		this.gameEndDate = gameEndDate;
	}
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	
	
}
