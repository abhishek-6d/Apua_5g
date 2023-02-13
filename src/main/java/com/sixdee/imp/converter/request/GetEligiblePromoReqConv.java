package com.sixdee.imp.converter.request;

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
 * <td>April 24,2013 05:44:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.converter.request.ReqConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.GetEligiblePromoDTO;
import com.sixdee.imp.util.GeneralProcesses;

public class GetEligiblePromoReqConv extends ReqConvCommon {

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. Converts Value Object to internal Data Transfer Object.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertReq(GenericDTO genericDTO)
			throws CommonException {
		logger.debug("Method => buildConvertReq()");
		GetEligiblePromoDTO getEligiblePromoDTO = null;
		GeneralProcesses generalProcesses = null;
		
		try{
			getEligiblePromoDTO = (GetEligiblePromoDTO) genericDTO.getObj();
			generalProcesses = new GeneralProcesses();
			getEligiblePromoDTO.setSubsNumber(generalProcesses.removalOfCountryCode(getEligiblePromoDTO.getSubsNumber()));
			genericDTO.setObj(getEligiblePromoDTO);
		}catch (Exception e) {
			logger.error("Exception occured throwing common exception ",e);
			genericDTO.setStatus("SC0003");
			throw new CommonException();
		}
		return genericDTO;
	}
}
