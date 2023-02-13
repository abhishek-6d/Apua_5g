package com.sixdee.imp.converter.response;

/**
 * 
 * @author Ananth
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
 * <td>April 20,2016 04:41:18 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.converter.response.RespConvCommon;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;


public class AlufuqSendCodesRespConv extends RespConvCommon {

	@Override
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {

		logger.debug(" Method => buildConvertGUIResp()");
		return genericDTO;
	
	}

	/**
	 * This method is called from the framework class in Response Converter Layer. Converts internal Data Transfer Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	


}
