package com.sixdee.imp.validator.format;
/**
 * 
 * @author Geevan
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
 * <td>May 11,2013 08:47:22 AM</td>
 * <td>Geevan</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.UserprofileDTO;


public class UserprofileFrmtValidator extends FrmtValidatorGUICommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	Logger logger = Logger.getLogger(UserprofileFrmtValidator.class);
	/*public GenericDTO buildValidateGUIReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		
		try{
			userProfileDTO = (UserprofileDTO) genericDTO.getObj();
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			
			if(userProfileDTO.getSubscriberNumber()!=null&&userProfileDTO.getSubscriberNumber().length()==subscriberSize)
				userProfileDTO.setSubscriberNumber(subscriberCountryCode+userProfileDTO.getSubscriberNumber());
		
		}catch (Exception e) {
			genericDTO.setStatusCode("SC0001");
			throw new CommonException("System Error Please try again");
		}finally{
			genericDTO.setObj(userProfileDTO);
		}
		return genericDTO;
	}*/
	public GenericDTO buildValidateGUIReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		UserprofileDTO userProfileDTO = null;
		try{
			userProfileDTO = (UserprofileDTO) genericDTO.getObj();
			if((userProfileDTO.getTransactionId()==null || userProfileDTO.getTransactionId().equalsIgnoreCase("")) ){
				throw new CommonException("Transaction id is missing in request");
			}
			if((userProfileDTO.getTimestamp()==null || userProfileDTO.getTimestamp().equalsIgnoreCase("")) ){
				throw new CommonException("TimeStamp id is missing in request");
			}
			if((userProfileDTO.getChannel()==null || userProfileDTO.getChannel().equalsIgnoreCase("")) ){
				throw new CommonException("Channel id is missing in request");
			}
			if((userProfileDTO.getSubscriberNumber()==null || userProfileDTO.getSubscriberNumber().equalsIgnoreCase("")) ){
				throw new CommonException("SubscriberNumber id is missing in request");
			}
			
		}catch(CommonException ce){
			userProfileDTO.setStatusCode("SC0001");
			userProfileDTO.setStatusDesc(ce.getMessage());
			genericDTO.setStatus(ce.getMessage());
			logger.info(">>>>mandatory parameter missing>>>>");
			throw ce;
		}
		genericDTO.setObj(userProfileDTO);
		return genericDTO;
	}

}
