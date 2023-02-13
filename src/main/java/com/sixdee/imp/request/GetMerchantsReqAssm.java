package com.sixdee.imp.request;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>June 29,2015 12:25:33 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.GetMerchantsDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantDetailsDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedeemReqDTO;



public class GetMerchantsReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> GetMerchantsReqAssm :: Method ==> buildAssembleGUIReq ");
		GetMerchantsDTO getMerchantsDTO = null;
		try{
			getMerchantsDTO=new GetMerchantsDTO();
			MerchantDetailsDTO dto=(MerchantDetailsDTO) genericDTO.getObj();
			
			if(dto.getSubscriberNumber()!=null)
			{
			getMerchantsDTO.setSubscriberNumber(dto.getSubscriberNumber());
			int length = Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue().toString());
			
			if(getMerchantsDTO.getSubscriberNumber().length()==length)
				getMerchantsDTO.setSubscriberNumber(Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue()+getMerchantsDTO.getSubscriberNumber());
			}
			getMerchantsDTO.setLanguageId(Integer.parseInt(dto.getLanguageID()));
			getMerchantsDTO.setTransactionId(dto.getTransactionId());
			getMerchantsDTO.setChannel(dto.getChannel());
			getMerchantsDTO.setTimestamp(dto.getTimestamp());
			logger.info(dto.getTransactionId()+" : MERCHANT ID IN REQ:"+dto.getMerchantId());
			if(dto.getMerchantId()!=null && !dto.getMerchantId().equalsIgnoreCase("") && !dto.getMerchantId().equalsIgnoreCase("null"))
				getMerchantsDTO.setMerchantId(Integer.parseInt(dto.getMerchantId()));
			else
				getMerchantsDTO.setMerchantId(0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(getMerchantsDTO);
			getMerchantsDTO = null;
		}

		return genericDTO;
	}

}
