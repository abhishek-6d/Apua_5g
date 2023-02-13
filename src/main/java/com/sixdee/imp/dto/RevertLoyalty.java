package com.sixdee.imp.dto;

import java.util.Date;

public class RevertLoyalty 
{
	private long sno;
	private String transactionID;
	private long loyaltyID;
	private Date date;
	private long tableId;
	private double points;
	private String status;
	private String uniqueId;
	private String channel;
	private String packageId;
	private String subscriberNumber;
	private String tierId;
	private int isSindabad;
	
	
	public String getTierId() {
		return tierId;
	}
	public void setTierId(String tierId) {
		this.tierId = tierId;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	 
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public long getSno() {
		return sno;
	}
	public void setSno(long sno) {
		this.sno = sno;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getTableId() {
		return tableId;
	}
	public void setTableId(long tableId) {
		this.tableId = tableId;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public int getIsSindabad() {
		return isSindabad;
	}
	public void setIsSindabad(int isSindabad) {
		this.isSindabad = isSindabad;
	}
}
