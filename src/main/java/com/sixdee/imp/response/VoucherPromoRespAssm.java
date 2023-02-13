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
 * <td>June 25,2015 11:18:20 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.Date;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;
import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class VoucherPromoRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		
		VoucherPromoTranverseDTO voucherPromoDTO = (VoucherPromoTranverseDTO) genericDTO.getObj();
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatusCode(voucherPromoDTO.getStatusCode());
		responseDTO.setStatusDescription(voucherPromoDTO.getStatusDescription());
		
		
		genericDTO.setObj(responseDTO);

		
		return genericDTO;
	}
}
