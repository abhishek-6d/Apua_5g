package com.sixdee.imp.request;
/**
 * 
 * @author Paramesh
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
 * <td>APR 15, 2013</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.UpdateVoucherDetailsDTO;
import com.sixdee.imp.service.serviceDTO.req.OrderStatusDTO;

public class UpdateVoucherDetailsReqAssm extends ReqAssmCommon 
{
	 
	/**
	 * This method is called from the framework class in Request Assembler Layer. Converts request Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	Logger logger=Logger.getLogger(UpdateVoucherDetailsReqAssm.class);
	

	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {

		UpdateVoucherDetailsDTO updateVoucherDetailsDTO=null;
		try {
			updateVoucherDetailsDTO=new UpdateVoucherDetailsDTO();
			

			
			OrderStatusDTO updateVoucherDTO=(OrderStatusDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(updateVoucherDTO.getSubscriberNumber()!=null && updateVoucherDTO.getSubscriberNumber().length()==subscriberSize)
				updateVoucherDetailsDTO.setSubscriberNo(subscriberCountryCode+updateVoucherDTO.getSubscriberNumber());
			else
				updateVoucherDetailsDTO.setSubscriberNo(updateVoucherDTO.getSubscriberNumber());
				updateVoucherDetailsDTO.setStatus(updateVoucherDTO.getStatusId());
			
			
			
			if(updateVoucherDTO.getOrderIds()!=null && updateVoucherDTO.getOrderIds().length>0)
			{
				List<String> orderList=Arrays.asList(updateVoucherDTO.getOrderIds());
				updateVoucherDetailsDTO.setOrderList(orderList);
			}
			
			updateVoucherDetailsDTO.setTransactionId(updateVoucherDTO.getTransactionId());
			updateVoucherDetailsDTO.setTimestamp(updateVoucherDTO.getTimestamp());
			
			
		}catch (Exception e) {
				e.printStackTrace();
		}finally {
			genericDTO.setObj(updateVoucherDetailsDTO);
		}
		return genericDTO;
	
	}
	
	
}
