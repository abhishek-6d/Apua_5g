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
 * <td>May 08,2013 02:27:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.ValidateSubscriberDAO;

public class ValidateSubscriberBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		
		ValidateSubscriberDAO  validateSubscriberDAO=new ValidateSubscriberDAO();
		try{
			
			validateSubscriberDAO.checkSubscriberProfile(genericDTO);
			genericDTO.setStatusCode("0");
			genericDTO.setStatus("SCCUESS");
		}catch (CommonException e) {
			genericDTO.setStatusCode("1");
			genericDTO.setStatus(e.getLocalizedMessage());
		}
		
		return genericDTO;
		
		}
}
