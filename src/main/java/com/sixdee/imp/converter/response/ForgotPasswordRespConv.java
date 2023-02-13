package com.sixdee.imp.converter.response;

/**
 * 
 * @author Paramesh
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
 * <td>June 28,2013 03:16:37 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.converter.response.RespConvGUICommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;


public class ForgotPasswordRespConv extends RespConvGUICommon {

	/**
	 * This method is called from the framework class in Response Converter Layer. Converts internal Data Transfer Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.debug(" Method => buildConvertGUIResp()");
		return genericDTO;
	}


}
