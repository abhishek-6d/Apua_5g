package com.sixdee.imp.request;

/**
 * 
 * @author Rahul K K
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
 * <td>May 27,2013 01:17:51 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.service.serviceDTO.req.SubscriberProfileDTO;
import com.sixdee.imp.vo.FailureActionVO;
import com.sixdee.ussd.util.inter.StatusCodes;



public class FailureActionReqAssm extends ReqAssmCommon {
	
	private static final Logger logger = Logger.getLogger(FailureActionReqAssm.class);
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> FailureActionReqAssm :: Method ==> buildAssembleGUIReq ");
		FailureActionVO failureActionVO = null;
		SubscriberProfileDTO subscriberProfileDTO = null;
		try{
			subscriberProfileDTO = (SubscriberProfileDTO) genericDTO.getObj();
			failureActionVO=new FailureActionVO();
			failureActionVO.setTransactionId(subscriberProfileDTO.getTransactionID());
			failureActionVO.setTimestamp(subscriberProfileDTO.getTimestamp());
			failureActionVO.setServiceName(subscriberProfileDTO.getServiceName());
			failureActionVO.setSubscriberNumber(subscriberProfileDTO.getSubscriberNumber());
			failureActionVO.setChannel(subscriberProfileDTO.getChannel());
		
			//genericDTO.setStatusCode("")
		} catch (Exception e) {
			logger.error("Exception occured",e);
			genericDTO.setStatusCode("SC0004");
			genericDTO.setStatus(StatusCodes.SC0001);
			throw new CommonException();
			
		} finally {
			
			genericDTO.setObj(failureActionVO);
			failureActionVO = null;
		}

		return genericDTO;
	}

	
}
