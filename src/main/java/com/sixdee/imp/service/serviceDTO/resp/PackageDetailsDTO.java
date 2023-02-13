package com.sixdee.imp.service.serviceDTO.resp;

import java.util.Arrays;

import com.sixdee.imp.dto.AreaLocationInfoDTO;


public class PackageDetailsDTO implements Comparable<PackageDetailsDTO>
{
	
	private PackageDetailsDTO[] subPackageDetails;
	private String category;
	private String packageName;
	private String offerType;
	private int cost;
	private String discount;
	private String merchantName;
	private String packageID;
	private int redeemPoints;
	private String info;
	private int typeId;
	private String typeName;
	private String squareImagePath;
	private String rectangleImagePath;
	private AreaLocationInfoDTO [] areaLocations ;
	
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the subPackageDetails
	 */
	public PackageDetailsDTO[] getSubPackageDetails() {
		return subPackageDetails;
	}
	/**
	 * @param subPackageDetails the subPackageDetails to set
	 */
	
	
	public void setSubPackageDetails(PackageDetailsDTO[] subPackageDetails) {
		this.subPackageDetails = subPackageDetails;
	}
	public String getSquareImagePath() {
		return squareImagePath;
	}
	public void setSquareImagePath(String squareImagePath) {
		this.squareImagePath = squareImagePath;
	}
	public String getRectangleImagePath() {
		return rectangleImagePath;
	}
	public void setRectangleImagePath(String rectangleImagePath) {
		this.rectangleImagePath = rectangleImagePath;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getPackageID() {
		return packageID;
	}
	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}
	public AreaLocationInfoDTO [] getAreaLocations() {
		return areaLocations;
	}
	public void setAreaLocations(AreaLocationInfoDTO [] areaLocations) {
		this.areaLocations = areaLocations;
	}
	
	
	public int compareTo(PackageDetailsDTO p) {
		if(this.redeemPoints<p.redeemPoints)
			return -1;
		else if (this.redeemPoints>p.redeemPoints)
			return 1;
		return 0;
	}
	@Override
	public String toString() {
		return "PackageDetailsDTO [subPackageDetails=" + Arrays.toString(subPackageDetails) + ", category=" + category
				+ ", packageName=" + packageName + ", offerType=" + offerType + ", cost=" + cost + ", discount="
				+ discount + ", merchantName=" + merchantName + ", packageID=" + packageID + ", redeemPoints="
				+ redeemPoints + ", info=" + info + ", typeId=" + typeId + ", typeName=" + typeName
				+ ", squareImagePath=" + squareImagePath + ", rectangleImagePath=" + rectangleImagePath
				+ ", areaLocations=" + Arrays.toString(areaLocations) + "]";
	}
	
	
}
