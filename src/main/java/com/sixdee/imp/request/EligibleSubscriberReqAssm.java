package com.sixdee.imp.request;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 04,2013 07:46:17 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.EligibleSubscriberDTO;



public class EligibleSubscriberReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> EligibleSubscriberReqAssm :: Method ==> buildAssembleGUIReq ");
		EligibleSubscriberDTO eligibleSubscriberDTO = null;
		try{
			eligibleSubscriberDTO=new EligibleSubscriberDTO();
			com.sixdee.imp.service.serviceDTO.req.EligibleSubscriberDTO reqDTO = (com.sixdee.imp.service.serviceDTO.req.EligibleSubscriberDTO)genericDTO.getObj();
			
			eligibleSubscriberDTO.setChannel(Cache.channelDetails.get(reqDTO.getChannel().toUpperCase()));
			eligibleSubscriberDTO.setMsisdn(reqDTO.getMsisdn());
			eligibleSubscriberDTO.setMonth(reqDTO.getMonth());
			eligibleSubscriberDTO.setTransactionId(reqDTO.getTransactionId());
			eligibleSubscriberDTO.setUserName(reqDTO.getUserName());
			eligibleSubscriberDTO.setPassword(reqDTO.getPassword());
			
			logger.info("usernam = "+eligibleSubscriberDTO.getUserName());
			logger.info("passwd  = "+eligibleSubscriberDTO.getPassword());
			logger.info("channel = "+eligibleSubscriberDTO.getChannel());
			logger.info("msisdn  = "+eligibleSubscriberDTO.getMsisdn());
			logger.info("month   = "+eligibleSubscriberDTO.getMonth());
			logger.info("TransactionId   = "+eligibleSubscriberDTO.getTransactionId());
			
			

		} catch (Exception e) {
			logger.error(eligibleSubscriberDTO.getTransactionId()+" Transaction id ",e);
		} finally {
			genericDTO.setObj(eligibleSubscriberDTO);
			eligibleSubscriberDTO = null;
		}

		return genericDTO;
	}

}
