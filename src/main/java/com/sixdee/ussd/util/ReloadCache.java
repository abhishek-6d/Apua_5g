package com.sixdee.ussd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sixdee.imp.ver.Version;


public class ReloadCache extends TimerTask{
	private static final Logger logger = Logger.getLogger(ReloadCache.class);
	public void run() {
		logger.info("Cache Reloading process is Started :" );
		Version ver = new Version();
		AppCache.reinitializeCache();
		String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss:S";
		Calendar cal2=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String Time1 =sdf.format(cal2.getTime());
		logger.info("Cache Reloading process is over :" +Time1);
		logger.info("----------------------------------------------------");
	}
}
