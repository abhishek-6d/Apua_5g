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
 * <td>April 25,2013 06:34:23 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.SubscriberTransactionHistoryDTO;
import com.sixdee.imp.service.serviceDTO.req.ActivatePackDTO;



public class DeActivatePackReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ActivatePAckReqAssm :: Method ==> buildAssembleGUIReq ");
		ActivatePackDTO activatePackDTO = null;
		SubscriberTransactionHistoryDTO historyDTO = null;
		try{
			activatePackDTO = (ActivatePackDTO)genericDTO.getObj();
			historyDTO = new SubscriberTransactionHistoryDTO();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			
			//historyDTO.setSubscriberNumber(Long.parseLong(activatePackDTO.getSubscriberNumber()));
			if(activatePackDTO.getSubscriberNumber()!=null)
			{
				if(activatePackDTO.getSubscriberNumber().startsWith(subscriberCountryCode))
					historyDTO.setSubscriberNumber(Long.parseLong(activatePackDTO.getSubscriberNumber().substring(subscriberCountryCode.length())));
				else
					historyDTO.setSubscriberNumber(Long.parseLong(activatePackDTO.getSubscriberNumber()));
			}
			logger.info("subscriber number == >" + historyDTO.getSubscriberNumber());
			
			if(activatePackDTO.getPromoName()!=null)
				historyDTO.setServiceID(1);
			
			if(activatePackDTO.getPackID()!=0)
				logger.info("pack Id == == >"+activatePackDTO.getPackID()+" only printing ......");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(historyDTO);
			historyDTO = null;
		}

		return genericDTO;
	}

}
