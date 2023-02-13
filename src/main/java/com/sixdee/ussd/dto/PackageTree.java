/**
 * 
 */
package com.sixdee.ussd.dto;

import java.util.HashMap;

import com.sixdee.ussd.webserviceHandler.PointManagementStub.AreaLocationInfoDTO;


/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class PackageTree {

	private int id = 0;
	private String categoryId = null;
	private int packageId = 0;
	private String parentId = null;
	private String info = null;
	private String packageName = null;
	private String redeemPoints = null;
	private String typeName      = null;
	private int typeId           = 0;
	private boolean isLeafNode = false;
	private HashMap<Integer,HashMap<String, String[]> > locationMap=null;
	
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public boolean isLeafNode() {
		return isLeafNode;
	}
	public void setLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(String redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	
	
	public String toString() {
		String dtoId = "ParentId : "+parentId+" PackageId : "+packageId+" PackageName : "+packageName;
		return dtoId;
	}
	public HashMap<Integer,HashMap<String, String[]> > getLocationMap() {
		return locationMap;
	}
	public void setLocationMap(HashMap<Integer,HashMap<String, String[]> > locationMap) {
		this.locationMap = locationMap;
	}
	
	
	
}
