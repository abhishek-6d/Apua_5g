package com.sixdee.imp.dto;

/**
 * 
 * @author Rahul K K
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
 * <td>June 02,2013 11:26:13 AM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.List;

import com.sixdee.imp.service.serviceDTO.common.Data;


public class NextTierInfoDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subscriberNumber = null;
	private String adslNumber = null;
	private String loyaltyId = null;
	private String langId 	  = null;
	private TierDetails tierDetails = null;
	private int pin;
	private List<Data> dataSets;
	
	
	
	
	
	
	
	
	public List<Data> getDataSets() {
		return dataSets;
	}
	public void setDataSets(List<Data> dataSets) {
		this.dataSets = dataSets;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getLangId() {
		return langId;
	}
	public void setLangId(String langId) {
		this.langId = langId;
	}
	public String getAdslNumber() {
		return adslNumber;
	}
	public void setAdslNumber(String adslNumber) {
		this.adslNumber = adslNumber;
	}
	public TierDetails getTierDetails() {
		return tierDetails;
	}
	public void setTierDetails(TierDetails tierDetails) {
		this.tierDetails = tierDetails;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	
	
}
