package com.sixdee.imp.request;

/**
 * 
 * @author Rahul
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
 * <td>March 06,2015 11:46:47 AM</td>
 * <td>Rahul</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;
import org.apache.tomcat.modules.aaa.RealmBase;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.GetOfferCodeOnMarketingPlanDTO;
import com.sixdee.imp.service.serviceDTO.req.GetPromoDTO;



public class GetOfferCodeOnMarketingPlanReqAssm extends ReqAssmCommon {
	
	private static final Logger logger = Logger.getLogger(GetOfferCodeOnMarketingPlanReqAssm.class);
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> GetOfferCodeOnMarketingPlanReqAssm :: Method ==> buildAssembleReq ");
		GetOfferCodeOnMarketingPlanDTO getOfferCodeOnMarketingPlanDTO = null;
		GetPromoDTO getOfferCodeDTO = null;
		String requestId = null;
		try{
			getOfferCodeOnMarketingPlanDTO=new GetOfferCodeOnMarketingPlanDTO();
			getOfferCodeDTO = (GetPromoDTO) genericDTO.getObj();
			requestId = getOfferCodeDTO.getTransactionId();
			getOfferCodeOnMarketingPlanDTO = createTraversalObject(requestId,getOfferCodeOnMarketingPlanDTO,getOfferCodeDTO);
		} catch (Exception e) {
			logger.error("Service : GetPromoList - Transaction ID :- "+requestId+" Exception occured ",e );
			getOfferCodeOnMarketingPlanDTO.setStatusCode("SC0001");
			getOfferCodeOnMarketingPlanDTO.setStatusDesc("Invalid Request");
		} finally {
			genericDTO.setObj(getOfferCodeOnMarketingPlanDTO);
			getOfferCodeOnMarketingPlanDTO = null;
		}

		return genericDTO;
	}

	private GetOfferCodeOnMarketingPlanDTO createTraversalObject(String requestId,
			GetOfferCodeOnMarketingPlanDTO getOfferCodeOnMarketingPlanDTO,
			GetPromoDTO getOfferCodeDTO) {
		String loggingString = "Service : GetPromoList - Transaction ID :- "+requestId;
		getOfferCodeOnMarketingPlanDTO.setTransactionId(getOfferCodeDTO.getTransactionId());
		getOfferCodeOnMarketingPlanDTO.setTimestamp(getOfferCodeDTO.getTimestamp());
		getOfferCodeOnMarketingPlanDTO.setChannel(getOfferCodeDTO.getChannel());
		getOfferCodeOnMarketingPlanDTO.setUserName(getOfferCodeDTO.getUserName());
		getOfferCodeOnMarketingPlanDTO.setPassword(getOfferCodeDTO.getPassword());
		getOfferCodeOnMarketingPlanDTO.setMarketingPlanName(getOfferCodeDTO.getMarketingPlanName());
		getOfferCodeOnMarketingPlanDTO.setMarketingPlanId(getOfferCodeDTO.getMarketingPlanId());
		getOfferCodeOnMarketingPlanDTO.setLoggingMessage(loggingString);
		getOfferCodeOnMarketingPlanDTO.setStatusCode("SC0000");
		getOfferCodeOnMarketingPlanDTO.setStatusDesc("SUCCESS");

		 
		return getOfferCodeOnMarketingPlanDTO;
	}

}
