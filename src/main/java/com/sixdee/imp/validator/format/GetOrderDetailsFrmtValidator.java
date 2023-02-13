package com.sixdee.imp.validator.format;
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
 * <td>May 29,2013 12:11:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.GetOrderDetailsDTO;

public class GetOrderDetailsFrmtValidator extends FrmtValidatorGUICommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	Logger logger = Logger.getLogger(GetOrderDetailsFrmtValidator.class);
	public GenericDTO buildValidateGUIReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		GetOrderDetailsDTO getOrderDetailsDTO = null;
		try{
			getOrderDetailsDTO = (GetOrderDetailsDTO) genericDTO.getObj();
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(getOrderDetailsDTO.getSubscriberNumber()!=null&&getOrderDetailsDTO.getSubscriberNumber().length()==subscriberSize)
				getOrderDetailsDTO.setSubscriberNumber(subscriberCountryCode+getOrderDetailsDTO.getSubscriberNumber());
			
		}catch (Exception e) {	
			genericDTO.setStatusCode("SC0001");
		genericDTO.setStatus("GET_TRANS_PARAM_MISS_"+getOrderDetailsDTO.getLangId());
		
			throw new CommonException("Some problem here. Please check as fast as possible");
		}finally{
			genericDTO.setObj(getOrderDetailsDTO);
		}
		return genericDTO;
	}

}
