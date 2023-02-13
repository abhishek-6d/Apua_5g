package com.sixdee.imp.dto;



public class SMS_NotificationDTO {
	
	public String feature;
	public String response_url;
	public String user_name;
	public String campaign_name;
	public String circle_name;
	public String channel;
	public String oa;
	public String start_date;
	public String end_date;
	public String language_id;
	
	
	/**
	 * @return the language_id
	 */
	public String getLanguage_id() {
		return language_id;
	}
	/**
	 * @param language_id the language_id to set
	 */
	public void setLanguage_id(String language_id) {
		this.language_id = language_id;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getResponse_url() {
		return response_url;
	}
	public void setResponse_url(String responseUrl) {
		response_url = responseUrl;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getCampaign_name() {
		return campaign_name;
	}
	public void setCampaign_name(String campaignName) {
		campaign_name = campaignName;
	}
	public String getCircle_name() {
		return circle_name;
	}
	public void setCircle_name(String circleName) {
		circle_name = circleName;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getOa() {
		return oa;
	}
	public void setOa(String oa) {
		this.oa = oa;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String startDate) {
		start_date = startDate;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String endDate) {
		end_date = endDate;
	}
	
	

}
