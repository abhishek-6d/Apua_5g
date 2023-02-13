package com.sixdee.imp.util.loyaltyretry;

import java.util.List;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.LoyaltyCreationFailedDAO;
import com.sixdee.imp.dto.LoyaltyCreationFailedDTO;

public class LoyaltyAccountRetryThread extends Thread{

	private boolean flag=false;
	
	@Override
	public synchronized void start() {
		super.start();
		flag=true;
	}
	
	@Override
	public void run() {
	
		
		while(true)
		{
			try{
			
				LoyaltyCreationFailedDAO creationFailedDAO=new LoyaltyCreationFailedDAO();
				LoyaltyAccountCreationCall accountCreationCall=new LoyaltyAccountCreationCall();
				
				List<LoyaltyCreationFailedDTO> list=creationFailedDAO.getLoyaltyCreationFailed();
				
				if(list!=null)
				{
					// Calling one by one Transaction 
					for(LoyaltyCreationFailedDTO creationFailedDTO:list)
					{
						accountCreationCall.loyaltyAccountCall(creationFailedDTO);
					}
					
					
					// Update the Complete Status fro those Transaction
					creationFailedDAO.updateLoyaltyCreationFailed(list);
				}
			
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				
				LoyaltyAccountRetryThread.sleep(Long.parseLong(Cache.getCacheMap().get("CRM_FAILED_RETRY_INTERVAL")));
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}//while
		
		
	}// run
	
}
