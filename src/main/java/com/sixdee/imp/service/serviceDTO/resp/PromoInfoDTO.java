package com.sixdee.imp.service.serviceDTO.resp;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class PromoInfoDTO 
{
	
	private String transactionId;
	private String timestamp;
	private Data[] data;
	private String channel;
	private String status ;
	private String statusDesc ;
	private PromoDTO[] promolist;
	
	
 	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public PromoDTO[] getPromolist() {
		return promolist;
	}
	public void setPromolist(PromoDTO[] promolist) {
		this.promolist = promolist;
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
}
