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
 * <td>May 06,2013 06:00:07 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.converter.response.RespConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.vo.GetTransactionDetailsVO;


public class GetTransactionDetailsRespConv extends RespConvCommon {

	/**
	 * This method is called from the framework class in Response Converter Layer. Converts internal Data Transfer Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {
		logger.debug(" Method => buildConvertResp()");
		/*GetTransactionDetailsVO getTransactionDetailsVO = null;
		getTransactionDetailsVO = (GetTransactionDetailsVO) genericDTO.getObj();
		genericDTO.setObj(getTransactionDetailsVO);
		getTransactionDetailsVO=null;*/
		return genericDTO;
	}


}
