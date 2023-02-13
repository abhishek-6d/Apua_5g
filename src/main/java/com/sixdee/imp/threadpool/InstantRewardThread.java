/**
 * 
 */
package com.sixdee.imp.threadpool;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;

/**
 * @author Rahul K K
 *
 */
public class InstantRewardThread extends Thread {

	Logger logger = Logger.getLogger(InstantRewardThread.class);
	private LinkedList<RequestProcessDTO> taskList = null;
	private volatile boolean flag = true;
	
	public void setFlagFalse(){
	    flag = false;
	    this.interrupt();
	}
	public InstantRewardThread(LinkedList<RequestProcessDTO> taskList, int i) {
		this.taskList = taskList;
		setName("InstantReward_"+i);
	}
	
    public void run() {
	RequestProcessDTO notify = null;
	LMSWebServiceAdapter webServiceAdapter = null;
	while (flag) {
	    try {
		while (true) {
		    synchronized (taskList) {
			if (!taskList.isEmpty()) {
			    notify = taskList.remove();
			    break;
			} else {
			    try {
				taskList.wait();
			    } catch (InterruptedException e) {
				// TODO: handle exception
				break;
			    }
			}
		    }
		}
		if (notify != null) {
		   //logger.info("Removed From ThreadPool of Size ["+ThreadInitiator.instantRewardPool.getSize()+"] For Feature ["+notify.getFeatureName()+"]");
			webServiceAdapter = new LMSWebServiceAdapter();
			webServiceAdapter.callFeature(notify);

		}
	    } catch (Exception e) {
		
		logger.error("Exception thrown ", e);
	    }
	}
    }
	

}
