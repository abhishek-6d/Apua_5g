package com.sixdee.imp.fetchprofile.response;

import java.util.List;

public class ProfileInfo{
    public String profile_id;
    public String profile_type;
    public String first_name;
    public String middle_name;
    public String last_name;
    public String contact_number_1;
    public String contact_email_id;
    public String pin;
    public String ssn;
    public String tin_ein;
    public String customer_type;
    public List<Address> addresses;
	public String getProfile_id() {
		return profile_id;
	}
	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}
	public String getProfile_type() {
		return profile_type;
	}
	public void setProfile_type(String profile_type) {
		this.profile_type = profile_type;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getContact_number_1() {
		return contact_number_1;
	}
	public void setContact_number_1(String contact_number_1) {
		this.contact_number_1 = contact_number_1;
	}
	public String getContact_email_id() {
		return contact_email_id;
	}
	public void setContact_email_id(String contact_email_id) {
		this.contact_email_id = contact_email_id;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getTin_ein() {
		return tin_ein;
	}
	public void setTin_ein(String tin_ein) {
		this.tin_ein = tin_ein;
	}
	public String getCustomer_type() {
		return customer_type;
	}
	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
    
    
}
