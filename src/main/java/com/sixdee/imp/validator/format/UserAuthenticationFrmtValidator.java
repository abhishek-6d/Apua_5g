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
 * <td>April 24,2013 05:54:40 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.UserAuthenticationDTO;

public class UserAuthenticationFrmtValidator extends FrmtValidatorGUICommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidateGUIReq(GenericDTO genericDTO) throws CommonException {

		logger.debug("Method =>buildValidateGUIReq()");
		
		UserAuthenticationDTO userAuthenticationDTO = null;
		try{
			userAuthenticationDTO = (UserAuthenticationDTO) genericDTO.getObj();
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(userAuthenticationDTO.getSubscriberNumber()!=null&&userAuthenticationDTO.getSubscriberNumber().length()==subscriberSize)
				userAuthenticationDTO.setSubscriberNumber(subscriberCountryCode+userAuthenticationDTO.getSubscriberNumber());
			
		}catch (Exception e) {
			genericDTO.setStatusCode("SC0001");
			throw new CommonException("Some problem here. Please check as fast as possible");
		}finally{
			genericDTO.setObj(userAuthenticationDTO);
		}
		return genericDTO;
	
	}

}
