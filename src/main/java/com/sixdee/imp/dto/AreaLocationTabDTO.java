package com.sixdee.imp.dto;

public class AreaLocationTabDTO {
	private int id;
	private String area;
	private String location;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setArea(String area){
		this.area = area;
	}
	
	public String getArea(){
		return area;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public String getLocation(){
		return location;
	}

}
