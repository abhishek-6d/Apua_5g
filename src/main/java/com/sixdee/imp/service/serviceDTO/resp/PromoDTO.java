package com.sixdee.imp.service.serviceDTO.resp;

public class PromoDTO 
{
	private int offerId;
	private String offerName;
	private String dialCode;
	private String smsKeyword;
	
	public String getDialCode() {
		return dialCode;
	}
	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}
	public int getOfferId() {
		return offerId;
	}
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getSmsKeyword() {
		return smsKeyword;
	}
	public void setSmsKeyword(String smsKeyword) {
		this.smsKeyword = smsKeyword;
	} 
}
