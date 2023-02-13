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
 * <td>April 22,2014 11:36:42 AM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.dto.ActDeactOfferDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class ActDeactOfferRespAssm extends RespAssmCommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException 
	{
		logger.info("Method => buildAssembleGUIResp()");
		ActDeactOfferDTO actDeactOfferDTO = (ActDeactOfferDTO)genericDTO.getObj();
		ResponseDTO responseDTO = new ResponseDTO();
		try
		{
			responseDTO.setStatusCode(actDeactOfferDTO.getStatusCode());
			responseDTO.setStatusDescription(actDeactOfferDTO.getStatusDesc());
		}
		catch(Exception e)
		{
			logger.error("Exception",e);
		}
		finally
		{
			genericDTO.setObj(responseDTO);
		}
		
		return genericDTO;
	}
}
