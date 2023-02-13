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
 * <td>May 06,2013 06:00:07 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.util.Validations;
import com.sixdee.imp.vo.GetTransactionDetailsVO;

public class GetTransactionDetailsFrmtValidator extends FrmtValidatorCommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateReq()");
	
		GetTransactionDetailsDTO transactionHistoryVO = null;
		Validations validate = null;
		String subsNumber = null;
		boolean isSubs = true;
		
		try{
		
			transactionHistoryVO = (GetTransactionDetailsDTO) genericDTO.getObj();
			validate = new Validations();
			
			
			if(transactionHistoryVO.getTransactionId()==null || transactionHistoryVO.getTransactionId().trim().equals("")){
				throw new CommonException("Transaction Id not recieved");
			}
			if(transactionHistoryVO.getTimestamp()==null || transactionHistoryVO.getTimestamp().trim().equals("") /*||
					!(validate.validateTimeStamp(transactionHistoryVO.getTimestamp(), "ddMMyyyyHHmmSS"))*/){
				throw new CommonException("TimeStamp not recieved or empty");
			}
			if(transactionHistoryVO.getChannel()==null || transactionHistoryVO.getChannel().trim().equals("")){
				throw new CommonException("Channel Id not recieved or empty");
			}
			subsNumber = transactionHistoryVO.getSubscriberNumber();
			if(subsNumber==null || subsNumber.trim().equals("")){
				isSubs = false;
				throw new CommonException("Subsriber Number recieved is null or empty");
			}
			
			/*if(!validate.isNumber(subsNumber)){
				transactionHistoryVO.setAdsl(true);
			}
		//	if()
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(subsNumber!=null&&subsNumber.length()==subscriberSize)
				transactionHistoryVO.setSubscriberNumber(subscriberCountryCode+subsNumber);
		
			if(!(validate.validateSubsNumber(subsNumber.trim()))){
				throw new CommonException("Subscriber Number ["+subsNumber+"] does not meet validation " +
						"criterias ");
			}*/
			if(transactionHistoryVO.getFromDate()==null || transactionHistoryVO.getFromDate().trim().equals("") || transactionHistoryVO.getEndDate()==null || transactionHistoryVO.getEndDate().trim().equals("")){
				if(transactionHistoryVO.getNoOfMonths() == 0){
					if(transactionHistoryVO.getNoOfLastTransactions()==0){
						throw new CommonException("Missing either of mandatory Parameter FromDate-ToDate/NoOfLastMonth/Number Of Transactions");
						
					}
				}
			}else{
				//Have to validate TimeStamp
				if(!validate.validateTimeStamp(transactionHistoryVO.getFromDate().trim(), "dd-MM-yyyy")){
					throw new CommonException("From Date not valid ["+transactionHistoryVO.getFromDate()+"]");
				}if(!validate.validateTimeStamp(transactionHistoryVO.getEndDate().trim(), "dd-MM-yyyy")){
					throw new CommonException("End Date not valid ["+transactionHistoryVO.getEndDate()+"]");
				}
			}
			
			
		//	if(transactionHistoryVO.get)
		
		}catch (CommonException e) {
			logger.error(e.getMessage()+" "+genericDTO.getTransactionId());
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_TRANS_PARAM_MISS_"+transactionHistoryVO.getLangId()).getStatusDesc());
			throw new CommonException(e.getMessage()+" "+genericDTO.getTransactionId());
		}catch (Exception e) {
			logger.error("Exception occured for Transaction ["+transactionHistoryVO.getTransactionId()+"]",e);
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_TRANS_COMMON_FAIL_"+transactionHistoryVO.getLangId()).getStatusDesc());
			throw new CommonException(e);
		}finally{
			genericDTO.setObj(transactionHistoryVO);
			validate = null;
		}
		return genericDTO;
	
	}

}
