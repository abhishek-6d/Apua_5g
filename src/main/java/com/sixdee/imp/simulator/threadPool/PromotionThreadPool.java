package com.sixdee.imp.simulator.threadPool;

import java.util.LinkedList;


public class PromotionThreadPool {

	private int noOfConnections;
	private LinkedList<String> taskList = null;
	private PromotionWorkerThread[] w;
	
	public PromotionThreadPool(int c) {
		noOfConnections = c;		
		taskList = new LinkedList<String>();
		createPool();
	}
	
	private void createPool()  {
		w =  new PromotionWorkerThread[noOfConnections];
		for(int i=0;i<noOfConnections;i++) {
			while(true) {
				w[i] = new PromotionWorkerThread(taskList, i);
				w[i].start();
				break;
			}
		}
	}
	
	public void addTask(String subscriberNumber) {
		synchronized (this) {
			synchronized(taskList) {
				taskList.addLast(subscriberNumber);
				taskList.notify();
			}
		}
		//System.out.println("taskList "+taskList.size());
	}
	
	public void shutdown() {
		for(int i=0;i<noOfConnections;i++) {
			w[i].setFlagFalse();
		}
	}

	public int getNoOfConnections() {
		return noOfConnections;
	}
	
}
