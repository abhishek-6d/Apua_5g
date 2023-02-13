package com.sixdee.imp.simulator.threadPool;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.imp.simulator.bo.PromotionSimulatorBL;

public class PromotionWorkerThread extends Thread {

	Logger logger = Logger.getLogger(PromotionWorkerThread.class);
	private LinkedList<String> taskList = null;
	private volatile boolean flag = true;
	
	public void setFlagFalse()
	{
	    flag = false;
	    this.interrupt();
	}
	public PromotionWorkerThread(LinkedList<String> taskList, int i) 
	{
		this.taskList = taskList;
		setName("PromotionWorkerThread_"+i);
	}
	
 
	public void run() 
	{
		String subscriberNumber = null;
		PromotionSimulatorBL bl = new PromotionSimulatorBL();
		while (flag) 
		{
			try 
			{
				while (true) 
				{
					synchronized (taskList) 
					{
						if (!taskList.isEmpty()) 
						{
							subscriberNumber = taskList.remove();
							break;
						} 
						else 
						{
							try 
							{
								taskList.wait();
							} 
							catch (InterruptedException e) 
							{
								break;
							}
						}
					}
				}
				if (subscriberNumber != null) 
				{
					logger.info("Removed From promotion Thread pool");
					bl.populateForPromotion(subscriberNumber);
					//bl.callCRMAndUpdateInMasterTable(oldNewAcdto); //commented for testing

				}
			} catch (Exception e) {

				logger.error("Exception thrown ", e);
			}
			finally
			{
				subscriberNumber = null;
			}
		}
	}
	
}
