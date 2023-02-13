/**
 * 
 */
package com.sixdee.ussd.dto.parser.ussdRequest;

import java.util.ArrayList;

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
public class Service {

	private String menuIndex = null;
	private String messageText = null;
	private String nextService = null;
	private int defaultOption = 0;
	private ArrayList<Parameters> paramList = null;
	
	
	
	public int getDefaultOption() {
		return defaultOption;
	}
	public void setDefaultOption(int defaultOption) {
		this.defaultOption = defaultOption;
	}
	public String getMenuIndex() {
		return menuIndex;
	}
	public void setMenuIndex(String menuIndex) {
		this.menuIndex = menuIndex;
	}
	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex+"";
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getNextService() {
		return nextService;
	}
	public void setNextService(String nextService) {
		this.nextService = nextService;
	}
	public ArrayList<Parameters> getParamList() {
		return paramList;
	}
	public void setParamList(ArrayList<Parameters> paramList) {
		this.paramList = paramList;
	}
	
	
}
