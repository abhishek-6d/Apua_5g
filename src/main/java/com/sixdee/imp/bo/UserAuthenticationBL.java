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
 * <td>April 24,2013 05:54:40 PM</td>
 * <td>Nithin Kunjappan</td>
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
import com.sixdee.imp.dao.UserAuthenticationDAO;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.UserAuthenticationDTO;

public class UserAuthenticationBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => UserAuthenticationBL :: Method => buildProcess()");
		
		UserAuthenticationDAO  userAuthenticationDAO=null;
		UserAuthenticationDTO userAuthenticationDTO = (UserAuthenticationDTO) genericDTO.getObj();
		TableDetailsDAO tabDAO=null;
		SubscriberNumberTabDTO subscriberNumDTO=null;
		AccountNumberTabDTO accntNumDTO=null;
		LoyaltyProfileTabDTO loyaltyProfileDTO=null;
		ADSLTabDTO adslDTO=null;
		CommonUtil commonUtil = new CommonUtil();
		try{
		
		if(userAuthenticationDTO.isAuthenticate())
		{
			userAuthenticationDAO=new UserAuthenticationDAO();
			tabDAO=new TableDetailsDAO();
			subscriberNumDTO=new SubscriberNumberTabDTO();
			accntNumDTO=new AccountNumberTabDTO();
			loyaltyProfileDTO=new LoyaltyProfileTabDTO();
			loyaltyProfileDTO.setDefaultLanguage(userAuthenticationDTO.getLanguageID());
			adslDTO=new ADSLTabDTO();
			int result=0;
			
			if(userAuthenticationDTO.getSubscriberNumber()!=null && userAuthenticationDTO.getPin()!=null && !userAuthenticationDTO.getPin().equals("0") )
			{				
				if(userAuthenticationDTO.isADSLNumber())
				{
					adslDTO=tabDAO.getADSLDetails(""+userAuthenticationDTO.getSubscriberNumber(),null);
					if(adslDTO!=null)
					{
						 Long loyaltyID= adslDTO.getLoyaltyID();						
						 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(loyaltyID); 
						 if(loyaltyProfileDTO!=null && userAuthenticationDTO.getLanguageID()==null)
							{
							 loyaltyProfileDTO.setDefaultLanguage(userAuthenticationDTO.getLanguageID());	
							}	
						 if(loyaltyProfileDTO!=null)
						 {							 
							 result=userAuthenticationDAO.checkStatus(loyaltyProfileDTO.getPin(),loyaltyProfileDTO.getStatusID(),userAuthenticationDTO.getPin());
							 if(result==0)
							 {	 
								 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID(), userAuthenticationDTO.getTransactionId());
								 userAuthenticationDTO.setStatusCode(genericDTO.getStatusCode());
								 userAuthenticationDTO.setStatusDesc(genericDTO.getStatus());
								 logger.info("User Authentication is Successfull...");
							 }
							 else if(result==1)
							 {	
								 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID(), userAuthenticationDTO.getTransactionId());
								 userAuthenticationDTO.setStatusCode(genericDTO.getStatusCode());
								 userAuthenticationDTO.setStatusDesc(genericDTO.getStatus());
								 logger.info("Invalid Loyalty ID or PIN");
							 }
							 else if(result==2)
							 {	
								 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID(), userAuthenticationDTO.getTransactionId());
								 userAuthenticationDTO.setStatusCode(genericDTO.getStatusCode());
								 userAuthenticationDTO.setStatusDesc(genericDTO.getStatus());
								 logger.info("Invalid");
							 }
							 else if(result==3)
							 {	
								 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID(), userAuthenticationDTO.getTransactionId());
								 userAuthenticationDTO.setStatusCode(genericDTO.getStatusCode());
								 userAuthenticationDTO.setStatusDesc(genericDTO.getStatus());
								 logger.info("Inactive");
							 }							
						 }
					}
					else
						{
						 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
						 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
						 logger.info("Invalid Credentials");	
						
						}
				}
				else
				{
					
			   		logger.info("SUBSRIBER NUMBER"+userAuthenticationDTO.getSubscriberNumber());
			   	//	logger.info("PIN"+userAuthenticationDTO.getPin());
			   	//	logger.debug("Language id "+userAuthenticationDTO.getLanguageID());
					 Long subNum=Long.parseLong(userAuthenticationDTO.getSubscriberNumber());
					 subscriberNumDTO=tabDAO.getSubscriberNumberDetails(subNum,null);
					 if(subscriberNumDTO!=null)
					 {
						 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(subscriberNumDTO.getLoyaltyID()); 
						 if(loyaltyProfileDTO!=null && userAuthenticationDTO.getLanguageID()==null)
							{
							 loyaltyProfileDTO.setDefaultLanguage(userAuthenticationDTO.getLanguageID());	
							}	
						 if(loyaltyProfileDTO!=null)
						 {
							 
							 result=userAuthenticationDAO.checkStatus(loyaltyProfileDTO.getPin(),loyaltyProfileDTO.getStatusID(),userAuthenticationDTO.getPin());
							 if(result==0)
							 {	
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("User Authentication is Successfull...");
							 }
							 else if(result==1)
							 {	 
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("Invalid Loyalty ID or PIN");
							 }
							 
							 else if(result==2)
							 {	
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("Invalid");
							 }
							 else if(result==3)
							 {	
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("Inactive");
							 }			
						 }
						 else
							{ 
							 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("Invalid Credentials");	
							 }	
					 }
					 else
					 {
	    			 accntNumDTO=tabDAO.getAccountNumberDetails(subNum+"",null,null);
	    			 if(accntNumDTO!=null)
	    			 {
	    				 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(accntNumDTO.getLoyaltyID()); 
	    				 
						 if(loyaltyProfileDTO!=null && userAuthenticationDTO.getLanguageID()==null)
							{
							 loyaltyProfileDTO.setDefaultLanguage(userAuthenticationDTO.getLanguageID());	
							}	
	    				 if(loyaltyProfileDTO!=null)
						 {
	    				 result=userAuthenticationDAO.checkStatus(loyaltyProfileDTO.getPin(),loyaltyProfileDTO.getStatusID(),userAuthenticationDTO.getPin());					
						 if(result==0)
						 {	 
							 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("User Authentication is Successfull...");
						 }
						 else if(result==1)
						 {	 
							 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("Invalid Loyalty ID or PIN");
						 }
						 else if(result==2)
						 {	
							 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("Invalid");
						 }
						 else if(result==3)
						 {	
							 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("Inactive");
						 }			
						 }
	    				 else
	    				 { 
	    					 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("Invalid Credentials");	
	    				 }
	    			 }
	    			 else
	    			 {
	    				 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(subNum);
	    				 if(loyaltyProfileDTO!=null && userAuthenticationDTO.getLanguageID()==null)
							{
							 loyaltyProfileDTO.setDefaultLanguage(userAuthenticationDTO.getLanguageID());	
							}	
	    				 if(loyaltyProfileDTO!=null)
	    				 {
	    					 result=userAuthenticationDAO.checkStatus(loyaltyProfileDTO.getPin(),loyaltyProfileDTO.getStatusID(),userAuthenticationDTO.getPin());
							 if(result==0)
							 {	 
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_SUCCESS_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("User Authentication is Successfull...");
							 }
							 else if(result==1)
							 {	 
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("Invalid Loyalty ID or PIN");
							 }
							 else if(result==2)
							 {	
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("Invalid");
							 }
							 else if(result==3)
							 {	
								 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
								 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INACTIVE_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
								 logger.info("Inactive");
							 }			
	    				 } 
	    				 else		
	    				 {  
	    					 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
							 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_CRED_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
							 logger.info("Invalid Credentials");	
	    				 }
	    			 }
				   }
				}			
			}	
			else
				 {
				 if(userAuthenticationDTO.getSubscriberNumber()==null && userAuthenticationDTO.getPin().equals("0"))
				 { 
					userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_SUB_PIN_REQ_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
					userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_SUB_PIN_REQ_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());
					logger.info("Credentials cannot be NULL");
				 }
				 else if(userAuthenticationDTO.getSubscriberNumber()==null)
				 {
					 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
					 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("SUB_REQ_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());	
					 logger.info("Subscriber Number  cannot be NULL");
				 }
				 else if(userAuthenticationDTO.getPin().equals("0"))
				 {
					 userAuthenticationDTO.setStatusCode(Cache.getServiceStatusMap().get("PIN_REQ_"+userAuthenticationDTO.getLanguageID()).getStatusCode());
					 userAuthenticationDTO.setStatusDesc(Cache.getServiceStatusMap().get("PIN_REQ_"+userAuthenticationDTO.getLanguageID()).getStatusDesc());		
					 logger.info("Pin cannot be NULL");
					 
				 }
						
				}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
			return genericDTO;
		}
}
