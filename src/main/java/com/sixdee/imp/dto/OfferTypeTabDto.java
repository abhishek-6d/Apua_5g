package com.sixdee.imp.dto;

import java.util.Date;

public class OfferTypeTabDto {

	private int offerTypeId;

	private String offerType;

	private Date createDate;

	public OfferTypeTabDto() {}
	
	public int getOfferTypeId() {
		return offerTypeId;
	}

	public void setOfferTypeId(int offerTypeId) {
		this.offerTypeId = offerTypeId;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
