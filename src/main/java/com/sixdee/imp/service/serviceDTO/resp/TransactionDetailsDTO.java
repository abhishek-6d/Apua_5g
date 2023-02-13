package com.sixdee.imp.service.serviceDTO.resp;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class TransactionDetailsDTO
{
	
	private String date;
	private String accountLineNumber;
	private String activity;
	private int loyaltyPoints;
	private String type;
	private String expiryPoints;
	private String expiryDate;
	private Data[] data;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccountLineNumber() {
		return accountLineNumber;
	}
	public void setAccountLineNumber(String accountLineNumber) {
		this.accountLineNumber = accountLineNumber;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	 
	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	public String getExpiryPoints() {
		return expiryPoints;
	}
	public void setExpiryPoints(String expiryPoints) {
		this.expiryPoints = expiryPoints;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
}
