package com.sixdee.imp.log;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

public class CDRLog extends FileAppender {

	private boolean initialized = false;
	private String baseFileName = null;
	private int numLines = 0;
	private long startFile = 0;
	private int maxRecords = 0;
	private int maxSec = 0;
	private String dateFormat;
	private String file;
	private String suffix;
	private String tempSuffix;
	private static final Log log = LogFactory.getLog(CDRLog.class);
	private static final Timer timer = new Timer(true);
	
	/**
	 * 
	 * write to ActivityLog 
	 * 
	 * @param event logging event invoked.
	 *            
	 */

	@Override
	protected synchronized void subAppend(LoggingEvent event) {
		if(!initialized || checkStatus())
		{
			createNewFile();
		}
/*		synchronized (this) {
			super.subAppend(event);
			numLines++;
		}
*/
		super.subAppend(event);
		numLines++;
	
	}
	
	/**
	 * 
	 * create a new ActivityLog File 
	 * 
	 */
	public void createNewFile()
	{	
		try {
			baseFileName = file +  new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
			super.setFile(getTmpFileName());
			super.activateOptions();
			startFile = System.currentTimeMillis();
			log.debug("Created Activity Log: " + getTmpFileName());
			startTimer();
			initialized = true;
		} catch (Exception e) {
			log.error("Error in configuration of log4j params,unable to create ActivityLog file");
		}
	}
	
	/**
	 * 
	 * Activity Log status check.
	 * It will be closed as per configured time or configured max. no. of records
	 * 
	 * @return boolean true indicates current activity log file has been closed         
	 */
	private boolean checkStatus()
	{
		boolean flag = false;
		if ((maxSec==0 && numLines==maxRecords) || (maxRecords==0 && System.currentTimeMillis()>=maxSec*1000+startFile)||
			(maxSec!=0 && maxRecords!=0 && (numLines == maxRecords || System.currentTimeMillis()>=maxSec*1000+startFile))) 
		{
			rollOver();			
			flag=true;
		}	
		return flag;
	}
	
	/**
	 * 
	 * invokes File Appender's activateOptions() which controls the creation of log files. 
	 * 
	 */
	@Override
	public void activateOptions()
	{ 
		if (maxSec == 0 && maxRecords == 0) {
			log.info("ActivityLog: Time duration and Max records both are zeroed, not creating AL files");
		} else {
			super.setFile("dummy");
			super.activateOptions();
		}
	}
	
	/**
	 * 
	 * Close and rename the current ActivityLog file and reset counter and timestamp.
	 * 	         
	 */
	private void rollOver() {	
		
		    log.debug("Closing and Renaming Activity Log File: " + getTmpFileName());
			closeFile();	
			boolean loop = true;
			boolean flag = false;
			int count=0;
			int counter = 0;
			do {
				if(!loop)
				{
					count = counter;
					if(!(new File(getReportFileName(true,count)).exists()))
						flag = (new File(getTmpFileName())).renameTo(new File(getReportFileName(true,count)));
					
					counter++;
				}
				else
				{
					count = counter;
					if(!(new File(getReportFileName(false,count)).exists()))
						flag = (new File(getTmpFileName())).renameTo(new File(getReportFileName(false,count)));
					
					counter++;
				}
				loop = flag; 
			}while(!flag);
			numLines = 0;
			startFile=0;
			initialized = false;			
	}

	private String getReportFileName(boolean flag,int counter) {
		if(flag)
			return baseFileName+"."+counter+"."+suffix;
		else
			return baseFileName+"."+suffix;
	}

	private String getTmpFileName() {
		return baseFileName+"."+tempSuffix;
	}
	
	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public void setMaxSec(int maxSec) {
		this.maxSec = maxSec;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}	
	
	@Override
	public void setFile(String file) {
			this.file=file;		
	}
	/**
	 * 
	 * start the timer to monitor the time duration
	 *            
	 */
	private void startTimer() {
		if (maxSec != 0) {
			TimerTask timertask = new MonitorTime();
			timer.schedule(timertask, maxSec * 1000);
		}
	}
	/**
	 * 
	 * Private inner class,will monitor time duration and close the 
	 * Activity Log file if the configured time has elapsed
	 * 	         
	 */
	private class MonitorTime extends TimerTask {

		public void run() {
			// time over
			if (System.currentTimeMillis()>= maxSec * 1000 + startFile) {
				rollOver();

			}
		}
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setTempSuffix(String tempSuffix) {
		this.tempSuffix = tempSuffix;
	}
	
}