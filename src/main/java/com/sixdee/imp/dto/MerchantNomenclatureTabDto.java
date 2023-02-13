package com.sixdee.imp.dto;

public class MerchantNomenclatureTabDto {

	private int merchantId;

	private int languageId;

	private String merchantName;

	private String description;

	private String imageName;

	private String merchantNameDncr;

	private String descriptionDncr;

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getMerchantNameDncr() {
		return merchantNameDncr;
	}

	public void setMerchantNameDncr(String merchantNameDncr) {
		this.merchantNameDncr = merchantNameDncr;
	}

	public String getDescriptionDncr() {
		return descriptionDncr;
	}

	public void setDescriptionDncr(String descriptionDncr) {
		this.descriptionDncr = descriptionDncr;
	}

}
