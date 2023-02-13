/**
 * 
 */
package com.sixdee.gamification.service.dto.req;

/**
 * @author rahul.kr
 *
 */
public class GetMyGamingHistoryReqDTO extends CommonReqDTO {

	private String gameId = null;
	private String gameName = null;
	private String fromDate = null;
	private String toDate = null;
	private int noOfMonths = 0;
	private int noOfTxns = 10;
	private int txnType = 0;
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
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public int getNoOfMonths() {
		return noOfMonths;
	}
	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}
	public int getNoOfTxns() {
		return noOfTxns;
	}
	public void setNoOfTxns(int noOfTxns) {
		this.noOfTxns = noOfTxns;
	}
	public int getTxnType() {
		return txnType;
	}
	public void setTxnType(int txnType) {
		this.txnType = txnType;
	}
	
	
}
