package com.sixdee.imp.bo;

/**
 * 
 * @author Athul Gopal
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
 * <td>April 21,2015 11:42:08 AM</td>
 * <td>Athul Gopal</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.MerchantRedeemDAO;
import com.sixdee.imp.dto.MerchantRedeemDTO;

public class MerchantRedeemBL extends BOCommon {

	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => MerchantRedeemBL :: Method => buildProcess()");

		MerchantRedeemDAO merchantRedeemDAO = new MerchantRedeemDAO();
		MerchantRedeemDTO merchantRedeemDTO = (MerchantRedeemDTO) genericDTO.getObj();
        CommonUtil commonUtil = new CommonUtil();
		try {

			// For getting subscriber Info
			merchantRedeemDAO.getSubsciberInfo(merchantRedeemDTO);
			merchantRedeemDAO.merchantRedeem(genericDTO);
			genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_MERCHANT_SUCCESS_REDEEM_" + merchantRedeemDTO.getLanguageID(), merchantRedeemDTO.getTransactionId());
			/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_REDEEM_" + merchantRedeemDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_REDEEM_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());
*/
		} catch (CommonException ce) {
			
			genericDTO.setStatusCode(ce.getStatusCode());
			genericDTO.setStatus(ce.getLocalizedMessage());

		} catch (Exception e) {
			
			genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID(), merchantRedeemDTO.getTransactionId());
			/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantRedeemDTO.getLanguageID()).getStatusDesc());
			*/
		}finally{
			
			//For writing CDR
			merchantRedeemDAO.writeCDR(genericDTO);
			
			merchantRedeemDAO=null;
			merchantRedeemDTO=null;
			
		}

		return genericDTO;
	}//buildProcess
}//
