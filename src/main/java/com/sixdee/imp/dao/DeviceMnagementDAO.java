package com.sixdee.imp.dao;

/**
 * 
 * @author Somesh
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>September 25,2014 07:06:05 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.imp.dto.DeviceMnagementDTO;

public class DeviceMnagementDAO {

	private Logger log = Logger.getLogger(DeviceMnagementDAO.class);
	public boolean execute(DeviceMnagementDTO deviceMnagementDTO)
	{
		boolean flag = false;
		try
		{
			log.fatal(deviceMnagementDTO);  //Writing CDR
			flag = true;
		}
		catch (Exception e) {
			log.error("Exception",e);
		}
		
		return flag;
	}
}
