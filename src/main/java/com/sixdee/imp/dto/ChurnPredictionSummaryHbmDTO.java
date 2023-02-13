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


public class ChurnPredictionSummaryHbmDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private String subscriberNo;
	private String rangeID;
	private String churnStatus;
	private String churnChance;
	private String arpuBand;
	private String churnCategory;
	
	
	public String getSubscriberNo() {
		return subscriberNo;
	}
	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
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
	public String getRangeID() {
		return rangeID;
	}
	public void setRangeID(String rangeID) {
		this.rangeID = rangeID;
	}
	public String getChurnStatus() {
		return churnStatus;
	}
	public void setChurnStatus(String churnStatus) {
		this.churnStatus = churnStatus;
	}
	
	
	

}
