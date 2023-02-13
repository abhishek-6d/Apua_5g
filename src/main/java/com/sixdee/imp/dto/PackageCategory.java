package com.sixdee.imp.dto;

import java.io.Serializable;
import java.util.Set;

public class PackageCategory implements Serializable
{
	private Integer serialNo;
	private Integer categoryId;
	private String categoryName;
	private String categoryDesc;
	private Integer parentId;
	private String isPackage;
	private String categorySynonm;
	private int typeId;
	private int languageId;
	private Set<PackageDetails> packages;
	
	public Set<PackageDetails> getPackages() {
		return packages;
	}
	public void setPackages(Set<PackageDetails> packages) {
		this.packages = packages;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getIsPackage() {
		return isPackage;
	}
	public void setIsPackage(String isPackage) {
		this.isPackage = isPackage;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	} 
	
	@Override
	public String toString() {
		return "category="+ categoryId+"categoryname ="+ categoryName+"" +
				" category desc ="+categoryDesc+"parent id "+parentId +" is package ="+isPackage;
	}
	public String getCategorySynonm() {
		return categorySynonm;
	}
	public void setCategorySynonm(String categorySynonm) {
		this.categorySynonm = categorySynonm;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
}
