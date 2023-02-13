package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class UpdateAccountDTO 
{
	private String transactionId;
	private String timestamp;
	private String channel;
	private Data[] data;
	private String msisdn;
	private String dedicatedActId;
	private String amountChanrged;
	private String freeUnits;
	private String validity;
	private String factor;
	private String extendExpiry;
	private int operation;
	
	public String getAmountChanrged() {
		return amountChanrged;
	}
	public void setAmountChanrged(String amountChanrged) {
		this.amountChanrged = amountChanrged;
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
	public String getDedicatedActId() {
		return dedicatedActId;
	}
	public void setDedicatedActId(String dedicatedActId) {
		this.dedicatedActId = dedicatedActId;
	}
	public String getFactor() {
		return factor;
	}
	public void setFactor(String factor) {
		this.factor = factor;
	}
	public String getFreeUnits() {
		return freeUnits;
	}
	public void setFreeUnits(String freeUnits) {
		this.freeUnits = freeUnits;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public int getOperation() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = operation;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getExtendExpiry() {
		return extendExpiry;
	}
	public void setExtendExpiry(String extendExpiry) {
		this.extendExpiry = extendExpiry;
	}
	
	
}
