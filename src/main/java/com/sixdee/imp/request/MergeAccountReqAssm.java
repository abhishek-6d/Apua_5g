package com.sixdee.imp.request;

/**
 * 
 * @author Jiby Jose
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
 * <td>May 17,2013 01:19:06 PM</td>
 * <td>Jiby Jose</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.MergeAccountDTO;
import com.sixdee.imp.service.serviceDTO.req.AccountMergingDTO;



public class MergeAccountReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> MergeAccountReqAssm :: Method ==> buildAssembleGUIReq ");
		
		MergeAccountDTO mergeAccountDTO = null;
		try{
			mergeAccountDTO=new MergeAccountDTO();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			AccountMergingDTO accountMergingDTO=(AccountMergingDTO)genericDTO.getObj();
			
			
			//Set Subscriber Numbers
			if(accountMergingDTO.getRegisterNumbers()[0].length()==subscriberSize)
			   mergeAccountDTO.setSubscriberNum1(subscriberCountryCode+accountMergingDTO.getRegisterNumbers()[0]);
			else
				mergeAccountDTO.setSubscriberNum1(accountMergingDTO.getRegisterNumbers()[0]);
			
			if(accountMergingDTO.getRegisterNumbers()[1].length()==subscriberSize)
				   mergeAccountDTO.setSubscriberNum2(subscriberCountryCode+accountMergingDTO.getRegisterNumbers()[1]);
			else
			   	mergeAccountDTO.setSubscriberNum2(accountMergingDTO.getRegisterNumbers()[1]);
			
			
			
			//Log Subscriber Numbers
			logger.info("SubScriber Numbers to merge are No 1 :: "+mergeAccountDTO.getSubscriberNum1()+" No 2 ::"+mergeAccountDTO.getSubscriberNum2());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(mergeAccountDTO);
			mergeAccountDTO = null;
		}

		return genericDTO;
	}

}
