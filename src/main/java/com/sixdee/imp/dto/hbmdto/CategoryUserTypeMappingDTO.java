package com.sixdee.imp.dto.hbmdto;

public class CategoryUserTypeMappingDTO {
	
	private int userType;
	private int active;
	private int categoryId;
	public int getUserTupe() {
		return userType;
	}
	public void setUserTupe(int userTupe) {
		this.userType = userTupe;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	

}
