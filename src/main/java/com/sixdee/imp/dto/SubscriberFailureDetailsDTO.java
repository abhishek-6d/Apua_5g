/**
 * 
 */
package com.sixdee.imp.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NITHIN KUNJAPPAN
 *
 */
public class SubscriberFailureDetailsDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	

	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String transactionID;
	
	private String subscriberNumber;
	
	private String serviceName;
	
	private String ruleName;
	
	private String failureDesc;
	
	private Date failureDate;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getFailureDesc() {
		return failureDesc;
	}

	public void setFailureDesc(String failureDesc) {
		this.failureDesc = failureDesc;
	}

	public Date getFailureDate() {
		return failureDate;
	}

	public void setFailureDate(Date failureDate) {
		this.failureDate = failureDate;
	}
	

}
