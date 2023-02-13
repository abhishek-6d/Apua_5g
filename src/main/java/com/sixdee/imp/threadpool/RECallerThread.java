/**
 * 
 */
package com.sixdee.imp.threadpool;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.util.ThirdPartyCall;

/**
 * @author Rahul K K
 *
 */
public class RECallerThread extends Thread {

	Logger logger = Logger.getLogger(RECallerThread.class);
	private LinkedList<RERequestHeader> taskList = null;
	private volatile boolean flag = true;
	
	public void setFlagFalse(){
	    flag = false;
	    this.interrupt();
	}
	public RECallerThread(LinkedList<RERequestHeader> taskList, int i) {
		this.taskList = taskList;
		setName("RECALLER_"+i);
	}
	
 
	public void run() {
	RERequestHeader response = null;
	while (flag) {
	    try {
		while (true) {
		    synchronized (taskList) {
			if (!taskList.isEmpty()) {
				response = taskList.remove();
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
		if (response != null) {
		   //logger.info("Removed From ThreadPool of Size ["+ThreadInitiator.reCallerPool.getSize()+"] For request ["+response.getRequestId()+"]");
			ThirdPartyCall tpCall = new ThirdPartyCall();
			tpCall.makeRECall(response);

		}
	    } catch (Exception e) {
		
		logger.error("Exception thrown ", e);
	    }finally{
	    	response = null;
	    }
	}
    }
	

}
