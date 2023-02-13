/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2006
 * All Rights Reserved.
 */

package com.sixdee.imp.threadpool;

/**
 * @author Sumanth Kalyan
 * @version 1.9.0.0
 * @since Dec 13, 2006
 *
 * Development History
 * 
 * Date					Author				Description
 * ------------------------------------------------------------------------------------------
 * Jun 04, 2007			Sumanth Kalyan		Wave1 >> Wave2 >> Wave3	
 * ------------------------------------------------------------------------------------------
 * Added this Thread Pool Code to Replace the JDK 1.5 Thread Pool
 * 
 */

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.CreateAccountDTO;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;



public class RequestProcessWorkerThread extends Thread {
	
	private Logger logger=Logger.getLogger(RequestProcessWorkerThread.class);

	private LinkedList taskList;
	private volatile boolean flag = true;
	private RequestProcessThreadPool pool;
	
	public RequestProcessWorkerThread(LinkedList taskList, int threadNo,RequestProcessThreadPool pool) {
		super("RequestProcessWorkerThread_"+threadNo);
		this.taskList = taskList;
		logger.info("RequestProcessWorkerThread "+getName());
		this.pool=pool;
	}
	
	public void setFlagFalse() {
		flag = false;
		this.interrupt();
	}
	
	public void run() {
		RequestProcessDTO requestProcessDTO= null;
		LMSWebServiceAdapter webServiceAdapter = null;
		while (flag) {
			synchronized (taskList) {
				while (true) {
					if (!taskList.isEmpty()) {
						requestProcessDTO =(RequestProcessDTO)taskList.removeFirst();
						break;
					} else {
						try {
							taskList.wait();
						} catch (InterruptedException e) {
							break;
						}
					}
				}
			}
	
			try {
				if (requestProcessDTO != null) {
					logger.info(getName()+" Final Request : "+requestProcessDTO.getFeatureName()+" Remaining Thread Size "+pool.size());
					webServiceAdapter = new LMSWebServiceAdapter();
					webServiceAdapter.callFeature(requestProcessDTO);
					logger.info(getName()+"  OVer");
				}
			}catch (Error e) {
				logger.error("In "+getName()+" run() Exception" ,e);
			} 
			catch(Exception e) {
				logger.error("In "+getName()+" run() Exception" ,e);
				e.printStackTrace();
			}finally{
				requestProcessDTO=null;
				webServiceAdapter=null;
			}
		}
	}
}