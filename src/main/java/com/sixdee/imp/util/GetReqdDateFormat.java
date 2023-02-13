package com.sixdee.imp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Robin P Thomas
 * @version 1.0.0.0
 * @since Dec 10, 2009
 * 
 */
public class GetReqdDateFormat {

	private static long transactionID=1001;

	private static long currentDate=0;
	/*
	 * public static void main(String[] args) {
	 * 
	 * System.out.println(getDateTime("ddMMyyyyHHmmss")); }
	 */

	public synchronized String genrateTransactionID() {
		long date=GetReqdDateFormat.getCurrentDate();
		if(currentDate!=date){
			currentDate=date;
			transactionID=1001;
		}
		return currentDate+""+transactionID++;
	}
	
	public static String getDate(int value) {
		String dateformat = "yyyyMMdd";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat(dateformat);
		cal.add(Calendar.DAY_OF_MONTH, value);
		String todaysDate = null;
		todaysDate = date.format(cal.getTime());
		return todaysDate;
	}
	
	public static long getCurrentDate() {
		String dateformat = "yyyyMMdd";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat(dateformat);
		String todaysDate = date.format(cal.getTime());
		long lDate=Long.parseLong(todaysDate);
		return lDate;
	}

	public synchronized static String getDateTime(String Dateformat) {
		// String dateformat="MM/dd/yyyy HH:mm:ss ";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat(Dateformat);
		String todaysDate = null;
		todaysDate = date.format(cal.getTime());
		return todaysDate;
	}
	
	
	public synchronized static String getYesterdayDateTime(String Dateformat) {
		// String dateformat="MM/dd/yyyy HH:mm:ss ";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat date = new SimpleDateFormat(Dateformat);
		String todaysDate = null;
		todaysDate = date.format(cal.getTime());
		return todaysDate;
	}
	
	public synchronized static long getDateTimeInLong(String Dateformat) {
		// String dateformat="MM/dd/yyyy HH:mm:ss ";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat(Dateformat);
		String todaysDate = null;
		todaysDate = date.format(cal.getTime());
		return Long.parseLong(todaysDate);
	}

	public synchronized static int getDateDiffn(String startDate, String endDate) {

		return 1;
	}
	
	public static void main(String args[]){
	
			GetReqdDateFormat getReqdDateFormat= new GetReqdDateFormat();
			for (int i = 0; i < 10; i++) {
				
			System.out.println(getReqdDateFormat.genrateTransactionID());
			}
	}

}
