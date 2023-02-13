package com.sixdee.imp.fetchprofile.response;

public class FetchProfileResponse {
	public BillingSystem BillingSystem;

	public BillingSystem getBillingSystem() {
		return BillingSystem;
	}

	public void setBillingSystem(BillingSystem billingSystem) {
		BillingSystem = billingSystem;
	}

	@Override
	public String toString() {
		return "FetchProfileResponse [BillingSystem=" + BillingSystem + "]";
	}
	
	
}
