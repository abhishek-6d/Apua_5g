package com.sixdee.imp.simulator.dto;

public class PromotionSubscriberOutPutdto {
	long id;
	String subscriberNumber;
	String lineNumber;
	String NationalId;
	String idType;
	
	public PromotionSubscriberOutPutdto(String subscriberNumber,String lineNumber, String nationalId, String idType) {
		this.subscriberNumber = subscriberNumber;
		this.lineNumber = lineNumber;
		NationalId = nationalId;
		this.idType = idType;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getNationalId() {
		return NationalId;
	}

	public void setNationalId(String nationalId) {
		NationalId = nationalId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

}
