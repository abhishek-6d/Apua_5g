package com.sixdee.imp.service.serviceDTO.resp;

import java.util.Arrays;

import com.sixdee.imp.service.serviceDTO.common.Data;

public class ResponseDTO 
{
	
	
	private String code;
	private String reason;
	private String id;
	private String statusCode;
	private String statusDescription;
	private String timestamp;
	private String transcationId;
	private Data[] data;
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTranscationId() {
		return transcationId;
	}
	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}
	@Override
	public String toString() {
		return "ResponseDTO [statusCode=" + statusCode + ", statusDescription=" + statusDescription + ", timestamp="
				+ timestamp + ", transcationId=" + transcationId + ", data=" + Arrays.toString(data) + "]";
	}
	
	

}
