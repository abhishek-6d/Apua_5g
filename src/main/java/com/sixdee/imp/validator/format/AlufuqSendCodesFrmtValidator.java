package com.sixdee.imp.validator.format;
/**
 * 
 * @author Ananth
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
 * <td>April 20,2016 04:41:18 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;

public class AlufuqSendCodesFrmtValidator extends FrmtValidatorCommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */


	@Override
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		return genericDTO;
	}

}
