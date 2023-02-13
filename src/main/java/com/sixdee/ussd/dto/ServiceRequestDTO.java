/**
 * 
 */
package com.sixdee.ussd.dto;

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
public class ServiceRequestDTO {

	private int serviceId = 0;
	private String serviceClass = null;
	private String serviceName = null;
	private int serviceType = 0;
	private String url = null;
	private String failureMessage = null;
	private int callReqd  = 0;
	private int authReqd = 0;
	private int level = 0;
	private int keyWordId = 0;
	private String keyword = null;
	private String menuDesc = null ;
	private int langId = 1;
	private boolean isBackOption = false;
	private long timeout = 0;
	
	
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public boolean isBackOption() {
		return isBackOption;
	}
	public void setBackOption(boolean isBackOption) {
		this.isBackOption = isBackOption;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getMenuDesc() {
		return menuDesc;
	}
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getKeyWordId() {
		return keyWordId;
	}
	public void setKeyWordId(int keyWordId) {
		this.keyWordId = keyWordId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getAuthReqd() {
		return authReqd;
	}
	public void setAuthReqd(int authReqd) {
		this.authReqd = authReqd;
	}
	public String getFailureMessage() {
		return failureMessage;
	}
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
	public int getCallReqd() {
		return callReqd;
	}
	public void setCallReqd(int callReqd) {
		this.callReqd = callReqd;
	}
	public int getServiceType() {
		return serviceType;
	}
	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
	
}
