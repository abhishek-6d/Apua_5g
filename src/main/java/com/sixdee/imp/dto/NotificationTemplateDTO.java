package com.sixdee.imp.dto;

public class NotificationTemplateDTO {
	
	//public String template_id;
	public String channel_id;
	public String data;
	public String subject;
	public String language_id;
	public String service_id;
	public String message_id;
	
	
	
	
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String messageId) {
		message_id = messageId;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channelId) {
		channel_id = channelId;
	}
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String serviceId) {
		service_id = serviceId;
	}
	public String getLanguage_id() {
		return language_id;
	}
	public void setLanguage_id(String languageId) {
		language_id = languageId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/*public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String templateId) {
		template_id = templateId;
	}*/
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
