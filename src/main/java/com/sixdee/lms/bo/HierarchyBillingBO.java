/**
 * 
 */
package com.sixdee.lms.bo;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.Json.RequestRealTimeTrigger;
import com.sixdee.lms.dao.LoyaltyProfileManagerDAO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;

/**
 * @author rahul.kr
 *
 */
public class HierarchyBillingBO implements BusinessLogics {

	private static final Logger logger = Logger.getLogger("HierarchyBillingBO");
	/* (non-Javadoc)
	 * @see com.sixdee.lms.serviceInterfaces.BusinessLogics#executeBusinessProcess(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		// TODO Auto-generated method stub
		RequestRealTimeTrigger requestRealTimeTrigger = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		LoyaltyProfileManagerDAO loyaltyProfileManagerDAO = null;
		TableInfoDAO tableInfoDAO = null;
		int flag = 0;
		try{
			requestRealTimeTrigger = (RequestRealTimeTrigger) genericDTO.getObj();
			tableInfoDAO = new TableInfoDAO();
			loyaltyProfileTabDTO = tableInfoDAO.getLoyaltyProfileDetails(genericDTO.getRequestId(), requestRealTimeTrigger.getCustomerRefernceNumber(), 3);
			loyaltyProfileManagerDAO = new LoyaltyProfileManagerDAO();
			if (loyaltyProfileTabDTO != null) {
				if (requestRealTimeTrigger.getKeyword().equals(KeywordConstants.EIF_ACT_HIERARCHY_BILLING)) {
					// loyaltyProfileTabDTO.setIsHierarchyBillingActivated(1);
					loyaltyProfileManagerDAO.updateLoyaltyProfileForHierarchyBilling(genericDTO.getRequestId(),
							loyaltyProfileTabDTO.getLoyaltyID(), 1);

				} else if (requestRealTimeTrigger.getKeyword().equals(KeywordConstants.EIF_DACT_HIERARCHY_BILLING)) {
					loyaltyProfileManagerDAO.updateLoyaltyProfileForHierarchyBilling(genericDTO.getRequestId(),
							loyaltyProfileTabDTO.getLoyaltyID(), 0);
				} else if (requestRealTimeTrigger.getKeyword().equals(KeywordConstants.EIF_ADD_BLACKLIST)) {
					loyaltyProfileManagerDAO.updateLoyaltyProfileForBlacklisted(genericDTO.getRequestId(),
							loyaltyProfileTabDTO.getLoyaltyID(), 0);
				} else if (requestRealTimeTrigger.getKeyword().equals(KeywordConstants.EIF_ADD_BLACKLIST)) {
					loyaltyProfileManagerDAO.updateLoyaltyProfileForBlacklisted(genericDTO.getRequestId(),
							loyaltyProfileTabDTO.getLoyaltyID(), 0);
				} else if (requestRealTimeTrigger.getKeyword().equals(KeywordConstants.barUnbarService)){
					/*
					 * 
					 */
				} else if (requestRealTimeTrigger.getKeyword().equals(KeywordConstants.terminateAccount)){
					/*
					 *Delete all account details . Ask for confirmation			
					 */
				}
			}
		}catch (Exception e) {
			logger.error("RequestID : Exception occured ",e);
		}
		return genericDTO;
		
	}

}
