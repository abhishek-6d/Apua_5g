package com.sixdee.imp.validator.format;
/**
 * 
 * @author Rahul
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
 * <td>March 06,2015 11:46:47 AM</td>
 * <td>Rahul</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.dto.GetOfferCodeOnMarketingPlanDTO;

public class GetOfferCodeOnMarketingPlanFrmtValidator extends FrmtValidatorCommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private static final Logger logger = Logger.getLogger(GetOfferCodeOnMarketingPlanFrmtValidator.class);
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		String loggingMessage = null;
		GetOfferCodeOnMarketingPlanDTO getOfferCodeOnMarketingPlanDTO = null;
		String emptyString = "";
		try{
			getOfferCodeOnMarketingPlanDTO = (GetOfferCodeOnMarketingPlanDTO) genericDTO.getObj();
			loggingMessage = getOfferCodeOnMarketingPlanDTO.getLoggingMessage();
			logger.info(loggingMessage+" Request in format validation layer");
			if(getOfferCodeOnMarketingPlanDTO.getTransactionId()==null||getOfferCodeOnMarketingPlanDTO.getTransactionId().trim().equals(emptyString)){
				throw new CommonException("No Transaction id for this request");
			}if(getOfferCodeOnMarketingPlanDTO.getMarketingPlanId()==null||getOfferCodeOnMarketingPlanDTO.getMarketingPlanId().trim().equals(emptyString)){
				if(getOfferCodeOnMarketingPlanDTO.getMarketingPlanName()==null||getOfferCodeOnMarketingPlanDTO.getMarketingPlanName().trim().equals(emptyString)){
					throw new CommonException("No Marketing PlanId/Name for this request");
				}
			}if(getOfferCodeOnMarketingPlanDTO.getUserName()==null||getOfferCodeOnMarketingPlanDTO.getUserName().trim().equals(emptyString)){
				throw new CommonException("No username for this request");
			}if(getOfferCodeOnMarketingPlanDTO.getPassword()==null||getOfferCodeOnMarketingPlanDTO.getPassword().trim().equals(emptyString)){
				throw new CommonException("No userpassword for this request");
			}
		}catch(CommonException ce){
			logger.error(loggingMessage+" Format validation failed ",ce);
			getOfferCodeOnMarketingPlanDTO.setStatusCode("SC0002");
			getOfferCodeOnMarketingPlanDTO.setStatusDesc("Mandatory Param Missing : "+ce.getMessage());
			genericDTO.setObj(getOfferCodeOnMarketingPlanDTO);
		}
		return genericDTO;
	}

}
