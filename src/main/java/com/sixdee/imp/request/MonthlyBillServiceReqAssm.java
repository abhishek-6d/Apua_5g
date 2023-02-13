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
 * <td>January 06,2016 10:55:46 AM</td>
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
import com.sixdee.imp.dto.MonthlyBillServiceDTO;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedemptionDTO;
import com.sixdee.imp.service.serviceDTO.req.MonthlyBillDTO;



public class MonthlyBillServiceReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> MonthlyBillServiceReqAssm :: Method ==> buildAssembleGUIReq ");
		MonthlyBillServiceDTO monthlyBillServiceDTO = null;
		try{
			monthlyBillServiceDTO=new MonthlyBillServiceDTO();
			
			
			MonthlyBillDTO monthlyBillDTO=(MonthlyBillDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			
		
			if(monthlyBillDTO.getChannel()!=null && !monthlyBillDTO.getChannel().equalsIgnoreCase(""))
				monthlyBillServiceDTO.setChannel(monthlyBillDTO.getChannel());
			
			
			
			if(monthlyBillDTO.getAccountNo()!=null && !monthlyBillDTO.getAccountNo().equalsIgnoreCase(""))
				monthlyBillServiceDTO.setAccountNo(monthlyBillDTO.getAccountNo());
			
			
			if(monthlyBillDTO.getMsisdn()!=null && monthlyBillDTO.getMsisdn().length()==subscriberSize)
				monthlyBillServiceDTO.setSubscriberNo(subscriberCountryCode+monthlyBillDTO.getMsisdn());
			else
				monthlyBillServiceDTO.setSubscriberNo(monthlyBillDTO.getMsisdn());
			
			if(monthlyBillDTO.getRequestDate()!=null && !monthlyBillDTO.getRequestDate().equalsIgnoreCase(""))
				monthlyBillServiceDTO.setRequestDate(monthlyBillDTO.getRequestDate());
			
			
			
			monthlyBillServiceDTO.setTranscationId(monthlyBillDTO.getTransactionId());
			monthlyBillServiceDTO.setTimestamp(monthlyBillDTO.getTimestamp());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(monthlyBillServiceDTO);
			monthlyBillServiceDTO = null;
		}

		return genericDTO;
	}

}
