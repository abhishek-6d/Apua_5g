/**
 * 
 */
package com.sixdee.lms.bo;

import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.Json.RequestRealTimeTrigger;
import com.sixdee.imp.response.ResponseDTO;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.dto.OnlineTriggerTableDTO;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.util.DTOConverterUtil;
import com.sixdee.lms.util.RealTimeTriggerUtil;

/**
 * @author rahul.kr
 *
 */
public class NotificationRequestProcessBO implements BusinessLogics{

	private static final Logger logger = Logger.getLogger("NotifactionRequestBO");
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		// TODO Auto-generated method stub
		RequestRealTimeTrigger realTimeTrigger = null;
		RealTimeTriggerUtil realTimeTriggerUtil = null;
		ExtNotificationDTO extNotificationDTO = null;
		DTOConverterUtil dtoConverterUtil = null;
		String requestId = null;
		Request request = null;
		InitiateRECallBO initiateRECallBO = null;
		OnlineRuleInitiatorBO onlineRuleInitiatorBO = new OnlineRuleInitiatorBO();
		List<OnlineTriggerTableDTO> onlineTableDTOList = null;
		ResponseDTO response = null;
		try{
			requestId = genericDTO.getRequestId();
			realTimeTrigger = (RequestRealTimeTrigger) genericDTO.getObj();
			//realconverEIFRequestToRuleEngineRequest(realTimeTrigger);
			realTimeTriggerUtil = new RealTimeTriggerUtil();
			extNotificationDTO = realTimeTriggerUtil.validateTrigger(requestId,realTimeTrigger.getKeyword());
			/*
			 * log request into table
			 */
			if(extNotificationDTO!=null && extNotificationDTO.getIsDiscard()==0){
				dtoConverterUtil = new DTOConverterUtil();
				request = dtoConverterUtil.convertTriggerObjectToOnlineRuleObject(realTimeTrigger);
				//realTimeTrigger.setObj(extNotificationDTO);
				//((BusinessLogics)Class.forName(extNotificationDTO.getClassName()).newInstance()).executeBusinessProcess(genericDTO);
			/*	initiateRECallBO = new InitiateRECallBO();
				initiateRECallBO.createRequestXML(extNotificationDTO, request);*/
				
				if(Cache.triggerTableMap!=null){
					onlineTableDTOList=Cache.getTriggerTableMap().get(extNotificationDTO.getTriggerId());
					if(onlineTableDTOList!=null){
						for(OnlineTriggerTableDTO onlineTriggerTableDTO : onlineTableDTOList){
							response = onlineRuleInitiatorBO.generateRECall(requestId,extNotificationDTO.getTrigger(),onlineTriggerTableDTO,request);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured ",e);
		} finally{
				genericDTO.setObj(response);
			}
			return genericDTO;
	}

	
}
