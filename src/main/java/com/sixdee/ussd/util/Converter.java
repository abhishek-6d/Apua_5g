/**
 * 
 */
package com.sixdee.ussd.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.UserDTO;

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
public class Converter {
	Logger logger = Logger.getLogger(Converter.class);
	
	public HashMap<String, String> dtoToMapConverter(UserDTO userDTO){/*
		HashMap<String, String> processMap = null;
		boolean isFirstTimeConversion = true;
		String reqTag = null;
		Field[] fields = userDTO.getClass().getDeclaredFields();
		//HashMap<String, String> reqTagMap = AppCache.reqTagMap;
		for(Field f : fields){
			try {
				reqTag = f.getName();
					Object getMethodValue = PropertyUtils.getSimpleProperty(userDTO, reqTag);
					if(getMethodValue != null  && !f.getType().toString().equalsIgnoreCase("class java.util.HashMap")){
						if(isFirstTimeConversion){
							processMap = new HashMap<String, String>();
							isFirstTimeConversion = false;
						}	
						processMap.put(reqTag, getMethodValue.toString().toUpperCase());
						
					}
				
			} catch (IllegalAccessException e) {
				logger.error("Exception occured",e);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.error("Exception occured",e);
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.error("Exception occured",e);
				e.printStackTrace();
			}
		}
		return processMap;
	*/
		return null;
				}


}
