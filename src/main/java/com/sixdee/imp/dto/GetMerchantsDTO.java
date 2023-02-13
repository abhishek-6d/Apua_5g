package com.sixdee.imp.dto;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>June 29,2015 12:25:33 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class GetMerchantsDTO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String transactionId;
	private String subscriberNumber;
	private HashMap<Integer, PackageCategory> merchantMap;
	private HashMap<Integer, List<Integer>>  categoryMapWthParentID;
	private String channel;
	private Integer merchantId;
	private int languageId;
	
	/**
	 * @return the categoryMapWthParentID
	 */
	public HashMap<Integer, List<Integer>> getCategoryMapWthParentID() {
		return categoryMapWthParentID;
	}
	/**
	 * @param categoryMapWthParentID the categoryMapWthParentID to set
	 */
	public void setCategoryMapWthParentID(
			HashMap<Integer, List<Integer>> categoryMapWthParentID) {
		this.categoryMapWthParentID = categoryMapWthParentID;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the subscriberNumber
	 */
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * @param subscriberNumber the subscriberNumber to set
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	/**
	 * @return the merchantMap
	 */
	public HashMap<Integer, PackageCategory> getMerchantMap() {
		return merchantMap;
	}
	/**
	 * @param merchantMap the merchantMap to set
	 */
	public void setMerchantMap(HashMap<Integer, PackageCategory> merchantMap) {
		this.merchantMap = merchantMap;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the merchantId
	 */
	public Integer getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return the languageId
	 */
	public int getLanguageId() {
		return languageId;
	}
	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	


}
