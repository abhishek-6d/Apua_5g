package com.sixdee.imp.dao;

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

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.MerchantDiscountDTO;
import com.sixdee.imp.dto.MerchantTierMappingDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class MerchantDiscountDAO {

	private static Logger logger = Logger.getLogger(MerchantDiscountDAO.class);

	TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
	CommonUtil commonUtil = new CommonUtil();
	LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;

	public void getSubsciberInfo(MerchantDiscountDTO merchantDiscountDTO) throws CommonException {

		try {

			long start = System.currentTimeMillis();
			SubscriberNumberTabDTO subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(merchantDiscountDTO.getSubscriberNumber()));
			
			if (subscriberNumberTabDTO != null) {

				logger.info(" get subscriber: --------------------------------->" + (System.currentTimeMillis() - start) +"      "+subscriberNumberTabDTO.getAccountTypeName()+"    "+subscriberNumberTabDTO.getAccountTypeId());
				
				if(Cache.accountTypeMap.get(subscriberNumberTabDTO.getAccountTypeId()+"_"+merchantDiscountDTO.getLanguageID())!=null){
					
					merchantDiscountDTO.setSubscriberType(Cache.accountTypeMap.get(subscriberNumberTabDTO.getAccountTypeId()+"_"+merchantDiscountDTO.getLanguageID()));
				}
				
				if (subscriberNumberTabDTO.getStatusID() != 1) {
					throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantDiscountDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());
				}
				merchantDiscountDTO.setLoyaltyID(subscriberNumberTabDTO.getLoyaltyID());
				merchantDiscountDTO.setSubscriber(true);
			}

			if (merchantDiscountDTO.getLoyaltyID() == null) {
				AccountNumberTabDTO accountNumberTabDTO = tableDetailsDAO.getAccountNumberDetails(merchantDiscountDTO.getSubscriberNumber());
				if (accountNumberTabDTO != null) {

					if (accountNumberTabDTO.getStatusID() != 1)
						throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantDiscountDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());

					merchantDiscountDTO.setLoyaltyID(accountNumberTabDTO.getLoyaltyID());

					merchantDiscountDTO.setAccount(true);


				}
			}
			
			if(merchantDiscountDTO.getLoyaltyID() != null){
				
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile((merchantDiscountDTO.getLoyaltyID()));
				
			}

			if (loyaltyProfileTabDTO==null) {
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(Long.parseLong(merchantDiscountDTO.getSubscriberNumber()));
			}
			
			if (loyaltyProfileTabDTO == null) {

				logger.info(merchantDiscountDTO.getTransactionId() + ": Loyalty ID not exist for Subsriber Number :" + merchantDiscountDTO.getSubscriberNumber());
				throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + merchantDiscountDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INVALID_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());

			}
			
			if (loyaltyProfileTabDTO.getStatusID() != 1)
				throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantDiscountDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_SUB_INACTIVE_" + merchantDiscountDTO.getLanguageID()).getStatusDesc());
			
			merchantDiscountDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
			merchantDiscountDTO.setTierId(loyaltyProfileTabDTO.getTierId().toString());
			merchantDiscountDTO.setTierName(Cache.tierLanguageInfoMap.get(merchantDiscountDTO.getTierId()+"_"+merchantDiscountDTO.getLanguageID()));

		}catch (CommonException e) {
			logger.info(merchantDiscountDTO.getTransactionId() + ": Service Failed :" + merchantDiscountDTO.getSubscriberNumber());
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info(merchantDiscountDTO.getTransactionId() + ": Service Failed :" + merchantDiscountDTO.getSubscriberNumber());
			throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_"+merchantDiscountDTO.getLanguageID()).getStatusCode(), Cache.getServiceStatusMap().get("GET_MERCHANT_FAIL_"+merchantDiscountDTO.getLanguageID()).getStatusDesc());
		}

	}// getSubsciberInfo

	public boolean getMerchantDiscountInfo(MerchantDiscountDTO merchantDiscountDTO) throws CommonException {

		Session session = null;
		String hql = null;
		Query query = null;
		boolean process=false;

		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			
			logger.info(" The merchnat Id is  "+merchantDiscountDTO.getMerchantId()+" and the TIER id is ---- "+merchantDiscountDTO.getTierId());
			

			hql = " from MerchantTierMappingDTO where merchantId=:mId and tierId=:tId ";

			query = session.createQuery(hql);
			query.setParameter("mId", merchantDiscountDTO.getMerchantId());
			query.setParameter("tId", Integer.parseInt(merchantDiscountDTO.getTierId()));
			
			MerchantTierMappingDTO dto = (MerchantTierMappingDTO) query.uniqueResult();

			if (dto != null) {

				if (dto.getDiscount() != null && !dto.getDiscount().trim().equalsIgnoreCase("")) {

					merchantDiscountDTO.setDiscount(dto.getDiscount().trim());
					logger.info(merchantDiscountDTO.getTransactionId() + " : " + "  Discount is  " + merchantDiscountDTO.getDiscount());
					process=true;
				}
			}

		}catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

				if (query != null) {
					query = null;
				}

				if (session != null) {
					session.close();
					session = null;
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
return process;
	}// getMerchantDiscountInfo

}//
