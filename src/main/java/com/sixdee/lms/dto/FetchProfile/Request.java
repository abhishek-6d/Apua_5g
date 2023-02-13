package com.sixdee.lms.dto.FetchProfile;

public class Request {
	private Service service;

	//private Account account;

	//private Profile profile;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return "Request [service=" + service + "]";
	}

/*	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}*/


}