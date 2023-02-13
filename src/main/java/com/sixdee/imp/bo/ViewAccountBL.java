package com.sixdee.imp.bo;

/**
 * 
 * @author Paramesh
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
 * <td>April 22,2013 11:53:25 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.ViewAccountDAO;
import com.sixdee.imp.dto.ViewAccountDTO;

public class ViewAccountBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO){

		logger.info("Class => ViewAccountBL :: Method => buildProcess()");
		
			ViewAccountDAO  viewAccountDAO=new ViewAccountDAO();
			ViewAccountDTO viewAccountDTO = (ViewAccountDTO) genericDTO.getObj();
	
			logger.info(viewAccountDTO.getMoNumber());
			
			if(viewAccountDTO.isView())
			{
				viewAccountDAO.getAllAccountDetails(genericDTO);
			}
			
						
			return genericDTO;
	}
}
