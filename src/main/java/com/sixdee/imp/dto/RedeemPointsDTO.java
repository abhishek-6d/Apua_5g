package com.sixdee.imp.dto;

/**
 * 
 * @author Somesh
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>May 15,2013 12:24:17 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.HashMap;


public class RedeemPointsDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String channel;
	private String transactionId;
	private String subscriberNumber;
	private String moNumber;
	private boolean redeem;
	private boolean adsl;
	private String packageId;
	private int defaultLanguage;
	private String uniqueId;
	private String returnCode;
	private String returnDesc;
	private String custType;
	private String custAccountNumber;
	private String voucherCode;
	private Long loyaltyId;
	
	
	private String area;
	private String location;
	private int redeemPoints;
	private String cardNumber;
	private OfferMasterTabDto offerMasterTab;
	private String offerName;
	private HashMap<String, String> offerAddtionalInfoMap=null;
	private CustomerProfileTabDTO customerProfileTabDTO=null;
	private String unitType;
	
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnDesc() {
		return returnDesc;
	}
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getCustAccountNumber() {
		return custAccountNumber;
	}
	public void setCustAccountNumber(String custAccountNumber) {
		this.custAccountNumber = custAccountNumber;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	//sajith ks sts618****end
	
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public int getDefaultLanguage() {
		return defaultLanguage;
	}
	public void setDefaultLanguage(int defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public boolean isAdsl() {
		return adsl;
	}
	public void setAdsl(boolean adsl) {
		this.adsl = adsl;
	}
	public boolean isRedeem() {
		return redeem;
	}
	public void setRedeem(boolean redeem) {
		this.redeem = redeem;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getMoNumber() {
		return moNumber;
	}
	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getVoucherCode() {
		return voucherCode;
	}
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	public Long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public int getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(int redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public OfferMasterTabDto getOfferMasterTab() {
		return offerMasterTab;
	}
	public void setOfferMasterTab(OfferMasterTabDto offerMasterTab) {
		this.offerMasterTab = offerMasterTab;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public HashMap<String, String> getOfferAddtionalInfoMap() {
		return offerAddtionalInfoMap;
	}
	public void setOfferAddtionalInfoMap(HashMap<String, String> offerAddtionalInfoMap) {
		this.offerAddtionalInfoMap = offerAddtionalInfoMap;
	}
	public CustomerProfileTabDTO getCustomerProfileTabDTO() {
		return customerProfileTabDTO;
	}
	public void setCustomerProfileTabDTO(CustomerProfileTabDTO customerProfileTabDTO) {
		this.customerProfileTabDTO = customerProfileTabDTO;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
}
