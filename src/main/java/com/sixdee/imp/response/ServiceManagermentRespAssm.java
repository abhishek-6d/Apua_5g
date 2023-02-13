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
 * <td>September 20,2017 12:21:39 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.dto.ServiceManagementDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.ServiceManagementResponseDTO;

public class ServiceManagermentRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		
		ServiceManagementDTO serviceManagementDTO= (ServiceManagementDTO) genericDTO.getObj();
		ServiceManagementResponseDTO serviceManagementResponseDTO = new ServiceManagementResponseDTO();
		logger.info("STATUS CODE"+serviceManagementDTO.getStatusCode());
		logger.info("STATUS DESC"+serviceManagementDTO.getStatusDesc());
		serviceManagementResponseDTO.setStatusCode(serviceManagementDTO.getStatusCode());
		serviceManagementResponseDTO.setStatusDescription(serviceManagementDTO.getStatusDesc());
		serviceManagementResponseDTO.setTranscationId(serviceManagementDTO.getTransactionId());
		serviceManagementResponseDTO.setTimestamp(serviceManagementDTO.getTimestamp());
		genericDTO.setObj(serviceManagementResponseDTO);
		return genericDTO;

	}
}
