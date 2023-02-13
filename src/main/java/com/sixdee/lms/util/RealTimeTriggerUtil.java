/**
 * 
 */
package com.sixdee.lms.util;

import org.apache.log4j.Logger;

import com.sixdee.lms.dao.RealTimeTriggerDAO;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;
import com.sixdee.lms.dto.persistent.TriggerDetailsDTO;

/**
 * @author rahul.kr
 *
 */
public class RealTimeTriggerUtil {

	private static final Logger logger = Logger.getLogger("RealTimeTriggerUtil");
	
	public ExtNotificationDTO validateTrigger(String requestId, String keyword) {
		RealTimeTriggerDAO realTimeTriggerDAO = null;
		ExtNotificationDTO extNotificationDTO = null;
		TriggerDetailsDTO triggerDetailsDTO = null;
		try {
			realTimeTriggerDAO = new RealTimeTriggerDAO();
			// extNotificationDTO = realTimeTriggerDAO.getNotificationProperties(keyword);
			extNotificationDTO = (ExtNotificationDTO) realTimeTriggerDAO
					.getNotificationProperties(ExtNotificationDTO.class, keyword);
			if (extNotificationDTO != null) {
				logger.info("extNotificationDTO dto " + extNotificationDTO.toString());
				triggerDetailsDTO = (TriggerDetailsDTO) realTimeTriggerDAO
						.getNotificationPropertiesRule(TriggerDetailsDTO.class, extNotificationDTO.getTrigger());
				logger.info("Triggers dto " + triggerDetailsDTO.toString());
				logger.info("Trigger Id " + triggerDetailsDTO.getTriggerId());
				if (triggerDetailsDTO != null)
					extNotificationDTO.setTriggerId(triggerDetailsDTO.getTriggerId());
			}
		} finally {

			triggerDetailsDTO = null;
			realTimeTriggerDAO = null;

		}
		return extNotificationDTO;
	}
}
