package com.sixdee.imp.dto;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class GetTransHistoryDTO extends CommonVO {
	private String subscriberNumber;
	private String fromDate;
	private String toDate;
	private int noOfLastTransaction;
	private Data[] data;
	private Object obj ;
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public int getNoOfLastTransaction() {
		return noOfLastTransaction;
	}
	public void setNoOfLastTransaction(int noOfLastTransaction) {
		this.noOfLastTransaction = noOfLastTransaction;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
