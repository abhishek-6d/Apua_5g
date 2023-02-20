/**
 * 
 */
package com.sixdee.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;
import com.sixdee.lms.dao.LoyaltyProfileManagerDAO;

/**
 * @author rahul.kr
 *
 */
public class LoyaltyAccountCreationUtil {

	private static final Logger logger = Logger.getLogger("LoyaltyAccountCreationUtil");
	public boolean createAccount(String requestId,String channelId,LoyaltyProfileTabDTO loyaltyProfileTabDTO,List<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredMDNList,
								List<AccountNumberTabDTO> accountNumberList, ArrayList<SubscriberNumberTabDTO> subscriberNumberList){
		Session session = null;
		Transaction transaction = null;
		LoyaltyProfileManagerDAO loyaltyProfileManagerDAO = null;
		boolean isAccountCreated = false;
		String tableName = null;
		TableInfoDAO infoDAO = null;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		try{
			loyaltyProfileManagerDAO = new LoyaltyProfileManagerDAO();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			infoDAO = new TableInfoDAO();
			tableName=infoDAO.getLoyaltyProfileTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
			logger.info("tableName:"+tableName);
			//loyaltyProfileManagerDAO.persistLoyaltyAccount(session, transaction, loyaltyProfileTabDTO);
			loyaltyProfileManagerDAO.persistLoyaltyAccount(session,transaction,tableName,loyaltyProfileTabDTO);
		
			for(LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO : loyaltyRegisteredMDNList){
				tableName=infoDAO.getLoyaltyRegisteredNumberTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
				loyaltyProfileManagerDAO.persistLoyaltyRegisteredNumber(session,transaction,tableName,loyaltyRegisteredNumberTabDTO);
			}
			for(AccountNumberTabDTO accountNumberTabDTO : accountNumberList){
				tableName=infoDAO.getAccountNumberTable(accountNumberTabDTO.getAccountNumber()+"");
				loyaltyProfileManagerDAO.persistAccountNumberDetails(session,transaction,tableName,accountNumberTabDTO);
			}
			for(SubscriberNumberTabDTO subscriberNumberTabDTO : subscriberNumberList){
				tableName=infoDAO.getSubscriberNumberTable(subscriberNumberTabDTO.getSubscriberNumber()+"");
				loyaltyProfileManagerDAO.persistSubscriberNumberDetails(session,transaction,tableName,subscriberNumberTabDTO);
			}
			loyaltyTransactionTabDTO = logTransaction(requestId,channelId,loyaltyProfileTabDTO,subscriberNumberList,accountNumberList);
			tableName = infoDAO.getLoyaltyTransactionTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
			//logger.info("TableName "+tableName+" ");
			loyaltyProfileManagerDAO.persistLoyaltyTransaction(requestId,session,transaction,tableName,channelId,loyaltyTransactionTabDTO);
			transaction.commit();
			isAccountCreated = true;
			
		}catch (Exception e) {
			logger.error("RequestId "+requestId+" Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return isAccountCreated;
	}
	private LoyaltyTransactionTabDTO logTransaction(String requestId, String channel,LoyaltyProfileTabDTO loyaltyProfileTabDTO,
			ArrayList<SubscriberNumberTabDTO> subscriberNumberList, List<AccountNumberTabDTO> accountNumberList) {
		LoyalityCommonTransaction loyaltyCommonTransaction = null;
		LoyaltyTransactionTabDTO loyalityTransaction = null;
		HashMap<String,String> loyaltyTransactionMap = null; 
		try{
			loyaltyTransactionMap = new HashMap<>();
			loyalityTransaction = new LoyaltyTransactionTabDTO();
			loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID, String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId, String.valueOf(LoyaltyTransactionStatus.loyaltyIDCreated));//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.channel,channel);//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.curTierId,String.valueOf(loyaltyProfileTabDTO.getTierId()));//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.preTierId,String.valueOf(loyaltyProfileTabDTO.getTierId()));//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.reqTransactionID,requestId);//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.serverId,Cache.cacheMap.get("SERVER_ID"));//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber,loyaltyProfileTabDTO.getContactNumber());//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.accountNumber,loyaltyProfileTabDTO.getAccountNumber());//added S
			loyaltyTransactionMap.put(LoyalityTransactionConstants.statusDescription,Cache.getConfigParameterMap().get("ACCOUNT_CREATION_TXN_DESC").getParameterValue());
			loyaltyCommonTransaction = new LoyalityCommonTransaction();
			
			loyalityTransaction = loyaltyCommonTransaction.loyaltyTransactionSetter(loyalityTransaction, loyaltyTransactionMap);
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			
		}
		return loyalityTransaction;
	}
}
