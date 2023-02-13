package com.sixdee.imp.dto;

public class Part {
	
	private String name;    
	private String values;
	
	public Part() {
	}
	
	public Part(String name,String value) {
		this.name=name;
		this.values=value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	  
	@Override
	public String toString() {
		return "Token :"+name+" value :"+values;
	}

}
