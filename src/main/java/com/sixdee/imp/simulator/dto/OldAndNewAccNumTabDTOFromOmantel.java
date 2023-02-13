package com.sixdee.imp.simulator.dto;

public class OldAndNewAccNumTabDTOFromOmantel {



	public long id;
	public String subscriberNumber;
	public Long accountNumber;
	public Long loyaltyID;
	public String status;
	public Long newAccountNumber;
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getId(){
		return id;
	}
	
	public void setSubscriberNumber(String subscriberNumber){
		this.subscriberNumber = subscriberNumber;
	}
	
	public String getSubscriberNumber(){
		return subscriberNumber;
	}
	
	public void setAccountNumber(Long accountNumber){
		this.accountNumber = accountNumber;
	}
	
	public Long getAccountNumber(){
		return accountNumber;
	}
	
	public void setLoyaltyID(Long loyaltyID){
		this.loyaltyID = loyaltyID;
	}
	
	public Long getloyaltyID(){
		return loyaltyID;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setNewAccountNumber(Long newAccountNumber){
		this.newAccountNumber = newAccountNumber;
	}
	
	public Long getNewAccountNumber(){
		return newAccountNumber;
	}
	

}
