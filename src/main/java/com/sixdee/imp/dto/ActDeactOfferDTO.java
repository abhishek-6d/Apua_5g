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
 * <td>April 22,2014 11:36:42 AM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.List;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class ActDeactOfferDTO extends CommonVO implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private String subscriberNumber;
	private String keyWord;
	private int marketingPlanId;
	private String dialCode;
	private String smsKeyword;
	private String transactionId;
	private boolean isSuccess;
	private List<Data> datasList;
	
	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getDialCode() {
		return dialCode;
	}
	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public int getMarketingPlanId() {
		return marketingPlanId;
	}
	public void setMarketingPlanId(int marketingPlanId) {
		this.marketingPlanId = marketingPlanId;
	}
	public String getSmsKeyword() {
		return smsKeyword;
	}
	public void setSmsKeyword(String smsKeyword) {
		this.smsKeyword = smsKeyword;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public List<Data> getDatasList() {
		return datasList;
	}
	public void setDatasList(List<Data> datasList) {
		this.datasList = datasList;
	}
	

}
