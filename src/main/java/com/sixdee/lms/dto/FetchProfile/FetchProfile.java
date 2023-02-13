package com.sixdee.lms.dto.FetchProfile;

public class FetchProfile {
	private BillingSystem BillingSystem;

	public BillingSystem getBillingSystem() {
		return BillingSystem;
	}

	public void setBillingSystem(BillingSystem BillingSystem) {
		this.BillingSystem = BillingSystem;
	}

	@Override
	public String toString() {
		return "ClassPojo [BillingSystem = " + BillingSystem + "]";
	}
}