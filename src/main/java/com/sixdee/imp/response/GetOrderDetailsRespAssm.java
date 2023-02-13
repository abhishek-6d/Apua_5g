package com.sixdee.imp.response;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>May 29,2013 12:11:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.dto.GetOrderDetailsDTO;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;

public class GetOrderDetailsRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleResp()");
		
		GetOrderDetailsDTO getOrderDetailsDTO = (GetOrderDetailsDTO) genericDTO.getObj();
		//getOrderDetailsDTO.setStatusCode(genericDTO.getStatus());
		logger.info("getOrderDetailsDTO.getStatusCode()@@@@@@@@@@@@@@@@@@@"+getOrderDetailsDTO.getStatusCode());
		logger.info("getOrderDetailsDTO.getStatusDesc()@@@@@@@@@@@@@@@@@@@"+getOrderDetailsDTO.getStatusDesc());
	//	if(getOrderDetailsDTO.getStatusCode().equals("SC0000"))
	//		genericDTO.setStatusCode("0");
	//	else
	//		genericDTO.setStatusCode("1");
		if(getOrderDetailsDTO!=null)
		genericDTO.setObj(getOrderDetailsDTO);
		
		return genericDTO;
	}
}
