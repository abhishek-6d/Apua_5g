package com.sixdee.imp.simulator.dto;

import java.util.Date;

public class NationalIDMergingTabDTONumbersFromOmantel {

    
	public long id;
	public String subscriberNumber;
	public long loyaltyId;
	public String oldNationalId;
	public String oldNationalIdType;
	public Date createTime;
	public int statusId;
	public String newNationalId;
	public String newNationalIdType;
	public int mismatchFlag;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setLoyaltyId(long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}

	public long getLoyaltyId() {
		return loyaltyId;
	}

	public void setOldNationalId(String oldNationalId) {
		this.oldNationalId = oldNationalId;
	}

	public String getOldNationalId() {
		return oldNationalId;
	}

	public void setOldNationalIdType(String oldNationalIdType) {
		this.oldNationalIdType = oldNationalIdType;
	}

	public String getOldNationalIdType() {
		return oldNationalIdType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setNewNationalId(String newNationalId) {
		this.newNationalId = newNationalId;
	}

	public String getNewNationalId() {
		return newNationalId;
	}

	public void setNewNationalIdType(String newNationalIdType) {
		this.newNationalIdType = newNationalIdType;
	}

	public String getNewNationalIdType() {
		return newNationalIdType;
	}
	
	public void setMismatchFlag(int mismatchFlag) {
		this.mismatchFlag = mismatchFlag;
	}

	public int getMismatchFlag() {
		return mismatchFlag;
	}

}
