package com.sixdee.imp.dto;

import java.util.Date;

public class LoyaltyHierarchyDetailsDTO {
	
	private int id;
	private Long parentLoyaltyId;
	private Long childLoyaltyId;
	private Date createDate;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Long getParentLoyaltyId() {
		return parentLoyaltyId;
	}
	public void setParentLoyaltyId(Long parentLoyaltyId) {
		this.parentLoyaltyId = parentLoyaltyId;
	}
	public Long getChildLoyaltyId() {
		return childLoyaltyId;
	}
	public void setChildLoyaltyId(Long childLoyaltyId) {
		this.childLoyaltyId = childLoyaltyId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	

}
