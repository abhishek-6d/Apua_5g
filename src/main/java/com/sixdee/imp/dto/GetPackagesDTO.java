package com.sixdee.imp.dto;

import java.io.Serializable;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class GetPackagesDTO extends CommonVO implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private String offerType;
	private String offerId;
	private String isFirstLevel;
	private boolean getPackage;
	private int languageId;
	private Data[]	 	data;
	
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public String getIsFirstLevel() {
		return isFirstLevel;
	}
	public void setIsFirstLevel(String isFirstLevel) {
		this.isFirstLevel = isFirstLevel;
	}
	public boolean isGetPackage() {
		return getPackage;
	}
	public void setGetPackage(boolean getPackage) {
		this.getPackage = getPackage;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	
}
