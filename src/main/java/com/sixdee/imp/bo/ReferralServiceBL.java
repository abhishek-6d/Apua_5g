package com.sixdee.imp.bo;

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
 * <td>May 21,2015 07:10:21 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.ReferralServiceDAO;
import com.sixdee.imp.dto.ReferralServiceDTO;

public class ReferralServiceBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => ReferralServiceBL :: Method => buildProcess()");
		
		ReferralServiceDAO  refServDAO;
		ReferralServiceDTO referralServiceDTO = (ReferralServiceDTO) genericDTO.getObj();
		CommonUtil commonUtil = new CommonUtil();
		
		try{
			refServDAO=new ReferralServiceDAO();	
			if(refServDAO.checkProfileDetails(referralServiceDTO))
			{
				if(refServDAO.insertInstantDetails(referralServiceDTO))
				{
					genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "REFERRAL_SUCCESS_"+referralServiceDTO.getLanguageId(), referralServiceDTO.getTransactionId());
					referralServiceDTO.setStatusCode(genericDTO.getStatusCode());
					referralServiceDTO.setStatusDesc(genericDTO.getStatus());
					logger.info(referralServiceDTO.getStatusDesc());
				}
				else
				{
					genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "REFERRAL_FAILURE_"+referralServiceDTO.getLanguageId(), referralServiceDTO.getTransactionId());
					referralServiceDTO.setStatusCode(genericDTO.getStatusCode());
					referralServiceDTO.setStatusDesc(genericDTO.getStatus());
					logger.info(referralServiceDTO.getStatusDesc());
				}
			}
			else
			{
				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "REFERRAL_FAILURE_"+referralServiceDTO.getLanguageId(), referralServiceDTO.getTransactionId());
				referralServiceDTO.setStatusCode(genericDTO.getStatusCode());
				referralServiceDTO.setStatusDesc(genericDTO.getStatus());
				logger.info(referralServiceDTO.getStatusDesc());
			}
		}
		catch (Exception e) {
			logger.error("Exception Occured:"+e.getMessage());
		}
		
		finally
		{
			
		//	logger.fatal(referralServiceDTO.toString()+"|"+referralServiceDTO.getStatusCode()+"|"+referralServiceDTO.getStatusDesc());
			
		}
		
			return genericDTO;
	}
}
