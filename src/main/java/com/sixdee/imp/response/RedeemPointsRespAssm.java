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
 * <td>May 15,2013 12:24:17 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class RedeemPointsRespAssm extends RespAssmCommon{
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		RedeemPointsDTO redeemPointsDTO = null;
		ResponseDTO responseDTO = null;
		try
		{
			redeemPointsDTO = (RedeemPointsDTO)genericDTO.getObj();
			logger.info("Status in response Assm ="+genericDTO.getStatusCode());
			logger.info("Status in response Assm ="+genericDTO.getStatus());
			
			responseDTO = new ResponseDTO();
			responseDTO.setTimestamp(df.format(new Date()));
			responseDTO.setTranscationId(genericDTO.getTransactionId());
			responseDTO.setStatusCode(genericDTO.getStatusCode());
			responseDTO.setStatusDescription(genericDTO.getStatus());
			genericDTO.setObj(responseDTO);
			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return genericDTO;
	}

}