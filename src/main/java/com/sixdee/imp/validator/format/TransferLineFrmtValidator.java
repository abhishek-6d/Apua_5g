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
 * <td>June 20,2013 04:39:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.request.TransferLineVO;

public class TransferLineFrmtValidator extends FrmtValidatorCommon {
	
	private Logger logger = Logger.getLogger(TransferLineFrmtValidator.class);
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method => buildConvertReq()");
		TransferLineVO transferLineVO = (TransferLineVO) genericDTO.getObj();
		String donorSubscriber = null;
		String recieverSubscriber = null;
		String txnId = null;
		try{
			txnId = transferLineVO.getTransactionId();
			if(txnId
					==null || txnId.trim().equals(""))
				throw new CommonException("Transcation id is coming as empty");
			if(transferLineVO.getTimestamp() == null || transferLineVO.getTimestamp().trim().equals(""))
				throw new CommonException("TimeStamp is coming as empty");
			donorSubscriber = transferLineVO.getDonorSubscriber();
			if(donorSubscriber == null || donorSubscriber.trim().equals(""))
				throw new CommonException("Donor Subscriber Number is empty");
			recieverSubscriber= transferLineVO.getRecieverSubscriber();
			if(recieverSubscriber==null || recieverSubscriber.trim().equals(""))
				throw new CommonException("B Subscriber number is empty");
			/*validations = new Validations();
			if(validations.isNumber(donorSubscriber))
				transferLineVO.setaAdslNumber(donorSubscriber);
			if(validations.isNumber(transferLineVO.getbSubscriberNumber()))
				transferLineVO.setbAdslNumber(transferLineVO.getbAdslNumber());
	*/	}catch (CommonException e) {
			logger.error("TransactionId "+txnId+" Exception occured ",e);
			transferLineVO.setStatusCode("SC0001");
			transferLineVO.setStatusDesc(e.getMessage());
			genericDTO.setStatusCode("1");
			genericDTO.setStatusCode("Mandatory Parmaeter Missing");
		}finally{
			logger = null;
		}
		genericDTO.setObj(transferLineVO);
		return genericDTO;
	}

}
