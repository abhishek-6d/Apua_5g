package com.sixdee.imp.service.serviceDTO.resp;


public class MerchantRedeemResDTO extends ResponseDTO {
	
	private String discoutValue;
	private String subscriberNumber;
	private String tierName;
	private String merchantId;
	private String subscriberType;
	
	
	public String getTierName() {
		return tierName;
	}

	public void setTierName(String tierName) {
		this.tierName = tierName;
	}

	public String getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getDiscoutValue() {
		return discoutValue;
	}

	public void setDiscoutValue(String discoutValue) {
		this.discoutValue = discoutValue;
	}
	
}//
