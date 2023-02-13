package com.sixdee.imp.dto;

import java.util.Date;

public class LoyaltyRedeemDTO 
{
	private long loyaltyID;
	private String subscriberNumber;
	private long redeemPoint;
	private Date date;
	private int pakcageID;
	private String voucherOrderID;
	private int statusID;
	
	private int channel;
	private int tier;
	private String subscriberType;
	private int isSindabad;
	
	
	
	
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	public String getSubscriberType() {
		return subscriberType;
	}
	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}
	/**
	 * @return the statusID
	 */
	public int getStatusID() {
		return statusID;
	}
	/**
	 * @param statusID the statusID to set
	 */
	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}
	public String getVoucherOrderID() {
		return voucherOrderID;
	}
	public void setVoucherOrderID(String voucherOrderID) {
		this.voucherOrderID = voucherOrderID;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public int getPakcageID() {
		return pakcageID;
	}
	public void setPakcageID(int pakcageID) {
		this.pakcageID = pakcageID;
	}
	public long getRedeemPoint() {
		return redeemPoint;
	}
	public void setRedeemPoint(long redeemPoint) {
		this.redeemPoint = redeemPoint;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public int getIsSindabad() {
		return isSindabad;
	}
	public void setIsSindabad(int isSindabad) {
		this.isSindabad = isSindabad;
	}
}
