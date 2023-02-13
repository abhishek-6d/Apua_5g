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
 * <td>April 25,2013 06:34:23 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.DeActivatePackDAO;
import com.sixdee.imp.dto.SubscriberTransactionHistoryDTO;

public class DeActivatePackBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => DeActivatePackBL :: Method => buildProcess()");
		
		DeActivatePackDAO  deActivatePackDAO;
		SubscriberTransactionHistoryDTO dto = null;
		try
		{
			deActivatePackDAO = new DeActivatePackDAO();
			dto = (SubscriberTransactionHistoryDTO)genericDTO.getObj();
			
			//Here we have to call MORouter to deactivate pack XML over HTTP
			
			if(deActivatePackDAO.insertTransactionHistory(dto))
			{
				logger.info("Success full ............");
				genericDTO.setStatus("Success");
				genericDTO.setStatusCode("SC0000");
			}
			else
			{
				logger.info("Failure .. .. .. ");
				genericDTO.setStatus("Failure ..");
				genericDTO.setStatusCode("SC0001");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		
					
		return genericDTO;
	}
}
