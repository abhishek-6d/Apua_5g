package com.sixdee.imp.service.serviceDTO.resp;

/**
 * 
 * @author @jith
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
 * <td>June 25,2015 11:18:20 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.List;

import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;


public class VoucherPromoDTO extends ResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subscriberNo;
	private String merchandID;
	private String timestamp;
	private String transcationId;
	private String promoCode;
	private String userlimit;
	private String wholelimit;
	private String channel;
	private String languageID;
	
	
	
	public String getSubscriberNo() {
		return subscriberNo;
	}
	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}
	public String getMerchandID() {
		return merchandID;
	}
	public void setMerchandID(String merchandID) {
		this.merchandID = merchandID;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTranscationId() {
		return transcationId;
	}
	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public String getUserlimit() {
		return userlimit;
	}
	public void setUserlimit(String userlimit) {
		this.userlimit = userlimit;
	}
	public String getWholelimit() {
		return wholelimit;
	}
	public void setWholelimit(String wholelimit) {
		this.wholelimit = wholelimit;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	
	

}
