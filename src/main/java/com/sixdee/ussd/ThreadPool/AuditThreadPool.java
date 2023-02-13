/**
 * 
 */
package com.sixdee.ussd.ThreadPool;

import java.util.LinkedList;

import com.sixdee.ussd.dto.TransactionHistoryDTO;

/**
 * @author Rahul K K
 *
 */
public class AuditThreadPool {
	
	private LinkedList<TransactionHistoryDTO> taskList = null;
	private int maxNoOFConnections = 0;
	private int newNoOfConns;
	private AuditThread[] auditThread = null;
	private AuditThread[] auditThreadTemp = null;
	public AuditThreadPool(int maxNoOFConnections) {
	    	this.maxNoOFConnections = maxNoOFConnections;
	    	taskList = new LinkedList<TransactionHistoryDTO>();
		createPool(taskList,maxNoOFConnections);
	}

private void createPool(LinkedList<TransactionHistoryDTO> taskList,
	    int maxNoOFConnections) {

	auditThread = new AuditThread[maxNoOFConnections];
	this.taskList = taskList;
	for (int i = 0; i < maxNoOFConnections; i++) {
	    boolean isThreadCreated = true;
	    while (true) {
		try {
		    auditThread[i] = new AuditThread(taskList, i);
		    auditThread[i].start();
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

	public void addTask(TransactionHistoryDTO notify) {
		synchronized (taskList) {
			taskList.add(notify);
			taskList.notify();
		}
		
	}
	
	public void setFlagFalse(){
	    int i=0;
	    while(i<maxNoOFConnections){
		auditThread[i].setFlagFalse();
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
	auditThreadTemp = new AuditThread[newNoOfConns];

	for (int i = 0; i < newNoOfConns; i++) {
	    auditThreadTemp[i] = auditThread[i];
	}

	for (int i = newNoOfConns; i < maxNoOFConnections; i++) {
	    auditThread[i].setFlagFalse();
	}

	auditThread = auditThreadTemp;

}


private void incrementThreadProc(){
//		int diffVal=newNoOfConns-noOfConnections;
	    auditThreadTemp  = new AuditThread[newNoOfConns];
		for(int i=0;i<maxNoOFConnections;i++) {
		    auditThreadTemp[i]=auditThread[i];
		}

		for(int i=maxNoOFConnections;i<newNoOfConns;i++) {

			boolean isThreadCreated=false;

			while(true) {

				try{
				    	auditThreadTemp[i] = new AuditThread(taskList, i);
					    auditThreadTemp[i].start();
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
		
		auditThread=auditThreadTemp;

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
