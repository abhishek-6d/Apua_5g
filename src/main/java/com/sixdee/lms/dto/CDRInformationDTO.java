/**
 * 
 */
package com.sixdee.lms.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author rahul.kr
 *
 */
public class CDRInformationDTO {
	
	private String transactionId = "";
	private Date timeStamp ;
	private int commandId =0;
	private String channelID = "";
	private String loyaltyId = "";
	private String subscriberNumber = "";
	private String accountNumber = "";
	private String subscriberType = "";
	private String statusCode = "";
	private String statusDescription = "";
	private double tierPoints = 0.0;
	private double bonusPoints = 0.0;
	private int previousTier = 0;
	private int currentTier = 0;
	private int categoryId = 0;
	private String categoryDesc = "";
	private String offerId = "";
	private String merchantId = "";
	private String voucherNumber = "";
	private String destSubscriberNumber = "";
	private String destLoyaltyId = "";
	/**
	 * Field 1 - Volume for point accumulation
	 */
	private String field1 = "";
	private String currentBalance = "";
	private String oldBalance = "";
	private String field2 = "";
	private String field3 = "";
	private String field4 = "";
	private String field5 = "";
	private String field6 = "";
	private String field7 = "";
	private String field8 = "";
	private String field9 = "";
	private String field10 = "";
	
	
	
	@Override
	public String toString() {
		StringBuilder sbuilder = new StringBuilder("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		sbuilder.append(transactionId).append(",")
			.append(sdf.format(timeStamp!=null?timeStamp:Calendar.getInstance().getTime())).append(",")
			.append(commandId>0?commandId:"-1").append(",")
			.append(channelID).append(",")
			.append(loyaltyId).append(",")
			.append(subscriberNumber).append(",")
			.append(accountNumber).append(",")
			.append(subscriberType).append(",")
			.append(statusCode).append(",")
			.append(statusDescription).append(",")
			.append(tierPoints).append(",")
			.append(bonusPoints).append(",")
			.append(previousTier).append(",")
			.append(currentTier).append(",")
			.append(categoryId).append(",")
			.append(categoryDesc).append(",")
			.append(offerId).append(",")
			.append(merchantId).append(",")
			.append(voucherNumber).append(",")
			.append(destSubscriberNumber).append(",")
			.append(destLoyaltyId).append(",")
			.append(field1).append(",")
			.append(currentBalance).append(",")
			.append(oldBalance).append(",")
			.append(field4).append(",")
			.append(field5).append(",")
			.append(field6).append(",")
			.append(field7).append(",")
			.append(field8).append(",")
			.append(field9).append(",")
			.append(field10);
		return sbuilder.toString();
	}


	

	public String getChannelID() {
		return channelID;
	}




	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}




	public String getTransactionId() {
		return transactionId;
	}



	public Date getTimeStamp() {
		return timeStamp;
	}



	public int getCommandId() {
		return commandId;
	}



	public String getLoyaltyId() {
		return loyaltyId;
	}



	public String getSubscriberNumber() {
		return subscriberNumber;
	}



	public String getAccountNumber() {
		return accountNumber;
	}



	public String getSubscriberType() {
		return subscriberType;
	}



	public String getStatusCode() {
		return statusCode;
	}



	public String getStatusDescription() {
		return statusDescription;
	}



	public double getTierPoints() {
		return tierPoints;
	}



	public double getBonusPoints() {
		return bonusPoints;
	}



	public int getPreviousTier() {
		return previousTier;
	}



	public int getCurrentTier() {
		return currentTier;
	}



	public int getCategoryId() {
		return categoryId;
	}



	public String getCategoryDesc() {
		return categoryDesc;
	}



	public String getOfferId() {
		return offerId;
	}



	public String getMerchantId() {
		return merchantId;
	}



	public String getVoucherNumber() {
		return voucherNumber;
	}



	public String getDestSubscriberNumber() {
		return destSubscriberNumber;
	}



	public String getDestLoyaltyId() {
		return destLoyaltyId;
	}



	public String getField1() {
		return field1;
	}



	public String getCurrentBalance() {
		return currentBalance;
	}



	public String getOldBalance() {
		return oldBalance;
	}



	public String getField2() {
		return field2;
	}



	public String getField3() {
		return field3;
	}



	public String getField4() {
		return field4;
	}



	public String getField5() {
		return field5;
	}



	public String getField6() {
		return field6;
	}



	public String getField7() {
		return field7;
	}



	public String getField8() {
		return field8;
	}



	public String getField9() {
		return field9;
	}



	public String getField10() {
		return field10;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}



	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}



	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}



	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}



	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}



	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}



	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}



	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}



	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}



	public void setTierPoints(double tierPoints) {
		this.tierPoints = tierPoints;
	}



	public void setBonusPoints(double bonusPoints) {
		this.bonusPoints = bonusPoints;
	}



	public void setPreviousTier(int previousTier) {
		this.previousTier = previousTier;
	}



	public void setCurrentTier(int currentTier) {
		this.currentTier = currentTier;
	}



	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}



	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}



	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}



	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}



	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}



	public void setDestSubscriberNumber(String destSubscriberNumber) {
		this.destSubscriberNumber = destSubscriberNumber;
	}



	public void setDestLoyaltyId(String destLoyaltyId) {
		this.destLoyaltyId = destLoyaltyId;
	}



	public void setField1(String field1) {
		this.field1 = field1;
	}



	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}



	public void setOldBalance(String oldBalance) {
		this.oldBalance = oldBalance;
	}



	public void setField2(String field2) {
		this.field2 = field2;
	}



	public void setField3(String field3) {
		this.field3 = field3;
	}



	public void setField4(String field4) {
		this.field4 = field4;
	}



	public void setField5(String field5) {
		this.field5 = field5;
	}



	public void setField6(String field6) {
		this.field6 = field6;
	}



	public void setField7(String field7) {
		this.field7 = field7;
	}



	public void setField8(String field8) {
		this.field8 = field8;
	}



	public void setField9(String field9) {
		this.field9 = field9;
	}



	public void setField10(String field10) {
		this.field10 = field10;
	}
	
}
