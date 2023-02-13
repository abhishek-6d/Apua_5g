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
 * <td>May 27,2013 01:17:51 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */


import org.apache.log4j.Logger;

import com.sixdee.fw.converter.response.RespConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.vo.FailureActionVO;


public class FailureActionRespConv extends RespConvCommon {

	private static final Logger logger = Logger.getLogger(FailureActionRespConv.class);
	/**
	 * This method is called from the framework class in Response Converter Layer. Converts internal Data Transfer Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method => buildConvertGUIReq()");
		FailureActionVO failureActionVO = null;
		//FailureActionVO failureActionVO = null;
		try{
			failureActionVO = (FailureActionVO) genericDTO.getObj(); 
			genericDTO.setObj(failureActionVO);
		}catch (Exception e) {
			logger.error("Exception ",e);
			throw new CommonException();
		}
		return genericDTO;
	}


}
