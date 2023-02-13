package com.sixdee.imp.service.serviceDTO.resp;



public class AccountLineDTO extends ResponseDTO 
{
	
	public AccountStatusDTO[] subscriberNumbers ;
	

	public AccountStatusDTO[] getSubscriberNumbers() {
		return subscriberNumbers;
	}

	public void setSubscriberNumbers(AccountStatusDTO[] subscriberNumbers) {
		this.subscriberNumbers = subscriberNumbers;
	}

}
