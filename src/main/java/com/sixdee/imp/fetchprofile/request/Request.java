package com.sixdee.imp.fetchprofile.request;

public class Request{
    public Filter filter;
    public CustomerInfo customer_info;
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public CustomerInfo getCustomer_info() {
		return customer_info;
	}
	public void setCustomer_info(CustomerInfo customer_info) {
		this.customer_info = customer_info;
	}
    
    
}
