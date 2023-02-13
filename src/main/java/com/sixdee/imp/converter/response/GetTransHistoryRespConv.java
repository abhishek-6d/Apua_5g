/**
 * 
 */
package com.sixdee.imp.converter.response;

import org.apache.log4j.Logger;

import com.sixdee.fw.converter.response.RespConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.vo.GetTransHistoryVO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GetTransHistoryRespConv extends RespConvCommon{

	private static final Logger logger = Logger.getLogger(GetTransHistoryRespConv.class);
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {
		GetTransHistoryVO getTransactionHistoryVO =  null;
		try{
			getTransactionHistoryVO = (GetTransHistoryVO) genericDTO.getObj();
			genericDTO.setObj(getTransactionHistoryVO);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatus("SC0003");
		}
		return genericDTO;
	}

}
