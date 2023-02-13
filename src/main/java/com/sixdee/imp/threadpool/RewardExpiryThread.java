package com.sixdee.imp.threadpool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;

import com.sixdee.imp.bo.RewardExpiryBL;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.config.ContextLoaderServlet;

public class RewardExpiryThread extends Thread {

	// this is thread is to expiry the points based on the time give in
	// system.properties file this is for monthly expiry
	private Logger log = Logger.getLogger(RewardExpiryThread.class);
	public void run() {
		boolean flag=true;
		RewardExpiryBL rewardExpiryBL = new RewardExpiryBL();
		Calendar cal = null;
		try {
			while (true) {
				cal = Calendar.getInstance();
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
				String expiryTime = Cache.getCacheMap().get(
						"REWARD_EXPIRY_THREAD_TIME");
				String currentDate = sdf.format(cal.getTime());
				String[] arr = expiryTime.split("##");
				//if (currentDate.compareTo(String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH))) == 0)
				if(currentDate.compareTo(arr[0])==0)
				{
					String currentHour = sdf1.format(cal.getTime());
				
					if (currentHour.compareTo(arr[1]) == 0 && flag==true) {		
						log.info(" Calling expiry points from [RewardExpiryThread]");
						rewardExpiryBL.expiryPoints();
						flag=false;
						
					} else {
						SimpleDateFormat sdfm=new SimpleDateFormat("mm");					
						int min=59-Integer.parseInt(sdfm.format(cal.getTime()));
						min++;
						log.info("hours:"+sdf1.format(cal.getTime())+"current mins:"+sdfm.format(cal.getTime())+"Thread will sleep for:"+min+" minutes");
						Thread.sleep(min * 60 * 1000);
					}
				} else {
					flag=true;
					int time=23-Integer.parseInt(sdf1.format(cal.getTime()));
					time++;
					log.info("Todays date is :"+sdf.format(cal.getTime())+" So RewardExpiry Thread will sleep for the day It will run only on:"+cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					Thread.sleep(time * 60 * 60 * 1000);
				}
			}
		} catch (Exception e) {
			log.error(""+e);
			e.printStackTrace();
		}

	}

}
