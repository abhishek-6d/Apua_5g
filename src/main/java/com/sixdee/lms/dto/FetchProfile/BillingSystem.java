package com.sixdee.lms.dto.FetchProfile;

public class BillingSystem {
	private String request_id;

	private Request request;

	private String action;

	private String request_timestamp;

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRequest_timestamp() {
		return request_timestamp;
	}

	public void setRequest_timestamp(String request_timestamp) {
		this.request_timestamp = request_timestamp;
	}

	@Override
	public String toString() {
		return "ClassPojo [request_id = " + request_id + ", request = " + request + ", action = " + action
				+ ", request_timestamp = " + request_timestamp + "]";
	}
}
