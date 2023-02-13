package com.sixdee.imp.dto;

/**
 * 
 * @author Paramesh
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
 * <td>June 28,2013 03:16:37 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class SindabadRegistrationDTO extends CommonVO implements Serializable {
	private static Logger logger=Logger.getLogger(SindabadRegistrationDTO.class);
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subscriberNumber;
	private String lineNumber;
	private String lineLanguageId;
	private Long loyaltyID;
	private String defaultLanguage;
	private String sindabadNumber;
	private boolean mobileApp;
	private boolean travellerApp;
	private String isSindabadUpdate;
	
	
	
	
	

	public boolean isMobileApp() {
		return mobileApp;
	}

	public void setMobileApp(boolean mobileApp) {
		this.mobileApp = mobileApp;
	}

	public boolean isTravellerApp() {
		return travellerApp;
	}

	public void setTravellerApp(boolean travellerApp) {
		this.travellerApp = travellerApp;
	}

	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the lineLanguageId
	 */
	public String getLineLanguageId() {
		return lineLanguageId;
	}

	/**
	 * @param lineLanguageId the lineLanguageId to set
	 */
	public void setLineLanguageId(String lineLanguageId) {
		this.lineLanguageId = lineLanguageId;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public Long getLoyaltyID() {
		return loyaltyID;
	}

	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getSindabadNumber() {
		return sindabadNumber;
	}

	public void setSindabadNumber(String sindabadNumber) {
		this.sindabadNumber = sindabadNumber;
	}

	public void writeSindabadCdr(
			SindabadRegistrationDTO sindabadRegistrationDto,
			LoyaltyProfileTabDTO loyaltyProfileTabDTO, String transactionId, Integer partnerPoints) {
		
		logger.fatal(String.format("%-16s",sindabadRegistrationDto.getSindabadNumber())
				+String.format("%-30s","")
				+String.format("%-30s","")
				+String.format("%-8s",sdf.format(new Date()))
				+String.format("%-10s","OML")
				+String.format("%-10s","SPEND")
				+String.format("%-10s","Muscat")
				+String.format("%-20s",transactionId)
				+String.format("%010d",00)
				+String.format("%010d",partnerPoints)
				+String.format("%010d",00));
		
	}

	public String getIsSindabadUpdate() {
		return isSindabadUpdate;
	}

	public void setIsSindabadUpdate(String isSindabadUpdate) {
		this.isSindabadUpdate = isSindabadUpdate;
	}

	
	

}
