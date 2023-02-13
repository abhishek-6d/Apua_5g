/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;

/**
 * @author NITHIN
 *
 */
public class PostPaidInstantDTO {
	
	private Long id;
	private String msisdn;
	private Date actualPaymentDate;
	private Date createDate;
	private String volume;
	private String collectionCenterID;
	private String serviceType;
	private String actualPaymentTimeStamp;
	
	
	public String getActualPaymentTimeStamp() {
		return actualPaymentTimeStamp;
	}
	public void setActualPaymentTimeStamp(String actualPaymentTimeStamp) {
		this.actualPaymentTimeStamp = actualPaymentTimeStamp;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public Date getActualPaymentDate() {
		return actualPaymentDate;
	}
	public void setActualPaymentDate(Date actualPaymentDate) {
		this.actualPaymentDate = actualPaymentDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getCollectionCenterID() {
		return collectionCenterID;
	}
	public void setCollectionCenterID(String collectionCenterID) {
		this.collectionCenterID = collectionCenterID;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	
}
