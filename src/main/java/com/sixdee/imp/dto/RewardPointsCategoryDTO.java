package com.sixdee.imp.dto;

public class RewardPointsCategoryDTO {

	private int categoryID;
	private String categoryName;
	private String categoryDesc;
	private String unitsCalculation;
	
	/**
	 * @return the categoryDesc
	 */
	public String getCategoryDesc() {
		return categoryDesc;
	}
	/**
	 * @param categoryDesc the categoryDesc to set
	 */
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	/**
	 * @return the categoryID
	 */
	public int getCategoryID() {
		return categoryID;
	}
	/**
	 * @param categoryID the categoryID to set
	 */
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @return the isInUnits
	 */
	/**
	 * @return the unitsCalculation
	 */
	public String getUnitsCalculation() {
		return unitsCalculation;
	}
	/**
	 * @param unitsCalculation the unitsCalculation to set
	 */
	public void setUnitsCalculation(String unitsCalculation) {
		this.unitsCalculation = unitsCalculation;
	}
 
	
	
	
}
