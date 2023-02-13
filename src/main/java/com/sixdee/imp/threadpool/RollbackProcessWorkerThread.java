package com.sixdee.imp.threadpool;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.DeleteAccountBL;
import com.sixdee.imp.bo.ReserveAndCommitBL;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.ReserveAndCommitReqDto;
import com.sixdee.imp.service.LMSWebServiceAdapter;

public class RollbackProcessWorkerThread extends Thread {
	private Logger logger=Logger.getLogger(RollbackProcessWorkerThread.class);

	private LinkedList<GenericDTO> taskList = null;
	private volatile boolean flag = true;
	private RollbackThreadPool pool;

	public RollbackProcessWorkerThread(LinkedList taskList, int threadNo,RollbackThreadPool pool) {
		super("RollbackThreadPool"+threadNo);
		this.taskList = taskList;
		logger.info("RequestProcessWorkerThread "+getName());
		this.pool=pool;
	}
	
	public void run() {
		ReserveAndCommitBL bl=null;
		GenericDTO request=null;
		
		while (flag) {
		    try {
			while (true) {
						//synchronized (this) {
							synchronized (taskList) {
								if (!taskList.isEmpty()) {								
									request = taskList.remove();
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
			if (request != null) {			
				bl=new ReserveAndCommitBL();
				bl.buildProcess(request);
				}
		    } catch (Exception e) {		
			logger.error("Exception thrown ", e);
		    }finally{
		    	request = null;
		    	bl=null;	
		    	
		    }
		}
	    }
	public void setFlagFalse() {
		flag = false;
		this.interrupt();
	}
}
