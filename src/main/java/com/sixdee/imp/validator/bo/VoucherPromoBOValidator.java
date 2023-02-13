package com.sixdee.imp.validator.bo;

/**
 * 
 * @author @jith
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
 * <td>June 25,2015 11:18:20 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.bo.BOValidatorCommon;

public class VoucherPromoBOValidator extends BOValidatorCommon {

	/**
	 * This method should be called from Business Objects.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidate(GenericDTO genericDTO) throws CommonException {
		logger.info("Class => VoucherPromoBOValidator :: Method => buildValidate()");
		
		return genericDTO;
	}

}
