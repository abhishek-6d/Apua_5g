package com.sixdee.imp.response;

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



import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.dto.GetOfferCodeOnMarketingPlanDTO;
import com.sixdee.imp.dto.MarketingPlanDTO;
import com.sixdee.imp.service.serviceDTO.resp.PromoDTO;
import com.sixdee.imp.service.serviceDTO.resp.PromoInfoDTO;

public class GetOfferCodeOnMarketingPlanRespAssm extends RespAssmCommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private static final Logger logger = Logger.getLogger(GetOfferCodeOnMarketingPlanRespAssm.class); 
			
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleResp()");
		PromoInfoDTO promoInfoDTO = null;
		GetOfferCodeOnMarketingPlanDTO getOfferCodeOnMarketingPlanDTO = null;
		PromoDTO[] promoDTOList = null;
		HashMap<String, MarketingPlanDTO> marketingPlanMap = null;
		String loggingMessage = null;
		try{
			getOfferCodeOnMarketingPlanDTO = (GetOfferCodeOnMarketingPlanDTO) genericDTO.getObj();
			loggingMessage = getOfferCodeOnMarketingPlanDTO.getLoggingMessage();
			promoInfoDTO = new PromoInfoDTO();
			String status = getOfferCodeOnMarketingPlanDTO.getStatusCode();
			logger.debug(loggingMessage+" Status of processing "+status);
			if (status.equals("SC0000")) {
				// promoInfoDTO.setgetOfferCodeOnMarketingPlanDTO.getStatusCode())
				promoInfoDTO.setTransactionId(getOfferCodeOnMarketingPlanDTO
						.getTransactionId());
				promoInfoDTO.setTimestamp(new Date() + "");
				promoInfoDTO.setChannel(getOfferCodeOnMarketingPlanDTO
						.getChannel());
				marketingPlanMap = getOfferCodeOnMarketingPlanDTO
						.getMarketingPlanMap();
				if (marketingPlanMap != null) {
					promoDTOList = createPromoList(loggingMessage,marketingPlanMap);
				}
				promoInfoDTO.setPromolist(promoDTOList);
			}
			
		}finally{
			
			genericDTO.setObj(promoInfoDTO);
		}
		
		
		return genericDTO;
	}

	private PromoDTO[] createPromoList(String loggingMessage,
			HashMap<String, MarketingPlanDTO> marketingPlanMap) {

		PromoDTO[] promoDTOList = new PromoDTO[marketingPlanMap.size()];
		int i = 0;
		for(String key : marketingPlanMap.keySet()){
			PromoDTO promoDTO  = new PromoDTO();
			promoDTO.setOfferId(Integer.parseInt(key));
			MarketingPlanDTO marketingPlanDTO = marketingPlanMap.get(key);
			promoDTO.setOfferName(marketingPlanDTO.getMarketingPlanName());
			promoDTO.setDialCode(marketingPlanDTO.getDialCode());
			promoDTO.setSmsKeyword(marketingPlanDTO.getSmsKeyword());
			logger.debug(loggingMessage+" "+promoDTO.getOfferName()+" :Keyword: "+promoDTO.getSmsKeyword()+" :DialCode: "+promoDTO.getDialCode());
			promoDTOList[i] = promoDTO;
			i++;
		}
		return promoDTOList;
	}
}
