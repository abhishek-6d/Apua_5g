package com.sixdee.imp.dto;

public class VoucherLocationTabDTO {

	private int id;
	private Integer packId;
	//private String locationName;
	private String areaName;
	private int langId;
	
	public void setId(int id){
		this.id = id; 
	}
	
	public int getId(){
		return id;
	}
	
	public void setPackId(int packId){
		this.packId = packId; 
	}
	
	public int getPackId(){
		return packId;
	}
	
	/*public void setLocationName(String locationName){
		this.locationName = locationName; 
	}
	
	public String getLocationName(){
		return locationName;
	}*/
	
	public void setAreaName(String areaName){
		this.areaName = areaName; 
	}
	
	public String getAreaName(){
		return areaName;
	}
	
	public void setLangId(int langId){
		this.langId = langId; 
	}
	
	public int getLangId(){
		return langId;
	}
	
}
