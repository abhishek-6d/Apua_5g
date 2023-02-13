/**
 * 
 */
package com.sixdee.imp.dto.parser.in;

import java.util.ArrayList;

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
public class LoyaltyDiscountDTO {

	
	private String 	requestId 			= null;
	private String feature			= null;
	private long 	msisdn 				= 0;
	private long 	accountNumber 		= 0;
	private String 	componentId 		= null;
	private long 	packageId 			= 0;
	private String 	instanceId      	= null;
	private String  systemId            = null;
	private String 	serverId			= null;
	private boolean isRetryReqd 		= false;
	private boolean isRetrydProcess     = false;
	private ParamList parameters        = null;
	
	
	
	
	public String getFeature() {
		return feature;
	}
	public void setFeature(String featureId) {
		this.feature = featureId;
	}
	public ParamList getParameters() {
		return parameters;
	}
	public void setParameters(ParamList parameters) {
		this.parameters = parameters;
	}
	public ParamList getParam() {
		return parameters;
	}
	
	public boolean isRetrydProcess() {
		return isRetrydProcess;
	}
	public void setRetrydProcess(boolean isRetrydProcess) {
		this.isRetrydProcess = isRetrydProcess;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public boolean isRetryReqd() {
		return isRetryReqd;
	}
	public void setRetryReqd(boolean isRetryReqd) {
		this.isRetryReqd = isRetryReqd;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public long getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(long msisdn) {
		this.msisdn = msisdn;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public long getPackageId() {
		return packageId;
	}
	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}
	
	
}
