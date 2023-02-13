/**
 * 
 */
package com.sixdee.ussd.dto;

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
public class KeywordMappingDTO {

	private int id = 0;
	private int serviceId = 0;
	private String KeyWord = null;
	private int parentService = 0;
	private String desc = null;
	private String channel = null;
	private String serviceType = null;
	private int respReqd = 0;
	private String responseMenu = null;
	private String respService = null;
	private int respServiceId = 0;
	private int level = 0;
	private int authReqd = 0;
	private int langId = 0;
	private int backOptionEnabled = 0;
	
	
	
	
	
	
	
	
	public int getBackOptionEnabled() {
		return backOptionEnabled;
	}
	public void setBackOptionEnabled(int backOptionEnabled) {
		this.backOptionEnabled = backOptionEnabled;
	}
	public int getAuthReqd() {
		return authReqd;
	}
	public void setAuthReqd(int authReqd) {
		this.authReqd = authReqd;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRespReqd() {
		return respReqd;
	}
	public void setRespReqd(int respReqd) {
		this.respReqd = respReqd;
	}

	public String getResponseMenu() {
		return responseMenu;
	}
	public void setResponseMenu(String responseMenu) {
		this.responseMenu = responseMenu;
	}
	public String getRespService() {
		return respService;
	}
	public void setRespService(String respService) {
		this.respService = respService;
	}
	public int getRespServiceId() {
		return respServiceId;
	}
	public void setRespServiceId(int respServiceId) {
		this.respServiceId = respServiceId;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getKeyWord() {
		return KeyWord;
	}
	public void setKeyWord(String keyWord) {
		KeyWord = keyWord;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public int getParentService() {
		return parentService;
	}
	public void setParentService(int parentService) {
		this.parentService = parentService;
	}
	
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	
}
