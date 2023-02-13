package com.sixdee.imp.request;

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



import java.util.HashMap;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountTypeDTO;
import com.sixdee.imp.vo.AccountConversionVO;



public class AccountConversionReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> AccountConversionReqAssm :: Method ==> buildAssembleReq ");
		AccountConversionVO accountConversionDTO = null;
		AccountTypeDTO accountTypeDTO = null;
		String channel = null;
		Data[] data = null;
		HashMap<String, String> optionsMap = new HashMap<String, String>();
		int currentAccountType = 0;
		int newAccountType = 0;
	//	String 
		//long accountNumber = 0;
		try{
			accountTypeDTO = (AccountTypeDTO) genericDTO.getObj();
			channel = accountTypeDTO.getChannel();
			accountConversionDTO=new AccountConversionVO();
			accountConversionDTO.setTransactionId(accountTypeDTO.getTransactionId());
			accountConversionDTO.setTimestamp(accountTypeDTO.getTimestamp());
			accountConversionDTO.setChannel(channel!=null?channel:"CRM");
			
			accountConversionDTO.setSubscriberNumber(accountTypeDTO.getSubscriberNumber());
			newAccountType = accountTypeDTO.getNewAccountType();
			accountConversionDTO.setNewTypeId(newAccountType);
			//accountConversionDTO.setCurrentType("Postpaid");

			
			accountConversionDTO.setAccountNumber(accountTypeDTO.getAccountNumber());
			data = accountTypeDTO.getData();
			for(Data d : data){
				optionsMap.put(d.getName(), d.getValue());
			}
			accountConversionDTO.setOptionsMap(optionsMap);
			currentAccountType=accountTypeDTO.getCurrentAccountType();
			if(currentAccountType==0)
				if(optionsMap.get("MIGRATON_REQ")!=null)
					currentAccountType = newAccountType==9?14:9;
			accountConversionDTO.setCurrentTypeId(accountTypeDTO.getCurrentAccountType());
			//accountConversionDTO.setcurrentType("Prepaid");
			accountConversionDTO.setStatusCode("SC0000");
			accountConversionDTO.setStatusDesc("Converted Successfully");
		} catch (Exception e) {
			logger.error("Exception occured ",e);
			e.printStackTrace();
		} finally {
			genericDTO.setObj(accountConversionDTO);
			accountConversionDTO = null;
		}

		return genericDTO;
	}

}
