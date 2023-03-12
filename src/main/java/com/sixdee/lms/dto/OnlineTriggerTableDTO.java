/**
 * 
 */
package com.sixdee.lms.dto;

import java.io.Serializable;

/**
 * @author NITHIN
 *
 */
public class OnlineTriggerTableDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long Id;
	private Integer triggerId;
	private Integer planId;
	private Integer ruleId;
	private String ruleURL;
	private String triggerName ;
	/**
	 * @return the ruleURL
	 */
	
	
	public String getRuleURL() {
		return ruleURL;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	/**
	 * @param ruleURL the ruleURL to set
	 */
	public void setRuleURL(String ruleURL) {
		this.ruleURL = ruleURL;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}
	/**
	 * @return the triggerId
	 */
	public Integer getTriggerId() {
		return triggerId;
	}
	/**
	 * @param triggerId the triggerId to set
	 */
	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}
	/**
	 * @return the planId
	 */
	public Integer getPlanId() {
		return planId;
	}
	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	/**
	 * @return the ruleId
	 */
	public Integer getRuleId() {
		return ruleId;
	}
	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "OnlineTriggerTableDTO [Id=" + Id + ", triggerId=" + triggerId + ", ruleId=" + ruleId + ", ruleURL="
				+ ruleURL + ", triggerName=" + triggerName + "]";
	}
	

	
}
