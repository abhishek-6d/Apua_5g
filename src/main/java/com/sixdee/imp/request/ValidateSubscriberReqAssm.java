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
 * <td>May 08,2013 02:27:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.SubscriberProfileDTO;



public class ValidateSubscriberReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		SubscriberProfileDTO profileDTO = null;
		
		try{
			profileDTO=new SubscriberProfileDTO();
			
			com.sixdee.imp.service.serviceDTO.req.SubscriberProfileDTO subscriberProfileDTO=(com.sixdee.imp.service.serviceDTO.req.SubscriberProfileDTO)genericDTO.getObj();
			
			profileDTO.setTransactionId(subscriberProfileDTO.getTransactionID());
			profileDTO.setServiceName(subscriberProfileDTO.getServiceName());
			profileDTO.setSubscriberNumber(Long.parseLong(subscriberProfileDTO.getSubscriberNumber()));
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(profileDTO);
			profileDTO = null;
		}

		return genericDTO;
	}

}
