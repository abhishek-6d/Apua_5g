/**
 * 
 */
package com.sixdee.imp.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dao.CustomerProfileDetailsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.ProfileInformationUpdateDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

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
public class CustomerProfileDetailsUtil {

	private static final Logger logger = Logger.getLogger(CustomerProfileDetailsUtil.class);
	public SubscriberNumberTabDTO getSubscriberNumberInformation(String service,String txnId,
			String subscriberNumber) {
		CustomerProfileDetailsDAO customerProfileDetailsDAO = null;
		ArrayList<SubscriberNumberTabDTO> subscriberNumberList = null;
		TableInfoDAO tableInfoDAO = null;	
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		try{
			tableInfoDAO = new TableInfoDAO();
			String tableName = tableInfoDAO.getSubscriberNumberTable(subscriberNumber);
			logger.debug("SERVICE :- "+service+" - Transaction ID :- ["+txnId+"] "
					+ "MSISDN :- ["+subscriberNumber+"] getting details from table "+tableName+" for subscriber number "+subscriberNumber);
			customerProfileDetailsDAO = new CustomerProfileDetailsDAO();
			subscriberNumberList = customerProfileDetailsDAO.getLoyaltyIdBasedOnSubscriberNumber(service,txnId,tableName, subscriberNumber);
			if(subscriberNumberList!=null&&subscriberNumberList.size()>0){
				subscriberNumberTabDTO = subscriberNumberList.get(0);
			}
		}catch(Exception e){
			logger.error("SERVICE :- "+service+" - Transaction ID :- ["+txnId+"] "
					+ "MSISDN :- ["+subscriberNumber+"] Exception occured ",e);
		}	
		finally{
			customerProfileDetailsDAO = null;
		}
		return subscriberNumberTabDTO;
	}
	public ArrayList<NationalNumberTabDTO> getNationalIdForAccount(String service, String txnId,
			long loyaltyID) {
		CustomerProfileDetailsDAO customerProfileDetailsDAO = null;
		ArrayList<NationalNumberTabDTO> nationalNumberList = null;
		TableInfoDAO tableInfoDAO = null;	
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		try{
			tableInfoDAO = new TableInfoDAO();
			String tableName = tableInfoDAO.getNationalNumberTable("0");
			logger.debug("SERVICE :- "+service+" - Transaction ID :- ["+txnId+"] "
					+ "LoyaltyId :- ["+loyaltyID+"] getting details from table "+tableName);
			customerProfileDetailsDAO = new CustomerProfileDetailsDAO();
			nationalNumberList = customerProfileDetailsDAO.getNationalIdBasedOnLoyaltyId(service,txnId,tableName, loyaltyID);
		}catch(Exception e){
			logger.error("SERVICE :- "+service+" - Transaction ID :- ["+txnId+"] "
					+ "LoyaltyId :- ["+loyaltyID+"] Exception occured ",e);
		}	
		finally{
			customerProfileDetailsDAO = null;
		}
		return nationalNumberList;
	}
	public boolean updateNationalIdInfo(String service, String txnId,
			NationalNumberTabDTO nationalNumberTabDTO, ProfileInformationUpdateDTO profileInformationUpdateDTO) {
		CustomerProfileDetailsDAO customerProfileDetailsDAO = null;
		TableInfoDAO tableInfoDAO = null;	
		boolean isUpdated = false;
		int updateCount = 0;
		try{
			tableInfoDAO = new TableInfoDAO();
			String tableName = tableInfoDAO.getNationalNumberTable(nationalNumberTabDTO.getNationalNumber());
			logger.debug("SERVICE :- "+service+" - Transaction ID :- ["+txnId+"] "
					+ "LoyaltyId :- ["+nationalNumberTabDTO.getNationalNumber()+"] getting details from table "+tableName);
			customerProfileDetailsDAO = new CustomerProfileDetailsDAO();
			updateCount =customerProfileDetailsDAO.updateNationalIdInfo(service,txnId,tableName,nationalNumberTabDTO,profileInformationUpdateDTO.getNewNationalIdType());
			if(updateCount>0){
				isUpdated = true;
			}
		}catch(Exception e){
			logger.error("SERVICE :- "+service+" - Transaction ID :- ["+txnId+"] "
					+ "LoyaltyId :- ["+nationalNumberTabDTO.getLoyaltyID()+"] Exception occured ",e);
		}	
		finally{
			customerProfileDetailsDAO = null;
		}
		return isUpdated;
	}

}
