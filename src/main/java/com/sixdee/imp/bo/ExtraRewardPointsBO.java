package com.sixdee.imp.bo;

import org.apache.log4j.Logger;

import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.PromoTableDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class ExtraRewardPointsBO {
	private static final Logger log = Logger.getLogger(ExtraRewardPointsBO.class);
    long  loyaltyId=0;
    AccountNumberTabDTO accnumtab = null; 
	TableDetailsDAO tabdetails= new TableDetailsDAO();
	PromoTableDTO promtab= null;
	
	public void assignExtraReward(String subscriberNumber){
		log.info("******assignExtraReward()******");
		SubscriberNumberTabDTO subscriberNumberTabDTO = new SubscriberNumberTabDTO();
		subscriberNumberTabDTO = tabdetails.getSubscriberNumberDetails(Long.parseLong(subscriberNumber));
		loyaltyId = subscriberNumberTabDTO.getLoyaltyID();
		if(loyaltyId==0){
		AccountNumberTabDTO accnumtab= new AccountNumberTabDTO();
		accnumtab = tabdetails.getAccountNumberDetails(subscriberNumber);
		loyaltyId =  accnumtab.getLoyaltyID();
		}
		if(loyaltyId==0){
			
		}
		promtab = tabdetails.getPromoDetails(Long.parseLong(subscriberNumber));
		if(promtab==null){
			
			tabdetails.updatePromoDetails(Long.parseLong(subscriberNumber), loyaltyId);
		}
		
	}
}