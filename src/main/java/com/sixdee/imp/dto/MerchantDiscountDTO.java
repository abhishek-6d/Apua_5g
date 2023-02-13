package com.sixdee.imp.dto;

/**
 * 
 * @author Athul Gopal
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
 * <td>April 15,2015 06:58:41 PM</td>
 * <td>Athul Gopal</td>
 * </tr>
 * </table>
 * </p>
 */

import java.io.Serializable;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class MerchantDiscountDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String transactionId;
	private String timestamp;
	private String channel;
	private String subscriberNumber;
	private Data[] data;
	private Integer pin;
	private String languageID;
	private String merchantId;
	private String subscriberType;
	private Long loyaltyID;
	private String discount;
	private String tierId;
	private String tierName;
	
	private boolean isGetInfo;
	private boolean isRedeem;
	private boolean isSubscriber;
	private boolean isAccount;
	
	
	
	public String getTierName() {
		return tierName;
	}

	public void setTierName(String tierName) {
		this.tierName = tierName;
	}

	public String getTierId() {
		return tierId;
	}

	public void setTierId(String tierId) {
		this.tierId = tierId;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public boolean isSubscriber() {
		return isSubscriber;
	}

	public void setSubscriber(boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}

	public boolean isAccount() {
		return isAccount;
	}

	public void setAccount(boolean isAccount) {
		this.isAccount = isAccount;
	}

	public String getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public Long getLoyaltyID() {
		return loyaltyID;
	}

	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}

	public boolean isGetInfo() {
		return isGetInfo;
	}

	public void setGetInfo(boolean isGetInfo) {
		this.isGetInfo = isGetInfo;
	}

	public boolean isRedeem() {
		return isRedeem;
	}

	public void setRedeem(boolean isRedeem) {
		this.isRedeem = isRedeem;
	}

	public String getLanguageID() {
		return languageID;
	}

	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public Data[] getData() {
		return data;
	}

	public void setData(Data[] data) {
		this.data = data;
	}

	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

}
