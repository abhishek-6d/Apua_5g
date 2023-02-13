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
 * <td>May 27,2013 01:17:51 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.util.Validations;
import com.sixdee.imp.vo.FailureActionVO;

public class FailureActionFrmtValidator extends FrmtValidatorCommon {
	
	
	private static final Logger logger = Logger.getLogger(FailureActionFrmtValidator.class);
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		FailureActionVO failureActionVO = null;
		Validations validations = null;
		try{
			failureActionVO = (FailureActionVO) genericDTO.getObj();
		/*	if(failureActionVO.getTransactionId()==null || (failureActionVO.getTransactionId().trim().equals("")))
				throw new CommonException("Transaction Id is empty");
		*/	if(failureActionVO.getSubscriberNumber()==null || (failureActionVO.getSubscriberNumber().trim().equals("")))
				throw new CommonException("Subscriber Number is empty");
			/*validations = new Validations();
			if(!(validations.validateSubsNumber(failureActionVO.getSubscriberNumber())))
				throw new CommonException("Invalid Subscriber Number "+failureActionVO.getSubscriberNumber());*/
			if(failureActionVO.getServiceName() == null || (failureActionVO.getServiceName().trim().equals("")))
				throw new CommonException("Service Name recieved is empty");
		}catch (Exception e) {
			logger.error("Exception occured",e);
			throw new CommonException();
		}finally{
			validations = null;
		}
		
		
		return genericDTO;
	}

}
