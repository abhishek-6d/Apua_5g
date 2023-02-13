/**
 * 
 */
package com.sixdee.imp.util;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
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
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GeneralProcesses {
	
	private static Logger logger = Logger.getLogger(GeneralProcesses.class);
	
	public String identifyTable(String tableName,String subsNumber) throws Exception {
		int tableIdentifier = 0;
		//String tableName = null;
		try{
			tableIdentifier = Integer.parseInt(Cache.cacheMap.get("TABLE_SUFFIX_LENGTH"));
			//tableName = Cache.cacheMap.get("TRANSACTION_TABLE_PREFIX");
			//System.out.println(subsNumber.substring(subsNumber.length()));
			tableName+="_"+(subsNumber.substring(subsNumber.length()-tableIdentifier));
			System.out.println(tableName);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			throw e;
		}
		return tableName;
	}

	public String removalOfCountryCode(String subsNumber) throws CommonException {
		int numSeriesLength = 8;
		String coutryCode = "";
		try{
			if(Cache.cacheMap.get("IS_COUNTRY_CODE_CHK_REQD")!=null && 
					Cache.cacheMap.get("IS_COUNTRY_CODE_CHK_REQD")
							.equalsIgnoreCase("true")) {
				coutryCode = Cache.getConfigParameterMap().get(
						"SUBSCRIBER_COUNTRY_CODE").getParameterValue();
				if (subsNumber.length() >= numSeriesLength
						&& subsNumber.startsWith(coutryCode)) {
					subsNumber = subsNumber.substring(coutryCode.trim()
							.length());
				}

			}
		}catch (Exception e) {

			logger.error("Exception occured ",e);
			throw new CommonException("Unknown Error");
		
		}
		return subsNumber;
	}
	
	
	public String appendCountryCode(String subsNumber) throws CommonException {
		int numSeriesLength = 8;
		String coutryCode = "";
		try {
			if (Cache.cacheMap.get("IS_COUNTRY_CODE_CHK_REQD") != null
					&& Cache.cacheMap.get("IS_COUNTRY_CODE_CHK_REQD")
							.equalsIgnoreCase("true")) {
				coutryCode = Cache.getConfigParameterMap().get(
						"SUBSCRIBER_COUNTRY_CODE").getParameterValue();
				if (subsNumber.length() >= numSeriesLength
						&& subsNumber.startsWith(coutryCode)) {
					subsNumber = coutryCode + subsNumber;
				}

			}
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			throw new CommonException("Subscriber Number invalid");
		}
		return subsNumber;
	}

	/*public static void main(String[] args) {
		System.out.println("231321312321".substring(2));
	}*/
	
}
