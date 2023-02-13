package com.sixdee.lms.dto.FetchProfile;

public class Account {
	private String show_itemized_preferences;

	private String account_id;

	public String getShow_itemized_preferences() {
		return show_itemized_preferences;
	}

	public void setShow_itemized_preferences(String show_itemized_preferences) {
		this.show_itemized_preferences = show_itemized_preferences;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [show_itemized_preferences = " + show_itemized_preferences + ", account_id = " + account_id
				+ "]";
	}
}
