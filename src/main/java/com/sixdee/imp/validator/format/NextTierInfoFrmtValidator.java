package com.sixdee.imp.validator.format;
/**
 * 
 * @author Rahul K K
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
 * <td>June 02,2013 11:26:13 AM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.util.Validations;
import com.sixdee.imp.vo.NextTierInfoVO;

public class NextTierInfoFrmtValidator extends FrmtValidatorCommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private Logger logger = Logger.getLogger(NextTierInfoFrmtValidator.class);
	
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		if(logger.isDebugEnabled())
			logger.debug("Method =>buildValidateReq()");
		NextTierInfoVO nextTierInfoVO = null;
		Validations validations = null;
		try{
			nextTierInfoVO = (NextTierInfoVO) genericDTO.getObj();
			if(nextTierInfoVO.getTransactionId()==null || nextTierInfoVO.getTransactionId().trim().equals(""))
				throw new CommonException("Transaction id is empty");
			if(nextTierInfoVO.getTimestamp() == null || nextTierInfoVO.getTimestamp().trim().equals(""))
				throw new CommonException("TimeStamp is empty");
			
			validations = new Validations();

			if((nextTierInfoVO.getLoyaltyId()==null || nextTierInfoVO.getLoyaltyId().equals(""))&& 
					(nextTierInfoVO.getSubscriberNumber()==null || nextTierInfoVO.getSubscriberNumber().equals(""))){
				throw new CommonException("Either LoyaltyId  ["+nextTierInfoVO.getLoyaltyId()+"] or  Subscriber Number is ["+nextTierInfoVO.getSubscriberNumber()+"]");
			}
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(nextTierInfoVO.getSubscriberNumber().length()==subscriberSize)
				nextTierInfoVO.setSubscriberNumber(subscriberCountryCode+nextTierInfoVO.getSubscriberNumber());
		
			if(!validations.isNumber(nextTierInfoVO.getSubscriberNumber())){
				nextTierInfoVO.setAdslNumber(nextTierInfoVO.getSubscriberNumber());
			}
		}catch (CommonException e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus("GET_TRANS_PARAM_MISS_"+nextTierInfoVO.getLangId());
			throw e;
		}
		finally{
			genericDTO.setObj(nextTierInfoVO);
		}
		return genericDTO;
	}

}
