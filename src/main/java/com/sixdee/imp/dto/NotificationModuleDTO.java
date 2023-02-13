package com.sixdee.imp.dto;

import java.util.ArrayList;
import java.util.List;

public class NotificationModuleDTO {
	

  private String mdn;
	private boolean isEmail; 
	private boolean isSMS;   
	private String subject;    
	private String content;  
	private String emailID;
	private String languageId;
	private String serviceId;
	public ArrayList<Part> data;
	private String messageId;
	private List<String> emailList;
	private String isMerchant;
	
	
	
	
	public List<String> getEmailList() {
		return emailList;
	}
	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return emailID;
	}
	/**
	 * @param emailID the emailID to set
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	public boolean isEmail() {
		return isEmail;
	}
	public void setEmail(boolean isEmail) {
		this.isEmail = isEmail;
	}
	public boolean isSMS() {
		return isSMS;
	}
	public void setSMS(boolean isSMS) {
		this.isSMS = isSMS;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	 
	public ArrayList<Part> getData() {
		return data;
	}
	public void setData(ArrayList<Part> data) {
		this.data = data;
	}
	public String getIsMerchant() {
		return isMerchant;
	}
	public void setIsMerchant(String isMerchant) {
		this.isMerchant = isMerchant;
	}
	
	
	
    
    @Override
  public String toString() {
    return "NotificationModuleDTO [mdn=" + mdn + ", isEmail=" + isEmail + ", isSMS=" + isSMS
        + ", subject=" + subject + ", content=" + content + ", emailID=" + emailID
        + ", languageId=" + languageId + ", serviceId=" + serviceId + ", data=" + data
        + ", messageId=" + messageId + ", emailList=" + emailList + ", isMerchant=" + isMerchant
        + "]";
  }	
	
	         








}



/*class part{
	String name;
	String values;
	
}


part p=new part();
p.setName("<loyaltyid>");
p.setValue("26546254256");

part p=new part();
p.setName("<pin>");
p.setValue("343");
*/