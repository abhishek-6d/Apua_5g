/**
 * 
 */
package com.sixdee.imp.validator.format;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.util.Validations;
import com.sixdee.imp.vo.GetTransHistoryVO;


/**
 * @author Rahul K K
 * @version 1.0.0
  * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GetTransHistoryFrmtValidator extends FrmtValidatorCommon{

	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		GetTransHistoryVO transactionHistoryVO = null;
		Validations validate = null;
		String subsNumber = null;
		try{
			transactionHistoryVO = (GetTransHistoryVO) genericDTO.getObj();
			if(transactionHistoryVO.getTransactionId()==null || transactionHistoryVO.getTransactionId().trim().equals("")){
				throw new CommonException("Transaction Id not recieved");
			}
			if(transactionHistoryVO.getTimestamp()==null || transactionHistoryVO.getTimestamp().trim().equals("")){
				throw new CommonException("TimeStamp not recieved or empty");
			}
			if(transactionHistoryVO.getChannel()==null || transactionHistoryVO.getChannel().trim().equals("")){
				throw new CommonException("Channel Id not recieved or empty");
			}
			subsNumber = transactionHistoryVO.getSubscriberNumber();
			if(subsNumber==null || subsNumber.trim().equals("")){
				throw new CommonException("Subsriber Number recieved is null or empty");
			}

			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(transactionHistoryVO.getSubscriberNumber()!=null&&transactionHistoryVO.getSubscriberNumber().length()==subscriberSize)
				transactionHistoryVO.setSubscriberNumber(subscriberCountryCode+transactionHistoryVO.getSubscriberNumber());
		
			if(transactionHistoryVO.getNoOfLastTransaction()==0){
					if(transactionHistoryVO.getFromDate()==null || transactionHistoryVO.getFromDate().trim().equals("")){
						throw new CommonException("No Of Last transactions to retrieve is 0 and no from date is provided");
					}else if(transactionHistoryVO.getToDate()==null || transactionHistoryVO.getToDate().trim().equals("")){
						throw new CommonException("No Of Last transactions to retrieve is 0 and no End date is provided");						
					}
			}
			
			
		//	if(transactionHistoryVO.get)
		
		}catch (CommonException e) {
			logger.error(e.getMessage()+" "+genericDTO.getTransactionId());
			genericDTO.setStatus("SC0001");
			
			throw new CommonException(e.getMessage()+" "+genericDTO.getTransactionId());
		}
		catch (Exception e) {
			logger.error("Exception occured for Transaction ["+transactionHistoryVO.getTransactionId()+"]");
			genericDTO.setStatus("SC0003");
			throw new CommonException(e);
		}
		return genericDTO;
	}

}
