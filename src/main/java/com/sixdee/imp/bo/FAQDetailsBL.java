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
 * <td>APR 15, 2013</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */
import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.FAQDetailsDAO;

public class FAQDetailsBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => UpdateVoucherDetailsBL :: Method => buildProcess()");
		

			 FAQDetailsDAO updateVoucherDetailsDAO=new FAQDetailsDAO();
		    
			 updateVoucherDetailsDAO.GetFAQDetails(genericDTO);
			
			return genericDTO;
		}
	}
			
    

	
