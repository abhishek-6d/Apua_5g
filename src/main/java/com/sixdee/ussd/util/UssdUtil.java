/**
 * 
 */
package com.sixdee.ussd.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;

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
public class UssdUtil {
	
	Logger logger = Logger.getLogger(UssdUtil.class);

	public String removalOfCountryCode(String subsNumber) throws CommonException {
		int numSeriesLength = 8;
		String countryCode = "";
		HashMap<String, String> configMap = (HashMap<String, String>) AppCache.util.get("CONFIG_DETAILS");
		try{
			if(configMap != null){
				numSeriesLength = Integer.parseInt(configMap.get("SUBSCRIBER_NUMBER_LENGTH"));
				countryCode = configMap.get("SUBSCRIBER_COUNTRY_CODE");
			}
			
			if (subsNumber.length() == (numSeriesLength+countryCode.length())
						&& subsNumber.startsWith(countryCode)) {
					subsNumber = subsNumber.substring(countryCode.trim()
							.length());

					}
			logger.info(subsNumber);
		}catch (Exception e) {

			logger.error("Exception occured ",e);
			throw new CommonException("Unknown Error");
		
		}
		return subsNumber;
	}
	
	
	public String appendCountryCode(String subsNumber) throws CommonException {
		int numSeriesLength = 8;
		String countryCode = "";
		HashMap<String, String> configMap = (HashMap<String, String>) AppCache.util.get("CONFIG_DETAILS");
		try{
			if(configMap != null){
				numSeriesLength = Integer.parseInt(configMap.get("SUBSCRIBER_NUMBER_LENGTH"));
				countryCode = configMap.get("SUBSCRIBER_COUNTRY_CODE");
			}
					if (subsNumber.length() == numSeriesLength) {
					subsNumber = countryCode+subsNumber;
				

			}
		}catch (Exception e) {

			logger.error("Exception occured ",e);
			throw new CommonException("Unknown Error");
		
		}
		return subsNumber;
	}

}
