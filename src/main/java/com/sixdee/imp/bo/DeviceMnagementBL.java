package com.sixdee.imp.bo;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.DeviceMnagementDAO;
import com.sixdee.imp.dto.DeviceMnagementDTO;

public class DeviceMnagementBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {
		DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");

		logger.info("Class => DeviceMnagementBL :: Method => buildProcess()");
		
		DeviceMnagementDTO deviceMnagementDTO = (DeviceMnagementDTO) genericDTO.getObj();

		DeviceMnagementDAO dao = new DeviceMnagementDAO();
		deviceMnagementDTO.setCreateDate(df.format(new Date()));
		boolean flag = dao.execute(deviceMnagementDTO);
		
		if(flag)
		{
			genericDTO.setStatusCode("SC0000");
			genericDTO.setStatus("SUCCESS");
		}
		else
		{
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus("Failure !!!");

		}
		
					
		return genericDTO;
		}
}
