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
 * <td>April 24,2013 04:37:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.TransferPointsDTO;



public class TransferPointsReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> TransferPointsReqAssm :: Method ==> buildAssembleGUIReq ");
		TransferPointsDTO transferPointsDTO = null;
		try{
			transferPointsDTO=new TransferPointsDTO();
			
			CommonUtil commonUtil=new CommonUtil();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			com.sixdee.imp.service.serviceDTO.req.TransferPointsDTO pointsDTO=(com.sixdee.imp.service.serviceDTO.req.TransferPointsDTO)genericDTO.getObj();
			
			if(pointsDTO.getSubscriberNumber()!=null)
			{
					
				if(pointsDTO.getSubscriberNumber().length()==subscriberSize)
					transferPointsDTO.setSubscriberNumber(subscriberCountryCode+pointsDTO.getSubscriberNumber());
				else
					transferPointsDTO.setSubscriberNumber(pointsDTO.getSubscriberNumber().trim());
			}
			
			
			if(pointsDTO.getDestSubscriberNumber()!=null)
			{
					
				if(pointsDTO.getDestSubscriberNumber().length()==subscriberSize)
					transferPointsDTO.setDestinationSubscriberNumber(subscriberCountryCode+pointsDTO.getDestSubscriberNumber().trim());
				else
					transferPointsDTO.setDestinationSubscriberNumber(pointsDTO.getDestSubscriberNumber().trim());
			}
			
			
			logger.info("Subscriber Number : "+pointsDTO.getSubscriberNumber());
			logger.info("Dest Subscriber Number : "+pointsDTO.getDestSubscriberNumber());
			logger.info("Transfer points : "+pointsDTO.getPoints());
			
			
			transferPointsDTO.setTransfer(true);
			
			transferPointsDTO.setTransferPoints(Long.parseLong(pointsDTO.getPoints()+""));
			transferPointsDTO.setChannel(pointsDTO.getChannel().trim());
			transferPointsDTO.setPin(pointsDTO.getPin());
			transferPointsDTO.setSubLanguageID(pointsDTO.getLanguageID());
			transferPointsDTO.setTimestamp(pointsDTO.getTimestamp());
			transferPointsDTO.setTransactionId(pointsDTO.getTransactionID());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(transferPointsDTO);
			transferPointsDTO = null;
		}

		return genericDTO;
	}

}
