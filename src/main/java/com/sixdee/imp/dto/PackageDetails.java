package com.sixdee.imp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PackageDetails 
{
	private Integer serialNo;
	private Integer packageId;
	private String packageName;
	private String packageDesc;
	private Integer categoryId;
	private Integer redeemPoints;
	private String packageSynonm;
	private String dialCode;
	private PackageCategory packageCategory;
	private AreaLocationInfoDTO [] areaLocations ; 
	//private HashMap<String , ArrayList<String>> areaLocationMap;
	
	private String rateCardName;
	private Integer quantity;
	private String voucherType;
	private Date expiryDate;
	private Long orderRefNumber;
	private Date createDate;
	private int languageId;
	private String keyword ;
	private String chargingType;
	private String chargingIndicator;
	private String dedAcntId ;
	private String chargeAmt ;
	private String creditAmt ; 
	private String creditUnits ;
	private Integer merchantId ;	
	private String offerName;
	private Integer validityDays;
	private Integer units;
	private Integer rewardType;
	private Integer currencyValue;
	private Integer partnerPoints;
	
	
	
	
	public Integer getPartnerPoints() {
		return partnerPoints;
	}
	public void setPartnerPoints(Integer partnerPoints) {
		this.partnerPoints = partnerPoints;
	}
	public Integer getCurrencyValue() {
		return currencyValue;
	}
	public void setCurrencyValue(Integer currencyValue) {
		this.currencyValue = currencyValue;
	}
	/**
	 * @return the merchantId
	 */
	public Integer getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return the dialCode
	 */
	public String getDialCode() {
		return dialCode;
	}
	/**
	 * @param dialCode the dialCode to set
	 */
	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}
	public String getCreditUnits() {
		return creditUnits;
	}
	public void setCreditUnits(String creditUnits) {
		this.creditUnits = creditUnits;
	}
	public String getChargeAmt() {
		return chargeAmt;
	}
	public void setChargeAmt(String chargeAmt) {
		this.chargeAmt = chargeAmt;
	}
	public String getCreditAmt() {
		return creditAmt;
	}
	public void setCreditAmt(String creditAmt) {
		this.creditAmt = creditAmt;
	}
	public String getDedAcntId() {
		return dedAcntId;
	}
	public void setDedAcntId(String dedAcntId) {
		this.dedAcntId = dedAcntId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getChargingType() {
		return chargingType;
	}
	public void setChargingType(String chargingType) {
		this.chargingType = chargingType;
	}
	public String getChargingIndicator() {
		return chargingIndicator;
	}
	public void setChargingIndicator(String chargingIndicator) {
		this.chargingIndicator = chargingIndicator;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Long getOrderRefNumber() {
		return orderRefNumber;
	}
	public void setOrderRefNumber(Long orderRefNumber) {
		this.orderRefNumber = orderRefNumber;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getRateCardName() {
		return rateCardName;
	}
	public void setRateCardName(String rateCardName) {
		this.rateCardName = rateCardName;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public PackageCategory getPackageCategory() {
		return packageCategory;
	}
	public void setPackageCategory(PackageCategory packageCategory) {
		this.packageCategory = packageCategory;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getPackageDesc() {
		return packageDesc;
	}
	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Integer getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(Integer redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getPackageSynonm() {
		return packageSynonm;
	}
	public void setPackageSynonm(String packageSynonm) {
		this.packageSynonm = packageSynonm;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public Integer getValidityDays() {
		return validityDays;
	}
	public void setValidityDays(Integer validityDays) {
		this.validityDays = validityDays;
	}
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}

	
	/*public HashMap<String , ArrayList<String>> getAreaLocationMap() {
		return areaLocationMap;
	}
	public void setAreaLocationMap(HashMap<String , ArrayList<String>> areaLocationMap) {
		this.areaLocationMap = areaLocationMap;
	}*/
	
	
	public AreaLocationInfoDTO [] getAreaLocations() {
		return areaLocations;
	}
	public void setAreaLocations(AreaLocationInfoDTO [] areaLocations) {
		this.areaLocations = areaLocations;
	}
	public Integer getRewardType() {
		return rewardType;
	}
	public void setRewardType(Integer rewardType) {
		this.rewardType = rewardType;
	}
	
	
	
}
