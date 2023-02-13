/**
 * 
 */
package com.sixdee.ussd.dto.parser.ussdRequest;


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
public class UssdRequestDTO {

	
	private String transactionId = null;
	private String sessionId = null;
	private String msisdn	 = null;
	private String starCode  = null;
	private String timeStamp = null;
	private String application = null;
	private String traversalPath = null;
	private String channel = null;
	private int langId = 0;
	private ServiceList serviceList = null;
	
	
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getStarCode() {
		return starCode;
	}
	public void setStarCode(String starCode) {
		this.starCode = starCode;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getTraversalPath() {
		return traversalPath;
	}
	public void setTraversalPath(String traversalPath) {
		this.traversalPath = traversalPath;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public ServiceList getServiceList() {
		return serviceList;
	}
	public void setServiceList(ServiceList serviceList) {
		this.serviceList = serviceList;
	}
	
	
	
}
