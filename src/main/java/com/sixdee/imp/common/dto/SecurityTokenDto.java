package com.sixdee.imp.common.dto;

public class SecurityTokenDto {
	private long id;
	private String ostype;
	private String securityToken;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getOstype() {
		return ostype;
	}


	public void setOstype(String ostype) {
		this.ostype = ostype;
	}


	public String getSecurityToken() {
		return securityToken;
	}


	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
}
