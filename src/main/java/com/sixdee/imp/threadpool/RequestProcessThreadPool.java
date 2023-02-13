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
 * Date Author Description
 * ------------------------------------------------------------------------------------------
 * Jun 04, 2007 Sumanth Kalyan Wave1 >> Wave2 >> Wave3
 * ------------------------------------------------------------------------------------------
 * Added this Thread Pool Code to Replace the JDK 1.5 Thread Pool
 * 
 */

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.RequestProcessDTO;

public class RequestProcessThreadPool {

	private int noOfConnections;

	private LinkedList<RequestProcessDTO> taskList = null;

	private RequestProcessWorkerThread[] w;

	private static Logger logger = Logger
			.getLogger(RequestProcessThreadPool.class);

	public RequestProcessThreadPool(int c) {
		noOfConnections = c;
		taskList = new LinkedList<RequestProcessDTO>();
		createPool();
	}

	private void createPool() {
		w = new RequestProcessWorkerThread[noOfConnections];
		for (int i = 0; i < noOfConnections; i++) {
			while (true) {
				w[i] = new RequestProcessWorkerThread(taskList, i,this);
				w[i].start();
				break;
			}
		}
	}

	public void addTask(RequestProcessDTO requestProcessDTO) {
		synchronized (this) {
			synchronized (taskList) {
				taskList.addLast(requestProcessDTO);
				taskList.notify();
			}
		}
		// System.out.println("taskList "+taskList.size());
		logger.info("Request Thread Size " + size());
	}

	public void shutdown() {
		for (int i = 0; i < noOfConnections; i++) {
			w[i].setFlagFalse();
		}
	}
	
	public int size()
	{
		int i =0;
		synchronized (taskList) {
			i=taskList.size();
		}
		return i;
	}

	public int getNoOfConnections() {
		return noOfConnections;
	}

}
