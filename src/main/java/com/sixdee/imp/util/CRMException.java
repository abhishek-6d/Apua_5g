package com.sixdee.imp.util;

public class CRMException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String statusCode;
	private String statusDesc;
	
	public CRMException() {
		super();
	}
	
	public CRMException(String statusCode,String statusDesc) {
		super(statusDesc);
		setStatusCode(statusCode);
		setStatusDesc(statusDesc);
		
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	
	
}
