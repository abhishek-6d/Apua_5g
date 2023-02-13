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
 * <td>January 02,2015 07:09:43 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class StartServiceRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		StartServiceDTO startServDTO= (StartServiceDTO) genericDTO.getObj();
		ResponseDTO respDTO=new ResponseDTO();
		logger.info("STATUS CODE"+startServDTO.getStatusCode());
		logger.info("STATUS DESC"+startServDTO.getStatusDesc());
		respDTO.setStatusCode(startServDTO.getStatusCode());
		respDTO.setStatusDescription(startServDTO.getStatusDesc());
		genericDTO.setObj(respDTO);
		return genericDTO;
	}
}
