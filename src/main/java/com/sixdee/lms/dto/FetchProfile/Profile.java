package com.sixdee.lms.dto.FetchProfile;

public class Profile {
	private String profile_id;

	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [profile_id = " + profile_id + "]";
	}
}