package com.sixdee.imp.service.serviceDTO.resp;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class OrderTrackingDetailsDTO  
{
	
	private String orderId;
	private String orderDate;
	private String itemNumber;
	private String itemName;
	private int quantity;
	private int redeemPoints;
	private String orderStatus;
	private String orderStatusId;
	private String orderExpiryDate;
	private Data[] data;
	
	
	
	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(int redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	public String getOrderExpiryDate() {
		return orderExpiryDate;
	}
	public void setOrderExpiryDate(String orderExpiryDate) {
		this.orderExpiryDate = orderExpiryDate;
	}
	public String getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(String orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

}
