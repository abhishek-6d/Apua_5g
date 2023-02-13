package com.sixdee.imp.converter.request;

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
 * <td>May 29,2013 12:11:41 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.converter.request.ReqConvGUICommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.GetOrderDetailsDTO;
import com.sixdee.imp.util.GeneralProcesses;

public class GetOrderDetailsReqConv extends ReqConvGUICommon {

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. Converts Value Object to internal Data Transfer Object.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertGUIReq(GenericDTO genericDTO)
			throws CommonException {
		logger.debug("Method => buildConvertReq()");
		GetOrderDetailsDTO getOrderDetailsDTO = null;
		GeneralProcesses generalProcess = null;
		try{
			getOrderDetailsDTO = (GetOrderDetailsDTO) genericDTO.getObj();
			generalProcess = new GeneralProcesses();
			getOrderDetailsDTO.setSubscriberNumber(generalProcess.removalOfCountryCode(getOrderDetailsDTO.getSubscriberNumber()));
			genericDTO.setObj(getOrderDetailsDTO);
		}catch (Exception e) {

			logger.error("Exception occured throwing common exception ",e);
			genericDTO.setStatus("SC0003");
			throw new CommonException();
		}
		return genericDTO;
	}
}
