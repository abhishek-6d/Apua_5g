package com.sixdee.imp.service.serviceDTO.resp;


public class MerchantPackageDetailsDTO implements Comparable<MerchantPackageDetailsDTO>
{
	
	private MerchantPackageDetailsDTO[] subPackageDetails;
	private String category;
	private int categoryID;
	private String packageName;
	private int packageID;
	private int redeemPoints;
	private String info;
	private int typeId;
	private String typeName;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the subPackageDetails
	 */
	public MerchantPackageDetailsDTO[] getSubPackageDetails() {
		return subPackageDetails;
	}
	/**
	 * @param subPackageDetails the subPackageDetails to set
	 */
	public void setSubPackageDetails(MerchantPackageDetailsDTO[] subPackageDetails) {
		this.subPackageDetails = subPackageDetails;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	 
	public int getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(int redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	 
	public int getPackageID() {
		return packageID;
	}
	public void setPackageID(int packageID) {
		this.packageID = packageID;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public int compareTo(MerchantPackageDetailsDTO p) {
		if(this.redeemPoints<p.redeemPoints)
			return -1;
		else if (this.redeemPoints>p.redeemPoints)
			return 1;
		return 0;
	}
}
