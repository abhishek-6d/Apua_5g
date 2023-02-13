package com.sixdee.imp.converter.response;

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


import com.sixdee.fw.converter.response.RespConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.GetEligiblePromoDTO;
import com.sixdee.imp.util.GeneralProcesses;
import com.sixdee.imp.vo.GetEligiblePromoVO;


public class GetEligiblePromoRespConv extends RespConvCommon {

	/**
	 * This method is called from the framework class in Response Converter Layer. Converts internal Data Transfer Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {
		logger.debug(" Method => buildConvertResp()");
		GetEligiblePromoVO getEligiblePromoVO = null;
		GeneralProcesses generalProcess = null;
		try{

			getEligiblePromoVO = (GetEligiblePromoVO) genericDTO.getObj();
			generalProcess = new GeneralProcesses();
			getEligiblePromoVO.setSubsNumber(generalProcess.appendCountryCode(getEligiblePromoVO.getSubsNumber()));
			genericDTO.setObj(getEligiblePromoVO);
		
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatus("SC0003");
			throw new CommonException();
		}
		return genericDTO;
	}

	


}
