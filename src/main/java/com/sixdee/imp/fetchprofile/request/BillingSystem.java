package com.sixdee.imp.fetchprofile.request;

public class BillingSystem{
    public String request_id;
    public String request_timestamp;
    public String action;
    public String username;
    public String source;
    public String userid;
    public String entity_id;
    public Request request;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
    
    
}
