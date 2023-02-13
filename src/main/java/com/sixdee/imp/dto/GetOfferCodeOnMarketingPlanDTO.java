package com.sixdee.imp.dto;

/**
 * 
 * @author Rahul
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
 * <td>March 06,2015 11:46:47 AM</td>
 * <td>Rahul</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.HashMap;


public class GetOfferCodeOnMarketingPlanDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String marketingPlanId = null;
	private String marketingPlanName = null;
	private String userName = null;
	private String password = null;
	private String loggingMessage = null;
	private HashMap<String, MarketingPlanDTO> marketingPlanMap = null;
	
	
	public HashMap<String, MarketingPlanDTO> getMarketingPlanMap() {
		return marketingPlanMap;
	}
	public void setMarketingPlanMap(
			HashMap<String, MarketingPlanDTO> marketingPlanMap) {
		this.marketingPlanMap = marketingPlanMap;
	}
	public String getLoggingMessage() {
		return loggingMessage;
	}
	public void setLoggingMessage(String loggingMessage) {
		this.loggingMessage = loggingMessage;
	}

	public String getMarketingPlanId() {
		return marketingPlanId;
	}
	public void setMarketingPlanId(String marketingPlanId) {
		this.marketingPlanId = marketingPlanId;
	}
	public String getMarketingPlanName() {
		return marketingPlanName;
	}
	public void setMarketingPlanName(String marketingPlanName) {
		this.marketingPlanName = marketingPlanName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
}
