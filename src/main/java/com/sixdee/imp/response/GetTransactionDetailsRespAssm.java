package com.sixdee.imp.response;

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



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;

public class GetTransactionDetailsRespAssm extends RespAssmCommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleResp()");
		
	//	GetTransactionDetailsDTO getTransactionDetailsDTO = (GetTransactionDetailsDTO) genericDTO.getObj();
	//	getTransactionDetailsDTO.setStatusCode(genericDTO.getStatus());
	//	genericDTO.setObj(getTransactionDetailsDTO);
		logger.info(genericDTO.getStatusCode());
		return genericDTO;
	}
}
