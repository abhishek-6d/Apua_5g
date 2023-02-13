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
 * <td>June 02,2013 11:26:13 AM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import org.apache.log4j.Logger;

import com.sixdee.fw.converter.request.ReqConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class NextTierInfoReqConv extends ReqConvCommon {

	private Logger logger = Logger.getLogger(NextTierInfoReqConv.class);
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
		
		return genericDTO;
	}
}
