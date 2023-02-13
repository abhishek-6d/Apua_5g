/**
 * 
 */
package com.sixdee.ussd.manager;

import java.util.HashMap;

import com.sixdee.ussd.ThreadPool.AuditThreadPool;
import com.sixdee.ussd.util.AppCache;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ThreadInitiator {
    
   


    private static ThreadInitiator threadInitiator = null;

    
    public static AuditThreadPool auditPool = null;

    private static boolean startFlag = true;
	
	/**
     * @return the startFlag
     */
    public static boolean isStartFlag() {
        return startFlag;
    }



    /**
     * @param startFlag the startFlag to set
     */
    public static void setStartFlag(boolean startFlag) {
        ThreadInitiator.startFlag = startFlag;
        auditPool.setFlagFalse();

    }



	@SuppressWarnings("unchecked")
	public void initiateThreads(){
	    	HashMap<String,String> threadMap = null;
	    	
	    	threadMap = ((HashMap<String, String>)AppCache.util.get("USSD_MANAGER"));
		
	    	int size = threadMap.get("AUDIT_THREAD_SIZE")!=null ? Integer.parseInt(threadMap.get("AUDIT_THREAD_SIZE")) :3;
		
	    	auditPool = new AuditThreadPool(size);
	}
	
	
	
	public static ThreadInitiator getInstance(){
		if(threadInitiator == null){
			threadInitiator = new ThreadInitiator();
		}
		return threadInitiator;
	}

}
