package com.sixdee.imp.dto;


public class UserCategoryMasterTabDto {

	//@Column(name = "CATEGORYID")
	private int categoryId;

	//@Column(name = "CATEGORYNAME")
	private String categoryName;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
