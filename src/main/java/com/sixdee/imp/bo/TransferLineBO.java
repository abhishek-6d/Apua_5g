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
 * <td>June 20,2013 04:39:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;



import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.AccountConversionDAO;
import com.sixdee.imp.dao.BasicInformationDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dao.TransferLineDAO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TransferLineDTO;
import com.sixdee.imp.util.CDRLoggerUtil;

public class TransferLineBO extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private Logger logger = Logger.getLogger(TransferLineBO.class);

	
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => TransferLineBO :: Method => buildProcess()");
		
		TransferLineDTO transferLineDTO = null;
		try{
			transferLineDTO = (TransferLineDTO) genericDTO.getObj();
			transferAccount(transferLineDTO);
			genericDTO.setStatusCode("SC0000");
		}catch (CommonException e) {
			transferLineDTO.setStatusCode("SC1000");
			transferLineDTO.setStatusDesc(e.getMessage());
			genericDTO.setStatusCode("SC0003");
		}
		finally{
			
		}
		
					
			return genericDTO;
		}
	private void transferAccount(TransferLineDTO transferLineDTO) throws CommonException {
		//TransferLineDAO  transferLineDAO = null;
		TableInfoDAO tableDetailsDAO = null;
		String fromTableName = null;
		String toTableName = null;
		String donorSubscriber = null;
		String recieverSubscriber = null;
		String txnId = null;
		SubscriberNumberTabDTO donorDTO = null;
		SubscriberNumberTabDTO recieverDTO = null;
		BasicInformationDAO commonDAO = null;
		boolean isCommon = false;
		//String 
		//TableInfoDAO tableInfoDAO = null;
		String cdrString = null;
		String delimiter = "|";
		String status = null;
		String emptyString = "";
		 SimpleDateFormat sdf  = null;
		boolean isDonorNull = false;
		boolean isRecieverNull = false;
		CDRLoggerUtil cdrLoggerUtil = null;
		boolean isCDRWritten = false;
		String channel = null;
		long t1 = System.currentTimeMillis();
		
		try{
			//transferLineDAO = new TransferLineDAO();
		//	logger.info("Transfer Request for ["+transferLineDTO.getaSubscriberNumber()+"] to ["+transferLineDTO.getbSubscriberNumber()+"]");
			tableDetailsDAO = new TableInfoDAO();
			/*
			 * Finding A Table Details
			 */
			
			/*if(transferLineDTO.getDonorADSL()!=null){
				aNumber = transferLineDTO.getDonorADSL();
				fromTableName=tableDetailsDAO.getADSLNumberTable(aNumber);
			}else{
				aNumber = transferLineDTO.getaSubscriberNumber();
				fromTableName = tableDetailsDAO.getSubscriberNumberTable(aNumber);
			}
			*//*
			 * Finding B Table Details
			 */
			/*if(transferLineDTO.getbAdslNumber()!=null){
				bNumber = transferLineDTO.getbAdslNumber();
				toTableName=tableDetailsDAO.getADSLNumberTable(bNumber);
			}else{
				bNumber = transferLineDTO.getbSubscriberNumber();
				toTableName = tableDetailsDAO.getSubscriberNumberTable(bNumber);
			}
			*/
			channel = transferLineDTO.getChannel();
		
			fromTableName = tableDetailsDAO.getSubscriberNumberTable(donorSubscriber);
			toTableName = tableDetailsDAO.getSubscriberNumberTable(recieverSubscriber);
			donorSubscriber = transferLineDTO.getDonorSubscriber();
			recieverSubscriber = transferLineDTO.getRecieverSubscriber();
			txnId = transferLineDTO.getTransactionId();
			logger.info("Transaction Id : "+txnId+" DonorNumber : "+donorSubscriber+" RecieverNumber : "+recieverSubscriber+" Starting Transfer Activity ");
			commonDAO = new BasicInformationDAO();
			donorDTO = commonDAO.getLoyaltyDetails(fromTableName, Long.parseLong(donorSubscriber));
			if(donorDTO != null){
				if(donorDTO.getStatusID()==1){
					recieverDTO = commonDAO.getLoyaltyDetails(toTableName, Long.parseLong(recieverSubscriber));
					if(recieverDTO != null){
						if(recieverDTO.getStatusID()==1){
							if(transferLineDTO.isPointsReqd()){
								transferLineWithPoints(donorDTO,recieverDTO);
								auditTransferWithPoints();
							}else{
								
								cdrString= transferLineWithOutPoints(txnId,fromTableName,donorDTO,recieverDTO,donorSubscriber,recieverSubscriber,channel);
								auditTransferWithPoints();
								
							}
						}else{
							status = "104";
							throw new CommonException("Reciever  Subscriber "+recieverSubscriber+" is expired/preactive/delete");
						}
					}else{
						status = "103";
						isRecieverNull = true;
						throw new CommonException("Reciever Subscriber "+recieverSubscriber+" is not a Loyalty Customer . For availng the service please register for Loyalty Service ");
					}
				}else{
					status = "102";
					isRecieverNull = true;
					throw new CommonException("Donor Subscriber "+donorSubscriber+" is expired/preactive/delete");
				}
			}else{
				status = "101";
				isDonorNull = true;
				isRecieverNull = true;
				throw new CommonException("Donor  Subscriber "+donorSubscriber+" is not a Loyalty Customer . For availng the service please register for Loyalty Service ");
			}
		}catch (CommonException e) {
			logger.error("Transaction ID "+txnId+" Exception occured",e);
			isCommon = true;
			if(e.getErrorCode()==1000){
				isCDRWritten = true;
			}
			throw e;
		}catch (Exception e) {
			logger.error("Transaction ID "+txnId+" Exception occured",e);
			if(isCommon){
				throw new CommonException(e.getMessage());
			}else{
				throw new CommonException("Unknown System Failure");
			}
		}
		finally{
			if(!isCDRWritten ){
				if( cdrString == null){
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d = new Date();
					cdrString = txnId+delimiter+channel+delimiter+donorSubscriber+delimiter+emptyString+delimiter+(isDonorNull?emptyString:donorDTO.getStatusID())+delimiter+(isDonorNull?emptyString:donorDTO.getPoints())+delimiter+recieverSubscriber+delimiter+emptyString+delimiter+(!isRecieverNull?recieverDTO.getStatusID():emptyString)+delimiter+emptyString+delimiter+(transferLineDTO.isPointsReqd()?2:1)+delimiter+35+delimiter+status;
					
				}
			cdrLoggerUtil = new CDRLoggerUtil();
			//cdrLoggerUtil.flushFatalCDR(logger, delimiter, cdrString);
			tableDetailsDAO=null;
			}
		}
	}
	
	private void auditTransferWithPoints() {
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO=new LoyaltyTransactionTabDTO();
		
		try{
			
		}catch(Exception e){
			
		}
		
	}
	private String transferLineWithOutPoints(
			String txnId, String fromTableName, SubscriberNumberTabDTO donorDTO,
			SubscriberNumberTabDTO recieverDTO, String donorSubscriber, String recieverSubscriber, String channel) throws CommonException {
		TransferLineDAO transferLineDAO = null;
		TableInfoDAO tableInfoDAO = null;
		
		long donorLoyaltyId = 0;
		long recieverLoyaltyId = 0;
		String donorRegTable = null;
		LoyaltyRegisteredNumberTabDTO donorRegDTO = null;
		AccountConversionDAO accountConversionDAO = null;
		String recieverRegTable = null;
		boolean isUpdated = false;
		 SimpleDateFormat sdf  = null;
		 String cdrString = null;
		 String delimiter = "|";
		 String emptyString = "0";
		String status = "100";String statusMsg = null;
		boolean isException = false;
		// String donorSubscriber = null;String recieverSubscriber = null;
		try{
			transferLineDAO = new TransferLineDAO();
		
			recieverLoyaltyId = recieverDTO.getLoyaltyID();
			donorLoyaltyId = donorDTO.getLoyaltyID();
			recieverLoyaltyId = recieverDTO.getLoyaltyID();
			if(recieverLoyaltyId == donorLoyaltyId){
				logger.info("Transaction ID "+txnId+" Reciever LoyaltyId "+recieverLoyaltyId+" And Donor LoyaltyId "+donorLoyaltyId+" are same ");
				status = "105";
				throw new CommonException("Reciever and Donor are same");
			}
			long s1 = System.currentTimeMillis();
			logger.info("Tranasaction ID "+txnId+" Subscriber : "+donorDTO.getSubscriberNumber()+" Transfering to Loyalty Id "+recieverDTO.getLoyaltyID());
			if(transferLineDAO.lineTransfer(txnId,fromTableName,donorDTO, recieverDTO)){
				long s2 = System.currentTimeMillis();
				logger.info("Transaction ID "+txnId+" Time Taken for Transfer of  Lines in Subscriber_Number Tables "+(s2-s1));
				tableInfoDAO = new TableInfoDAO();
				
				//if(re)
				donorRegTable = tableInfoDAO.getLoyaltyRegisteredNumberTable(donorLoyaltyId+"");
				recieverRegTable = tableInfoDAO.getLoyaltyRegisteredNumberTable(recieverLoyaltyId+"");
				accountConversionDAO = new AccountConversionDAO();
				ArrayList<LoyaltyRegisteredNumberTabDTO> donorInfo = accountConversionDAO.getLoyaltyLinkedInfo(txnId, donorLoyaltyId, (donorDTO.getSubscriberNumber())+"", donorRegTable);
				if(donorInfo!=null && donorInfo.size()>0){
					donorRegDTO = donorInfo.get(0);
					donorRegDTO.setLoyaltyID(recieverLoyaltyId);
					logger.info("Transaction ID "+txnId+" Transfering Loyalty Line "+donorDTO.getSubscriberNumber()+ " to LoyaltyId "+recieverLoyaltyId);
					if(donorRegTable.equals(recieverRegTable)){
						isUpdated = transferLineDAO.lineRegistTransfer(donorRegDTO, recieverRegTable);
						if(!isUpdated){
							logger.warn("Transaction ID "+txnId+" Transfering has failed in loyalty registration table");
							//status = "110";
							throw new Exception("Updation Failed");
						}
					}
					else{
						//Delete from Donor Registration table and insert in 
						logger.debug("Need to delete from Donor Loyalty Table "+donorRegTable+" and add to reciever Loyalty Table "+recieverRegTable);
					}
				}else
					logger.info("Transaction ID : "+txnId+" Donor Information is not available in Loyalty Registered Table");
			}
				
		}
		catch(Exception e){
			if(status.equals("100")){
				logger.error("Transaction ID "+txnId+" Exception ",e);
				status = "110";
				statusMsg = "Unknow System Error";
			}else{
				statusMsg = e.getMessage();
						
			}
			isException = true;
			
		}
		finally{
			String accountStatus = "1";
			cdrString = txnId+delimiter+channel+delimiter+donorSubscriber+delimiter+donorLoyaltyId+delimiter+accountStatus+delimiter+donorDTO.getPoints()+delimiter+recieverSubscriber+delimiter+recieverLoyaltyId+delimiter+accountStatus+delimiter+emptyString+delimiter+1+delimiter+35+delimiter+status;
			tableInfoDAO=null;
			if(isException){
				CDRLoggerUtil cdrLoggerUtil = new CDRLoggerUtil();
				
				//cdrLoggerUtil.flushFatalCDR(logger, delimiter, cdrString);
				
				throw new CommonException(1000,statusMsg);
			}
			
		}
		return cdrString;
	}
	private void transferLineWithPoints(
			SubscriberNumberTabDTO donorDTO,
			SubscriberNumberTabDTO recieverDTO) {

		TransferLineDAO transferLineDAO = null;
		TableInfoDAO tableInfoDAO = null;
		long donorLoyaltyId = 0;
		long recieverLoyaltyId = 0;
		String aAccountTable = null;
		String bAccountTable = null;
		String txnId = null;
		try{
			transferLineDAO = new TransferLineDAO();
		
			long t1 = System.currentTimeMillis();
			if(transferLineDAO.pointTransfer(donorDTO, recieverLoyaltyId)){
				long t2= System.currentTimeMillis();
			//	logger.info("TransactionId "++"Time Taken to complete transfer of subscriber Numbers "+(t2-t1));
				donorLoyaltyId = donorDTO.getLoyaltyID();
				recieverLoyaltyId = recieverDTO.getLoyaltyID();
				
				tableInfoDAO = new TableInfoDAO();
				if(recieverDTO.getAccountNumber()!=null){
					bAccountTable = tableInfoDAO.getAccountNumberTable(recieverDTO.getAccountNumber()+"");
					transferLineDAO.updateAccountTable(aAccountTable,bAccountTable,donorDTO,recieverDTO);
				}
				
				
			}
				
		}finally{
			tableInfoDAO=null;
		}
	
	}
}
