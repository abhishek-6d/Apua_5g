package com.sixdee.imp.request;
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
import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.FAQDetailsDTO;

//import com.sixdee.imp.service.serviceDTO.req.UpdateVoucherDTO;

public class FAQDetailsReqAssm extends ReqAssmCommon 
{
	 
	/**
	 * This method is called from the framework class in Request Assembler Layer. Converts request Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	Logger logger=Logger.getLogger(FAQDetailsReqAssm.class);
	

	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {

		FAQDetailsDTO detailsDTO = null;
		try {
			detailsDTO = new FAQDetailsDTO();
			
			
		}catch (Exception e) {
				e.printStackTrace();
		}finally {
			genericDTO.setObj(detailsDTO);
		}
		return genericDTO;
	
	}
	
	
}
