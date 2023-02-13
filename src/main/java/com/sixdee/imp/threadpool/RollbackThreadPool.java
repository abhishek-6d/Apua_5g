package com.sixdee.imp.threadpool;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.RedemptionDTO;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.ReserveAndCommitReqDto;

public class RollbackThreadPool {
	Logger logger = Logger.getLogger(RollbackThreadPool.class);

	private int noOfConnections = 0;
	private LinkedList<ReserveAndCommitReqDto> taskList = null;
	private RollbackProcessWorkerThread[] w;

	public RollbackThreadPool(int c) {
		noOfConnections = c;
	    	
	    	taskList = new LinkedList<ReserveAndCommitReqDto>();
	createPool();
}
	private void createPool() {
		w = new RollbackProcessWorkerThread[noOfConnections];
		for (int i = 0; i < noOfConnections; i++) {
			while (true) {
				w[i] = new RollbackProcessWorkerThread(taskList, i,this);
				w[i].start();
				break;
			}
		}
	}
	public void addTask(ReserveAndCommitReqDto requestProcessDTO) {
		synchronized (this) {
			synchronized (taskList) {
				taskList.addLast(requestProcessDTO);
				taskList.notify();
			}
		}
		// System.out.println("taskList "+taskList.size());
		
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
