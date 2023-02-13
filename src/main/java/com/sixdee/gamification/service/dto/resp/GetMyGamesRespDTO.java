/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import java.util.ArrayList;

import javax.jws.WebService;

/**
 * @author rahul.kr
 *
 */
@WebService
public class GetMyGamesRespDTO extends CommonRespDTO{

	private String msisdn = null;
	private Games games = null;
	private int offSet = 0;
	private int limit = 0;
	private int totalGames = 0;
	
	
	public Games getGames() {
		return games;
	}
	public void setGames(Games games) {
		this.games = games;
	}
	public int getTotalGames() {
		return totalGames;
	}
	public void setTotalGames(int totalGames) {
		this.totalGames = totalGames;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
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
	
	
}
