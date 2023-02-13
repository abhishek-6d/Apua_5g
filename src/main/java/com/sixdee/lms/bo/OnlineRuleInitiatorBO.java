/**
 * 
 */
package com.sixdee.lms.bo;

import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.RequestParseXML;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.dto.OnlineTriggerTableDTO;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.thirdPartyCall.CallThirdParty;
import com.sixdee.lms.util.RealTimeTriggerUtil;
import com.sixdee.lms.util.constant.SystemConstants;

/**
 * @author rahul.kr
 *
 */
public class OnlineRuleInitiatorBO implements BusinessLogics{

	private static final Logger logger = Logger.getLogger("OnlineRuleInitiatorBO");
	
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		// TODO Auto-generated method stub
		String requestId= null;
		String keyword = null;
		Request request = null;
		Response response = null;
		ExtNotificationDTO extNotificationDTO = null;
		RealTimeTriggerUtil realTimeTriggerUtil = null;
		List<OnlineTriggerTableDTO> onlineTableDTOList = null;
		long t1 = System.currentTimeMillis();
		try{
			
			request = (Request) genericDTO.getObj();
			requestId = request.getRequestId();
			keyword = request.getKeyWord();
			realTimeTriggerUtil = new RealTimeTriggerUtil();
			extNotificationDTO = realTimeTriggerUtil.validateTrigger(requestId, keyword);
			if(extNotificationDTO!=null && extNotificationDTO.getIsTrigger()==1){
			if(Cache.triggerTableMap!=null){
				onlineTableDTOList=Cache.getTriggerTableMap().get(extNotificationDTO.getTriggerId());
				if(onlineTableDTOList!=null){
					for(OnlineTriggerTableDTO onlineTriggerTableDTO : onlineTableDTOList){
						response = generateRECall(requestId,extNotificationDTO.getTrigger(),onlineTriggerTableDTO,request);
					}
				}else{
					logger.info(">>>no details in RE_TRIGGERS");
				}
			}
			}else{
				logger.info(">>>no details in LMS_CNFG_EXT_TRIGGER_MASTER or RE_TRIGGER is 0");
			}
			
		/*	initiateRECallBO = new InitiateRECallBO();
			initiateRECallBO.createRequestXML(extNotificationDTO, request);*/
		}finally{
			 long t2 =System.currentTimeMillis();
			genericDTO.setObj(response);
			extNotificationDTO=null;
    		request=null;
    		onlineTableDTOList=null;
    		realTimeTriggerUtil=null;
    		
			logger.info("Transaction id ::"+requestId+" keyword  ::"+keyword+"  processing Time ::"+(t2-t1));
		}
		return genericDTO;
	}

	public Response generateRECall(String requestId,String triggerName, OnlineTriggerTableDTO onlineTriggerTableDTO, Request request) {
		Response response = null;
		String reqXml = null;
		CallThirdParty callThirdParty = null;
		String respXML;
		try{
			request.getDataSet().getParameter1().add(new Param(SystemConstants.TRIGGER_NAME,triggerName));
			request.getDataSet().getParameter1().add(new Param(SystemConstants.RE_SERVICE_ID,onlineTriggerTableDTO.getTriggerId()+""));
			request.getDataSet().getParameter1().add(new Param("ID","1"));
			request.getDataSet().getParameter1().add(new Param(SystemConstants.RE_SCHEULE_ID,onlineTriggerTableDTO.getRuleId()+""));
			reqXml=RequestParseXML.getRequest().toXML(request);
			callThirdParty = new CallThirdParty();
			respXML=callThirdParty.makeThirdPartyCall(reqXml, onlineTriggerTableDTO.getRuleURL(), Integer.valueOf(Cache.configParameterMap.get("RE_CALL_TIMEOUT_CONFIG").getParameterValue().trim()));
			logger.info("Response from ThirdParty :"+respXML);
			if(respXML!=null && !(respXML.startsWith("<SUCCESS>")))
				response =  (Response) RequestParseXML.responseMultiDataXstream().fromXML(respXML);
			//else
				
		}finally{
			callThirdParty=null;
			respXML=null;
			reqXml=null;
		}
		return response;
	}

}
