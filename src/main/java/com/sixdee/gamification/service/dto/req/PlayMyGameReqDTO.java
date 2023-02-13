/**
 * 
 */
package com.sixdee.gamification.service.dto.req;

import java.util.ArrayList;

import com.sixdee.gamification.service.dto.common.Parameters;

/**
 * @author rahul.kr
 *
 */
public class PlayMyGameReqDTO {

	private String gameId = null;
	private String mileStoneId = "-1";
	private Parameters parameters = null;
	private String[] answers = null;
	
	
	

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	public String getMileStoneId() {
		return mileStoneId;
	}

	public void setMileStoneId(String mileStoneId) {
		this.mileStoneId = mileStoneId;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	
}
