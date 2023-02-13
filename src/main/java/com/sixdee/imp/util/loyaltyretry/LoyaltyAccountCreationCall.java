package com.sixdee.imp.util.loyaltyretry;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sixdee.imp.dto.LoyaltyCreationFailedDTO;
import com.sixdee.imp.service.AccountManagement;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;

public class LoyaltyAccountCreationCall {

	SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public void loyaltyAccountCall(LoyaltyCreationFailedDTO creationFailedDTO)
	{
		
		AccountManagement accountManagement=new AccountManagement();
		AccountDTO accountDTO=new AccountDTO();
		accountDTO.setTransactionId(creationFailedDTO.getTransactionID());
		accountDTO.setRegisterNumbers(creationFailedDTO.getSubscriberNumber().toArray(new String[creationFailedDTO.getSubscriberNumber().size()]));
		accountDTO.setChannel(creationFailedDTO.getChannel());
		accountDTO.setTimestamp(dateFormat.format(new Date()));
		
		if(creationFailedDTO.getStatus()!=null)
		{
			Data data=new Data();
			data.setName("STATUS");
			data.setValue(creationFailedDTO.getStatus()+"");
			
			Data[] datas=new Data[1];
			datas[0]=data;
			
			accountDTO.setData(datas);
			
		}
		
		accountManagement.createLoyaltyAccount(accountDTO);
		
	}//loyaltyAccountCall
	
	
}
