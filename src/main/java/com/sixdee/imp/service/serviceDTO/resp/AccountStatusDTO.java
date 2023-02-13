package com.sixdee.imp.service.serviceDTO.resp;

public class AccountStatusDTO {

	private String subscriberNumber;
	private String status;
	private int typeId;
	private String typeName;
	 
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	
	@Override
	public boolean equals(Object object) {
		
		if(object instanceof AccountStatusDTO)
		{
			AccountStatusDTO statusDTO=(AccountStatusDTO)object;
	
			if(this.subscriberNumber.equalsIgnoreCase(statusDTO.getSubscriberNumber()))
				return true;
		}
		return false;
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	
	
	
}
