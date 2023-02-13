/**
 * 
 */
package com.sixdee.imp.validator.format;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.vo.ProfileInformationUpdateVO;

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
public class UpdateSubscriberInfoFrmtValidator extends FrmtValidatorCommon {

	/* (non-Javadoc)
	 * @see com.sixdee.fw.validator.format.FrmtValidatorInfCommon#buildValidateReq(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		ProfileInformationUpdateVO profileInformationUpdateVO = null;
		String emptyString = "";
		try{
			profileInformationUpdateVO = (ProfileInformationUpdateVO) genericDTO.getObj();
			logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateVO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateVO.getMsisdn()+"]  Update Profile subscriber info reached formated layer");
			if(profileInformationUpdateVO.getTransactionId()==null || profileInformationUpdateVO.getTransactionId().trim().equals(emptyString)){
				throw new CommonException("TransactionId cannot be null");
			}if(profileInformationUpdateVO.getMsisdn()==null||profileInformationUpdateVO.getMsisdn().trim().equals(emptyString)){
				throw new CommonException("Msisdn cannot be null");
			}if(profileInformationUpdateVO.getOldNationalId()==null||profileInformationUpdateVO.getOldNationalId().trim().equals(emptyString)){
				throw new CommonException("Old National Id cannot be null");
			}if(profileInformationUpdateVO.getOldNationalIdType()==null||profileInformationUpdateVO.getOldNationalIdType().trim().equals(emptyString)){
				throw new CommonException("Old National Id Type cannot be null");
			}if(profileInformationUpdateVO.getNewNationalId()==null||profileInformationUpdateVO.getNewNationalIdType().trim().equals(emptyString)){
				throw new CommonException("New National Id Type cannot be null");
			}if(profileInformationUpdateVO.getNewNationalIdType()==null||profileInformationUpdateVO.getNewNationalId().trim().equals(emptyString)){
				throw new CommonException("New National Id cannot be null");
			}
			String subsNumber = profileInformationUpdateVO.getMsisdn();
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(subsNumber!=null&&subsNumber.length()==subscriberSize)
				profileInformationUpdateVO.setMsisdn(subscriberCountryCode+subsNumber);
		
		}catch(Exception e){
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateVO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateVO.getMsisdn()+"] Exception occured ",e);
			profileInformationUpdateVO.setStatusCode("SC0001");
			profileInformationUpdateVO.setStatusDesc(e.getMessage());
			throw new CommonException(e.getMessage());
		}
		finally{
			
		}
		return genericDTO;
	}

}
