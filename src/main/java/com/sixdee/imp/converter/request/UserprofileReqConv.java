package com.sixdee.imp.converter.request;

/**
 * 
 * @author Geevan
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
 * <td>May 11,2013 08:47:22 AM</td>
 * <td>Geevan</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.converter.request.ReqConvGUICommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class UserprofileReqConv extends ReqConvGUICommon {

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. Converts Value Object to internal Data Transfer Object.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertGUIReq(GenericDTO genericDTO)
			throws CommonException {
		logger.debug("Method => buildConvertGUIReq()");

		return genericDTO;
	}
}
