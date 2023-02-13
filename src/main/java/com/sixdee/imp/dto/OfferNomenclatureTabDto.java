package com.sixdee.imp.dto;

public class OfferNomenclatureTabDto {

	private int offerId;

	private int languageId;

	private String offerName;

	private String description;

	private String imageName;

	private String offerNameDncr;

	private String descriptionDncr;

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
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

	public String getOfferNameDncr() {
		return offerNameDncr;
	}

	public void setOfferNameDncr(String offerNameDncr) {
		this.offerNameDncr = offerNameDncr;
	}

	public String getDescriptionDncr() {
		return descriptionDncr;
	}

	public void setDescriptionDncr(String descriptionDncr) {
		this.descriptionDncr = descriptionDncr;
	}

	@Override
	public String toString() {
		return "OfferNomenclatureTabDto [offerId=" + offerId + ", languageId=" + languageId + ", offerName=" + offerName
				+ ", description=" + description + ", imageName=" + imageName + ", offerNameDncr=" + offerNameDncr
				+ ", descriptionDncr=" + descriptionDncr + "]";
	}

}
