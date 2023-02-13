package com.sixdee.imp.response;

/**
 * 
 * @author @jith
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
 * <td>January 06,2016 10:55:46 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;

public class MonthlyBillServiceRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		

		
		return genericDTO;
	}
}
