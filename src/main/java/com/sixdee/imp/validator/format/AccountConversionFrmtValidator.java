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
 * <td>January 21,2014 12:55:09 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.vo.AccountConversionVO;

public class AccountConversionFrmtValidator extends FrmtValidatorCommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateReq()");
		AccountConversionVO accountConversionVO = null;
		String subscriberNumber = null;
		String transactionId 	= null;
		int currentType 		= 0;
		int newType 	= 0;
		String subscriberCountryCode = null;
		
		try{
			accountConversionVO = (AccountConversionVO) genericDTO.getObj();
			transactionId = accountConversionVO.getTransactionId();
			if(transactionId==null||transactionId.trim().equals("")){
				throw new CommonException("Transaction Id Missing in request");
			}
			subscriberNumber = accountConversionVO.getSubscriberNumber();
			if(subscriberNumber==null||subscriberNumber.trim().equalsIgnoreCase("")){
				throw new CommonException("Customer Number missing in request");
			}
			currentType = accountConversionVO.getCurrentTypeId();
			if(currentType==0 ){
				throw new CommonException("Current Type of customer is missing");
			}
			newType = accountConversionVO.getNewTypeId();
			if(newType==0){
				throw new CommonException("New type of customer is missing");
			}
			if(currentType == newType){
				throw new CommonException("Current Type "+currentType+" is same as new type "+newType);
			}
			 subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(subscriberNumber.length()==subscriberSize)
				accountConversionVO.setSubscriberNumber(subscriberCountryCode+subscriberNumber);
		
			/*if(!(validate.validateSubsNumber(subsNumber.trim()))){
				throw new CommonException("Subscriber Number ["+subsNumber+"] does not meet validation " +
						"criterias ");
			}*/
		}catch(CommonException ce){
			accountConversionVO.setStatusCode("SC0001");
			accountConversionVO.setStatusDesc(ce.getMessage());
			throw ce;
		}finally{
			genericDTO.setObj(accountConversionVO);
			subscriberNumber = null;
			//currentType = null;
			transactionId = null;
			subscriberCountryCode = null;
		}
		return genericDTO;
	}

}
