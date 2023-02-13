/**
 * 
 */
package com.sixdee.imp.common.dto;

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
 * <td>Feb 10, 2014</td>
 * <td>Rahul K K</td>
 * <td>Will be having information regarding IN service</td>
 * </tr>
 * </table>
 * </p>
 */
public class INServiceDetailsDTO {

	private int serviceId = 0;
	private String requestorName = null;
	private String balnceCheckString = null;
	private String provisioningUrl = null;
	private String serviceName = null;
	private String arabicUssdString = null;
	
	
	
	
	
	public String getArabicUssdString() {
		return arabicUssdString;
	}
	public void setArabicUssdString(String arabicUssdString) {
		this.arabicUssdString = arabicUssdString;
	}
	public String getRequestorName() {
		return requestorName;
	}
	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}
	public String getProvisioningUrl() {
		return provisioningUrl;
	}
	public void setProvisioningUrl(String provisioningUrl) {
		this.provisioningUrl = provisioningUrl;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getBalnceCheckString() {
		return balnceCheckString;
	}
	public void setBalnceCheckString(String balnceCheckString) {
		this.balnceCheckString = balnceCheckString;
	}
	
	
	
}
