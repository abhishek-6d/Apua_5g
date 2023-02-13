package com.sixdee.imp.fetchprofile.response;

public class BillingSystem{
    public String request_id;
    public String request_timestamp;
    public String response_timestamp;
    public String action;
    public String source;
    public String username;
    public String userid;
    public String entity_id;
    public String result_code;
    public String result_desc;
    public String upfront_payment;
    public Response response;
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getRequest_timestamp() {
		return request_timestamp;
	}
	public void setRequest_timestamp(String request_timestamp) {
		this.request_timestamp = request_timestamp;
	}
	public String getResponse_timestamp() {
		return response_timestamp;
	}
	public void setResponse_timestamp(String response_timestamp) {
		this.response_timestamp = response_timestamp;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getEntity_id() {
		return entity_id;
	}
	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getResult_desc() {
		return result_desc;
	}
	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}
	public String getUpfront_payment() {
		return upfront_payment;
	}
	public void setUpfront_payment(String upfront_payment) {
		this.upfront_payment = upfront_payment;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	@Override
	public String toString() {
		return "BillingSystem [request_id=" + request_id + ", request_timestamp=" + request_timestamp
				+ ", response_timestamp=" + response_timestamp + ", action=" + action + ", source=" + source
				+ ", username=" + username + ", userid=" + userid + ", entity_id=" + entity_id + ", result_code="
				+ result_code + ", result_desc=" + result_desc + ", upfront_payment=" + upfront_payment + ", response="
				+ response + "]";
	}
    
    
}
