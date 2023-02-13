package com.sixdee.imp.validator.format;
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
 * <td>APR 18, 2013</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */
import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.dto.UserprofileDTO;

public class DeleteAccountFrmtValidator extends FrmtValidatorGUICommon 
{

	Logger logger = Logger.getLogger(DeleteAccountFrmtValidator.class);
	@Override
	public GenericDTO buildValidateGUIReq(GenericDTO genericDTO)throws CommonException {
		

		logger.debug("Method =>buildValidateGUIReq()");
		DeleteAccountDTO deleteAccountDTO = null;
		try{
			deleteAccountDTO = (DeleteAccountDTO) genericDTO.getObj();
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(deleteAccountDTO.getMoNumber()!=null&&deleteAccountDTO.getMoNumber().SIZE==subscriberSize)
				deleteAccountDTO.setMoNumber(Long.parseLong(subscriberCountryCode+""+deleteAccountDTO.getMoNumber()));
		
		}catch (Exception e) {
			throw new CommonException("System Error Please try again");
		}
		return genericDTO;
	
	}
}
