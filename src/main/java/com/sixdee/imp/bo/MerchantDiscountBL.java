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
 * <td>April 15,2015 06:58:41 PM</td>
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
import com.sixdee.imp.dao.MerchantDiscountDAO;
import com.sixdee.imp.dto.MerchantDiscountDTO;

public class MerchantDiscountBL extends BOCommon {

	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => MerchantDiscountBL :: Method => buildProcess()");

		MerchantDiscountDAO merchantDiscountDAO = new MerchantDiscountDAO();
		MerchantDiscountDTO merchantDiscountDTO = (MerchantDiscountDTO) genericDTO.getObj();
		CommonUtil commonUtil = new CommonUtil();
		boolean process=false;
		try {

			// For getting subscriber Info
			merchantDiscountDAO.getSubsciberInfo(merchantDiscountDTO);

			process=merchantDiscountDAO.getMerchantDiscountInfo(merchantDiscountDTO);
       if(process){
    	    genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_MERCHANT_SUCCESS_" + merchantDiscountDTO.getLanguageID(), merchantDiscountDTO.getTransactionId());
			/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_" + merchantDiscountDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_SUCCESS_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());*/
           }else
               {
        	   genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_MERCHANT_DETAILS_NOTFOUND_" + merchantDiscountDTO.getLanguageID(), merchantDiscountDTO.getTransactionId());
        	   /*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_DETAILS_NOTFOUND_" + merchantDiscountDTO.getLanguageID()).getStatusCode());
        	   genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_DETAILS_NOTFOUND_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());
*/
               }
		} catch (CommonException ce) {
			
			ce.printStackTrace();

			genericDTO.setStatusCode(ce.getStatusCode());
			genericDTO.setStatus(ce.getLocalizedMessage());

		} catch (Exception e) {
            genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_MERCHANT_FAIL_" + merchantDiscountDTO.getLanguageID(), merchantDiscountDTO.getTransactionId());
			/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantDiscountDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());
*/
			e.printStackTrace();
		}

		return genericDTO;
	}
}
