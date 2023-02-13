package com.sixdee.imp.dto;

import java.util.Date;

public class ConfigureParameterDTO {

	private Integer parameterID;
	private String parameterName;
	private String parameterValue;
	private String parameterType;
	private String parameterDesc;
	private Date createDate;
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the parameterDesc
	 */
	public String getParameterDesc() {
		return parameterDesc;
	}
	/**
	 * @param parameterDesc the parameterDesc to set
	 */
	public void setParameterDesc(String parameterDesc) {
		this.parameterDesc = parameterDesc;
	}
	/**
	 * @return the parameterID
	 */
	public Integer getParameterID() {
		return parameterID;
	}
	/**
	 * @param parameterID the parameterID to set
	 */
	public void setParameterID(Integer parameterID) {
		this.parameterID = parameterID;
	}
	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	/**
	 * @return the parameterType
	 */
	public String getParameterType() {
		return parameterType;
	}
	/**
	 * @param parameterType the parameterType to set
	 */
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	/**
	 * @return the parameterValue
	 */
	public String getParameterValue() {
		return parameterValue;
	}
	/**
	 * @param parameterValue the parameterValue to set
	 */
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	
	
	
}
