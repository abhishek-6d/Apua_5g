package com.sixdee.imp.response;

/**
 * 
 * @author Himanshu
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
 * <td>October 07,2015 12:24:01 PM</td>
 * <td>Himanshu</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.dto.TerminationServiceDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class TerminationServiceRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		TerminationServiceDTO terminationServiceDTO= (TerminationServiceDTO) genericDTO.getObj();
		ResponseDTO respDTO=new ResponseDTO();
		logger.info("STATUS CODE"+terminationServiceDTO.getStatusCode());
		logger.info("STATUS DESC"+terminationServiceDTO.getStatusDesc());
		respDTO.setStatusCode(terminationServiceDTO.getStatusCode());
		respDTO.setStatusDescription(terminationServiceDTO.getStatusDesc());
		genericDTO.setObj(respDTO);
		
		return genericDTO;
	}
}
