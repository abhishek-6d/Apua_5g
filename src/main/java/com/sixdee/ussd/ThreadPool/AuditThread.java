/**
 * 
 */
package com.sixdee.ussd.ThreadPool;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.TransactionHistoryDTO;
import com.sixdee.ussd.manager.FileReconUtilDAO;
import com.sixdee.ussd.manager.ThreadInitiator;

/**
 * @author Rahul K K
 *
 */
public class AuditThread extends Thread {
	
	Logger logger = Logger.getLogger(AuditThread.class);

	private LinkedList<TransactionHistoryDTO> taskList = null;
	private volatile boolean flag = true;
	
	public void setFlagFalse(){
	    flag = false;
	    this.interrupt();
	}
	public AuditThread(LinkedList<TransactionHistoryDTO> taskList, int i) {
		this.taskList = taskList;
		setName("AUDIT_THREAD_"+i);
	}
	
public void run() {
	TransactionHistoryDTO notify = null;
	FileReconUtilDAO fileReconUtilDAO = null;
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
		    logger.info("File Removed for audit file processing :- [" + notify.getTransactionId()+"] Session Id ["+notify.getSessionId()+"] Size Of Pool ["+ThreadInitiator.auditPool.getSize()+"]");
		    fileReconUtilDAO = new FileReconUtilDAO();
		    fileReconUtilDAO.processMessage(notify,notify.getStage() );
		}
	    } catch (Exception e) {
		
		logger.error("Exception thrown ", e);
	    }
	}
}
	

}
