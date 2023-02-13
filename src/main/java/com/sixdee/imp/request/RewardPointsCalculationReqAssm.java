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
 * <td>April 26,2013 04:24:47 PM</td>
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
import com.sixdee.imp.dto.RewardPointsCalculationDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.RewardPointsDTO;



public class RewardPointsCalculationReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		RewardPointsCalculationDTO rewardPointsCalculationDTO = null;
		CommonUtil commonUtil=null;
		try{

			boolean isAccount=false;
			commonUtil = new CommonUtil();
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			
			
			rewardPointsCalculationDTO=new RewardPointsCalculationDTO();
			
			RewardPointsDTO rewardPointsDTO=(RewardPointsDTO)genericDTO.getObj();
			
			//logger.info(">>>>>>>>>"+rewardPointsDTO.getData().length);
			if(rewardPointsDTO.getData()!=null&&rewardPointsDTO.getData().length>0)
			{
				for (Data data : rewardPointsDTO.getData()) {
					logger.info(">>>>>>>>>" + data.getName());
					logger.info(">>>>>>>>>" + data.getValue());
					if (data != null && data.getName() != null && data.getValue() != null) {
						if (data.getName().trim().equalsIgnoreCase(Cache.getConfigParameterMap().get("MANUAL_ADJUSTMENT__REASON_KEYWORD").getParameterValue())) {
							rewardPointsCalculationDTO.setDesc(data.getValue());
						}
					}
				}
			}

			rewardPointsCalculationDTO.setTransactionId(rewardPointsDTO.getTransactionID());
			rewardPointsCalculationDTO.setTimestamp(rewardPointsDTO.getTimestamp());
			
			if(isAccount)
				rewardPointsCalculationDTO.setSubscriberNumber(rewardPointsDTO.getSubscriberNumber().trim());
			else
				rewardPointsCalculationDTO.setSubscriberNumber(commonUtil.discardCountryCodeIfExists(rewardPointsDTO.getSubscriberNumber()));
			
			rewardPointsCalculationDTO.setRewardPointsCategory((rewardPointsDTO.getRewardPointsCategory()==null?0:rewardPointsDTO.getRewardPointsCategory()));
			rewardPointsCalculationDTO.setVolume(rewardPointsDTO.getVolume());
			rewardPointsCalculationDTO.setRequestRewardPoints(rewardPointsDTO.getRewardPoints());
			rewardPointsCalculationDTO.setRequestStatusPoints((rewardPointsDTO.getStatusPoints()));
			rewardPointsCalculationDTO.setChannel(Cache.channelDetails.get(rewardPointsDTO.getChannel().toUpperCase()));
			if(rewardPointsDTO.getChannel().equalsIgnoreCase("ebill"))
				rewardPointsCalculationDTO.setEbill(true);
			logger.info("EBILL Feature :"+rewardPointsCalculationDTO.isEbill());
			rewardPointsCalculationDTO.setStatusPoints((rewardPointsDTO.isStatusPointsOnly()==null?false:rewardPointsDTO.isStatusPointsOnly()));
			rewardPointsCalculationDTO.setPointsCalculation(true);
			logger.info(rewardPointsCalculationDTO.getTransactionId()+": Paymnt Agency ID : "+rewardPointsCalculationDTO.getPaymentAgencyID());
			logger.info(rewardPointsCalculationDTO.getTransactionId()+": Service Type : "+rewardPointsCalculationDTO.getServiceType());
			logger.info(rewardPointsCalculationDTO.getTransactionId()+": Subscriber Number : "+rewardPointsCalculationDTO.getSubscriberNumber());
			logger.info(rewardPointsCalculationDTO.getTransactionId()+": isAccountNumber : "+isAccount);
			logger.info(rewardPointsCalculationDTO.getTransactionId()+": Reward Point Category "+rewardPointsCalculationDTO.getRewardPointsCategory());
			logger.info(rewardPointsCalculationDTO.getTransactionId()+": Credit Points Number : "+rewardPointsCalculationDTO.getVolume());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(rewardPointsCalculationDTO);
			rewardPointsCalculationDTO = null;
			commonUtil=null;
		}

		return genericDTO;
	}

}
