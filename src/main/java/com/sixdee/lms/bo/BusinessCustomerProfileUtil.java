package com.sixdee.lms.bo;

import org.apache.log4j.Logger;

import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.lms.util.constant.SystemConstants;

public class BusinessCustomerProfileUtil {

	private final static Logger logger = Logger
			.getLogger(BusinessCustomerProfileUtil.class);

	public String checkTelecomAdmin(String requestId, String msisdn) {
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		String result = null;
		try {
			subscriberNumberTabDTO = tableDetailsDAO
					.getSubscriberNumberDetails(Long.parseLong(msisdn));
			if (subscriberNumberTabDTO != null)
				loyaltyProfileTabDTO = tableDetailsDAO
						.getLoyaltyProfile(subscriberNumberTabDTO
								.getLoyaltyID());
			if (loyaltyProfileTabDTO != null) {
				if (loyaltyProfileTabDTO.getIsBusiness() == 1) {
					customerProfileTabDTO = tableDetailsDAO
							.getCustomerProfile(msisdn);
					if (customerProfileTabDTO != null) {
						if (customerProfileTabDTO.getAccountCategoryType()
								.equalsIgnoreCase("2")) {
							if (customerProfileTabDTO.getTelecoAdmin()
									.equalsIgnoreCase(msisdn))
								result = SystemConstants.TELCO_ADMIN;
							else
								result = SystemConstants.NOT_A_TELCO_ADMIN;
						} else {
							logger.info(">>>>>business prepaid customer");
							result = SystemConstants.NOT_BUSINESS_CUSTOMER;
						}
					}
				} else {
					result = SystemConstants.NOT_BUSINESS_CUSTOMER;
				}
			} else
				result = SystemConstants.NO_ACCOUNT_FOUND;
		} catch (Exception e) {

		}
		return result;

	}

	public String checkIfCustomerRegisteredUnderTelecomAdmin(String requestId, String loyaltyid, String redemptionNumber){
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		AccountNumberTabDTO accountNumberTabDTO = null;
		String result = null;
		try{
			subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(redemptionNumber));
			if(subscriberNumberTabDTO!=null){
				if(loyaltyid.equalsIgnoreCase(subscriberNumberTabDTO.getLoyaltyID()+"")){
					result = SystemConstants.RED_NUM_UNDER_SAME_TELCO_ADMIN;
				}else{
					result = SystemConstants.NOT_SAME_ACCOUNT;
				}
			}else{
				accountNumberTabDTO = tableDetailsDAO.getAccountNumberDetails(redemptionNumber);
				if(accountNumberTabDTO!=null)
				{
					if(loyaltyid.equalsIgnoreCase(accountNumberTabDTO.getLoyaltyID()+"")){
						result = SystemConstants.RED_NUM_UNDER_SAME_TELCO_ADMIN;
						}else{
							result = SystemConstants.NOT_SAME_ACCOUNT;
						}
				}else{
					result = SystemConstants.NO_ACCOUNT_FOUND;
				}
			}
		}catch(Exception e){
			logger.info("error"+e);
			e.printStackTrace();
		}
		return result;
		
	}
	
	public String checkMoNumberComesUnderTA(String requestId,String subscriberNumber ,String moNumber){
		CustomerProfileTabDTO customerProfileTabDTO = null;
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		String result = null;
		try {
			subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(moNumber));
			if (subscriberNumberTabDTO != null) {
				customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(moNumber);
				if (customerProfileTabDTO != null) {
					if (customerProfileTabDTO.getTelecoAdmin().equalsIgnoreCase(moNumber))
						result = SystemConstants.TELCO_ADMIN;
					else
						result = SystemConstants.NOT_A_TELCO_ADMIN;
				} else {
					result = SystemConstants.NO_ACCOUNT_FOUND;
				}

			} else
				result = SystemConstants.NO_ACCOUNT_FOUND;
		} catch (Exception e) {

		}
		return result;
		
	}

}
