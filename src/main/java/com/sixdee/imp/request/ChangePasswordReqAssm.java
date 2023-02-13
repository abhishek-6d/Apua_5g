package com.sixdee.imp.request;

/**
 * 
 * @author Paramesh
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
 * <td>July 24,2013 03:58:55 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ChangePasswordDTO;



public class ChangePasswordReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ChangePasswordReqAssm :: Method ==> buildAssembleGUIReq ");
		ChangePasswordDTO changePasswordDTO = null;
		try{
			changePasswordDTO=new ChangePasswordDTO();

			
			com.sixdee.imp.service.serviceDTO.req.ChangePasswordDTO passwordDTO=(com.sixdee.imp.service.serviceDTO.req.ChangePasswordDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(passwordDTO.getSubscriberNumber().length()==subscriberSize)
				passwordDTO.setSubscriberNumber(subscriberCountryCode+passwordDTO.getSubscriberNumber());
			
			
			changePasswordDTO.setSubscriberNumber(passwordDTO.getSubscriberNumber());
			changePasswordDTO.setChannel(passwordDTO.getChannel());
			changePasswordDTO.setOldPin(passwordDTO.getOldPin());
			changePasswordDTO.setPin(passwordDTO.getNewPin());
			changePasswordDTO.setDefaultLanguage(passwordDTO.getLanguageId());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(changePasswordDTO);
			changePasswordDTO = null;
		}

		return genericDTO;
	}

}
