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
 * <td>May 27,2013 01:17:51 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import org.apache.log4j.Logger;

import com.sixdee.fw.converter.request.ReqConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.FailureActionDTO;

public class FailureActionReqConv extends ReqConvCommon {

	private static final Logger logger = Logger.getLogger(FailureActionReqConv.class);
	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. Converts Value Object to internal Data Transfer Object.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertReq(GenericDTO genericDTO)
			throws CommonException {
		logger.debug("Method => buildConvertGUIReq()");
		FailureActionDTO failureActionDTO = null;
		//FailureActionVO failureActionVO = null;
		try{
			failureActionDTO = (FailureActionDTO) genericDTO.getObj(); 
		}catch (Exception e) {
			logger.error("Exception ",e);
			throw new CommonException();
		}
		return genericDTO;
	}
}
