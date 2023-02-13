package com.sixdee.imp.bo;

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
import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.UpdateVoucherDetailsDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.UpdateVoucherDetailsDTO;

public class UpdateVoucherDetailsBL extends BOCommon {

	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO)
			throws CommonException {

		logger
				.info("Class => UpdateVoucherDetailsBL :: Method => buildProcess()");

		UpdateVoucherDetailsDAO updateVoucherDetailsDAO = new UpdateVoucherDetailsDAO();
		TableDetailsDAO tabDAO=null;
		LoyaltyProfileTabDTO loyaltyProfileDTO=null;
        CommonUtil commonUtil = new CommonUtil();
		try {
			// Find the Loyalty Id
			tabDAO=new TableDetailsDAO();
			Long LoyaltyID = updateVoucherDetailsDAO.GetLoyaltyId(genericDTO);
			loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(LoyaltyID); 
			if(loyaltyProfileDTO!=null && loyaltyProfileDTO.getDefaultLanguage()==null)
			{
				 loyaltyProfileDTO.setDefaultLanguage(Cache.defaultLanguageID);	
			}	
			logger.info("--- LoyaltyID ---"+LoyaltyID);
			if (LoyaltyID != null) {
				if(updateVoucherDetailsDAO.UpdateVoucherDetails(
						genericDTO, LoyaltyID))
				{
					logger.info("-- SUCCESS --");
					/*genericDTO.setStatus("SUCCESS");
					genericDTO.setStatusCode("SC0000");*/
					if(loyaltyProfileDTO!=null)
					{
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_ORDER_STATUS_SUCCESS_"+loyaltyProfileDTO.getDefaultLanguage(), genericDTO.getTransactionId());
						/*genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_SUCCESS_"+loyaltyProfileDTO.getDefaultLanguage()).getStatusDesc());
						genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_SUCCESS_"+loyaltyProfileDTO.getDefaultLanguage()).getStatusCode());*/
					}
					else
					{
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_ORDER_STATUS_SUCCESS_"+loyaltyProfileDTO.getDefaultLanguage(), genericDTO.getTransactionId());
						/*genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_SUCCESS_"+Cache.defaultLanguageID).getStatusDesc());
						genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_SUCCESS_"+Cache.defaultLanguageID).getStatusCode());
					*/
					}
				}
					
				else
				{	
					logger.info("-- FAILURE --");
					/*genericDTO.setStatus("FAILURE");
					genericDTO.setStatusCode("SC0001");*/
					if(loyaltyProfileDTO!=null)
					{
						genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_FAILURE_"+loyaltyProfileDTO.getDefaultLanguage()).getStatusDesc());
						genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_FAILURE_"+loyaltyProfileDTO.getDefaultLanguage()).getStatusCode());
					}
					else
					{
						genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_FAILURE_"+Cache.defaultLanguageID).getStatusDesc());
						genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_STATUS_FAILURE_"+Cache.defaultLanguageID).getStatusCode());
					}
				}
			}
			
		}
		catch (CommonException e) 
		{
			// TODO: handle exception
			e.printStackTrace();
			if(e.getMessage().equalsIgnoreCase("Subscriber is not registered with LMS"))
			genericDTO.setStatus(e.getMessage());
			else
			{
				genericDTO.setStatus("FAILURE");
				genericDTO.setStatusCode("SC0001");
				
			}
				
		}
		catch (Exception e) 
		{	
			e.printStackTrace();
			logger.info("Error", e);
			genericDTO.setStatus("FAILURE");
			genericDTO.setStatusCode("SC0001");
		}

		return genericDTO;
	}
}
