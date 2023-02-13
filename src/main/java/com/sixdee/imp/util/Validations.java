/**
 * 
 */
package com.sixdee.imp.util;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;


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
 * <td>Class used for validation purpose</td>
 * </tr>
 * </table>
 * </p>
 */
public class Validations {
	
	private static final Logger logger = Logger.getLogger(Validations.class); 
	private int numSeriesLength = 8;
	String nationalId = "";
	
	public Validations() {
		numSeriesLength = Integer.parseInt(Cache.cacheMap.get("MSISDN_LENGTH")!=null?Cache.cacheMap.get("MSISDN_LENGTH"):"8");
		nationalId = Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
	}
	public boolean validateSubsNumber(String subsumber){
		boolean flag = true;
		boolean isNumCheckReqd = false;
		try{
		//	isNumCheckReqd = Boolean.parseBoolean(Cache.cacheMap.get("GET_TRANSACTION_ON_NUMBER")!=null ? Cache.cacheMap.get("GET_TRANSACTION_ON_NUMBER"):"true");
		
				if(checkIfNumber(subsumber)){
					if(subsumber.length()==numSeriesLength){
						flag=true;
					}else if(subsumber.length()>numSeriesLength && nationalId != null){
						if(subsumber.startsWith(nationalId) && subsumber.substring(nationalId.length(), subsumber.length()).length()==numSeriesLength){
							flag = true;
						}
					}else{
						flag = false;
					}
				}
			
		}catch (Exception e) {
			logger.error("Exception ",e);
			flag = false;
		}
		
		return true ;
	}

	private boolean checkIfNumber(String subsumber) throws Exception {
		try{
			Double.parseDouble(subsumber);
		}catch (Exception e) {
			throw e;
			//return false;
			
		}
		return true;
	}
	public boolean validateTimeStamp(String timeStamp,String timeFormat) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		try{
			sdf.parse(timeStamp);
			flag = true;
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			flag = false;
		}
		return flag;
	}
	public boolean isNumber(String subscriberNumber) {
		boolean isNumber = true;
		try{
			Long.parseLong(subscriberNumber);
		}catch (NumberFormatException e) {
			logger.info("Adsl Number recieved "+subscriberNumber);
			isNumber = false;
		}
		return isNumber;
	}
	
	
}
