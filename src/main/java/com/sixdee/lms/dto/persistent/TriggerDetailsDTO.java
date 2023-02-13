/**
 * 
 */
package com.sixdee.lms.dto.persistent;

import java.util.Set;

import com.sixdee.lms.dto.OnlineTriggerTableDTO;

/**
 * @author rahul.kr
 *
 */
public class TriggerDetailsDTO {

	private String triggerName = null;
	private int triggerId = 0;
	
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public int getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(int triggerId) {
		this.triggerId = triggerId;
	}
	@Override
	public String toString() {
		return "TriggerDetailsDTO [triggerName=" + triggerName + ", triggerId=" + triggerId + "]";
	}
	
	
	
	
}
