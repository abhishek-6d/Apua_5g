/**
 * 
 */
package com.sixdee.imp.dto.parser;

import com.sixdee.imp.service.httpcall.dto.SubscriberDataSet;

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
public class InstantRequestDTO {
	
	private String requestId = null;
	private String msisdn    = null;
	private String featureId = null;
	private String timeStamp = null;
	private SubscriberDataSet dataSet  = null;
	
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public SubscriberDataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(SubscriberDataSet dataSet2) {
		this.dataSet = dataSet;
	}
	
	
	
			
}
