package com.sixdee.imp.dto;

/**
 * 
 * @author @jith
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>September 20,2017 12:21:39 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class ChurnPredictionSummaryDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subscriberNo;
	private String timestamp;
	private String transcationId;
	private String channel;
	private String languageID;
	private String rangeId;
	private String churnChance;
	private String arpuBand;
	private String churnCategory;
	private String churnStatus;
	
	
	public String getChurnStatus() {
		return churnStatus;
	}
	public void setChurnStatus(String churnStatus) {
		this.churnStatus = churnStatus;
	}
	public String getSubscriberNo() {
		return subscriberNo;
	}
	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTranscationId() {
		return transcationId;
	}
	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public String getRangeId() {
		return rangeId;
	}
	public void setRangeId(String rangeId) {
		this.rangeId = rangeId;
	}
	public String getChurnChance() {
		return churnChance;
	}
	public void setChurnChance(String churnChance) {
		this.churnChance = churnChance;
	}
	public String getArpuBand() {
		return arpuBand;
	}
	public void setArpuBand(String arpuBand) {
		this.arpuBand = arpuBand;
	}
	public String getChurnCategory() {
		return churnCategory;
	}
	public void setChurnCategory(String churnCategory) {
		this.churnCategory = churnCategory;
	}
	
	
	

}
