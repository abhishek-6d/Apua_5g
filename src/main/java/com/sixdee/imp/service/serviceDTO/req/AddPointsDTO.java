package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.PointAdjustmentId;

public class AddPointsDTO 
{
	
	private String accountNumber;
	private int numberOfPoints;
	private String pointAdjustmentId;
	private String userName;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPointAdjustmentId() {
		return pointAdjustmentId;
	}
	public void setPointAdjustmentId(String pointAdjustmentId) {
		this.pointAdjustmentId = pointAdjustmentId;
	}
	
	
	
	
	
}
