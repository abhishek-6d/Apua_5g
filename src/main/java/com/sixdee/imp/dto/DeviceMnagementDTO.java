package com.sixdee.imp.dto;

/**
 * 
 * @author Somesh
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>September 25,2014 07:06:05 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class DeviceMnagementDTO extends CommonVO implements Serializable 
{

	private static final long serialVersionUID = 1L;
	private String transactionId;
	private String channel;
	
	private String modelName;
	private String subscriberNumber;
	private String languageId;
	private String createDate;
	private String action;
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(transactionId);
		sb.append("|");
		sb.append(channel);
		sb.append("|");
		sb.append(subscriberNumber);
		sb.append("|");
		sb.append(modelName);
		
		sb.append("|");
		sb.append(languageId);
		
		sb.append("|");
		sb.append(createDate);
		
		sb.append("|");
		sb.append(action);
		
		return sb.toString();
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
