package com.sixdee.imp.request;

/**
 * 
 * @author Somesh
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>September 25,2014 07:06:05 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.DeviceMnagementDTO;
import com.sixdee.imp.service.serviceDTO.req.DeviceDTO;



public class DeviceMnagementReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> DeviceMnagementReqAssm :: Method ==> buildAssembleGUIReq ");
		DeviceMnagementDTO deviceMnagementDTO = null;
		try{
			deviceMnagementDTO=new DeviceMnagementDTO();
			
			DeviceDTO deviceDTO = (DeviceDTO)genericDTO.getObj();
			
			deviceMnagementDTO.setChannel(Cache.channelDetails.get(deviceDTO.getChannel().toUpperCase()));
			deviceMnagementDTO.setTransactionId(deviceDTO.getTransactionId());
			deviceMnagementDTO.setModelName(deviceDTO.getModelName());
			deviceMnagementDTO.setSubscriberNumber(deviceDTO.getSubscriberNumber());
			deviceMnagementDTO.setLanguageId(deviceDTO.getLanguageId());
			
			deviceMnagementDTO.setAction(deviceDTO.getData()[0].getValue());  //DATA for Action
			if(deviceMnagementDTO.getLanguageId()==null || deviceMnagementDTO.getLanguageId().trim().equalsIgnoreCase(""))
				deviceMnagementDTO.setLanguageId(Cache.defaultLanguageID);  // Default language id 
			
			int length = Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue().toString());
			
			if(deviceDTO.getSubscriberNumber().length()==length)
				deviceMnagementDTO.setSubscriberNumber(Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue()+deviceMnagementDTO.getSubscriberNumber());

			
			logger.info("Channel        ="+deviceMnagementDTO.getChannel());
			logger.info("Transaction id ="+deviceMnagementDTO.getTransactionId());
			logger.info("Model name     ="+deviceMnagementDTO.getModelName());
			logger.info("Subscriber no  ="+deviceMnagementDTO.getSubscriberNumber());
			logger.info("language id    ="+deviceMnagementDTO.getLanguageId());
			
			logger.info("Action         ="+deviceMnagementDTO.getAction());
			


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(deviceMnagementDTO);
			deviceMnagementDTO = null;
		}

		return genericDTO;
	}

}
