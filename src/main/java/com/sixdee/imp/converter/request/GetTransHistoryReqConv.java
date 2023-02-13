/**
 * 
 */
package com.sixdee.imp.converter.request;

import com.sixdee.fw.converter.request.ReqConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.GetTransHistoryDTO;
import com.sixdee.imp.util.GeneralProcesses;

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
public class GetTransHistoryReqConv extends ReqConvCommon{

	public GenericDTO buildConvertReq(GenericDTO genericDTO) throws CommonException {
		GetTransHistoryDTO getTransactionHistoryDTO = null;
		GeneralProcesses generalProcesses = null;
		try{
			getTransactionHistoryDTO = (GetTransHistoryDTO) genericDTO.getObj();
			generalProcesses = new GeneralProcesses();
			getTransactionHistoryDTO.setSubscriberNumber(generalProcesses.removalOfCountryCode(getTransactionHistoryDTO.getSubscriberNumber()));
			genericDTO.setObj(getTransactionHistoryDTO);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatus("SC0003");
		}
		return genericDTO;
	}

}
