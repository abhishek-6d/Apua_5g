package com.sixdee.imp.request;

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



import org.apache.log4j.Logger;

import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.dto.GetOrderDetailsDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.OrderTrackingDTO;



public class GetOrderDetailsReqAssm extends ReqAssmGUICommon {
	
	private static final Logger logger= Logger.getLogger(GetOrderDetailsReqAssm.class);
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> GetOrderDetailsReqAssm :: Method ==> buildAssembleGUIReq ");
		GetOrderDetailsDTO getOrderDetailsDTO = null;
		
		OrderTrackingDTO orderDTO =null;
		String subsNumber = null;
		try{

			getOrderDetailsDTO=new GetOrderDetailsDTO();
			orderDTO= (OrderTrackingDTO) genericDTO.getObj();
	
			getOrderDetailsDTO.setLangId(orderDTO.getLanguageID());
			getOrderDetailsDTO.setTransactionId(orderDTO.getTranscationId());
			genericDTO.setTimestamp(orderDTO.getTimestamp());
			getOrderDetailsDTO.setTimestamp(orderDTO.getTimestamp());
			getOrderDetailsDTO.setMsisdn(subsNumber);
			getOrderDetailsDTO.setSubscriberNumber(orderDTO.getSubscriberNumber());
			getOrderDetailsDTO.setChannel(orderDTO.getChannel()+"");
			getOrderDetailsDTO.setFromDate(orderDTO.getFromDate());
			getOrderDetailsDTO.setEndDate(orderDTO.getToDate());
			getOrderDetailsDTO.setNoOfMonths(orderDTO.getNoOfMonths());
			getOrderDetailsDTO.setNoOfLastTransactions(orderDTO.getNoOfLastTransaction());
			getOrderDetailsDTO.setOffset(orderDTO.getOffSet());
			getOrderDetailsDTO.setLimit(orderDTO.getLimit());
			logger.info("FROM DATE"+getOrderDetailsDTO.getFromDate());
			logger.info("END DATE"+getOrderDetailsDTO.getEndDate());
			logger.info("NO OF MONTHS"+getOrderDetailsDTO.getNoOfMonths());
			logger.info("LAST TRANS"+getOrderDetailsDTO.getNoOfLastTransactions());			
			genericDTO.setTimestamp(orderDTO.getTimestamp());
			genericDTO.setTransactionId(orderDTO.getTranscationId());
			genericDTO.setStatus("SC0000");
			Data data[] = orderDTO.getData();
			if(data!=null && data.length>0)
			{
				if(data[0].getName()!=null && data[0].getValue()!=null)
				if(data[0].getName().equalsIgnoreCase("isLulu"))
				{
					if(data[0].getValue().equalsIgnoreCase("true"))
						getOrderDetailsDTO.setLuluVoucher(true);
					logger.info("Is LULU Voucher >>>"+getOrderDetailsDTO.isLuluVoucher());	
				}
			}
			
			genericDTO.setObj(getOrderDetailsDTO);

		} catch (Exception e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus("GET_TRANS_COMMON_FAIL_"+getOrderDetailsDTO.getLangId());
		} finally {
			genericDTO.setObj(getOrderDetailsDTO);
			getOrderDetailsDTO = null;
		}

		return genericDTO;
	}

}
