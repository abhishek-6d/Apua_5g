package com.sixdee.imp.request;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.service.serviceDTO.req.RedeemDTO;



public class RedeemPointsReqAssm extends ReqAssmCommon{
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> RedeemPointsReqAssm :: Method ==> buildAssembleGUIReq ");
		RedeemPointsDTO redeemPointsDTO = null;
		CommonUtil commonUtil=null;
		try{
			commonUtil=new CommonUtil();
			redeemPointsDTO=new RedeemPointsDTO();
			RedeemDTO redeemDTO = (RedeemDTO)genericDTO.getObj();
			
			redeemPointsDTO.setChannel(redeemDTO.getChannel());
			redeemPointsDTO.setTransactionId(redeemDTO.getTransactionID());
			redeemPointsDTO.setSubscriberNumber(redeemDTO.getLineNumber());
			redeemPointsDTO.setTimestamp(redeemDTO.getTimestamp());
			redeemPointsDTO.setMoNumber(redeemDTO.getMoNumber());
			redeemPointsDTO.setPackageId(String.valueOf(redeemDTO.getPackID()));
			redeemPointsDTO.setDefaultLanguage(redeemDTO.getLanguageId());
			
			if(redeemPointsDTO.getMoNumber()!=null && redeemPointsDTO.getSubscriberNumber()!=null)
			{
				if(!redeemPointsDTO.getSubscriberNumber().equalsIgnoreCase(redeemPointsDTO.getMoNumber()))
				{
					logger.info("Subscriber number and mo number is different");
				}
			}
			if(redeemPointsDTO.getSubscriberNumber()==null && redeemPointsDTO.getMoNumber()!=null)
			{
				redeemPointsDTO.setSubscriberNumber(redeemPointsDTO.getMoNumber());
			}
			
			if(redeemPointsDTO.getSubscriberNumber()!=null && redeemPointsDTO.getChannel()!=null)
			{
				redeemPointsDTO.setRedeem(true);
			}
			
			CommonUtil util = new CommonUtil();
			
			
			if(redeemPointsDTO.isRedeem() && util.isItChar(redeemPointsDTO.getSubscriberNumber()))
			{
				redeemPointsDTO.setAdsl(true);
			}
			
		
			
			//int length = Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue().toString());
			
			redeemPointsDTO.setSubscriberNumber(commonUtil.discardCountryCodeIfExists(redeemPointsDTO.getSubscriberNumber()));
			
			logger.info("Subscriber number =="+redeemPointsDTO.getSubscriberNumber());
			logger.info("MO number =="+redeemPointsDTO.getMoNumber());
			logger.info("Channel =="+redeemPointsDTO.getChannel());
			logger.info("Package =="+redeemPointsDTO.getPackageId());
			logger.info("redeem  =="+redeemPointsDTO.isRedeem());
			logger.info("language  =="+redeemPointsDTO.getDefaultLanguage());
			logger.info("Transaction Id  =="+redeemPointsDTO.getTransactionId());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(redeemPointsDTO);
			redeemPointsDTO = null;
			commonUtil=null;
		}

		return genericDTO;
	}

}
