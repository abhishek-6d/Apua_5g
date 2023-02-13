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
 * <td>June 29,2015 12:25:32 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.GetMerchantsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.GetMerchantsDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.service.serviceDTO.resp.PackageDetailsDTO;

public class GetMerchantsBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => GetMerchantsBL :: Method => buildProcess()");
		
		GetMerchantsDAO  getMerchantsDAO;
		GetMerchantsDTO getMerchantsDTO = (GetMerchantsDTO) genericDTO.getObj();
		TableDetailsDAO tableDetailsDAO = null;
		ADSLTabDTO adslDTO = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null; 
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		AccountNumberTabDTO accntNumDTO=null;
		Long loyaltyId= null;
		boolean check=false;
		String txnId= null;
		String subscriberNumber = null;
		CommonUtil commonUtil = new CommonUtil();
		
		try{
			txnId = getMerchantsDTO.getTransactionId();
			subscriberNumber = getMerchantsDTO.getSubscriberNumber();
			tableDetailsDAO = new TableDetailsDAO();
			
			if(getMerchantsDTO.getMerchantId()!=0)
			{
			
			if(getMerchantsDTO.getSubscriberNumber()==null)
			{
				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SUB_REQ_"+getMerchantsDTO.getLanguageId(), getMerchantsDTO.getTransactionId());
				/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+getMerchantsDTO.getLanguageId()).getStatusCode());
				genericDTO.setStatus(Cache.getServiceStatusMap().get("SUB_REQ_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
				*/
				throw new CommonException(genericDTO.getStatus());
			}
			
			
			/*if(getMerchantsDTO.isAdsl())
			{
				//check whether particular number exist or not
				adslDTO = tableDetailsDAO.getADSLDetails(getMerchantsDTO.getSubscriberNumber());
				
				if(adslDTO == null)
				{
					genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusCode());
					genericDTO.setStatus(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
					throw new CommonException(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
				}
				else
					loyaltyId = adslDTO.getLoyaltyID();
			}*/
			
			if(adslDTO == null)
			{
				//check whether particular number exist or not
				 subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(getMerchantsDTO.getSubscriberNumber()));
				 
    			 if(subscriberNumberTabDTO!=null)
    			 {	
    				 loyaltyId = subscriberNumberTabDTO.getLoyaltyID();
    				 logger.debug("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" >>>>>>>>>>>>>>>>loyaltyId got from subscriberNumberTabDTO >>>>>"+loyaltyId);
    			 }
    			 else
    			 {
    				 accntNumDTO=tableDetailsDAO.getAccountNumberDetails(getMerchantsDTO.getSubscriberNumber());
    			 
    				 if(accntNumDTO !=null)
    				 {  
    					 loyaltyId = accntNumDTO.getLoyaltyID();
    					 logger.debug("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" >>>>>>>>>>>>>>>>loyaltyId got from accntNumDTO >>>>>"+loyaltyId);
    				 }
    				 else
    				 {
    				 
    					 loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfile(Long.parseLong(getMerchantsDTO.getSubscriberNumber()));
    					 if(loyaltyProfileTabDTO!=null)	
    					 {  
    						 check=true;
    						 loyaltyId = loyaltyProfileTabDTO.getLoyaltyID();
    						 logger.debug("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" >>>>>>>>>>>>>>>>loyaltyId got from loyaltyProfileTabDTO >>>>>"+loyaltyId);
        			 
    					 } 
    					 else
    					 {
    						 genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusCode());
    						 genericDTO.setStatus(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
    						 throw new CommonException(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
    					 }
    				 }
    			 }
    		 }
			
			if(loyaltyId!=null)
			{
				if(!check)
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);
				logger.debug("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" Loyalty ID checking == "+loyaltyProfileTabDTO);
			}
			else
			{
				logger.info("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" LoyaltyId cannot be obtained ");
				 genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusCode());
				 genericDTO.setStatus(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
				 throw new CommonException(Cache.getServiceStatusMap().get("GETPKG_SUB_NOT_REG_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
			}
			}
			
			getMerchantsDAO=new GetMerchantsDAO();
			
			HashMap<Integer, PackageCategory> categoryMap = getMerchantsDAO.getMerchantMap(genericDTO);
			logger.debug("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" MAP ==> "+categoryMap);
			getMerchantsDTO.setMerchantMap(categoryMap);
			logger.info("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" SIZE OF MAP:"+getMerchantsDTO.getMerchantMap().size());
			
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_PACK_SUCCESS_"+getMerchantsDTO.getLanguageId()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_PACK_SUCCESS_"+getMerchantsDTO.getLanguageId()).getStatusDesc());
		}
		catch (Exception e) {
			logger.error("Service : GetMerchants -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" Exception occured ",e);
			e.printStackTrace();
		}
					
			return genericDTO;
		}
}
