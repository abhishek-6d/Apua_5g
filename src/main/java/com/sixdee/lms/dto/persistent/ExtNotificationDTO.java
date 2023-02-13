/**
 * 
 */
package com.sixdee.lms.dto.persistent;

/**
 * @author rahul.kr
 *
 */

public class ExtNotificationDTO {

	
	private String keyword = null;
	private String trigger = null;
	private int isDiscard = 0;
	private String className=null;
	private int isTableInsert = 0;
	private int isTrigger = 0;
	private int triggerId = 0;
	
	
	public int getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(int triggerId) {
		this.triggerId = triggerId;
	}
	public int getIsTrigger() {
		return isTrigger;
	}
	public void setIsTrigger(int isTrigger) {
		this.isTrigger = isTrigger;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public int getIsTableInsert() {
		return isTableInsert;
	}
	public void setIsTableInsert(int isTableInsert) {
		this.isTableInsert = isTableInsert;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public int getIsDiscard() {
		return isDiscard;
	}
	public void setIsDiscard(int isDiscard) {
		this.isDiscard = isDiscard;
	}
	@Override
	public String toString() {
		return "ExtNotificationDTO [keyword=" + keyword + ", trigger=" + trigger + ", isDiscard=" + isDiscard
				+ ", className=" + className + ", isTableInsert=" + isTableInsert + ", isTrigger=" + isTrigger
				+ ", triggerId=" + triggerId + "]";
	}
	
	
	
	
}
