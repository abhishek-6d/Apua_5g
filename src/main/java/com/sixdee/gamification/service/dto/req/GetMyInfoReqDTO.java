/**
 * 
 */
package com.sixdee.gamification.service.dto.req;

/**
 * @author rahul.kr
 *
 */
public class GetMyInfoReqDTO extends CommonReqDTO {

	private String msisdn = null;
	private String gameId = null;
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	
}
