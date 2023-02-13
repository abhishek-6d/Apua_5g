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
 * <td>May 21,2015 07:10:21 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.dto.ReferralServiceDTO;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class ReferralServiceRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		ReferralServiceDTO referralDTO= (ReferralServiceDTO) genericDTO.getObj();
		ResponseDTO respDTO=new ResponseDTO();
		logger.info("STATUS CODE"+referralDTO.getStatusCode());
		logger.info("STATUS DESC"+referralDTO.getStatusDesc());
		respDTO.setStatusCode(referralDTO.getStatusCode());
		respDTO.setStatusDescription(referralDTO.getStatusDesc());
		genericDTO.setObj(respDTO);
		return genericDTO;
	}
}
