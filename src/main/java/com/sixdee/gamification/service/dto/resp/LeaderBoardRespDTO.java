/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Param;

/**
 * @author rahul.kr
 *
 */
public class LeaderBoardRespDTO extends CommonRespDTO{

	private LeaderBoardDetailsDTO[] leaderBoardDetailsDTOs = null;
	private Param parameters = null;
	
	
	public Param getParameters() {
		return parameters;
	}
	public void setParameters(Param parameters) {
		this.parameters = parameters;
	}
	public LeaderBoardDetailsDTO[] getLeaderBoardDetailsDTOs() {
		return leaderBoardDetailsDTOs;
	}
	public void setLeaderBoardDetailsDTOs(LeaderBoardDetailsDTO[] leaderBoardDetailsDTOs) {
		this.leaderBoardDetailsDTOs = leaderBoardDetailsDTOs;
	}
	
	
}
