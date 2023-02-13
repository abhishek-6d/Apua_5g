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
 * <td>July 10,2013 04:00:18 PM</td>
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
import com.sixdee.imp.dto.RedeemIRDTO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.util.Validations;

public class RedeemIRFrmtValidator extends FrmtValidatorCommon {
	
	
	private Logger logger = Logger.getLogger(RedeemIRFrmtValidator.class);
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	
	
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateReq()");
		RedeemIRDTO instantRewardsVO = null;
		Validations validate = null;
		String transactionId = null;
		String timeStamp = null;
		String subscriberNumber = null;
		RERequestHeader reRequestHeader = null;
		try{
			///logger.info(genericDTO.getObj());
			reRequestHeader = (RERequestHeader) genericDTO.getObj();
			instantRewardsVO = (RedeemIRDTO) reRequestHeader.getObj();
			transactionId = instantRewardsVO.getTransactionId();
			timeStamp = instantRewardsVO.getTimestamp();
			subscriberNumber = instantRewardsVO.getSubscriberNumber();
			if(transactionId == null || transactionId.equalsIgnoreCase(""))
				throw new CommonException("Transaction id recieved is null ");
			if(timeStamp == null || timeStamp.equalsIgnoreCase(""))
				throw new CommonException("Time stamp recieved is null");
			
			if(subscriberNumber==null || subscriberNumber.equalsIgnoreCase("")){
				throw new CommonException("Subscriber Number is empty");
			}
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(subscriberNumber.length()==subscriberCountryCode.length()+subscriberSize){
				instantRewardsVO.setSubscriberNumber(subscriberNumber.substring(subscriberCountryCode.length()));
			}
		
			
		}catch (CommonException e) {
			logger.error("Exception occured "+e.getMessage());
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus(e.getMessage());
			throw new CommonException(e.getMessage());
		}
		finally{
			reRequestHeader.setObj(instantRewardsVO);
			genericDTO.setObj(reRequestHeader);
		}
		return genericDTO;
	}
	
}
