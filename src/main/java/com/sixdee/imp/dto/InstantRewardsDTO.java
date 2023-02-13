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
 * <td>May 30,2013 08:26:31 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class InstantRewardsDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private String subscriberNumber = null;
	private String bSubscriberNumber = null;
	private String mode 			= null;
	private String offerId			= null;
	private boolean isTransfer = false;
	private String offerName = null;
	private String respUrl = null;
	private String packId = null;
	private long id = 0;
	private String offerSynonym = null;
	private boolean isReverseTransfer = false;
	
	
	
	public boolean isReverseTransfer() {
		return isReverseTransfer;
	}

	public void setReverseTransfer(boolean isReverseTransfer) {
		this.isReverseTransfer = isReverseTransfer;
	}

	public String getOfferSynonym() {
		return offerSynonym;
	}

	public void setOfferSynonym(String offerSynonym) {
		this.offerSynonym = offerSynonym;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getRespUrl() {
		return respUrl;
	}

	public void setRespUrl(String respUrl) {
		this.respUrl = respUrl;
	}



	
	
	
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public boolean isTransfer() {
		return isTransfer;
	}
	public void setTransfer(boolean isTransfer) {
		this.isTransfer = isTransfer;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public String getbSubscriberNumber() {
		return bSubscriberNumber;
	}
	public void setbSubscriberNumber(String bSubscriberNumber) {
		this.bSubscriberNumber = bSubscriberNumber;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	
}
