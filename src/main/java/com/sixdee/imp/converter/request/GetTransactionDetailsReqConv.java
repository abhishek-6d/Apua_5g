package com.sixdee.imp.converter.request;

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

import com.sixdee.fw.converter.request.ReqConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.util.GeneralProcesses;

public class GetTransactionDetailsReqConv extends ReqConvCommon {

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. Converts Value Object to internal Data Transfer Object.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertReq(GenericDTO genericDTO)
			throws CommonException {
		logger.debug("Method => buildConvertReq()");
		GetTransactionDetailsDTO getTransactionDetailsDTO = null;
		GeneralProcesses generalProcess = null;
		try{
			getTransactionDetailsDTO = (GetTransactionDetailsDTO) genericDTO.getObj();
			generalProcess = new GeneralProcesses();
			getTransactionDetailsDTO.setSubscriberNumber(generalProcess.removalOfCountryCode(getTransactionDetailsDTO.getSubscriberNumber()));
			genericDTO.setObj(getTransactionDetailsDTO);
		}catch (Exception e) {

			logger.error("Exception occured throwing common exception ",e);
			genericDTO.setStatusCode("SC0003");
			throw new CommonException();
		}finally{
			getTransactionDetailsDTO=null;
		}
		return genericDTO;
	}
}
