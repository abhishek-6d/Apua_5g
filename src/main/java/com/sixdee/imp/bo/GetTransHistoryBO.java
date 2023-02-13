/**
 * 
 */
package com.sixdee.imp.bo;

import java.util.ArrayList;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.GetTransHistoryDAO;
import com.sixdee.imp.dto.GetTransHistoryDTO;
import com.sixdee.imp.dto.GetTransactionHistoryDetailsDTO;
import com.sixdee.imp.dto.SubscriberTransactionHistroyDTO;
import com.sixdee.imp.util.GeneralProcesses;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GetTransHistoryBO extends BOCommon {

	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {
		GetTransHistoryDTO getTransHistoryDTO = null;
		ArrayList<SubscriberTransactionHistroyDTO> transactionList = null;
		try{
			getTransHistoryDTO = (GetTransHistoryDTO) genericDTO.getObj();
			transactionList = generateTransactions(getTransHistoryDTO);
			getTransHistoryDTO.setObj(transactionList);
	//		genericDTO.setObj(genericDTO);
		}catch (CommonException e) {
			if(e.getMessage().equals("No Transaction for Subscriber")){
				genericDTO.setStatus("SC0004");
			}else{
				genericDTO.setStatus("SC0003");
			}
			throw e;
		}
		catch (Exception e) {
			genericDTO.setStatus("SC0003");
			throw new CommonException(e);				
		}finally{
			genericDTO.setObj(getTransHistoryDTO);
		}
		return genericDTO;
	}

	/**
	 * Getting Transaction history of one subscriber
	 * @param GetTransHistoryDTO
	 * @return 
	 * @throws Exception 
	 */
	private ArrayList<SubscriberTransactionHistroyDTO> generateTransactions(GetTransHistoryDTO getTransHistoryDTO)
	throws CommonException,Exception {
		ArrayList<GetTransactionHistoryDetailsDTO> transList = null;
		ArrayList<SubscriberTransactionHistroyDTO> subsHistoryList = null;
		GetTransHistoryDAO getTransHistDAO = null;
		GeneralProcesses generalProcesses = null;
		int noOfLastTrans = 0;
		
		String subsNumber = null;
		String fromDate = null;
		String endDate = null;
		String tableName = null;
		try{
			noOfLastTrans = getTransHistoryDTO.getNoOfLastTransaction();
			subsNumber = getTransHistoryDTO.getSubscriberNumber();
			fromDate = getTransHistoryDTO.getFromDate();
			endDate = getTransHistoryDTO.getToDate();
			getTransHistDAO = new GetTransHistoryDAO();
			generalProcesses = new GeneralProcesses();
			tableName = generalProcesses.identifyTable(Cache.cacheMap.get("TRANSACTION_TABLE_PREFIX"),subsNumber);
			/*
			 * if n Last transactions to be find out
			 * then these cases can come
			 * 	1.Last n transaction from the entire history
			 *  2.Last n transaction from the particular time interval
			 * 
			 *  if n last transactions has come go for the particular time interval
			 */
			if(noOfLastTrans != 0){
				System.out.println(tableName);
				if((fromDate==null || fromDate.trim().equals(""))&& (endDate == null || endDate.trim().equals(""))){
					//tableName = identifyTable(subsNumber);
					subsHistoryList = getTransHistDAO.getTransactionDetails(tableName,subsNumber,noOfLastTrans);
				}else{
					//tableName = identifyTable(subsNumber);
					subsHistoryList = getTransHistDAO.getTransactionDetails(tableName,subsNumber,noOfLastTrans,fromDate,endDate);
				}
			}else{
				//tableName = identifyTable(subsNumber);
				subsHistoryList = getTransHistDAO.getTransactionDetails(tableName,subsNumber,fromDate,endDate);
			}
			if(subsHistoryList == null || subsHistoryList.size()==0){
				logger.warn("Subscriber has no transaction on the period ["+subsNumber+" for period ["+fromDate+"] " +
						" - ["+endDate+"]");
				throw new CommonException("No Transaction for Subscriber");
			}else{
				transList = new ArrayList<GetTransactionHistoryDetailsDTO>();
				for(SubscriberTransactionHistroyDTO subsHistory : subsHistoryList){
					GetTransactionHistoryDetailsDTO getTransactionHistoryDTO = new GetTransactionHistoryDetailsDTO();
					getTransactionHistoryDTO.setActivationDate(subsHistory.getActivationDate()+"");
					getTransactionHistoryDTO.setStartDate(subsHistory.getStartDate()+"");
					getTransactionHistoryDTO.setEndDate(subsHistory.getEndDate()+"");
					
					getTransactionHistoryDTO.setServiceName(Cache.appMap.get(subsHistory.getServiceID()).getServiceName());
					getTransactionHistoryDTO.setTransactionType(subsHistory.getTransactionType());
					getTransactionHistoryDTO.setDate(subsHistory.getDate()+"");
					transList.add(getTransactionHistoryDTO);
				}
			}
		}catch (Exception e) {
			logger.error("Exception occured for transaction id ["+getTransHistoryDTO.getTransactionId()+"]",e);
			throw e;
		}
		return subsHistoryList;
	}

	
	public static void main(String[] args) {
		String su = "2312381923";
		System.out.println(su.substring(su.length()-2));
	}

}
