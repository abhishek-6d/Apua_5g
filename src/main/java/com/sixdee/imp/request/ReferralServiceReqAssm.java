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
 * <td>May 21,2015 07:10:21 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ReferralServiceDTO;
import com.sixdee.imp.service.serviceDTO.req.ReferralRequestDTO;

public class ReferralServiceReqAssm extends ReqAssmGUICommon {
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ReferralServiceReqAssm :: Method ==> buildAssembleGUIReq ");
		ReferralServiceDTO referralServiceDTO = null;
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
		Date today=new Date();
		try{

			referralServiceDTO=new ReferralServiceDTO();
			ReferralRequestDTO reqDTO=(ReferralRequestDTO)genericDTO.getObj();
			referralServiceDTO.setTransactionId(reqDTO.getTransactionId());
			referralServiceDTO.setTimestamp(reqDTO.getTimestamp());
			referralServiceDTO.setChannel(reqDTO.getChannel());
			referralServiceDTO.setReferrer(reqDTO.getReferrer());
			referralServiceDTO.setReferee(reqDTO.getReferee());
			logger.info("Referral Date from Request:"+reqDTO.getReferralDate());
			if(reqDTO.getReferralDate()!=null && !reqDTO.getReferralDate().trim().equalsIgnoreCase("") && !reqDTO.getReferralDate().trim().equalsIgnoreCase("null"))
				referralServiceDTO.setReferralDate(reqDTO.getReferralDate());
			else
				referralServiceDTO.setReferralDate(dateFormat.format(today));
			
			referralServiceDTO.setLanguageId(Cache.defaultLanguageID);
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(reqDTO.getReferrer()!=null && reqDTO.getReferrer().length()==subscriberSize)
				referralServiceDTO.setReferrer(subscriberCountryCode+reqDTO.getReferrer());
			if(reqDTO.getReferee()!=null && reqDTO.getReferee().length()==subscriberSize)
				referralServiceDTO.setReferee(subscriberCountryCode+reqDTO.getReferee());
			
			if(reqDTO.getData()!=null && reqDTO.getData()[0]!=null)
			{
				if(reqDTO.getData()[0].getName()!=null && !reqDTO.getData()[0].getName().equalsIgnoreCase("") && reqDTO.getData()[0].getName().equalsIgnoreCase("LanguageID"))
				{
					try
					{
						referralServiceDTO.setLanguageId(reqDTO.getData()[0].getValue());
						logger.info("Requested Language Id = "+reqDTO.getData()[0].getValue());
					}
					catch (NumberFormatException e) {
						logger.info("Got Exception Default Language Id = "+Cache.defaultLanguageID);
						referralServiceDTO.setLanguageId(Cache.defaultLanguageID);
					}
				}
				else
				{
					logger.info("Default Language Id = "+Cache.defaultLanguageID);
					referralServiceDTO.setLanguageId(Cache.defaultLanguageID);
				}
			}
				
			
			logger.info("Transaction ID:"+reqDTO.getTransactionId());
			logger.info("TimeStamp:"+reqDTO.getTimestamp());
			logger.info("Channel:"+reqDTO.getChannel());
			logger.info("Referrer Number:"+reqDTO.getReferrer());
			logger.info("Referee Number:"+reqDTO.getReferee());
			logger.info("Referee Language ID:"+referralServiceDTO.getLanguageId());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(referralServiceDTO);
			referralServiceDTO = null;
		}

		return genericDTO;
	}

}
