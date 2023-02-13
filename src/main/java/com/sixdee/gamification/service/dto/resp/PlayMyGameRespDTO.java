/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.imp.dto.parser.in.Parameter;

/**
 * @author rahul.kr
 *
 */
public class PlayMyGameRespDTO extends CommonRespDTO{

	private String gameId = null;
	
	private int gameType = 0;
	private String gameActivationTime = null;
	private MileStones mileStoneToAchieve = null;
	private AchievementList achievmentsTill = null;
	private Parameter parameter = null;
	private Images images = null;
	private String gameTerminationTime = null;
	//DD:HH:MM:SS
	private String timeToLapse = null;
	private String valueToNextMileStone = "0";
	private boolean isEliminated = false;
	private QuizMaster quizMaster = null;
	
	
	public AchievementList getAchievmentsTill() {
		return achievmentsTill;
	}
	public void setAchievmentsTill(AchievementList achievmentsTill) {
		this.achievmentsTill = achievmentsTill;
	}
	public QuizMaster getQuizMaster() {
		return quizMaster;
	}
	public void setQuizMaster(QuizMaster quizMaster) {
		this.quizMaster = quizMaster;
	}
	public boolean isEliminated() {
		return isEliminated;
	}
	public void setEliminated(boolean isEliminated) {
		this.isEliminated = isEliminated;
	}
	public String getValueToNextMileStone() {
		return valueToNextMileStone;
	}
	public void setValueToNextMileStone(String valueToNextMileStone) {
		this.valueToNextMileStone = valueToNextMileStone;
	}
	public String getGameActivationTime() {
		return gameActivationTime;
	}
	public void setGameActivationTime(String gameActivationTime) {
		this.gameActivationTime = gameActivationTime;
	}
	public Images getImages() {
		return images;
	}
	public void setImages(Images images) {
		this.images = images;
	}
	public String getGameTerminationTime() {
		return gameTerminationTime;
	}
	public void setGameTerminationTime(String gameTerminationTime) {
		this.gameTerminationTime = gameTerminationTime;
	}
	public String getTimeToLapse() {
		return timeToLapse;
	}
	public void setTimeToLapse(String timeToLapse) {
		this.timeToLapse = timeToLapse;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public MileStones getMileStoneToAchieve() {
		return mileStoneToAchieve;
	}
	public void setMileStoneToAchieve(MileStones mileStoneToAchieve) {
		this.mileStoneToAchieve = mileStoneToAchieve;
	}

	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	
	
	
	
}
