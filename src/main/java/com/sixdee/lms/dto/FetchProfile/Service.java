package com.sixdee.lms.dto.FetchProfile;

public class Service {
	private String service_id;

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [service_id = " + service_id + "]";
	}
}
