package com.sixdee.imp.dto;

public class LoyaltyBalance {
private ExpiringPoints expiringPoints;
private String id;
private String balance;
private String bonusPoints;
private String statusPoints;
private String statusUpdateDate;
private String tierUpdateDate;
private String category;
public ExpiringPoints getExpiringPoints() {
	return expiringPoints;
}
public void setExpiringPoints(ExpiringPoints expiringPoints) {
	this.expiringPoints = expiringPoints;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getBalance() {
	return balance;
}
public void setBalance(String balance) {
	this.balance = balance;
}
public String getBonusPoints() {
	return bonusPoints;
}
public void setBonusPoints(String bonusPoints) {
	this.bonusPoints = bonusPoints;
}
public String getStatusPoints() {
	return statusPoints;
}
public void setStatusPoints(String statusPoints) {
	this.statusPoints = statusPoints;
}
public String getStatusUpdateDate() {
	return statusUpdateDate;
}
public void setStatusUpdateDate(String statusUpdateDate) {
	this.statusUpdateDate = statusUpdateDate;
}
public String getTierUpdateDate() {
	return tierUpdateDate;
}
public void setTierUpdateDate(String tierUpdateDate) {
	this.tierUpdateDate = tierUpdateDate;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}



}
