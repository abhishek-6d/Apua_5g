package com.sixdee.imp.bo;

/**
 * 
 * @author Rahul K K
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
 * <td>January 21,2014 12:55:09 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Globals;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.AccountConversionDAO;
import com.sixdee.imp.dao.AccountNumberDAO;
import com.sixdee.imp.dao.BasicInformationDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.AccountConversionDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.util.CDRLoggerUtil;

public class AccountConversionBO extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	private Logger logger = Logger.getLogger(AccountConversionBO.class);

	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => AccountConversionBL :: Method => buildProcess()");
		
		AccountConversionDAO  accountConversionDAO = null;
		AccountConversionDTO accountConversionDTO = null;
		ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyLinkedLineList = null;
		SubscriberNumberTabDTO subscriberInfoDTO = null;
		LoyaltyRegisteredNumberTabDTO loyaltyLinkInfo = null;
		TableInfoDAO tableInfoDAO = null;
		String transactionId = null;
		String subscriberNumber = null;
		int currentTypeId = 0;
		int previousTypeId = 0;
		String subscriberTableName = null;
		long loyaltyId = 0;
		String loyaltyLinkedTableName = null;
		SimpleDateFormat sdf = null;
		int statusOfConversion = 3; //1-Success , 2-No Information in loyalty platform , 3 - Modified state is same in loyalty platform
		double pointsAtConversion = 0;
		String accountNumber = null;
		ArrayList<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredNumberTabDTOs = null;
		AccountNumberTabDTO accountNumberTabDTO = null;
		AccountNumberDAO accountNumberDAO = null;
		Session modifySession = null;
		Transaction modifyTransaction = null;
		try{
			accountConversionDTO = (AccountConversionDTO) genericDTO.getObj();
			
			transactionId = accountConversionDTO.getTransactionId();
			subscriberNumber = accountConversionDTO.getSubscriberNumber();
			previousTypeId = accountConversionDTO.getCurrentTypeId();
			currentTypeId = accountConversionDTO.getNewTypeId();
				accountNumber = accountConversionDTO.getAccountNumber();
			
			logger.info("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Conversion Required ,Previous Type "+previousTypeId+" To  Current Type "+currentTypeId+" With Account Number "+accountNumber);
			//Check if subscriber is in Loyalty Platform
			tableInfoDAO = new TableInfoDAO();
			subscriberTableName = tableInfoDAO.getSubscriberNumberTable(subscriberNumber);
			accountConversionDAO=new AccountConversionDAO();
			long t1 = System.currentTimeMillis();
			subscriberInfoList = accountConversionDAO.getSubscriberInformation(transactionId,subscriberTableName,subscriberNumber);
		
			long t2 = System.currentTimeMillis();
			logger.debug("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Time Taken For identifying subscriber in loyalty table "+(t2-t1));
			
			if(subscriberInfoList != null && subscriberInfoList.size()!=0){
				//Subscriber is a loyalty platform customer
				subscriberInfoDTO = subscriberInfoList.get(0);
				loyaltyId = subscriberInfoDTO.getLoyaltyID();
				pointsAtConversion = subscriberInfoDTO.getPoints();
				String oldAccountNumber = subscriberInfoDTO.getAccountNumber();
				accountConversionDTO.setOldAccountNumber(oldAccountNumber+"");
				
					//subscriberInfoDTO.getPoints();
				loyaltyLinkedTableName = tableInfoDAO.getLoyaltyRegisteredNumberTable(loyaltyId+"");
				t1 = System.currentTimeMillis();
				loyaltyLinkedLineList = accountConversionDAO.getLoyaltyLinkedInfo(transactionId,loyaltyId,subscriberNumber,loyaltyLinkedTableName);
				t2 = System.currentTimeMillis();
				logger.debug("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Time Taken For identifying subscriber in loyalty table "+(t2-t1));
				
				if(loyaltyLinkedLineList == null || loyaltyLinkedLineList.size()==0){
					statusOfConversion = 2;
				}else{
					modifySession = HiberanteUtil.getSessionFactory().openSession();
					modifyTransaction = modifySession.beginTransaction();
					if(subscriberInfoDTO.getAccountTypeId()==currentTypeId ){
						logger.info("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Customer is Already in the Type "+currentTypeId);
					}else{
						subscriberInfoDTO.setAccountTypeId(currentTypeId);
						if(accountNumber!=null){
							subscriberInfoDTO.setAccountNumber(accountNumber);
						}
						t1 = System.currentTimeMillis();
						boolean b  = accountConversionDAO.updateSubscriberTypeInformation(transactionId,subscriberTableName,modifySession,modifyTransaction,subscriberInfoDTO);
						t2 = System.currentTimeMillis();
						logger.info("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Finished Updating Subscriber Information "+(t2-t1));
						statusOfConversion = 1;
					}
					loyaltyLinkInfo = loyaltyLinkedLineList.get(0);
					//oldAccountNumber = loyaltyLinkInfo.getAccountNumber();
					if(loyaltyLinkInfo.getAccountTypeId()==currentTypeId){
						logger.info("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Customer with Loyalty Id "+loyaltyId+" is Already in the Type "+currentTypeId);						
					}else{
						loyaltyLinkInfo.setAccountTypeId(currentTypeId);
						if(accountNumber!=null)
							loyaltyLinkInfo.setAccountNumber(accountNumber);
						t1 = System.currentTimeMillis();
						boolean b  = accountConversionDAO.updateLoyaltyLinkedInformation(transactionId,loyaltyLinkedTableName,modifySession,modifyTransaction,loyaltyLinkInfo);
						t2 = System.currentTimeMillis();
						logger.info("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Finished Updating Loyalty Linked Information "+(t2-t1));
						statusOfConversion = 1;
						
					}
					
					}
				//subscriberInfoDTO
				if(statusOfConversion==1 && accountNumber != null && !(oldAccountNumber.equalsIgnoreCase(accountNumber))){
					logger.debug("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : old account number is "+oldAccountNumber);
					String accountTableName = tableInfoDAO.getAccountNumberTable(oldAccountNumber+"");
					BasicInformationDAO basicInformationDAO = new BasicInformationDAO();
					//accountConversionDAO=new AccountConversionDAO();
					
					t1 = System.currentTimeMillis();
					ArrayList<AccountNumberTabDTO> accountList = null;
					//accountList = basicInformationDAO.getSubscriberAccountInformation(transactionId,accountTableName,oldAccountNumber);
					accountNumberDAO = new AccountNumberDAO();
					accountList = accountNumberDAO.getAccountDetails(transactionId, accountTableName, loyaltyId, oldAccountNumber);
					loyaltyRegisteredNumberTabDTOs = accountConversionDAO.getLoyaltyLinkedInfo(transactionId, loyaltyId, loyaltyLinkedTableName);
					if(loyaltyRegisteredNumberTabDTOs.size()==1){
						accountNumberTabDTO = accountList.get(0);
						accountNumberTabDTO.setStatusID(13);
						accountNumberDAO.deleteAccount(transactionId, accountTableName, modifySession,modifyTransaction,loyaltyId,accountTableName, oldAccountNumber, accountNumberTabDTO);
						logger.info("Transaction ID : "+transactionId+" LoyaltyId : "+loyaltyId+" AccountNumber "+oldAccountNumber+" Action : Deletion of account is successfull");
					}
				//	AccountNumberTabDTO accountNumberTabDTO = null;
					accountList = null;
					accountTableName = tableInfoDAO.getAccountNumberTable(accountNumber+"");
					accountList = accountNumberDAO.getAccountDetails(transactionId, accountTableName, loyaltyId, accountNumber);
					if(accountList != null && accountList.size()>0){
						accountNumberTabDTO = accountList.get(0);
						if(accountNumberTabDTO.getLoyaltyID()==loyaltyId){
							logger.info("Transaction ID : "+transactionId+" LoyaltyId : "+loyaltyId+" AccountNumber "+accountNumber+" Matching loyalty ids , do nothing");
							
						}else{
							logger.info("Transaction ID : "+transactionId+" LoyaltyId : "+loyaltyId+" AccountNumber "+accountNumber+" Mismatch in loyalty ids ");
							accountConversionDTO.setStatusCode("SC1001");
							accountConversionDTO.setStatusDesc("Account Number already exists for another loyalty id");
							statusOfConversion = 5;
							throw new CommonException("Account Number already exists for another loyalty id");
							//writeCDR(accountConversionDTO, loyaltyId, pointsAtConversion, statusOfConversion);
						}
					}else{
						logger.debug("Transaction ID : "+transactionId+" LoyaltyId : "+loyaltyId+" AccountNumber "+accountNumber+" Action : Adding new account info");
						accountNumberTabDTO = new AccountNumberTabDTO();
						accountNumberTabDTO.setAccountNumber(accountNumber);
						accountNumberTabDTO.setLoyaltyID(loyaltyId);
						accountNumberTabDTO.setPoints(0.0);
						accountNumberTabDTO.setStatusID(1);
						logger.debug(
								"**");
						basicInformationDAO.saveOrUpdateInstance(transactionId,modifySession,modifyTransaction,accountTableName,accountNumberTabDTO,1);
						
					}
					//for()
					
					t2 = System.currentTimeMillis();
					logger.debug("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Time Taken For identifying subscriber in loyalty table "+(t2-t1));
				}
			}else{
				logger.info("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : This subscriber is not registered in loyalty platform .");
				statusOfConversion = 2;

			}
			
		}catch(CommonException ce){
			logger.error("TransactionId : "+transactionId+" SubscriberNumber : "+subscriberNumber+" Message : Exception ",ce);
			if(modifyTransaction != null){
				modifyTransaction.rollback();
				modifyTransaction = null;
			}
			if(statusOfConversion==1|| statusOfConversion==3)
				statusOfConversion = 4;
		}
		catch(Exception e){

			if(modifyTransaction != null){
				modifyTransaction.rollback();
				modifyTransaction = null;
			}
			statusOfConversion = 4;
		
			logger.error("Exception occured ",e);
			statusOfConversion = 4;
		}
		finally{
			if(modifyTransaction!=null){
				modifyTransaction.commit();
			}
			if(statusOfConversion == 2){
				accountConversionDTO.setStatusCode("SC1000");
				accountConversionDTO.setStatusDesc("Customer Not Registered for Loyalty Platform");
			}else if(statusOfConversion == 3){
				accountConversionDTO.setStatusCode("SC1000");
				accountConversionDTO.setStatusDesc("Customer is already in the modified Type");
			}else if(statusOfConversion==4){
				accountConversionDTO.setStatusCode("SC1000");
				accountConversionDTO.setStatusDesc("Abnormal Termination");				
			}
			writeCDR(accountConversionDTO,loyaltyId,pointsAtConversion,statusOfConversion);
			if(modifySession!=null){
				modifySession.close();
			}
		}
		
					
			return genericDTO;
		}

	private void writeCDR(AccountConversionDTO accountConversionDTO,
			long loyaltyId, double pointsAtConversion, int statusOfConversion) {
		String delimiter = "|";
		String cdr = null;
		String emptyString= "";
		CDRLoggerUtil cdrLogger = new CDRLoggerUtil();
//		String service = "34"
		
	
			cdr =accountConversionDTO.getTransactionId()+delimiter+accountConversionDTO.getTimestamp()+delimiter+accountConversionDTO.getChannel()+delimiter+
					accountConversionDTO.getActivity()+delimiter+accountConversionDTO.getSubscriberNumber()+delimiter+loyaltyId+delimiter+accountConversionDTO.getOldAccountNumber()+delimiter+
					accountConversionDTO.getAccountNumber()+delimiter+accountConversionDTO.getCurrentTypeId()+delimiter+accountConversionDTO.getNewTypeId()+delimiter+
					pointsAtConversion+delimiter+statusOfConversion+delimiter+(accountConversionDTO.getStatusDesc()!=null?accountConversionDTO.getStatusDesc():"Converted Successfully");	
			
		
		//cdrLogger.flushFatalCDR(logger, delimiter, cdr);
	}
	
	
	public static void main(String[] args) throws com.sixdee.arch.exception.CommonException, CommonException {
		AccountConversionDTO accountConversionDTO = new AccountConversionDTO();
		accountConversionDTO.setTransactionId("12345");
		accountConversionDTO.setSubscriberNumber("96891121244");
		accountConversionDTO.setCurrentTypeId(14);
		accountConversionDTO.setNewTypeId(9);
		GenericDTO genericDTO = new GenericDTO();
		Globals.loadProperties();
		Globals.cacheMap();
		
		genericDTO.setObj(accountConversionDTO);
		
		//new AccountManagement().accountMerging(accountMergingDTO)
		
	}
}
