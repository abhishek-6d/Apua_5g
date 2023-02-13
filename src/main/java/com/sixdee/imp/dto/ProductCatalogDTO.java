package com.sixdee.imp.dto;

/**
 * 
 * @author S@j!th
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
 * <td>October 30,2015 11:55:15 AM</td>
 * <td>S@j!th</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class ProductCatalogDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String transactionId;
	private boolean isAdsl;
	private String subscriberNumber;
	private HashMap<String, PackageCategory> categoryMap;
	private HashMap<String, List<Integer>>  categoryMapWthParentID;
	private String channel;
	private boolean getPackage;
	private int defaultLanguageId;


	public int getDefaultLanguageId() {
		return defaultLanguageId;
	}

	public void setDefaultLanguageId(int defaultLanguageId) {
		this.defaultLanguageId = defaultLanguageId;
	}

	public boolean isGetPackage() {
		return getPackage;
	}

	public void setGetPackage(boolean getPackage) {
		this.getPackage = getPackage;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public HashMap<String, PackageCategory> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(HashMap<String, PackageCategory> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public boolean isAdsl() {
		return isAdsl;
	}

	public void setAdsl(boolean isAdsl) {
		this.isAdsl = isAdsl;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public HashMap<String, List<Integer>> getCategoryMapWthParentID() {
		return categoryMapWthParentID;
	}

	public void setCategoryMapWthParentID(
			HashMap<String, List<Integer>> categoryMapWthParentID) {
		this.categoryMapWthParentID = categoryMapWthParentID;
	}



}
