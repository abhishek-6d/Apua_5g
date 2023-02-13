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
 * <td>April 23,2013 09:49:31 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.ActivatePackDAO;
import com.sixdee.imp.dto.SubscriberTransactionHistoryDTO;

public class ActivatePackBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException 
	{

		logger.info("Class => ActivatePAckBL :: Method => buildProcess()");
		
		ActivatePackDAO  activatePackDAO;
		SubscriberTransactionHistoryDTO dto = null;
		try
		{
			activatePackDAO = new ActivatePackDAO();
			dto = (SubscriberTransactionHistoryDTO)genericDTO.getObj();
			
			//Here call MORouter for Activate call XML over HTTP
			
			if(activatePackDAO.insertTransactionHistory(dto))
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
