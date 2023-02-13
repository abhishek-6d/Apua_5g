/**
 * 
 */
package com.sixdee.gamification.service.dto.req;

import java.util.ArrayList;

import com.sixdee.gamification.service.dto.common.Parameters;
import com.sixdee.gamification.service.dto.resp.QuestionAnswerDTO;

/**
 * @author rahul.kr
 *
 */
public class LeaderBoardReqDTO extends CommonReqDTO{

	private String gameId = null;
	private int offSet = 0;
	private int limit = 0;
	private String affinity = null;
	
	
	
	



	

	public int getOffSet() {
		return offSet;
	}

	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getAffinity() {
		return affinity;
	}

	public void setAffinity(String affinity) {
		this.affinity = affinity;
	}

	

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	
}
