package com.sixdee.imp.request;

/**
 * 
 * @author @jith
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
 * <td>September 20,2017 12:21:39 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ChurnPredictionSummaryDTO;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.ChurnPredictionSummaryRequestDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedemptionDTO;



public class ChurnPredictionSummaryReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ChurnPredictionSummaryReqAssm :: Method ==> buildAssembleGUIReq ");
		ChurnPredictionSummaryDTO churnPredictionSummaryDTO = null;
		try{
			churnPredictionSummaryDTO=new ChurnPredictionSummaryDTO();

			ChurnPredictionSummaryRequestDTO predictionSummaryRequestDTO=(ChurnPredictionSummaryRequestDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(predictionSummaryRequestDTO.getSubscriberNumber()!=null && predictionSummaryRequestDTO.getSubscriberNumber().length()==subscriberSize)
				churnPredictionSummaryDTO.setSubscriberNo(subscriberCountryCode+predictionSummaryRequestDTO.getSubscriberNumber());
			else
				churnPredictionSummaryDTO.setSubscriberNo(predictionSummaryRequestDTO.getSubscriberNumber());
			
			if(predictionSummaryRequestDTO.getChannel()!=null && !predictionSummaryRequestDTO.getChannel().equalsIgnoreCase(""))
				churnPredictionSummaryDTO.setChannel(predictionSummaryRequestDTO.getChannel());
			
			if(predictionSummaryRequestDTO.getLanguageId()!=null && !predictionSummaryRequestDTO.getLanguageId().equalsIgnoreCase(""))
				churnPredictionSummaryDTO.setLanguageID(predictionSummaryRequestDTO.getLanguageId());
			
	
			
			churnPredictionSummaryDTO.setTranscationId(predictionSummaryRequestDTO.getTransactionID());
			churnPredictionSummaryDTO.setTimestamp(predictionSummaryRequestDTO.getTimestamp());
			
			
			

		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(churnPredictionSummaryDTO);
			churnPredictionSummaryDTO = null;
		}

		return genericDTO;
	}

}
