package com.sixdee.imp.threadpool;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.LoyaltyExpiryDAO;

public class LoyaltyExpiryTherad extends Thread {

	private Logger logger=Logger.getLogger(LoyaltyExpiryTherad.class);
	
	public void run() {
		
		boolean flag=true;
		LoyaltyExpiryDAO expiryDAO = new LoyaltyExpiryDAO();
		logger.info("Running loyalty expiry starter thread");
		while(true)
		{
			Calendar cal = null;
			
			try{
				cal = Calendar.getInstance();
			    int configureHour = Integer.parseInt(Cache.getCacheMap().get("LOYALTY_EXPIRY_THREAD_TIME"));	
			    logger.debug("SOMESH +++++++++++>>>>>>>"+cal.getTime());
				if(cal.get(Calendar.HOUR_OF_DAY) == configureHour)
				{ 
					logger.info("Hours are equall");
					logger.info("flag value"+flag);
					if(flag)
					{
						logger.info("calling startExpiry()");
						expiryDAO.startExpiry();
						flag=false;
					}else{
						Thread.sleep(5*60*1000);						
					}
				}else{
					flag=true;
					//	Thread is going to sleep 
					 Thread.sleep(1*60*60*1000);
				}
				
			}catch (Exception e) {
				logger.error("Exception=>",e);
			}finally{
				cal = null;
			}
		}//while
		
		
		
	}//run
	
}
