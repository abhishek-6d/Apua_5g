package com.sixdee.imp.service.serviceDTO.resp;


public class OrderTrackingInfoDTO extends ResponseDTO  
{
	private int offSet;
	private int limit;
	private int totalCount;
	private OrderTrackingDetailsDTO[] orderDetails;

	public OrderTrackingDetailsDTO[] getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderTrackingDetailsDTO[] orderDetails) {
		this.orderDetails = orderDetails;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffSet() {
		return offSet;
	}

	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
