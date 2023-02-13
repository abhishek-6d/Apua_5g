package com.sixdee.imp.bo;

/**
 * 
 * @author Himanshu
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
 * <td>October 07,2015 12:24:00 PM</td>
 * <td>Himanshu</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.DeleteAccountDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TerminationServiceDAO;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TerminationServiceDTO;

public class TerminationServiceBL extends BOCommon {

	/**
	 * This method is called from BOCommon. Access the DAO object is to create
	 * CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => TerminationServiceBL :: Method => buildProcess()");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		TerminationServiceDAO terminationServiceDAO;
		TerminationServiceDTO terminationServiceDTO = (TerminationServiceDTO) genericDTO.getObj();
		DeleteAccountDAO deleteAccntDAO = null;
		TableDetailsDAO tabDAO = null;
		SubscriberNumberTabDTO subscriberNumDTO = null;
		AccountNumberTabDTO accntNumDTO = null;
		LoyaltyProfileTabDTO loyaltyProfileDTO = null;
		LoyaltyProfileTabDTO statusMessage = null;
		ADSLTabDTO adslDTO = null;
		boolean process = true;
		List<LoyaltyRegisteredNumberTabDTO> numberList = null;
		String logFile = "";

		terminationServiceDAO = new TerminationServiceDAO();
		process=terminationServiceDAO.updateProfileDetails(terminationServiceDTO);

		tabDAO = new TableDetailsDAO();
		subscriberNumDTO = new SubscriberNumberTabDTO();
		accntNumDTO = new AccountNumberTabDTO();
		loyaltyProfileDTO = new LoyaltyProfileTabDTO();
		deleteAccntDAO=new DeleteAccountDAO(terminationServiceDTO.getTransactionId());
		adslDTO = new ADSLTabDTO();
		boolean done = false;
		statusMessage = new LoyaltyProfileTabDTO();
		CommonUtil commonUtil = new CommonUtil();
		if(process){
		if (terminationServiceDTO.getSubscriberNumber() != null && !terminationServiceDTO.getSubscriberNumber().equalsIgnoreCase("")) {
			if (terminationServiceDTO.getAccountType() == 200) {

   			 adslDTO=tabDAO.getADSLDetails(terminationServiceDTO.getSubscriberNumber());
   			 if(adslDTO!=null)
   			 {
   				 int count=deleteAccntDAO.checkLoyaltyRegisteredNumberDetails(adslDTO.getLoyaltyID(),adslDTO.getAccountNumber(),terminationServiceDTO.getSubscriberNumber());
   				 if(count>0)
   					 done=deleteAccntDAO.deleteLoyaltyRegNumberTableDetails(terminationServiceDTO,null,null,adslDTO,count,0);
   				 if(done)
	    			 { 
   					 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
   					terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
   					terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
						 logger.info("Sucessfully deleted");    
	    			 }
   				 else
	    			 { 
   					 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
    					terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
    					terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
						 logger.info("Failure-No data in Table");    	
	    			 }
   			 }	    			 
   			 else
   			 { 
   				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
				terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
				terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
				 logger.info("Failure-No data in Table");  
   			 }
   		 
			} else {
				logger.info("SUBSCRIBER NUMBER::" + terminationServiceDTO.getSubscriberNumber());
				subscriberNumDTO = tabDAO.getSubscriberNumberDetails(Long.parseLong(terminationServiceDTO.getSubscriberNumber()));
				if (subscriberNumDTO != null) {
					terminationServiceDTO.setDeleteKey("SUBSCRIBER_NUMBER");
					logger.info("DELETEKEY IS SUBSCRIBER NUMBER");
					// Need to take all Numbers for this Loyalty
					numberList = deleteAccntDAO.getLinkedNumbers(subscriberNumDTO.getLoyaltyID(), Long.parseLong(terminationServiceDTO.getSubscriberNumber()));
					terminationServiceDTO.setNumberList(numberList);

					if (terminationServiceDTO.getSubscriberNumber().equals(deleteAccntDAO.getPrimaryNumber(subscriberNumDTO.getLoyaltyID())))
						terminationServiceDTO.setPrimary(true);
					logger.info("IS PRIMARY NUMBER:::" + terminationServiceDTO.isPrimary());

					int count = deleteAccntDAO.checkLoyaltyRegisteredNumberDetails(subscriberNumDTO.getLoyaltyID(), subscriberNumDTO.getAccountNumber(), "" + terminationServiceDTO.getSubscriberNumber());
					if (count > 0)
						done = deleteAccntDAO.deleteLoyaltyRegNumberTableDetails(terminationServiceDTO, subscriberNumDTO, null, null, count, 1);
					if (done) {
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
	   					terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
	   					terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
	   					logger.info("Sucessfully deleted");
						logFile = terminationServiceDTO.getTransactionId() + "|" + dateFormat.format(new Date()) + "|" + terminationServiceDTO.getSubscriberNumber() + "|||" + terminationServiceDTO.getChannel() + "||'" + Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_" + Cache.defaultLanguageID).getStatusCode() + "'|'" + Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_" + Cache.defaultLanguageID).getStatusDesc() + "'|6|" + subscriberNumDTO.getLoyaltyID() + "||||";
						logger.warn(logFile);
					} else {
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
						terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
						terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
						logger.info("Failure-No data in Loyalty Table");
						logFile = terminationServiceDTO.getTransactionId() + "|" + dateFormat.format(new Date()) + "|" + terminationServiceDTO.getSubscriberNumber() + "|||" + terminationServiceDTO.getChannel() + "||'" + Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_" + Cache.defaultLanguageID).getStatusCode() + "'|'" + Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_" + Cache.defaultLanguageID).getStatusDesc() + "'|6|" + subscriberNumDTO.getLoyaltyID() + "||||";
						logger.warn(logFile);
					}
				} else {
					accntNumDTO = tabDAO.getAccountNumberDetails(terminationServiceDTO.getSubscriberNumber());
					if (accntNumDTO != null) {
						terminationServiceDTO.setDeleteKey("ACCOUNT_NUMBER");
						logger.info("DELETEKEY IS ACCOUNT NUMBER");
						numberList = deleteAccntDAO.getLinkedNumbers(accntNumDTO.getLoyaltyID(), Long.parseLong(terminationServiceDTO.getSubscriberNumber()));
						terminationServiceDTO.setNumberList(numberList);

						if (terminationServiceDTO.getSubscriberNumber().equals(deleteAccntDAO.getPrimaryNumber(accntNumDTO.getLoyaltyID())))
							terminationServiceDTO.setPrimary(true);
						logger.info("IS PRIMARY NUMBER:::" + terminationServiceDTO.isPrimary());

						int count = deleteAccntDAO.checkLoyaltyRegisteredNumberDetails(accntNumDTO.getLoyaltyID(), accntNumDTO.getAccountNumber(), "" + terminationServiceDTO.getSubscriberNumber());
						if (count > 0)
							done = deleteAccntDAO.deleteLoyaltyRegNumberTableDetails(terminationServiceDTO, null, accntNumDTO, null, count, 2);
						if (done) {
							genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
		   					terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
		   					terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
		   					logger.info("Sucessfully deleted");
							logFile = terminationServiceDTO.getTransactionId() + "|" + dateFormat.format(new Date()) + "|" + terminationServiceDTO.getSubscriberNumber() + "|||" + terminationServiceDTO.getChannel() + "||'" + Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_" + Cache.defaultLanguageID).getStatusCode() + "'|'" + Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_" + Cache.defaultLanguageID).getStatusDesc() + "'|6|" + accntNumDTO.getLoyaltyID() + "||||";
							logger.warn(logFile);

						} 
					} else {
						loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(Long.parseLong(terminationServiceDTO.getSubscriberNumber()));
						if (loyaltyProfileDTO != null) {
							terminationServiceDTO.setDeleteKey("LOYALTY_ID");
							logger.info("DELETEKEY IS LOYALTY ID");
							// Need to take all Numbers for this LoyaltyID

							numberList = deleteAccntDAO.getLineNumbers(loyaltyProfileDTO.getLoyaltyID());

							int count = deleteAccntDAO.checkLoyaltyIDRegisteredNumberDetails(loyaltyProfileDTO.getLoyaltyID(), "" + terminationServiceDTO.getSubscriberNumber());
							if (count > 0)
								done = deleteAccntDAO.deleteLoyaltyIDRegNumberTableDetails(numberList, terminationServiceDTO, loyaltyProfileDTO, count, 3);
							if (done) {
								genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
			   					terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
			   					terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
			   					logger.info("Sucessfully deleted");
								for (LoyaltyRegisteredNumberTabDTO loyalDTO : numberList) {
									logFile = terminationServiceDTO.getTransactionId() + "|" + dateFormat.format(new Date()) + "|" + loyalDTO.getLinkedNumber() + "|||" + terminationServiceDTO.getChannel() + "||'" + Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_" + Cache.defaultLanguageID).getStatusCode() + "'|'" + Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_" + Cache.defaultLanguageID).getStatusDesc() + "'|6|" + loyaltyProfileDTO.getLoyaltyID() + "||||";
									logger.warn(logFile);
								}
							} 
						} else {
							genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DEL_SUB_NT_REG_"+Cache.defaultLanguageID, terminationServiceDTO.getTransactionId());
		   					terminationServiceDTO.setStatusCode(genericDTO.getStatusCode());
		   					terminationServiceDTO.setStatusDesc(genericDTO.getStatus());
							logger.info("Failure-Sub not Registered");
						}
					}
				}
			}
		}
	}
		return genericDTO;
	}
}
