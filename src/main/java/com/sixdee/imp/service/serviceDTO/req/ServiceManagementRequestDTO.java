package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class ServiceManagementRequestDTO 
{
	
	private String 		moNumber  	=null;
	private String 		transactionID		=null;
	private String		timestamp			=null;
	private String 		channel				=null;
	private Data[]	 	data;
	private String 		languageId			=null;
	private int    		serviceIdentifier 	=0;
	
	
	
	
	
	public String getMoNumber() {
		return moNumber;
	}
	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
	}
	
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public int getServiceIdentifier() {
		return serviceIdentifier;
	}
	public void setServiceIdentifier(int serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}
	
	
	
	

}
