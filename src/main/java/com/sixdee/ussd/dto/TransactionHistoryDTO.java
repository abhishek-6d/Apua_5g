/**
 * 
 */
package com.sixdee.ussd.dto;

import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class TransactionHistoryDTO {

	private long transactionId = 0;
	private long sessionId = 0;
	private String msisdn = null;
	private String traversalPath = null;
	private String messageText = null;
	private String reqXml = null;
	private String respXml = null;
	private String successCode = null;
	private String successDesc = null;
	private int stage = 0;
	
	
	
	
	public String getSuccessDesc() {
		return successDesc;
	}
	public void setSuccessDesc(String successDesc) {
		this.successDesc = successDesc;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getTraversalPath() {
		return traversalPath;
	}
	public void setTraversalPath(String traversalPath) {
		this.traversalPath = traversalPath;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getReqXml() {
		return reqXml;
	}
	public void setReqXml(String reqXml) {
		this.reqXml = reqXml;
	}
	public String getRespXml() {
		return respXml;
	}
	public void setRespXml(String respXml) {
		this.respXml = respXml;
	}
	public String getSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	
	
	

	
	
}


