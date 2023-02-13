package com.sixdee.imp.dto;

import java.io.Serializable;
import java.util.Date;

public class NationalNumberTabDTO implements Serializable {

	public String nationalNumber;
	public Long loyaltyID;
	public String idType;
	private int statusId ;
	public Date createDate;//sajith ks added
	
	//sajith ks added ***start
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	//sajith ks added ***end
	
	
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	/**
	 * @return the loyaltyID
	 */
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	/**
	 * @param loyaltyID the loyaltyID to set
	 */
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	/**
	 * @return the nationalNumber
	 */
	public String getNationalNumber() {
		return nationalNumber;
	}
	/**
	 * @param nationalNumber the nationalNumber to set
	 */
	public void setNationalNumber(String nationalNumber) {
		this.nationalNumber = nationalNumber;
	}

	@Override
	public boolean equals(Object object) {
		
		if(object instanceof NationalNumberTabDTO)
		{
			NationalNumberTabDTO tabDTO=(NationalNumberTabDTO)object;
	
			if(this.nationalNumber.equalsIgnoreCase(tabDTO.getNationalNumber())&&this.idType.equalsIgnoreCase(tabDTO.getIdType()))
				return true;
		}
		return false;
	}//equals
	
	

	@Override
	public String toString() {
		return "[ ID = "+getNationalNumber()+" , Type : "+getIdType()+" , Loyalty ID : "+getLoyaltyID()+" ]";
	}
	
	
}//class
