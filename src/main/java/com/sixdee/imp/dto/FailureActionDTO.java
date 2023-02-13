package com.sixdee.imp.dto;

/**
 * 
 * @author Rahul K K
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
 * <td>May 27,2013 01:17:51 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class FailureActionDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subscriberNumber = null;
	private String ruleName = null;
	private String serviceName = null;
	
	
	
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	

}
