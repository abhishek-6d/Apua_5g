/**
 * 
 */
package com.sixdee.imp.bo;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.CustomerProfileDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.ProfileInformationUpdateDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.service.AccountManagement;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.util.CustomerProfileDetailsUtil;

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
public class UpdateSubscriberInfoBO extends BOCommon{

	private static final Logger logger = Logger.getLogger(UpdateSubscriberInfoBO.class);
	@Override
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {
		ProfileInformationUpdateDTO profileInformationUpdateDTO = null;
		try{
			profileInformationUpdateDTO = (ProfileInformationUpdateDTO) genericDTO.getObj();
			logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Request in BO layer");
			profileInformationUpdateDTO = updateSubscriberProfileInfoBO(profileInformationUpdateDTO);
		}catch(CommonException e){
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Exception in BO layer "+e.getMessage());
			throw e;
		}
		return genericDTO;
	}
	private ProfileInformationUpdateDTO updateSubscriberProfileInfoBO(
			ProfileInformationUpdateDTO profileInformationUpdateDTO) throws CommonException{
		String subscriberNumber = null;
		CustomerProfileDetailsUtil customerProfileDetailsUtil = null;
		String txnId = null;
		ArrayList<NationalNumberTabDTO> nationalNumberTabDTOs = null;
		try{
			subscriberNumber = profileInformationUpdateDTO.getMsisdn();
			txnId = profileInformationUpdateDTO.getTransactionId();
			customerProfileDetailsUtil = new CustomerProfileDetailsUtil();
			SubscriberNumberTabDTO subscriberNumberTabDTO = customerProfileDetailsUtil.
					getSubscriberNumberInformation("UpdateSubscriberAccount",txnId,subscriberNumber);
			if(subscriberNumberTabDTO==null){
				throw new CommonException("Subscriber Not registered in Program");
			}
			nationalNumberTabDTOs = customerProfileDetailsUtil.getNationalIdForAccount("UpdateSubscriberAccount",txnId,subscriberNumberTabDTO.getLoyaltyID());
			if(nationalNumberTabDTOs==null || nationalNumberTabDTOs.size()==0){
				throw new CommonException("Subscriber Not registered in Program");
			}
			ArrayList<Object> resultList = checkProfileMatching(profileInformationUpdateDTO,nationalNumberTabDTOs);
			
			profileInformationUpdateDTO = performActivity(profileInformationUpdateDTO,resultList);
		}catch(Exception e){
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Exception in BO layer",e);
			throw new CommonException(e.getMessage());
		}
		finally{
			logger.fatal(profileInformationUpdateDTO.toString());
		}
		return profileInformationUpdateDTO;
	}
	private ProfileInformationUpdateDTO performActivity(
			ProfileInformationUpdateDTO profileInformationUpdateDTO, ArrayList<Object> resultList) {
		NationalNumberTabDTO nationalNumberTabDTO = null;
		int opCode = 0;
		CustomerProfileDetailsUtil customerProfileDetailsUtil = null;
		try{
			opCode = (Integer) resultList.get(0);
			if(opCode!=0){
				nationalNumberTabDTO = (NationalNumberTabDTO) resultList.get(1);
			}
			profileInformationUpdateDTO.setOpCode(opCode);
			logger.debug("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
	+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] opcode "+opCode);
			switch (opCode) {
			case 1://Go For Update
		//		nationalNumberTabDTO.setIdType(profileInformationUpdateDTO.getNewNationalIdType());
				customerProfileDetailsUtil = new CustomerProfileDetailsUtil();
				if(customerProfileDetailsUtil.updateNationalIdInfo("UpdateSubscriberAccount",profileInformationUpdateDTO.
						getTransactionId(),nationalNumberTabDTO,profileInformationUpdateDTO)){
					logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
			+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] update is success");
					profileInformationUpdateDTO.setStatusCode("SC0000");
					profileInformationUpdateDTO.setStatusDesc("SUCCESS");
				}
				break;
			case 2://call delete
				AccountDTO accountDTO = new AccountDTO();
				accountDTO.setTransactionId(profileInformationUpdateDTO.getTransactionId());
				accountDTO.setTimestamp(profileInformationUpdateDTO.getTimestamp());
				accountDTO.setChannel("CRM-Delete");
				accountDTO.setMoNumber(profileInformationUpdateDTO.getMsisdn());
				Data d = new Data();
				d.setName("status");
				d.setValue("3");
				Data[] data= new Data[1];
				data[0]=d;
				accountDTO.setData(data);
				AccountManagement accountManagement = new AccountManagement();
				ResponseDTO responseDTO = accountManagement.deleteLoyaltyAccount(accountDTO);
				profileInformationUpdateDTO.setStatusCode(responseDTO.getStatusCode());
				profileInformationUpdateDTO.setStatusDesc(responseDTO.getStatusDescription());
				break;
			default://do Nothing
				break;
			}
		}finally{
			
		}
		return profileInformationUpdateDTO;
	}
	private ArrayList<Object> checkProfileMatching(
			ProfileInformationUpdateDTO profileInformationUpdateDTO,
			ArrayList<NationalNumberTabDTO> nationalNumberList) {
		ArrayList<Object> resultList = null;
		int isProfileMatching = 0;
		NationalNumberTabDTO matchingIdDTO = null;
		try{
			logger.debug("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Performing profile matching");
			resultList = new ArrayList<Object>();
			/*
			 * 2 cases has to be checked
			 * 1.old national Id Type and new national Id Type is same
			 * 	1.1. check in table this national id type is present , if not there do nothing 
			 *  1.1.1. if there , then check numbers are different , if yes call the delete request and audit in table
			 * 2.Old national Id Type and new national Id Type is different
			 *  2.1 Check old national id is present in table, if not there do nothing
			 *  2.1.1 if there , then check numbers are same 
			 *  2.1.1.1 if yes do nothing
			 *  2.1.1.2 if no delete request and audit in table
			 *  Return Type
			 *  0-Do nothing
			 *  1-Update IdType
			 *  2-delete id
			 */
			if(profileInformationUpdateDTO.getOldNationalIdType().equals(profileInformationUpdateDTO.getNewNationalIdType())){
				for(NationalNumberTabDTO nationalNumberTabDTO:nationalNumberList){
					logger.debug("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
						+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] NationalNumber IdType : "+nationalNumberTabDTO.getIdType()+""
						+" Old IdType"+profileInformationUpdateDTO.getOldNationalIdType()+" old Number :"+nationalNumberTabDTO.getNationalNumber()+""
						+ " CRM OldNumber "+profileInformationUpdateDTO.getOldNationalId()+" CRM New IDType "+profileInformationUpdateDTO.getNewNationalIdType()
						+" CRM New Number "+profileInformationUpdateDTO.getNewNationalId());
				if(nationalNumberTabDTO.getIdType().equals(profileInformationUpdateDTO.getOldNationalIdType())){
					if(nationalNumberTabDTO.getNationalNumber()!=profileInformationUpdateDTO.getNewNationalId()){
						logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
				+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Old Id Type differs and national number is different ,"
						+ " so delete account");
						matchingIdDTO = nationalNumberTabDTO;
						isProfileMatching=2;
						break;
					}
				}
				}
			}else{
				for(NationalNumberTabDTO nationalNumberTabDTO:nationalNumberList){
					logger.debug("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
							+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] NationalNumber IdType : "+nationalNumberTabDTO.getIdType()+""
							+" Old IdType"+profileInformationUpdateDTO.getOldNationalIdType()+" old Number :"+nationalNumberTabDTO.getNationalNumber()+""
							+ " CRM OldNumber "+profileInformationUpdateDTO.getOldNationalId()+" CRM New IDType "+profileInformationUpdateDTO.getNewNationalIdType()
							+" CRM New Number "+profileInformationUpdateDTO.getNewNationalId());
							
					if(nationalNumberTabDTO.getIdType().equals(profileInformationUpdateDTO.getOldNationalIdType())){
						if(nationalNumberTabDTO.getNationalNumber().equals(profileInformationUpdateDTO.getNewNationalId())){
							//update id type
							logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Old Id Type matches and national number is same ,"
							+ " so need to update id type");
							matchingIdDTO = nationalNumberTabDTO;
							isProfileMatching = 1;
							break;
						}else{
							//Send delete request
							logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateDTO.getMsisdn()+"] Old Id Type matches and national number is different ,"
							+ " so delete account");
							matchingIdDTO = nationalNumberTabDTO;
							isProfileMatching = 2;
							break;
						}
					}
				
				}
			}
		}finally{
			resultList.add(isProfileMatching);
			resultList.add(matchingIdDTO);
		}
		return resultList;
	}

}
