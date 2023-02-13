package com.sixdee.imp.dto;

import java.util.Date;

public class OfferMasterTabDto {

	private int offerId;

	private int merchantId=0;

	private int offerTypeId;

	private String shortCode;

	private Date startDate;

	private Date endDate;

	private int priority;

	private int topOffer;

	private int expiryDays;

	private int expiryHour;

	private int expiryMinute;

	private int points;

	private int voucherType;

	private int subscriptionType;

	private Date createDate;

	private Date updateDate;
	
	private int interfaceId;
	
	private int teleCategory;
	
	private String cost;
	
	private int transferPoint=0;
	
	private int redeemLimit=0;
	

	public int getRedeemLimit() {
		return redeemLimit;
	}

	public void setRedeemLimit(int redeemLimit) {
		this.redeemLimit = redeemLimit;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getOfferTypeId() {
		return offerTypeId;
	}

	public void setOfferTypeId(int offerTypeId) {
		this.offerTypeId = offerTypeId;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getTopOffer() {
		return topOffer;
	}

	public void setTopOffer(int topOffer) {
		this.topOffer = topOffer;
	}

	public int getExpiryDays() {
		return expiryDays;
	}

	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}

	public int getExpiryHour() {
		return expiryHour;
	}

	public void setExpiryHour(int expiryHour) {
		this.expiryHour = expiryHour;
	}

	public int getExpiryMinute() {
		return expiryMinute;
	}

	public void setExpiryMinute(int expiryMinute) {
		this.expiryMinute = expiryMinute;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(int voucherType) {
		this.voucherType = voucherType;
	}

	public int getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(int subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	public int getTeleCategory() {
		return teleCategory;
	}

	public void setTeleCategory(int teleCategory) {
		this.teleCategory = teleCategory;
	}
	
	public int getTransferPoint() {
		return transferPoint;
	}

	public void setTransferPoint(int transferPoint) {
		this.transferPoint = transferPoint;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "OfferMasterTabDto [offerId=" + offerId + ", merchantId=" + merchantId + ", offerTypeId=" + offerTypeId
				+ ", shortCode=" + shortCode + ", startDate=" + startDate + ", endDate=" + endDate + ", priority="
				+ priority + ", topOffer=" + topOffer + ", expiryDays=" + expiryDays + ", expiryHour=" + expiryHour
				+ ", expiryMinute=" + expiryMinute + ", points=" + points + ", voucherType=" + voucherType
				+ ", subscriptionType=" + subscriptionType + ", createDate=" + createDate + ", updateDate=" + updateDate
				+ ", interfaceId=" + interfaceId + "]";
	}
	
	

}
