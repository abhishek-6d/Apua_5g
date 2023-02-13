package com.sixdee.imp.simulator;

public class LoyaltyLineNewCustIDDTO {

	private Long loyaltyId;
	private String lineNumber;
	private String lmsCustId;
	private String lmsCustIdType;
	
	private String crmCustId;
	private String crmCustIdType;
	
	public String getCrmCustId() {
		return crmCustId;
	}
	public void setCrmCustId(String crmCustId) {
		this.crmCustId = crmCustId;
	}
	public String getCrmCustIdType() {
		return crmCustIdType;
	}
	public void setCrmCustIdType(String crmCustIdType) {
		this.crmCustIdType = crmCustIdType;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getLmsCustId() {
		return lmsCustId;
	}
	public void setLmsCustId(String lmsCustId) {
		this.lmsCustId = lmsCustId;
	}
	public String getLmsCustIdType() {
		return lmsCustIdType;
	}
	public void setLmsCustIdType(String lmsCustIdType) {
		this.lmsCustIdType = lmsCustIdType;
	}
	public Long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	
	
	 
	
	
}
