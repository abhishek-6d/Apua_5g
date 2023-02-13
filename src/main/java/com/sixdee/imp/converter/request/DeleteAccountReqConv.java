package com.sixdee.imp.converter.request;

/**
 * 
 * @author Somesh
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
 * <td>APR 18, 2013</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.converter.request.ReqConvGUICommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class DeleteAccountReqConv extends ReqConvGUICommon {

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. Converts Value Object to internal Data Transfer Object.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	 
	@Override
	public GenericDTO buildConvertGUIReq(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}
}
