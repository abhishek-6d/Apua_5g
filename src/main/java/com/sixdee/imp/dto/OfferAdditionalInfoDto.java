package com.sixdee.imp.dto;

import java.io.Serializable;

public class OfferAdditionalInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int offerId;
	private String paramId = null;
	private String paramValue = null;
	private int categoryId;

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "OfferAdditionalInfoDto [offerId=" + offerId + ", paramId=" + paramId + ", paramValue=" + paramValue
				+ ", categoryId=" + categoryId + "]";
	}
}
