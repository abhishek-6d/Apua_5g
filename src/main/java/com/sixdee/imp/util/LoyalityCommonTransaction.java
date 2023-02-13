package com.sixdee.imp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.lms.util.constant.SystemConstants;

public class LoyalityCommonTransaction {
	private static Logger logger = Logger.getLogger(LoyalityCommonTransaction.class);
	public LoyaltyTransactionTabDTO loyaltyTransactionSetter(LoyaltyTransactionTabDTO loyalityTransaction,
			HashMap<String, String> mapVal) throws ParseException {
		SimpleDateFormat date1=new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date  createDate = null;
		for (Entry<String, String> entry : mapVal.entrySet()) {
			logger.info("LoyaltyTransactionKey : " + entry.getKey() + " LoyaltyTransactionValue : " + entry.getValue());
		}
		for (Entry<String, String> entry : mapVal.entrySet()) {
			
			if(mapVal.get(LoyalityTransactionConstants.accountNumber)!=null){
				loyalityTransaction.setAccountNumber(mapVal.get(LoyalityTransactionConstants.accountNumber));	
			}else if(mapVal.get(SystemConstants.ACCOUNT_NUM)!=null){
				loyalityTransaction.setAccountNumber(mapVal.get(SystemConstants.ACCOUNT_NUM));				
			}
			if (mapVal.get(LoyalityTransactionConstants.channel)!=null)
				loyalityTransaction.setChannel(mapVal.get(LoyalityTransactionConstants.channel));
			
			if (mapVal.get(LoyalityTransactionConstants.createTime)!=null)
			{
				createDate=date1.parse(mapVal.get(LoyalityTransactionConstants.createTime));
				loyalityTransaction.setCreateTime((Date)createDate);
			}
			if (mapVal.get(LoyalityTransactionConstants.curRewardPoints)!=null)
				loyalityTransaction.setCurRewardPoints(Double.valueOf(mapVal.get(LoyalityTransactionConstants.curRewardPoints)));

			if (mapVal.get(LoyalityTransactionConstants.curStatusPoints)!=null)
				loyalityTransaction.setCurStatusPoints(Double.valueOf(mapVal.get(LoyalityTransactionConstants.curStatusPoints)));

			if (mapVal.get(LoyalityTransactionConstants.destLoyaltyId)!=null)
				loyalityTransaction.setDestLoyaltyID(Long.valueOf(mapVal.get(LoyalityTransactionConstants.destLoyaltyId)));
			
			else if (entry.getKey().equals(LoyalityTransactionConstants.id))
				loyalityTransaction.setId(Long.valueOf(mapVal.get(LoyalityTransactionConstants.id)));

			else if (entry.getKey().equals(LoyalityTransactionConstants.loyaltyID))
				loyalityTransaction.setLoyaltyID(Long.valueOf(mapVal.get(LoyalityTransactionConstants.loyaltyID)));
			
			else if (entry.getKey().equals(LoyalityTransactionConstants.preRewardPoints))
				loyalityTransaction.setPreRewardPoints(Double.valueOf(mapVal.get(LoyalityTransactionConstants.preRewardPoints)));

			else if (entry.getKey().equals(LoyalityTransactionConstants.preStatusPoints))
				loyalityTransaction.setPreStatusPoints(Double.valueOf(mapVal.get(LoyalityTransactionConstants.preStatusPoints)));
			
			else if (entry.getKey().equals(LoyalityTransactionConstants.statusId))
				loyalityTransaction.setStatusID(Integer.valueOf(mapVal.get(LoyalityTransactionConstants.statusId)));
			
			else if (entry.getKey().equals(LoyalityTransactionConstants.subscriberNumber))
				loyalityTransaction.setSubscriberNumber(mapVal.get(LoyalityTransactionConstants.subscriberNumber));
			
			else if (entry.getKey().equals(LoyalityTransactionConstants.voucherOrderId))
				loyalityTransaction.setVoucherOrderID(mapVal.get(LoyalityTransactionConstants.voucherOrderId));
			else if (entry.getKey().equals(LoyalityTransactionConstants.rewardPoints))
				loyalityTransaction.setRewardPoints(Double.valueOf(mapVal.get(LoyalityTransactionConstants.rewardPoints)));
			else if (entry.getKey().equals(LoyalityTransactionConstants.reqTransactionID))
				loyalityTransaction.setReqTransactionID(mapVal.get(LoyalityTransactionConstants.reqTransactionID));
			else if(entry.getKey().equals(LoyalityTransactionConstants.statusDescription))
				loyalityTransaction.setStatusDesc(mapVal.get(LoyalityTransactionConstants.statusDescription));
			else if(entry.getKey().equals(LoyalityTransactionConstants.expiryDate))
				loyalityTransaction.setExpiryDate(sdf.parse(mapVal.get(LoyalityTransactionConstants.expiryDate)));
			else if(entry.getKey().equals(LoyalityTransactionConstants.description))
				loyalityTransaction.setDescription(mapVal.get(LoyalityTransactionConstants.description));
		}
		return loyalityTransaction;
	}

}
