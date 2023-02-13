/**
 * 
 */
package com.sixdee.imp.threadpool;

import java.util.LinkedList;

import com.sixdee.imp.dto.parser.RERequestHeader;

/**
 * @author Rahul K K
 *
 */
public class ReCallerPool {

	private LinkedList<RERequestHeader> taskList = null;
	private int maxNoOFConnections = 0;
	private int newNoOfConns;
	private RECallerThread[] csvThread = null;
	private RECallerThread[] csvThreadTemp = null;
 	public ReCallerPool(int maxNoOFConnections) {
 	    	this.maxNoOFConnections = maxNoOFConnections;
 	    	taskList = new LinkedList<RERequestHeader>();
		createPool(taskList,maxNoOFConnections);
	}

    private void createPool(LinkedList<RERequestHeader> taskList,
	    int maxNoOFConnections) {

	csvThread = new RECallerThread[maxNoOFConnections];
	this.taskList = taskList;
	for (int i = 0; i < maxNoOFConnections; i++) {
	    boolean isThreadCreated = true;
	    while (true) {
		try {
		    csvThread[i] = new RECallerThread(taskList, i);
		    csvThread[i].start();
		    isThreadCreated = true;
		} catch (Exception e) {
		    /**
		     * if exception occurs don't do anything
		     */
		}
		if (isThreadCreated)
		    break;
	    }
	}

    }

	public int getSize() {
		
		return taskList.size();
	}

	public void addTask(RERequestHeader subscriberRequest) {
		synchronized (taskList) {
			taskList.add(subscriberRequest);
			taskList.notify();
		}
		
	}
	
	public void setFlagFalse(){
	    int i=0;
	    while(i<maxNoOFConnections){
		csvThread[i].setFlagFalse();
		i++;
	    	}
	   }
	
	public void changeThreadConn(){

		switch (compPrevConn()) {

		case -1:
			decrementThreadProc();
			break;
		case 0:
			break;
		case 1:
			incrementThreadProc();
			break;
		default:
			break;

		}
	}

    private void decrementThreadProc() {
	// int diffVal=noOfConnections-newNoOfConns;
	csvThreadTemp = new RECallerThread[newNoOfConns];

	for (int i = 0; i < newNoOfConns; i++) {
	    csvThreadTemp[i] = csvThread[i];
	}

	for (int i = newNoOfConns; i < maxNoOFConnections; i++) {
	    csvThread[i].setFlagFalse();
	}

	csvThread = csvThreadTemp;

    }

    
    private void incrementThreadProc(){
//		int diffVal=newNoOfConns-noOfConnections;
	    csvThreadTemp  = new RECallerThread[newNoOfConns];
		for(int i=0;i<maxNoOFConnections;i++) {
		    csvThreadTemp[i]=csvThread[i];
		}

		for(int i=maxNoOFConnections;i<newNoOfConns;i++) {

			boolean isThreadCreated=false;

			while(true) {

				try{
				    	csvThreadTemp[i] = new RECallerThread(taskList, i);
					    csvThreadTemp[i].start();
					isThreadCreated=true;
				}catch (Exception e) {
					/**
					 * if exception occurs
					 * don't do anything
					 */
				}
				if(isThreadCreated)
					break;
			}
		}
		
		csvThread=csvThreadTemp;

	}

	
	/** 
	 * Check if new more than old or less than old
	 * @return
	 */
	private int compPrevConn(){

		int isChangeReq=0;
		int val=newNoOfConns-maxNoOFConnections;

		if(val<0) {
			isChangeReq=-1;
		}else if(val>0){
			isChangeReq=1;
		}

		return isChangeReq;
	}
	/**
	 * @return the newNoOfConns
	 */
	public int getNewNoOfConns() {
	    return newNoOfConns;
	}

	/**
	 * @param newNoOfConns the newNoOfConns to set
	 */
	public void setNewNoOfConns(int newNoOfConns) {
	    this.newNoOfConns = newNoOfConns;
	}

	
}
