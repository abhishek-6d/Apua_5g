package com.sixdee.imp.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;



/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2007
 * All Rights Reserved.
 */

/**
 * @author Sumanth Kalyan
 * @version 1.0.0.0
 * @since Dec 4, 2007
 * 
 * Development History
 * 
 * Date Author Description ------------------------------------------------------------------------------------------ Dec 4, 2007 Sumanth Kalyan
 * ------------------------------------------------------------------------------------------
 */
public class HeartBeatThread {
	private static String		playFileLocation	= null;				// Location of .exe
	private static String		appId				= null;				// Application ID
	private static String		processMgrId		= null;				// Process Manager ID
	private static int			processId			= 0;					// Process Id
	private static String		command				= null;
	private static int			timeInterval;
	private Timer				timer;
	private static boolean		isTrue				= true;

	public  static Logger	logger	= Logger.getLogger(HeartBeatThread.class.getName());
	public static void cacheMap() throws Exception {
		try {
			loadProperties();
		} catch (Exception e) {
			throw e;
		}
	}

	private static void loadProperties() throws Exception {

		try {
			playFileLocation = Cache.getCacheMap().get("HB.EXELOCATION");
			appId = Cache.getCacheMap().get("HB.APPLICATIONID");
			processMgrId = Cache.getCacheMap().get("HB.APPMGERID");
			timeInterval = Integer.parseInt(Cache.getCacheMap().get("HB.TIMEINTERVAL").toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	static {
		try {
			cacheMap();
		} catch (Exception e) {
			isTrue = false;
			
			logger.error("Not abble to send heartbeat to AppManager.");
		}
		processId = getPID();
	}

	public HeartBeatThread() {
		command = playFileLocation + " " + appId + " " + processMgrId + " " + processId;
		if (isTrue) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					BufferedReader brInput = null;
					BufferedReader brError = null;
					Process p = null;
					try {
						logger.info("HeartBeat Command >>>>>>>>>>>>>>>> : "+command);						
						String[] callAndArgs = { "sh", "-c", command };

						Runtime rt = Runtime.getRuntime();
						p = rt.exec(callAndArgs, null, null);
												
						brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

						String s;
						while ((s = brError.readLine()) != null) {
							logger.error("brError >> " + s);
						}
						brError.close();
						
						brInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

						while ((s = brInput.readLine()) != null) {
							logger.info("brInput >> " + s);
						}
						brInput.close();
						
//						p.waitFor();
						
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (brError != null)
								brError.close();
					
							if (brInput != null)
								brInput.close();
						} catch (IOException ioe) {
							//
						}
					}
				}
			}, 0, 1000 * timeInterval);
		}
	}
	
	
	public static int getPID() {
		int pid = 0;

		String pidString=ManagementFactory.getRuntimeMXBean().getName();
		//System.out.println("PID Value "+pidString);
		TokenizeString tokenizeString =new TokenizeString(pidString,'@');
		String[] tokens=tokenizeString.getTokens();
		pid =Integer.parseInt(tokens[0]);
		logger.info("-----Process ID---->" + pid);
		
		return pid;
	}


	public void shutdown() {
		if (timer != null)
			timer.cancel();
	}
	
}
