package com.sixdee.imp.response;

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


import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.Model;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class DeleteAccountRespAssm extends RespAssmCommon {

	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException {
		
		DeleteAccountDTO delAcntDTO= (DeleteAccountDTO) genericDTO.getObj();
		ResponseDTO respDTO=new ResponseDTO();
		logger.info("STATUS CODE"+delAcntDTO.getStatusCode());
		logger.info("STATUS DESC"+delAcntDTO.getStatusDesc());
		respDTO.setStatusCode(delAcntDTO.getStatusCode());
		respDTO.setStatusDescription(delAcntDTO.getStatusDesc());
		genericDTO.setObj(respDTO);
		return genericDTO;
	}
}
