package com.sixdee.imp.dto;


public class PromoTableDTO {

	public Long loyaltyID;
	public String subscriberNumber;
	
	public PromoTableDTO(long loyaltyID, String subscriberNumber){
		this.loyaltyID = loyaltyID;
		this.subscriberNumber = subscriberNumber;
	}
	
	public Long getLoyaltyID(){
		return loyaltyID;
	}
	
	public String getSubscriberNumber(){
		return subscriberNumber;
	}
	
	public void setLoyaltyID(long loyaltyID){
		this.loyaltyID=loyaltyID;
	}
	
	public void setSubscriberNumber(String subscriberNumber){
		this.subscriberNumber=subscriberNumber;
	}
}
