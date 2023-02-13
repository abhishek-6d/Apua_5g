/**
 * 
 */
package com.sixdee.lms.bo;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.dto.Json.RequestRealTimeTrigger;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.util.DTOConverterUtil;

/**
 * @author rahul.kr
 *
 */
public class RERequestProcessBO implements BusinessLogics{

	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		RequestRealTimeTrigger requestRealTimeTrigger = null;
		ExtNotificationDTO extNotificationDTO = null;
		DTOConverterUtil dtoConverterUtil = null;
		InitiateRECallBO initiateRECallBO = null;
		Request requestHeader = null;
		Response response = null;
		try{
			requestRealTimeTrigger = (RequestRealTimeTrigger) genericDTO.getObj();
			extNotificationDTO = (ExtNotificationDTO) requestRealTimeTrigger.getObj();
			dtoConverterUtil = new DTOConverterUtil();
			requestHeader = dtoConverterUtil.convertTriggerObjectToOnlineRuleObject(requestRealTimeTrigger);
			initiateRECallBO = new InitiateRECallBO();
			response = initiateRECallBO.createRequestXML(extNotificationDTO, requestHeader);
		}catch (Exception e) {
			if(response==null){
				response = new Response();
				response.setRespCode("SC0002");
				response.setRespDesc(e.getMessage());
			}
		}
		finally{
			genericDTO.setObj(response);
		}
		return genericDTO;
	}

}
