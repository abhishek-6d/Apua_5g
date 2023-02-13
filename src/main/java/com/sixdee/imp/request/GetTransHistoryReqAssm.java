/**
 * 
 */
package com.sixdee.imp.request;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.service.serviceDTO.req.TransactionHistoryDTO;
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
public class GetTransHistoryReqAssm extends ReqAssmCommon{

	private static final Logger logger = Logger.getLogger(GetTransHistoryReqAssm.class);
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		GetTransHistoryVO accountVO = null;
		TransactionHistoryDTO transactionHistoryDTO = null;
		try{
			transactionHistoryDTO = (TransactionHistoryDTO)genericDTO.getObj();
			accountVO = new GetTransHistoryVO();
			accountVO.setTransactionId(transactionHistoryDTO.getTransactionId());
			accountVO.setTimestamp(transactionHistoryDTO.getTimestamp());
			accountVO.setChannel(transactionHistoryDTO.getChannel());
			accountVO.setData(transactionHistoryDTO.getData());
			accountVO.setNoOfLastTransaction(transactionHistoryDTO.getNoOfLastTransaction());
			accountVO.setFromDate(transactionHistoryDTO.getFromDate());
			accountVO.setToDate(transactionHistoryDTO.getToDate());
			accountVO.setSubscriberNumber(transactionHistoryDTO.getSubscriberNumber());
			
			genericDTO.setTransactionId(accountVO.getTransactionId());
			logger.info("Request recieved for transactionid ["+genericDTO.getTransactionId()+"] " +
					"for Subscriber Number ["+accountVO.getSubscriberNumber()+"]");
			genericDTO.setStatus("SC0000");
		}catch (ClassCastException e) {
			logger.error("Immediate Attention requested . Please check services are mapped properly",e);
			genericDTO.setStatus("SC1000");
			throw new CommonException(e.getMessage());
		}
		catch (Exception e) {
			logger.error("Exception occured . Please check",e);
			genericDTO.setStatus("SC0003");
			throw new CommonException(e.getMessage());			
		}finally{
			genericDTO.setObj(accountVO);
			transactionHistoryDTO = null;
		}
		return genericDTO;
	}

}
