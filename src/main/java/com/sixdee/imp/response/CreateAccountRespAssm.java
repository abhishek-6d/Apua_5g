package com.sixdee.imp.response;

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
 * <td>APR 15, 2013</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.Model;
import com.sixdee.fw.response.RespAssmGUICommon;

public class CreateAccountRespAssm extends RespAssmGUICommon {

	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		genericDTO.setObj(new Model("alarmdetailsVO", null));
		return genericDTO;
	}
}