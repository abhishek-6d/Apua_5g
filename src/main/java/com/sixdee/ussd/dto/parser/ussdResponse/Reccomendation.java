/**
 * 
 */
package com.sixdee.ussd.dto.parser.ussdResponse;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class Reccomendation {
	
	private String application = null;
	private int menuIndex      = 1;
	private String messageText = null;
	private String timeStamp = null;
	private String nextService = null;
	
	
	public String getNextService() {
		return nextService;
	}
	public void setNextService(String nextService) {
		this.nextService = nextService;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public int getMenuIndex() {
		return menuIndex;
	}
	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
