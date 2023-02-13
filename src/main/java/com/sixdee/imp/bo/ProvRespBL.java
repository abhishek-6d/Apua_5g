package com.sixdee.imp.bo;

/**
 * 
 * @author Rahul K K
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
 * <td>July 21,2013 09:11:35 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.ProvRespDAO;
import com.sixdee.imp.dto.ProvRespDTO;

public class ProvRespBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => ProvRespBL :: Method => buildProcess()");
		
		ProvRespDAO  provRespDAO;
		ProvRespDTO provRespDTO = (ProvRespDTO) genericDTO.getObj();

		
					
			return genericDTO;
		}
}
