/**
 * 
 */
package com.sixdee.imp.bo.interfaces;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.lms.bo.OnlineRuleInitiatorBO;

/**
 * @author rahul.kr
 *
 */
public interface OnlineRuleInterface {

	default GenericDTO initateOnlineRule(GenericDTO genericDTO){
		OnlineRuleInitiatorBO onlineRuleInitiatorBO = new OnlineRuleInitiatorBO();
		genericDTO = onlineRuleInitiatorBO.executeBusinessProcess(genericDTO);
		return genericDTO;
	}
}
